package material.tree.binarytree;

import material.Position;
import material.tree.iterators.InorderBinaryTreeIterator;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;

public class ArrayBinaryTree<E> implements BinaryTree<E> {
    //TODO: Practica 3 Ejercicio 2
    private int size;
    private int root;
    private BTPos<E>[] treeArray;
    private class BTPos<T> implements Position<T> {
        private T element;
        private int rank;

        public BTPos(T element, int rank) {
            this.element = element;
            this.rank = rank;
        }

        @Override
        public T getElement() {
            return element;
        }

        public void setElement(T element) {
            this.element = element;
        }

        public int getRank() {
            return rank;
        }

        public void setRank(int rank) {
            this.rank = rank;
        }

    }

    public ArrayBinaryTree () {
        this.size = 0;
        this.root = 0;
        this.treeArray = (BTPos<E>[]) new BTPos[50];
    }

    public ArrayBinaryTree (int capacity) {
        this.size = 0;
        this.root = 0;
        this.treeArray = (BTPos<E>[]) new BTPos[capacity];
    }

    @Override
    public Position<E> left(Position<E> v) throws RuntimeException {
        BTPos<E> node = checkPosition(v);
        BTPos<E> leftPos = this.treeArray[node.getRank()*2];
        if (leftPos == null) {
            throw new RuntimeException("No left child");
        }
        return leftPos;
    }

    @Override
    public Position<E> right(Position<E> v) throws RuntimeException {
        BTPos<E> node = checkPosition(v);
        BTPos<E> rightPos = this.treeArray[(node.getRank()*2)+1];
        if (rightPos == null) {
            throw new RuntimeException("No right child");
        }
        return rightPos;
    }

    @Override
    public boolean hasLeft(Position<E> v) {
        BTPos<E> node = checkPosition(v);
        BTPos<E> leftPos = this.treeArray[node.getRank()*2];

        return (leftPos != null);
    }

    @Override
    public boolean hasRight(Position<E> v) {
        BTPos<E> node = checkPosition(v);
        BTPos<E> rightPos = this.treeArray[(node.getRank()*2)+1];

        return (rightPos != null);
    }

    @Override
    public E replace(Position<E> p, E e) {
        BTPos<E> node = checkPosition(p);
        E temp = node.getElement();
        node.setElement(e);
        return temp;
    }

    @Override
    public Position<E> sibling(Position<E> p) throws RuntimeException {
        BTPos<E> node = checkPosition(p);
        if (!isRoot(node)) {
            Position<E> parent = this.parent(p);

            if (node.getRank() % 2 == 0) {
                if (this.hasRight(parent))
                    return this.treeArray[node.getRank() + 1];
            } else {
                if (this.hasLeft(parent))
                    return this.treeArray[node.getRank() - 1];

            }
        }

        throw new RuntimeException("No sibling");
    }

    @Override
    public Position<E> insertLeft(Position<E> p, E e) throws RuntimeException {
        BTPos<E> node = checkPosition(p);
        if (hasLeft(node)) {
            throw new RuntimeException("Node already has a left child");
        }
        int leftPos = node.getRank()*2;
        BTPos<E> newNode = new BTPos<>(e, leftPos);
        this.treeArray[leftPos] = newNode;
        this.size++;
        return newNode;
    }

    @Override
    public Position<E> insertRight(Position<E> p, E e) throws RuntimeException {
        BTPos<E> node = checkPosition(p);
        if (hasRight(node)) {
            throw new RuntimeException("Node already has a right child");
        }
        int rightPos = (node.getRank()*2)+1;
        BTPos<E> newNode = new BTPos<>(e, rightPos);
        treeArray[rightPos] = newNode;
        this.size++;
        return newNode;
    }

