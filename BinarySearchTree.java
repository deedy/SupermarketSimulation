package HomeworkFinal;
import java.util.*;

import HomeworkFinal.BSTNode;
/** BinarySearchTree
 * The Binary Search Tree implements the Binary Tree interface and performs general simple Binary Tree
 * functionality, with an additional fix in the BSTNode class to account for frequency so the List and
 * BST in LinkTree can represent the same thing in case multiple equal elements are added.
 * 
 * NOTE: The only reason ArrayLists were used is so a neat and easy Binary Search Tree visual can
 * be obtained
 * @author Deedy
 *
 * @param <E>
 */
public class BinarySearchTree<E extends Comparable> implements BinaryTree<E> {
	BSTNode<E> root; //Binary Search Tree Node pointing to the root
	int size = 0; //The number of unique elements in the Binary Search Tree
	private ArrayList<ArrayList<BSTNode<E>>> a; //Stores a visual representation of the Binary Search Tree
	
	/** Constructor */
    public BinarySearchTree( ) {
        root = null;
    }
    
    /** Test if the tree is logically empty. */
    public boolean isEmpty( ) {
        return root == null;
    }
    
    /** Returns the size of the tree */
    public int size() {
    	return size;
    }
    
	/** Insert element into tree */
	public synchronized void add(E element) {
	    if (root == null && element != null) {
	        root = new BSTNode<E>(element);
	        size++;
	    } else if (element != null) {
	        root = insert(root, element);
	    }
	}
	
	 /**  Remove from the tree */
    public synchronized void remove(E element) {
        root = remove(element, root );
        size--;
    }
    
	/** Recursive private function to insert into the tree */
	private synchronized BSTNode<E> insert(BSTNode<E> node, E value) {
		BSTNode<E> result = new BSTNode<E>(node);
		int compare = node.value.compareTo(value);
		if (compare == 0) {
			result.incrementFrequency ();
		    return result;
		}
		if (compare > 0) {
		    if (result.left != null) {
		        result.left = insert(result.left, value);
		    } else {
		        result.left = new BSTNode<E>(value);
		        size++;
		    }
		}
		
		else if (compare < 0) {
		    if (result.right != null) {
		        result.right = insert(result.right, value);
		    } else {
		        result.right = new BSTNode<E>(value);
		        size++;
		    }
		}
		return result;
	}
	
	/** Find an item in the tree. */
	public synchronized E find(E key) {
	    if (root == null)
	        return null;

	    BSTNode<E> node = root;
	    int compareResult;
	    while ((compareResult = node.value.compareTo(key)) != 0) {
	        if (compareResult > 0) {
	            if (node.left != null)
	                node = node.left;
	            else
	                return null;
	        } else {
	            if (node.right != null)
	                node = node.right;
	            else
	                return null;
	        }
	    }
	    return node.value;
	}
	
	 /** Internal method to remove from a subtree. */
    protected synchronized BSTNode<E> remove(E x, BSTNode<E> t ) {
        if( t == null )
            throw new ItemNotFoundException(x.toString( ) );
        if( x.compareTo( t.value ) < 0 )
            t.left = remove( x, t.left );
        else if( x.compareTo( t.value ) > 0 )
            t.right = remove( x, t.right );
        else if (t.getFrequency()>1) {
        	t.decrementFrequency();
        	return t;
        }
        else if( t.left != null && t.right != null ) // Two children
        {
            t.value = findMin( t.right ).value;
            t.right = removeMin( t.right );
        } else
            t = ( t.left != null ) ? t.left : t.right;
        
        return t;
    }
    
    /**  Internal method to remove minimum item from a subtree. */
    protected synchronized BSTNode<E> removeMin( BSTNode<E> t ) {
        if( t == null )
            throw new ItemNotFoundException();
        else if( t.left != null ) {
            t.left = removeMin( t.left );
            return t;
        } else
            return t.right;
    }
    
    /** Internal method to find the smallest item in a subtree. */
    protected synchronized BSTNode<E> findMin( BSTNode<E> t ) {
        if( t != null )
            while( t.left != null )
                t = t.left;
        
        return t;
    }
    
    /** Return String representation of this Binary Tree */
    public String toString () {
    	String tmp=getString(root);
    	if (tmp.length()>1)
    		return "["+tmp.substring(0,tmp.length()-1)+"]";
    	else
    		return "["+tmp+"]";
    }
    
    protected String getString(BSTNode<E> node) {
    	if (node==null)
    		return "";
    	String s="";
    	s+=getString(node.left);
    	s+=node.value+",";
    	s+=getString(node.right);
    	return s;
    }
    
    /** Return Height of Binary Tree */
    public int heightOfBinaryTree(BSTNode<E> node) {
        if (node == null) {
            return 0;
        } else {
            return 1 + Math.max(heightOfBinaryTree(node.left),heightOfBinaryTree(node.right));
        }
    }
    
   
    
    /** Return Visual representation of this Binary Tree */
    public void VisualDisplay () {
    	a=new ArrayList<ArrayList<BSTNode<E>>>();
    	int height=heightOfBinaryTree(root);
    	for (int i=0;i<height;i++)
    		a.add(new ArrayList<BSTNode<E>>());
    	getVisualArray(root,height-1);
    	System.out.println("Height: "+height);
    	System.out.println("Binary Tree: ");
    	for (int i=height-1;i>=0;i--) {
    		for (int j=0;j<Math.pow(2,i)-1;j++)
    			System.out.print ("\t");
    		for (int j=0;j<a.get(i).size();j++) {
    			if (a.get(i).get(j)!=null)
    				System.out.print (a.get(i).get(j).value+" ("+a.get(i).get(j).getFrequency()+")");
    			for (int k=0;k<Math.pow(2,i+1);k++) {
    				System.out.print("\t");
    			}
    		}
    		System.out.println();
    	}
    } 
    
    /** Return Visual representation of this Binary Tree Without Frequencies attached */
    public void VisualDisplayWithoutFrequency () {
    	a=new ArrayList<ArrayList<BSTNode<E>>>();
    	int height=heightOfBinaryTree(root);
    	for (int i=0;i<height;i++)
    		a.add(new ArrayList<BSTNode<E>>());
    	getVisualArray(root,height-1);
    	System.out.println("Height: "+height);
    	System.out.println("Binary Tree: ");
    	for (int i=height-1;i>=0;i--) {
    		for (int j=0;j<Math.pow(2,i)-1;j++)
    			System.out.print ("\t");
    		for (int j=0;j<a.get(i).size();j++) {
    			if (a.get(i).get(j)!=null)
    				System.out.print (a.get(i).get(j).value);
    			for (int k=0;k<Math.pow(2,i+1);k++) {
    				System.out.print("\t");
    			}
    		}
    		System.out.println();
    	}
    } 
    
    
    /** Internal method to generate the arraylist used to visually display the tree */
    protected void getVisualArray(BSTNode<E> node,int depth) {
    	if (depth<0)
    		return;
    	
    	if (node==null) {
    		a.get(depth).add(null);
    		getVisualArray(null,depth-1);
    		getVisualArray(null,depth-1);
    		return;
    	}
    	a.get(depth).add(node);
    	getVisualArray(node.left,depth-1);
    	getVisualArray(node.right,depth-1);
    }
    
    /** Returns an iterator of all the elements in the binarysearchtree. */
	public Iterator<E> iterator(){ 
		try {
			return new BSTIterator<E>(this); 
		} catch (Exception e) {
			return null;
		}
	}
	
}
