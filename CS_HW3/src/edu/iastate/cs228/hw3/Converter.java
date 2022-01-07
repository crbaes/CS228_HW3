package edu.iastate.cs228.hw3;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Stack;

/**
 * This class converts a given ArrayList<String> infix expression from input. txt to a String postfix expression that can be viewed in output.txt
 * Finds if an error exists within the expression and writes the given error to output.txt
 * 
 * @author Charlene Baes
 *
 */
public class Converter 
{
	ArrayList<String> infix = new ArrayList<String>();
	Stack<String> stk = new Stack<String>();
	int index = 0; //keeps track of the index of postfix
	private static int errorIndex = 0; //keeps track of the index to be returned in the error message
	
	String postfix = "";
	
	/**
	 * Constructor for a converter given an ArrayList String input
	 * 
	 * @param infix
	 */
	public Converter(ArrayList<String> infix)
	{
		this.infix = infix;
	}
	
	
	/**
	 * Converts the given infix expression to a postfix expression
	 * 
	 * First, checks the toPrint() method to see if there is an error within the expression
	 * Iterates through the infix array by calling the variable at infix.get(index)
	 * 
	 * 1. If the element is not an operator (it is a number / letter) add to postfix
	 * 2. If the current element is an operator:
	 * 		2a. Push it to the stack if the stack is empty
	 * 		2b. If current has greater precedence than the top of the stack, push it to the stack
	 * 		2c. If current has equal precedence than the top of the stack, pop the top of the stack and push the current element
	 * 			2c1. If current and peek() are exponents, do not pop the top, only push the current element
	 * 		2d. If current is less than or equal to precedence than the top of the stack, while the stack is not empty or equals an open parenthesis
	 * 			pop the top of the stack and add it to the postfix, and then add the current element to the stack
	 * 	3. If the character is an open parenthesis, push it to the stack
	 * 		3a. If the character is a closed parenthesis, pop all elements of the stack until an open parenthesis is found
	 * 		3b. Pop the top of the stack, the last open parenthesis
	 * 		
	 * @throws IOException 
	 */
	public void conversion() throws IOException
	{
	
	if(!toPrint(infix)) //if toPrint has not already found an error within the expression, then continue
	{
		while(index < infix.size()) //iterate through the whole infix ArrayList
		{
			String current = infix.get(index);
			
			if(!isOperator(current) && !isParenthesis(current)) //if the current element is not an operator, add to postfix
			{
				postfix += current + " ";
				index++;
			}
			else if(isOperator(current)) //if the current element is an operator
			{
				if(stk.empty()) //if the operator stack is empty, push it to the stack
				{
					stk.push(current);
				}
				else //if the operator stack is not empty
				{
					/*
					 * If the precedence of the current element is GREATER THAN the top of the stack and is not an open parenthesis
					 * Push the current element to the top of the stack
					 */
					if(stk.peek().equals("(") || Precedence.getPrec(current) > Precedence.getPrec(stk.peek()))
					{
						stk.push(current);
					}
					
					/*
					 * If the precedence of the current element is EQUAL TO the top of the stack
					 */
					else if(Precedence.getPrec(current) == (Precedence.getPrec(stk.peek())))
					{
						/*
						 * Different rules apply to exponents when precedence is equal, they do not pop the top of the stack
						 */
						if(current.equals("^") && stk.peek().equals("^"))
							stk.push(current);
						else //if the current operator is not a "^", but is still equal to peek()
						{
							postfix += stk.pop() + " ";
							stk.push(current);	
						}
					}
					/*
					 * If the precedence of the current element is LESS THAN the one at the top of the stack
					 */
					else
					{
						/*
						 * While stack is not empty, operator is less than the precedence of top of stack, and peek() is not an open parenthesis
						 * Pop the top of the stack to the postfix and then push the current element to the stack
						 */
						
						while(!stk.isEmpty() && Precedence.getPrec(current) <= Precedence.getPrec(stk.peek()) && !stk.peek().equals("("))
						{	
							postfix += stk.pop() + " ";
						}
						
						stk.push(current);
					}
				}
				index++;
			}
			else //if the character is a parenthesis
			{
				if(current.equals("("))
					stk.push(current);
				
				else //with a closed bracket, pop the stack until current == "("
				{
					while(!stk.peek().equals("("))
					{
						postfix += stk.pop() + " ";
					}
					
					stk.pop(); //this pop should remove (, which is at top of the stack
				}
				
				index++;
			}
		}
		
		/*
		 * For any remaining operators in the infix, pop them to the postfix
		 */
		while(!stk.isEmpty())
			postfix += stk.pop() + " ";
		
		ReadAndWrite.writeFile(postfix);
	}
	}
	
