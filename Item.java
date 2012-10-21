package HomeworkFinal;
import java.util.*;
// The Item Class provides a layer of abstraction between the CashierQueue and the data in the Queue
// The Item represents the "Person" that stands in the queue.
public class Item implements Comparable {
	private static int id_count=0; //The counter which tracks the number of Items created
	private int id; //The ID number of this Item
	private int data; //The data contained in this Item
	private boolean skipmark=false; //A boolean denoting whether this Item is undergoing the "skip" process
	public boolean tracked=false; //A boolean denoting whether this Item is currently being tracked or not
	public static int patience_min_time=200; //Default minimum patience time for this person
	public static int patience_max_time=400; //Default maximum patience time for this person
	public int patience; //The exact patient time for this person
	//The Constructor to create this Item
	public Item () {
		id=++Item.id_count;
		data=-1;
		patience=(int)(patience_min_time+(patience_max_time-patience_min_time)*Math.random());
	}
	//The Constructor to create this item with a specific data
	public Item (int data) {
		id=++Item.id_count;
		this.data=data;
		patience=(int)(patience_min_time+(patience_max_time-patience_min_time)*Math.random());
	}
	//A getter method to get the ID of this Item
	public int getID() {
		return this.id;
	}
	//A setter method to set the data of this Item
	public void setData (int data) {
		this.data=data;
	}
	//A getter method to get the data of the Item
	public int getData () {
		return data;
	}
	//Returns a string representation of this item, it's id
	public String toString() {
		return id+"";
	}
	//A method to toggle the marked situation of this Item for skipping
	public void toggleMark() {
		skipmark=skipmark?false:true;
	}
	
	//A method to toggle the tracking situation of this Item
	public void toggleTracked() {
		tracked=tracked?false:true;
	}
	
	//A getter method to get the Skipping marker of this Item
	public boolean getMark() {
		return skipmark;
	}
	
	//A getter method to get the Tracking marker of this Item
	public boolean isTracked() {
		return tracked;
	}
	
	//A getter method to return the patience of this particular person
	public int getPatience() {
		return patience;
	}
	
	//A compareTo method which is required because Item implements Comparable, by the basis of ID
	public int compareTo(Object o) {
		return ((Integer)this.id).compareTo(((Item)o).getID());
	}
}