package Functions;

import Execute.ExportToCsv;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;

public class CollectionOperator {

    public static int maxFrequency(ArrayList<HashSet<Integer>> setofSet) {
        ArrayList<Integer> list = new ArrayList<>();
        for (HashSet<Integer> setofSet1 : setofSet) {
            list.addAll(setofSet1);
        }
        HashSet<Integer> tempset = new HashSet<>(list);
        int max = 0;
        int maxelement = 0;
        for (int element : tempset) {
            if (max < Collections.frequency(list, element)) {
                max = Collections.frequency(list, element);
                maxelement = element;
            }
        }
        return maxelement;
    }

    public static int maxFrequency(ArrayList<HashSet<Integer>> setofSet, HashSet<Integer> Exception) {
        ArrayList<Integer> list = new ArrayList<>();
        for (HashSet<Integer> setofSet1 : setofSet) {
            list.addAll(setofSet1);
        }
        HashSet<Integer> tempset = new HashSet<>(list);
        int max = 0;
        int maxelement = 0;
        tempset.removeAll(Exception);
        for (int element : tempset) {
            if (max < Collections.frequency(list, element)) {
                max = Collections.frequency(list, element);
                maxelement = element;
            }
        }
        return maxelement;
    }

    public static ArrayList<HashSet<Integer>> integrateSet(ArrayList<HashSet<Integer>> setofSet) {
        HashSet<Integer> Exception = new HashSet<>();
        ArrayList<HashSet<Integer>> clusterList = new ArrayList<>();
        while (setofSet.isEmpty() == false) {
            HashSet<Integer> candidate = new HashSet<>();
            ArrayList<HashSet<Integer>> candidateSetList = new ArrayList<>();
            int maxElement = maxFrequency(setofSet, Exception);
            while (maxElement != 0) {
                for (int i = 0; i < setofSet.size(); i++) {
                    if (candidate.contains(i) == false) {
                        if (setofSet.get(i).contains(maxElement)) {
                            candidate.add(i);
                            if (candidateSetList.contains(setofSet.get(i)) == false) {
                                candidateSetList.add(setofSet.get(i));
                            }
                        }
                    }
                }
                Exception.add(maxElement);
                maxElement = maxFrequency(candidateSetList, Exception);
            }
            HashSet<Integer> newcluster = new HashSet<>();
            for (int setindex : candidate) {
                newcluster.addAll(setofSet.get(setindex));
            }
            setofSet.removeAll(candidateSetList);

            clusterList.add(newcluster);

        }
//		for(int i=0;i<clusterList.size();i++){
//			System.out.println("Cluster"+(i+1)+":"+clusterList.get(i));
//		}
        ExportToCsv.export(clusterList);
        return clusterList;
    }

    public static HashSet<HashSet<Integer>> binarysubset(HashSet<Integer> set) {
        ArrayList<Integer> tempList = new ArrayList<>(set);
        HashSet<HashSet<Integer>> setOfSubset = new HashSet<>();
        for (int i = 0; i < tempList.size() - 1; i++) {
            for (int j = i + 1; j < tempList.size(); j++) {
                HashSet<Integer> tempSet = new HashSet<>();
                tempSet.add(tempList.get(i));
                tempSet.add(tempList.get(j));
                setOfSubset.add(tempSet);
            }
        }
        return setOfSubset;
    }

    public static HashSet<HashSet<Integer>> setOfPairs(ArrayList<Integer> elementList) {
        HashSet<HashSet<Integer>> setOfPairs = new HashSet<>();
        for (int i = 0; i < elementList.size() - 1; i++) {
            for (int j = i + 1; j < elementList.size(); j++) {
                HashSet<Integer> tempSet = new HashSet<>();
                tempSet.add(elementList.get(i));
                tempSet.add(elementList.get(j));
                setOfPairs.add(tempSet);
            }
        }
        return setOfPairs;
    }

    public static HashSet<HashSet<Integer>> pairGraph(ArrayList<HashSet<Integer>> clusterList) {
        HashSet<HashSet<Integer>> globalPairGraph = new HashSet<>();
        for (HashSet<Integer> clusterList1 : clusterList) {
            if (clusterList1.size() > 1) {
                globalPairGraph.addAll(CollectionOperator.binarysubset(clusterList1));
            }
        }
        return globalPairGraph;
    }

