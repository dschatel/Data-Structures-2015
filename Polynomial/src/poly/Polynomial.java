package poly;

import java.io.*;
import java.util.StringTokenizer;

/**
 * This class implements a term of a polynomial.
 * 
 * @author runb-cs112
 *
 */
class Term {
	/**
	 * Coefficient of term.
	 */
	public float coeff;
	
	/**
	 * Degree of term.
	 */
	public int degree;
	
	/**
	 * Initializes an instance with given coefficient and degree.
	 * 
	 * @param coeff Coefficient
	 * @param degree Degree
	 */
	public Term(float coeff, int degree) {
		this.coeff = coeff;
		this.degree = degree;
	}
	
	/* (non-Javadoc)
	 * @see java.lang.Object#equals(java.lang.Object)
	 */
	public boolean equals(Object other) {
		return other != null &&
		other instanceof Term &&
		coeff == ((Term)other).coeff &&
		degree == ((Term)other).degree;
	}
	
	/* (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	public String toString() {
		if (degree == 0) {
			return coeff + "";
		} else if (degree == 1) {
			return coeff + "x";
		} else {
			return coeff + "x^" + degree;
		}
	}
}

/**
 * This class implements a linked list node that contains a Term instance.
 * 
 * @author runb-cs112
 *
 */
class Node {
	
	/**
	 * Term instance. 
	 */
	Term term;
	
	/**
	 * Next node in linked list. 
	 */
	Node next;
	
	/**
	 * Initializes this node with a term with given coefficient and degree,
	 * pointing to the given next node.
	 * 
	 * @param coeff Coefficient of term
	 * @param degree Degree of term
	 * @param next Next node
	 */
	public Node(float coeff, int degree, Node next) {
		term = new Term(coeff, degree);
		this.next = next;
	}
}

/**
 * This class implements a polynomial.
 * 
 * @author runb-cs112
 *
 */
public class Polynomial {
	
	/**
	 * Pointer to the front of the linked list that stores the polynomial. 
	 */ 
	Node poly;
	
	/** 
	 * Initializes this polynomial to empty, i.e. there are no terms.
	 *
	 */
	public Polynomial() {
		poly = null;
	}
	
	/**
	 * Reads a polynomial from an input stream (file or keyboard). The storage format
	 * of the polynomial is:
	 * <pre>
	 *     <coeff> <degree>
	 *     <coeff> <degree>
	 *     ...
	 *     <coeff> <degree>
	 * </pre>
	 * with the guarantee that degrees will be in descending order. For example:
	 * <pre>
	 *      4 5
	 *     -2 3
	 *      2 1
	 *      3 0
	 * </pre>
	 * which represents the polynomial:
	 * <pre>
	 *      4*x^5 - 2*x^3 + 2*x + 3 
	 * </pre>
	 * 
	 * @param br BufferedReader from which a polynomial is to be read
	 * @throws IOException If there is any input error in reading the polynomial
	 */
	public Polynomial(BufferedReader br) throws IOException {
		String line;
		StringTokenizer tokenizer;
		float coeff;
		int degree;
		
		poly = null;
		
		while ((line = br.readLine()) != null) {
			tokenizer = new StringTokenizer(line);
			coeff = Float.parseFloat(tokenizer.nextToken());
			degree = Integer.parseInt(tokenizer.nextToken());
			poly = new Node(coeff, degree, poly);
		}
	}
	
	
	/**
	 * Returns the polynomial obtained by adding the given polynomial p
	 * to this polynomial - DOES NOT change this polynomial
	 * 
	 * @param p Polynomial to be added
	 * @return A new polynomial which is the sum of this polynomial and p.
	 */
	public Polynomial add(Polynomial p) {
		
		//Returns other list if adding zero to it
		if (p.poly == null)
			return this;
		else if (this.poly == null)
			return p;
		
		Node list1 = this.poly;
		Node list2 = p.poly;
		Polynomial newList = new Polynomial();
		
		//Iterates through both lists
		while (list1 != null && list2 != null) {
			if (list1.term.degree == list2.term.degree) {		//Checks for similar degrees between lists
				if(list1.term.coeff + list2.term.coeff == 0) { //Checks if the coefficients total 0, skips making a node
					list1 = list1.next;
					list2 = list2.next;
				}
				else {	//If coeff total is not 0, makes a new node adding them
					float newCoeff = list1.term.coeff + list2.term.coeff; 
					Node n = new Node(newCoeff, list1.term.degree, null);
					newList.addToEnd(n);
					list1 = list1.next;
					list2 = list2.next;
				}
			}
			
			//If currently evaluated nodes are not equal, then there are no like degrees between lists
			//Adds new node with the currently evaluated term's values and increments respective pointer
			else if (list1.term.degree > list2.term.degree) {
				Node n = new Node (list2.term.coeff, list2.term.degree, null);
				newList.addToEnd(n);
				list2 = list2.next;
			}
			else if (list2.term.degree > list1.term.degree) {
				Node n = new Node (list1.term.coeff, list1.term.degree, null);
				newList.addToEnd(n);
				list1 = list1.next;
			}
		}
		
		
		//Deals with uneven list: whatever list isn't finished parsing is simply added to end of newList
		Node tmp = null;
		
		if (list1 != null) 
			tmp = list1;
		else if (list2 != null)
			tmp = list2;
		
		while (tmp != null) {
			Node n = new Node (tmp.term.coeff, tmp.term.degree, null);
			newList.addToEnd(n);
			tmp = tmp.next;
		}
		
		return newList;
	}

	
	/**
	 * Returns the polynomial obtained by multiplying the given polynomial p
	 * with this polynomial - DOES NOT change this polynomial
	 * 
	 * @param p Polynomial with which this polynomial is to be multiplied
	 * @return A new polynomial which is the product of this polynomial and p.
	 */
	public Polynomial multiply(Polynomial p) {
		
		//If multiplying by zero, return the null list
		if (p.poly == null)
			return p;
		else if (this.poly == null)
			return this;
		
		Polynomial newList = new Polynomial();
		
		//Nested loop to multiply terms. Multiplies first term by every term in second list and so on.
		for (Node list1 = this.poly; list1 != null; list1 = list1.next) {
			for (Node list2 = p.poly; list2 != null; list2 = list2.next) {
				Node n = new Node (list1.term.coeff * list2.term.coeff, list1.term.degree + list2.term.degree, null);
				newList.addToEnd(n);
			}
		}
		
		//sorts newList in ascending order and then combines like degrees.
		newList.sort();
		newList.compress();

		return newList;
	}
	
