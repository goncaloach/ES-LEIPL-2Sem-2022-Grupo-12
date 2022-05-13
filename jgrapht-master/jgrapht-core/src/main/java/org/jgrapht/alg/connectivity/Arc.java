package org.jgrapht.alg.connectivity;

import static org.jgrapht.util.AVLTree.TreeNode;
import org.jgrapht.util.DoublyLinkedList.ListNode;

/**
 * An internal representation of the tree edges.
 * <p>
 * Two arcs are created for every existing tree edge. This complies with the way an Euler tour
 * is constructed.
 */
class Arc<T>
{
    /**
     * The target of this arc
     */
    Node<T> target;
    /**
     * A list node this arc is stored in. This is needed for constant time query time on the
     * doubly linked list.
     */
    ListNode<Arc<T>> listNode;
    /**
     * The occurrence of the source node, which precedes the subtree Euler tour stored in the
     * binary tree
     */
    TreeNode<T> arcTreeNode;

    /**
     * Constructs a new arc with the target node {@code target} and the tree node reference
     * {@code arcTreeNode}
     *
     * @param target target node of this arc
     * @param arcTreeNode source tree node reference
     */
    public Arc(Node<T> target, TreeNode<T> arcTreeNode)
    {
        this.target = target;
        this.arcTreeNode = arcTreeNode;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String toString()
    {
        return String.format("{%s} -> {%s}", arcTreeNode.getValue(), target.value);
    }
}
