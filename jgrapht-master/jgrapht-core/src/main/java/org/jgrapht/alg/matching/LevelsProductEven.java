package org.jgrapht.alg.matching;


public class LevelsProductEven {
    private int[] even;

    public int[] getEven2() {
        return even;
    }

    public void setEven(int[] even) {
        this.even = even;
    }

    public int getEven(int v) {
        return even[v];
    }

    public boolean isEven(int v) {
        return even[v] != DenseEdmondsMaximumCardinalityMatching.NIL;
    }
}
