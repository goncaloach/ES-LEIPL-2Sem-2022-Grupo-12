package org.jgrapht.alg.cycle;


import org.jgrapht.Graph;
import java.util.Set;
import org.jgrapht.alg.connectivity.ConnectivityInspector;

public class BergeGraphInspectorProduct<V,E> {
    /**
     * A vertex y is X-complete if y contained in V(g)\X is adjacent to every vertex in X.
     * @param g  A Graph
     * @param y  Vertex whose X-completeness is to assess
     * @param x  Set of vertices
     * @return  whether y is X-complete
     */
    public boolean isYXComplete(Graph<V, E> g, V y, Set<V> x) {
        return x.stream().allMatch(t -> g.containsEdge(t, y));
    }

    /**
     * For each anticomponent X, find the maximal connected subset F' containing v5 with the properties that v1,v2 have no neighbours in F' and no vertex of F'\v5 is X-complete
     * @param g  A Graph
     * @param setX  A set of vertices
     * @param v1  A vertex
     * @param v2  A vertex
     * @param v5  A Vertex
     * @return  The maximal connected vertex subset containing v5, no neighbours of v1 and v2, and no X-complete vertex except v5
     */
    public Set<V> findMaximalConnectedSubset(Graph<V, E> g, Set<V> setX, V v1, V v2, V v5) {
        Set<V> fPrime = new ConnectivityInspector<>(g).connectedSetOf(v5);
        fPrime.removeIf(t -> t != v5 && isYXComplete(g, t, setX) || v1 == t || v2 == t || g.containsEdge(v1, t)
                || g.containsEdge(v2, t));
        return fPrime;
    }

    /**
     * Reports whether v has at least one neighbour in set
     * @param g  A Graph
     * @param set  A set of vertices
     * @param v  A vertex
     * @return  whether v has at least one neighbour in set
     */
    public boolean hasANeighbour(Graph<V, E> g, Set<V> set, V v) {
        return set.stream().anyMatch(s -> g.containsEdge(s, v));
    }

    /**
     * Reports whether a vertex has at least one nonneighbour in X
     * @param g  A Graph
     * @param v  A Vertex
     * @param setX  A set of vertices
     * @return  whether v has a nonneighbour in X
     */
    public boolean hasANonneighbourInX(Graph<V, E> g, V v, Set<V> setX) {
        return setX.stream().anyMatch(x -> !g.containsEdge(v, x));
    }
}
