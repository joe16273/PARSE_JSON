package com.taboola.assignment.Tree;

public interface TreeSerializer {

    String serialize(Node root);

    Node deserialize(String str);

}
