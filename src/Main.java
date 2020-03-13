import java.io.File; 
import java.io.FileNotFoundException; 
import java.util.Scanner; 

public class Main {

	public static void main(String[] args) {	
		String userData = "userData.txt";
		String availableItems = "availableItems.txt";
		int temp;
		
		FileHandler fileHandler = new FileHandler(userData, availableItems);
		LogWriter logWriter = new LogWriter();
		String error = ""; 
		try {
			File file = new File("dailyTransactions.txt");
			Scanner scanner = new Scanner(file);
			String[] line;
			String transaction;
			while (scanner.hasNextLine()) {
				transaction = scanner.nextLine();
				line = transaction.split("\\s+");
				
				
				temp = Integer.parseInt(line[0]);
				System.out.println(temp);
				
				switch(temp) {
				case 1:
					fileHandler.create(line[1]);
					break;
				case 2:
					fileHandler.delete(line[1]);
					break;
				case 3:
					fileHandler.advertise(line[1], line[2], Integer.parseInt(line[3]), Float.parseFloat(line[4]));
					break;
				case 4:
					fileHandler.bid(line[1], line[2], line[3], Float.parseFloat(line[4]));
					break;
				case 5:
					fileHandler.refund(line[1], line[2], Float.parseFloat(line[3]));
					break;
				case 6:
					fileHandler.addcredit(line[1], Float.parseFloat(line[3]));
					break;
				}

				
			}
			
			
			scanner.close();
			
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		logWriter.write(error);

	}

}
