/*
 * (C) Copyright 2016-2021, by Dimitrios Michail and Contributors.
 *
 * JGraphT : a free Java graph-theory library
 *
 * See the CONTRIBUTORS.md file distributed with this work for additional
 * information regarding copyright ownership.
 *
 * This program and the accompanying materials are made available under the
 * terms of the Eclipse Public License 2.0 which is available at
 * http://www.eclipse.org/legal/epl-2.0, or the
 * GNU Lesser General Public License v2.1 or later
 * which is available at
 * http://www.gnu.org/licenses/old-licenses/lgpl-2.1-standalone.html.
 *
 * SPDX-License-Identifier: EPL-2.0 OR LGPL-2.1-or-later
 */
package org.jgrapht.alg.cycle;

import org.jgrapht.*;
import org.jgrapht.alg.interfaces.*;
import org.jgrapht.alg.interfaces.CycleBasisAlgorithm.*;
import org.jgrapht.graph.*;
import org.junit.*;

import java.util.*;
import java.util.stream.*;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

/**
 * Unit tests for {@link StackBFSFundamentalCycleBasis}.
 * 
 * @author Dimitrios Michail
 */
public class StackBFSFundamentalCycleBasisTest
{
    @Test
    public void testSimple()
    {
        Graph<Integer, DefaultEdge> graph = new SimpleGraph<>(DefaultEdge.class);
        Graphs.addEdgeWithVertices(graph, 0, 1);
        Graphs.addEdgeWithVertices(graph, 1, 2);
        Graphs.addEdgeWithVertices(graph, 2, 0);

        CycleBasisAlgorithm<Integer, DefaultEdge> fcb = new StackBFSFundamentalCycleBasis<>(graph);

        CycleBasis<Integer, DefaultEdge> cb = fcb.getCycleBasis();
        assertEquals(1, cb.getCycles().size());
        List<List<DefaultEdge>> cycles = new ArrayList<>(cb.getCycles());

        List<DefaultEdge> c1 = cycles.get(0);
        assertTrue(c1.contains(graph.getEdge(0, 1)));
        assertTrue(c1.contains(graph.getEdge(1, 2)));
        assertTrue(c1.contains(graph.getEdge(2, 0)));
        assertEquals(3, c1.size());
        assertEquals(3, cb.getLength());
        assertEquals(3.0, cb.getWeight(), 0.0001);

        Graphs.addEdgeWithVertices(graph, 2, 3);
        Graphs.addEdgeWithVertices(graph, 3, 0);

        cb = fcb.getCycleBasis();
        cycles = new ArrayList<>(cb.getCycles());
        assertEquals(2, cb.getCycles().size());
        c1 = cycles.get(0);
        assertTrue(c1.contains(graph.getEdge(1, 2)));
        assertTrue(c1.contains(graph.getEdge(2, 0)));
        assertTrue(c1.contains(graph.getEdge(1, 0)));
        assertEquals(3, c1.size());

        List<DefaultEdge> c2 = cycles.get(1);
        assertTrue(c2.contains(graph.getEdge(2, 3)));
        assertTrue(c2.contains(graph.getEdge(0, 2)));
        assertTrue(c2.contains(graph.getEdge(0, 3)));
        assertEquals(3, c2.size());
        assertEquals(6, cb.getLength());
        assertEquals(6.0, cb.getWeight(), 0.0001);

        Graphs.addEdgeWithVertices(graph, 3, 1);

        cb = fcb.getCycleBasis();
        cycles = new ArrayList<>(cb.getCycles());
        assertEquals(3, cb.getCycles().size());
        c1 = cycles.get(0);
        assertTrue(c1.contains(graph.getEdge(1, 2)));
        assertTrue(c1.contains(graph.getEdge(2, 0)));
        assertTrue(c1.contains(graph.getEdge(1, 0)));
        assertEquals(3, c1.size());

        c2 = cycles.get(1);
        assertTrue(c2.contains(graph.getEdge(2, 3)));
        assertTrue(c2.contains(graph.getEdge(0, 2)));
        assertTrue(c2.contains(graph.getEdge(0, 3)));
        assertEquals(3, c2.size());

        List<DefaultEdge> c3 = cycles.get(2);
        assertTrue(c3.contains(graph.getEdge(1, 3)));
        assertTrue(c3.contains(graph.getEdge(0, 1)));
        assertTrue(c3.contains(graph.getEdge(0, 3)));
        assertEquals(3, c3.size());

        assertEquals(9, cb.getLength());
        assertEquals(9.0, cb.getWeight(), 0.0001);

        Graphs.addEdgeWithVertices(graph, 3, 4);
        Graphs.addEdgeWithVertices(graph, 4, 2);

        cb = fcb.getCycleBasis();
        cycles = new ArrayList<>(cb.getCycles());
        assertEquals(4, cb.getCycles().size());

        c1 = cycles.get(0);
        assertTrue(c1.contains(graph.getEdge(1, 2)));
        assertTrue(c1.contains(graph.getEdge(2, 0)));
        assertTrue(c1.contains(graph.getEdge(1, 0)));
        assertEquals(3, c1.size());

        c2 = cycles.get(1);
        assertTrue(c2.contains(graph.getEdge(2, 3)));
        assertTrue(c2.contains(graph.getEdge(0, 2)));
        assertTrue(c2.contains(graph.getEdge(0, 3)));
        assertEquals(3, c2.size());

        c3 = cycles.get(2);
        assertTrue(c3.contains(graph.getEdge(1, 3)));
        assertTrue(c3.contains(graph.getEdge(0, 1)));
        assertTrue(c3.contains(graph.getEdge(0, 3)));
        assertEquals(3, c3.size());

        List<DefaultEdge> c4 = cycles.get(3);
        assertTrue(c4.contains(graph.getEdge(3, 4)));
        assertTrue(c4.contains(graph.getEdge(0, 3)));
        assertTrue(c4.contains(graph.getEdge(0, 2)));
        assertTrue(c4.contains(graph.getEdge(2, 4)));
        assertEquals(4, c4.size());

        assertEquals(13, cb.getLength());
        assertEquals(13.0, cb.getWeight(), 0.0001);

        Graphs.addEdgeWithVertices(graph, 4, 5);

        cb = fcb.getCycleBasis();
        cycles = new ArrayList<>(cb.getCycles());
        assertEquals(4, cb.getCycles().size());

        c1 = cycles.get(0);
        assertTrue(c1.contains(graph.getEdge(1, 2)));
        assertTrue(c1.contains(graph.getEdge(2, 0)));
        assertTrue(c1.contains(graph.getEdge(1, 0)));
        assertEquals(3, c1.size());

        c2 = cycles.get(1);
        assertTrue(c2.contains(graph.getEdge(2, 3)));
        assertTrue(c2.contains(graph.getEdge(0, 2)));
        assertTrue(c2.contains(graph.getEdge(0, 3)));
        assertEquals(3, c2.size());

        c3 = cycles.get(2);
        assertTrue(c3.contains(graph.getEdge(1, 3)));
        assertTrue(c3.contains(graph.getEdge(0, 1)));
        assertTrue(c3.contains(graph.getEdge(0, 3)));
        assertEquals(3, c3.size());

        c4 = cycles.get(3);
        assertTrue(c4.contains(graph.getEdge(3, 4)));
        assertTrue(c4.contains(graph.getEdge(0, 3)));
        assertTrue(c4.contains(graph.getEdge(0, 2)));
        assertTrue(c4.contains(graph.getEdge(2, 4)));
        assertEquals(4, c4.size());

        assertEquals(13, cb.getLength());
        assertEquals(13.0, cb.getWeight(), 0.0001);

        Graphs.addEdgeWithVertices(graph, 5, 2);

        cb = fcb.getCycleBasis();
        cycles = new ArrayList<>(cb.getCycles());
        assertEquals(5, cb.getCycles().size());

        c1 = cycles.get(0);
        assertTrue(c1.contains(graph.getEdge(1, 2)));
        assertTrue(c1.contains(graph.getEdge(2, 0)));
        assertTrue(c1.contains(graph.getEdge(1, 0)));
        assertEquals(3, c1.size());

        c2 = cycles.get(1);
        assertTrue(c2.contains(graph.getEdge(2, 3)));
        assertTrue(c2.contains(graph.getEdge(0, 2)));
        assertTrue(c2.contains(graph.getEdge(0, 3)));
        assertEquals(3, c2.size());

        c3 = cycles.get(2);
        assertTrue(c3.contains(graph.getEdge(1, 3)));
        assertTrue(c3.contains(graph.getEdge(0, 1)));
        assertTrue(c3.contains(graph.getEdge(0, 3)));
        assertEquals(3, c3.size());

        c4 = cycles.get(3);
        assertTrue(c4.contains(graph.getEdge(4, 2)));
        assertTrue(c4.contains(graph.getEdge(2, 0)));
        assertTrue(c4.contains(graph.getEdge(0, 3)));
        assertTrue(c4.contains(graph.getEdge(3, 4)));
        assertEquals(4, c4.size());

        List<DefaultEdge> c5 = cycles.get(4);
        assertTrue(c5.contains(graph.getEdge(5, 2)));
        assertTrue(c5.contains(graph.getEdge(2, 0)));
        assertTrue(c5.contains(graph.getEdge(0, 3)));
        assertTrue(c5.contains(graph.getEdge(3, 4)));
        assertTrue(c5.contains(graph.getEdge(4, 5)));
        assertEquals(5, c5.size());

        assertEquals(18, cb.getLength());
        assertEquals(18.0, cb.getWeight(), 0.0001);

        Graphs.addEdgeWithVertices(graph, 5, 6);
        Graphs.addEdgeWithVertices(graph, 6, 4);

        cb = fcb.getCycleBasis();
        cycles = new ArrayList<>(cb.getCycles());
        assertEquals(6, cb.getCycles().size());

        c1 = cycles.get(0);
        assertTrue(c1.contains(graph.getEdge(1, 2)));
        assertTrue(c1.contains(graph.getEdge(2, 0)));
        assertTrue(c1.contains(graph.getEdge(1, 0)));
        assertEquals(3, c1.size());

        c2 = cycles.get(1);
        assertTrue(c2.contains(graph.getEdge(2, 3)));
        assertTrue(c2.contains(graph.getEdge(0, 2)));
        assertTrue(c2.contains(graph.getEdge(0, 3)));
        assertEquals(3, c2.size());

        c3 = cycles.get(2);
        assertTrue(c3.contains(graph.getEdge(1, 3)));
        assertTrue(c3.contains(graph.getEdge(0, 1)));
        assertTrue(c3.contains(graph.getEdge(0, 3)));
        assertEquals(3, c3.size());

        c4 = cycles.get(3);
        assertTrue(c4.contains(graph.getEdge(4, 2)));
        assertTrue(c4.contains(graph.getEdge(2, 0)));
        assertTrue(c4.contains(graph.getEdge(0, 3)));
        assertTrue(c4.contains(graph.getEdge(3, 4)));
        assertEquals(4, c4.size());

        c5 = cycles.get(4);
        assertTrue(c5.contains(graph.getEdge(5, 2)));
        assertTrue(c5.contains(graph.getEdge(2, 0)));
        assertTrue(c5.contains(graph.getEdge(0, 3)));
        assertTrue(c5.contains(graph.getEdge(3, 4)));
        assertTrue(c5.contains(graph.getEdge(4, 5)));
        assertEquals(5, c5.size());

        List<DefaultEdge> c6 = cycles.get(5);
        assertTrue(c6.contains(graph.getEdge(5, 6)));
        assertTrue(c6.contains(graph.getEdge(6, 4)));
        assertTrue(c6.contains(graph.getEdge(4, 5)));
        assertEquals(3, c6.size());

        assertEquals(21, cb.getLength());
        assertEquals(21.0, cb.getWeight(), 0.0001);

    }

