package HomeworkFinal;

import java.util.*;

import HomeworkFinal.BSTNode;
/** The Binary Search Tree Iterator Class
 * The Binary Search Tree iterates over a Binary Search Tree in inorder, thus 
 * giving the tree in ascending order using Stacks.
 * @author Deedy
 *
 * @param <E>
 */
public class BSTIterator<E extends Comparable> implements Iterator<E> { 
	protected BinarySearchTree<E> bst = null; //The Binary Search Tree to be iterated over
	protected BSTNode<E> root = null; //The BSTNode supposed to point to the root of the BinarySearchTree
    protected Stack<BSTNode<E>> visiting = new Stack<BSTNode<E>>(); //A Stack which contains the Binary Search Tree Nodes visited
    protected Stack<Boolean> visitingRightChild = new Stack<Boolean>(); //A Stack which contains booleans as to whether the right child of the subtree was visiteds
   

    /** Constructor */
    public BSTIterator(BinarySearchTree<E> bst) {
    	this.bst=bst;
        this.root = bst.root;
        visiting = new Stack<BSTNode<E>>();
        visitingRightChild = new Stack<Boolean>();
    }
    
	/** Returns whether or not the iterator points to a next element */
    public boolean hasNext() {
        return (root != null);
    }
    
    /** Moves the Iterator forward */
    public E next() {
        if (!hasNext()) {
            throw new NoSuchElementException("No Next Element");
        }
        if (visiting.empty()) {	
            pushLeftmostNode(root);
        } 
        BSTNode<E> node = visiting.pop();
        E result = node.value;
        if (node.right != null) {
            BSTNode<E> right = node.right;
            pushLeftmostNode (right);
        }
        if (visiting.empty()) {
            root = null;
        }
        return result;
    }
    
    /** Finds the leftmost node from this root, pushing all the
     * intermediate nodes onto the visiting stack */
    private void pushLeftmostNode(BSTNode<E> node) {
        if (node != null) {
            visiting.push(node); 
            pushLeftmostNode(node.left); 
        }
    }

	/** Removes the node the BST Iterator is pointing to */
	public void remove() {
		bst.remove(this.next());
	}
}

    
    