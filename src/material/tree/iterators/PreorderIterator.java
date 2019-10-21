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
    private final Deque<Position<E>> nodeDeque = new LinkedList<>();
    private final Tree<E> tree;
    private Predicate<Position<E>> hasPredicate;

    public PreorderIterator (Tree<E> tree) {
        this.tree = tree;
        if (!this.tree.isEmpty())
            nodeDeque.addFirst(this.tree.root());
    }

    public PreorderIterator (Tree<E> tree, Position<E> start) {
        this.tree = tree;
        nodeDeque.addFirst(start);
    }

    //PreorderIterator with predicates
    public PreorderIterator (Tree<E> tree, Position<E> start, Predicate<Position<E>> predicate) {
        this.tree = tree;
        this.hasPredicate = predicate;
        getValidPredIteration(start, predicate);
    }

    @Override
    public boolean hasNext () {
        return (!nodeDeque.isEmpty());
    }

    @Override
    public Position<E> next () {
        Position<E> aux = nodeDeque.removeFirst();

        //As I have the auxiliary method that will iterate in case that a predicate exists, in that case I only need to remove
        //the first element on my deque, in the other hand if a predicate is missing I have to take care of the deque elements order.
        if (this.hasPredicate == null) {
            Deque<Position<E>> auxDeque = new LinkedList<>();

            if (!nodeDeque.isEmpty()) {
                for (Position<E> p : nodeDeque) {
                    auxDeque.addLast(p);
                }
            }

            nodeDeque.clear();

            for (Position<E> node : tree.children(aux)) {
                nodeDeque.addLast(node);
            }

            for (Position<E> n : auxDeque) {
                nodeDeque.addLast(n);
            }
        }

        return aux;
    }

    //No need to implement.
    @Override
    public void remove () {
        throw new UnsupportedOperationException("Not implemented in Java.");
    }

    private void getValidPredIteration (Position<E> p, Predicate<Position<E>> pred) {
        if (pred.test(p))
            nodeDeque.addLast(p);

        //The tree that is being iterated has an inorder iterator so this enhanced for, will
        //iterate in an inorder way and will save the elements in the correct order in which it'll be returned afterwards.
        for (Position<E> node: this.tree.children(p)) {
            getValidPredIteration(node, pred);
        }
    }

}


