package com.taboola.assignment.Tree;

import java.util.Arrays;
import java.util.Deque;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.Set;

public class TreeSerExp implements TreeSerializer {
    private static final String comma= ",";
    private static final String null_val= "NULL";

    /** Serializes a binary tree into a string representation, while checking for cycles.
     *
     * @param root The root node of the binary tree to be serialized.
     * @return The serialized string representation of the binary tree.
     * @throws IllegalArgumentException If a cycle is detected in the binary tree during
     *                                  serialization. */

    @Override
    public String serialize(Node root) {
        if (hasCycle(root)) {
            throw new IllegalArgumentException(
                "Detected a cycle in the tree during serialization.");
        }

        StringBuilder curr= new StringBuilder();
        dfs(root, curr);
        if (curr.length() > 0 && curr.charAt(curr.length() - 1) == ',') {
            curr.setLength(curr.length() - 1);
        }
        return curr.toString();
    }

    /** Helper function for the serialization process. Performs a depth-first traversal and appends
     * node values to the StringBuilder.
     *
     * @param root The current node being processed.
     * @param res  The StringBuilder used to build the serialized string. */

    private void dfs(Node root, StringBuilder res) {
        if (root == null) {
            res.append(null_val);
            res.append(comma);
        } else {
            res.append(root.num);
            res.append(comma);
            dfs(root.left, res);
            dfs(root.right, res);
        }
    }

    /** Checks if a binary tree has a cycle by performing a depth-first traversal and tracking
     * visited nodes.
     *
     * @param root The root node of the binary tree to check for cycles.
     * @return True if a cycle is detected, false otherwise. */
    private boolean hasCycle(Node root) {
        Set<Node> visited= new HashSet<>();
        return hasCycleHelper(root, visited);
    }

    /** Helper function for cycle detection. Recursively performs depth-first traversal and
     * maintains a set of visited nodes.
     *
     * @param root    The current node being processed.
     * @param visited The set of visited nodes.
     * @return True if a cycle is detected, false otherwise. */

    private boolean hasCycleHelper(Node root, Set<Node> visited) {
        if (root == null) { return false; }
        if (visited.contains(root)) { return true; }
        visited.add(root);
        boolean leftHasCycle= hasCycleHelper(root.left, visited);
        boolean rightHasCycle= hasCycleHelper(root.right, visited);
        visited.remove(root);
        return leftHasCycle || rightHasCycle;
    }

    /** Deserializes a string representation of a binary tree back into a binary tree structure.
     *
     * @param str The serialized string representation of the binary tree.
     * @return The root node of the deserialized binary tree. */

    @Override
    public Node deserialize(String str) {
        Deque<String> node_list= new LinkedList<>();
        node_list.addAll(Arrays.asList(str.split(comma)));
        return treeDfs(node_list);
    }

    /** Helper function for the deserialization process. Recursively constructs the binary tree from
     * the serialized string using a depth-first approach.
     *
     * @param nodes A queue of node values from the serialized string.
     * @return The current node being processed. */

    private Node treeDfs(Deque<String> nodes) {
        String val= nodes.remove();
        if (val.equals(null_val)) {
            return null;
        } else {
            Node node= new Node(Integer.parseInt(val));
            node.left= treeDfs(nodes);
            node.right= treeDfs(nodes);
            return node;
        }
    }

}