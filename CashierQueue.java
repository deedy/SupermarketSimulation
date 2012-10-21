package HomeworkFinal;

import Homework4True.DNode;

//Is a subclass of Queue which is Runnable
//It stores a queue of any objects
//and when run, dequeues them at a given interval of time
public class CashierQueue<E> extends Queue<Item> implements Runnable {
	private int id; //Unique ID for this Cashier Queue
	private static int count=0; //Static variable to keep track of number of CashierQueues
	private int time; //Time in milliseconds that this CashierQueue takes to process one customer
	private int cust_count; //Number of objects this CashierQueue has processed
	public static int process_min_time=500; //Default Minimum Time interval for processing
	public static int process_max_time=1000; //Default Maximum Time interval for processing
	public static int process_time_var=200; //Default Up and Down Time variation for processing
	protected CashierQueueList parent; //The CashierQueueList this CashierQueue is a part of
	
	//Constructor that calls super constructor, sets ID and sets processing time interval for this
	//CashierQueue
	public CashierQueue() {
		super();
		this.id=++CashierQueue.count;
		time=(int)(process_min_time+(process_max_time-process_min_time)*Math.random());
		cust_count=0;
	}
	//Primary run methods for this thread which simply dequeues its elements every given time interval
	public void run() {
		try {
			while(true) {
				Thread.sleep(time+(int)(Math.random()*2*process_time_var)-process_time_var);
				if (!isEmpty()) {
					deque();
					cust_count++;	
				}
			}
		}
		catch (Exception e) {
			System.out.println(e.getMessage());
		}
	}
	//Returns the time taken for this CashierQueue to process a single customer (in milliseconds)
	public int getTime () {
		return time;
	}
	//Returns the unique ID for this CashierQueue
	public int getID () {
		return id;
	}
	//Returns the number of objects this CashierQueue has processed
	public int getCustomerCount() {
		return this.cust_count;
	}
	
	//Special CashierQueue Enque enques an element as an Item to the back of the CashierQueue
	//and adds it to the indexing binary search tree
	public synchronized void enque (int elem) {
		Item item=new Item(elem);
		parent.bst.add(item);
		super.enqueue(item);
	}
	
	//Removes and returns the element at the front of the Queue
	public synchronized int deque() throws EmptyQueueException { 
		if (size == 0)
			throw new EmptyQueueException("Queue is empty."); 
		Item tmp = head.getElement();
		head = head.getNext();
		size--;
		if (size == 0)
			tail = null;
		return tmp.getData(); 
	}
	
	//Removes and returns the element at the position 'index' of the Queue
	public synchronized Item remove(int index) throws EmptyQueueException { 
		if (index<0)
			throw new IndexOutOfBoundsException("Index is out of bounds"); 
		if (index>=size)
			index=size-1;
		Item item;
		if (index==0) {
			item = super.head.getElement();
			super.head=super.head.getNext();
		}
		else {
			Node<Item> cursor = super.head;
			for (int i = 0; i < index-1; i++) {
				cursor = cursor.getNext();
			}
			item = cursor.getNext().getElement();
			if (cursor.getNext()==super.tail) {
				cursor.setNext(null);
				super.tail=cursor;
			}
			else {
				cursor.setNext(cursor.getNext().getNext());
			}
		}
		size--;
		if (size == 0)
			tail = null;
		return item;
		}
	
	//Retrieved the Item at position 'index' of this CashierQueue
	public Item get(int index) throws EmptyQueueException { 
		if (index<0 && index>=size)
			throw new IndexOutOfBoundsException("Index is out of bounds"); 
		Node<Item> cursor = super.head;
		for (int i = 0; i < index; i++) {
			cursor = cursor.getNext();
		}
		return cursor.getElement();
	}
	
	
	
	

}
