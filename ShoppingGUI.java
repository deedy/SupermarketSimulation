package HomeworkFinal;
import java.applet.Applet;
import java.awt.event.*;

import java.util.Date;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.io.*;
import java.awt.*;
import java.util.*;
/*ShoppingGUI is the main class to illustrate the Queue problem as a runnable thread itself
 *and an extended applet. It can be modified by changing some hardcoded variables like -
 *refreshrate, QueueNo, feeder_min_time, feeder_max_time, process_min_time and process_max_time
 *It shows the Shopping Cashier Queues functioning in the GUI.
 *The Shopping GUI functionality includes but is not limited to:
 * 1.Complete Customizability of durations and time intervals of about everything imaginable.
 * 2.Multithreaded Queues and a thread for visualization and another for feeding and skipping
 * 3.Markers to denote feeding and skipping
 * 4.Skipping, a phenomenon activated each time a certain number of people join the queue, depends
 * on patience levels of the person, the time advantage of the queue they wish to skip to as well as
 * the opportunity cost depending on the proximity of the faster queue.
 * 5.Tracking can be done when a person is clicked on. He immediately turns a different color and 
 * is followed until he checks out of the "mall". Multiple Tracking is also enabled.
 * 6.LogFile writing is also implemented. Statistical results of all operations performed during a test
 * run is appended to the end of a log file along with a time stamp.
 * 7.RealTime Statistical Display. Many crucial figures are displayed on screen not only to give the 
 * simulation a science-y feel but to display essential continually varying figures.
 * 8.Indexing. Although, AVL auto-balancing has not been implemented, all customers ever processed are
 * stored and can be retrieved from the binary tree extremely efficiently. The tree holds "Items" which 
 * denote People. This further layer of abstraction allows for many prospective benefits including
 * position tracking.
 * 9.Scalability through OOP design: Each class has its own distinct functionality, with little to no
 * overlapping functionality. 
 * 
 * Major Data Structures used are: Queues, Iterators, Binary Search Trees, Vectors
 */

public class ShoppingGUI extends Applet implements Runnable, MouseListener {	
	   Font f = new Font ("Arial",Font.PLAIN,12); //Default Primary FontStyle
	   Font theFont = new Font("TimesRoman",Font.BOLD,24); //Default Secondary FontStyle;
	   CashierQueueList<CashierQueue> cql; //The Cashier Queue List which stores all the Cashier Queues
	   int refreshrate=10; //Refresh Rate for Animation Display
	   Thread runner; //A Thread for this class
	   final int QueueNo=10;//Number of Queues you want to simulate
	   
	   
//	   Initializes the maximum and minimum random limits for the feeder
//	   as well as the cashiers, creates all the cashier Threads
//	   and starts the Feeder structure as well as the Queues
	   public void init () {
		   //Setting the Minimum and Maximum Patience per Person
		   Item.patience_min_time=300;
		   Item.patience_max_time=600;
		   //Setting Feeder Minimum and Maximum Times
		   CashierQueueList.feeder_min_time=250;
		   CashierQueueList.feeder_max_time=500;
		   //Setting Per Queue Opportunity Cost for Moving to another Queue
		   CashierQueueList.time_per_queue=50;
		   //Setting the Number of Feeds Until Skip is considered
		   CashierQueueList.feeds_per_skip=5;
		   //Setting Minimum and Maximum Times for the Cashiers
		   CashierQueue.process_min_time=10000;
		   CashierQueue.process_max_time=15000;
		   //Setting the Variation in Cashier Processing Time
		   CashierQueue.process_time_var=3000;
		   //Add Cashiers
		   populateCashierQueues();
		   //Adding Mouse Listener
		   addMouseListener(this); 
		   //Start Cashiers
		   for (int i=0;i<cql.size();i++) {
			   new Thread ((CashierQueue)cql.get(i)).start () ;
		   }
		   //Start Feeder
		   new Thread (cql).start();
	   }
	   
	   // Starts the GUI Thread in this class
	   public void start() {
	     if (runner == null) {
	       runner = new Thread(this);
	       runner.start();
	     }
	   }
	   
	   //Stops the GUI Thread in this class
	   public void stop() {
	     if (runner != null) {
	       runner.stop();
	       runner = null;
	     }
	    writeStatsToLogFile();
	   }
	  
