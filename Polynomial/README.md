Polynomial.java

This program reads a Polynomial from a text file (written in a specified format), converts the polynomial into a linked list containing term Objects holding values for degree and coefficient, then allows the user to perform operations on the polynomials.

Parts of the program I was responsible for coding:

1) add

This method adds two polynomials together and returns a new linked list with the combined terms.

2) multiply

This method multiplies the terms of two polynomials together and returns the result.

3) evaluate

This method takes an input for x and returns the value that the polynomial evaluates to.

Notes on polynomials:

Negative terms are represented with negative numbers. i.e. 3x^2 - 2x is represented as 3x^2 + -2x

The polynomial linked list is maintained in ascending order.

Example:

Suppose we have the polynomial 4x^5 + 3x^2 + 5x + 10

The text file input will appear as follows:

4 5<br/>
3 2<br/>
5 1 <br/>
10 0

The polynomial will be represented in the linked list as follows:

head --> 10,0 --> 5,1 --> 3,2 --> 4,5

The linked lists that add and multiply return must adhere to this format.