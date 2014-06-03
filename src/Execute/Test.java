package Execute;

import java.util.HashMap;
import java.util.HashSet;

public class Test {

    public static void main(String[] args) {
//        SemanticTree.buildTree();
//        Tree<String> RO=SemanticTree.RO;
//        HashSet<String> leaves=new HashSet<>();
//        leaves.add("JN");
//        leaves.add("PAT");
//        leaves.add("NPR");
//        ArrayList<Integer> a=SemanticTree.getSemanticSig(leaves);
//        System.out.println(a);
//        System.out.println("done");
        HashSet<Integer> o = new HashSet<>();
        HashSet<Integer> p = new HashSet<>();
        o.add(2);
        p.add(1);
        System.out.println(o.equals(p));
    }
}
