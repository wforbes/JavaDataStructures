package net.wforbes.javadatastructures.tree;

import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

public class BinarySearchTreeUnitTest {


	private <E extends Comparable<E>> BinarySearchTreeInterface<E> getTree() {
		return new BinarySearchTree<>();
	}
	
	private <T> void assertEquals(String message, T expected, T actual) {
		assertEquals(message, expected, actual, null);
	}
	
	private <T, E extends Comparable<E>> void assertEquals(String message, T expected, T actual, BinarySearchTreeInterface<E> tree) {
		if(expected == null && actual == null) {
			return;
		}
		if(expected == null|| !expected.equals(actual)) {
			String fullMessage = message + "\n\tExpected: " + expected + "\n\tActual: " + actual;
			if(tree != null) {
				fullMessage += "\nTree:\n" + treeToString(tree);
			}
			throw new AssertionError(fullMessage);
		}
	}
	
	private <E extends Comparable<E>> void assertEquals(String message, BinarySearchTreeInterface<E> expected, BinarySearchTreeInterface<E> actual) {
		if(!treeNodesEqual(expected.getRoot(), actual.getRoot())) {
			throw new AssertionError(message + "\n\tExpected:\n" + treeToString(expected) + 
					"\n\tActual:\n" + treeToString(actual));
		}
	}
	
	private <E extends Comparable<E>> boolean treeNodesEqual(BinarySearchTreeNodeInterface<E> expected, BinarySearchTreeNodeInterface<E> actual) {
		if((expected == null) != (actual == null)) {
			return false;
		}
		if(expected == null) {
			return true;
		}
		if(!expected.getData().equals(actual.getData())) {
			return false;
		}
		return treeNodesEqual(expected.getLeft(), actual.getLeft()) &&
				treeNodesEqual(expected.getRight(), actual.getRight());
	}
	
	private <E extends Comparable<E>> String treeToString(BinarySearchTreeInterface<E> tree) {
		if(tree.getRoot() == null) {
			return "Empty Tree";
		}
		else {
			StringBuilder output = new StringBuilder();
			List<? extends List<BinarySearchTreeNodeInterface<E>>> grid = treeToGrid(tree.getRoot());
			
			List<Integer> columnWidths = new ArrayList<>(grid.get(0).size());
			for(int c = 0; c < grid.get(0).size(); c++) {
				int maxSize = 0;
				for(int r = 0; r < grid.size(); r++) {
					BinarySearchTreeNodeInterface<E> node = grid.get(r).get(c);
					int length = (node == null) ? 0 : node.getData().toString().length();
					if(length > maxSize) {
						maxSize = length;
					}
				}
				columnWidths.add(maxSize);
			}
			
			for(int r = 0; r < grid.size(); r++) {
				List<BinarySearchTreeNodeInterface<E>> row = grid.get(r);
				
				for(int c = 0; c < row.size(); c++) {
					BinarySearchTreeNodeInterface<E> cell = row.get(c);
					String cellValue;
					if(cell == null) {
						cellValue = "";
					}
					else {
						cellValue = cell.getData().toString();
					}
					
					for(int i = cellValue.length(); i < columnWidths.get(c); i++) {
						cellValue += " ";
					}
					
					output.append(cellValue);
				}
				output.append("\n");
				
				//Now do the edge lines
				if(r + 1 < grid.size()) {
					int curIndex = 0;
					List<BinarySearchTreeNodeInterface<E>> nextRow = grid.get(r+1);
					for(int i = 0; i < row.size(); i++) {
						if(row.get(i) != null) {
							if(row.get(i).getLeft() != null) {
								//Find what column its left child is in
								int leftCol = findIndex(nextRow, row.get(i).getLeft(), columnWidths);
								String edge = getEdgeLine(curIndex, colToIndex(i, columnWidths), leftCol);
								curIndex += edge.length();
								output.append(edge);
							}
							
							if(row.get(i).getRight() != null) {
								int rightCol = findIndex(nextRow, row.get(i).getRight(), columnWidths);
								String edge = getEdgeLine(curIndex, colToIndex(i, columnWidths), rightCol);
								curIndex += edge.length();
								output.append(edge);
							}
						}
					}
					output.append("\n");
				}
				
			}
			return output.toString();
		}
	}
	
