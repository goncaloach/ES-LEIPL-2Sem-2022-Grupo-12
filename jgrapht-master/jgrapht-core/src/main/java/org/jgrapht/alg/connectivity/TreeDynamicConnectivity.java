/*
 * (C) Copyright 2020-2021, by Timofey Chudakov and Contributors.
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
package org.jgrapht.alg.connectivity;

import org.jgrapht.util.*;

import java.util.*;

import static org.jgrapht.util.AVLTree.TreeNode;

/**
 * Data structure for storing dynamic trees and querying node connectivity
 * <p>
 * This data structure supports the following operations:
 * <ul>
 * <li>Adding an element in $\mathcal{O}(\log 1)$</li>
 * <li>Checking if an element in present in $\mathcal{O}(1)$</li>
 * <li>Connecting two elements in $\mathcal{O}(\log n)$</li>
 * <li>Checking if two elements are connected in $\mathcal{O}(\log n)$</li>
 * <li>Removing connection between two nodes in $\mathcal{O}(\log n)$</li>
 * <li>Removing an element in $\mathcal{O}(deg(element)\cdot\log n + 1)$</li>
 * </ul>
 * <p>
 * This data structure doesn't allow to store graphs with cycles. Also, the edges are considered to
 * be undirected. The memory complexity is linear in the number of inserted elements. The
 * implementation is based on the <a href="https://en.wikipedia.org/wiki/Euler_tour_technique">
 * Euler tour technique</a>.
 * <p>
 * For the description of the Euler tour data structure, we refer to the <i>Monika Rauch Henzinger,
 * Valerie King: Randomized dynamic graph algorithms with polylogarithmic time per operation. STOC
 * 1995: 519-527</i>
 *
 * @param <T> element type
 * @author Timofey Chudakov
 */
public class TreeDynamicConnectivity<T>
{

    private TreeDynamicConnectivityProduct<T> tdcProduct = new TreeDynamicConnectivityProduct<T>();
    /**
     * Mapping from tree minimums to the trees they're stored in. This map contains one entry per
     * each tree, which has at least two nodes.
     */
    private Map<TreeNode<T>, AVLTree<T>> minToTreeMap;
    /**
     * Mapping from the user specified values to the internal nodes they're represented by
     */
    private Map<T, Node<T>> nodeMap;
    /**
     * Mapping from zero-degree nodes to their trees. This map contains one entry for each
     * zero-degree node
     */
    private Map<Node<T>, AVLTree<T>> singletonNodes;

    /**
     * Constructs a new {@code TreeDynamicConnectivity} instance
     */
    public TreeDynamicConnectivity()
    {
        minToTreeMap = new HashMap<>();
        nodeMap = new HashMap<>();
        singletonNodes = new HashMap<>();
    }

    /**
     * Adds an {@code element} to this data structure. If the {@code element} has been added before,
     * this method returns {@code false} and has no effect.
     * <p>
     * This method has $\mathcal{O}(\log 1)$ running time complexity
     *
     * @param element an element to add
     * @return {@code true} upon successful modification, {@code false} otherwise
     */
    public boolean add(T element)
    {
        if (contains(element)) {
            return false;
        }

        AVLTree<T> newTree = new AVLTree<>();
        Node<T> node = new Node<T>(element);

        nodeMap.put(element, node);
        singletonNodes.put(node, newTree);

        return true;
    }

    /**
     * Removes the {@code element} from this data structure. This method has no effect if the
     * {@code element} hasn't been added to this data structure
     * <p>
     * This method has $\mathcal{O}(deg(element)\cdot\log n + 1)$ running time complexity
     *
     * @param element an element to remove
     * @return {@code true} upon successful modification, {@code false} otherwise
     */
    public boolean remove(T element)
    {
        if (!contains(element)) {
            return false;
        }

        Node<T> node = getNode(element);
        while (!node.isSingleton()) {
            T targetValue = node.arcs.getLast().target.value;
            cut(element, targetValue);
        }

        nodeMap.remove(element);
        singletonNodes.remove(node);
        return true;
    }

    /**
     * Checks if this data structure contains the {@code element}.
     * <p>
     * This method has expected $\mathcal{O}(1)$ running time complexity
     *
     * @param element an element to check presence of
     * @return {@code true} if the {@code element} is stored in this data structure, {@code false}
     *         otherwise
     */
    public boolean contains(T element)
    {
        return nodeMap.containsKey(element);
    }

