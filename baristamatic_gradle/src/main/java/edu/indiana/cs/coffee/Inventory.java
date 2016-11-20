package edu.indiana.cs.coffee;

import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.*;

import org.apache.log4j.Logger;

public class Inventory {
	final static Logger logger = Logger.getLogger(Inventory.class);

	/* private members */
	private HashMap <String,ArrayList<Double>> inventory;
	private ArrayList<String> invNames;
	private String inventoryFileName;
	
	/* public methods */
	public Inventory(String inventoryFileName) {
		this.inventoryFileName = inventoryFileName;
		this.inventory = new HashMap<String, ArrayList<Double>>();
		
		// Initially the Barista-matic should contain 10 units of all ingredients.
		this.restock();
	}
	
	
	// How many units of an inventory item are there?
	public int getItemStock(String key) {
		return this.inventory.get(key).get(0).intValue();
	}

	
	// What is the price per unit of an inventory item?
	public double getItemPrice(String key) {
		return this.inventory.get(key).get(1);
	}
	
	
	public boolean checkOutItem (String key, Integer units) {
		double newValue = this.inventory.get(key).get(0) - units;
		if (newValue >= 0) {
			this.inventory.get(key).set(0, newValue);
			return true;
		} else {
			return false;
		}
	}
	
	
	/*
	 * GIVEN: It is not required that the initial machine configuration (inventory counts, available drinks and prices, etc.) be dynamic. 
	 * In particular, it is acceptable to perform this initialization in code, rather than reading the configuration from an external file 
	 * or database. However, your program should be flexible enough to allow new drinks to be added to the menu without requiring extensive 
	 * code changes.
	 * 
	 * Because it is decided to put all drinks menu & recipe in the external file. So that menu is flexible enough to allow new drinks to be added 
	 * without requiring extensive code changes.
	 * 
	 * Assuming, new drinks (like - Pumpkin Spice Latte) may include include new ingredients (like - Pumpkin Spice Sauce), it is decided to put all 
	 * ingredients and its <restock quantity, per unit price> in the external file.
	 * 
	 * Advantage of keeping <restock quantity, per unit price> in the external file:
	 *  - In future, price of individual ingredients may change.
	 *  - In future, restock quantity of individual ingredients may change.
	 *  
	 *  
	 *  Just make changes to the external file and call restock() method.
	 * 
	 * */
	@SuppressWarnings("unchecked")
	public void restock() {
		// Restocking the machine should restore each ingredient to a maximum of 10 units.
		
		// Idea is to parse json and reload <total-units, revised-prices>.
		try {
			byte[] jsonData = Files.readAllBytes(Paths.get(inventoryFileName));			
			ObjectMapper objectMapper = new ObjectMapper();
			this.inventory = objectMapper.readValue(jsonData, HashMap.class);
			
			// Maintain sorted list of ingredients for printing inventory.
			this.invNames = new ArrayList<String>(inventory.keySet());
			Collections.sort(this.invNames);
		} catch (IOException e) {
			logger.error(e);
		}
	}
	
	
	// NOTE: Not required to wite unit-test because it is neither modifying any data nor returning any value.
	public void showInventory() {
		// The inventory list should be displayed in alphabetic order (i.e., by ingredient name)
		System.out.println("\nInventory:");
		for(String key:this.invNames) {
			try {
				System.out.printf("%s,%d\n",key,this.inventory.get(key).get(0).intValue());
			} catch (ClassCastException e) {
				logger.error(e);
				System.out.printf("WARNING: \"%s\" item has integer type values in \"inventory.json\" file.\n",key);
				System.out.printf("       : Please exit program now, enter decimal format values, and start program again.\n");
				break;
			}
		}
	}
}
