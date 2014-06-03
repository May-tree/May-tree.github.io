/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Functions;

import Functions.Tree.Node;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;

/**
 *
 * @author Nicolas
 */
public class SemanticTree {

    public static Tree<String> RO = new Tree("RO");
    public static HashMap<String, Node<String>> nodeMap = new HashMap<>();
    public static ArrayList<String> leaveList = new ArrayList<>();

    public static void buildMap(Node<String> node) {
        nodeMap.put(node.data, node);
    }

    public static void buildTree() {
        buildMap(RO.root);
        Node<String> PUB = new Node<>("PUB");
        Node<String> PR = new Node<>("PR");
        Node<String> PAT = new Node<>("PAT");
        Node<String> NPR = new Node<>("NPR");
        Node<String> JN = new Node<>("JN");
        Node<String> CON = new Node<>("CON");
        Node<String> BK = new Node<>("BK");
        Node<String> RP = new Node<>("RP");
        Node<String> TH = new Node<>("TH");
        buildMap(PUB);
        buildMap(PR);
        buildMap(PAT);
        buildMap(NPR);
        buildMap(JN);
        buildMap(CON);
        buildMap(BK);
        buildMap(RP);
        buildMap(TH);
        RO.root.addChild(PUB);
        RO.root.addChild(PAT);
        PUB.addChild(PR);
        PUB.addChild(NPR);
        PR.addChild(JN);
        PR.addChild(CON);
        PR.addChild(BK);
        NPR.addChild(RP);
        NPR.addChild(TH);
        leaveList.add("JN");
        leaveList.add("CON");
        leaveList.add("BK");
        leaveList.add("RP");
        leaveList.add("TH");
        leaveList.add("PAT");

    }

    public static HashSet<String> leaves(Node<String> node) {
        HashSet<String> leaves = new HashSet<>();
        
        if (node.children.isEmpty()) {
            leaves.add(node.data);
            return leaves;
        }
        List<Node<String>> list = node.children;
        for (Node<String> candiNode : list) {
            if (candiNode.children.isEmpty()) {
                leaves.add(candiNode.data);
            } else {
                leaves.addAll(leaves(candiNode));
            }
        }
        return leaves;
    }

    public static ArrayList<Integer> getSemanticSig(HashSet<String> cateSet) {
        ArrayList<Integer> sig = new ArrayList<>();
        HashSet<String> leaves = new HashSet<>();
        for (String cate : cateSet) {
            Node<String> temp = nodeMap.get(cate);
            leaves.addAll(leaves(temp));
        }
        for (String leaveList1 : leaveList) {
            if (leaves.contains(leaveList1)) {
                sig.add(1);
            } else {
                sig.add(0);
            }
        }
        return sig;
    }
}
