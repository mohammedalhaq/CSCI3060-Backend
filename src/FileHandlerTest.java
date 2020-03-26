import static org.junit.Assert.*;

import java.io.IOException;

import org.junit.Test;

public class FileHandlerTest {
	
	@Test
	public void createTest1() throws IOException {
		FileHandler fh = new FileHandler("userData.txt", "availableItems.txt");
		fh.create("admin", "AA");
		LogWriter lw = new LogWriter("TestErrorLog.txt");
		lw.write(fh.getErrors());
	}
	
	@Test
	public void createTest2() throws IOException {
		FileHandler fh = new FileHandler("userData.txt", "availableItems.txt");
		fh.create("thisismorethanfifteencharacters", "FS");
	}
	
	@Test
	public void createTest3() throws IOException {
		FileHandler fh = new FileHandler("userData.txt", "availableItems.txt");
		fh.create("newUser", "FS");
		assertEquals(true, fh.getErrors().isEmpty());
	}

	@Test
	public void deleteTest1() throws IOException {
		FileHandler fh = new FileHandler("userData.txt", "availableItems.txt");
		fh.delete("newUser");
		
	}
	
	@Test
	public void deleteTest2() throws IOException {
		FileHandler fh = new FileHandler("userData.txt", "availableItems.txt");
		fh.delete("newUser");
	}
}
