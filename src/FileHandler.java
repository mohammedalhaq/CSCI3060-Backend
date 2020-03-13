import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Scanner;


public class FileHandler { 
	//Old Files
	private String userData;
	private String availableItems;
	//New Files
	private String newUserData;
	private String newAvailableItems;		
	
	private String errors;					//Variable to hold all the error messages to later write
	private User user;
	
	FileHandler(String userData, String availableItems){ 
		this.userData = userData;
		this.availableItems = availableItems;
		this.newUserData = "newUserData.txt";
		this.newAvailableItems = "newAvailableItems.txt";
		this.errors = "";
	}
	
	private User getUser(String username) throws IOException{
		File file;
		if (new File(this.newUserData).exists()) {
			file = new File(this.newUserData);
		} else {
			file = new File(this.userData);			
		}
		
		Scanner scanner = new Scanner(file);
		String[] line;
		while(scanner.hasNextLine()) {
			line = scanner.nextLine().split("\\s+");
			if (line[0].equals(username)) {
				scanner.close();
				return new User(username, line[1], Float.parseFloat(line[2]));
			}
		}
			
		scanner.close();
		return new User();
	}
	
	//Function to update the new UserData file
	void create(String username, String type) throws IOException {	
		//check is user exists
		if (getUser(username).getUsername() != null) {
			this.errors += "ERROR: Username " + username + " already in use.\n";
		} else {
			FileOutputStream outFile; 
			//check if file exists to decide whether to append new user or create new file
			if (!new File(this.newUserData).exists()) {	
				outFile = new FileOutputStream(this.newUserData);
				File file = new File(userData);
				Scanner scanner = new Scanner(file);
		
				while (scanner.hasNextLine()) {
					byte[] oldUsers = (scanner.nextLine() + "\n").getBytes();
					outFile.write(oldUsers);
				}
				scanner.close();

			} else {
				outFile = new FileOutputStream(this.newUserData, true);
			}
		
			byte[] newUser = (username + " " +  type + " 00.00" + "\n").getBytes();
			outFile.write(newUser);
			outFile.close();
		}
	}
	
	//Function to delete a user
	void delete(String username) throws IOException {
		FileOutputStream userFile = new FileOutputStream("tempusers.txt"); //writes a temp file if a new user data exists
		File file;
		
		if (new File(this.newUserData).exists()) {
			file = new File(this.newUserData);
			
		} else {
			file = new File(this.userData);
		}

		Scanner userScanner = new Scanner(file);
		String line;
		while (userScanner.hasNextLine()) {
			line = userScanner.nextLine();
			String[] temp = line.split("\\s+");
			
			if (!temp[0].equals(username)) {
				byte[] oldUsers = (line + "\n").getBytes();
				userFile.write(oldUsers);
			}
		}
		userFile.close();
		userScanner.close();
		new File(this.newUserData).delete();
		new File("tempusers.txt").renameTo(new File(this.newUserData));
		
		
		
		FileOutputStream itemsFile = new FileOutputStream(this.newAvailableItems);
		if (new File(this.availableItems).exists()) {
			file = new File(this.newAvailableItems);
		} else {
			file = new File(this.availableItems);
		}
		
		
		Scanner itemsScanner = new Scanner(file);
		while (itemsScanner.hasNextLine()) {
			line = itemsScanner.nextLine();
			String[] temp = line.split("\\s+");
			if (!temp[1].equals(username)) {
				byte[] oldUsers = (line + "\n").getBytes();
				itemsFile.write(oldUsers);
			}
		}
		itemsFile.close();
		itemsScanner.close();
	}
	
	//Creates a new listing for an item in newItems file
	void advertise(String itemName, String username, int duration, float minBid) throws IOException {
		FileOutputStream outFile;
		//check if file exists to decide whether to append new user or create new file
		if (!new File(this.newAvailableItems).exists()) {	
			outFile = new FileOutputStream(this.newAvailableItems);
			File file = new File(this.availableItems);
			Scanner scanner = new Scanner(file);
			while (scanner.hasNextLine()) {
				String line = scanner.nextLine();
				System.out.println(line);
				byte[] oldItems = (line + "\n").getBytes();
				
				outFile.write(oldItems);
			}
			scanner.close();
		} else {
			outFile = new FileOutputStream(this.newAvailableItems, true);
		}
	
		byte[] newItem = (itemName + " " + username + " " +  username + " " + duration + " " + minBid + "\n").getBytes();
		outFile.write(newItem);
		outFile.close();
	}
	
	void bid(String itemName, String seller, String currentUser, float amount) {
		
	}
	
	void refund(String buyer, String seller, float credit) {
		
	}
	
	void addcredit(String username, float credit) {
		
	}
	
	//Gets the errors for the main to use to send to LogWriter to write
	String getErrors() {
		return this.errors;
	}
	
	
}
