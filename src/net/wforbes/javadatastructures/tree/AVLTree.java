package net.wforbes.javadatastructures.tree;

import java.util.ArrayList;
import java.util.List;

public class AVLTree<E extends Comparable<E>> implements BinarySearchTreeInterface<E>{
  private BinarySearchTreeNode<E> root;
  
  public AVLTree(){
    this.root = null;
  }
  
  @Override
  public BinarySearchTreeNodeInterface<E> getRoot(){
    return this.root;
  }
  
  @Override
  public boolean add(E value){
      /*//move contains check to while loop
    if(this.contains(value)){
      return false;
    }*/
    
    if(root == null){
      this.root = new BinarySearchTreeNode<E>(value, null);
      return true;
    }
    
    BinarySearchTreeNode<E> current = this.root;
    
    while(true){
        if(current.getData().compareTo(value)==0){
            return false;
        }
        
        //nodes need to track their parents now to keep tree balanced
        BinarySearchTreeNode<E> parent = current;
        
        boolean lessThanAddVal = current.getData().compareTo(value) > 0;
        current = (lessThanAddVal)? current.getLeft() : current.getRight();
        
        if(current == null){
            if(lessThanAddVal){
                parent.setLeft(new BinarySearchTreeNode<E>(value, parent));
            }else{
                parent.setRight(new BinarySearchTreeNode<E>(value, parent));
            }
            this.balance(parent);
            break;
        }
    }
    return true;
    /* //Old BST add method
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
    */
  }
  
  private void balance(BinarySearchTreeNode<E> node){
      this.resetHeight(node);
      int newBalance = height(node.getLeft()) - height(node.getRight());
      node.setBalanceFactor(newBalance);
      if(node.getBalanceFactor() == -2){
          //check right child balance factor
          
          if(node.getRight()!= null && node.getRight().getBalanceFactor() == -1){
              node = rotateRight(node);//Right-right rotation
          }else if(node.getRight()!= null && node.getRight()!= null && node.getRight().getBalanceFactor() == 1){
              node = rotateLeftRight(node);
          }
      }else if(node.getBalanceFactor() == 2){
          //check left child balance factor
          if(node.getLeft()!= null && node.getLeft().getBalanceFactor() == 1){
              node = rotateLeft(node);
          }else if(node.getLeft()!= null && node.getLeft().getBalanceFactor() == -1){
              node = rotateRightLeft(node);
          }
      }
      
      if(node.getParent() != null){
          balance(node.getParent());
      }else{
          this.root = node;
      }
  }
  
  //-2, 1 (Right-left naming conflicts with AVL lecture slide..)
  private BinarySearchTreeNode<E> rotateLeftRight(BinarySearchTreeNode<E> node){
      node.setRight(rotateLeft(node.getRight()));
      return rotateRight(node);
  }
  
  //2, -1 (Left-right naming conflicts with AVL lecture slide..)
  private BinarySearchTreeNode<E> rotateRightLeft(BinarySearchTreeNode<E> node){
      node.setLeft(rotateRight(node.getLeft()));
      return rotateLeft(node);
  }
  
  //rotateLeft = "left-left" (2,1) case from AVL lecture slides
  private BinarySearchTreeNode<E> rotateLeft(BinarySearchTreeNode<E> a){
      //System.out.println("ROTATING LEFT");
      BinarySearchTreeNode<E> b = a.getLeft();//get left child (B)
      b.setParent(a.getParent());//B gets A's parent
      
      a.setLeft(b.getRight());//move t3 from B's right to A's left
      if(a.getLeft() != null){
          a.getLeft().setParent(a);//update t3's parent as A
      }
      
      //B's new right child is A
      b.setRight(a);
      a.setParent(b);
      
      //update B's parent, checking for left/right child position of A
      if(b.getParent() != null){//if B has a parent (A wasn't root)
          if(b.getParent().getLeft() == a){
              b.getParent().setLeft(b);
          }else{
              b.getParent().setRight(b);
          }
      }
      
      //recalculate new height and balance of A
      this.resetHeight(a);
      int newBalance = height(a.getLeft()) - height(a.getRight());
      a.setBalanceFactor(newBalance);
      
      //recalculate new height and balance of B
      this.resetHeight(b);
      newBalance = height(b.getLeft()) - height(b.getRight());
      b.setBalanceFactor(newBalance);
      
      return b;
  }
  
  //rotateRight = "right-right" (-2, -1) case from AVL lecture slides
  private BinarySearchTreeNode<E> rotateRight(BinarySearchTreeNode<E> a){
      BinarySearchTreeNode<E> b = a.getRight();//get right child (B)
      b.setParent(a.getParent());//right child (B) trades places with node (A)
      
      a.setRight(b.getLeft());//move t2 from left of B to right of A
      if(a.getRight() != null){//if t2 exists
        a.getRight().setParent(a);//update t2's parent to A
      }
      
      //B's new left child is A
      b.setLeft(a);
      a.setParent(b);
      
      //update B's parent, checking for left or right child position of A
      if(b.getParent() != null){//if B has a parent (A wasn't root)
          if(b.getParent().getLeft() == a){//if A was left node
              b.getParent().setLeft(b);
          }else{
              b.getParent().setRight(b);
          }
      }
      
      //recalculate new height and balance of A
      this.resetHeight(a);
      int newBalance = height(a.getLeft()) - height(a.getRight());
      a.setBalanceFactor(newBalance);
      
      //recalculate new height and balance of B
      this.resetHeight(b);
      newBalance = height(b.getLeft()) - height(b.getRight());
      b.setBalanceFactor(newBalance);
      
      return b;
  }
  
