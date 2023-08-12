package com.taboola.assignment.Tree;

import java.util.Arrays;
import java.util.Deque;
import java.util.LinkedList;

public class TreeSer implements TreeSerializer {
    private static final String comma= ",";
    private static final String null_val= "NULL";

    /** Serialize a binary tree to a string.
     *
     * @param root The root node of the binary tree.
     * @return A serialized string representation of the binary tree. */
    @Override
    public String serialize(Node root) {
        // TODO Auto-generated method stub
        StringBuilder curr= new StringBuilder();
        dfs(root, curr);
        if (curr.length() > 0 && curr.charAt(curr.length() - 1) == ',') {
            curr.deleteCharAt(curr.length() - 1);
        }
        return curr.toString();

    }

    /** Helper method for serializing the binary tree using DFS(pre-order) traversal.
     *
     * @param root The current node being visited.
     * @param res  The StringBuilder to accumulate the serialized string. */

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

    /** Deserialize a string to a binary tree.
     *
     * @param str A serialized string representation of the binary tree.
     * @return The root node of the deserialized binary tree. */
    @Override
    public Node deserialize(String str) {
        // TODO Auto-generated method stub
        Deque<String> node_list= new LinkedList<>();
        node_list.addAll(Arrays.asList(str.split(comma)));
        return treeDfs(node_list);
    }

    /** Helper method for deserializing the binary tree using DFS traversal.
     *
     * @param nodes A Deque of string tokens representing the serialized binary tree.
     * @return The root node of the binary tree. */

    private Node treeDfs(Deque<String> nodes) {
        String val= nodes.remove();
        if (val.equals("NULL")) {
            return null;
        } else {
            Node node= new Node(Integer.parseInt(val));
            node.left= treeDfs(nodes);
            node.right= treeDfs(nodes);
            return node;
        }

    }

}