package edu.iastate.cs228.hw3;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Scanner;

/**
 * This class reads one expression at a time from the input.txt file and performs the infix to postfix conversion on them
 * The output of this conversion will be printed to output.txt in postfix form
 * 
 * @author Charlene Baes
 *
 */
public class ReadAndWrite 
{
	/**
	 * Method that reads a infix expression from the file "input.txt"
	 * Reads lines of the text file one at a time and converts them to postfix
	 * 
	 * @param fileName
	 * @throws IOException 
	 */
	public static void readFile(File fileName) throws IOException
	{
		Scanner scnr = new Scanner(fileName);
	
		String line; //represents a singular line in the text file
		String element = ""; //represents String created by a one or more digit element in the text file
	
		ArrayList<String> infix = new ArrayList<String>();
		
		int index;
	
		while(scnr.hasNextLine()) //while the input file has a next line / next expression ...
		{
			line = scnr.nextLine();
	
			for(int i = 0; i < line.length(); i++)
			{
				if(line.charAt(i) != ' ') //if given char is not a space, concatenate to element
					element += line.charAt(i);
				else //if given char is a space, add element to the infix arrayList
				{
					infix.add(element);
					element = "";
				}
			
				if(i == line.length() - 1) //accounts for the last element of the string, does not have space at end
					infix.add(element);
			}
			
			index = infix.size() - 1;
			
			Converter c = new Converter(infix);
			c.conversion();
			
			/*
			 * Clear element, line, and the infix arrayList before beginning with the next expression
			 */
			while(infix.size() > 0)
			{
				infix.remove(index);
				index--;
			}
			
			element = "";
			line = "";
			
		}
	}
	
	/**
	 * Write the postfix output to an output text file named output.txt
	 * Appends to the file, does not rewrite
	 * 
	 * @param fileName
	 * @return true if the file was successfully written to
	 * @return false if there was an error during file writing
	 * @throws IOException 
	 */
	public static void writeFile(String input) throws IOException
	{
		/*
		 * relative path to output.txt does not work when writing inside of my eclipse
		 * correct line of code when testing: FileWriter writer = new FileWriter("output.txt", true);
		 */
		FileWriter writer = new FileWriter("output.txt", true);
		
		writer.write(input.trim() + "\n");
		writer.close();
	}
}