	private String getEdgeLine(int startCol, int parentIndex, int childIndex) {
		int diff = Math.abs(parentIndex - childIndex);
		
		StringBuilder sb = new StringBuilder();
		
		int leftPadding = Math.min(parentIndex, childIndex) - startCol;
		
		for(int i = 0; i < diff - 1; i++) {
			sb.append("_");
		}
		
		if(parentIndex > childIndex) {
			sb.append("/");
		}
		else {
			sb.insert(0,  "\\");
		}
		
		for(int i = 0; i < leftPadding; i++) {
			sb.insert(0, " ");
		}
		
		
		return sb.toString();
	}
	
	private <E extends Comparable<E>> int findIndex(List<BinarySearchTreeNodeInterface<E>> nextRow, 
							BinarySearchTreeNodeInterface<?> node,
							List<Integer> colWidths) {
		int col = -1;
		for(int i = 0; i < nextRow.size(); i++) {
			if(nextRow.get(i) == node) {
				col = i;
				break;
			}
		}
		
		return colToIndex(col, colWidths);
	}
	
	private int colToIndex(int col, List<Integer> colWidths) {
		int index = 0;
		for(int i = 0; i < col; i++) {
			index += colWidths.get(i);
		}
		return index;
	}
	
	private <E extends Comparable<E>> 
	List<? extends List<BinarySearchTreeNodeInterface<E>>> treeToGrid(BinarySearchTreeNodeInterface<E> node) {
		List<List<BinarySearchTreeNodeInterface<E>>> grid = new LinkedList<>();
		List<BinarySearchTreeNodeInterface<E>> me = new LinkedList<>();
		me.add(node);
		grid.add(me);
		
		int cellsToTheLeft = 0;
		
		List<? extends List<BinarySearchTreeNodeInterface<E>>> left = Collections.emptyList();
		if(node.getLeft() != null) {
			left = treeToGrid(node.getLeft());
			cellsToTheLeft = left.get(0).size();
			for(int i = 0; i < cellsToTheLeft; i ++) {
				me.add(0, null);
			}
			
			grid.addAll(left);
		}
		
		if(node.getRight() != null) {
			List<? extends List<BinarySearchTreeNodeInterface<E>>> right = treeToGrid(node.getRight());
			for(int r = 0; r < right.size(); r++) {
				List<BinarySearchTreeNodeInterface<E>> rowToAddTo;
				if(grid.size() > r + 1) {
					rowToAddTo = grid.get(r + 1);
				}
				else {
					rowToAddTo = new LinkedList<>();
					for(int i =0; i < cellsToTheLeft; i++) {
						rowToAddTo.add(null);
					}
					grid.add(rowToAddTo);
				}
				rowToAddTo.add(null);
				rowToAddTo.addAll(right.get(r));
				
			}
		}
		
		//make it a uniform grid
		int longestRow = 0;
		for(List<BinarySearchTreeNodeInterface<E>> row : grid) {
			if(row.size() > longestRow) {
				longestRow = row.size();
			}
		}
		
		for(List<BinarySearchTreeNodeInterface<E>> row : grid) {
			for(int i = row.size(); i < longestRow; i++) {
				row.add(null);
			}
		}
		
		return grid;
	}
	
	/**
	 * Quick utility class for generating expected trees for comparisons in tests
	 * @author nate
	 *
	 * @param <E>
	 */
	private static class TreeStructure<E extends Comparable<E>> implements BinarySearchTreeNodeInterface<E> {

		private E data;
		private TreeStructure<E> left;
		private TreeStructure<E> right;
		
		private TreeStructure(E data) {
			this.data = data;
		}
		
		@Override
		public E getData() {
			return data;
		}

		@Override
		public BinarySearchTreeNodeInterface<E> getLeft() {
			return left;
		}

		@Override
		public BinarySearchTreeNodeInterface<E> getRight() {
			return right;
		}
		
		private void add(E valToAdd) {
			if(valToAdd.compareTo(this.data) < 0) {
				if(left == null) {
					this.left = new TreeStructure<>(valToAdd);
				}
				else {
					left.add(valToAdd);
				}
			}
			else if(valToAdd.compareTo(this.data) > 0) {
				if(right == null) {
					this.right = new TreeStructure<>(valToAdd);
				}
				else {
					right.add(valToAdd);
				}
			}
		}
		