	/**
	 * Evaluates this polynomial at the given value of x
	 * 
	 * @param x Value at which this polynomial is to be evaluated
	 * @return Value of this polynomial at x
	 */
	public float evaluate(float x) {
		float result = 0;
		
		//Iterates through list and evaluates each term, adding and storing data in result
		for (Node tmp = poly; tmp != null; tmp = tmp.next) {
			result = result + ((float)Math.pow(x, tmp.term.degree))* tmp.term.coeff;
		}
		return result;
	}
	
	/* (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	public String toString() {
		String retval;
		
		if (poly == null) {
			return "0";
		} else {
			retval = poly.term.toString();
			for (Node current = poly.next ;
			current != null ;
			current = current.next) {
				retval = current.term.toString() + " + " + retval;
			}
			return retval;
		}
	}
	
	//Helper method to add new nodes to the end of a linked list.
	private void addToEnd (Node toAdd) {
		
		if (poly == null) {
			poly = toAdd;
		}
		else {
			Node tmp;
			for (tmp = poly; tmp.next != null; tmp = tmp.next) {}
			tmp.next = toAdd;
			
		}
	}
	
	//Helper method to sort an unsorted linked list in ascending order. All like degrees will be next to each other.
	private void sort() {
		
		Polynomial newList = new Polynomial();

		for (Node tmp1 = poly; tmp1 != null; tmp1 = tmp1.next) {
			Node n = new Node(tmp1.term.coeff, tmp1.term.degree, null);
			
			if (newList.poly == null)
				newList.poly = n;
			else {
				for (Node tmp2 = newList.poly; tmp2 != null; tmp2 = tmp2.next) {
					if (tmp2.next == null) {
						tmp2.next = n;
						break;
					}
					else if (tmp2.next.term.degree > n.term.degree) {
						n.next = tmp2.next;
						tmp2.next = n;
						break;
					}
				}
			}
		}
		poly = newList.poly;
	}
	
	//Helper method to combine all like terms in a list. MUST BE SORTED FIRST.
	private void compress() {
		
		Node tmp = poly;
		
		while(tmp.next!=null) {
			if (tmp.term.degree == tmp.next.term.degree) {
				tmp.term.coeff += tmp.next.term.coeff;
				tmp.next = tmp.next.next;
			}
			else if (tmp.term.degree < tmp.next.term.degree)
				tmp = tmp.next;
		}
		
	}
}
