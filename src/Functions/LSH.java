package Functions;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Random;

public class LSH {

    public int l;
    public int k;
    public int n;
    public static int Nb;
    public static int Nbd;
    public static int Np;
    public static int maxLSH = Integer.MAX_VALUE;

    public LSH(int k2, int l2) {
        k = k2;
        l = l2;
    }

    public LSH(int k2, int l2, int n2) {
        k = k2;
        l = l2;
        n = n2;
    }

    public ArrayList<HashMap<List<Integer>, HashSet<Integer>>> lshBucket(HashMap<Integer, ArrayList<Integer>> sigList) {
        ArrayList<HashMap<List<Integer>, HashSet<Integer>>> lshBuckets = new ArrayList<>();
        for (int i = 0; i < l; i++) {
            lshBuckets.add(new HashMap<List<Integer>, HashSet<Integer>>());
            for (int key : sigList.keySet()) {
                HashSet<Integer> sigset=new HashSet<>(sigList.get(key));
                
                if (lshBuckets.get(i).containsKey(sigList.get(key).subList(i * k, (i + 1) * k))) {
                    HashSet<Integer> temp = lshBuckets.get(i).get(sigList.get(key).subList(i * k, (i + 1) * k));
                    temp.add(key);
                    lshBuckets.get(i).put(sigList.get(key).subList(i * k, (i + 1) * k), temp);
                } else {
                    HashSet<Integer> temp = new HashSet<>();
                    temp.add(key);
                    lshBuckets.get(i).put(sigList.get(key).subList(i * k, (i + 1) * k), temp);
                }
            }
        }

//		System.out.println(set.size());
//		for(int i=0;i<set.size();i++){
//			System.out.println("bucket"+(i+1)+":"+set.get(i));
//		}
        return lshBuckets;
    }

    public ArrayList<HashMap<List<Integer>, HashSet<Integer>>> lshBucketN(HashMap<Integer, ArrayList<Integer>> sigList, HashMap<Integer, ArrayList<Integer>> semanList) {
        ArrayList<HashMap<List<Integer>, HashSet<Integer>>> lshBuckets = lshBucket(sigList);
        for (HashMap<List<Integer>, HashSet<Integer>> lshBucket : lshBuckets) {
            Random rand = new Random();
            ArrayList<Integer> randList = new ArrayList<>();
            for (int i = 0; i < n; i++) {
                randList.add(rand.nextInt(5));
            }
            for (List<Integer> sigs : lshBucket.keySet()) {

                HashSet<Integer> block = lshBucket.get(sigs);
                Iterator<Integer> iter = block.iterator();
                while (iter.hasNext()) {
                    int record = iter.next();
                    for (int j = 0; j < n; j++) {
                        if (semanList.get(record).get(randList.get(j)) == 0) {
                            iter.remove();
                            break;
                        }
                    }
                }
                lshBucket.put(sigs, block);
            }
        }

        return lshBuckets;
    }

    public ArrayList<HashMap<List<Integer>, HashSet<Integer>>> lshBucketO(HashMap<Integer, ArrayList<Integer>> sigList, HashMap<Integer, ArrayList<Integer>> semanList) {
        ArrayList<HashMap<List<Integer>, HashSet<Integer>>> lshBuckets = lshBucket(sigList);
        for (int j = 0; j < lshBuckets.size(); j++) {
            Random rand = new Random();
            ArrayList<Integer> randList = new ArrayList<>();
            for (int i = 0; i < n; i++) {
                randList.add(rand.nextInt(5));
            }
            HashMap<List<Integer>, HashSet<Integer>> newlshBucket = new HashMap<>();
            for (List<Integer> sigs : lshBuckets.get(j).keySet()) {
                List<Integer> temp = CollectionOperator.deepCopy(sigs);
                HashSet<Integer> block = lshBuckets.get(j).get(sigs);
                ArrayList<HashSet<Integer>> setList = new ArrayList<>();
                for (int i = 0; i < n; i++) {
                    setList.add(new HashSet<Integer>());
                }
                for (int record : block) {
                    for (int i = 0; i < n; i++) {
                        if (semanList.get(record).get(randList.get(i)) == 1) {
                            setList.get(i).add(record);
                        }
                    }
                }
                HashSet<HashSet<Integer>> setSet = new HashSet<>(setList);
                ArrayList<HashSet<Integer>> pureSetList = new ArrayList<>();
                for (HashSet<Integer> set : setSet) {
                    if (!set.isEmpty()) {
                        pureSetList.add(set);
                    }
                }
                for (int i = 0; i < pureSetList.size(); i++) {

                    temp.add(i);
                    newlshBucket.put(CollectionOperator.deepCopy(temp), pureSetList.get(i));
                    temp.remove(k);
                }
            }
            lshBuckets.set(j, newlshBucket);
//            for(List<Integer> key:lshBuckets.get(j).keySet()){
//                System.out.println(lshBuckets.get(j).get(key));
//            }
        }

        return lshBuckets;
    }

