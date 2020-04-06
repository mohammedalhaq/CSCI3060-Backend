import static org.junit.Assert.*;

import java.io.File;
import java.io.IOException;
import java.util.Scanner;

import org.junit.Test;

public class FileHandlerTest {
	
	//Test username in use
	@Test
	public void createTest1() throws IOException {
		FileHandler fh = new FileHandler("userData.txt", "availableItems.txt");
		fh.create("admin", "AA");
		assertEquals("ERROR: Username admin already in use.\n", fh.getErrors()); //checks if error messages matches
	}
	
	//Test username length
	@Test
	public void createTest2() throws IOException {
		FileHandler fh = new FileHandler("userData.txt", "availableItems.txt");
		fh.create("thisismorethanfifteencharacters", "FS");
		assertEquals("ERROR: Username length must be 15 characters or less\n", fh.getErrors());
	}
	
	//Test successful add
	@Test
	public void createTest3() throws IOException {
		FileHandler fh = new FileHandler("userData.txt", "availableItems.txt");
		fh.create("newUser", "FS");
		assertTrue(fh.getErrors().isEmpty()); //Checks if there are no errors
	}

	//Test successful delete
	@Test
	public void deleteTest1() throws IOException {
		FileHandler fh = new FileHandler("userData.txt", "availableItems.txt");
		fh.endDay();
		fh.delete("user01");
		assertTrue(fh.getErrors().isEmpty());
	}
	
	//Test non existent user
	@Test
	public void deleteTest2() throws IOException {
		FileHandler fh = new FileHandler("userData.txt", "availableItems.txt");
		fh.endDay();
		fh.delete("newUser");
		assertEquals("ERROR: User does not exist\n", fh.getErrors());
	}
	
	//Test successful advertisement
	@Test
	public void advertiseTest1() throws IOException {
		FileHandler fh = new FileHandler("userData.txt", "availableItems.txt");
		fh.endDay();
		fh.advertise("Beans", "userSS", 45, 25.00f);
		assertTrue(fh.getErrors().isEmpty());
	}
	
	//Test max price error
	@Test
	public void advertiseTest2() throws IOException {
		FileHandler fh = new FileHandler("userData.txt", "availableItems.txt");
		fh.endDay();
		fh.advertise("Beans", "userSS", 45, 1000.00f);
		assertEquals("ERROR: Max price for an item is $999.99\n", fh.getErrors());
	}
	
	//Test max duration error
	@Test
	public void advertiseTest3() throws IOException {
		FileHandler fh = new FileHandler("userData.txt", "availableItems.txt");
		fh.endDay();
		fh.advertise("Beans", "userSS", 405, 25.00f);
		assertEquals( "ERROR: Max duration for an item is 100 days\n", fh.getErrors());
	}
	
	//Test max item name error
	@Test
	public void advertiseTest4() throws IOException {
		FileHandler fh = new FileHandler("userData.txt", "availableItems.txt");
		fh.endDay();
		fh.advertise("theitemnameisabove25characterssoitwillnotbeaddedtotheitemsfile", "userSS", 45, 25.00f);
		assertEquals("ERROR: Max length for the item name is 25 characters\n", fh.getErrors());
	}
	
	//Test successful bid
	@Test
	public void bidTest1() throws IOException {
		FileHandler fh = new FileHandler("userData.txt", "availableItems.txt");
		fh.endDay();
		fh.bid("Shoe", "userSS", "admin", 98.00f);
		assertTrue(fh.getErrors().isEmpty());
	}
	
	//Test bidding over user credit
	@Test
	public void bidTest2() throws IOException {
		FileHandler fh = new FileHandler("userData.txt", "availableItems.txt");
		fh.endDay();
		fh.bid("Shoe", "userSS", "userFS", 110.00f);
		assertEquals("ERROR: User does not have sufficient credit\n", fh.getErrors());
	}
	
	//Test bidding on own item
	@Test
	public void bidTest3() throws IOException {
		FileHandler fh = new FileHandler("userData.txt", "availableItems.txt");
		fh.endDay();
		fh.bid("Shoe", "user01", "user01", 110.00f);
		assertEquals("ERROR: Cannot bid on your own item\n", fh.getErrors());
	}
	