    @Override
    public E remove(Position<E> p) throws RuntimeException {
        //Reallocation of the array heritage left for making the tree resize properly.
        BTPos<E> node = checkPosition(p);
        BTPos<E> leftChild = this.treeArray[node.getRank()*2];
        BTPos<E> rightChild = this.treeArray[(node.getRank()*2)+1];

        if (hasLeft(node) && hasRight(node)) {
            throw new RuntimeException("Cannot remove node with two children");
        }

        //the only child of p, if any, null otherwise
        BTPos<E> child = leftChild != null ? leftChild : rightChild;
        //child = this.treeArray[node.getRank()*2]; || child = this.treeArray[(node.getRank()*2)+1];

        if (node == this.treeArray[this.root]) { // p is the root
            this.treeArray[this.root] = child;
            if (child != null) {
                reallocArray(this.root,child.getRank());
                child.setRank(this.root);
            }
        }
        else { // p is not the root
            BTPos<E> parent;
            BTPos<E> parentLeft;
            BTPos<E> parentRight;

            if (node.getRank()%2 == 0)
                parent = this.treeArray[node.getRank()/2];
            else
                parent = this.treeArray[(node.getRank()-1)/2];

            parentLeft = this.treeArray[parent.getRank()*2];
            parentRight = this.treeArray[(parent.getRank()*2)+1];

            if (node == parentLeft) {
                this.treeArray[parentLeft.getRank()] = child;
                if (child != null) {
                    reallocArray(parentLeft.getRank(),child.getRank());
                    child.setRank(parentLeft.getRank());
                }
            }
            else {
                this.treeArray[parentRight.getRank()] = child;
                if (child != null) {
                    reallocArray(parentRight.getRank(), child.getRank());
                    child.setRank(parentRight.getRank());
                }
            }
        }
        this.size--;
        return p.getElement();
    }

    private void reallocArray (int actualRank, int lastRank) {
        if (this.treeArray[lastRank*2] != null) {
            BTPos<E> left = this.treeArray[lastRank*2];
            int keepLastLeftRank = left.getRank();
            this.treeArray[actualRank*2] = left;
            left.setRank(actualRank*2);
            reallocArray(left.getRank(),keepLastLeftRank);
        }
        if (this.treeArray[(lastRank*2)+1] != null){
            BTPos<E> right = this.treeArray[(lastRank*2)+1];
            int keepLastRightRank = right.getRank();
            this.treeArray[(actualRank*2)+1] = right;
            right.setRank((actualRank*2)+1);
            reallocArray(right.getRank(),keepLastRightRank);
        }
    }

    @Override
    public void swap(Position<E> p1, Position<E> p2) {
        BTPos<E> node1 = checkPosition(p1);
        BTPos<E> node2 = checkPosition(p2);

        BTPos<E> copyNode1 = new BTPos<>(node1.getElement(),node1.getRank());
        node1.setRank(node2.getRank());
        node2.setRank(copyNode1.getRank());

        this.treeArray[node2.getRank()] = node2;
        this.treeArray[node1.getRank()] = node1;
    }

    @Override
    public BinaryTree<E> subTree(Position<E> v) {
        BTPos<E> node = checkPosition(v);
        ArrayBinaryTree<E> subTree = new ArrayBinaryTree<>();
        subTree.root = 1;

        if (node == this.treeArray[this.root]) {
            subTree.size = this.size;
            for (int i = 0; i < this.treeArray.length; i++) {
                subTree.treeArray[i] = this.treeArray[i];
                this.treeArray[i] = null;
                this.size--;
            }
        }
        else
            recursiveInsertionAndDeletion(this,subTree,node,1);

        return subTree;
    }

    private void recursiveInsertionAndDeletion (ArrayBinaryTree<E> thisTree, ArrayBinaryTree<E> tree, BTPos<E> node, int pos) {
        if (node != null) {
            tree.treeArray[pos] = node;
            thisTree.treeArray[node.getRank()] = null;
            thisTree.size--;
            tree.size++;

            if (this.hasLeft(node)) {
                BTPos<E> left = this.treeArray[node.getRank()*2];
                recursiveInsertionAndDeletion(this,tree,left,pos*2);
            }
            else if (this.hasRight(node)) {
                BTPos<E> right = this.treeArray[(node.getRank()*2)+1];
                recursiveInsertionAndDeletion(this,tree,right,(pos*2)+1);
            }
        }
    }