    public static HashSet<HashSet<Integer>> pairGraph(HashMap<Integer, HashSet<Integer>> originCluster) {
        ArrayList<HashSet<Integer>> clusterList = new ArrayList<>();
        for (int key : originCluster.keySet()) {
            clusterList.add(originCluster.get(key));
        }
        HashSet<HashSet<Integer>> globalPairGraph = new HashSet<>();
        for (HashSet<Integer> clusterList1 : clusterList) {
            if (clusterList1.size() > 1) {
                globalPairGraph.addAll(CollectionOperator.binarysubset(clusterList1));
            }
        }
        return globalPairGraph;
    }

    public static int[] tpfpfn(HashSet<HashSet<Integer>> orig, HashSet<HashSet<Integer>> computed) {
        int tpfpfn[] = new int[3];
        int tp = 0;
        int fp = 0;
        int fn = 0;
        for (HashSet<Integer> set : computed) {
            if (orig.contains(set)) {
                tp++;
            } else {
//				ArrayList<Integer> id=new ArrayList<Integer>();
//				for(int element:set){
//					id.add(element);
//				}
                fp++;
//				System.out.println("!"+set+":"+SetSimi(importFromDatabase.recordList.get(id.get(0)), importFromDatabase.recordList.get(id.get(1)), 4));
            }
        }
        for (HashSet<Integer> set : orig) {
            if (computed.contains(set) == false) {
                fn++;
            }
        }
        tpfpfn[0] = tp;
        tpfpfn[1] = fp;
        tpfpfn[2] = fn;
//        System.out.println("tp:" + tp);
//        System.out.println("fp:" + fp);
//        System.out.println("fn:" + fn);
        return tpfpfn;
    }

    public static double[] preRcFo(HashSet<HashSet<Integer>> orig, HashSet<HashSet<Integer>> computed) {

        int[] tpfpfn = tpfpfn(orig, computed);
        double[] preRcFo = new double[3];
        DecimalFormat df = new DecimalFormat("#.##");
        preRcFo[0] = Double.parseDouble(df.format(((double) tpfpfn[0] / (double) (tpfpfn[0] + tpfpfn[1]))));
        preRcFo[1] = Double.parseDouble(df.format(((double) tpfpfn[0] / (double) (tpfpfn[0] + tpfpfn[2]))));
        preRcFo[2] = Double.parseDouble(df.format(2 * preRcFo[0] * preRcFo[1] / (preRcFo[0] + preRcFo[1])));
        return preRcFo;
    }

    public static HashSet<String> naiveshingle(String doc, int gramFactor) {
        HashSet<String> docr = new HashSet<>();
        for (int i = 0; i < doc.length() - gramFactor + 1; i++) {
            docr.add(doc.substring(i, i + gramFactor));
        }
        return docr;
    }

    public static String[] rmSpace(String doc) {
        String[] noSpace = (doc.trim().split("\\s+"));
        return noSpace;
    }

//    public static HashSet<String> shingle(String doc, int gramFactor) {
//        String[] noSpace = rmSpace(doc);
//        HashSet<String> globalShingles = new HashSet<>();
//        for (String noSpace1 : noSpace) {
//            globalShingles.addAll(naiveshingle(noSpace1, gramFactor));
//        }
//        return globalShingles;
//    }
     public static HashSet<String> shingle(String doc, int gramFactor) {
        String[] noSpace = rmSpace(doc);
        HashSet<String> globalShingles = new HashSet<>();
        for (String noSpace1 : noSpace) {
            globalShingles.add(noSpace1);
        }
        return globalShingles;
    }
//    public static HashSet<String> shingle(String doc, int gramFactor) {
//        HashSet<String> docr = new HashSet<>();
//        String temp = doc.trim().replaceAll("^ +| +$|( )+", "$1");
//        for (int i = 0; i < temp.length() - gramFactor + 1; i++) {
//            docr.add(temp.substring(i, i + gramFactor));
//        }
//        return docr;
//    }

    public static int shinglenum(String doc, int gramFactor) {
//		System.out.println(doc);
//		System.out.println(shingle(doc,gramFactor));
//		System.out.println(shingle(doc, gramFactor).size());
        return shingle(doc, gramFactor).size();
    }

