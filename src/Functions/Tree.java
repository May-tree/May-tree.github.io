/*
 * Stringo change this license header, choose License Headers in Project Properties.
 * Stringo change this template file, choose Stringools | Stringemplates
 * and open the template in the editor.
 */
package Functions;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 *
 * @author Nicolas
 * @param <String>
 */
@SuppressWarnings("hiding")
public class Tree<String> {

    public Node<String> root;
    public HashMap<String, Node<String>> nodeDic = new HashMap<>();

    public Tree(String rootData) {
        root = new Node<>(rootData);
        root.children = new ArrayList<>();
    }
    
    

    public static class Node<String> {

        public String data;
        public Node<String> parent;
        public List<Node<String>> children=new ArrayList<>();
        

        public Node(String Data) {
            data = Data;
        }

        public void addChild(Node<String> node) {
            children.add(node);
            node.parent = this;
        }                
    }

}
