package org.jgrapht.alg.connectivity;


import org.jgrapht.util.AVLTree;

public class TreeDynamicConnectivityProduct<T> {
    /**
     * Makes the  {@code  node}  the root of the tree. In practice, this means that the value of the {@code  node}  is the first in the Euler tour
     * @param tree  a tree the  {@code  node}  is stored in
     * @param node  a node to make a root
     */
    public void makeRoot(AVLTree<T> tree, Node<T> node) {
        if (node.arcs.isEmpty()) {
            return;
        }
        makeFirstArc(tree, node.arcs.get(0));
    }

    /**
     * Makes the  {@code  arc}  the first arc traversed by the Euler tour
     * @param tree  corresponding binary tree the Euler tour is stored in
     * @param arc  an arc to use for tree re-rooting
     */
    public void makeFirstArc(AVLTree<T> tree, Arc<T> arc) {
        AVLTree<T> right = tree.splitBefore(arc.arcTreeNode);
        tree.mergeBefore(right);
    }

    /**
     * Makes the  {@code  arc}  the last arc of the  {@code  node}  according to the Euler tour
     * @param tree  corresponding binary tree the Euler tour is stored in
     * @param node  a new root node
     * @param arc  an arc incident to the  {@code  node}
     */
    public void makeLastArc(AVLTree<T> tree, Node<T> node, Arc<T> arc) {
        if (node.arcs.size() == 1) {
            makeRoot(tree, node);
        } else {
            Arc<T> nextArc = node.getNextArc(arc);
            makeFirstArc(tree, nextArc);
        }
    }
}
