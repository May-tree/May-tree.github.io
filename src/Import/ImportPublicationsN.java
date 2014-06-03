package Import;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.ResultSet;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.HashMap;
import Functions.CollectionOperator;
import Functions.SemanticTree;

public class ImportPublicationsN {

    public static HashMap<Integer, String> recordList = new HashMap<>();
    public static HashMap<Integer, ArrayList<Integer>> semanticSig = new HashMap<>();
    public static HashMap<Integer, HashSet<Integer>> originCluster = new HashMap<>();
    public static HashSet<Integer> idset = new HashSet<>();

    
    public static HashSet<String> rules(ResultSet rs) throws SQLException{
        HashSet<String> cate_Set=new HashSet<>();
        String tech=rs.getString("tech");
        String type=rs.getString("type");
        String note=rs.getString("note");
        String journal=rs.getString("journal");
        String institution=rs.getString("institution");
        String booktitle=rs.getString("booktitle");
        
        //Robust Rules
//        if(booktitle!=null&&(booktitle.contains("Proceedings")||booktitle.contains("Conference"))){
//            cate_Set.add("CON");
//            return cate_Set;
//        }
//        if(type!=null && type.contains("thesis")){   
//            cate_Set.add("TH");
//            return cate_Set;
//        }
//        if(type!=null && type.contains("Technical")){
//            cate_Set.add("RP");
//            return cate_Set;
//        }
//        if(note!=null && note.contains("ISBN")){
//            cate_Set.add("BK");
//            return cate_Set;
//        }
        
        if(journal!=null){
            if(institution!=null){
                if(booktitle!=null){
                    cate_Set.add("NPR");
                    cate_Set.add("JN");
                    cate_Set.add("CON");
                    return cate_Set;
                }
                else{
                    cate_Set.add("NPR");
                    cate_Set.add("JN");
                    return cate_Set;
                }
                
            }
            else{
                if(booktitle!=null){
                    cate_Set.add("JN");
                    cate_Set.add("CON");
                    return cate_Set;  
                }
                else{
                    cate_Set.add("JN");
                    return cate_Set;
                }
            }
        }
        else{
//            if(tech!=null){
//                if(booktitle!=null){
//                    cate_Set.add("RP");
//                    cate_Set.add("CON");
//                    return cate_Set;
//                }
//                else{
//                    cate_Set.add("RP");
//                    return cate_Set;
//                }
//            }
//            else{
//                if(booktitle!=null){
//                    if(institution!=null){
//                        cate_Set.add("CON");
//                        cate_Set.add("NPR");
//                        return cate_Set;
//                    }
//                    else{
//                        cate_Set.add("CON");
//                        return cate_Set;
//                    } 
//                }
//                else{
//                    if(institution!=null){
//                        cate_Set.add("NPR");
//                        return cate_Set;
//                    }
//                    else{
//                        cate_Set.add("PUB");
//                        return cate_Set;
//                    }
//                }
//            }
            if(institution!=null){
                if(booktitle!=null){
                    cate_Set.add("NPR");
                    cate_Set.add("CON");
                    return cate_Set;
                }
                else{
                    cate_Set.add("NPR");
                    return cate_Set;
                }
                
            }
            else{
                if(booktitle!=null){
                    cate_Set.add("CON");
                    return cate_Set;  
                }
                else{
                    cate_Set.add("PUB");
                    return cate_Set;
                }
            }
        }
    }
    
    public static HashSet<Integer> testAtt(ResultSet rs, double percent) throws SQLException {
        int columnCount = rs.getMetaData().getColumnCount();
        int[] testAtt = new int[columnCount];
        int rowCount = 0;
        HashSet<Integer> passAtt = new HashSet<>();
        while (rs.next()) {
            for (int i = 1; i < columnCount + 1; i++) {
                if (rs.getString(i) == null) {
                    testAtt[i]++;
                }
            }
            rowCount++;
        }
        for (int i = 0; i < columnCount; i++) {
            if ((double) testAtt[i] / (double) rowCount > percent) {
                passAtt.add(i + 1);
            }
        }
        rs.close();
        return passAtt;
    }