    /**
     * Adds an edge between the {@code first} and {@code second} elements. The method has no effect
     * if the elements are already connected by some path, i.e. belong to the same tree. In the case
     * some of the nodes haven't been added before, they're added to this data structure.
     * <p>
     * This method has $\mathcal{O}(\log n)$ running time complexity
     *
     * @param first an element
     * @param second an element
     * @return {@code true} upon successful modification, {@code false} otherwise
     */
    public boolean link(T first, T second)
    {
        /*
         * Example: we have two trees [1 - 2] and [3 - 4 - 5]
         *
         * Euler tour of the first tree: [1 - 2] Euler tour of the second tree: [3 - 4 - 5 - 4]
         *
         * By invariant used in this implementation, we do not return to the start node
         *
         * Suppose, that we have a request: link(1, 5)
         */

        addIfAbsent(first);
        addIfAbsent(second);

        if (connected(first, second)) {
            return false;
        }

        Node<T> firstNode = getNode(first);
        Node<T> secondNode = getNode(second);

        AVLTree<T> firstTree = getTree(firstNode);
        AVLTree<T> secondTree = getTree(secondNode);

        minToTreeMap.remove(firstTree.getMin());
        minToTreeMap.remove(secondTree.getMin());

        /*
         * First we make the nodes 1 and 5 the roots of the corresponding trees:
         *
         * [1 - 2] --> [1 - 2] [3 - 4 - 5 - 4] --> [5 - 4 - 3 - 4]
         */
        tdcProduct.makeRoot(firstTree, firstNode);
        tdcProduct.makeRoot(secondTree, secondNode);

        /*
         * Add one more occurrence for the first element to the second tree:
         *
         * [5 - 4 - 3 - 4] --> [1 - 5 - 4 - 3 - 4]
         */
        TreeNode<T> newFirstOccurrence = secondTree.addMin(first);
        Arc<T> newFirstArc = new Arc<T>(secondNode, newFirstOccurrence);
        if (firstNode.isSingleton()) {
            // newFirstArc becomes the first arc of the first node
            singletonNodes.remove(firstNode);
            firstNode.addArcLast(newFirstArc);
        } else {
            /*
             * Since second element will be not the only element adjacent to the first element, we
             * are going to insert the arc to the second element into the circular list of arcs of
             * the first node
             *
             * Since first element is a root currently, we can find out the last outgoing arc by
             * simply checking the last element in its Euler tour
             *
             * In the example above, the last arc is (1, 2), so we're going to append a new arc (1,
             * 5) after it.
             *
             * By invariant we're maintaining, a subtree tour computed by following the arc is
             * placed after the arc tree node reference.
             *
             * For example, the first node will have 2 arcs: (1, 2) and (1, 5). If we follow the arc
             * (1, 2), a subtour will be just [2]. If we follow the arc (1, 5), the subtour will be
             * [5 - 4 - 3 - 4 - 5]. So, the arc will have the following tree node references
             *
             * (1, 2) [(1) - 2 - 1 - 5 - 4 - 3 - 4 - 5] | | ------------ (1, 5) [1 - 2 - (1) - 5 - 4
             * - 3 - 4 - 5] | | --------------------
             *
             * If we decide to make the arc (1, 5) the first arc, the method will just take the tree
             * node reference of the arc (1, 5) and will place it at the first place (1, 5) [(1) - 5
             * - 4 - 3 - 4 - 5 - 1 - 2] | | ------------ (1, 2) [1 - 5 - 4 - 3 - 4 - 5 - (1) - 2] |
             * | ------------------------------------
             */
            T lastChild = firstTree.getMax().getValue();
            Node<T> lastChildNode = getNode(lastChild);
            Arc<T> arcToLastChild = firstNode.getArcTo(lastChildNode);
            firstNode.addArcAfter(arcToLastChild, newFirstArc);
        }

        /*
         * Add one more occurrence for the second element to the second tree:
         *
         * [1 - 5 - 4 - 3 - 4] -> [1 - 5 - 4 - 3 - 4 - 5]
         *
         */
        TreeNode<T> newSecondOccurrence = secondTree.addMax(second);
        Arc<T> newSecondArc = new Arc<T>(firstNode, newSecondOccurrence);
        if (secondNode.isSingleton()) {
            // newSecondArc becomes the first arc of the second node
            singletonNodes.remove(secondNode);
            secondNode.addArcLast(newSecondArc);
        } else {
            /*
             * Similarly to the first case, we need to find out the last arc of the second node. At
             * this moment, the second tree looks like this:
             *
             * [1 - 5 - 4 - 3 - 4 - 5]
             *
             * The only arc of the node 5 is (5, 4). After the link operation, the node five will
             * have one more arc: (5, 1). The tree node references for the node 5 will look like
             * this:
             *
             * (5, 4) [1 - 2 - 1 - (5) - 4 - 3 - 4 - 5] | | -------------------------- (5, 1) [1 - 2
             * - 1 - 5 - 4 - 3 - 4 - (5)] | | ------------------------------------------
             *
             * Note that the invariant of the arc tree node references has a circular manner: the
             * subtree tour of the arc (5, 1) is [1 - 2 - 1], which is right after the tree node
             * reference of the arc (5, 1).
             */
            T lastChild = secondTree.getMax().getPredecessor().getValue();
            Node<T> lastChildNode = getNode(lastChild);
            Arc<T> arcToLastChild = secondNode.getArcTo(lastChildNode);
            secondNode.addArcAfter(arcToLastChild, newSecondArc);
        }

        /*
         * Merge the first and the second tree to obtain an Euler tour of the combined tree:
         *
         * [1 - 2] + [1 - 5 - 4 - 3 - 4 - 5] = [1 - 2 - 1 - 5 - 4 - 3 - 4 - 5]
         */
        firstTree.mergeAfter(secondTree);
        minToTreeMap.put(firstTree.getMin(), firstTree);

        return true;
    }

