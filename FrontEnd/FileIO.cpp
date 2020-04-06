/*
Definition of header file to handle all read/write to the files:
dailyTransactions.txt
userData.txt
availableItems.txt
*/

#include "FileIO.h"
#include <iostream>
#include <fstream>
#include <sstream>
#include "Transactions.cpp"

std::string transactionsFile;
std::string  userData;
std::string  availableItems;

//Default empty constructor for fileout, used when defined to empty.
FileIO::FileIO() {

	
	userData = "../../userData.txt";	//userData - name of current user accounts
	availableItems = "../../availableItems.txt";	//availableItems - name of items up for auction

}

void FileIO::setIteration(std::string iteration)
{
	transactionsFile = "../../DailyTransactions/dailyTransaction"+iteration+".txt"; //transactionsFile - name of daily transaction summary
}

//sets userdata file to read as the argv[2]
void FileIO::setUserData(std::string currentUsers) {
	userData = currentUsers;
}

//sets available items file to read as the argv[3]
void FileIO::setAvailableItems(std::string availables) {
	availableItems = availables;
}

//function to read current user accounts file and use it to check if user exist and log them in
//params: username of user
bool FileIO::logUserIn(std::string username)
{
	std::ifstream file;
	file.open(userData);
	std::string name;
	std::string type;
	float credit;

	//Reads in userdata file to match username
	while (file >> name >> type >> credit) {
		if (username == name) {
			file.close();
			return true;
		}

	}
	file.close();
	return false;
}

//function to read current user accounts file and use it to check the user account types 
//params: username of user
std::string FileIO::getAccountType(std::string username) {
	std::ifstream file;
	std::string line;
	file.open(userData);

	std::string name;
	std::string type;
	float credit;

	//Reads in userdata file to match username and get type
	while (file >> name >> type >> credit) {
		if (username == name) {
			file.close();
			return type;
		}

	}
	file.close();
	return "";
}

//function to read user accounts file to view user's credit 
//params: username of user
float FileIO::getUserCredit(std::string username)
{
	std::ifstream file;
	std::string line;
	file.open(userData);

	std::string name;
	std::string type;
	float credit;

	//Reads in userdata file to match username and get credit
	while (file >> name >> type >> credit) {
		if (username == name) {
			file.close();
			return credit;
		}

	}
	file.close();
	return false;
}

//function to read userData and update the credit of the user
//params: username of user, newValue of credity
void FileIO::setUserCredit(std::string username, float newCredit) {
	std::ofstream tempFile;
	tempFile.open("tempUser.txt");

	std::ifstream userFile;
	userFile.open(userData);

	std::string name;
	std::string type;
	float credit;

	//Reads in userdata file to match username and find credit to update
	while (userFile >> name >> type >> credit) {
		if (name == username) {
			tempFile << name << " " << type << " " << newCredit << "\n"; //rewrites file with new credit
		}
		else {
			tempFile << name << " " << type << " " << credit << "\n"; //rewrites file with same credit
		}
	}
	tempFile.close();
	userFile.close();

	int err = remove(userData.c_str()); //deletes old userdata file
	err = rename("tempUser.txt", userData.c_str());	//renames temp file to userData

}

//function to read current user accounts file and use it to add new user in create() function in Transactions.cpp
//params: username of user, type of user
bool FileIO::createNewUser(std::string username, std::string type)
{
	std::ifstream file;
	std::string line;
	file.open(userData);

	std::string name;
	std::string tempType;
	float credit;

	//reads in userdata file check if user already exists
	while (file >> name >> tempType >> credit) {
		if (username == name) {
			file.close();
			break;
		}
	}
	file.close();

	std::ofstream writeUser;
	writeUser.open(userData, std::ios::app);
	if (username != name) {
		writeUser << username << " " << type << " 0.0\n"; //appends new user to file
		return true;
	}
	return false;
}

