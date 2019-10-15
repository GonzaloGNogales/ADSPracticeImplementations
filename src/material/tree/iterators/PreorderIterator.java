package material.tree.iterators;

import material.Position;
import material.tree.Tree;

import java.util.*;
import java.util.function.Predicate;

/**
 * Generic preorder iterator for trees.
 *
 * @param <E>
 * @author A. Duarte, J. Vélez, J. Sánchez-Oro, JD. Quintana
 */
//TODO: Practica 2 Ejercicio 3
public class PreorderIterator<E> implements Iterator<Position<E>> {
    private final Deque<Position<E>> nodeDeque;
    private final Tree<E> tree;

    public PreorderIterator(Tree<E> tree) {
        nodeDeque = new LinkedList<>();
        this.tree = tree;
        if (!this.tree.isEmpty())
            nodeDeque.addFirst(this.tree.root());
    }

    public PreorderIterator(Tree<E> tree, Position<E> start) {
        nodeDeque = new LinkedList<>();
        this.tree = tree;
        nodeDeque.addFirst(start);
    }

    //Review PreorderIterator with predicates (learn how to use Predicates)
    public PreorderIterator(Tree<E> tree, Position<E> start, Predicate<Position<E>> predicate) {
        nodeDeque = new LinkedList<>();
        this.tree = tree;
        nodeDeque.addFirst(start);

        if (!predicate.test(start)) {
            while (!predicate.test(start)) {
                start = next();
            }
            nodeDeque.clear();
            nodeDeque.addFirst(start);
        }
    }


    @Override
    public boolean hasNext() {
        return (nodeDeque.size() != 0);
    }

    @Override
    public Position<E> next() {
        Position<E> aux = nodeDeque.removeFirst();
        Deque<Position<E>> auxDeque = new LinkedList<>();

        for (Position<E> p: nodeDeque) {
            auxDeque.addLast(p);
        }

        nodeDeque.clear();

        for (Position<E> node : tree.children(aux)) {
            nodeDeque.addLast(node);
        }
        for (Position<E> n: auxDeque) {
            nodeDeque.addLast(n);
        }

        return aux;
    }

}
