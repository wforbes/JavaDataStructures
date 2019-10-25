package net.wforbes.javadatastructures.tree;

import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

public class AVLTreeUnitTest {


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
					int length = (node == null) ? 0 : nodeToString(node).length();
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
						cellValue = nodeToString(cell);
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
	
	private <E extends Comparable<E>> String nodeToString(BinarySearchTreeNodeInterface<E> node) {
		return node.getData().toString()+"-H=" + node.getHeight() + ",B=" + node.getBalanceFactor();
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

		@Override
		public int getHeight() {
			int leftHeight = getHeight(left);
			int rightHeight = getHeight(right);
			
			return 1 + Math.max(leftHeight, rightHeight);
			
		}

		@Override
		public void setHeight(int height) {

		}

		private int getHeight(TreeStructure<E> node) {
			if(node == null) {
				return -1;
			}
			else
				return node.getHeight();
		}

		@Override
		public int getBalanceFactor() {
			return getHeight(left) - getHeight(right);
		}

		//TODO: double check the functionality here, unsure what item to set the factor on
		//		or if i need to at all... I notice setheight is empty, too
		@Override
		public void setBalanceFactor(int balanceFactor){

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
	
	public void testHeightWithRotations() {
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
		tree.add(12);
		assertEquals("Incorrect height value after expected rotation", 2, tree.height(), tree);
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
		tree.add(2);
		tree.add(7);
		tree.remove(6);
		assertEquals("Incorrect tree after removal", TreeStructure.build(3,2,7), tree);
	}
	
	public void removeInnerNodeWith2Children() {

		BinarySearchTreeInterface<Integer> tree = getTree();
		tree.add(0);
		tree.add(-20);
		tree.add(10);
		tree.add(-30);
		tree.add(-10);
		tree.add(5);
		tree.add(30);
		tree.add(-40);
		tree.add(-25);
		tree.add(2);
		tree.add(7);
		tree.add(20);
		tree.add(35);
		tree.add(1);
		tree.add(15);
		tree.add(25);
		tree.add(40);
		tree.add(-15);
		tree.add(-45);
		tree.add(18);
		//Its such a big tree, we should check before the removal as well
		assertEquals("Incorrect tree prior to removal",
				TreeStructure.build(0, -20, 10, -30, -10, 5, 30, -40, -25, -15, 2, 7, 20, 35, -45, 1, 15, 25, 40, 18),
				tree);
		tree.remove(10);
		assertEquals("Incorrect tree after removal", 
				TreeStructure.build(0, -20, 15, -30, -10, 5, 30, -40, -25, -15, 2, 7, 20, 35, -45, 1, 18, 25, 40), 
				tree);
		
	}
	
	public void removeRoot() {
		BinarySearchTreeInterface<Integer> tree = getTree();
		tree.add(45);
		tree.remove(45);
		assertEquals("Incorrect tree after removal of root in 1 element tree", TreeStructure.build(), tree);
		
		tree.add(67);
		tree.add(34);
		tree.remove(67);
		assertEquals("Incorrect tree after removal of root with one child", TreeStructure.build(34), tree);
		
		
		tree.add(12);
		tree.add(100);
		tree.remove(34);
		assertEquals("Incorrect tree after removal of root with 2 children", TreeStructure.build(100, 12), tree);
	}
	
	//AVL specific tests
	
	public void testHeights() {
		BinarySearchTreeInterface<Integer> tree = getTree();
		
		tree.add(0);
		tree.add(-1);
		tree.add(2);
		tree.add(3);
		tree.add(1);
		
		BinarySearchTreeNodeInterface<Integer> root = tree.getRoot();
		assertEquals("Root node height should be 2", 2, root.getHeight(), tree);
		assertEquals("Root's left's height should be 0", 0, root.getLeft().getHeight(), tree);
		assertEquals("Root's right's height should be 1", 1, root.getRight().getHeight(), tree);	
	}
	
	public void testBalanceFactors() {
		BinarySearchTreeInterface<Integer> tree = getTree();
		
		tree.add(0);
		tree.add(-5);
		tree.add(10);
		tree.add(-10);
		tree.add(-2);
		tree.add(8);
		tree.add(15);
		tree.add(6);
		
		BinarySearchTreeNodeInterface<Integer> root = tree.getRoot();
		assertEquals("Root's balance factor should be -1", -1, root.getBalanceFactor(), tree);
		assertEquals("Root's left's balance factor should be 0", 0, root.getLeft().getBalanceFactor(), tree);
		assertEquals("Root's right's balance factor should be 1", 1, root.getRight().getBalanceFactor(), tree);
		assertEquals("Root's  right's right's balance factor should be 0", 0, root.getRight().getRight().getBalanceFactor(), tree);	
		assertEquals("Root's  right's left balance factor should be 1", 1, root.getRight().getLeft().getBalanceFactor(), tree);	
	}
	
	public void leftLeftRotation() {
		BinarySearchTreeInterface<Integer> tree = getTree();
		tree.add(5);
		tree.add(6);
		tree.add(4);
		tree.add(2);
		tree.add(0);
		
		assertEquals("Incorrect tree after left-left rotation", TreeStructure.build(5, 2, 6, 0, 4), tree);
	}
	
	public void leftRightRotation() {
		
		BinarySearchTreeInterface<Integer> tree = getTree();
		tree.add(5);
		tree.add(6);
		tree.add(4);
		tree.add(0);
		tree.add(2);
		assertEquals("Incorrect tree after left-right rotation", TreeStructure.build(5, 2, 6, 0, 4), tree);
	}
	
	public void rightRightRotation() {
		BinarySearchTreeInterface<Integer> tree = getTree();
		tree.add(5);
		tree.add(6);
		tree.add(4);
		tree.add(8);
		tree.add(10);
		assertEquals("Incorrect tree after left-right rotation", TreeStructure.build(5, 4, 8, 6, 10), tree);
	}
	
	public void rightLeftRotation() {
		BinarySearchTreeInterface<Integer> tree = getTree();
		tree.add(5);
		tree.add(6);
		tree.add(4);
		tree.add(10);
		tree.add(8);
		assertEquals("Incorrect tree after left-right rotation", TreeStructure.build(5, 4, 8, 6, 10), tree);
	}
	
	public void leftLeftRotationWithChildren() {
		BinarySearchTreeInterface<Integer> tree = getTree();

		tree.add(0);
		tree.add(5);
		tree.add(-5);
		tree.add(10);
		tree.add(2);
		tree.add(-4);
		tree.add(-10);
		tree.add(15);
		tree.add(-3);
		tree.add(-7);
		tree.add(-20);
		tree.add(-6);
		tree.add(-15);
		tree.add(-30);
		//Since this is a pretty big tree, we'll do a check here before any rotations take place
		assertEquals("Incorrect tree prior to required rotation",
				TreeStructure.build( 0, 5, -5, 10, 2, -4, -10, 15, -3, -7, -20, -6, -15, -30),
				tree);
		tree.add(-35);
		assertEquals("Incorrect tree after left-left rotation with children", 
				TreeStructure.build(0, 5, -10, 10, 2, -5, -20, 15, -4, -7, -15, -30, 15, -3, -6, -35),
				tree);
	}
	
	public void leftRightRotationWithChildren() {
		BinarySearchTreeInterface<Integer> tree = getTree();

		tree.add(0);
		tree.add(-5);
		tree.add(10);
		tree.add(-10);
		tree.add(-2);
		tree.add(5);
		tree.add(-15);
		tree.add(-7);
		tree.add(-1);
		tree.add(20);
		tree.add(30);
		tree.add(-20);
		tree.add(-8);
		tree.add(-6);
		//Since this is a pretty big tree, we'll do a check here before any rotations take place
		assertEquals("Incorrect tree prior to required rotation",
				TreeStructure.build( 0, -5, 10, -10, -2, 5, -15, -7, -1, 20, 30, -20, -8, -6),
				tree);
		tree.add(-9);
		assertEquals("Incorrect tree after left-right rotation with children", 
				TreeStructure.build(0, -7, 10, -10, -5, 5, 20, -15, -8, -6, -2, 30, -20, -9, -1),
				tree);
		
		
	}
	
	public void rightRightRotationWithChildren() {
		BinarySearchTreeInterface<Integer> tree = getTree();

		tree.add(0);
		tree.add(-5);
		tree.add(5);
		tree.add(-10);
		tree.add(-2);
		tree.add(4);
		tree.add(10);
		tree.add(-15);
		tree.add(3);
		tree.add(7);
		tree.add(20);
		tree.add(6);
		tree.add(15);
		tree.add(30);
		//Since this is a pretty big tree, we'll do a check here before any rotations take place
		assertEquals("Incorrect tree prior to required rotation",
				TreeStructure.build( 0, -5, 5, -10, -2, 4, 10, -15, 3, 7, 20, 6, 15, 30),
				tree);
		tree.add(35);
		assertEquals("Incorrect tree after right-right rotation with children", 
				TreeStructure.build(0, -5, 10, -10, -2, 5, 20, -15, 4, 7, 15, 30, -15, 3, 6, 35),
				tree);
		
	}
	
	public void rightLeftRotationWithChildren() {
		BinarySearchTreeInterface<Integer> tree = getTree();

		tree.add(0);
		tree.add(5);
		tree.add(-10);
		tree.add(10);
		tree.add(2);
		tree.add(-5);
		tree.add(15);
		tree.add(7);
		tree.add(1);
		tree.add(-20);
		tree.add(-30);
		tree.add(20);
		tree.add(8);
		tree.add(6);
		//Since this is a pretty big tree, we'll do a check here before any rotations take place
		assertEquals("Incorrect tree prior to required rotation",
				TreeStructure.build( 0, 5, -10, 10, 2, -5, 15, 7, 1, -20, -30, 20, 8, 6),
				tree);
		tree.add(9);
		assertEquals("Incorrect tree after left-right rotation with children", 
				TreeStructure.build(0, 7, -10, 10, 5, -5, -20, 15, 8, 6, 2, -30, 20, 9, 1),
				tree);
		
	}
	
	public void rotationOnRoot() {
		BinarySearchTreeInterface<Integer> tree = getTree();

		tree.add(3);
		tree.add(2);
		tree.add(1);
		assertEquals("Incorrect tree after left-left rotation on root", TreeStructure.build(2, 1, 3), tree);

		tree.add(1);
		tree.add(2);
		tree.add(3);
		assertEquals("Incorrect tree after right-right rotation on root", TreeStructure.build(2, 1, 3), tree);

		tree.add(3);
		tree.add(1);
		tree.add(2);
		assertEquals("Incorrect tree after left-right rotation on root", TreeStructure.build(2, 1, 3), tree);

		tree.add(1);
		tree.add(3);
		tree.add(2);
		assertEquals("Incorrect tree after right-left rotation on root", TreeStructure.build(2, 1, 3), tree);
	}

	public void removeLeftLeftRotation() {
		BinarySearchTreeInterface<Integer> tree = getTree();

		tree.add(0);
		tree.add(-10);
		tree.add(10);
		tree.add(-20);
		tree.remove(10);
		
		assertEquals("Incorrect tree after remove with left-left rotation", TreeStructure.build(-10, 0, -20), tree);
	}
	
	public void removeLeftRightRotation() {
		BinarySearchTreeInterface<Integer> tree = getTree();

		tree.add(0);
		tree.add(10);
		tree.add(-10);
		tree.add(15);
		tree.add(-5);
		tree.add(-12);
		tree.add(-3);
		tree.add(-7);
		tree.remove(15);
		
		assertEquals("Incorrect tree after remove with left-right rotation", 
				TreeStructure.build(-5, 0, -10, 10, -3, -7, -12), tree);
	}
	
	public void removeRightRightRotation() {
		BinarySearchTreeInterface<Integer> tree = getTree();

		tree.add(0);
		tree.add(-10);
		tree.add(10);
		tree.add(20);
		tree.remove(-10);
		
		assertEquals("Incorrect tree after remove with right-right rotation", TreeStructure.build(10, 0, 20), tree);
	}
	
	public void removeRightLeftRotation() {
		BinarySearchTreeInterface<Integer> tree = getTree();

		tree.add(0);
		tree.add(-10);
		tree.add(10);
		tree.add(-15);
		tree.add(5);
		tree.add(12);
		tree.add(3);
		tree.add(7);
		tree.remove(-15);
		
		assertEquals("Incorrect tree after remove with right-left rotation", 
				TreeStructure.build(5, 0, 10, -10, 3, 7, 12), tree);
	}
	
	public void removeWithMultipleRotations() {
		BinarySearchTreeInterface<Integer> tree = getTree();

		tree.add(20);
		tree.add(10);
		tree.add(40);
		tree.add(5);
		tree.add(15);
		tree.add(30);
		tree.add(60);
		tree.add(3);
		tree.add(7);
		tree.add(14);
		tree.add(17);
		tree.add(25);
		tree.add(50);
		tree.add(70);
		tree.add(2);
		tree.add(4);
		tree.add(8);
		tree.add(18);
		tree.add(80);
		tree.add(1);
		assertEquals("Incorrect tree prior to removal",
				TreeStructure.build(20, 10, 40, 5, 15, 30, 60, 3, 7, 14, 17, 25, 50, 70, 2, 4, 8, 18, 80, 1),
				tree);
		
		tree.remove(40);
		
		assertEquals("Incorrect tree after remove with multiple rotations", 
				TreeStructure.build(10, 5, 20, 3, 7, 15, 50, 2, 4, 8, 14, 17, 30, 70, 1, 18, 25, 60, 80), 
				tree);
	}
	
    public boolean passed(PrintWriter out) {
        
    	Map<String, Runnable> tests = new LinkedHashMap<>();
    	tests.put("Add non-duplicates increases size", this::addNonDuplicatesIncreasesSize);
    	tests.put("Add duplicates does not increase size", this::addDuplicatesDoesNotIncreaseSize);
    	tests.put("Test add return codes", this::addReturnCodes);
    	tests.put("Test contains method",  this::testContains);
    	tests.put("Test clear method", this::testClear);
//    	tests.put("In-order traversal", this::testInOrderTraversal);
//    	tests.put("Pre-order traversal", this::testPreOrderTraversal);
//    	tests.put("Post-order traversal", this::testPostOrderTraversal);
    	tests.put("Remove leaf node", this::removeLeafNode);
    	tests.put("Remove inner node with 1 child", this::removeInnerNodeWith1Child);
    	tests.put("Remove inner node with 2 children", this::removeInnerNodeWith2Children);
    	tests.put("Remove root (every root removal case)", this::removeRoot);
    	tests.put("Test heights", this::testHeights);
    	tests.put("Test Balance factors", this::testBalanceFactors);
    	tests.put("Insert that requires left-left rotation with no children", this::leftLeftRotation);
    	tests.put("Insert that requires left-right rotation with no children", this::leftRightRotation);
    	tests.put("Insert that requires right-left rotation with no children", this::rightLeftRotation);
    	tests.put("Insert that requires right-right rotation with children", this::rightRightRotation);
    	tests.put("Insert that requires left-left rotation with children", this::leftLeftRotationWithChildren);
    	tests.put("Insert that requires left-right rotation with children", this::leftRightRotationWithChildren);
    	tests.put("Insert that requires right-left rotation with children", this::rightLeftRotationWithChildren);
    	tests.put("Insert that requires right-right rotation with children", this::rightRightRotationWithChildren);
    	tests.put("Insert that requires rotations on root (every case)", this::rotationOnRoot);
    	tests.put("Test height with a rotation", this::testHeightWithRotations);
    	tests.put("Remove with left-left rotation", this::removeLeftLeftRotation);
    	tests.put("Remove with left-right rotation", this::removeLeftRightRotation);
    	tests.put("Remove with right-left rotation", this::removeRightLeftRotation);
    	tests.put("Remove with right-right rotation", this::removeRightRightRotation);
    	tests.put("Remove with multiple rotations", this::removeWithMultipleRotations);
    	
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
    	
    	AVLTreeUnitTest test = new AVLTreeUnitTest();
    	
    	PrintWriter output = new PrintWriter(System.err);
    	if(test.passed(output)) {
    		System.out.println("Passed");
    	}
    	else 
    		System.out.println("Failed");
    	//output.flush();
    }
}
