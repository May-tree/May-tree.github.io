package Import;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.HashMap;
import Functions.CollectionOperator;

public class ImportVenues {

    public static HashMap<Integer, HashSet<Integer>> venuesPCluster = new HashMap<>();
    public static HashMap<Integer, HashSet<Integer>> originCluster = new HashMap<>();
    public static HashMap<Integer, String> recordList = new HashMap<>();
    public static HashMap<Integer, Integer> venueIdPubId = new HashMap<>();
    public static Connection c = null;
    public static Statement stmt = null;
    public static String sql;
    public static double threshold = 0;
    public static ArrayList<Integer> idset = new ArrayList<>();

    public static void venuesImport() throws ClassNotFoundException, SQLException {
        Class.forName("org.postgresql.Driver");
        c = DriverManager.getConnection("jdbc:postgresql://localhost:5432/postgres", "postgres", "qqkfq0513");
        stmt = c.createStatement();
        sql = "select * from venues";
        ResultSet rs = stmt.executeQuery(sql);
        while (rs.next()) {
            String venue = rs.getString("venue");
            int id = rs.getInt("id");
            idset.add(id);
            int pid = rs.getInt("pid");
            venueIdPubId.put(id, pid);
            int cluster = rs.getInt("cluster");
            if (originCluster.containsKey(cluster)) {
                HashSet<Integer> tempSet = originCluster.get(cluster);
                tempSet.add(id);
                originCluster.put(cluster, tempSet);
            } else {
                HashSet<Integer> tempSet = new HashSet<>();
                tempSet.add(id);
                originCluster.put(cluster, tempSet);
            }
            recordList.put(id, venue);
        }
    }

//	public static HashSet<HashSet<Integer>> computedPairs(int gramFactor,HashSet<HashSet<Integer>> originRawPair){	
//		HashSet<HashSet<Integer>> computedPairs=new HashSet<HashSet<Integer>>();
//		for(HashSet<Integer> pair:originRawPair){
//			ArrayList<Integer> id=new ArrayList<Integer>();
//			for(int element:pair){
//				id.add(element);
//			}
//			double simi=CollectionOperator.SetSimi(recordList.get(id.get(0)), recordList.get(id.get(1)), gramFactor);
//			if(simi>=threshold){
//				computedPairs.add(pair);
//			}
//		}
//		return computedPairs;
//		
//	}
    public static HashSet<HashSet<Integer>> computedPairs(int gramFactor) {
        HashSet<HashSet<Integer>> computedPairs = new HashSet<>();
        for (int i = 0; i < idset.size() - 1; i++) {
            for (int j = i + 1; j < idset.size(); j++) {
                double simi = CollectionOperator.SetSimi(recordList.get(idset.get(i)), recordList.get(idset.get(j)), gramFactor);
                if (simi >= threshold) {
                    HashSet<Integer> temp = new HashSet<>();
                    temp.add(idset.get(i));
                    temp.add(idset.get(j));
                    computedPairs.add(temp);
                }
            }
        }
        return computedPairs;
    }

    public static void main(String args[]) throws ClassNotFoundException, SQLException {
        venuesImport();
        threshold = 0.4;
        int gramFactor = 4;
        HashSet<HashSet<Integer>> groundTruthPairs = CollectionOperator.pairGraph(originCluster);
        HashSet<HashSet<Integer>> computedPairs = computedPairs(gramFactor);
        double[] preRcFo = CollectionOperator.preRcFo(groundTruthPairs, computedPairs);
        System.out.println(preRcFo[0] + " " + preRcFo[1] + " " + preRcFo[2]);
//		ArrayList<HashSet<Integer>> pureClusters=CollectionOperator.transiveRule(computedPairs);
        HashSet<HashSet<Integer>> computedPubPairs = new HashSet<>();
        for (HashSet<Integer> pair : computedPairs) {
            HashSet<Integer> temp = new HashSet<>();
            for (int a : pair) {
                int pa = venueIdPubId.get(a);
                temp.add(pa);
            }
            computedPubPairs.add(temp);
        }
        int size1 = computedPubPairs.size();
        ImportPublications.importWithOthers();
        HashSet<HashSet<Integer>> globalPairs2 = CollectionOperator.pairGraph(ImportPublications.originCluster);
        globalPairs2.retainAll(computedPubPairs);
        System.out.println(size1);
        int size2 = globalPairs2.size();
        System.out.println(+size2);
        HashSet<HashSet<Integer>> allPubPairs = CollectionOperator.binarysubset(ImportPublications.idset);
        int size3 = allPubPairs.size() - size1;
        System.out.println(size3);
        allPubPairs.removeAll(computedPubPairs);
        allPubPairs.retainAll(CollectionOperator.pairGraph(ImportPublications.originCluster));
        int size4 = allPubPairs.size();
        System.out.println(size4);
//		HashSet<HashSet<Integer>> globalPairs1=CollectionOperator.pairGraph(venuesPCluster);
//		System.out.println(globalPairs1.size());
//		ImportPublications.importWithOthers();
//		HashSet<HashSet<Integer>> globalPairs2=CollectionOperator.pairGraph(ImportPublications.originCluster);
//		globalPairs2.retainAll(globalPairs1);
//		System.out.println(globalPairs2.size());
    }

}