    @Override
    public void attachLeft(Position<E> p, BinaryTree<E> tree) throws RuntimeException {
        BTPos<E> node = checkPosition(p);

        if (tree == this) {
            throw new RuntimeException("Cannot attach a tree over himself");
        }
        if (this.hasLeft(p))
            throw new RuntimeException("Node already has a left child");

        if (tree != null && !tree.isEmpty()) {
            //Check position will fail if tree is not an instance of ArrayBinaryTree
            BTPos<E> r = checkPosition(tree.root());
            this.treeArray[node.getRank()*2] = r;
            r.setRank(node.getRank()*2);
            this.size += tree.size();
        }
    }

    @Override
    public void attachRight(Position<E> p, BinaryTree<E> tree) throws RuntimeException {
        BTPos<E> node = checkPosition(p);

        if (tree == this) {
            throw new RuntimeException("Cannot attach a tree over himself");
        }
        if (this.hasRight(p))
            throw new RuntimeException("Node already has a right child");

        if (tree != null && !tree.isEmpty()) {
            //Check position will fail if tree is not an instance of ArrayBinaryTree
            BTPos<E> r = checkPosition(tree.root());
            this.treeArray[node.getRank()*2+1] = r;
            r.setRank(node.getRank()*2+1);
            this.size += tree.size();
        }
    }

    @Override
    public boolean isComplete() {
        int countLeaf = 0;
        int countComplete = 0;

        for (Position<E> p : treeArray) {
            if (p != null) {
                if ((!this.isLeaf(p))) {
                    if ((this.hasLeft(p)) && (this.hasRight(p)))
                        countComplete++;
                }
                else
                    countLeaf++;
            }
        }

        return (countComplete == (this.size-countLeaf));
    }

    @Override
    public int size() {
        return this.size;
    }

    @Override
    public boolean isEmpty() {
        return (this.size == 0);
    }

    @Override
    public Position<E> root() throws RuntimeException {
        if (this.isEmpty()) {
            throw new RuntimeException("The tree is empty");
        }
        return treeArray[this.root];
    }

    @Override
    public Position<E> parent(Position<E> v) throws RuntimeException {
        BTPos<E> node = checkPosition(v);
        int parentPos;
        if (node.getRank()%2 == 0)
            parentPos = node.getRank()/2;
        else
            parentPos = (node.getRank()-1)/2;

        if (this.treeArray[parentPos] == null) {
            throw new RuntimeException("No parent");
        }

        return this.treeArray[parentPos];
    }

    @Override
    public Iterable<? extends Position<E>> children(Position<E> v) {
        List<Position<E>> children = new ArrayList<>();
        checkPosition(v);

        if (hasLeft(v)) {
            children.add(left(v));
        }
        if (hasRight(v)) {
            children.add(right(v));
        }

        return Collections.unmodifiableCollection(children);
    }

    @Override
    public boolean isInternal(Position<E> v) {
        checkPosition(v);
        return (hasLeft(v) || hasRight(v));
    }

    @Override
    public boolean isLeaf(Position<E> v) throws RuntimeException {
        return !isInternal(v);
    }

    @Override
    public boolean isRoot(Position<E> v) {
        BTPos<E> node = checkPosition(v);
        return (treeArray[this.root] == node);
    }

    @Override
    public Position<E> addRoot(E e) throws RuntimeException {
        if (!this.isEmpty())
            throw new RuntimeException("Tree already has a root");
        this.root = 1;
        BTPos<E> newNode = new BTPos<>(e,this.root);
        this.treeArray[this.root] = newNode;
        this.size++;
        return newNode;
    }

    @Override
    public Iterator<Position<E>> iterator() {
        return new InorderBinaryTreeIterator<>(this);
    }

    private BTPos<E> checkPosition(Position<E> p) throws RuntimeException {
        if (p == null || !(p instanceof BTPos)) {
            throw new RuntimeException("The position is invalid");
        }
        return (BTPos<E>) p;
    }

}
