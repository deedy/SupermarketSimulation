package HomeworkFinal;

/** BST Node Class
 * The Binary Node class simply contains a value and a pointer to the left and right
 * child Binary Node of it. It is special in the way that it also contains a frequency 
 * data member to keep track of frequency in a Binary Search Tree as well.
 * @author Deedy
 *
 * @param <E>
 */
class BSTNode<E> {
 	public BSTNode<E> left; //A pointer to this BSTNode's left child
 	public BSTNode<E> right; // A pointer to this BSTNode's right child
 	public E value; //The value of this BSTNode
 	private int frequency; //The Number of times this value is in the BST
 	/** Constructor */
 	BSTNode(E value) {
 		this.value = value;
 		frequency=1;
 	}
 	/** Constructor 2 */
 	BSTNode(BSTNode<E> node) {
 		left = node.left;
 		right = node.right;
 		value = node.value;
 		frequency=node.frequency;
 	}
 	/** Used to increment the frequency of the value of this BSTNode in the BST */
 	protected void incrementFrequency () {
 		frequency++;
 	}
 	/** Used to decrement the frequency of that the value of this BSTNode in the BST */
 	protected void decrementFrequency () {
 		frequency--;
 	}
 	/** Used to return the frequency of the value of this BSTNode in the BST */
 	protected int getFrequency () {
 		return frequency;
 	}
}