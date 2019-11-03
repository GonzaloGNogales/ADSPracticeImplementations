package material.tree.narytree;

import material.Position;
import java.util.Iterator;
import java.util.LinkedList;
import material.tree.iterators.BFSIterator;
import java.util.List;

/**
 * A linked class for a tree where nodes have an arbitrary number of children.
 *
 * @param <E> the elements stored in the tree
 * @author Raul Cabido, Abraham Duarte, Jose Velez, Jesús Sánchez-Oro
 */
public class LCRSTree<E> implements NAryTree<E> {
    //TODO: Practica 2 Ejercicio 2
    LCRSNode<E> root;
    int size;
    private class LCRSNode<E> implements Position<E>{
        private E element;
        private LCRSNode<E> parent;
        private LCRSNode<E> leftChild, rightSibling;
        private LCRSTree<E> myTree;

        public LCRSNode (E e, LCRSNode<E> p, LCRSNode<E> lC, LCRSNode<E> rS, LCRSTree<E> tid) {
            this.element = e;
            this.parent = p;
            this.leftChild = lC;
            this.rightSibling = rS;
            this.myTree = tid;
        }

        @Override
        public E getElement () {
            return element;
        }

        public void setElement (E e) {
            this.element = e;
        }

        public LCRSNode<E> getParent () {
            return parent;
        }

        public void setParent (LCRSNode<E> p) {
            this.parent = p;
        }

        public LCRSNode<E> getLeftChild () {
            return leftChild;
        }

        public void setLeftChild (LCRSNode<E> leftChild) {
            this.leftChild = leftChild;
        }

        public LCRSNode<E> getRightSibling () {
            return rightSibling;
        }

        public void setRightSibling (LCRSNode<E> rightSibling) {
            this.rightSibling = rightSibling;
        }

        public LCRSTree<E> getMyTree () {
            return myTree;
        }

        public void setMyTree (LCRSTree<E> mT) {
            this.myTree = mT;
        }
    }

    @Override
    public int size () {
        return this.size;
    }

    @Override
    public boolean isEmpty () {
        return (this.size == 0);
    }

    @Override
    public Position<E> root () throws RuntimeException {
        if (this.root == null) {
            throw new RuntimeException("The tree is empty");
        }
        return this.root;
    }

    @Override
    public Position<E> parent (Position<E> v) throws RuntimeException {
        LCRSNode<E> node = checkPosition(v);
        LCRSNode<E> parentNode = node.getParent();
        if (parentNode == null) {
            throw new RuntimeException("The node has not parent");
        }
        return parentNode;
    }

    @Override
    public Iterable<? extends Position<E>> children (Position<E> v) {
        LCRSNode<E> node = checkPosition(v);
        LCRSNode<E> nodeLeftChild = node.getLeftChild();
        List<LCRSNode<E>> childList = new LinkedList<>();

        if (nodeLeftChild != null) {
            childList.add(nodeLeftChild);

            while (nodeLeftChild.getRightSibling() != null) {
                childList.add(nodeLeftChild.getRightSibling());
                nodeLeftChild = nodeLeftChild.getRightSibling();
            }
        }


        return childList;
    }

    @Override
    public boolean isInternal (Position<E> v) {
        return !isLeaf(v);
    }

    @Override
    public boolean isLeaf (Position<E> v) throws RuntimeException {
        LCRSNode<E> node = checkPosition(v);
        return (node.getLeftChild() == null);
    }

    @Override
    public boolean isRoot (Position<E> v) {
        LCRSNode<E> node = checkPosition(v);
        return (node == this.root());
    }

    @Override
    public Position<E> addRoot (E e) throws RuntimeException {
        if (!this.isEmpty()) {
            throw new IllegalStateException("Tree already has a root");
        }

        this.size = 1;
        this.root = new LCRSNode<>(e,null,null,null,this);
        return this.root;
    }

