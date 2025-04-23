package com.organization.orgHierarchy.service;

import java.util.ArrayList;
import java.util.List;

public class TreeNode {
    private String name; // The name of the node (e.g., employee ID)
    private List<TreeNode> children; // The children of this node (subordinates)

    // Constructor
    public TreeNode(String name) {
        this.name = name;
        this.children = new ArrayList<>();
    }

    // Getter for name
    public String getName() {
        return name;
    }

    // Setter for name
    public void setName(String name) {
        this.name = name;
    }

    // Getter for children
    public List<TreeNode> getChildren() {
        return children;
    }

    // Method to add a child node
    public void addChild(TreeNode child) {
        this.children.add(child);
    }

    // Method to check if a node has children
    public boolean hasChildren() {
        return !children.isEmpty();
    }
}
