package net.wforbes.javadatastructures.tree;

public class BinarySearchTreeNode<E extends Comparable<E>> implements BinarySearchTreeNodeInterface<E>{

    private E data;
    private BinarySearchTreeNode<E> left;
    private BinarySearchTreeNode<E> right;

    public BinarySearchTreeNode(E value){
        this.data = value;
    }

    @Override
    public E getData(){
        return this.data;
    }

    @Override
    public BinarySearchTreeNode<E> getLeft(){
        return this.left;
    }

    @Override
    public BinarySearchTreeNode<E> getRight(){
        return this.right;
    }

    public void setData(E value){
        this.data = value;
    }
    public void setLeft(BinarySearchTreeNode<E> node){
        this.left = node;
    }
    public void setRight(BinarySearchTreeNode<E> node){
        this.right = node;
    }

}