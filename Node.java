package HomeworkFinal;
//A Node class used to implement linked lists
public class Node<E> {
	private E element; //The Element contained as data in the node
	private Node<E> next; //A pointer to the next Node
	//A constructor to initialize the data members
	public Node() {
		this(null,null);
	}
	//An overloaded constructor to initialize the data members
	public Node(E e, Node<E> n) {
		this.element=e;
		this.next=n;
	}
	//Returns the data stored in the Node
	public E getElement() {
		return element;
	}
	//Returns a pointer to the next Node
	public Node<E> getNext() {
		return next;
	}
	//Lets the user set the data stored in this node
	public void setElement(E e) {
		this.element=e;
	}
	//Let the user set the Node this Node points to
	public void setNext(Node<E> n) {
		this.next=n;
	}
}