		@SafeVarargs
		public static <E extends Comparable<E>> BinarySearchTreeInterface<E> build(E... values) {
			
			TreeStructure<E>  tree = null;
			if(values.length > 0) {
				tree = new TreeStructure<>(values[0]);
				for(int i = 1; i < values.length; i++) {
					tree.add(values[i]);
				}
			}
			
			final TreeStructure<E> finalTree = tree;
			
			return new BinarySearchTreeInterface<E>() {

				@Override
				public BinarySearchTreeNodeInterface<E> getRoot() {
					return finalTree;
				}

				@Override public boolean add(E value) {return false;}
				@Override public boolean contains(E value) {return false;}
				@Override public void clear() {}
				@Override public int size() {return 0;}
				@Override public int height() {return 0;}
				@Override public List<E> getDataInOrder() {return null;}
				@Override public List<E> getDataPreOrder() {return null;}
				@Override public List<E> getDataPostOrder() {return null;}
				@Override public boolean remove(E value) {return false;}
			};
		}
		
	}
	
	
	////////////////////////////////////////////////////////////////////
	//
	//				TESTS
	//
	////////////////////////////////////////////////////////////////////
	
	private void addNonDuplicatesIncreasesSize() {
		BinarySearchTreeInterface<String> tree = getTree();
		assertEquals("Initial size of tree should be 0", 0, tree.size());
		tree.add("foobar");
		assertEquals("After adding one element, tree size should be 1", 1, tree.size());
		tree.add("catapult");
		tree.add("french fries");
		tree.add("ardvark");
		tree.add("barbeque");
		tree.add("rasputin");
		assertEquals("After adding 6 itmes, tree size should be 6", 6, tree.size(), tree);
	}
	
	public void addDuplicatesDoesNotIncreaseSize() {
		BinarySearchTreeInterface<String> tree = getTree();
		tree.add("F");
		tree.add("G");
		tree.add("G");
		assertEquals("After adding two unique items and one duplicate, size should be 2",
				2, tree.size(), tree);
	}
	
	public void addReturnCodes() {
		BinarySearchTreeInterface<Integer> tree = getTree();
		
		assertEquals("Add first node should return true", true, tree.add(5), tree);
		assertEquals("Add duplicate of root should return false", false, tree.add(5), tree);
		assertEquals("Add unique value should return true", true, tree.add(1), tree);
		assertEquals("Add unique value should return true", true, tree.add(4), tree);
		assertEquals("Add unique value should return true", true, tree.add(-23), tree);
		assertEquals("Add unique value should return true", true, tree.add(34), tree);
		assertEquals("Add unique value should return true", true, tree.add(23), tree);
		assertEquals("Add duplicate should return false", false, tree.add(4), tree);
		assertEquals("Add duplicate should return false", false, tree.add(34), tree);
	}
	
	public void addTreeStructure() {
		BinarySearchTreeInterface<Integer> tree = getTree();
		tree.add(45);
		tree.add(23);
		tree.add(96);
		tree.add(231);
		tree.add(10);
		tree.add(35);
		tree.add(0);
		tree.add(12);
		tree.add(100);
		assertEquals("Adding several values should result in BST structure",
				TreeStructure.build(45, 23, 96, 231, 10, 35, 0, 12, 100),
				tree);
		
	}
	
	public void testContains() {
		BinarySearchTreeInterface<Integer> tree = getTree();
		tree.add(56);
		tree.add(32);
		tree.add(12);
		tree.add(85);
		tree.add(23);
		tree.add(96);
		
		assertEquals("Contains should return when item is root. Item: " + 56, 
				true, tree.contains(56), tree);
		assertEquals("Contains should return true on item that is present. Item: " + 32, 
				true, tree.contains(32), tree);
		assertEquals("Contains should return true on item that is present. Item: " + 12, 
				true, tree.contains(12), tree);
		assertEquals("Contains should return true on item that is present. Item: " + 85, 
				true, tree.contains(85), tree);
		assertEquals("Contains should return true on item that is present. Item: " + 23, 
				true, tree.contains(23), tree);
		assertEquals("Contains should return false on item that is not present. Item: " + 95, 
				false, tree.contains(95), tree);
		
	}
	
