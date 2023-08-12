package com.taboola.assignment.Tree;

public class TreeSerExpTest {

    public static void main(String[] args) {
        // Create a cyclic tree
        Node root= new Node(1);
        root.left= new Node(2);
        root.right= new Node(3);
        root.left.left= new Node(4);
        root.left.right= root; // Introduce a cycle

        // Create a TreeSerExp instance
        TreeSerExp treeSerializer= new TreeSerExp();

        try {
            // Attempt to serialize the cyclic tree
            String serializedTree= treeSerializer.serialize(root);
            System.out.println("Serialized Tree: " + serializedTree);

        } catch (IllegalArgumentException e) {
            System.out.println("Exception: " + e.getMessage());
        }

        Node root1= new Node(1);
        root1.left= new Node(2);
        root1.right= new Node(7);
        root1.right.left= new Node(5);
        root1.right.right= new Node(28);

        TreeSerExp treeSerializer1= new TreeSerExp();

        try {
            // Attempt to serialize the tree
            String serializedTree= treeSerializer1.serialize(root1);
            System.out.println("Serialized Tree: " + serializedTree);
        } catch (IllegalArgumentException e) {
            System.out.println("Exception: " + e.getMessage());
        }
    }

}
