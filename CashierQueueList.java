package HomeworkFinal;
import java.util.*;

//CashierQueueList is a Thread that extends Vector which stores different CashierQueues
//and when run, acts as a Feeder structure itself to feed those Queues, as well as a
//skipping utility to skip people form one queue to the next when required
public class CashierQueueList<E> extends Vector<E> implements Runnable{
	private int time; //The most recent time it took to feed a person
	public static int feeder_min_time=500; //Default Minimum Time interval for feeding
	public static int feeder_max_time=1000; //Default Maximum Time interval for feeding
	public static int time_per_queue=100; //Default opportunity cost for travelling across one queue
	public static int feeds_per_skip=1; //Default number of new people added before skip decision making occurs
	private int numadd; //Counts the total number of people added to any queue
	private int pos; //The index of the queue where a person was most recently added
	private long start_time; //The time this thread was created
	private int init_skip_pos; //The position in the initial queue of the last "skip"
	private int final_skip_pos; //The position in the initial queue of the last "skip"
	private int init_skip_cq; //The initial queue of the last "skip"
	private int final_skip_cq; //The final queue of the last "skip"
	private double skip_time; //Time from beginning of thread at which last "skip" occured
	private int skip_no; //Total number of skips performed
	private char skip='n'; //Skip Status - 'n' when not in skip mode, 'b' before a skip (impatience) and 'a' after a skip
	BinarySearchTree<Item> bst=new BinarySearchTree(); // A binary search tree that indexes all the people ever added
	
	//Constructor to call the super class and initialize all the data members
	public CashierQueueList() {
		super();
		numadd=0;
		pos=0;
		start_time=0;
		init_skip_cq=-1;
		final_skip_cq=-1;
		skip_no=0;
	}
	
	//The primary thread run method. 
	//Feeds a person into the queue every n seconds, where n is between the intervals supplied
	//Also scans all the Queues every fixed time to check if any individual would be served faster if they skipped
	//to another Queue, and moves the people
	public void run() {
		start_time=System.currentTimeMillis();
		int feed_count=0;
		long last_time=start_time;
		try {
			while(true) {
				time=(int)(feeder_min_time+(feeder_max_time-feeder_min_time)*Math.random());
				//Pause for certain amount of time
				Thread.sleep(time);
				//Feed a person to the CashierQueueList
				feed();
				feed_count++;
				skip='n';
				if (feed_count%CashierQueueList.feeds_per_skip==0) {
					//Checks if any person in the CashierQueueList wants to skip to another queue, 
					//and does the needful
					for (int i=0;i<this.size();i++) {
						CashierQueue<Integer> t=((CashierQueue<Integer>)this.get(i));
						for (int j=0;j<t.size();j++) {
							if (skip(i,j)) {
								skip_time=this.getTimeElapsed()-last_time;
								last_time=this.getTimeElapsed();
								skip_no++;
							}
						}
					}
					
				}
				
			}
		}
		catch (Exception e) {
			System.out.println(e.getMessage());
		}
	}
	
	//Inputs init_cq= the index of the queue and init_pos, the index of the person in that queue to be checked
	//and calculates the time taken to be served for himself and at the back of all other queues
	//If the time taken at the back of another queue is less than the time he'll have to wait,
	//The person is moved to the fastest alternatives
	private boolean skip(int init_cq, int init_pos) {
		//Calculating whether any faster options exist
		CashierQueue<E> cq=((CashierQueue<E>)this.get(init_cq));
		int own_time=init_pos*cq.getTime();
		int fin_cq=-1;
		int patience=((CashierQueue<E>)this.get(init_cq)).get(init_pos).getPatience();
		for (int i=0;i<this.size();i++) {
			int other_time=((CashierQueue<E>)this.get(i)).size()*((CashierQueue<E>)this.get(i)).getTime();
			if (other_time+patience+Math.abs(i-init_cq)*time_per_queue<own_time) {
				fin_cq=i;
			}
		}
		if (fin_cq==-1)
			return false;
		//Setting all the skip variables
		skip='b';
		init_skip_pos=init_pos;
		final_skip_pos=((CashierQueue<Integer>)this.get(fin_cq)).size();
		init_skip_cq=init_cq;
		final_skip_cq=fin_cq;
		//Getting Impatient and moving 
		Item moving=((CashierQueue<E>)this.get(init_cq)).get(init_pos);
		moving.toggleMark();
		CashierQueue<E> cq1=((CashierQueue<E>)this.get(init_skip_cq));
		try {
			Thread.sleep(patience);
		}catch(Exception e) {}
		
		((CashierQueue<E>)this.get(final_skip_cq)).enqueue(cq1.remove(init_skip_pos));
		CashierQueue<E> end=((CashierQueue<E>)this.get(final_skip_cq));
		skip='a';
		try {
			Thread.sleep(patience);
		}catch(Exception e) {}
		end.get(end.size-1).toggleMark();	
		skip='n';
		return true;
		
	}
	
