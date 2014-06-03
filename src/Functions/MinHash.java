package Functions;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.HashMap;
import java.util.Random;

public class MinHash {

    public static HashMap<String, Integer> uHstb = new HashMap<>();
    private final int gramFactor;
    public int numOfFunc;
    public static final int MAX_INT_SMALLER_TWIN_PRIME = 2147482949;
    public static int max = Integer.MAX_VALUE;

    public MinHash(int gramF, int numOfF) {
        gramFactor = gramF;
        numOfFunc = numOfF;
    }

    //extend the universal character table, which grows with the signature computing process one by one  
    public static void addToUniversalHstb(HashSet<String> set) {
        for (String t : set) {
            if (uHstb.containsKey(t) == false) {
                uHstb.put(t, uHstb.size() + 1);
            }
        }
    }

    //Hash a certain row number with a certain seed, which represents a certain hashfunction 
    private int hashFunc(int orig, int seed) {
        Random rand = new Random(seed);
        int[] seedsValues = new int[3];
        seedsValues[0] = rand.nextInt(1000000000);
        seedsValues[1] = rand.nextInt(1000000000);
        seedsValues[2] = rand.nextInt(1000000000);
        return (int) ((seedsValues[0] * (orig >> 4) + seedsValues[1] * orig + seedsValues[2]) % MAX_INT_SMALLER_TWIN_PRIME);
    }

    //Initialize the signature List for any record
    private ArrayList<Integer> initSL() {
        ArrayList<Integer> List = new ArrayList<>();
        for (int i = 0; i < numOfFunc; i++) {
            List.add(max);
        }
        return List;
    }

    //Compute the signature List for a certain record
    public ArrayList<Integer> sig(String record) {
        HashSet<String> recordset = CollectionOperator.shingle(record, gramFactor);
        if(record.length()<=4){
            System.out.println(record);
        }
        addToUniversalHstb(recordset);
        ArrayList<Integer> record_sig;
        record_sig = initSL();
        for (int i = 0; i < numOfFunc; i++) {
            for (String element : recordset) {
                record_sig.set(i, Math.min(record_sig.get(i), hashFunc(uHstb.get(element), i)));
            }
        }
        return record_sig;
    }

    public void establishUniverseDic(String record) {
        HashSet<String> recordset = CollectionOperator.shingle(record, gramFactor);
        addToUniversalHstb(recordset);
    }

}