    public ArrayList<HashMap<List<Integer>, HashSet<Integer>>> lshBucketA(HashMap<Integer, ArrayList<Integer>> sigList, HashMap<Integer, ArrayList<Integer>> semanList) {
        ArrayList<HashMap<List<Integer>, HashSet<Integer>>> lshBuckets = lshBucket(sigList);
        for (int j = 0; j < lshBuckets.size(); j++) {
            HashMap<List<Integer>, HashSet<Integer>> newlshBucket = new HashMap<>();
            for (List<Integer> sigs : lshBuckets.get(j).keySet()) {
                List<Integer> temp = CollectionOperator.deepCopy(sigs);
                HashSet<Integer> block = lshBuckets.get(j).get(sigs);
                ArrayList<HashSet<Integer>> setList = new ArrayList<>();
                for (int i = 0; i < 7; i++) {
                    setList.add(new HashSet<Integer>());
                }
                for (int record : block) {
                    for (int i = 0; i < 7; i++) {
                        if (semanList.get(record).get(i) == 1) {
                            setList.get(i).add(record);
                        }
                    }
                }
                HashSet<HashSet<Integer>> setSet = new HashSet<>(setList);
                ArrayList<HashSet<Integer>> pureSetList = new ArrayList<>();
                for (HashSet<Integer> set : setSet) {
                    if (!set.isEmpty()) {
                        pureSetList.add(set);
                    }
                }
                for (int i = 0; i < pureSetList.size(); i++) {

                    temp.add(i);
                    newlshBucket.put(CollectionOperator.deepCopy(temp), pureSetList.get(i));
                    temp.remove(k);
                }
            }
            lshBuckets.set(j, newlshBucket);
//            for(List<Integer> key:lshBuckets.get(j).keySet()){
//                System.out.println(lshBuckets.get(j).get(key));
//            }
        }

        return lshBuckets;
    }

    public static HashSet<HashSet<Integer>> rawBlockSets(ArrayList<HashMap<List<Integer>, HashSet<Integer>>> lshBuckets) {
        HashSet<HashSet<Integer>> blockSets = new HashSet<>();
        for (HashMap<List<Integer>, HashSet<Integer>> lshBucket : lshBuckets) {
            for (List<Integer> key : lshBucket.keySet()) {
                blockSets.add(lshBucket.get(key));
            }
        }
        return blockSets;
    }

    public static HashMap<Integer, Integer> blockdistribute(HashSet<HashSet<Integer>> pureBlockSets) {
        HashMap<Integer, Integer> blockdis = new HashMap<>();
        for (HashSet<Integer> block : pureBlockSets) {
            int size = block.size();
            if (blockdis.containsKey(size)) {
                int temp = blockdis.get(size) + 1;
                blockdis.put(size, temp);
            } else {
                blockdis.put(size, 1);
            }
        }
        return blockdis;
    }

    public static void obtainNb(HashSet<HashSet<Integer>> pureBlockSets) {
        int NbTemp = 0;
        for (HashSet<Integer> block : pureBlockSets) {
            NbTemp += block.size() * (block.size() - 1) / 2;
        }
        Nb = NbTemp;
    }

    public static HashSet<HashSet<Integer>> binaryBlockSets(HashSet<HashSet<Integer>> pureBlockSets) {
        HashSet<HashSet<Integer>> blockSets = new HashSet<>();
        for (HashSet<Integer> block : pureBlockSets) {
            if (block.size() > 1) {
                blockSets.addAll(CollectionOperator.binarysubset(block));
            }
        }
        return blockSets;
    }

    public static void obtainNp(HashSet<HashSet<Integer>> binaryBlockSets, HashSet<HashSet<Integer>> originPairSet) {
        HashSet<HashSet<Integer>> candidatePairs = new HashSet<>();
        HashSet<HashSet<Integer>> noncandidatePairs = new HashSet<>();
        for (HashSet<Integer> pair : binaryBlockSets) {
            if (originPairSet.contains(pair)) {
                candidatePairs.add(pair);
            } else {
                noncandidatePairs.add(pair);
            }
        }
//		for(HashSet<Integer> Pair:candidatePairs){
//			ArrayList<Integer> id=new ArrayList<Integer>();
//			for(int element:Pair){
//				id.add(element);
//			}
//			double simi=collectionOperator.SetSimi(importFromDatabase.recordList.get(id.get(0)), importFromDatabase.recordList.get(id.get(1)), 4);
//			if(simi<0.3){
//				System.out.println("opps!"+id+":"+simi);
//			}
//		}
        Np = candidatePairs.size();
        Nbd = noncandidatePairs.size()+Np;
    }
    public static double[] blocksimi(HashSet<HashSet<Integer>> binarySet, HashMap<Integer,String> recordList) {
        int[] pernum = new int[11];
        int size=binarySet.size();
        for (HashSet<Integer> pair : binarySet) {
            ArrayList<Integer> id = new ArrayList<>();
            for (int element : pair) {
                id.add(element);
            }
            double simi = CollectionOperator.SetSimi(recordList.get(id.get(0)), recordList.get(id.get(1)), 4);
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
        DecimalFormat df = new DecimalFormat("#.##");
        for (int i = 0; i < 11; i++) {
            finaldis[i] = Double.parseDouble(df.format((double) (pernum[i]) / (double) (size)));
        }
        return finaldis;

    }

}
