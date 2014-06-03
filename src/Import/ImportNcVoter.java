package Import;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.HashMap;
import Functions.SemanticTree;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;

public class ImportNcVoter {

    public static HashMap<Integer, String> recordList = new HashMap<>();
    public static HashMap<Integer, ArrayList<Integer>> semanticSig = new HashMap<>();
    public static HashMap<Integer, HashSet<Integer>> originCluster = new HashMap<>();
    public static HashSet<Integer> idset = new HashSet<>();
    public static void NcVoterImport() throws ClassNotFoundException, FileNotFoundException, IOException {
        SemanticTree.buildTree();
        File csv = new File("D:\\File\\Dropbox\\Project\\onetoone.csv");
        BufferedReader br = new BufferedReader(new FileReader(csv));
        for(int i=0;i<10000;i++){
            String[] line=br.readLine().split(",");
            StringBuilder longString = new StringBuilder();
            longString.append(line[3]);
            longString.append(line[5]);
            longString.append(line[12]);
            longString.append(line[14]);
            int id=Integer.valueOf(line[0]);
            HashSet<String> Race=new HashSet<>();
            Race.add(line[9]);
            ArrayList<Integer> semanticSig1=SemanticTree.getSemanticSig(Race);
            semanticSig.put(id, semanticSig1);
            recordList.put(id,longString.toString());
            idset.add(id);
        }
    }
}
