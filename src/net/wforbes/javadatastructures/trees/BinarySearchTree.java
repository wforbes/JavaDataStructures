package net.wforbes.javadatastructures.trees;

import java.util.ArrayList;
import java.util.List;

public class BinarySearchTree<E extends Comparable<E>> implements BinarySearchTreeInterface<E>{
    private BinarySearchTreeNode<E> root;

    public BinarySearchTree(){
        this.root = null;
    }

    @Override
    public BinarySearchTreeNodeInterface<E> getRoot(){
        return this.root;
    }

    @Override
    public boolean add(E value){
        if(this.contains(value)){
            return false;
        }

        if(root == null){
            this.root = new BinarySearchTreeNode<E>(value);
            return true;
        }

        BinarySearchTreeNode<E> current = this.root;

        while(current != null){
            if(current.getData().compareTo(value) > 0){
                if(current.getLeft() == null){
                    current.setLeft(new BinarySearchTreeNode<E>(value));
                    current = null;
                }else{
                    current = current.getLeft();
                }
            }else{
                if(current.getRight() == null){
                    current.setRight(new BinarySearchTreeNode<E>(value));
                    current = null;
                }else{
                    current = current.getRight();
                }
            }
        }
        return true;
    }

    @Override
    public boolean contains(E value){
        BinarySearchTreeNode<E> current = this.root;
        while(current != null){
            if(current.getData().compareTo(value) == 0){
                return true;
            }else if(current.getData().compareTo(value) > 0){
                current = current.getLeft();
            }else{
                current = current.getRight();
            }
        }
        return false;
    }

    @Override
    public void clear(){
        this.root = null;
    }

    @Override
    public int size(){
        return this.size(this.root);
    }
    public int size(BinarySearchTreeNode<E> node){
        if(node == null){
            return 0;
        }else{
            return 1 + size(node.getLeft()) + size(node.getRight());
        }
    }

    @Override
    public int height(){
        if(root == null){
            return 0;
        }
        return this.height(root);
    }
    public int height(BinarySearchTreeNode<E> node){
        if(node == null){
            return -1;
        }
        int leftHeight = this.height(node.getLeft());
        int rightHeight = this.height(node.getRight());
        return 1 + Math.max(leftHeight, rightHeight);
    }

    @Override
    public List<E> getDataInOrder(){
        if(height() == 0 && root == null){
            return new ArrayList<E>();
        }
        ArrayList<E> list = new ArrayList<>();
        this.getDataInOrder(root, list);
        return list;
    }
    private List<E> getDataInOrder(BinarySearchTreeNode<E> node, List<E> list){
        if(node == null){
            return list;
        }
        list = this.getDataInOrder(node.getLeft(), list);
        list.add(node.getData());
        list = this.getDataInOrder(node.getRight(), list);
        return list;
    }

    @Override
    public List<E> getDataPreOrder(){
        if(height() == 0 && root == null){
            return new ArrayList<E>();
        }
        ArrayList<E> list = new ArrayList<>();
        this.getDataPreOrder(root, list);
        return list;
    }
    private List<E> getDataPreOrder(BinarySearchTreeNode<E> node, List<E> list){
        if(node == null){
            return list;
        }
        list.add(node.getData());
        list = this.getDataPreOrder(node.getLeft(), list);
        list = this.getDataPreOrder(node.getRight(), list);
        return list;
    }

    @Override
    public List<E> getDataPostOrder(){
        if(height() == 0 && root == null){
            return new ArrayList<E>();
        }
        ArrayList<E> list = new ArrayList<>();
        this.getDataPostOrder(root, list);
        return list;
    }
    private List<E> getDataPostOrder(BinarySearchTreeNode<E> node, List<E> list){
        if(node == null){
            return list;
        }
        list = this.getDataPostOrder(node.getLeft(), list);
        list = this.getDataPostOrder(node.getRight(), list);
        list.add(node.getData());
        return list;
    }

    @Override
    public boolean remove(E value){
        BinarySearchTreeNode<E> parent = null;
        BinarySearchTreeNode<E> current = this.root;

        while(current != null){
            if(current.getData().compareTo(value) == 0){
                //if node is the root
                if(current.getLeft() == null && current.getRight() == null){
                    if(parent == null){
                        this.root = null;
                    }else if(parent.getLeft() == current){
                        parent.setLeft(null);
                    }else{
                        parent.setRight(null);
                    }
                    //if node has only left child
                }else if(current.getLeft() != null && current.getRight() == null){
                    if(parent == null){
                        this.root = current.getLeft();
                    }else if(parent.getLeft() == current){
                        parent.setLeft(current.getLeft());
                    }else{
                        parent.setRight(current.getLeft());
                    }
                    //if node has only right child
                }else if(current.getRight() != null && current.getLeft() == null){
                    if(parent == null){
                        this.root = current.getRight();
                    }else if(parent.getLeft() == current){
                        parent.setLeft(current.getRight());
                    }else{
                        parent.setRight(current.getRight());
                    }
                    //if node has two children
                }else{
                    //find successor
                    BinarySearchTreeNode<E> successor = current.getRight();
                    while(successor.getLeft() != null){
                        successor = successor.getLeft();
                    }
                    E successorData = successor.getData();
                    this.remove(successorData);
                    current.setData(successorData);
                }
                return true;

            }else if(current.getData().compareTo(value) < 0){
                parent = current;
                current = current.getRight();
            }else{
                parent = current;
                current = current.getLeft();
            }
        }
        return false;
    }
}