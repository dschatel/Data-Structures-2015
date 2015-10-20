package apps;

import java.io.IOException;
import java.util.ArrayList;
import java.util.NoSuchElementException;
import java.util.Scanner;
import java.util.StringTokenizer;

import structures.Stack;

public class Expression {

	/**
	 * Expression to be evaluated
	 */
	String expr;

	/**
	 * Scalar symbols in the expression
	 */
	ArrayList<ScalarSymbol> scalars;

	/**
	 * Array symbols in the expression
	 */
	ArrayList<ArraySymbol> arrays;

	/**
	 * Positions of opening brackets
	 */
	ArrayList<Integer> openingBracketIndex;

	/**
	 * Positions of closing brackets
	 */
	ArrayList<Integer> closingBracketIndex;

	/**
	 * String containing all delimiters (characters other than variables and
	 * constants), to be used with StringTokenizer
	 */
	public static final String delims = " \t*+-/()[]";

	/**
	 * Initializes this Expression object with an input expression. Sets all
	 * other fields to null.
	 * 
	 * @param expr
	 *            Expression
	 */
	public Expression(String expr) {
		this.expr = expr;
		scalars = null;
		arrays = null;
		openingBracketIndex = null;
		closingBracketIndex = null;
	}

	/**
	 * Matches parentheses and square brackets. Populates the
	 * openingBracketIndex and closingBracketIndex array lists in such a way
	 * that closingBracketIndex[i] is the position of the bracket in the
	 * expression that closes an opening bracket at position
	 * openingBracketIndex[i]. For example, if the expression is:
	 * 
	 * <pre>
	 * (a + (b - c)) * (d + A[4])
	 * </pre>
	 * 
	 * then the method would return true, and the array lists would be set to:
	 * 
	 * <pre>
	 * openingBracketIndex: [0 3 10 14] closingBracketIndex: [8 7 17 16] </pe>
	 * 
	 * See the FAQ in project description for more details.
	 * 
	 * @return True if brackets are matched correctly, false if not
	 */
	public boolean isLegallyMatched() {

		openingBracketIndex = new ArrayList<Integer>();
		closingBracketIndex = new ArrayList<Integer>();
		Stack<Bracket> bracketMatch = new Stack<Bracket>();

		for (int i = 0; i < expr.length(); i++) {
			if (expr.charAt(i) == '[' || expr.charAt(i) == '(') {
				Bracket tmp = new Bracket(expr.charAt(i), i);
				bracketMatch.push(tmp);
			} else if (expr.charAt(i) == ']' || expr.charAt(i) == ')') {
				try {
					Bracket tmp2 = bracketMatch.pop();
					if (tmp2.ch == '[' && expr.charAt(i) == ']') {
						openingBracketIndex.add(tmp2.pos);
						closingBracketIndex.add(i);
						continue;
					} else if (tmp2.ch == '(' && expr.charAt(i) == ')') {
						openingBracketIndex.add(tmp2.pos);
						closingBracketIndex.add(i);
						continue;
					}
					else 
						return false;
				} catch (NoSuchElementException e) {
					return false;
				}
			}
		}

		return bracketMatch.isEmpty();
	}

	private void printArrayLists() { //Checks if openingBracketIndex and closingBracketIndex populate correctly
		for (int i = 0; i < openingBracketIndex.size(); i++) {
			System.out.print(openingBracketIndex.get(i) + " " + closingBracketIndex.get(i));
			System.out.println();
		}
	}

	/**
	 * Populates the scalars and arrays lists with symbols for scalar and array
	 * variables in the expression. For every variable, a SINGLE symbol is
	 * created and stored, even if it appears more than once in the expression.
	 * At this time, the constructors for ScalarSymbol and ArraySymbol will
	 * initialize values to zero and null, respectively. The actual values will
	 * be loaded from a file in the loadSymbolValues method.
	 */
	public void buildSymbols() {
		// COMPLETE THIS METHOD
		
		StringTokenizer st = new StringTokenizer(expr, delims, true);
		Stack<String> expression = new Stack<String>();
		
		while(st.hasMoreTokens()) {
			String token = st.nextToken();
			if (!token.equals(" ") && !token.equals("	"))
				expression.push(token);
		}
		
		scalars = new ArrayList<ScalarSymbol>();
		arrays = new ArrayList<ArraySymbol>();
		
		while(!expression.isEmpty()) {
			String var = expression.pop();
			if(var.equals("[")) {
				ArraySymbol arr = new ArraySymbol(expression.pop());
				if (!arrays.contains(arr))
					arrays.add(arr);
			}
			else if (isScalarVar(var)) {
				ScalarSymbol sca = new ScalarSymbol(var);
				if (!scalars.contains(sca))
					scalars.add(sca);
			}
		}

	}
	

