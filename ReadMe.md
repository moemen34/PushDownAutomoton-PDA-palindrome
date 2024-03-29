# Program Description

This program represents the implementation of a Non-deterministic Pushdown Automaton (NPDA) that checks whether a string inputted by the user is a palindrome or not.

This NPDA consists of 4 states:

### State 1
This is the Starting state. Pushes a ‘$’ to the stack while transitioning to State 2.

### State 2
Reads the first character of the string and pushes it to the stack. It then attempts to take an epsilon transition to state 3 by reading the next character without pushing it (test for odd length palindromes by not pushing the middle character). If the string wasn’t accepted as an odd-length character, push the skipped character and take an epsilon transition (reading and pushing nothing) to state 3 to check if the string is an even-length palindrome. Repeat this process until the string is accepted or the end of the string is reached.

### State 3
Attempt to take an epsilon transition to state 4 (popping ‘$’ from the stack). If the string was not accepted, read the first character of the string and pop one character from the stack. Repeat this process as long as the read and popped characters match.

### State 4
Accepting state. If this state was reached, the string was accepted as a palindrome.

Once the process above is finished, if the string was accepted, the program prints an instance of the decision tree that takes the finite machine to the accepting state with the given input string. If the string was not accepted, it prints a statement stating that the string is not a palindrome.

# How to Run the Program

1. Open a command prompt window and go to the directory where you saved the Java program.
2. Type "javac PDA.java" to compile the program.
3. Type "java PDA" to run the program.
4. Once the program runs, it will ask you to input an input string to check if it's a palindrome implementing the PDA.
