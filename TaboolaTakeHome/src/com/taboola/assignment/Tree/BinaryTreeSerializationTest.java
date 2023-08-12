package com.taboola.assignment.Tree;

public class BinaryTreeSerializationTest {
    public static void main(String[] args) {
        TreeSer serializer= new TreeSer();

        // Test Case 1: Balanced tree
        Node root1= new Node(1);
        root1.left= new Node(2);
        root1.right= new Node(3);
        root1.left.left= new Node(4);
        root1.left.right= new Node(5);
        String serialized1= serializer.serialize(root1);
        Node deserialized1= serializer.deserialize(serialized1);
        System.out.println(serialized1);

        // Test Case 2: Unbalanced tree
        Node root2= new Node(10);
        root2.left= new Node(5);
        root2.left.left= new Node(3);
        String serialized2= serializer.serialize(root2);
        Node deserialized2= serializer.deserialize(serialized2);
        System.out.println(serialized2);

        // Test Case 3: Null tree
        Node root3= null;
        String serialized3= serializer.serialize(root3);
        Node deserialized3= serializer.deserialize(serialized3);
        System.out.println(serialized3);

        // Test Case 4: Tree with only left branch
        Node root5= new Node(1);
        root5.left= new Node(2);
        String serialized5= serializer.serialize(root5);
        Node deserialized5= serializer.deserialize(serialized5);
        System.out.println(serialized5);

        // Test Case 5: Tree with only right branch
        Node root6= new Node(1);
        root6.right= new Node(2);
        String serialized6= serializer.serialize(root6);
        Node deserialized6= serializer.deserialize(serialized6);
        System.out.println(serialized6);

        // Test Case 6: Tree with duplicate values
        Node root7= new Node(2);
        root7.left= new Node(1);
        root7.right= new Node(2);
        String serialized7= serializer.serialize(root7);
        Node deserialized7= serializer.deserialize(serialized7);
        System.out.println(serialized7);

        // Test Case 7: Complex balanced tree
        Node root8= new Node(5);
        root8.left= new Node(3);
        root8.left.left= new Node(1);
        root8.left.right= new Node(4);
        root8.right= new Node(7);
        root8.right.left= new Node(6);
        root8.right.right= new Node(8);
        String serialized8= serializer.serialize(root8);
        Node deserialized8= serializer.deserialize(serialized8);
        System.out.println(serialized8);

        // Test Case 8: Complex unbalanced tree
        Node root9= new Node(10);
        root9.left= new Node(5);
        root9.left.left= new Node(3);
        root9.left.left.left= new Node(2);
        root9.left.right= new Node(7);
        root9.left.right.left= new Node(6);
        root9.left.right.right= new Node(8);
        String serialized9= serializer.serialize(root9);
        Node deserialized9= serializer.deserialize(serialized9);
        System.out.println(serialized9);
    }
}
