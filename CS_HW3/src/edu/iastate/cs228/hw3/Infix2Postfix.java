package edu.iastate.cs228.hw3;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Scanner;

/**
 * This class contains the main method of this program
 * The user will input if they want to read from input.txt, to quit they hit 0
 * 
 * @author Charlene Baes
 *
 */
public class Infix2Postfix 
{
	public static void main(String[] args) throws IOException
	{
		Scanner scnr;
		
		System.out.println("Infix to Postfix Converter\nPress 1 to read from input.txt, Press 0 to exit\n");
		
		scnr = new Scanner(System.in);
		int userInput = scnr.nextInt();
		
		while(userInput == 1)
		{
			/*
			 * relative path to output.txt does not work when writing inside of my eclipse
			 * correct line of code when testing: File fileName = new File("input.txt");
			 */
			File fileName = new File("C:\\Users\\charb\\OneDrive\\Desktop\\cs 228\\CS_HW3\\src\\edu\\iastate\\cs228\\hw3\\input.txt\\");
			ReadAndWrite.readFile(fileName);
			
			System.out.println("Result printed to output.txt\nPress 1 to read from input.txt, Press 0 to exit");
			
			scnr = new Scanner(System.in);
			userInput = scnr.nextInt();
		}
		
		System.out.println("Exit key selected. Goodbye.");
		
	}
}