	public void testClear() {
		BinarySearchTreeInterface<Integer> tree = getTree();
		tree.add(56);
		tree.add(32);
		tree.add(12);
		tree.add(85);
		tree.clear();
		
		assertEquals("Cleared tree should have size 0", 0, tree.size());
		assertEquals("Cleared tree should have null root", null, tree.getRoot());
	}
	
	public void testHeight() {
		BinarySearchTreeInterface<Integer> tree = getTree();
		
		assertEquals("Empty tree should have 0 height", 0, tree.height());
		tree.add(65);
		assertEquals("1 element tree should have 0 height", 0, tree.height());
		tree.add(42);
		assertEquals("Incorrect height value", 1, tree.height(), tree);
		tree.add(75);
		assertEquals("Incorrect height value", 1, tree.height(), tree);
		tree.add(32);
		assertEquals("Incorrect height value", 2, tree.height(), tree);
		tree.add(50);
		assertEquals("Incorrect height value", 2, tree.height(), tree);
		tree.add(12);
		assertEquals("Incorrect height value", 3, tree.height(), tree);
		tree.add(-18);
		assertEquals("Incorrect height value", 4, tree.height(), tree);
	}
	
	public void testInOrderTraversal() {
		BinarySearchTreeInterface<Integer> tree = getTree();
		
		assertEquals("Empty tree in-order should return empty list", 
				Collections.emptyList(), tree.getDataInOrder(), tree);
		
		tree.add(87);
		assertEquals("Single element in-order should return just that element", 
				Collections.singletonList(87), tree.getDataInOrder(), tree);
		
		tree.add(34);
		tree.add(23);
		tree.add(65);
		tree.add(452);
		tree.add(453);
		tree.add(234);
		tree.add(89);
		tree.add(79);
		tree.add(79);
		List<Integer> expected = new LinkedList<>();
		expected.add(23);
		expected.add(34);
		expected.add(65);
		expected.add(79);
		expected.add(87);
		expected.add(89);
		expected.add(234);
		expected.add(452);
		expected.add(453);
		
		assertEquals("Invalid in-order traversal", expected, tree.getDataInOrder(), tree);	
	}
	
	public void testPreOrderTraversal() {
		BinarySearchTreeInterface<Integer> tree = getTree();
		
		assertEquals("Empty tree pre-order should return empty list", 
				Collections.emptyList(), tree.getDataPreOrder(), tree);
		
		tree.add(87);
		assertEquals("Single element pre-order should return just that element", 
				Collections.singletonList(87), tree.getDataPreOrder(), tree);
		
		tree.add(34);
		tree.add(23);
		tree.add(65);
		tree.add(452);
		tree.add(453);
		tree.add(234);
		tree.add(89);
		tree.add(79);
		tree.add(79);
		List<Integer> expected = new LinkedList<>();
		expected.add(87);
		expected.add(34);
		expected.add(23);
		expected.add(65);
		expected.add(79);
		expected.add(452);
		expected.add(234);
		expected.add(89);
		expected.add(453);
		
		assertEquals("Invalid pre-order traversal", expected, tree.getDataPreOrder(), tree);
	}
	
	public void testPostOrderTraversal() {
		BinarySearchTreeInterface<Integer> tree = getTree();
		
		assertEquals("Empty tree post-order should return empty list", 
				Collections.emptyList(), tree.getDataPostOrder(), tree);
		
		tree.add(87);
		assertEquals("Single element post-order should return just that element", 
				Collections.singletonList(87), tree.getDataPostOrder(), tree);
		
		tree.add(34);
		tree.add(23);
		tree.add(65);
		tree.add(452);
		tree.add(453);
		tree.add(234);
		tree.add(89);
		tree.add(79);
		tree.add(79);
		List<Integer> expected = new LinkedList<>();
		expected.add(23);
		expected.add(79);
		expected.add(65);
		expected.add(34);
		expected.add(89);
		expected.add(234);
		expected.add(453);
		expected.add(452);
		expected.add(87);
		
		assertEquals("Invalid post-order traversal", expected, tree.getDataPostOrder(), tree);
	}
	
