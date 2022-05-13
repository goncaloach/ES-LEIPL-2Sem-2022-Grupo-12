package org.jgrapht.alg.matching;


public class SimpleMatchingProduct {
    private final int[] match;

    public SimpleMatchingProduct(int n) {
        this.match = new int[n];
    }

    public int[] getMatch() {
        return match;
    }

    /**
     * Test whether a vertex is matched (i.e. incident to a matched edge).
     */
    public boolean isMatched(int v) {
        return match[v] != DenseEdmondsMaximumCardinalityMatching.SimpleMatching.UNMATCHED;
    }

    /**
     * For a given vertex v and matched edge (v,w), this function returns vertex w.
     */
    public int opposite(int v) {
        assert isMatched(v);
        return match[v];
    }

    /**
     * Test whether a vertex is exposed (i.e. not incident to a matched edge).
     */
    public boolean isExposed(int v) {
        return match[v] == DenseEdmondsMaximumCardinalityMatching.SimpleMatching.UNMATCHED;
    }
}