	/**
	 * Returns whether or not the given input is an operator or an operand
	 * 
	 * @param input
	 * @return true if the given input is an operator +, -, *, /, %, ^
	 * @return false if the given input is an operand
	 */
	protected static boolean isOperator(String input)
	{
		if(input.equals("+") || input.equals("-") || input.equals("*") || input.equals("/") || input.equals("%") || input.equals("^"))
			return true;
		else
			return false;
	}
	
	/**
	 * Returns true if the given input is an open or close parenthesis
	 * 
	 * @param input
	 * @return true if the given input is a parenthesis
	 * @return false if the given input is not a parenthesis
	 */
	protected static boolean isParenthesis(String input)
	{
		if(input.equals("(") || input.equals(")"))
			return true;
		else
			return false;
	}
	
	/**
	 * This method checks if there are any errors within the given infix expression, before implementing conversion()
	 * 
	 * 1. There cannot be two operators next to each other
	 * 2. There cannot be an empty set of parenthesis. There must be a subexpression inside
	 * 3. There cannot be an odd amount of close & open parenthesis. All close parenthesis must have an open one, etc.
	 * 	  There are more open than close parenthesis 
	 * 4. There cannot be an odd amount of close & open parenthesis. All close parenthesis must have an open one, etc.
	 * 	  There are more close than parenthesis 
	 * 5. There cannot be two operands right next to each other, they must be separated by an operator or a parenthesis
	 * 
	 * @param infix
	 * @return the number corresponding with the given error
	 */
	protected static int errorFinder(ArrayList<String> infix)
	{
		int open = 0;
		int close = 0;
		
		//iterates through infix and counts the amount of open and closed parenthesis
		for(int i = 0; i < infix.size(); i++)
		{
			if(infix.get(i).equals("("))
			{
				open++;
			}
			else if(infix.get(i).equals(")"))
			{
				close++;
			}
		}
		
		for(int i = 0; i < infix.size() - 1; i++)
		{
			/*
			 * If there are two operators right next to each other
			 */
			if(isOperator(infix.get(i)) && isOperator(infix.get(i+1)))
			{
				errorIndex = i + 1;
				return 1;
			}
			else if ((infix.get(i).equals("(")) && isOperator(infix.get(i+1)))
			{
				errorIndex = i + 1;
				return 1;
			}
			/*
			 * There is a pair of parenthesis without a subexpression inside ()
			 */
			else if(infix.get(i).equals("(") && infix.get(i+1).equals(")"))
				return 2;
			/*
			 * If the current element and next element are not an operator and are not a parenthesis
			 * Two operands cannot be next to each other
			 */
			else if (!isOperator(infix.get(i)) && !(isOperator(infix.get(i+1))) && !isParenthesis(infix.get(i)) && !isParenthesis(infix.get(i+1)))
			{
				errorIndex = i + 1;
				return 5;
			}	
		}
		
		/*
		 * If there are too many open parenthesis, and not an equal amount of close parenthesis
		 */
		if(open > close)
			return 3;
		/*
		 * If there are too many close parenthesis, and not an equal amount of open parenthesis
		 */
		else if(close > open)
			return 4;
		else //return false if there are no errors within the expression
			return 0;
	}
	
	/**
	 * This method prints the error that is given by an incorrect expression to output.txt, based on the return value of errorFinder()
	 * If this method returns true, the program will not run conversion, it will print to output.txt and move to the next line if there is one
	 * 
	 * @param infix
	 * @return true if an error was found with the expression
	 * @return false if there was no error found with the expression
	 * @throws IOException
	 */
	public boolean toPrint(ArrayList<String> infix) throws IOException
	{
		String message = "";
		int flag = errorFinder(infix);
		
		if(flag == 1)
		{
			 message = "Error: too many operators in infix expression (" + infix.get(errorIndex) + ")";
			 ReadAndWrite.writeFile(message);
			 return true;
		}
		else if(flag == 2)
		{
			message = "Error: no subexpression detected (empty parenthesis) ()";
			ReadAndWrite.writeFile(message);
			return true;
		}
		else if(flag == 3)
		{
			message = "Error: no closing parenthesis detected, uneven parenthesis amount )";
			ReadAndWrite.writeFile(message);
			return true;
		}
		else if(flag == 4)
		{
			message =  "Error: no opening parenthesis detected, uneven parenthesis amount (";
			ReadAndWrite.writeFile(message);
			return true;
		}
		else if(flag == 5)
		{
			message =  "Error: too many operands (" + infix.get(errorIndex) + ")";
			ReadAndWrite.writeFile(message);
			return true;
		}
		else
			return false;
	}
}
