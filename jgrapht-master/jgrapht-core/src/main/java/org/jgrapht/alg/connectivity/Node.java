package org.jgrapht.alg.connectivity;

import java.util.HashMap;
import java.util.Map;
import java.util.stream.Collectors;

import org.jgrapht.alg.connectivity.Arc;
import org.jgrapht.util.DoublyLinkedList;


/**
 * An internal representation of the tree nodes.
 * <p>
 * Keeps track of the node values and outgoing arcs. The outgoing arcs are placed according to
 * the order they are traversed in the Euler tour
 */

class Node<T>
{
    /**
     * Node value
     */
    T value;
    /**
     * Arcs list
     */
    DoublyLinkedList<Arc<T>> arcs;
    /**
     * Target node to arc mapping
     */
    Map<Node<T>, Arc<T>> targetMap;

    /**
     * Constructs a new node
     *
     * @param value a user specified element to store in this node
     */
    public Node(T value)
    {
        this.value = value;
        arcs = new DoublyLinkedList<>();
        targetMap = new HashMap<>();
    }

    /**
     * Removes the {@code arc} from the arc list
     *
     * @param arc an arc to remove
     */
    void removeArc(Arc<T> arc)
    {
        arcs.removeNode(arc.listNode);
        arc.listNode = null;
        targetMap.remove(arc.target);
    }

    /**
     * Append the {@code arc} to the arc list
     *
     * @param arc an arc to add
     */
    void addArcLast(Arc<T> arc)
    {
        arc.listNode = arcs.addElementLast(arc);
        targetMap.put(arc.target, arc);
    }

    /**
     * Inserts the {@code newArc} in the arc list after the {@code arc}
     *
     * @param arc an arc already stored in the arc list
     * @param newArc a new arc to add to the arc list
     */
    void addArcAfter(Arc<T> arc, Arc<T> newArc)
    {
        newArc.listNode = arcs.addElementBeforeNode(arc.listNode.getNext(), newArc);
        targetMap.put(newArc.target, newArc);
    }

    /**
     * Returns an arc, which target is equal to the {@code node}
     *
     * @param node a target of the returned arc
     * @return an arc, which target is equal to the {@code node}
     */
    Arc<T> getArcTo(Node<T> node)
    {
        return targetMap.get(node);
    }

    /**
     * Returns an arc which is stored right after the {@code arc}. The result may be equal to the {@code arc}
     *
     * @param arc an arc stored in the arc list
     * @return an arc which is stored right after the {@code arc}
     */
    Arc<T> getNextArc(Arc<T> arc)
    {
        return arc.listNode.getNext().getValue();
    }

    /**
     * Checks if this node is a zero-degree node
     *
     * @return {@code true} if this node is a singleton node, {@code false otherwise}
     */
    public boolean isSingleton()
    {
        return arcs.isEmpty();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String toString()
    {
        return String
                .format(
                        "{%s} -> [%s]", value,
                        arcs
                                .stream().map(a -> a.target.value.toString())
                                .collect(Collectors.joining(",")));
    }
}
