package Import;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.ResultSet;
import java.util.HashSet;
import java.util.HashMap;
import Functions.CollectionOperator;

public class ImportAuthors {

    public static HashMap<Integer, HashSet<Integer>> authorCluster = new HashMap<>();
    public static HashMap<HashSet<Integer>, HashSet<Integer>> realCluster = new HashMap<>();
    public static Connection c = null;
    public static Statement stmt = null;
    public static String sql;
    public static HashSet<Integer> idset;

    public static void authorsImport() throws ClassNotFoundException, SQLException {
        Class.forName("org.postgresql.Driver");
        c = DriverManager.getConnection("jdbc:postgresql://localhost:5432/postgres", "postgres", "qqkfq0513");
        stmt = c.createStatement();
        sql = "select * from authors";
        ResultSet rs = stmt.executeQuery(sql);
        while (rs.next()) {
            int cluster = rs.getInt("cluster");
            int pid = rs.getInt("pid");
            if (authorCluster.containsKey(pid)) {
                HashSet<Integer> tempSet = authorCluster.get(pid);
                tempSet.add(cluster);
                authorCluster.put(pid, tempSet);
            } else {
                HashSet<Integer> tempSet = new HashSet<>();
                tempSet.add(cluster);
                authorCluster.put(pid, tempSet);
            }
        }
        for (int pub : authorCluster.keySet()) {
            HashSet<Integer> temp = authorCluster.get(pub);
            if (realCluster.containsKey(temp)) {
                HashSet<Integer> temp2 = realCluster.get(temp);
                temp2.add(pub);
                realCluster.put(temp, temp2);
            } else {
                HashSet<Integer> temp2 = new HashSet<>();
                temp2.add(pub);
                realCluster.put(temp, temp2);
            }
        }

    }

    public static void main(String args[]) throws ClassNotFoundException, SQLException {
        authorsImport();
        HashSet<HashSet<Integer>> globalPairs1 = new HashSet<>();
        for (HashSet<Integer> authors : realCluster.keySet()) {
            if (realCluster.get(authors).size() > 1) {
                globalPairs1.addAll(CollectionOperator.binarysubset(realCluster.get(authors)));
            }
        }
        System.out.println(globalPairs1.size());
        ImportPublications.importWithOthers();
        HashSet<HashSet<Integer>> globalPairs2 = CollectionOperator.pairGraph(ImportPublications.originCluster);
        globalPairs2.retainAll(globalPairs1);
        System.out.println(globalPairs2.size());
    }

}