	/**
	 * Loads values for symbols in the expression
	 * 
	 * @param sc
	 *            Scanner for values input
	 * @throws IOException
	 *             If there is a problem with the input
	 */
	public void loadSymbolValues(Scanner sc) throws IOException {
		while (sc.hasNextLine()) {
			StringTokenizer st = new StringTokenizer(sc.nextLine().trim());
			int numTokens = st.countTokens();
			String sym = st.nextToken();
			ScalarSymbol ssymbol = new ScalarSymbol(sym);
			ArraySymbol asymbol = new ArraySymbol(sym);
			int ssi = scalars.indexOf(ssymbol);
			int asi = arrays.indexOf(asymbol);
			if (ssi == -1 && asi == -1) {
				continue;
			}
			int num = Integer.parseInt(st.nextToken());
			if (numTokens == 2) { // scalar symbol
				scalars.get(ssi).value = num;
			} else { // array symbol
				asymbol = arrays.get(asi);
				asymbol.values = new int[num];
				// following are (index,val) pairs
				while (st.hasMoreTokens()) {
					String tok = st.nextToken();
					StringTokenizer stt = new StringTokenizer(tok, " (,)");
					int index = Integer.parseInt(stt.nextToken());
					int val = Integer.parseInt(stt.nextToken());
					asymbol.values[index] = val;
				}
			}
		}
	}

	/**
	 * Evaluates the expression, using RECURSION to evaluate subexpressions and
	 * to evaluate array subscript expressions. (Note: you can use one or more
	 * private helper methods to implement the recursion.)
	 * 
	 * @return Result of evaluation
	 */
	public float evaluate() {
		// COMPLETE THIS METHOD

		// FOLLOWING LINE ADDED TO MAKE COMPILER HAPPY
		String expression = expr;
		expression = rebuildExpression(expression);
		return parenEval(expression);
	}

	private float evaluate(String str) { 
		
		/* Tokenizes input around + and -, recursively throws down subexpressions with * and / to evalTerms
		 * Done this way to keep order of operations: * and / are evaluated first
		 */
		
		StringTokenizer st = new StringTokenizer(str, "+-", true);
		Stack<String> values = new Stack<String>();
		float val = 0;

		while(st.hasMoreTokens()) {
			String token = st.nextToken();
			if (token.equals("+")) {
				 val = evalTerms(values.pop()) + evalTerms(st.nextToken());
				 values.push(String.valueOf(val));
			}
			else if (token.equals("-")) {
				val = evalTerms(values.pop()) - evalTerms(st.nextToken());
				values.push(String.valueOf(val));
			}
			else
				values.push(token);
		}
		
		while(!values.isEmpty()){
			val = evalTerms(values.pop());
		}
		
		return val;

    }

    private float evalTerms (String str) {
    	
    	/*Tokenizes input around * and /, recursively throws down single numbers to evalFactors*/
		StringTokenizer st = new StringTokenizer(str, "*/", true);
		Stack<String> values = new Stack<String>();
		
		float val = 0;
		
		
		while(st.hasMoreTokens()) {
			String token = st.nextToken();
			if (token.equals("*")) {
				val = evalFactors(values.pop()) * evalFactors(st.nextToken());
				values.push(String.valueOf(val));
			}
			else if (token.equals("/")) {
				val = evalFactors(values.pop()) / evalFactors(st.nextToken());
				values.push(String.valueOf(val));
			}
			else
				values.push(token);
		}
		
		while(!values.isEmpty()){
			val = evalFactors(values.pop());
		}
    	return val;
    }
    