    @Test
    public void testMultigraphsWithLoops()
    {
        Graph<Integer, DefaultEdge> graph = new Pseudograph<>(DefaultEdge.class);
        Graphs.addEdgeWithVertices(graph, 0, 1);
        Graphs.addEdgeWithVertices(graph, 0, 2);
        Graphs.addEdgeWithVertices(graph, 0, 3);
        Graphs.addEdgeWithVertices(graph, 1, 2);
        Graphs.addEdgeWithVertices(graph, 2, 3);
        Graphs.addEdgeWithVertices(graph, 1, 4);
        Graphs.addEdgeWithVertices(graph, 2, 5);
        Graphs.addEdgeWithVertices(graph, 3, 6);
        Graphs.addEdgeWithVertices(graph, 4, 5);
        Graphs.addEdgeWithVertices(graph, 5, 6);
        Graphs.addEdgeWithVertices(graph, 4, 7);
        Graphs.addEdgeWithVertices(graph, 5, 8);
        Graphs.addEdgeWithVertices(graph, 6, 9);
        Graphs.addEdgeWithVertices(graph, 7, 8);
        DefaultEdge e89_1 = graph.addEdge(8, 9);
        Graphs.addEdgeWithVertices(graph, 7, 9);
        DefaultEdge e89_2 = graph.addEdge(8, 9);
        DefaultEdge e89_3 = graph.addEdge(8, 9);
        DefaultEdge e89_4 = graph.addEdge(8, 9);
        DefaultEdge e77_1 = graph.addEdge(7, 7);
        DefaultEdge e77_2 = graph.addEdge(7, 7);
        DefaultEdge e77_3 = graph.addEdge(7, 7);

        CycleBasisAlgorithm<Integer, DefaultEdge> fcb = new StackBFSFundamentalCycleBasis<>(graph);
        CycleBasis<Integer, DefaultEdge> cb = fcb.getCycleBasis();

        assertEquals(13, cb.getCycles().size());

        List<List<DefaultEdge>> cycles = new ArrayList<>(cb.getCycles());

        List<DefaultEdge> c1 = cycles.get(0);
        assertTrue(c1.contains(graph.getEdge(1, 2)));
        assertTrue(c1.contains(graph.getEdge(2, 0)));
        assertTrue(c1.contains(graph.getEdge(0, 1)));
        assertEquals(3, c1.size());

        List<DefaultEdge> c2 = cycles.get(1);
        assertTrue(c2.contains(graph.getEdge(2, 3)));
        assertTrue(c2.contains(graph.getEdge(3, 0)));
        assertTrue(c2.contains(graph.getEdge(0, 2)));
        assertEquals(3, c2.size());

        List<DefaultEdge> c3 = cycles.get(2);
        assertTrue(c3.contains(graph.getEdge(1, 4)));
        assertTrue(c3.contains(graph.getEdge(4, 7)));
        assertTrue(c3.contains(graph.getEdge(7, 9)));
        assertTrue(c3.contains(graph.getEdge(9, 6)));
        assertTrue(c3.contains(graph.getEdge(6, 3)));
        assertTrue(c3.contains(graph.getEdge(3, 0)));
        assertTrue(c3.contains(graph.getEdge(0, 1)));
        assertEquals(7, c3.size());

        List<DefaultEdge> c4 = cycles.get(3);
        assertTrue(c4.contains(graph.getEdge(2, 5)));
        assertTrue(c4.contains(graph.getEdge(5, 6)));
        assertTrue(c4.contains(graph.getEdge(6, 3)));
        assertTrue(c4.contains(graph.getEdge(3, 0)));
        assertTrue(c4.contains(graph.getEdge(0, 2)));
        assertEquals(5, c4.size());

        List<DefaultEdge> c5 = cycles.get(4);
        assertTrue(c5.contains(graph.getEdge(4, 5)));
        assertTrue(c5.contains(graph.getEdge(5, 6)));
        assertTrue(c5.contains(graph.getEdge(6, 9)));
        assertTrue(c5.contains(graph.getEdge(9, 7)));
        assertTrue(c5.contains(graph.getEdge(7, 4)));
        assertEquals(5, c5.size());

        List<DefaultEdge> c6 = cycles.get(5);
        assertTrue(c6.contains(graph.getEdge(5, 8)));
        assertTrue(c6.contains(graph.getEdge(8, 9)));
        assertTrue(c6.contains(graph.getEdge(9, 6)));
        assertTrue(c6.contains(graph.getEdge(6, 5)));
        assertEquals(4, c6.size());

        List<DefaultEdge> c7 = cycles.get(6);
        assertTrue(c7.contains(graph.getEdge(7, 8)));
        assertTrue(c7.contains(e89_1));
        assertTrue(c7.contains(graph.getEdge(9, 7)));
        assertEquals(3, c7.size());

        List<DefaultEdge> c8 = cycles.get(7);
        assertTrue(c8.contains(e89_2));
        assertTrue(c8.contains(e89_1));
        assertEquals(2, c8.size());

        List<DefaultEdge> c9 = cycles.get(8);
        assertTrue(c9.contains(e89_3));
        assertTrue(c9.contains(e89_1));
        assertEquals(2, c9.size());

        List<DefaultEdge> c10 = cycles.get(9);
        assertTrue(c10.contains(e89_4));
        assertTrue(c10.contains(e89_1));
        assertEquals(2, c10.size());

        List<DefaultEdge> c11 = cycles.get(10);
        assertTrue(c11.contains(e77_1));
        assertEquals(1, c11.size());

        List<DefaultEdge> c12 = cycles.get(11);
        assertTrue(c12.contains(e77_2));
        assertEquals(1, c12.size());

        List<DefaultEdge> c13 = cycles.get(12);
        assertTrue(c13.contains(e77_3));
        assertEquals(1, c13.size());

        for (List<DefaultEdge> c : cb.getCycles()) {
            assertCycle(graph, c);
        }

        assertEquals(39, cb.getLength());
        assertEquals(39.0, cb.getWeight(), 0.0001);
    }

