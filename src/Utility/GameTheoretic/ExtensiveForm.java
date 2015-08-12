package Utility.gametheoretic;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Hashtable;
import java.util.Scanner;

import Utility.Pair;

/**
 * @author Martin
 *
 */
public class ExtensiveForm {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		
		Hashtable<Pair<String, String>, Pair<Integer, Integer>> outcome = new Hashtable<Pair<String, String>, Pair<Integer, Integer>>();
	
		outcome.put(new Pair<String, String>("RS", "HP"), new Pair<Integer, Integer>(7, -7));
		outcome.put(new Pair<String, String>("URS", "HP"), new Pair<Integer, Integer>(8, -8));
		outcome.put(new Pair<String, String>("RS", "IHP"), new Pair<Integer, Integer>(10, -10));
		outcome.put(new Pair<String, String>("URS", "IHP"), new Pair<Integer, Integer>(2, -2));
		
		Scanner scanner = new Scanner( System.in );
		
		while (scanner.hasNext()) {
			
			ArrayList<String> tokens = new ArrayList<String>(Arrays.asList(scanner.nextLine().split(" ")));
			
			int playerAPayoff = 0;
			int playerBPayoff = 0;
			
			for ( int i = 0; i < tokens.size(); i += 2) {
			
				if ( outcome.containsKey(new Pair<String, String>(tokens.get(i), tokens.get(i+1))) ) {
					
					playerAPayoff += outcome.get(new Pair<String, String>(tokens.get(i), tokens.get(i+1))).getElement0();
					
					playerBPayoff += outcome.get(new Pair<String, String>(tokens.get(i), tokens.get(i+1))).getElement1();
					
				}
			
			}
			
			System.out.println("A: " + playerAPayoff);
			
			System.out.println("B: " + playerBPayoff);
			
		}
		
		scanner.close();
		
	}
	
}