    @Override
    public Iterator<Position<E>> iterator () {
        return new BFSIterator<>(this);
    }

    @Override
    public E replace (Position<E> p, E e) {
        LCRSNode<E> node = checkPosition(p);
        E temp = node.getElement();
        node.setElement(e);
        return temp;
    }

    @Override
    public void swapElements (Position<E> p1, Position<E> p2) {
        LCRSNode<E> node1 = checkPosition(p1);
        LCRSNode<E> node2 = checkPosition(p2);
        E temp = node2.getElement();
        node2.setElement(node1.getElement());
        node1.setElement(temp);
    }

    @Override
    public Position<E> add (E element, Position<E> p) {
        LCRSNode<E> nodeParent = checkPosition(p);
        LCRSNode<E> newNode = new LCRSNode<>(element,nodeParent,null,null,this);
        if (nodeParent.getLeftChild() == null)
            nodeParent.setLeftChild(newNode);
        else {
            LCRSNode<E> auxLeftChild = nodeParent.getLeftChild();

            while (auxLeftChild.getRightSibling() != null)
                auxLeftChild = auxLeftChild.getRightSibling();

            auxLeftChild.setRightSibling(newNode);
        }

        size++;
        return newNode;
    }

    @Override
    public void remove (Position<E> p) {
        LCRSNode<E> node = checkPosition(p);
        if (node.getParent() != null) {
            Iterator<Position<E>> it = new BFSIterator<>(this, p);
            int cont = 0;
            while (it.hasNext()) {
                LCRSNode<E> next = checkPosition(it.next());
                next.setMyTree(null);
                cont++;
            }
            size = size - cont;
        } else {
            this.root = null;
            this.size = 0;
        }

        node.setMyTree(null);
    }

    @Override
    public void moveSubtree (Position<E> pOrig, Position<E> pDest) throws RuntimeException {
        LCRSNode<E> origin = checkPosition(pOrig);
        LCRSNode<E> dest = checkPosition(pDest);
        LCRSNode<E> aux = dest;
        boolean isSubTree = false;

        //O(log N)
        while (aux != this.root) {
            if (aux == origin)
                isSubTree = true;

            aux = aux.getParent();
        }

        if (origin == dest)
            throw new RuntimeException("Both positions are the same");
        else if (origin == this.root)
            throw new RuntimeException("Root node can't be moved");
        else if (isSubTree)
            throw new RuntimeException("Target position can't be a sub tree of origin");


        LCRSNode<E> originParent = origin.getParent();
        LCRSNode<E> auxChild = originParent.getLeftChild();
        LCRSNode<E> auxLeftSibling;

        if (auxChild == origin)
            originParent.setLeftChild(auxChild.getRightSibling());
        else {
            int cont = 0;
            auxLeftSibling = auxChild;
            while ((auxChild != null) && (auxChild != origin)) {
                auxChild = auxChild.getRightSibling();
                if (cont >= 1)
                    auxLeftSibling = auxLeftSibling.getRightSibling();

                cont++;

                if (auxChild == origin)
                    auxLeftSibling.setRightSibling(auxChild.getRightSibling());
            }
        }

        if (auxChild != null) {
            auxChild.setParent(dest);
            auxChild.setRightSibling(dest.getLeftChild());
            dest.setLeftChild(auxChild);
        }
    }

    private LCRSNode<E> checkPosition (Position<E> p) throws IllegalStateException {
        if ((p == null) || !(p instanceof LCRSNode)) {
            throw new IllegalStateException("The position is invalid");
        }

        LCRSNode<E> aux = (LCRSNode<E>) p;

        if (aux.getMyTree() != this) {
            throw new IllegalStateException("The node is not from this tree");
        }

        return aux;
    }

    public int posLevel (Position<E> p) {
        int level = 0;
        LCRSNode<E> node = checkPosition(p);

        while (node.getParent() != null) {
            node = node.getParent();
            level++;
        }

        return level+1;
    }
}
