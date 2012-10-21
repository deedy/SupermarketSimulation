package HomeworkFinal;
//A Queue data structure implemented with LinkedLists 
//with synchronized enque and deque methods to allow for multithreaded access
public class Queue<E> {
	protected Node<E> head; //A pointer to the node at the beginning of the linked list
	protected Node<E> tail; //A pointer to the node at the end of the linked list
	protected int size; //The size of the queue
	//A constructor to initialize data members
	public Queue() {
		head = null;
		tail = null;
		size = 0; 
	}
	//Returns the size of the Queue
	public int size() { 
		return size; 
	} 
	//Returns true if Queue is empty and false if it is not
	public boolean isEmpty() {
		if (head == null) 
			return true;
		return false; 
		}
	//Adds an element to the back of the queue
	public synchronized void enqueue (E elem) {
		Node<E> node = new Node<E>(); 
		node.setElement(elem);
		node.setNext(null); 
		if (size == 0)
			head = node; 
		else
			tail.setNext(node); 
		tail = node; 
		size++;
		
	}
	//Returns the element at the front of the Queue
	public E front() throws EmptyQueueException {
		if (isEmpty())
			throw new EmptyQueueException("Queue is empty."); 
		return head.getElement();
	}
	//Removes and returns the element at the front of the Queue
	public synchronized E dequeue() throws EmptyQueueException { 
		if (size == 0)
			throw new EmptyQueueException("Queue is empty."); 
		E tmp = head.getElement();
		head = head.getNext();
		size--;
		if (size == 0)
			tail = null;
		return tmp; 
	}
	//Returns a String representation of the Queue
	public String toString() {
		String temp;
		if (head==null) 
			return "[]";
		else {
			temp="[";
			Node<E> tmp=head;
			while (tmp!=null) {
				temp+=""+tmp.getElement();
				if (tmp.getNext()!=null)
					temp+=",";
				tmp=tmp.getNext();
			}
		}
		return temp+"]";
	}
}