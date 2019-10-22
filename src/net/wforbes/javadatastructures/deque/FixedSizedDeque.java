package net.wforbes.javadatastructures.deque;

import java.util.*;

public class FixedSizedDeque<E> implements Deque<E> {

    private int sizeLimit;
    private LinkedList<E> list;

    public FixedSizedDeque(int sizeLimit){
        this.sizeLimit = sizeLimit;
        list = new LinkedList<>();
    }

    @Override
    public void addFirst(Object o) {
        if(list.size() < sizeLimit) {
            list.addFirst((E) o);
        }else{
            throw new IllegalStateException();
        }
    }

    @Override
    public void addLast(Object o) {
        if(list.size() < sizeLimit) {
            list.addLast((E) o);
        }else{
            throw new IllegalStateException();
        }
    }

    @Override
    public boolean offerFirst(Object o) {
        if(list.size() < sizeLimit) {
            return list.offerFirst((E) o);
        }
        return false;
    }

    @Override
    public boolean offerLast(Object o) {
        if(list.size() < sizeLimit) {
            return list.offerLast((E) o);
        }
        return false;
    }

    @Override
    public E removeFirst() {
        return list.removeFirst();
    }

    @Override
    public E removeLast() {
        return list.removeLast();
    }

    @Override
    public E pollFirst() {
        return list.pollFirst();
    }

    @Override
    public E pollLast() {
        return list.pollLast();
    }

    @Override
    public E getFirst() {
        return list.getFirst();
    }

    @Override
    public E getLast() {
        return list.getLast();
    }

    @Override
    public E peekFirst() {
        return list.peekFirst();
    }

    @Override
    public E peekLast() {
        return list.peekLast();
    }

    @Override
    public boolean removeFirstOccurrence(Object o) {
        return list.removeFirstOccurrence(o);
    }

    @Override
    public boolean removeLastOccurrence(Object o) {
        return list.removeLastOccurrence(o);
    }

    @Override
    public boolean add(Object o) {
        if(list.size() < sizeLimit) {

            return list.add((E) o);
        }else{
            throw new IllegalStateException();
        }
        //return false;
    }

    @Override
    public boolean offer(Object o) {
        if(list.size() < sizeLimit) {
            return list.offer((E) o);
        }
        return false;
    }

    @Override
    public E remove() {
        try {
            return list.remove();
        }catch(NoSuchElementException e){
            throw new NoSuchElementException();
        }
    }

    @Override
    public E poll() {
        return list.poll();
    }

    @Override
    public E element() {
        return list.element();
    }

    @Override
    public E peek() {
        return list.peek();
    }

    @Override
    public boolean addAll(Collection c) {
        boolean addedAny = false;
        for(Object o : c){
            addedAny |= this.add(o);
        }
        return addedAny;
        //return list.addAll(c);
    }

    @Override
    public void clear() {
        list.clear();
    }

    @Override
    public boolean retainAll(Collection c) {
        return list.retainAll(c);
    }

    @Override
    public boolean removeAll(Collection c) {
        return list.removeAll(c);
    }

    @Override
    public void push(Object o) {
        list.push((E) o);
    }

    @Override
    public E pop() {
        return list.pop();
    }

    @Override
    public boolean remove(Object o) {
        return list.remove(o);
    }

    @Override
    public boolean containsAll(Collection c) {
        return list.containsAll(c);
    }

    @Override
    public boolean contains(Object o) {
        return list.contains(o);
    }

    @Override
    public int size() {
        return list.size();
    }

    @Override
    public boolean isEmpty() {
        return list.isEmpty();
    }

    @Override
    public Iterator<E> iterator() {
        return list.iterator();
    }

    @Override
    public Object[] toArray() {
        return list.toArray();
    }

    @Override
    public <T> T[] toArray(T[] a) {
        return list.toArray(a);
    }

    @Override
    public Iterator<E> descendingIterator() {
        return list.descendingIterator();
    }
}
