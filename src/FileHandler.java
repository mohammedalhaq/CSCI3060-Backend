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
	
	FileHandler(String userData, String availableItems){ 
		this.userData = userData;
		this.availableItems = availableItems;
		this.newUserData = "newUserData.txt";
		this.newAvailableItems = "newAvailableItems.txt";
		this.errors = "";
	}
	
	
	//Function to get a User object with all their data
	private User getUser(String username) throws IOException{
		File file;
		
		//Checks whether it should look through new file or old
		if (new File(this.newUserData).exists()) {
			file = new File(this.newUserData);
		} else {
			file = new File(this.userData);			
		}
		
		Scanner scanner = new Scanner(file);
		String[] line;
		while(scanner.hasNextLine()) {
			line = scanner.nextLine().split("\\s+"); //Splits daily transaction into the words
			if (line[0].equals(username)) {
				scanner.close();
				return new User(username, line[1], Float.parseFloat(line[2])); //returns the user found
			}
		}
			
		scanner.close();
		return new User(); //returns a null user if nothing found
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
					byte[] oldUsers = (scanner.nextLine() + "\n").getBytes(); //writes all the old users to the new file
					outFile.write(oldUsers);
				}
				scanner.close();

			} else {
				outFile = new FileOutputStream(this.newUserData, true); //appends if file already exists
			}
		
			//creates the new user
			byte[] newUser = (username + " " +  type + " 00.00" + "\n").getBytes(); 
			outFile.write(newUser);
			outFile.close();
		}
	}
	
	//Function to delete a user
	void delete(String username) throws IOException {
		FileOutputStream userFile = new FileOutputStream("tempusers.txt"); //writes a temp file if a new user data exists
		File file;
		
		//Checks is newuser file exists to determine which file to read
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
			
			//Copies all users to temp file except the one to delete
			if (!temp[0].equals(username)) {
				byte[] oldUsers = (line + "\n").getBytes();
				userFile.write(oldUsers);
			}
		}
		userFile.close();
		userScanner.close();
		
		//deletes old newUserData file and renames temp file to newUserData
		new File(this.newUserData).delete();
		new File("tempusers.txt").renameTo(new File(this.newUserData));
		
		
		//temp items file to avoid opening the same file twice at once
		FileOutputStream itemsFile = new FileOutputStream("tempItems.txt");
		if (new File(this.newAvailableItems).exists()) {
			file = new File(this.newAvailableItems);
		} else {
			file = new File(this.availableItems);
		}
		
		
		Scanner itemsScanner = new Scanner(file);
		while (itemsScanner.hasNextLine()) {
			line = itemsScanner.nextLine();
			String[] temp = line.split("\\s+");
			
			//Copies all auctions to temp file except the one to delete
			if (!temp[1].equals(username)) {
				byte[] oldUsers = (line + "\n").getBytes();
				itemsFile.write(oldUsers);
			}
		}
		itemsFile.close();
		itemsScanner.close();
		
		
		//replaces old new items file with temp
		new File(this.newAvailableItems).delete();
		new File("tempItems.txt").renameTo(new File(this.newAvailableItems));
	}
	
	//Creates a new listing for an item in newItems file
	void advertise(String itemName, String username, int duration, float minBid) throws IOException {
		FileOutputStream outFile;
		//check if file exists to decide whether to append new user or create new file
		if (!new File(this.newAvailableItems).exists()) {	
			//Copies old data into new file
			outFile = new FileOutputStream(this.newAvailableItems);
			File file = new File(this.availableItems);
			Scanner scanner = new Scanner(file);
			while (scanner.hasNextLine()) {
				byte[] oldItems = (scanner.nextLine() + "\n").getBytes();
				
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
	
	void bid(String itemName, String seller, String currentUser, float amount) throws IOException {
		FileOutputStream outFile = new FileOutputStream("tempItems.txt");
		File file;
		//check if file exists to decide whether to append new user or create new file
		if (new File(this.newAvailableItems).exists()) {	
			file = new File(this.newAvailableItems);
		} else {
			file = new File(this.availableItems);
		}
	
		Scanner scanner = new Scanner(file);
		String line;
		while (scanner.hasNextLine()) {
			line = scanner.nextLine();
			String temp[] =  line.split("\\s+");
			byte[] item;
			
			//finds item then sets new price
			if (temp[0].equals(itemName) && temp[1].equals(seller)) { 
				item = (itemName + " " + seller + " " + currentUser + " " + amount + "\n").getBytes();		
			} else { //copy if item isnt one requested
				item = (line + "\n").getBytes();		
			}
			outFile.write(item);
		}
		scanner.close();
		outFile.close();
		
		
		//replaces old new items file with the temp
		new File(this.newAvailableItems).delete();
		new File("tempItems.txt").renameTo(new File(this.newAvailableItems));
	}
	
	
	//Function to handle return transactions
	void refund(String buyer, String seller, float credit) throws IOException {
		
	}
	
	
	//Function to handle add credit transasctions
	void addcredit(String username, float credit) throws IOException {
		
	}
	
	//Gets the errors for the main to use to send to LogWriter to write
	String getErrors() {
		return this.errors;
	}
	
	//Function to end the day and decrement duration of auctions
	void endDay() {
		
	}
	
	
}
