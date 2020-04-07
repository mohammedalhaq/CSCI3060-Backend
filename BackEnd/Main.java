import java.io.File; 
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Scanner; 

public class Main {

	public static void main(String[] args) throws IOException{	
		String userData = "../userData.txt";
		String availableItems = "../availableItems.txt";
		int temp;		//Holds the transaction (01-06)
		
		FileHandler fileHandler = new FileHandler(userData, availableItems);
		LogWriter logWriter = new LogWriter("../ErrorLog.txt");
		fileHandler.endDay();
		File file = new File("../DailyTransactions/MergedDailyTransactions.txt");
		Scanner scanner = new Scanner(file);
		String[] line;
		while (scanner.hasNextLine()) {
			line = scanner.nextLine().split("\\s+");
			temp = Integer.parseInt(line[0]);
				
			switch(temp) {
				case 1:
					fileHandler.create(line[1], line[2]);
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
		
		//Log all errors when daily file done being read
		logWriter.write(fileHandler.getErrors());
	}

}
