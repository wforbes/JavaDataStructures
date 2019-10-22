package net.wforbes.javadatastructures.tree;

public class BinarySearchTreeNode<E extends Comparable<E>> implements BinarySearchTreeNodeInterface<E>{

    private E data;
    private int height;
    private int balance;
    private BinarySearchTreeNode<E> parent;
    private BinarySearchTreeNode<E> left;
    private BinarySearchTreeNode<E> right;

    public BinarySearchTreeNode(E value){
        this.data = value;
    }

    public BinarySearchTreeNode(E value, BinarySearchTreeNode<E> parent){
        this.data = value;
        this.parent = parent;
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

    public BinarySearchTreeNode<E> getParent(){
        return this.parent;
    }

    @Override
    public int getBalanceFactor(){
        return this.balance;
    }

    public int getHeight(){
        return this.height;
    }

    @Override
    public void setBalanceFactor(int balanceFactor){
        this.balance = balanceFactor;
    }

    public void setHeight(int height){
        this.height = height;
    }

    public void setParent(BinarySearchTreeNode<E> node){
        this.parent = node;
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