import static org.junit.Assert.*;

import java.io.File;
import java.io.IOException;
import java.util.Scanner;

import org.junit.Test;

public class FileHandlerTest {
	
	@Test
	public void createTest1() throws IOException {
		FileHandler fh = new FileHandler("userData.txt", "availableItems.txt");
		fh.create("admin", "AA");
		assertEquals("ERROR: Username admin already in use.\n", fh.getErrors());
		new File("newUserData.txt").delete();
	}
	
	@Test
	public void createTest2() throws IOException {
		FileHandler fh = new FileHandler("userData.txt", "availableItems.txt");
		fh.create("thisismorethanfifteencharacters", "FS");
		assertEquals("ERROR: Username length must be 15 characters or less\n", fh.getErrors());
	}
	
	@Test
	public void createTest3() throws IOException {
		FileHandler fh = new FileHandler("userData.txt", "availableItems.txt");
		fh.create("newUser", "FS");
		assertTrue(fh.getErrors().isEmpty());
	}

	@Test
	public void deleteTest1() throws IOException {
		FileHandler fh = new FileHandler("userData.txt", "availableItems.txt");
		fh.endDay();
		fh.delete("user01");
		assertTrue(fh.getErrors().isEmpty());
		new File("newUserData.txt").delete();
		new File("newAvailableItems.txt").delete();
	}
	
	@Test
	public void deleteTest2() throws IOException {
		FileHandler fh = new FileHandler("userData.txt", "availableItems.txt");
		fh.endDay();
		fh.delete("newUser");
		assertEquals("ERROR: User does not exist\n", fh.getErrors());
		new File("newUserData.txt").delete();
		new File("newAvailableItems.txt").delete();
	}
	
	@Test
	public void advertiseTest1() throws IOException {
		FileHandler fh = new FileHandler("userData.txt", "availableItems.txt");
		fh.endDay();
		fh.advertise("Beans", "userSS", 45, 25.00f);
		assertTrue(fh.getErrors().isEmpty());
		new File("newUserData.txt").delete();
		new File("newAvailableItems.txt").delete();
	}
	
	@Test
	public void advertiseTest2() throws IOException {
		FileHandler fh = new FileHandler("userData.txt", "availableItems.txt");
		fh.endDay();
		fh.advertise("Beans", "userSS", 45, 1000.00f);
		assertEquals("ERROR: Max price for an item is $999.99\n", fh.getErrors());
		new File("newUserData.txt").delete();
		new File("newAvailableItems.txt").delete();
	}
	
	@Test
	public void advertiseTest3() throws IOException {
		FileHandler fh = new FileHandler("userData.txt", "availableItems.txt");
		fh.endDay();
		fh.advertise("Beans", "userSS", 405, 25.00f);
		assertEquals( "ERROR: Max duration for an item is 100 days\n", fh.getErrors());
		new File("newUserData.txt").delete();
		new File("newAvailableItems.txt").delete();
	}
	
	@Test
	public void advertiseTest4() throws IOException {
		FileHandler fh = new FileHandler("userData.txt", "availableItems.txt");
		fh.endDay();
		fh.advertise("theitemnameisabove25characterssoitwillnotbeaddedtotheitemsfile", "userSS", 45, 25.00f);
		assertEquals("ERROR: Max length for the item name is 25 characters\n", fh.getErrors());
		new File("newUserData.txt").delete();
		new File("newAvailableItems.txt").delete();
	}
	
	@Test
	public void bidTest1() throws IOException {
		FileHandler fh = new FileHandler("userData.txt", "availableItems.txt");
		fh.endDay();
		fh.bid("Shoe", "user01", "admin", 98.00f);
		assertTrue(fh.getErrors().isEmpty());
		new File("newUserData.txt").delete();
		new File("newAvailableItems.txt").delete();
	}
	
	@Test
	public void bidTest2() throws IOException {
		FileHandler fh = new FileHandler("userData.txt", "availableItems.txt");
		fh.endDay();
		fh.bid("Shoe", "userSS", "userFS", 110.00f);
		assertEquals("ERROR: User does not have sufficient credit\n", fh.getErrors());
		new File("newUserData.txt").delete();
	}
	
	@Test
	public void refundTest1() throws IOException {
		FileHandler fh = new FileHandler("userData.txt", "availableItems.txt");
		fh.endDay();
		fh.refund("admin", "userFS", 31.00f);
		assertTrue(fh.getErrors().isEmpty());
		new File("newUserData.txt").delete();
		new File("newAvailableItems.txt").delete();
	}
	
	@Test
	public void refundTest2() throws IOException {
		FileHandler fh = new FileHandler("userData.txt", "availableItems.txt");
		fh.endDay();
		fh.refund("admin", "userSS", 31.00f);
		assertEquals("ERROR: Seller does not have enough to credit refund\n", fh.getErrors());
		new File("newUserData.txt").delete();
		new File("newAvailableItems.txt").delete();
	}
	
	
	@Test
	public void addTest1() throws IOException {
		FileHandler fh = new FileHandler("userData.txt", "availableItems.txt");
		fh.addcredit("userBS", 31.00f);
		assertTrue(fh.getErrors().isEmpty());
		new File("newUserData.txt").delete();
	}
	
	@Test
	public void addTest2() throws IOException {
		FileHandler fh = new FileHandler("userData.txt", "availableItems.txt");
		fh.addcredit("userBS", 3100.00f);
		assertEquals("ERROR: Cannot add more than 1000 credit in one session\n", fh.getErrors());
		new File("newUserData.txt").delete();
	}
	
	@Test
	public void addTest3() throws IOException {
		FileHandler fh = new FileHandler("userData.txt", "availableItems.txt");
		fh.create("MaxCredit", "FS");
		
		for(int i = 0; i<=1001; i++) fh.addcredit("MaxCredit", 999);
		
		assertEquals("ERROR: Maximum credit has been reached\n", fh.getErrors());
		new File("newUserData.txt").delete();
	}
	
	@Test
	public void endDayTest() throws IOException {
		FileHandler fh = new FileHandler("userData.txt", "availableItems.txt");
		fh.endDay();
		
		Scanner actual = new Scanner(new File("newAvailableItems.txt"));
		Scanner expected = new Scanner(new File("newItemsTest.txt"));
		while (actual.hasNextLine()) {
			assertEquals(actual.nextLine(), expected.nextLine());
		}
		
		actual.close();
		expected.close();
	}
	
}
