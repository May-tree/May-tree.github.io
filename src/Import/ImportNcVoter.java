package Import;

import Functions.EthnicTree;
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
        RaceTree.buildTree();
        EthnicTree.buildTree();
        File csv = new File("C:\\Users\\Nicolas\\workspace\\NetBeans\\NicolasTree\\files\\onetoone.csv");
        BufferedReader br = new BufferedReader(new FileReader(csv));
        br.readLine();
        for(int i=0;i<10000;i++){
            String[] line=br.readLine().replace("\"", "").split(",");
            StringBuilder longString = new StringBuilder();
            longString.append(line[3]);
            longString.append(line[5]);
            longString.append(line[12]);
            longString.append(line[14]);
            int cluster=Integer.valueOf(line[0]);
            int id=i+1;
            HashSet<String> Race=new HashSet<>();
            Race.add(line[9]);
            HashSet<String> Ethnic=new HashSet<>();
            Ethnic.add(line[10]);
            ArrayList<Integer> semanticSig1=EthnicTree.getSemanticSig(Ethnic);
            ArrayList<Integer> semanticSig2=RaceTree.getSemanticSig(Race);
            semanticSig1.addAll(semanticSig2);
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
