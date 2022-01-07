package edu.iastate.cs228.hw3;

/**
 * This class stores the precedence of the given characters
 *
 * @author Charlene Baes
 *
 */
public class Precedence 
{
	protected int precedence;

	/**
	 * Checks the precedence of the input current
	 * + and - have the same precedence
	 * multiplication, division, and modulo have the same precedence
	 * and the carrot has the highest precedence
	 * @param current
	 * @return
	 */
	public static int getPrec(String current)
	{
		if(current.equals("+") || current.equals("-"))
			return 1;
		else if(current.equals("*") || current.equals("/") || current.equals("%"))
			return 2;
		else if(current.equals("^")) 
			return 3;
		else //accounts for parenthesis which is highest precedence
			return 4;
	}
}
