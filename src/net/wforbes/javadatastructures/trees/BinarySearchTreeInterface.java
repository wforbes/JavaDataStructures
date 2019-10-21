package net.wforbes.javadatastructures.trees;

import java.util.List;

/**
 * Basic interface for the binary search tree.
 *
 * The student's implementation must implement each of these
 * methods, plus a default constructor
 *
 * @param <E>
 */
public interface BinarySearchTreeInterface<E extends Comparable<E>> {

    /**
     * The method returns the root node of the tree. This is not a standard binary tree operation,
     * however, by giving public access to the tree node structure, this allows the zylabs unit tests
     * to give you much better feedback about any differences between your tree and the expected
     * results.
     * @return The root containing the entire tree (or null for an empty tree)
     */
    public BinarySearchTreeNodeInterface<E> getRoot();

    /**
     * Adds the specified item to the tree (if it does not already exist)
     * @return true if the item was added, otherwise, false
     */
    public boolean add(E value);

    /**
     * @param value value to look for
     * @return true if and only if the value is contained within the tree
     */
    public boolean contains(E value);

    /**
     * Clears the BST
     */
    public void clear();

    /**
     * @return the size of the tree
     */
    public int size();

    /**
     * Depth is the maximum number of edges between the root and any of its ancestors
     * @return the height of the tree
     */
    public int height();

    /**
     * @return list representing data in an in-order traversal
     */
    public List<E> getDataInOrder();

    /**
     * @return list representing data in a pre-order traversal
     */
    public List<E> getDataPreOrder();

    /**
     * @return list representing data in a post-order traversal
     */
    public List<E> getDataPostOrder();

    /**
     * Extra credit
     * </br></br>
     * Remove the specified item from the tree if it exists.
     * I'm going to be pretty generous with the extra credit on this one
     * so even if you don't think you can implement a complete remove method,
     * try to implement the easy cases (such as removing a leaf node). There will
     * be mulitple extra credit zylabs tests and some of them will be easier than others.
     * </br></br>
     * For the case of removing an internal node with two children, there are two algorithms
     * that work: replacing the node to remove with the smallest node on the right subtree or
     * replacing the node to remove with the largest node on the left subtree. Both of these
     * approaches are technically correct, however, you must choose the first option (removing
     * the smallest node on the right subtree) in order to pass the tests and get credit.
     * @param value value to remove
     * @return true if the value was present in the tree, false otherwise
     */
    public boolean remove(E value);

}