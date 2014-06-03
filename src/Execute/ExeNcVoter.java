package Execute;

import Functions.CollectionOperator;
import Functions.LSH;
import Functions.MinHash;
import Import.ImportNcVoter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;

public class ExeNcVoter {

    public static void main(String args[]) throws ClassNotFoundException, IOException {      
        ImportNcVoter.NcVoterImport();
        HashMap<Integer, HashSet<Integer>> originCluster = ImportNcVoter.originCluster;
        HashSet<HashSet<Integer>> originGlobalPair = CollectionOperator.pairGraph(originCluster);
        int size = ImportNcVoter.idset.size();
        int numOfPairs = size * (size - 1) / 2;
        int Nd = originGlobalPair.size();
        int Np;
        int Nb;
        int Nbd;
        int k = 4;
        int l=30;
        int n = 7;
//        double s = 0.3;
//        double p = 0.4;
        int gramFactor = 4;
//        int l = (int) Math.ceil(Statistics.computeL(k, s, p));
        HashMap<Integer, String> recordList = ImportNcVoter.recordList;
        HashMap<Integer, ArrayList<Integer>> semanList = ImportNcVoter.semanticSig;
        MinHash mHasher = new MinHash(gramFactor, k * l);
        LSH lsher = new LSH(k, l, n);
        System.out.println("Data Input Done.");
        System.out.println();

        HashMap<Integer, ArrayList<Integer>> sigList = new HashMap<>();
        for (int key : recordList.keySet()) {
            sigList.put(key, mHasher.sig(recordList.get(key)));
        }
        System.out.println("minHash Done.");
        System.out.println();

        ArrayList<HashMap<List<Integer>, HashSet<Integer>>> lshBuckets = lsher.lshBucketA(sigList, semanList);
        System.out.println();
        System.out.println("LSH Done.");
        System.out.println();

        HashSet<HashSet<Integer>> rawBlockSets = LSH.rawBlockSets(lshBuckets);
        HashSet<HashSet<Integer>> pureBlockSets = CollectionOperator.removeSubSet(rawBlockSets);
//        HashMap<Integer, Integer> blockdis = LSH.blockdistribute(pureBlockSets);
        HashSet<HashSet<Integer>> binaryBlockSets = LSH.binaryBlockSets(pureBlockSets);
        LSH.obtainNb(pureBlockSets);
        LSH.obtainNp(binaryBlockSets, originGlobalPair);
        Np = LSH.Np;
        Nb = LSH.Nb;
        Nbd= LSH.Nbd;
        System.out.println(Np);
        System.out.println(Nbd);
        System.out.println(Nb);
        System.out.println(Nd);
        double PC = (double) Np / (double) Nd;
        double PP = (double) Np / (double) Nbd;
        double PR = 1-(double) Nbd / (double) Nb;
        double RR = 1 - (double) binaryBlockSets.size() / (double) numOfPairs;
        double FM = (double) (2 * RR * PC) / (double) (RR + PC);
        
        System.out.println(PC);
        System.out.println(PP);
        System.out.println(PR);
        System.out.println(RR);
        System.out.println(FM);
        System.out.println();
        System.out.println("All Done.");
    }
    
}