    @Test
    public void testMultiGraphWithMultipleComponentsWithLoops()
    {
        Graph<Integer, DefaultEdge> graph = new Pseudograph<>(DefaultEdge.class);
        graph.addVertex(0);
        graph.addVertex(1);
        graph.addVertex(2);
        graph.addEdge(0, 1);
        graph.addEdge(0, 2);
        DefaultEdge e12_1 = graph.addEdge(1, 2);
        DefaultEdge e12_2 = graph.addEdge(1, 2);
        DefaultEdge e11_1 = graph.addEdge(1, 1);
        DefaultEdge e11_2 = graph.addEdge(1, 1);
        graph.addVertex(3);
        graph.addVertex(4);
        graph.addVertex(5);
        graph.addEdge(3, 4);
        graph.addEdge(3, 5);
        DefaultEdge e45_1 = graph.addEdge(4, 5);
        DefaultEdge e45_2 = graph.addEdge(4, 5);
        DefaultEdge e55_1 = graph.addEdge(5, 5);
        DefaultEdge e55_2 = graph.addEdge(5, 5);

        CycleBasisAlgorithm<Integer, DefaultEdge> fcb = new StackBFSFundamentalCycleBasis<>(graph);
        CycleBasis<Integer, DefaultEdge> cb = fcb.getCycleBasis();
        assertEquals(8, cb.getCycles().size());

        List<List<DefaultEdge>> cycles = new ArrayList<>(cb.getCycles());

        List<DefaultEdge> c1 = cycles.get(0);
        assertTrue(c1.contains(graph.getEdge(0, 1)));
        assertTrue(c1.contains(e12_1));
        assertTrue(c1.contains(graph.getEdge(2, 0)));
        assertEquals(3, c1.size());

        List<DefaultEdge> c2 = cycles.get(1);
        assertTrue(c2.contains(graph.getEdge(0, 1)));
        assertTrue(c2.contains(e12_2));
        assertTrue(c2.contains(graph.getEdge(2, 0)));
        assertEquals(3, c2.size());

        List<DefaultEdge> c3 = cycles.get(2);
        assertTrue(c3.contains(e11_1));
        assertEquals(1, c3.size());

        List<DefaultEdge> c4 = cycles.get(3);
        assertTrue(c4.contains(e11_2));
        assertEquals(1, c4.size());

        List<DefaultEdge> c5 = cycles.get(4);
        assertTrue(c5.contains(graph.getEdge(3, 4)));
        assertTrue(c5.contains(e45_1));
        assertTrue(c5.contains(graph.getEdge(5, 3)));
        assertEquals(3, c5.size());

        List<DefaultEdge> c6 = cycles.get(5);
        assertTrue(c6.contains(graph.getEdge(3, 4)));
        assertTrue(c6.contains(e45_2));
        assertTrue(c6.contains(graph.getEdge(5, 3)));
        assertEquals(3, c6.size());

        List<DefaultEdge> c7 = cycles.get(6);
        assertTrue(c7.contains(e55_1));
        assertEquals(1, c7.size());

        List<DefaultEdge> c8 = cycles.get(7);
        assertTrue(c8.contains(e55_2));
        assertEquals(1, c8.size());

        assertEquals(16, cb.getLength());
        assertEquals(16.0, cb.getWeight(), 0.0001);
    }

