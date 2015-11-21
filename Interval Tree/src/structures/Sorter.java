package structures;

import java.util.ArrayList;

/**
 * This class is a repository of sorting methods used by the interval tree.
 * It's a utility class - all methods are static, and the class cannot be instantiated
 * i.e. no objects can be created for this class.
 * 
 * @author runb-cs112
 */
public class Sorter {

	private Sorter() { }
	
	/**
	 * Sorts a set of intervals in place, according to left or right endpoints.  
	 * At the end of the method, the parameter array list is a sorted list. 
	 * 
	 * @param intervals Array list of intervals to be sorted.
	 * @param lr If 'l', then sort is on left endpoints; if 'r', sort is on right endpoints
	 */
	
	/* Uses an insertion sort algorithm to sort the ArrayLists by left endpoints and right endpoints respectively,
	 * depending on input. */
	
	public static void sortIntervals(ArrayList<Interval> intervals, char lr) {
		// COMPLETE THIS METHOD
		for (int i = 0; i < intervals.size(); i++) {
			Interval next = intervals.get(i);
			int j = i;
			if (lr == 'l') {
				while (j > 0 && intervals.get(j-1).leftEndPoint > next.leftEndPoint) {
					intervals.set(j, intervals.get(j-1));
					j--;
				}
			}
			else if (lr == 'r') {
				while (j > 0 && intervals.get(j-1).rightEndPoint > next.rightEndPoint) {
					intervals.set(j, intervals.get(j-1));
					j--;
				}
			}
			
			intervals.set(j, next);
		}
		
	}

	

	
	/**
	 * Given a set of intervals (left sorted and right sorted), extracts the left and right end points,
	 * and returns a sorted list of the combined end points without duplicates.
	 * 
	 * @param leftSortedIntervals Array list of intervals sorted according to left endpoints
	 * @param rightSortedIntervals Array list of intervals sorted according to right endpoints
	 * @return Sorted array list of all endpoints without duplicates
	 */
	/*Creates an Integer ArrayList with all unique endpoint values from the left endpoint sorted arraylist, then  adds all unique
	 * endpoint values from the right endpoint sorted arraylist, in order*/
	
	public static ArrayList<Integer> getSortedEndPoints(ArrayList<Interval> leftSortedIntervals, ArrayList<Interval> rightSortedIntervals) {
		ArrayList <Integer> points = new ArrayList<Integer>();
				
		for (int i = 0; i < leftSortedIntervals.size(); i++) {
			if (!points.contains(leftSortedIntervals.get(i).leftEndPoint))
				points.add(leftSortedIntervals.get(i).leftEndPoint);
		}
				
		for (int i = 0; i < rightSortedIntervals.size(); i++) {
			if (!points.contains(rightSortedIntervals.get(i).rightEndPoint)) {
				int tmpOne = rightSortedIntervals.get(i).rightEndPoint;
				int tmpTwo = tmpOne;
				for (int j = 0; j < points.size(); j++) {
					if (points.get(j) > tmpOne) {
						tmpTwo = points.get(j);
						points.set(j, tmpOne);
						tmpOne = tmpTwo;
					}
				}
				points.add(tmpTwo);
			}
		}
		
		return points;
		
	}
}
