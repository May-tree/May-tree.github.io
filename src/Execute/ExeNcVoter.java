package Execute;

import Functions.CollectionOperator;
import Functions.LSH;
import Functions.MinHash;
import Functions.Statistics;
import Import.ImportNcVoter;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;

public class ExeNcVoter {
	
//	public static void main(String args[]) throws  IOException, ClassNotFoundException{
//		ImportNcVoter.NcVoterImport();
//		HashMap<Integer, String> recordList = ImportNcVoter.recordList;
//		HashMap<Integer, ArrayList<Integer>> semanList = ImportNcVoter.semanticSig;
//		HashSet<HashSet<Integer>> differ=main1();
//		differ.removeAll(main2());
//		for(HashSet<Integer> pair:differ){
//			ArrayList<Integer> list=new ArrayList<>(pair);
//			System.out.println(pair+" "+CollectionOperator.SetSimi(recordList.get(list.get(0)), recordList.get(list.get(1)), 2)+" "+semanList.get(list.get(0))+" "+semanList.get(list.get(1)));
//		}
//		
//		
//		
//	}
    public static void main(String[] args) throws ClassNotFoundException, IOException {      
        ImportNcVoter.NcVoterImport();
        HashMap<Integer, HashSet<Integer>> originCluster = ImportNcVoter.originCluster;
        HashSet<HashSet<Integer>> originGlobalPair = CollectionOperator.pairGraph(originCluster);
        int size = ImportNcVoter.idset.size();
        int numOfPairs = size * (size - 1) / 2;
        int Nd = originGlobalPair.size();
        int Np;
        int Nb;
        int Nbd;
        int k = 3;
        int l=64;
        int n = 12;
        double s = 0.2;
        double p = 0.4;
        int gramFactor = 2;
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

        ArrayList<HashMap<List<Integer>, HashSet<Integer>>> lshBuckets = lsher.lshBucket(sigList);
        System.out.println();
        System.out.println("LSH Done.");
        System.out.println();

        HashSet<HashSet<Integer>> rawBlockSets = LSH.rawBlockSets(lshBuckets);
        HashSet<HashSet<Integer>> pureBlockSets = CollectionOperator.removeSubSet(rawBlockSets);
        HashMap<Integer, Integer> blockdis = LSH.blockdistribute(pureBlockSets);
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
        for(int key:blockdis.keySet()){ 
        	System.out.println(key); 
        } 
        for(int key:blockdis.keySet()){ 
        	System.out.println(blockdis.get(key)); 
      	} 
        System.out.println(PC);
        System.out.println(PP);
        System.out.println(PR);
        System.out.println(RR);
        System.out.println(FM);
        System.out.println();
        System.out.println("All Done.");
    }
    
    
}