    @Test
    public void testTwoParallelEdges()
    {
        Graph<Integer, DefaultEdge> graph = new Pseudograph<>(DefaultEdge.class);
        graph.addVertex(0);
        graph.addVertex(1);
        DefaultEdge e1 = graph.addEdge(0, 1);
        DefaultEdge e2 = graph.addEdge(0, 1);

        CycleBasisAlgorithm<Integer, DefaultEdge> fcb = new StackBFSFundamentalCycleBasis<>(graph);
        CycleBasis<Integer, DefaultEdge> cb = fcb.getCycleBasis();
        assertEquals(1, cb.getCycles().size());

        List<DefaultEdge> c1 = cb.getCycles().stream().findFirst().get();
        assertTrue(c1.contains(e1));
        assertTrue(c1.contains(e2));
        assertEquals(2, c1.size());

        assertEquals(2, cb.getLength());
        assertEquals(2.0, cb.getWeight(), 0.0001);
    }

    @Test
    public void testMoreParallelEdges()
    {
        Graph<Integer, DefaultEdge> graph = new Pseudograph<>(DefaultEdge.class);
        graph.addVertex(0);
        graph.addVertex(1);
        graph.addVertex(2);
        graph.addVertex(3);
        DefaultEdge e01_1 = graph.addEdge(0, 1);
        DefaultEdge e01_2 = graph.addEdge(0, 1);
        DefaultEdge e12 = graph.addEdge(1, 2);
        DefaultEdge e23_1 = graph.addEdge(2, 3);
        DefaultEdge e23_2 = graph.addEdge(2, 3);
        DefaultEdge e30 = graph.addEdge(3, 0);

        CycleBasisAlgorithm<Integer, DefaultEdge> fcb = new StackBFSFundamentalCycleBasis<>(graph);
        CycleBasis<Integer, DefaultEdge> cb = fcb.getCycleBasis();
        assertEquals(3, cb.getCycles().size());

        List<List<DefaultEdge>> cycles = new ArrayList<>(cb.getCycles());

        List<DefaultEdge> c1 = cycles.get(0);
        assertTrue(c1.contains(e01_2));
        assertTrue(c1.contains(e01_1));
        assertEquals(2, c1.size());

        List<DefaultEdge> c2 = cycles.get(1);
        assertTrue(c2.contains(e01_1));
        assertTrue(c2.contains(e12));
        assertTrue(c2.contains(e23_1));
        assertTrue(c2.contains(e30));
        assertEquals(4, c2.size());

        List<DefaultEdge> c3 = cycles.get(2);
        assertTrue(c3.contains(e23_2));
        assertTrue(c3.contains(e23_1));
        assertEquals(2, c3.size());

        assertEquals(8, cb.getLength());
        assertEquals(8.0, cb.getWeight(), 0.0001);
    }

