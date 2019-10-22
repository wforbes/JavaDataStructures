package net.wforbes.javadatastructures.sort;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;


public class MergeSort {

    private static <T extends Comparable<T>> void printList(List<T> l) {
        System.out.print("{ ");
        for (T i : l) {
            System.out.print(i + ", ");
        }
        System.out.println(" }");
    }

    public static <T extends Comparable<T>> T[] sort(T[] inputArray) {
        //convert array to list for ease of use
        List<T> array = new ArrayList<>(Arrays.asList(inputArray));
        //printList(array);
        //merge sort the list
        List<T> sortedList = mergeSort(array);

        //put sorted list back into array
        for (int i = 0; i < sortedList.size(); i++) {
            inputArray[i] = sortedList.get(i);
        }

        //profit
        return inputArray;
    }

    public static <T extends Comparable<T>> List<T> mergeSort(List<T> array) {
        //base case.. if list is 1 or less, return, theres nothing to mergesort
        if (array.size() <= 1) {
            return array;
        } else {
            //System.out.println("mergeSort: ("+array.get(0)+" - "+array.get(array.size()/2)+") AND ("+array.get(array.size()/2)+" - "+array.get(array.size()-1)+")");
            return merge(
                    mergeSort(array.subList(0, array.size() / 2)),
                    mergeSort(array.subList((array.size() / 2), array.size()))
            );
        }
    }

    public static <T extends Comparable<T>> List<T> merge(List<T> first, List<T> second) {
        Iterator<T> firstIter = first.iterator();
        T firstItem = firstIter.next();

        Iterator<T> secondIter = second.iterator();
        T secondItem = secondIter.next();

        List<T> merged = new ArrayList<>();

        while (true) {
            //compare first and second
            //System.out.println("Comparing: ("+firstItem+" with "+secondItem+")");
            if (firstItem.compareTo(secondItem) < 0) {
                //if first item is less add it to merged
                //System.out.println(firstItem+" is less");
                merged.add(firstItem);
                //System.out.println("Merged is now... ");
                //printList(merged);
                //if theres another item in first list
                if (firstIter.hasNext()) {
                    //get it ready for next loop comparison
                    firstItem = firstIter.next();
                } else {
                    //if not, add the rest of second list to merged
                    //System.out.println("No more in first, adding "+secondItem+" and the rest of second to merged");
                    merged.add(secondItem);
                    while (secondIter.hasNext()) {
                        merged.add(secondIter.next());
                    }
                    //System.out.println("Merged is now...");
                    //printList(merged);
                    break;
                }
            } else {
                //if second is less, add it to merged
                //System.out.println(secondItem+" is less");
                merged.add(secondItem);
                //System.out.println("Merged is now... ");
                //printList(merged);
                //if theres another item in second list
                if (secondIter.hasNext()) {
                    //get it ready for next loop comparison
                    secondItem = secondIter.next();
                } else {
                    //if not, add the rest of the first list to merged
                    //System.out.println("No more in second, adding "+firstItem+" and the rest of first to merged");
                    merged.add(firstItem);
                    while (firstIter.hasNext()) {
                        merged.add(firstIter.next());
                    }
                    //System.out.println("Merged is now...");
                    //printList(merged);
                    break;
                }
            }
        }
        return merged;
    }
}