	public void removeLeafNode() {
		BinarySearchTreeInterface<Integer> tree = getTree();
		
		tree.add(56);
		tree.add(67);
		tree.remove(67);
		assertEquals("Incorrect tree after removal", TreeStructure.build(56), tree);
	}
	
	public void removeInnerNodeWith1Child() {
		BinarySearchTreeInterface<Integer> tree = getTree();
		
		tree.add(3);
		tree.add(6);
		tree.add(8);
		tree.add(7);
		tree.add(9);
		tree.remove(6);
		assertEquals("Incorrect tree after removal", TreeStructure.build(3,8,7,9), tree);
	}
	
	public void removeInnerNodeWith2Children() {

		BinarySearchTreeInterface<Integer> tree = getTree();
		tree.add(1);
		tree.add(2);
		tree.add(20);
		tree.add(15);
		tree.add(30);
		tree.add(25);
		tree.add(23);
		tree.add(24);
		tree.remove(20);
		assertEquals("Incorrect tree after removal", TreeStructure.build(1,2,23,15,30,25,24), tree);
		
	}
	
	public void removeRoot() {
		BinarySearchTreeInterface<Integer> tree = getTree();
		tree.add(45);
		tree.remove(45);
		assertEquals("Incorrect tree after removal of root in 1 element tree", TreeStructure.build(), tree);
		
		tree.add(67);
		tree.add(34);
		tree.add(45);
		tree.remove(67);
		assertEquals("Incorrect tree after removal of root with one child", TreeStructure.build(34, 45), tree);
		
		
		tree.add(12);
		tree.remove(34);
		assertEquals("Incorrect tree after removal of root with 2 children", TreeStructure.build(45, 12), tree);
	}
	public void a() {
		
	}
    public boolean passed(PrintWriter out) {
        
    	Map<String, Runnable> tests = new LinkedHashMap<>();
    	tests.put("Adding non-duplicates increases size", this::addNonDuplicatesIncreasesSize);
    	tests.put("Adding duplicates doesn't increase size", this::addDuplicatesDoesNotIncreaseSize);
    	tests.put("Test all return values for add method", this::addReturnCodes);
    	tests.put("Test tree structure after several adds", this::addTreeStructure);
    	tests.put("Test contains method", this::testContains);
    	tests.put("Test clear method", this::testClear);
    	tests.put("Test height", this::testHeight);
    	tests.put("Test in-order traversal", this::testInOrderTraversal);
    	tests.put("Test pre-order traversal", this::testPreOrderTraversal);
    	tests.put("Test post-order traversal", this::testPostOrderTraversal);
    	tests.put("Test remove leaf node", this::removeLeafNode);
    	tests.put("Test remove inner node with 1 child", this::removeInnerNodeWith1Child);
    	tests.put("Test remove inner node with 2 children", this::removeInnerNodeWith2Children);
    	tests.put("Remove root (every root removal case)", this::removeRoot);
    	
    	int numFailures = 0;
    	
    	for(String test : tests.keySet()) {
    		try {
    			System.out.println("Running " + test);
    			tests.get(test).run();
        	}
        	catch(AssertionError err) {
        		out.println(err.getMessage());
        		out.flush();
        		System.err.println("\tTest Failed");
        		numFailures++;
        	}
    		catch(Throwable err) {
    			err.printStackTrace(out);
    			out.flush();
    			System.err.println("\tTest Failed");
        		numFailures++;
    		}
    		
    		//Sleep a tiny bit to ensure that stderr output finishes printing
    		//before we write more the stdout
    		try {
				Thread.sleep(100);
			} catch (InterruptedException e) {}
    	}
    	
        System.out.println(numFailures + " failures out of " + tests.size() + " tests");
        return numFailures == 0;
    }
    
    public static void main(String args[]) {
    	
    	BinarySearchTreeUnitTest test = new BinarySearchTreeUnitTest();
    	
    	PrintWriter output = new PrintWriter(System.err);
    	if(test.passed(output)) {
    		System.out.println("Passed");
    	}
    	else 
    		System.out.println("Failed");
    	//output.flush();
    }
}
