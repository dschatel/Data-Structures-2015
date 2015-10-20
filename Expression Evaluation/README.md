Expression.java <br/>
String expression evaluator

This program reads a string input in the form of an algebraic expression with +, -, * and / operators and evaluates the expression. The expression may contain variables in the form of scalar (single value) or array (multiple values, associated with array indices). Expressions may have any number of nested parentheses and array brackets and they may also have any number of spaces or tabs between tokens.

Input Assumptions:

a) No unary operators, only +, -, * and /. b) If a variable is used in an expression, then that variable will have a corresponding value in the accompanying text file.

I was responsible for coding the following methods:

1) isLegallyMatched:

This method verifies that the opening and closing brackets/parentheses in the expression are legally matched, i.e. that for every opening paren/bracket there is a corresponding closing paren/bracket in the appropriate place. It also creates two ArrayLists of the opening/closing bracket/paren indices. If the expression is legally matched, the method returns true; false otherwise.

2) buildSymbols:

This method parses the string and stores all scalar and array variables in ArrayLists. It only stores each variable once, even if it appears more than one time in the expression.

3) evaluate:

This method evaluates the String by parsing and evaluating each subexpression (i.e. expressions in parens/brackets). It was a requirement that this method be done recursively.

Examples of valid input:

Enter the expression, or hit return to quit => (a + A[a*2-b]) <br/>
Expression legally matched: true <br/>
Enter symbol values file name, or hit return if no symbols => etest1.txt <br/>
Value of expression = 8.0

Enter the expression, or hit return to quit => a - (b+A[B[2]])*d + 3 <br/>
Expression legally matched: true <br/>
Enter symbol values file name, or hit return if no symbols => etest1.txt <br/>
Value of expression = -106.0