    public static void publicationsDbImport() throws ClassNotFoundException, SQLException {
        Class.forName("org.postgresql.Driver");
        Connection c = DriverManager.getConnection("jdbc:postgresql://localhost:5432/postgres", "postgres", "qqkfq0513");
        Statement stmt = c.createStatement();
        String sql = "select * from publications";
//        HashSet<Integer> passAtt=testAtt(testrs,0.3);
        ResultSet rs = stmt.executeQuery(sql);
        SemanticTree.buildTree();
        
        while (rs.next()) { 
            StringBuilder longString = new StringBuilder();
            int id = Integer.parseInt(rs.getString("id"));
            idset.add(id);
            ArrayList<Integer> semanticSig1=SemanticTree.getSemanticSig(rules(rs));
            semanticSig.put(id, semanticSig1);
            int cluster = rs.getInt("clusterid");
//        	for(int att:passAtt){
//        		longString.append(rs.getString(att));
//        	}
            String authors=rs.getString("authors");
            String title = rs.getString("title");
            longString.append(authors);
            longString.append(title);
            if (originCluster.containsKey(cluster)) {
                HashSet<Integer> tempSet = originCluster.get(cluster);
                tempSet.add(id);
                originCluster.put(cluster, tempSet);
            } else {
                HashSet<Integer> tempSet = new HashSet<>();
                tempSet.add(id);
                originCluster.put(cluster, tempSet);
            }
            recordList.put(id, longString.toString());
        }
    }

    public static double[] simiDistribute(int gramFactor) {
        HashSet<HashSet<Integer>> originGlobalPair=CollectionOperator.pairGraph(originCluster);
        int size = originGlobalPair.size();
        int[] pernum = new int[11];
        for (HashSet<Integer> pair : originGlobalPair) {
            ArrayList<Integer> id = new ArrayList<>();
            for (int element : pair) {
                id.add(element);
            }
            double simi = CollectionOperator.SetSimi(recordList.get(id.get(0)), recordList.get(id.get(1)), gramFactor);
            if (simi == 0.0) {
                pernum[0]++;
            }
            if (simi == 0.1) {
                pernum[1]++;
            }
            if (simi == 0.2) {
                pernum[2]++;
            }
            if (simi == 0.3) {
                pernum[3]++;
            }
            if (simi == 0.4) {
                pernum[4]++;
            }
            if (simi == 0.5) {
                pernum[5]++;
            }
            if (simi == 0.6) {
                pernum[6]++;
            }
            if (simi == 0.7) {
                pernum[7]++;
            }
            if (simi == 0.8) {
                pernum[8]++;
            }
            if (simi == 0.9) {
                pernum[9]++;
            }
            if (simi == 1) {
                pernum[10]++;
            }
        }
        double[] finaldis = new double[11];
        DecimalFormat df = new DecimalFormat("#.####");
        for (int i = 0; i < 11; i++) {
            finaldis[i] = Double.parseDouble(df.format((double) (pernum[i]) / (double) (size)));
        }
        return finaldis;
    }