    public static double SetSimi(String set1, String set2, int gramFactor) {
        int intersect = 0;
        HashSet<String> union = new HashSet<>();
        HashSet<String> sset1 = shingle(set1, gramFactor);
        System.out.println(sset1);
        HashSet<String> sset2 = shingle(set2, gramFactor);
        System.out.println(sset2);
        union.addAll(sset1);
        union.addAll(sset2);
        for (String t : sset1) {
            if (sset2.contains(t)) {
                intersect++;
            }
        }
        DecimalFormat df = new DecimalFormat("#.#");
        if (union.isEmpty()) {
            return 0;
        }
        System.out.println(Double.parseDouble(df.format(((double)intersect/(double)union.size()))));
	return Double.parseDouble(df.format(((double)intersect/(double)union.size())));
    }
    

    public static HashSet<HashSet<Integer>> removeSubSet(HashSet<HashSet<Integer>> rawSet) {
        HashSet<HashSet<Integer>> temp = new HashSet<>();
        for (HashSet<Integer> set : rawSet) {
            temp.add(set);
        }
        for (HashSet<Integer> block : rawSet) {
            for (HashSet<Integer> block2 : rawSet) {
                if (block.equals(block2) == false) {
                    if (isSubset(block, block2)) {
                        temp.remove(block2);
                    }
                }
            }
        }
        return temp;
    }

    public static boolean isSubset(HashSet<Integer> set1, HashSet<Integer> set2) {
        HashSet<Integer> tempset2 = new HashSet<>();
        for (int element : set2) {
            tempset2.add(element);
        }
        tempset2.removeAll(set1);
        return tempset2.isEmpty();
    }

    public static boolean isSame(int a, int b, HashSet<HashSet<Integer>> setOfSets) {
        for (HashSet<Integer> set : setOfSets) {
            if (set.contains(a) && set.contains(b)) {
                return true;
            }
        }
        return false;
    }

    public static HashSet<HashSet<Integer>> deepCopySet(HashSet<HashSet<Integer>> setOfSets) {
        HashSet<HashSet<Integer>> tempSet = new HashSet<>();
        for (HashSet<Integer> temp : setOfSets) {
            HashSet<Integer> temp2 = new HashSet<>();
            for (int temp3 : temp) {
                temp2.add(temp3);
            }
            tempSet.add(temp2);
        }
        return tempSet;
    }

    public static HashSet<Integer> deepCopy(HashSet<Integer> set) {
        HashSet<Integer> tempSet = new HashSet<>();
        for (int temp : set) {
            tempSet.add(temp);
        }
        return tempSet;
    }
    
    public static ArrayList<Integer> deepCopy(ArrayList<Integer> list){
        ArrayList<Integer> list1=new ArrayList<>();
        for(int in:list){
            list1.add(in);
        }
        return list1;
    }
    
    public static List<Integer> deepCopy(List<Integer> list){
        List<Integer> list1=new ArrayList<>();
        for(int in:list){
            list1.add(in);
        }
        return list1;
    }

    public static boolean hasIntersect(HashSet<Integer> set1, HashSet<Integer> set2) {
        HashSet<Integer> temp = deepCopy(set1);
        temp.retainAll(set2);
        return !temp.isEmpty();
    }
    
    

    public static ArrayList<HashSet<Integer>> transiveRule(HashSet<HashSet<Integer>> originSets) {
        HashSet<HashSet<Integer>> tempSet = deepCopySet(originSets);
        ArrayList<HashSet<Integer>> clusterList = new ArrayList<>();
        for (HashSet<Integer> temp : tempSet) {
            clusterList.add(temp);
        }
        for (int i = 0; i < clusterList.size(); i++) {
            HashSet<Integer> temp = clusterList.get(i);
            boolean mark;
            do {
                mark = false;
                for (int j = i + 1; j < clusterList.size(); j++) {
                    HashSet<Integer> temp2 = clusterList.get(j);
                    if (hasIntersect(temp, temp2)) {
                        temp.addAll(temp2);
                        clusterList.remove(clusterList.get(j));
                        clusterList.set(i, temp);
                        mark = true;
                    }
                }
            } while (mark);

        }

        return clusterList;

    }
    
    

}
