import java.util.ArrayList;
import java.util.Scanner;
import java.util.Stack;

public class PDA {
	
	 //flag that is set to true if a palindrome is accepted
	 static boolean accepted = false;
	 static Stack<Character> copy = new Stack<Character>();		 //used to keep track of changes made when popping
	 static ArrayList<String> state1Transitions = new ArrayList<String>(); //arrayList that stores state1's transitions 
	 static ArrayList<String> state2Transitions = new ArrayList<String>(); //arrayList that stores state2's transitions
	 static ArrayList<String> state3Transitions = new ArrayList<String>(); //arrayList that stores state3's transitions
	 
	 
	 public static void main(String[] args) {
		 
		 Stack<Character> stack0 = new Stack<Character>();

		 String s = getString(); //string to Test
		 
		 //call the starting state of the PDA		 
		 state1(s, stack0);
		 
		 if(accepted == true) { 
			 printPath();
			 System.out.println();
			 System.out.println("The string "+ s +" IS a palindrome!");
		 }else
			 System.out.println("The string "+ s +" IS NOT a palindrome!");		 
	 }
	 
	 /*
	  * get a testing string from the used and return it
	  */
	 public static String getString() {
	     @SuppressWarnings("resource")    
		 Scanner scanner = new Scanner(System.in);  // Create a Scanner object
	     System.out.println("Enter Test String:");
	     String str = scanner.nextLine();  // Read user input;	 
		 return str;
	 }
	 

	//Start state of the PDA
	 public static void state1(String str1, Stack<Character> stack1) {
		 
		 //push '$' to stack
		 stack1.push('$');
		 //add first transition to arrayList
		 state1Transitions.add("E,E->$" + "          stack: "+ stack1);
		 
		 //create a copy of the stack that doesn't get affected by pops
		 //used to keep track of branching since this is a NDPDA
		 copy.push('$');		 
		 state2Odd(str1, stack1);
	 }
	 
	
	 /**
	  * Second state of the PDA, first skips pushing middle character into the 
	  * stack to check if the string is a palindrome of even length, if the 
	  * string was not accepted it checks if it's a palindrome of even length
	  * @param str2: remaining string to read
	  * @param stack2: PDA stack 
	  */
	 @SuppressWarnings("unchecked")
	public static void state2Odd(String str2, Stack<Character> stack2) {
		//take and add epsilon transition to state 3 to the state2Transitions arrayList		 
		if(str2.isEmpty()) {
			//if the string is empty don't read anything
			state2Transitions.add("E,E->E" + "          stack: " + copy);
			state3(str2, stack2); 
			return;						
		}else//if the string is not empty read the first char
			state2Transitions.add(str2.charAt(0) + ",E->E" + "          stack: " + copy);
		
		//Loop that skips pushing first char in the passed string while sending the PDA to state 3		
		while(state3(str2.substring(1), stack2) == false) {
			//remove the epsilon move from the arrayList since it failed
			state2Transitions.remove(state2Transitions.size()-1);
			//if the string was not accepted push its first char to the stack			
			copy.push(str2.charAt(0));
			//add move to arrayList that keeps track of state 2 transitions
			state2Transitions.add(str2.charAt(0) + ",E->" + str2.charAt(0) + "          stack: " + copy);
			//clone the stack to avoid changes made in state 3 when popping
			stack2 = (Stack<Character>) copy.clone();
			//get the resulting substring when removing the first character 
			str2 = str2.substring(1);
			
			//after pushing the char check if the string is a palindrome of even length 
			if(state3(str2.substring(0), stack2) == true) {
				state2Transitions.add("E,E->E" + "          stack: " + copy);
				break;
			}
			//since the string was not accepted, undo the changes done to the stack in state 3
			stack2 = (Stack<Character>) copy.clone();
			
			//if the substring is empty or the string was accepted break the loop
			if(str2.isEmpty() || accepted) {
				break;
			}
			//add epsilon transition to the arrayList 
			state2Transitions.add(str2.charAt(0) + ",E->E" + "          stack: " + copy);	
		 }		 
	 }
	 
	 
	 /**
	  * Third state of the PDA 
	  * @param str3: remaining string to read
	  * @param stack3: PDA stack 
	  * @return true:  if the string was found to be a palindrome 
	  * 		false: if the string was found to not be a palindrome
	  */
	 public static Boolean state3(String str3, Stack<Character> stack3) {
		 
		 /*check for epsilon transitions first to state 4
		 if the string isn't accepted and char on top of stack 
	  	 is the same as the first char of remaining string
		 pop it from stack and read char*/
		 while(state4(str3, stack3) == false) {
				 
			 //if the string or stack is empty clear the arrayList that keeps track 
			 //of the transitions and return false
			 if(str3.isEmpty() || stack3.empty()) {			 
				 state3Transitions.clear();				 
				return false;
			 }	
			 
			 //if the popped char matches the first in the string
			 if(stack3.pop() == str3.charAt(0)) {				 
				 //add the transition to the arrayList that keeps track of the transitions
				 state3Transitions.add(str3.charAt(0) + "," + str3.charAt(0) + "->E" + "          stack: " + stack3);
				 //remove the first letter from the string
				 str3 = str3.substring(1);				 	 
			 }else {
				 //else clear the arrayList and return false
				 state3Transitions.clear();
				 return false;
			 }
		 }
		 
		 //if state 4 returned true(string was accepted) and the end
		 //of the string is reached return true
		 if(str3.isEmpty() && accepted) {
			 return true;
		 }else {
			 //else clear the arrayList and return false
			 state3Transitions.clear();
			 return false;
		 }
	 }
	 
	 /**
	  * Fourth and accepting state of the PDA
	  * @param str: remaining string to read
	  * @param stack: PDA stack
	  * @return true:  if the string was found to be a palindrome 
	  * 		false: if the string was found to not be a palindrome
	  */
	 public static Boolean state4(String str, Stack<Character> stack) {
		 //if the stack is empty return false
		 if(stack.empty()) {
			 return false;
		 }
		 //if the end of the string and stack is reached set the accepted flag to true
		 //and return true
		 if(str.isEmpty() && (stack.pop() == '$' )) {
			state3Transitions.add("E," + "$->E" + "          stack: " + stack);
			accepted =true;	
 			return true;
 		}
 		else//else return false
 			return false;		 
	 }
	 
	 /**
	  * method that prints the acceptance path of palindromes
	  */
	 public static void printPath() {
		 
		 System.out.println();
		 System.out.println("STATE 1:");
		 for(String s : state1Transitions) {
			 System.out.println(s +"     TRANSITION TO STATE 2");
		 }
		 
		 
		 System.out.println();
		 System.out.println("STATE 2:");
		 for(int i=0; i < state2Transitions.size(); i++) {
			 if(i == state2Transitions.size()-1) {
				 System.out.println();
				 System.out.println(state2Transitions.get(i)+"     TRANSITION TO STATE 3");
			 }else
				 System.out.println(state2Transitions.get(i));
		 }
		 
		 
		 System.out.println();
		 System.out.println("STATE 3:");
		 for(int i=0; i < state3Transitions.size(); i++) {
			 if(i == state3Transitions.size()-1) {
				 System.out.println();
				 System.out.println(state3Transitions.get(i)+"     TRANSITION TO STATE 4");
			 }else
				 System.out.println(state3Transitions.get(i));
		 }
		 
		 
		 System.out.println();
		 System.out.println("STATE 4:");
		 System.out.println("ACCEPTING STATE REACHED!");	 		 
	 }	 
}
