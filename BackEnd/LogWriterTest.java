import static org.junit.Assert.*;

import java.io.File;
import java.io.IOException;
import java.util.Scanner;

import org.junit.Test;

public class LogWriterTest {

	//Test whether actual error log matches expected
	@Test
	public void logWriterTest1() throws IOException {
		LogWriter lw = new LogWriter("ActualErrorLog.txt");
		
		FileHandler fh = new FileHandler("userData.txt", "availableItems.txt");
		fh.endDay();
		
		//Creating various errors
		fh.addcredit("userBS", 3100.00f);
		fh.refund("admin", "userSS", 31.00f);
		fh.delete("newUser");
		fh.create("thisismorethanfifteencharacters", "FS");
		
		lw.write(fh.getErrors());
		Scanner actual = new Scanner(new File("ActualErrorLog.txt"));
		Scanner expected = new Scanner(new File("ExpectedErrorLog.txt"));
		while (actual.hasNextLine()) {
			assertEquals(actual.nextLine(), expected.nextLine());
		}
		actual.close();
		expected.close();
	}

}
