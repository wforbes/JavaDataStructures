package net.wforbes.javadatastructures.graph;

import java.util.Collection;

public interface WeightedGraph<E> {

    /**
     * Adds the specified node to the graph.
     * Note, for the sake of this lab, the tests will never try to add a
     * node that is already present, so you don't need to worry about checking for that
     * condition
     * @param value
     */
    public void addVertex(E value);

    /**
     * Adds an edge between the two vertices
     * Note that for this lab, the tests will never try to add an edge that involves a node that doesn't
     * exist, so you don't need to worry about that.
     * @param from Starting vertex 
     * @param to Ending vertex
     * @param weight weight of the edge
     */
    public void addEdge(E from, E to, double weight);

    /**
     * Returns a collection of all of the values to which the specified
     * value has edges.
     * @param value - the value contained in the starting vertex 
     * @return collection of all of the values found in vertices reachable directly (one hop) 
     * from the starting vertex.
     */
    public Collection<E> getAdjacentValues(E value);

    /**
     * Checks if there is an edge from vertex with the "from" value to the one
     * with the "to" value
     * @param from - value corresponding to the starting vertex
     * @param to - value correspondnig to the ending vertex
     * @return true if an edge exists from "from" to "to"
     */
    public boolean isAdjacent(E from, E to);

    /**
     * Returns the weight between two value. For this lab,
     * if the two values are not adjacent, then the weight between
     * them is considered to be infinity.
     * (Note, to return infinity, use Double.POSITIVE_INFINITY
     * @param from - the value found in the Starting vertex
     * @param to - the value found in the ending vertex
     * @return Weight of edge from "from" vertex to "to" vertex or infinity if no edge exists
     */
    public double getWeight(E from, E to);

}