    @Test
    public void testZeroCycleSpaceDimension()
    {
        Graph<Integer, DefaultEdge> graph = new Pseudograph<>(DefaultEdge.class);
        graph.addVertex(0);
        graph.addVertex(1);
        graph.addVertex(2);
        graph.addVertex(3);
        graph.addEdge(0, 1);
        graph.addEdge(2, 3);

        CycleBasisAlgorithm<Integer, DefaultEdge> fcb = new StackBFSFundamentalCycleBasis<>(graph);
        CycleBasis<Integer, DefaultEdge> cb = fcb.getCycleBasis();
        assertEquals(0, cb.getCycles().size());
        assertEquals(0, cb.getLength());
        assertEquals(0d, cb.getWeight(), 1e-9);
    }

    @Test
    public void testEmptyGraph()
    {
        Graph<Integer, DefaultEdge> graph = new Pseudograph<>(DefaultEdge.class);
        CycleBasisAlgorithm<Integer, DefaultEdge> fcb = new StackBFSFundamentalCycleBasis<>(graph);
        CycleBasis<Integer, DefaultEdge> cb = fcb.getCycleBasis();
        assertEquals(0, cb.getCycles().size());
        assertEquals(0, cb.getLength());
        assertEquals(0d, cb.getWeight(), 1e-9);
    }