	   //Is the Run method for the GUI thread in this class
	   //Repaints the GUI according to the refreshrate interval 
	   public void run() {
	     while (true) {
	      try { Thread.sleep(refreshrate); 
	      }
	       catch (InterruptedException e) { }
	      repaint();
	     }
	   }
	 
	  
	   //Draws the GUI
		public void paint(Graphics g) {
			resizeWindow();
			paintQueues(g);
			paintSkipTools(g);
		    paintStats(g); 
		}
		
		//GUI Associated Private Variables
		private int initposx=100;
		private int initposy=200;
		private int r_ball=5;
		
		//Graphically displays the Queue threads with biggish circles as people
		//Shows the Queue ID number, and its individual per average person processing speed as well
		//Displays red arrows beside Queues where new people were just added
		//Colors impatient folk and just moved folk as red
		//Colors "Tracked" people as green
		private void paintQueues (Graphics g) {
			g.drawString("QNo|AvgProcessTime|NoProcessed",initposx-40,initposy-30);
		     g.setFont(theFont);
		     for (int i=0;i<cql.size();i++) {
		    	String tmp="";
		    	//NEW-MEMBER-IN-QUEUE MARKER
		    	if (i==cql.getLastPos()) {
		    		Color ini=g.getColor();
		    		g.setColor(Color.red);
		    		int tritip_x=initposx+215+(((CashierQueue)cql.get(cql.getLastPos())).size()*(5+4*r_ball));
			    	int tritip_y=initposy+(i*30)-10;
			    	int pts_x[]={tritip_x, tritip_x+2*r_ball,tritip_x+2*r_ball};
			    	int pts_y[]={tritip_y,tritip_y-2*r_ball,tritip_y+2*r_ball};
			    	g.fillPolygon(pts_x,pts_y, 3);
			    	g.drawLine(tritip_x,tritip_y,tritip_x+300,tritip_y);
		    		g.setColor(ini);
		    	}
		    	
		    	//QUEUE DETAILS
				CashierQueue<Integer> t=(CashierQueue)cql.get(i);
				tmp=""+t.getID();
				g.drawString(tmp,initposx,initposy+(i*30));
			    g.setFont(f);
				tmp=""+t.getTime()+" ms";
				g.drawString(tmp,initposx+50,initposy+(i*30));
				g.setFont(theFont);
				g.drawString(""+t.getCustomerCount(),initposx+110, initposy+(i*30));
				
				//DRAWING QUEUE
				g.drawLine(initposx+200,initposy+(i*30)+5-(5+4*r_ball)-2,initposx+200+(t.size()*(5+4*r_ball)),initposy+(i*30)+5-(5+4*r_ball)-2);
				g.drawLine(initposx+200,initposy+(i*30)+5-(5+4*r_ball)+(4*r_ball+2),initposx+200+(t.size()*(5+4*r_ball)),initposy+(i*30)+5-(5+4*r_ball)+(4*r_ball+2));
				g.drawLine(initposx+200,initposy+(i*30)+5-(5+4*r_ball)-2,initposx+200-4*r_ball,initposy+(i*30)+5-(5+4*r_ball)+2*r_ball);
				g.drawLine(initposx+200-(4*r_ball),initposy+(i*30)+5-(5+4*r_ball)+2*r_ball,initposx+200,initposy+(i*30)+5-(5+4*r_ball)+(4*r_ball+2));
				g.drawArc(initposx+200+(t.size()*(5+4*r_ball))-2-2*r_ball,initposy+(i*30)+5-(5+4*r_ball)-2,(4*r_ball)+4,(4*r_ball)+4,-90,180);
				for (int j=0;j<t.size();j++) {
					Color ini=null;
					//IMPATIENCE MARKER
					if ((cql.get(i).get(j).getMark())) {
						ini=g.getColor();
						g.setColor(Color.red);
					}
					//TRACKING MARKER
					else if ((cql.get(i).get(j).isTracked())) {
						ini=g.getColor();
						g.setColor(new Color(50,205,50));
					}
					g.fillOval(initposx+200+(j*(5+4*r_ball)),initposy+(i*30)+5-(5+4*r_ball),4*r_ball,4*r_ball);
					if ((cql.get(i)).get(j).getMark()) {
						g.setColor(ini);
					}
					else if ((cql.get(i).get(j).isTracked())) {
						g.setColor(ini);
					}
				}
			} 
		}
		
