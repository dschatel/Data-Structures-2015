# Data-Structures-2015
Programming assignments created for CS 112

Current Items:

1) Polynomial.java

Reads and interprets a text file containing a polynomial expression in a certain format, then enters it into a linked list. The program then permits the user to perform addition, multiplication, and evaluation of polynomials.

2) Expression.java

Reads an algebraic expression in as a String and evaluates with Stacks and recursion. The expression is limited to integer constants and +, -, *, and / operators. It may contain any number of nested parentheses as well as variables. Variables may be scalar (containing a single value) or array (containing multiple values that correspond to array index numbers). Variable values are read in from a text file.

3) Interval Tree

Given an input of a series of intervals (e.g. [1, 5], [3, 4], [4, 8] etc), constructs a binary search tree where the leaf nodes are the individual endpoints and all other nodes consist of "split values" for these endpoints. Then maps each interval to a split value node. Afterward, given a new interval, returns what mapped intervals that interval intersects with.