    @Test
    public void test1()
    {
        Graph<Integer, DefaultEdge> graph = new SimpleGraph<>(DefaultEdge.class);
        Graphs
            .addAllVertices(
                graph, IntStream.rangeClosed(1, 7).boxed().collect(Collectors.toList()));
        graph.addEdge(1, 2);
        graph.addEdge(1, 3);
        graph.addEdge(2, 4);
        graph.addEdge(2, 5);
        graph.addEdge(3, 6);
        graph.addEdge(3, 7);

        graph.addEdge(4, 5);
        graph.addEdge(6, 7);
        graph.addEdge(4, 6);

        // @formatter:off
        // 
        //        1
        //       /  \
        //      2     3
        //           |  \
        //   4 - 5   6   7
        //   |       |
        //   ---------
        // 
        // @formatter:on
        CycleBasisAlgorithm<Integer, DefaultEdge> fcb = new StackBFSFundamentalCycleBasis<>(graph);
        CycleBasis<Integer, DefaultEdge> cb = fcb.getCycleBasis();

        int[] cyclesSizes = { 5, 6, 3 };
        Iterator<List<DefaultEdge>> it = cb.getCycles().iterator();
        for (int i = 0; i < 3; i++) {
            List<DefaultEdge> cycle = it.next();
            assertCycle(graph, cycle);
            assertEquals(cyclesSizes[i], cycle.size());
        }
        assertEquals(14, cb.getLength());
        assertEquals(14d, cb.getWeight(), 1e-9);
    }