  private void resetHeight(BinarySearchTreeNode<E> node){
      if(node != null){
            int newHeight = 1 + Math.max(height(node.getLeft()), height(node.getRight()));
            node.setHeight(newHeight);
      }
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
    return node.getHeight();
    /*//nodes track their own height now
    int leftHeight = this.height(node.getLeft());
    int rightHeight = this.height(node.getRight());
    return 1 + Math.max(leftHeight, rightHeight);
    */
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
  /* --prototype for remove method (single func method)
  public BinarySearchTreeNode<E> findNodeByValue(E value){
    BinarySearchTreeNode<E> current = this.root;
    while(current != null){
      if(current.getData().compareTo(value) == 0){
        return current;
      }else if(current.getData().compareTo(value) > 0){
        current = current.getLeft();
      }else{
        current = current.getRight();
      }
    }
    return null;
  }
  */
  private void remove(BinarySearchTreeNode<E> node){
      if(node.getLeft() == null && node.getRight() == null){
          if(node.getParent() == null){
              this.root = null;
          }else{
              BinarySearchTreeNode<E> parent = node.getParent();
              if(parent.getLeft() == node){
                  parent.setLeft(null);
              }else{
                  parent.setRight(null);
              }
              this.balance(parent);
          }
          return;
      }
    if(node.getLeft() != null){
        //System.out.println(node.getData()+" Left isn't null...");
        BinarySearchTreeNode<E> child = node.getLeft();
        while(child.getRight() != null){
            child = child.getRight();
        }
        node.setData(child.getData());
        remove(child);
    }else{
        BinarySearchTreeNode<E> child = node.getRight();
        while(child.getLeft() != null){
            child = child.getLeft();
        }
        node.setData(child.getData());
        remove(child);
    }
  }
  
  @Override
  public boolean remove(E value){
      /* //AVL removal with rotations ... still needs work (balance isn't right)
      if(this.root == null){
          return false;
      }
      
      BinarySearchTreeNode<E> child = root;
      while(child != null){
          BinarySearchTreeNode<E> node = child;
          child = node.getData().compareTo(value) <= 0 ? node.getRight() : node.getLeft();
          if(node.getData().compareTo(value) == 0){
              System.out.println("Found "+value+" ..removing..");
              remove(node);
              return true;
          }
      }
      return false;
      */
    // original BST removal - gets less fails somehow
  	BinarySearchTreeNode<E> parent = null;
    BinarySearchTreeNode<E> current = this.root;
    //System.out.println("!! Removing value: "+value);
    while(current != null){
      if(current.getData().compareTo(value) == 0){
        //System.out.println("Node found... : Value_"+value+" Current_"+current.getData());
        //if node has no children
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
            //System.out.println(current.getData()+" Node has one left child");
            //System.out.println("Child is "+current.getLeft().getData());
          if(parent == null){
              //System.out.println(current.getData()+" Node doesnt have parent");
          	this.root = current.getLeft();
          }else if(parent.getLeft() == current){
              //System.out.println(current.getData()+" Node is on left of parent");
          	parent.setLeft(current.getLeft());
          }else{
              //System.out.println(current.getData()+" Node is on right of parent");
          	parent.setRight(current.getLeft());
          }
        //if node has only right child
        }else if(current.getRight() != null && current.getLeft() == null){
            //System.out.println(current.getData()+" Node has one right child");
            //System.out.println("Child is "+current.getRight().getData());
          if(parent == null){
              //System.out.println(current.getData()+" Node doesnt have parent");
          	this.root = current.getRight();
          }else if(parent.getLeft() == current){
              //System.out.println(current.getData()+" Node is on left of parent");
          	parent.setLeft(current.getRight());
          }else{
              //System.out.println(current.getData()+" Node is on right of parent");
              //System.out.println("Parent is: "+parent.getData());
          	parent.setRight(current.getRight());
          }
        //if node has two children
        }else{
          //find successor
          //System.out.println(current.getData()+" Node has two children...");
          BinarySearchTreeNode<E> successor = current.getRight();
          while(successor.getLeft() != null){
          	successor = successor.getLeft();	
          }
          E successorData = successor.getData();
          this.remove(successorData);
          current.setData(successorData);
          balance(current);
        }
        return true;
        
      }else if(current.getData().compareTo(value) < 0){
          //System.out.println(value+" is greater than "+current.getData());
      	parent = current;
        current = current.getRight();
        //System.out.println("Moving on to .. "+current.getData());
      }else{
          //System.out.println(value+" is less than "+current.getData());
      	parent = current;
        current = current.getLeft();
        //System.out.println("Moving on to .. "+current.getData());
      }
    }
  	return false;
  }
}