package net.wforbes.javadatastructures.graph;

import java.util.*;

public class ShortestPathDWGraph<E> implements WeightedGraph<E> {

    private ArrayList<ArrayList<Double>> matrix;
    private Map<E, Integer> verticesToIndices;

    public ShortestPathDWGraph() {
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
                //System.out.println("adjacency found between "+value+" and "+vert);
                adjVals.add(vert);
            }
        }
        return adjVals;
    }

    @Override
    public boolean isAdjacent(E from, E to) {
        //System.out.println("checking if "+from+" is adjacent to "+to);

        return Double.isFinite(this.matrix
                .get(verticesToIndices.get(from))
                .get(verticesToIndices.get(to)));
    }

    private E popMinQueue(Set<E> queue, Map<E, Double> verticesToDistances){
        Double min = Double.POSITIVE_INFINITY;
        E minDistanceKey = null;
        //find key with min distance
        for (E vert : queue) {
            if (verticesToDistances.get(vert) < min) {
                min = verticesToDistances.get(vert);
                minDistanceKey = vert;
            }
        }
        queue.remove(minDistanceKey);
        return minDistanceKey;
    }

    @Override
    public double getWeight(E from, E to) {
        if(from.equals(to)){
            return 0;
        }else {
            Map<E, Double> verticesToDistances = new HashMap<>();
            Map<E, E> verticesToPredecessors = new HashMap<>();
            Set<E> unvisited = new HashSet<>(this.matrix.size());
            for (E vert : this.verticesToIndices.keySet()) {
                verticesToDistances.put(vert, Double.POSITIVE_INFINITY); //initialize all vertices distances to infinity
                verticesToPredecessors.put(vert, null); //initialize all vertices' predecessors to 0 (null)
                unvisited.add(vert); //push all vertices to a queue of unvisited vertices
            }
            verticesToDistances.put(from, 0.0); //assign the start vertex's distance with 0
            while (!unvisited.isEmpty()) {//while the queue is not empty

                //find minimum distance in queue
                E current = this.popMinQueue(unvisited, verticesToDistances);
                //pop the vertex with the shortest distance from the queue

                Collection<E> adjValues = this.getAdjacentValues(current);
                if(adjValues.isEmpty()){//probably uneccessary failsafe
                    break;
                }
                Double edgeWeight = 0.0;
                Double alternativePathDistance = 0.0;
                //for each adjacent vertex
                for (E adjVert : adjValues) {
                    //compute the distance of the path from the start vertex to the current vertex,
                    edgeWeight = this.matrix
                            .get(verticesToIndices.get(current))
                            .get(verticesToIndices.get(adjVert));
                    alternativePathDistance = verticesToDistances.get(current) + edgeWeight;

                    //if that path's distance is shorter than the adjacent vertex's current distance,
                    // a shorter path has been found
                    if (alternativePathDistance <= verticesToDistances.get(adjVert)) {
                        //the adjacent vertex's current distance is updated to the distance of the newly found shorter path's distance
                        // vertex's predecessor pointer is pointed to the current vertex
                        verticesToDistances.put(adjVert, alternativePathDistance);
                        verticesToPredecessors.put(adjVert, current);
                    }
                }
            }

            //if the destinations predecessor pointer is not zero
            if (verticesToPredecessors.get(to) != null) {
                //the shortest path is followed in reverse through
                // 	the predecessor pointers until reaching the start
                return verticesToDistances.get(to);
            } else {
                return Double.POSITIVE_INFINITY;//if 'to' doesn't have a predecessor, a path doesn't exist
            }
        }
    }
}