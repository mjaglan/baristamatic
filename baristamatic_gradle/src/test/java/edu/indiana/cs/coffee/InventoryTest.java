package edu.indiana.cs.coffee;

import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class InventoryTest {
	Inventory mixedTypeInventory = new Inventory("./res/mixed.types/inventory.json");
	
	Inventory inventory = new Inventory("./res/correct.input/inventory.json");
	
	String invItem = "Coffee";
	double invPrice = 0.75;
	int invUnit = 10;	

	
	@Test
	public void parseInvalidJSONTest() {
		boolean thrown = false;
		try {
			new Inventory("./res/invalid.json/inventory.json");
		} catch (Exception e) {
			thrown = true;
		}

		assertEquals("Should handle exception when bad JSON is parsed and print message", thrown, false);
	}
	
	@Test
	public void fileDoesNotExistTest() {
		boolean thrown = false;
		try {
			new Inventory("./res/hogwards/inventory.json");
		} catch (Exception e) {
			thrown = true;
		}

		assertEquals("Should handle exception when JSON file is not found and print message", thrown, false);
	}
	
	@Test
	public void getItemStockTest() {
	      assertEquals("Should correctly get an inventory stock unit",inventory.getItemStock(invItem),invUnit);
	}
	
	@Test
	public void getItemPriceTest() {
		assertEquals("Should correctly get an inventory stock price",inventory.getItemPrice(invItem),invPrice,0.00);
	}
	
	@Test
	public void checkOutItemSmallTest() {
		int checkOutUnitSmall = (inventory.getItemStock(invItem) /2 ); // half of the total units
		assertEquals("Should correctly check-out units of inventory item when stock is sufficient", inventory.checkOutItem(invItem, checkOutUnitSmall), true);
	}	
	
	@Test
	public void checkOutItemLargeTest() {
		int checkOutUnitLarge = (inventory.getItemStock(invItem) + 2 ); // two units more than current stock
		assertEquals("Should fail to check-out units of inventory item when stock is insufficient", inventory.checkOutItem(invItem, checkOutUnitLarge), false);
	}
	
	@Test
	public void restockTest() {
		inventory.restock();
		assertEquals("Should correctly restock inventory items from the input file", inventory.getItemStock(invItem), invUnit);
	}

	@Test
	public void getItemStockIntTypeTest() {
		boolean thrown = false;
		try {
			mixedTypeInventory.getItemStock(invItem); // should throw exception
		} catch (Exception e) {
			thrown = true;
		}

		assertEquals("Jackson Parser Limitation: Should throw exception when JSON file has any integer value", thrown, true);
	}
	
	@Test
	public void checkOutItemIntTypeTest() {
		boolean thrown = false;
		try {
			mixedTypeInventory.checkOutItem(invItem, 1); // should throw exception
		} catch (Exception e) {
			thrown = true;
		}

		assertEquals("Jackson Parser Limitation: Should throw exception when JSON file has any integer value", thrown, true);
	}	

}
