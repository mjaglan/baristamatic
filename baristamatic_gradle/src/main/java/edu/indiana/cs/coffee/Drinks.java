package edu.indiana.cs.coffee;

import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.*;
import org.apache.log4j.Logger;

public class Drinks {
	final static Logger logger = Logger.getLogger(Drinks.class);

	/* private members */
	private Inventory invObj;
	private HashMap<String, HashMap<String,Integer>>  menu;
	private ArrayList<String> menuNames;
	
	/* public methods */
	@SuppressWarnings("unchecked")
	public Drinks(String inventoryFileName, String menuFileName) {
		this.invObj = new Inventory(inventoryFileName);
		
		/*
		 * GIVEN: It is not required that the initial machine configuration (inventory counts, available drinks and prices, etc.) be dynamic. 
		 * In particular, it is acceptable to perform this initialization in code, rather than reading the configuration from an external file 
		 * or database. However, your program should be flexible enough to allow new drinks to be added to the menu without requiring extensive 
		 * code changes.
		 * 
		 * So, it is decided to put all drinks menu & recipe in the external file. Now menu is flexible enough to allow new drinks to be added 
		 * without requiring extensive code changes.
		 * 
		 * Just make changes to the external file and restart this simulator program.
		 * 
		 * */
		
		// code to parse json
		try {
			byte[] jsonData = Files.readAllBytes(Paths.get(menuFileName));
			ObjectMapper objectMapper = new ObjectMapper();
			this.menu = objectMapper.readValue(jsonData, HashMap.class);
			
			// requirement is to always display sorted list of menu items
			this.menuNames = new ArrayList<String>(this.menu.keySet());
			Collections.sort(this.menuNames);
			
		} catch (IOException e) {
			logger.error(e);
		}

	}
	
	
	public String getDrinkByID(int idx) {
		return this.menuNames.get(idx);
	}
	
	
	public boolean brewAndDispense(String drinkName) {
		
		// first check if all ingredients are in stock with required quantity.
		for(Map.Entry<String, Integer> entry : this.menu.get(drinkName).entrySet()) {
			if (this.invObj.getItemStock(entry.getKey()) < entry.getValue()) {
				return false;
			}
		}
		
		// If all ingredients are in stock with required quantity, brew and dispense the drink.
		System.out.println("Dispensing: "+drinkName);
		for(Map.Entry<String, Integer> entry : this.menu.get(drinkName).entrySet()) {
			// return value is not required here in this workflow.
			this.invObj.checkOutItem( entry.getKey(), entry.getValue() );
		}
		return true;
	}
	
	
	public void restockInventory() {
		this.invObj.restock();
	}
	
	
	public Integer showMenu() {

		// first show all inventory
		this.invObj.showInventory();
		
		String currentMenuItem = "None";

		try {
			// The drink menu should be displayed in alphabetic order (i.e., by drink name)
			int i=0;
			System.out.println("\nMenu:");
			for(String key:menuNames) {
				currentMenuItem = key;
				
				// While checking if all ingredients are in stock, compute the price of drink at the same time, inside the same loop.
				boolean inStock = true; // compute if-in-stock
				double price = 0.0;     // compute latest price
	
				for (Map.Entry<String, Integer> entry : this.menu.get(key).entrySet()) {
					if (this.invObj.getItemStock(entry.getKey()) < entry.getValue()) {
						inStock = false;
					}
					
					price += (this.invObj.getItemPrice(entry.getKey()) * entry.getValue());
				}
	
				double roundedPrice = Math.round(price*100.0)/100.0;
				System.out.printf("%d,%s,$%.2f,%b\n",(i+1),key,roundedPrice,inStock);
				++i;
			}
			
		} catch (NullPointerException  e){
			logger.error(e);
			System.out.printf("WARNING: Bad user configuration. Menu items from \"%s\" onwards are not loaded.\n",currentMenuItem);
			System.out.printf("       : Please check \"menu.json\" and \"inventory.json\" for correct matching ingredient entries.\n");
			
		} catch (ClassCastException e) {
			logger.error(e);
			System.out.printf("WARNING: \"inventory.json\" file has stock values in integral format.\n");
			System.out.printf("       : Please exit program now, enter decimal format values, and start program again.\n");
			
		} catch (Exception  e){
			logger.error(e);
			
		}
		
		return menuNames.size();
	}

}
