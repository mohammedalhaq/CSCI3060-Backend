import static org.junit.Assert.*;

import java.io.IOException;

import org.junit.Test;

public class FileHandlerTest {
	
	@Test
	public void createTest1() throws IOException {
		FileHandler fh = new FileHandler("userData.txt", "availableItems.txt");
		fh.create("admin", "AA");
		assertEquals("ERROR: Username admin already in use.\n", fh.getErrors());
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
	}
	
	@Test
	public void deleteTest2() throws IOException {
		FileHandler fh = new FileHandler("userData.txt", "availableItems.txt");
		fh.endDay();
		fh.delete("newUser");
		assertEquals("ERROR: User does not exist\n", fh.getErrors());
	}
	
	@Test
	public void advertiseTest1() throws IOException {
		FileHandler fh = new FileHandler("userData.txt", "availableItems.txt");
		fh.endDay();
		fh.advertise("Beans", "userSS", 45, 25.00f);
		assertTrue(fh.getErrors().isEmpty());
	}
	
	@Test
	public void advertiseTest2() throws IOException {
		FileHandler fh = new FileHandler("userData.txt", "availableItems.txt");
		fh.endDay();
		fh.advertise("Beans", "userSS", 45, 1000.00f);
		assertEquals("ERROR: Max price for an item is $999.99\n", fh.getErrors());
	}
	
	@Test
	public void advertiseTest3() throws IOException {
		FileHandler fh = new FileHandler("userData.txt", "availableItems.txt");
		fh.endDay();
		fh.advertise("Beans", "userSS", 405, 25.00f);
		assertEquals( "ERROR: Max duration for an item is 100 days\n", fh.getErrors());
	}
	
	@Test
	public void advertiseTest4() throws IOException {
		FileHandler fh = new FileHandler("userData.txt", "availableItems.txt");
		fh.endDay();
		fh.advertise("theitemnameisabove25characterssoitwillnotbeaddedtotheitemsfile", "userSS", 45, 25.00f);
		assertEquals("ERROR: Max length for the item name is 25 characters\n", fh.getErrors());
	}
	

	
}