    private float evalFactors (String str) {
    	/*Base case for recursive descent parsing: returns float value of String number*/
 		return Float.parseFloat(str);
    }
	private String rebuildExpression (String str) {
		
		/*Rebuilds a string input to eliminate spaces and tabs and to replace scalar variables with 
		 * integer constants. Necessary to allow other functions to Tokenize string*/
		
		String newExpression = "";
		
		StringTokenizer st = new StringTokenizer(expr, delims, true);
		Stack<String> expression = new Stack<String>();
		
		while(st.hasMoreTokens()) {
			String token = st.nextToken();
			if (!token.equals(" ") && !token.equals("	"))
				expression.push(token);
		}
		
		String prevVar = "";
		
		while(!expression.isEmpty()) {
				String var = expression.pop();
			
			
				if(isScalarVar(var) && !prevVar.equals("[")) {
					newExpression = scalars.get(findScalar(var)).value + newExpression;
				}
				else
					newExpression = var + newExpression;
				prevVar = var;
			
		}
	return newExpression;
		
	}
	
	private float parenEval(String str) {
		
		/* Tokenizes rebuilt String around delimiters and tosses tokens into a stack until it reaches a ) or ]
		 * token. Once a ) or ] is reached, pops tokens off Stack until an opening ( or [ is reached and builds
		 * a String subexpression from the popped tokens, then sends the subexpression to evaluate to be
		 * recursively evaluated.
		 * 
		 * Once a subexpression in parens () is evaluated, the result is turned back into a String and pushed back on to
		 * the Stack.
		 * 
		 * If the evaluated subexpression is within brackets [], retrieves the array variable name that will be 
		 * at the top of the stack and locates the associated integer constant from the ArrayList.*/
		
		StringTokenizer st = new StringTokenizer(str, delims, true);
		Stack<String> expression = new Stack<String>();
		
		while(st.hasMoreTokens()) {
			String token = st.nextToken();
			if (token.equals(")")){
				String newEval = "";
				String evalVar = "";
				do {
					newEval = evalVar + newEval;
					evalVar = expression.pop();
					
				} while (!evalVar.equals("("));
				expression.push(Float.toString(evaluate(newEval)));
			}
			else if (token.equals("]")) {
				String newEval = "";
				String evalVar = "";
				
				
				do {
					newEval = evalVar + newEval;
					evalVar = expression.pop();
					
				} while (!evalVar.equals("["));
				
				int arrayIndex = (int) evaluate(newEval);
				String arrayVar = expression.pop();
				expression.push(String.valueOf(arrayVarVal(arrayVar, arrayIndex)));
				
			}
			else 
				expression.push(token);
			
		}
		
		String result = "";
		
		while (!expression.isEmpty()) {
			result = expression.pop() + result;
		}
		
		return evaluate(result);
		
		
	}
	
	private float arrayVarVal (String name, int index) {
		
		/*Finds the integer constant associated with array variable of name at index.*/
		int i = 0;
		for (; i < arrays.size(); i++) {
			if(arrays.get(i).name.equals(name))
					break;
		}
		return arrays.get(i).values[index];
	}
	
	private int findScalar(String str) {
		
		/*Finds the index in the scalars ArrayList associated with the given scalar variable name*/
		
		int i = 0;
		for (; i < scalars.size(); i++) {
			if (scalars.get(i).name.equals(str))
				break;
		}
		return i;
	}
	
	
	private boolean isNumeric(String str) {
		
		/*Tests whether a given string is a number*/
		try {
			
			Float.parseFloat(str);
			
		} catch (NumberFormatException e) {
			return false;
		}
		
		return true;
	}
	
	private boolean isScalarVar (String str) {
		
		/*Returns true only if the passed token is not a delimiter or number*/
		
		if (str.equals("+") || str.equals("-") || str.equals("*") || str.equals("/") || str.equals("(") || str.equals(")") || str.equals("[") || str.equals("]") || isNumeric(str))
			return false;
		return true;
	}


	/**
	 * Utility method, prints the symbols in the scalars list
	 */
	public void printScalars() {
		for (ScalarSymbol ss : scalars) {
			System.out.println(ss);
		}
	}

	/**
	 * Utility method, prints the symbols in the arrays list
	 */
	public void printArrays() {
		for (ArraySymbol as : arrays) {
			System.out.println(as);
		}
	}

}
