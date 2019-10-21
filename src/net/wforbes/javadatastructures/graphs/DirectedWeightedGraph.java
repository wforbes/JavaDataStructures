package net.wforbes.javadatastructures.graphs;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;

public class DirectedWeightedGraph<E> implements WeightedGraph<E>{

	private ArrayList<ArrayList<Double>> matrix;
	private Map<E, Integer> verticesToIndices;
	
	public DirectedWeightedGraph() {
		this.matrix = new ArrayList<ArrayList<Double>>();
		this.verticesToIndices = new HashMap<E, Integer>();
	}
	
	@Override
	public void addVertex(E value) {
		int newSize = this.matrix.size() + 1;
		for(ArrayList<Double> r : this.matrix) {
			r.add(Double.POSITIVE_INFINITY);
		}
		
		ArrayList<Double> newRow = new ArrayList<>();
		for(int i = 0; i < newSize; i++) {
			newRow.add(Double.POSITIVE_INFINITY);
		}
		this.matrix.add(newRow);
		this.verticesToIndices.put(value, newSize-1);
		
	}

	@Override
	public void addEdge(E from, E to, double weight) {
		int fIndex = verticesToIndices.get(from);
		int tIndex = verticesToIndices.get(to);
		matrix.get(fIndex).set(tIndex, weight);
	}

	@Override
	public Collection<E> getAdjacentValues(E value) {
		Collection<E> adjVals = new HashSet<>();
		for(E vert : this.verticesToIndices.keySet()) {
			if(isAdjacent(value, vert)) {
				adjVals.add(vert);
			}
		}
		return adjVals;
	}

	@Override
	public boolean isAdjacent(E from, E to) {
		return Double.isFinite(this.getWeight(from, to));
	}

	@Override
	public double getWeight(E from, E to) {
		int fromIndex = verticesToIndices.get(from);
		int toIndex = verticesToIndices.get(to);
		return matrix.get(fromIndex).get(toIndex);
	}

}