    public static double[] rawSimiDistribute(int gramFactor) {
        HashSet<HashSet<Integer>> originRawPair = CollectionOperator.binarysubset(idset);
        int size = originRawPair.size();
        int[] pernum = new int[11];
        for (HashSet<Integer> pair : originRawPair) {
            ArrayList<Integer> id = new ArrayList<>();
            for (int element : pair) {
                id.add(element);
            }
            double simi = CollectionOperator.SetSimi(recordList.get(id.get(0)), recordList.get(id.get(1)), gramFactor);
            if (simi == 0) {
                pernum[0]++;
            }
            if (simi == 0.1) {
                pernum[1]++;
            }
            if (simi == 0.2) {
                pernum[2]++;
            }
            if (simi == 0.3) {
                pernum[3]++;
            }
            if (simi == 0.4) {
                pernum[4]++;
            }
            if (simi == 0.5) {
                pernum[5]++;
            }
            if (simi == 0.6) {
                pernum[6]++;
            }
            if (simi == 0.7) {
                pernum[7]++;
            }
            if (simi == 0.8) {
                pernum[8]++;
            }
            if (simi == 0.9) {
                pernum[9]++;
            }
            if (simi == 1) {
                pernum[10]++;
            }
        }
        double[] finaldis = new double[11];
        DecimalFormat df = new DecimalFormat("#.####");
        for (int i = 0; i < 11; i++) {
            finaldis[i] = Double.parseDouble(df.format((double) (pernum[i]) / (double) (size)));
        }
        return finaldis;
    }

    public static double[] notSimiDistribute(int gramFactor) {
        HashSet<HashSet<Integer>> originRawPair = CollectionOperator.binarysubset(idset);
        HashSet<HashSet<Integer>> originSimiPair = CollectionOperator.pairGraph(originCluster);
        HashSet<HashSet<Integer>> originNotSimiPair = originRawPair;
        originNotSimiPair.removeAll(originSimiPair);
        int size = originNotSimiPair.size();
        int[] pernum = new int[11];
        for (HashSet<Integer> pair : originNotSimiPair) {
            ArrayList<Integer> id = new ArrayList<>();
            for (int element : pair) {
                id.add(element);
            }
            double simi = CollectionOperator.SetSimi(recordList.get(id.get(0)), recordList.get(id.get(1)), gramFactor);
            if (simi == 0) {
                pernum[0]++;
            }
            if (simi == 0.1) {
                pernum[1]++;
            }
            if (simi == 0.2) {
                pernum[2]++;
            }
            if (simi == 0.3) {
                pernum[3]++;
            }
            if (simi == 0.4) {
                pernum[4]++;
            }
            if (simi == 0.5) {
                pernum[5]++;
            }
            if (simi == 0.6) {
                pernum[6]++;
            }
            if (simi == 0.7) {
                pernum[7]++;
            }
            if (simi == 0.8) {
                pernum[8]++;
            }
            if (simi == 0.9) {
                pernum[9]++;
            }
            if (simi == 1) {
                pernum[10]++;
            }
        }
        double[] finaldis = new double[11];
        DecimalFormat df = new DecimalFormat("#.####");
        for (int i = 0; i < 11; i++) {
            finaldis[i] = Double.parseDouble(df.format((double) (pernum[i]) / (double) (size)));
        }
        return finaldis;
    }

    public static void importWithOthers() throws ClassNotFoundException, SQLException {
        Class.forName("org.postgresql.Driver");
        Connection c = DriverManager.getConnection("jdbc:postgresql://localhost:5432/postgres", "postgres", "qqkfq0513");
        Statement stmt = c.createStatement();
        String sql = "select * from publications";
        ResultSet rs = stmt.executeQuery(sql);
        while (rs.next()) {
            int cluster = rs.getInt("clusterid");
            int id = Integer.parseInt(rs.getString("id"));
            idset.add(id);
            if (originCluster.containsKey(cluster)) {
                HashSet<Integer> tempSet = originCluster.get(cluster);
                tempSet.add(id);
                originCluster.put(cluster, tempSet);
            } else {
                HashSet<Integer> tempSet = new HashSet<>();
                tempSet.add(id);
                originCluster.put(cluster, tempSet);
            }
        }
    }

    public static void main(String[] args) {
        int gramFactor = 4;
        double[] rawSimiDist = rawSimiDistribute(gramFactor);
        for (int i = 0; i < rawSimiDist.length; i++) {
            System.out.println(rawSimiDist[i]);
        }
        double[] SimiDist = simiDistribute(gramFactor);
        for (int i = 0; i < SimiDist.length; i++) {
            System.out.println(SimiDist[i]);
        }

    }
}