		// Prints Skip Statistics - Time since last skip, Total Skip Number
		// Prints Skip Arc - shows the skip that just occurred
		private void paintSkipTools (Graphics g) {
			//SKIP DETAILS
		     if (cql.getLastSkipFinal()!=cql.getLastSkipInit()) {
		    	 g.setFont(f);
		    	 Color ini=g.getColor();
		    	 //SKIP STATS
		    	 g.setColor(new Color(200,50,50));
		    	 g.drawString("Time since the last skip occured (in s)",50,100);
		    	 g.drawString(""+cql.getLastSkipTime(),50,125);
		    	 g.drawString("Total Skip Number",300,100);
		    	 g.drawString(""+cql.getTotalSkipCount(),300,125);
		    	 //AFTER SKIP ARROW
		    	 if (cql.getSkipStatus()=='a') {
		    	 int smallestpos=cql.getLastSkipInit()<cql.getLastSkipFinal()?cql.getLastSkipInit():cql.getLastSkipFinal();
		    	 int sizefinal=((CashierQueue)cql.get(cql.getLastSkipFinal())).size();
		    	 int firstpos=cql.getLastSkipInit()<cql.getLastSkipFinal()?cql.getLastSkipInitPos():sizefinal;
		    	 int secondpos=cql.getLastSkipInit()<cql.getLastSkipFinal()?sizefinal:cql.getLastSkipInitPos();
		    	 int width_arc=300;

		    	 g.drawArc(initposx+200+2*r_ball+(firstpos*(5+4*r_ball))-width_arc,initposy+(smallestpos*30)+2*r_ball+5-(5+4*r_ball),2*width_arc,Math.abs(30*(cql.getLastSkipFinal()-cql.getLastSkipInit())),0,90);
		    	 width_arc+=(firstpos-secondpos)*(5+4*r_ball);
		    	 g.drawArc(initposx+200+2*r_ball+(secondpos*(5+4*r_ball))-width_arc,initposy+(smallestpos*30)+2*r_ball+5-(5+4*r_ball),2*width_arc,Math.abs(30*(cql.getLastSkipFinal()-cql.getLastSkipInit())),-90,90);
		    	 
		    	 int tritip_x=initposx+200+2*r_ball+(sizefinal*(5+4*r_ball));
		    	 int tritip_y=initposy+(cql.getLastSkipFinal()*30)+2*r_ball+5-(5+4*r_ball);
		    	 int pts_x[]={tritip_x, tritip_x+2*r_ball,tritip_x+2*r_ball};
		    	 int pts_y[]={tritip_y,tritip_y-2*r_ball,tritip_y+2*r_ball};
		    	 g.fillPolygon(pts_x,pts_y, 3);
		    	
		    	 }
		    	 g.setColor(ini);
		    	 g.setFont(theFont);
		    	 
		     }
		}
		
		
		//Visually shows some stats relevant to the Shopping Cashier Simulation
		private void paintStats(Graphics g) {
			 g.setFont(f);
		     g.drawString("Amount of time before last customer came (in ms)",600,500);
		     g.drawString(CashierQueueList.feeder_min_time+" <   "+cql.getLastTime()+"   < "+CashierQueueList.feeder_max_time, 600,525);
		     g.drawString("Expected Process Time (without skip)",550,25);
		     g.drawString(""+cql.getExpectedProcessingTime()+"ms", 600,50);
		     g.drawString("Total Elapsed Time",800,25);
		     g.drawString(""+(int)cql.getTimeElapsed()/1000+"s", 800,50);
		     g.drawString("Time per Customer Addition (in ms)",50,500);
		     if (cql.getNumberAdded()!=0)
		    	 g.drawString(CashierQueueList.feeder_min_time+" <  "+cql.getTimeElapsed()/cql.getNumberAdded()+"  < "+CashierQueueList.feeder_max_time, 50,525);
		     g.drawString("Total Number of Customers Added", 300, 500);
			 g.drawString(""+cql.getNumberAdded(), 300,525);
		     g.drawString("Time per Customer Processing (in ms)",300,25);
		     if (cql.getTotalCustCount()!=0)
				g.drawString(""+cql.getTimeElapsed()/cql.getTotalCustCount(), 400,50);
		     g.drawString("Total Customers Processed", 50, 25);
		     g.setFont(theFont);
		     g.drawString(""+cql.getTotalCustCount(), 50,50);
			
		}
		