	//Feeds a person into the shortest queue there is
	private void feed () {
		if (!isEmpty()) {
		      int min= ((CashierQueue<Integer>)this.get(0)).size(); 
		      pos=0;
		      for (int i=0;i<this.size();i++) {
		    	  if (((CashierQueue<Integer>)this.get(i)).size()<min) {
		    		  min= ((CashierQueue<Integer>)this.get(i)).size();
		    		  pos=i;
		    	  }
		      }
		    ((CashierQueue<Integer>)this.get(pos)).enque(1);
		}
		numadd++;
	}
	
	//Returns the Time Elapsed since the CashierQueueList Thread (aka the Feeder) began 
	public long getTimeElapsed() {
		return System.currentTimeMillis()-this.start_time;
	}
	//Return the last queue index where a person was added
	public int getLastPos() {
		return this.pos;
	}
	//Return the total number of people added ever
	public int getNumberAdded() {
		return this.numadd;
	}
	//Return the time waited before adding the most recent person to the queue
	public int getLastTime() {
		return this.time;
	}
	//Returns the position in the Queue of the queue the most recent skip occurred FROM
	public int getLastSkipInitPos() {
		return this.init_skip_pos;
	}
	//
	public int getLastSkipFinalPos() {
		return this.final_skip_pos;
	}	
	//Returns the Queue Index of the queue the most recent skip occurred FROM
	public int getLastSkipInit() {
		return this.init_skip_cq;
	}
	//Returns the Queue Index of the queue the most recent skip occurred TO
	public int getLastSkipFinal() {
		return this.final_skip_cq;
	}

	public char getSkipStatus() {
		return this.skip;
	}
	//Returns the Queue Index of the queue the most recent skip occurred TO
	public double getLastSkipTime() {
		return (double)Math.round(this.skip_time) / 1000;
	}
	//Returns the Queue Index of the queue the most recent skip occurred TO
	public int getTotalSkipCount() {
		return this.skip_no;
	}	
	//Returns the Number of Customers Processed Successfully
	public int getTotalCustCount () {
		int count=0;
		for (int i=0;i<this.size();i++) {
			count+=((CashierQueue)this.get(i)).getCustomerCount();
		}
		return count;
	}
	//Returns the Expected Processing Time if everyone chose the shortest queue and never 
	//moved or "skipped" to any other one
	public int getExpectedProcessingTime () {
		double count=0;
		for (int i=0;i<this.size();i++) {
			count+=((CashierQueue)this.get(i)).getTime();
		}
		count/=(this.size()*this.size());
		return (int)(count/(double)this.size()*this.size());
		
	}
	//Returns a string representation of the CashierQueueList
	public String toString() {
		String tmp="";
		for (int i=0;i<this.size();i++) {
			CashierQueue<Integer> t=(CashierQueue)this.get(i);
			tmp+=""+t.getTime()+"\t";
			for (int j=0;j<t.size();j++) {
				tmp+=((CashierQueue)this.get(i)).get(j).getMark();
			}
			tmp+="\n";
		}
		return tmp;
	}
	
	//Overridden add method of Vector to allow us the attach the CashierQueueList to each
	//sub Cashier Queue so that the primary indexing tree can be reached
	public boolean add(E elem) {
		if (elem.getClass().getSimpleName().equals("CashierQueue"));
			((CashierQueue)elem).parent=this;
		return super.add(elem);
		
	}
}
