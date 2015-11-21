package structures;

import java.util.*;

/**
 * Encapsulates an interval tree.
 * 
 * @author runb-cs112
 */
public class IntervalTree {
	
	/**
	 * The root of the interval tree
	 */
	IntervalTreeNode root;
	
	/**
	 * Constructs entire interval tree from set of input intervals. Constructing the tree
	 * means building the interval tree structure and mapping the intervals to the nodes.
	 * 
	 * @param intervals Array list of intervals for which the tree is constructed
	 */
	public IntervalTree(ArrayList<Interval> intervals) {
		
		// make a copy of intervals to use for right sorting
		ArrayList<Interval> intervalsRight = new ArrayList<Interval>(intervals.size());
		for (Interval iv : intervals) {
			intervalsRight.add(iv);
		}
		
		// rename input intervals for left sorting
		ArrayList<Interval> intervalsLeft = intervals;
		
		// sort intervals on left and right end points
		Sorter.sortIntervals(intervalsLeft, 'l');
		Sorter.sortIntervals(intervalsRight,'r');
		
		// get sorted list of end points without duplicates
		ArrayList<Integer> sortedEndPoints = Sorter.getSortedEndPoints(intervalsLeft, intervalsRight);
		
		// build the tree nodes
		root = buildTreeNodes(sortedEndPoints);
		
		
		// map intervals to the tree nodes
		mapIntervalsToTree(intervalsLeft, intervalsRight);
		
		inOrder(root);
		
	}
	
	/**
	 * Builds the interval tree structure given a sorted array list of end points.
	 * 
	 * @param endPoints Sorted array list of end points
	 * @return Root of the tree structure
	 */
	public static IntervalTreeNode buildTreeNodes(ArrayList<Integer> endPoints) {
		
		Queue <IntervalTreeNode> treeNodes = new Queue<IntervalTreeNode>();
		for (Integer i: endPoints) {
			IntervalTreeNode n = new IntervalTreeNode(i,i,i);
			treeNodes.enqueue(n);
		}
				
		return buildTree(treeNodes);
	}
	
	//Builds an interval tree based on a Queue of sorted endpoints.
	
	private static IntervalTreeNode buildTree(Queue<IntervalTreeNode> treeQueue) {
		
		int s = treeQueue.size();
		
		if (s == 1) {
			IntervalTreeNode T = treeQueue.dequeue();
			return T;
		}
		else {
			int tmp = s;
			while (tmp > 1) {
				IntervalTreeNode treeOne = treeQueue.dequeue();
				IntervalTreeNode treeTwo = treeQueue.dequeue();
				
				
				float firstVal = treeOne.maxSplitValue;
				float secondVal = treeTwo.minSplitValue;
				
				float x = (firstVal + secondVal) / 2;
								
								
				IntervalTreeNode n = new IntervalTreeNode(x, treeOne.minSplitValue, treeTwo.maxSplitValue);
				n.leftChild = treeOne;
				n.rightChild = treeTwo;
				
				treeQueue.enqueue(n);
				tmp = tmp - 2;
			}
			
			if (tmp == 1) {
				treeQueue.enqueue(treeQueue.dequeue());
			}
			
		}
		return 	buildTree(treeQueue);
		
	}
	
	/**
	 * Maps a set of intervals to the nodes of this interval tree. 
	 * 
	 * @param leftSortedIntervals Array list of intervals sorted according to left endpoints
	 * @param rightSortedIntervals Array list of intervals sorted according to right endpoints
	 */
	public void mapIntervalsToTree(ArrayList<Interval> leftSortedIntervals, ArrayList<Interval> rightSortedIntervals) {
		
		
		for (Interval i: leftSortedIntervals) {
			IntervalTreeNode temp = root;
			
			while (temp.leftChild != null && temp.rightChild!= null) {
				
				if (temp.splitValue <= i.rightEndPoint && temp.splitValue >= i.leftEndPoint) {
					if(temp.leftIntervals == null)
						temp.leftIntervals = new ArrayList<Interval>();
					temp.leftIntervals.add(i);
					break;
				}
				else if (i.leftEndPoint > temp.splitValue)
					temp = temp.rightChild;
				else if (i.rightEndPoint < temp.splitValue)
					temp = temp.leftChild;
			}			
		}
		
		for (Interval i: rightSortedIntervals) {
			IntervalTreeNode temp = root;
			
			while (temp.leftChild != null && temp.rightChild!= null) {
				
				if (temp.splitValue <= i.rightEndPoint && temp.splitValue >= i.leftEndPoint) {
					if(temp.rightIntervals == null)
						temp.rightIntervals = new ArrayList<Interval>();
					temp.rightIntervals.add(i);
					break;
				}
				else if (i.leftEndPoint > temp.splitValue)
					temp = temp.rightChild;
				else if (i.rightEndPoint < temp.splitValue)
					temp = temp.leftChild;
			}
		}
		
	}
	
	/**
	 * Gets all intervals in this interval tree that intersect with a given interval.
	 * 
	 * @param q The query interval for which intersections are to be found
	 * @return Array list of all intersecting intervals; size is 0 if there are no intersections
	 */
	public ArrayList<Interval> findIntersectingIntervals(Interval q) {
	
		return findIntersectingIntervals(root, q);
	}
	
	private ArrayList<Interval> findIntersectingIntervals(IntervalTreeNode n, Interval q) {
		ArrayList<Interval> resultsList = new ArrayList<Interval>();
		
		if(n.leftChild == null && n.rightChild == null)
			return resultsList;
		
		if(n.splitValue <= q.rightEndPoint && n.splitValue >= q.leftEndPoint) {
			if (n.leftIntervals != null)
				resultsList.addAll(n.leftIntervals);
			resultsList.addAll(findIntersectingIntervals(n.leftChild, q));
			resultsList.addAll(findIntersectingIntervals(n.rightChild, q));
		}
		else if (n.splitValue < q.leftEndPoint) {
			if (n.rightIntervals != null) {
				int i = n.rightIntervals.size() -1;
			
				while (i >= 0 && n.rightIntervals.get(i).intersects(q)) {
					resultsList.add(n.rightIntervals.get(i));
					i--;
				}
			}
			resultsList.addAll(findIntersectingIntervals(n.rightChild, q));
		}
		else if (n.splitValue > q.rightEndPoint) {
			if (n.leftIntervals != null) {
				int i = 0;
			
				while (i < n.leftIntervals.size() && n.leftIntervals.get(i).intersects(q)) {
					resultsList.add(n.leftIntervals.get(i));
					i++;
				}
			}
			resultsList.addAll(findIntersectingIntervals(n.leftChild, q));
		}
		
		return resultsList;
	}
	
	/**
	 * Returns the root of this interval tree.
	 * 
	 * @return Root of interval tree.
	 */
	public IntervalTreeNode getRoot() {
		return root;
	}

	
	//LVR Traversal
	
	private void inOrder (IntervalTreeNode root) {
		
		if (root == null)
			return;
		
		inOrder(root.leftChild);
		System.out.println(root);
		System.out.println(root);
		inOrder(root.rightChild);
	}
}

