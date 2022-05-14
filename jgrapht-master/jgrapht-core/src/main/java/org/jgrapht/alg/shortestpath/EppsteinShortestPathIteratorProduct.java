package org.jgrapht.alg.shortestpath;


import java.util.List;
import org.jgrapht.alg.shortestpath.EppsteinShortestPathIterator.PathsGraphVertex;

public class EppsteinShortestPathIteratorProduct<V, E> {
	/**
	* Builds a min-heap out of the  {@code  vertices}  list
	* @param vertices  vertices
	* @param size  size of vertices
	*/
	public void heapify(List<EppsteinShortestPathIterator<V, E>.PathsGraphVertex> vertices, int size) {
		for (int i = size / 2 - 1; i >= 0; i--) {
			siftDown(vertices, i, size);
		}
	}

	public void siftDown(List<EppsteinShortestPathIterator<V, E>.PathsGraphVertex> vertices, int i, int size) {
		int left;
		int right;
		int smaller;
		int current = i;
		while (true) {
			left = 2 * current + 1;
			right = 2 * current + 2;
			smaller = current;
			if (left < size && vertices.get(left).compareTo(vertices.get(smaller)) < 0) {
				smaller = left;
			}
			if (right < size && vertices.get(right).compareTo(vertices.get(smaller)) < 0) {
				smaller = right;
			}
			if (smaller == current) {
				break;
			}
			swap(vertices, current, smaller);
			current = smaller;
		}
	}

	public void swap(List<EppsteinShortestPathIterator<V, E>.PathsGraphVertex> vertices, int i, int j) {
		if (i != j) {
			EppsteinShortestPathIterator<V, E>.PathsGraphVertex tmp = vertices.get(i);
			vertices.set(i, vertices.get(j));
			vertices.set(j, tmp);
		}
	}
}
