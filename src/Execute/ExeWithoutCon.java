package Execute;

import Functions.CollectionOperator;
import Functions.LSH;
import Functions.MinHash;
import Functions.Statistics;
import Import.ImportPublications;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.HashMap;
import java.util.List;

public class ExeWithoutCon {

    public static void main(String args[]) throws ClassNotFoundException, SQLException {
        ImportPublications.publicationsDbImport();
        HashMap<Integer, HashSet<Integer>> originCluster = ImportPublications.originCluster;
        HashSet<HashSet<Integer>> originGlobalPair = CollectionOperator.pairGraph(originCluster);
        int size=ImportPublications.idset.size();
        int numOfPairs=size*(size-1)/2;
        int Nd = originGlobalPair.size();
        int Np;
        int Nb;
        int Nbd;
        int k = 6;
        double s = 0.3;
        double p = 0.4;
        int gramFactor = 0;
//        double[] hey=ImportPublications.simiDistribute(gramFactor);
//        for(int i=0;i<hey.length;i++){
//            System.out.println(hey[i]);
//        }
        int l = (int) Math.ceil(Statistics.computeL(k, s, p));
        HashMap<Integer, String> recordList = ImportPublications.recordList;
        MinHash mHasher = new MinHash(gramFactor, k * l);
        LSH LSHer = new LSH(k, l);
        System.out.println("Data Input Done.");
        System.out.println();

        HashMap<Integer, ArrayList<Integer>> sigList = new HashMap<>();
        for (int key : recordList.keySet()) {
            sigList.put(key, mHasher.sig(recordList.get(key)));
        }
        System.out.println("minHash Done.");
        System.out.println();

        System.out.println("The number of HashFunctions:" + LSHer.l * LSHer.k);
        System.out.println("The number of Bands:" + LSHer.l);
        System.out.println();

        ArrayList<HashMap<List<Integer>, HashSet<Integer>>> LSHBuckets = LSHer.lshBucket(sigList);
        System.out.println("LSH Done.");
        System.out.println();

        HashSet<HashSet<Integer>> rawBlockSets = LSH.rawBlockSets(LSHBuckets);
        HashSet<HashSet<Integer>> pureBlockSets = CollectionOperator.removeSubSet(rawBlockSets);
//        HashMap<Integer, Integer> blockdis = LSH.blockdistribute(pureBlockSets);
        HashSet<HashSet<Integer>> binaryBlockSets = LSH.binaryBlockSets(pureBlockSets);
        double[] blocksimi=LSH.blocksimi(binaryBlockSets, recordList);
        for (int i=0;i<blocksimi.length;i++){
            System.out.println(blocksimi[i]);
        }
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
//		for(int key:blockdis.keySet()){
//			System.out.println(key);
//		}
//		for(int key:blockdis.keySet()){
//			System.out.println(blockdis.get(key));
//		}
        System.out.println();
        System.out.println("All Done.");
    }
}