	//Decision coverage of the the condition 	
	//if (temp[0].equals(itemName) && temp[1].equals(seller)) { 
	@Test
	public void bidTest4() throws IOException {
		FileHandler fh = new FileHandler("userData.txt", "availableItems.txt");
		fh.endDay();
		fh.bid("Shoe", "userSS", "user01", 110.00f);
		assertTrue(fh.getErrors().isEmpty());
	}
		
	
	//Decision coverage of the the condition 	
	//if (temp[0].equals(oldBidder) && !seller.equals(oldBidder)){
	@Test
	public void bidTest5() throws IOException {
		FileHandler fh = new FileHandler("userData.txt", "availableItems.txt");
		fh.endDay();
		fh.bid("Shoe", "user01", "admin", 110.00f);
		assertTrue(fh.getErrors().isEmpty());
	}
		
	//Loop coverage last iteration	
	@Test
	public void bidTest6() throws IOException {
		FileHandler fh = new FileHandler("userData.txt", "availableItems.txt");
		fh.endDay();
		fh.bid("Shoe", "user01", "admin", 110.00f);
		assertTrue(fh.getErrors().isEmpty());
	}
	
	//Loop coverage last iteration	
	@Test
	public void bidTest7() throws IOException {
		FileHandler fh = new FileHandler("userData.txt", "availableItems.txt");
		fh.endDay();
		fh.bid("FakeItem", "user01", "admin", 110.00f);
		assertEquals("ERROR: Item does not exist\n", fh.getErrors());
	}
	
	//Test non existent user
	@Test
	public void bidTest8() throws IOException {
		FileHandler fh = new FileHandler("userData.txt", "availableItems.txt");
		fh.endDay();
		fh.bid("Shoe", "user01", "ffakeuser", 110.00f);
		assertEquals("User ffakeuser does not exist\n", fh.getErrors());
	}
	
	//Test successful refund
	@Test
	public void refundTest1() throws IOException {
		FileHandler fh = new FileHandler("userData.txt", "availableItems.txt");
		fh.endDay();
		fh.refund("admin", "userFS", 31.00f);
		assertTrue(fh.getErrors().isEmpty());
	}
	
	//Test refunding more that credit
	@Test
	public void refundTest2() throws IOException {
		FileHandler fh = new FileHandler("userData.txt", "availableItems.txt");
		fh.endDay();
		fh.refund("admin", "userSS", 31.00f);
		assertEquals("ERROR: Seller does not have enough to credit refund\n", fh.getErrors());
	}
	
	@Test
	public void refundTest3() throws IOException {
		FileHandler fh = new FileHandler("userData.txt", "availableItems.txt");
		fh.endDay();
		fh.refund("admin", "ffakeuser", 31.00f);
		assertEquals("ERROR: One or both of the users do not exist\n", fh.getErrors());
	}
	
	//Testing successful addcredit
	@Test
	public void addTest1() throws IOException {
		FileHandler fh = new FileHandler("userData.txt", "availableItems.txt");
		fh.addcredit("userBS", 31.00f);
		assertTrue(fh.getErrors().isEmpty());
	}
	
	//Testing adding more than 1k
	@Test
	public void addTest2() throws IOException {
		FileHandler fh = new FileHandler("userData.txt", "availableItems.txt");
		fh.addcredit("userBS", 3100.00f);
		assertEquals("ERROR: Cannot add more than 1000 credit in one session\n", fh.getErrors());
	}
	
	//Testing adding upto max credit
	@Test
	public void addTest3() throws IOException {
		FileHandler fh = new FileHandler("userData.txt", "availableItems.txt");
		fh.create("MaxCredit", "FS");
		
		for(int i = 0; i<=1001; i++) fh.addcredit("MaxCredit", 999);
		
		assertEquals("ERROR: Maximum credit has been reached\n", fh.getErrors());
	}
	
	//Test whether the day decrements and newUserData file exists
	@Test
	public void endDayTest() throws IOException {
		FileHandler fh = new FileHandler("userData.txt", "availableItems.txt");
		fh.endDay();
		
		Scanner actual = new Scanner(new File("newAvailableItems.txt"));
		Scanner expected = new Scanner(new File("expectedAvailableItems.txt"));
		while (actual.hasNextLine()) {
			assertEquals(actual.nextLine(), expected.nextLine());
		}
		
		assertTrue(new File("newUserData.txt").exists());
		
		actual.close();
		expected.close();
	}
	
}
