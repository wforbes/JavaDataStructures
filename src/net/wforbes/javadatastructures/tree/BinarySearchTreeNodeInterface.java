package net.wforbes.javadatastructures.tree;

/**
 *
 * Represents a node within the binary search tree.
 * @param <E> The type of data that will be contained within each node
 */
public interface BinarySearchTreeNodeInterface<E extends Comparable<E>> {

    /**
     * @return the data contained within this node
     */
    public E getData();

    /**
     * @return this nodes left child, or null if there is none
     */
    public BinarySearchTreeNodeInterface<E> getLeft();

    /**
     * @return this nodes right child, or null if there is none
     */
    public BinarySearchTreeNodeInterface<E> getRight();
}
