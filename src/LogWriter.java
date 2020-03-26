import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Scanner;

public class LogWriter {
	String errorFile;
	
	LogWriter(String filename){ 
		this.errorFile = filename;
	}
	
	void write(String error) throws IOException {
		FileOutputStream outFile = new FileOutputStream(this.errorFile);
		byte[] oldUsers = (error).getBytes();
		outFile.write(oldUsers);
		
	}
	
}