// function to read current user accounts file and use it to delete current user in deleteUser() function in Transactions.cpp
//params: username of user
bool FileIO::deleteUser(std::string username)
{
	bool found = false; //bool to mark successful delete
	std::ofstream tempFile;
	tempFile.open("temp.txt");

	std::string itemName;
	std::string tempSeller;
	std::string tempBuyer;
	int days;
	float bid;

	//Goes through items file to delete all user listings
	std::ifstream items;
	items.open(availableItems);
	while (items >> itemName >> tempSeller >> tempBuyer >> days >> bid) {
		//Writes all other listings to a temp file
		if (tempSeller != username) {
			tempFile << itemName << " " << tempSeller << " " << tempBuyer << " " << days << " " << bid << "\n";
		}
		else {
			found = true;
		}
	}

	tempFile.close();
	items.close();


	//deletes original available items file
	int err = remove(availableItems.c_str());
	err = rename("temp.txt", availableItems.c_str()); //renames temp file to available items
	tempFile.open("temp.txt");

	std::ifstream userFile;
	userFile.open(userData);

	std::string name;
	std::string type;
	float credit;

	//parse userfile to delete user
	while (userFile >> name >> type >> credit) {
		if (name != username) {
			tempFile << name << " " << type << " " << credit << "\n"; //writes all users besides the selected
		}
		else {
			found = true;
		}
	}

	tempFile.close();
	userFile.close();

	err = remove(userData.c_str());
	err = rename("temp.txt", userData.c_str());

	return found;
}

//Writes advertisement to available items
void FileIO::createAdvert(std::string username, std::string itemName, float minBid, int duration)
{
	std::ofstream file;
	file.open(availableItems, std::ios::app);
	file << itemName << " " << username << " " << username << " " << duration << " " << minBid << "\n";
	file.close();
}

//Finds current bid on an item in available items
float FileIO::getCurrentBid(std::string username, std::string itemName) {
	std::ifstream file;

	std::string item;
	std::string seller;
	std::string buyer;
	int duration;
	float minBid;

	file.open(availableItems);

	//parse available items
	while (file >> item >> seller >> buyer >> duration >> minBid) {
		if (username == seller && itemName == item) {
			return minBid;
		}
	}


	return 0.0f;
}

//sets new bid on available items
void FileIO::setBid(std::string seller, std::string item, float bid, std::string buyer) {
	float buyerCred = getUserCredit(buyer);
	//Checks if buyer has enough money
	if (buyerCred < bid) {
		std::cout << "Bid Error: Insufficient Funds\n";
	}

	std::ofstream tempFile;
	tempFile.open("temp.txt");

	std::ifstream itemsFile;
	itemsFile.open(availableItems);

	std::string tempItem;
	std::string tempSeller;
	std::string tempBuyer;
	int duration;
	float minBid;

	//parses item file
	while (itemsFile >> tempItem >> tempSeller >> tempBuyer >> duration >> minBid) {
		if (tempSeller == seller && tempItem == item) {
			tempFile << tempItem << " " << tempSeller << " " << buyer << " " << duration << " " << bid << "\n";
			setUserCredit(buyer, buyerCred - bid); //upodates users money

		}
		else {
			tempFile << tempItem << " " << tempSeller << " " << tempBuyer << " " << duration << " " << minBid << "\n";
		}
	}
	tempFile.close();
	itemsFile.close();

	//removes original available items file
	int err = remove(availableItems.c_str());
	err = rename("temp.txt", availableItems.c_str()); //sets temp file as available items

}

bool FileIO::itemExists(std::string username, std::string itemName) {
	std::ifstream itemsFile;
	itemsFile.open(availableItems);

	std::string tempItem;
	std::string tempSeller;
	std::string tempBuyer;
	int duration;
	float minBid;

	//parses item file
	while (itemsFile >> tempItem >> tempSeller >> tempBuyer >> duration >> minBid) {
		if (tempSeller == username && tempItem == itemName) {
			itemsFile.close();
			return true;
		}
	}
	if (itemsFile.is_open()) itemsFile.close();
	return false;
}

//Function to handle refunding user
void FileIO::refundUser(std::string buyer, std::string seller, float credit)
{
	float buyerCredit = getUserCredit(buyer);
	float sellerCredit = getUserCredit(seller);
	float tempBuyer = buyerCredit + credit;
	float tempSeller = sellerCredit - credit;

	setUserCredit(buyer, tempBuyer);
	setUserCredit(seller, tempSeller);

}

//function to write daily transaction log
//params: log - dailyTransactionLog
void FileIO::generateDailyTransaction(std::string log)
{
	std::ofstream file;
	file.open(transactionsFile);
	file << log;
	file.close();
}