    /**
     * Checks if the {@code first} and {@code second} belong to the same tree. The method will
     * return {@code false} if either of the elements hasn't been added to this data structure
     * <p>
     * This method has $\mathcal{O}(\log n)$ running time complexity
     *
     * @param first an element
     * @param second an element
     * @return {@code true} if the elements belong to the same tree, {@code false} otherwise
     */
    public boolean connected(T first, T second)
    {
        if (!contains(first) || !contains(second)) {
            return false;
        }
        Node<T> firstNode = getNode(first);
        if (firstNode.isSingleton()) {
            return false;
        }
        Node<T> secondNode = getNode(second);
        if (secondNode.isSingleton()) {
            return false;
        }
        return getTree(firstNode) == getTree(secondNode);
    }

    /**
     * Removes an edge between the {@code first} and {@code second}. This method doesn't have any
     * effect if there's no edge between these elements
     * <p>
     * This method has $\mathcal{O}(\log n)$ running time complexity
     *
     * @param first an element
     * @param second an element
     * @return {@code true} upon successful modification, {@code false} otherwise
     */
    public boolean cut(T first, T second)
    {
        if (!connected(first, second)) {
            return false;
        }
        /*
         * Suppose, we have a tree [2 - [1] - 5 - 4 - 3], which has the following Euler tour:
         *
         * [1 - 2 - 1 - 5 - 4 - 3 - 4 - 5]
         *
         * Let's assume that we received a query: cut(1, 2)
         */
        Node<T> firstNode = getNode(first);
        Node<T> secondNode = getNode(second);

        AVLTree<T> tree = getTree(firstNode);
        minToTreeMap.remove(tree.getMin());

        /*
         * The arcToSecond is (1, 2). The operation of making the arc (1, 2) the last arc will
         * transform the tree as follows:
         *
         * (1, 2) [(1) - 2 - 1 - 5 - 4 - 3 - 4 - 5] --> [1 - 5 - 4 - 3 - 4 - 5 - (1) - 2] | | |
         * --------------------------------------------------------------------------
         *
         * After this operation, a subtree of the arc (1, 2) is at the end of the Euler tour
         */
        Arc<T> arcToSecond = firstNode.getArcTo(secondNode);
        if (arcToSecond == null) {
            throw new IllegalArgumentException(
                    String.format("Elements {%s} and {%s} are not connected", first, second));
        }
        tdcProduct.makeLastArc(tree, firstNode, arcToSecond);

        /*
         * Now we remove the subtree of the arc (1, 2) from the Euler tour:
         *
         * |-------> [1 - 5 - 4 - 3 - 4 - 5 - 1] (left part [1 - 5 - 4 - 3 - 4 - 5 - 1 - 2] -----|
         * |-------> [2] (right part)
         */
        AVLTree<T> right = tree.splitAfter(arcToSecond.arcTreeNode);

        /*
         * Removing the last occurrence of the element 1 from the Euler tour:
         *
         * [1 - 5 - 4 - 3 - 4 - 5 - 1] --> [1 - 5 - 4 - 3 - 4 - 5]
         *
         * Now the left part is a valid Euler tour
         */
        tree.removeMax();
        firstNode.removeArc(arcToSecond);
        if (!firstNode.isSingleton()) {
            minToTreeMap.put(tree.getMin(), tree);
        } else {
            singletonNodes.put(firstNode, tree);
        }

        /*
         * Removing the last occurrence of the element 2 from the right tree:
         *
         * [2] -> []
         *
         * The element 2 becomes an element of zero degree (a singleton node). No arcs means an
         * empty tree
         *
         * That's why we place it to the map for zero degree nodes
         */
        Arc<T> secondToFirst = secondNode.getArcTo(firstNode);
        right.removeMax();
        secondNode.removeArc(secondToFirst);
        if (!secondNode.isSingleton()) {
            minToTreeMap.put(right.getMin(), right);
        } else {
            singletonNodes.put(secondNode, right);
        }

        return true;
    }

    /**
     * Returns an internal representation of the {@code element}
     *
     * @param element a user specified node element
     * @return an internal representation of the {@code element}
     */
    private Node<T> getNode(T element)
    {
        return nodeMap.get(element);
    }

    /**
     * Returns a binary tree, which contains an Euler tour of the tree the {@code node} belong to
     *
     * @param node a node
     * @return a corresponding binary tree an Euler tour is stored in
     */
    private AVLTree<T> getTree(Node<T> node)
    {
        if (node.isSingleton()) {
            return singletonNodes.get(node);
        }
        return minToTreeMap.get(node.arcs.get(0).arcTreeNode.getTreeMin());
    }

    /**
     * Adds the {@code element} to this data structure if it is not already present
     *
     * @param element a user specified element
     */
    private void addIfAbsent(T element)
    {
        if (!contains(element)) {
            add(element);
        }
    }
}
