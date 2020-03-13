import java.util.Scanner;

public class FileHandler {
	private String userData;
	private String availableItems;
	private User user;
	
	FileHandler(String userData, String availableItems){ 
		this.userData = userData;
		this.availableItems = availableItems;
	}
	
	private User getUser(String username) {
		Scanner scanner = new Scanner(userData);
		
		while(scanner.hasNextLine()) {
			
		}
			
		
		scanner.close();
		return user;
	}
	
	
	void create(String username) {
		
	}
	
	void delete(String username) {
		
	}
	
	void advertise(String itemName, String username, int duration, float minBid) {
		
	}
	
	void bid(String itemName, String seller, String currentUser, float amount) {
		
	}
	
	void refund(String buyer, String seller, float credit) {
		
	}
	
	void addcredit(String username, float credit) {
		
	}
}
