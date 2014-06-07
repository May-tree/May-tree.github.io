package Import;

import Functions.EthnicTree;
import Functions.GenderTree;
import Functions.NaiveMap;
import Functions.RaceTree;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;

public class ImportNcVoter {

    public static HashMap<Integer, String> recordList = new HashMap<>();
    public static HashMap<Integer, ArrayList<Integer>> semanticSig = new HashMap<>();
    public static HashMap<Integer, HashSet<Integer>> originCluster = new HashMap<>();
    public static HashSet<Integer> idset = new HashSet<>();
    public static void NcVoterImport() throws ClassNotFoundException, FileNotFoundException, IOException {
        GenderTree.buildTree();
        RaceTree.buildTree();
        File csv = new File("/students/u5323288/Downloads/onetoonetwo.csv");
        BufferedReader br = new BufferedReader(new FileReader(csv));
        br.readLine();
        for(int i=0;i<10000;i++){
            String[] line=br.readLine().replace("\"", "").split(",");
            StringBuilder longString = new StringBuilder();
            longString.append(line[2]);
            longString.append(" ");
            longString.append(line[3]);
//            longString.append(" ");
//            longString.append(line[7]);
            int cluster=Integer.valueOf(line[1]);
            int id=Integer.valueOf(line[0]);
//            HashSet<String> Gender=new HashSet<>();
//            Gender.add(line[4]);
//            HashSet<String> Race=new HashSet<>();
//            Race.add(line[5]);
            char race=line[5].charAt(0);
            char gender=line[4].charAt(0);
            ArrayList<Integer> semanticSig1=NaiveMap.NMap(race, gender);
//            ArrayList<Integer> semanticSig1=RaceTree.getSemanticSig(Race);
//            ArrayList<Integer> semanticSig2=RaceTree.getSemanticSig(Race);
//            semanticSig1.addAll(semanticSig2);
            semanticSig.put(id, semanticSig1);
            recordList.put(id,longString.toString());
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
}
