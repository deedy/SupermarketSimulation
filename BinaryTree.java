package HomeworkFinal;

import java.util.Iterator;
/** A simple Binary Tree Interface
 * with simple functionality like add remove, find, get height, get iterator
 * and various ways of representing and visualizing the BinaryTree
 * @author Deedy
 *
 * @param <E>
 */
public interface BinaryTree<E> {
    /** Insert element into tree */
    public void add(E x);
    /** Remove element from tree */
    public void remove(E x);
    /** Find an item in the tree. */
    public E find (E x);
    /** Test if the tree is logically empty. */
    public boolean isEmpty();
    /** Returns the number of unique elements of the tree */
    public int size();
    /** Return String representation of this Binary Tree */
    public String toString ();
    /** Return Height of Binary Tree */
    public int heightOfBinaryTree(BSTNode<E> node);
    /** Return Visual representation of this Binary Tree */
    public void VisualDisplay ();
    /** Return Visual representation of this Binary Tree Without Frequencies attached */
    public void VisualDisplayWithoutFrequency ();
    /** Returns an iterator of all the elements in the binarysearchtree. */
	public Iterator<E> iterator();
}