    // assert that a list of edges is a cycle
    private void assertCycle(Graph<Integer, DefaultEdge> g, List<DefaultEdge> edges)
    {
        if (edges.isEmpty()) {
            return;
        }

        boolean isDirected = g.getType().isDirected();
        DefaultEdge prev = null;
        DefaultEdge first = null, last = null;
        Iterator<DefaultEdge> it = edges.iterator();
        Set<DefaultEdge> dupCheck = new HashSet<>();
        while (it.hasNext()) {
            DefaultEdge cur = it.next();
            assertTrue(dupCheck.add(cur));
            if (prev == null) {
                first = cur;
            } else {
                if (isDirected) {
                    assertTrue(g.getEdgeSource(cur).equals(g.getEdgeTarget(prev)));
                } else {
                    assertTrue(
                        g.getEdgeSource(cur).equals(g.getEdgeSource(prev))
                            || g.getEdgeSource(cur).equals(g.getEdgeTarget(prev))
                            || g.getEdgeTarget(cur).equals(g.getEdgeSource(prev))
                            || g.getEdgeTarget(cur).equals(g.getEdgeTarget(prev)));
                }
            }
            if (!it.hasNext()) {
                last = cur;
            }
            prev = cur;
        }
        if (edges.size() > 1) {
            if (isDirected) {
                assertTrue(g.getEdgeSource(first).equals(g.getEdgeTarget(last)));
            } else {
                assertTrue(
                    g.getEdgeSource(first).equals(g.getEdgeSource(last))
                        || g.getEdgeSource(first).equals(g.getEdgeTarget(last))
                        || g.getEdgeTarget(first).equals(g.getEdgeSource(last))
                        || g.getEdgeTarget(first).equals(g.getEdgeTarget(last)));
            }
        }
    }

}
