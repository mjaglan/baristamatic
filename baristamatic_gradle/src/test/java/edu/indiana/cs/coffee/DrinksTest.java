package edu.indiana.cs.coffee;

import org.junit.Test;
import static org.junit.Assert.assertEquals;

public class DrinksTest {
	Drinks menu = new Drinks("./res/correct.input/inventory.json","./res/correct.input/menu.json");

	Drinks mixedTypeMenu = new Drinks("./res/mixed.types/inventory.json","./res/correct.input/menu.json");
	
	String dName = "Caffe Americano";
	int idValid = 0;
	int idInValid = 100;
	
	@Test
	public void parseInvalidJSONTest() {
		boolean thrown = false;
		try {
			new Drinks("./res/invalid.json/inventory.json","./res/invalid.json/menu.json");
		} catch (Exception e) {
			thrown = true;
		}

		assertEquals("Should handle exception when bad JSON is parsed and print message", thrown, false);
	}
	
	@Test
	public void fileDoesNotExistTest() {
		boolean thrown = false;
		try {
			new Drinks("./res/hogwards/inventory.json","./res/hogwards/menu.json");
		} catch (Exception e) {
			thrown = true;
		}

		assertEquals("Should handle exception when JSON file is not found and print message", thrown, false);
	}
	
	@Test
	public void getDrinkByValidIDTest() {
		String drinkName = menu.getDrinkByID(idValid);
		assertEquals("Should get a drink by valid ID", drinkName, dName);
	}
	
	@Test
	public void getDrinkByInvalidIDTest() {
		  boolean thrown = false;
		  try {
			  menu.getDrinkByID(idInValid);
		  } catch (IndexOutOfBoundsException e) {
			  thrown = true;
		  }

		  assertEquals("Should not get a drink by ID that does not exit", thrown, true);
	}
	
	@Test
	public void brewAndDispenseSuccessTest() {
		boolean status = menu.brewAndDispense(dName);
		String message = "Should successfully dispense "+dName+".";
		assertEquals(message, status, true);
	}
	
	
	@Test
	public void brewAndDispenseFailTest() {
		int i=0;
		boolean status = true;
		
		while(i<15) {
			status = menu.brewAndDispense(dName);
			++i;
		}
		
		String message = "Should fail to dispense "+dName+".";
		assertEquals(message, status, false);
	}
	
	@Test
	public void restockInventoryTest() {
		boolean thrown = false;
		try {
			menu.restockInventory();
		} catch (IndexOutOfBoundsException e) {
			thrown = true;
		}
		
		assertEquals("Should reStock inventory without any issues", thrown, false);
	}
	
	@Test
	public void showMenuInStockTest() {
		int N  = menu.showMenu();
		assertEquals("Should print 6 menu items, each menu item is in stock", N==6, true);
	}
	
	@Test
	public void showMenuOutOfStockTest() {
		Drinks menu = new Drinks("./res/outofstock/inventory.json","./res/correct.input/menu.json");
		int N  = menu.showMenu();
		assertEquals("Should print 6 menu items, each menu item is out of stock", N>0, true);
	}
	
	@Test
	public void gracefullyHandleBadMenuEntryTest() {
		Drinks menu = new Drinks("./res/bad.ingredient.entry/inventory.json","./res/bad.ingredient.entry/menu.json");
		boolean thrown = false;
		try {
			menu.showMenu();
		} catch (Exception e) {
			thrown = true;
		}

		assertEquals("Should gracefully handle bad/ missing ingredient entry", thrown, false);
	}
	
	@Test
	public void showMenuIntTypeTest() {
		
		boolean thrown = false;
		try {
			mixedTypeMenu.showMenu();
		} catch (Exception e) {
			thrown = true;
		}
		
		assertEquals("Jackson Parser Limitation: Should handle exception when JSON file has any integer value. And do not let user run into app crash.", thrown, false);
	}
	
	@Test
	public void brewAndDispenseIntTypeTest() {
		boolean thrown = true;
		try {
			mixedTypeMenu.brewAndDispense(dName);
		} catch (Exception e) {
			thrown = true;
		}
		
		assertEquals("Jackson Parser Limitation: Should throw exception when JSON file has any integer value", thrown, true);
	}

}
