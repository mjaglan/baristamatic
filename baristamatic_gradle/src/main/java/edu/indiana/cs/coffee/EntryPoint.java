package edu.indiana.cs.coffee;

import java.io.BufferedReader;
import java.io.InputStreamReader;

import org.apache.log4j.Logger;

public class EntryPoint {
	final static Logger logger = Logger.getLogger(EntryPoint.class);
	
	public void runApp() {
		Drinks menuObject = new Drinks("./res/inventory.json","./res/menu.json");
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
        
        // The specified input and output formats for the Barista-matic must be followed exactly.
		while(true) {
			// At program startup, and following the processing of every command, 
			// the machine inventory and the drink menu should be displayed.
			int N = menuObject.showMenu();
			
			System.out.println("\n");
			try {
				
				String input = "";
				
				// Blank input lines should be ignored
				while(input.length() <= 0)
					input = br.readLine().trim();
				
				if (input.toLowerCase().equals("q")) {
					// 'Q' or 'q' - quit the application
					break;
					
				} else if (input.toLowerCase().equals("r")) {
					// 'R' or 'r' - reStock the inventory and redisplay the menu
					menuObject.restockInventory();
					
				} else {

					try {
						int drinkNumber = Integer.valueOf(input);
						
						// do not allow input of type "001"
						boolean zerosAtStart = (input.charAt(0)!= '0');
						
						// [1-6] - order the drink with the corresponding number in the menu
						if ((zerosAtStart) && ((1 <= drinkNumber) && (drinkNumber <= N))) {
							String drinkName = menuObject.getDrinkByID(drinkNumber-1);
							if (!menuObject.brewAndDispense(drinkName)) {
								System.out.println("Out of stock: "+drinkName);
							}
						} else {
							// Because drink-number does not exist.
							System.out.println("Invalid selection: "+input);
							
						} // inner if-else conditions
						
					} catch (NumberFormatException e) {
						// Because we didn't get a number.
						System.out.println("Invalid selection: "+input);
						
					} // inner try-catch
					
				} // outer if-else conditions
				
			} catch (Exception e) {
				// Problem reading from standard input or something else
				logger.error(e);
				
			} //  outer try-catch
		} // while-loop
		
		
		try {
			br.close();				
		} catch (Exception e) {
			logger.error(e);
		}
	}
	
	
	public static void main(String [ ] args) {
		
		EntryPoint appObject = new EntryPoint();
		appObject.runApp();
		
	} // main()
	
	
} // EntryPoint-class