		//Resizes the GUI window to 3/4th the screen's size
		private void resizeWindow() {
			if (this.size().height<300 || this.size().width<300) {
				   Dimension dim = Toolkit.getDefaultToolkit().getScreenSize();
				   resize(3*dim.width/4,3*dim.height/4);
			}
		}
		
		//Populates the CashierQueueList of the mall with predefined number of Queues
		private void populateCashierQueues() {
			   int c=QueueNo;
			   cql= new CashierQueueList<CashierQueue> ();
			   while (c>0) {
				   cql.add(new CashierQueue<Integer> ());
				   c--;
			   }
		   }
//		Writes Relevant Statistical Data from the simulation session to a Logfile.txt
		private void writeStatsToLogFile() {
			 try {
		    	    BufferedWriter out = new BufferedWriter(new FileWriter("Logfile.txt",true));
		    	    DateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
		    	    Date date = new Date();
		    	    out.write("SESSION "+dateFormat.format(date)+"\n");
		    	    out.write("Total Running Time: "+(int)cql.getTimeElapsed()/1000+"s\n");
		    	    out.write("Feeder Minimum Time Interval: "+CashierQueueList.feeder_min_time+"ms\n");
		    	    out.write("Feeder Maximum Time Interval: "+CashierQueueList.feeder_max_time+"ms\n");
		    	    out.write("Total Number of Customers Added: "+cql.getNumberAdded()+"\n");
		    	    out.write("Actual Time per Customer Addition: "+cql.getTimeElapsed()/cql.getNumberAdded()+"ms\n");
		    	    out.write("Queue Processing Minimum Time Interval: "+CashierQueue.process_min_time+"ms\n");
		    	    out.write("Queue Processing Maximum Time Interval: "+CashierQueue.process_max_time+"ms\n");
		    	    out.write("Total Number of Customers Processed: "+cql.getTotalCustCount()+"\n");
		    	    out.write("Expected Process Time per Customer: "+cql.getExpectedProcessingTime()+"ms"+"\n");
		    	    out.write("Actual Process Time per Customer: "+cql.getTimeElapsed()/cql.getTotalCustCount()+"ms"+"\n");
		    	    out.write("Total Skip Number: "+cql.getTotalSkipCount()+"\n");
		    	    if (cql.getExpectedProcessingTime()>(cql.getTimeElapsed()/cql.getNumberAdded())) {
			    	    double num=(cql.getTimeElapsed()/cql.getTotalCustCount())-(cql.getTimeElapsed()/cql.getNumberAdded());
			    	    double den=cql.getExpectedProcessingTime()-(cql.getTimeElapsed()/cql.getNumberAdded());
			    	    out.write("Percentage Improvement with Skip Method: "+(int)(100*(1-num/den))+"%\n");
		    	    }
		    	    out.write("\n\n");
		    	    out.close();
		    	} catch (IOException e) {
		    		System.out.println(e.getMessage());
		    	}
		}

		//Calculates what Person the user clicked on, and marks it for tracking purposes
		public void mouseClicked(MouseEvent me) {
			int xpos = me.getX(); 
			int ypos = me.getY();
			int queue_no=(int)((ypos-initposy+(5+4*r_ball)-5)/30);
			int position=(int)((xpos-initposx-200)/(5+4*r_ball));
			if (queue_no<cql.size() && position<cql.get(queue_no).size()) {
				cql.get(queue_no).get(position).toggleTracked();
			}
			repaint();
		}
		
		//MouseListener Function that are compulsorily overridden
		public void mouseEntered(MouseEvent me) {}
		public void mouseExited(MouseEvent arg0) {}
		public void mousePressed(MouseEvent arg0) {}
		public void mouseReleased(MouseEvent arg0) {}


}
