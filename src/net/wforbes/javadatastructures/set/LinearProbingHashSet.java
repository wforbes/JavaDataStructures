package net.wforbes.javadatastructures.set;

import java.util.ArrayList;
import java.util.Set;
import java.util.Iterator;
import java.util.Collection;

public class LinearProbingHashSet<E> implements Set<E> {

    private ArrayList<E> list;
    private final int initialSize = 10;
    private int listSize;
    private final double LOAD_FACTOR = 0.5;

    public LinearProbingHashSet() {
        this.listSize = 0;
        this.initializeList(new ArrayList<>(initialSize));
    }

    private void initializeList(ArrayList<E> list) {
        this.list = list;

        for(int i = 0; i < this.initialSize; i++){
            this.list.add(i, null);
        }

    }

    private int hash(E e) {
        return Math.abs(e.hashCode()) % list.size();
    }

    private void checkAndGrow(){
        if(this.listSize >= this.list.size() * LOAD_FACTOR){
            this.grow();
        }
    }

    private void grow(){
        ArrayList<E> items = new ArrayList<>(this.list);
        int currentListSize = this.list.size();
        this.list = new ArrayList<>(currentListSize * 2);
        this.listSize = 0;
        for(int i = 0; i < currentListSize*2; i++){
            this.list.add(i, null);
        }

        for(E item : items){
            if(item!=null){
                this.add(item);
            }
        }

    }

    private boolean probeInsert(int hash, E e) {
        if (this.contains(e)) {//contains a duplicate
            return false;
        }
        for (int i = hash + 1; i < this.list.size() - hash - 1; i++) {
            if (this.list.get(i) == null) {
                this.list.set(i, e);
                //this.checkAndGrow();
                this.listSize++;
                return true;
            }
        }

        for (int i = 0; i < hash; i++) {
            if (this.list.get(i) == null) {
                this.list.set(i, e);
                //this.checkAndGrow();
                this.listSize++;
                return true;
            }
        }

        return false;
    }

    //Adds the specified element to this set if it is not already present (optional operation).
    public boolean add(E e) {
        this.checkAndGrow();
        int hash = this.hash(e);//positional hash
        if (this.list.get(hash) != null) {//already element at that position
            if (this.probeInsert(hash, e)) {
                return true;
            }
        } else if (this.list.get(hash) == null) {//might not need this double null check
            this.list.set(hash, e);
            this.listSize++;
            return true;
        }

        return false;
    }

    //Adds all of the elements in the specified collection to this set if they're not already present (optional operation).
    public boolean addAll(Collection<? extends E> c) {
        boolean anyAdded = false;
        for(E e : c){
            anyAdded |= this.add(e);
        }
        return anyAdded;
    }

    //Removes all of the elements from this set (optional operation).
    public void clear() {

    }

    //Returns true if this set contains the specified element.
    @Override
    public boolean contains(Object o) {
        for(E e : this.list){
            if(e!=null && e.equals(o)){
                return true;
            }
        }
        return false;
    }

    //Returns true if this set contains all of the elements of the specified collection.
    public boolean containsAll(Collection<?> c) {
        for(Object o : c){
            if(!this.contains(o)){
                return false;
            }
        }
        return true;
    }

    //Compares the specified object with this set for equality.
    public boolean equals(Object o) {
        return this.equals(o);
    }

    //Returns the hash code value for this set.
    public int hashCode() {
        return this.hashCode();
    }

    //Returns true if this set contains no elements.
    public boolean isEmpty() {
        return false;
    }

    //Returns an iterator over the elements in this set.
    public Iterator<E> iterator() {
        ArrayList<E> newList = new ArrayList<>();

        for(int i = 0; i < this.list.size(); i++){
            if(this.list.get(i)!=null){
                newList.add(this.list.get(i));
            }
        }

//        for(E e : this.list){
//            if(e!=null){
//                newList.add(e);
//            }
//        }
        return newList.iterator();
    }

    //Removes the specified element from this set if it is present (optional operation).
    public boolean remove(Object o) {
        E e = (E)o;
        int hash = this.hash(e);//positional hash
        if (this.list.get(hash) != null) {
            if(this.list.get(hash).equals(e)){
                this.list.remove(hash);
                listSize--;
                return true;
            }else{
                for (int i = hash + 1; i < this.list.size() - hash - 1; i++) {
                    if (this.list.get(i).equals(e)) {
                        this.list.remove(hash);
                        listSize--;
                        return true;
                    }
                }

                for (int i = 0; i < hash; i++) {
                    if (this.list.get(i).equals(e)) {
                        this.list.remove(hash);
                        listSize--;
                        return true;
                    }
                }
            }
        }
        return false;
    }

    //Removes from this set all of its elements that are contained in the specified collection (optional operation).
    public boolean removeAll(Collection<?> c) {
        return false;
    }

    //Retains only the elements in this set that are contained in the specified collection (optional operation).
    public boolean retainAll(Collection<?> c) {
        return false;
    }

    //Returns the number of elements in this set (its cardinality).
    public int size() {
        return this.listSize;
    }

    //Returns an array containing all of the elements in this set.
    public Object[] toArray() {
        ArrayList<E> newList = new ArrayList<>();
        for(E e : this.list){
            if(e!=null){
                newList.add(e);
            }
        }
        return newList.toArray();
    }

    //Returns an array containing all of the elements in this set; the runtime type of the returned array is that of the specified array.
    public <T> T[] toArray(T[] a) {
        ArrayList<T> newList = new ArrayList<>();
        for(E e : this.list){
            if(e!=null){
                newList.add((T)e);
            }
        }
        return newList.toArray(a);
    }
}
