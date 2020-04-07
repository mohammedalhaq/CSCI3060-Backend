/*
This class handles all the transactions and their communication with the user
It tells FileIO what to look for/what to change
All errors directly relating to the user's input are handled here
*/
#include "user.cpp"  
#include <iostream>
#include "FileIO.h"
#include <string>
#include <fstream>
#include "auction.cpp"

class Transactions {
	//Private variables
	std::string log;	//Log file to be written to daily transaction
	FileIO file;		//To call read/write function
	User user;			//To get user properties
	Auction auction;	//Auction to be used in future for bidding/auctionning

public:
	Transactions() {
		//Initialzies the daily transaction log as blank
		log = "";
	}

	void setIteration(std::string iteration) {
		file.setIteration(iteration);
	}

	//sets userdata file to read as the argv[2]
	void setUserData(std::string currentUsers) {
		file.setUserData(currentUsers);
	}

	//sets available items file to read as the argv[3]
	void setAvailableItems(std::string availables) {
		file.setAvailableItems(availables);
	}

	/* User requests the login transaction	*/
	bool login() {

		std::string input;
		std::cout << "Enter your username:\n";
		std::cin >> input;

		bool success = file.logUserIn(input);
		if (success) {
			std::cout << "Welcome back, " << input << "\n";
			user.setUsername(input);
			user.setType(file.getAccountType(input));
			user.setCredit(file.getUserCredit(input));
		}
		else {
			std::cout << "Login Error: No such user\nTry logging in again\n";

			//login();
		}

		return success;
	}

	//User requests the logout transaction. This function is complete
	void logout() {
		std::cout << user.getUsername() << " logged out\n\n";

		//Appends to transaction to the log 
		log += "00 " + user.getUsername() + " " + user.getType() + " " + std::to_string(user.getCredit()) + "\n";
		file.generateDailyTransaction(log);		//Creates the file with all transactions executed 
	}

	/*
	User requests the create account transaction
	*/
	void create() {
		std::string accountType;
		std::string username;

		//Requires admin privileges
		if (user.getType() == "AA") {
			std::cout << "Enter the account type you wish to create (FS/BS/SS/AA):\n";
			std::cin >> accountType;

			if (accountType != "AA" && accountType != "BS" && accountType != "SS" && accountType != "FS") {
				std::cout << "Create Error: Invalid Account Type\n";
				create();
			}

			std::cout << "Please enter the requested username:\n";
			std::cin >> username;

			if (username.length() > 15) {
				std::cout << "Create Error: Username exceeds 15 character limit.\n";
			}

			std::cout << accountType << " account " << username << " created\n";

			//Appends the transaction to the log
			log += "01 " + username + " " + accountType + " 00.00\n";

		}
		else {
			std::cout << "Create Error: Insufficient permission\n";
			std::cin >> accountType;
			std::cin >> username;
		}
	}

	/*
	User requests the delete transaction
	*/
	void deleteUser() {
		//Requires admin privileges
		if (user.getType() == "AA") {

			std::string username;

			std::cout << "Please enter the username of the account to delete:\n";
			std::cin >> username;

			if (username == user.getUsername())
			{
				std::cout << "Delete Error: Cannot delete current user\n";
			}
			else {
				if (!file.logUserIn(username)) {
					std::cout << "Delete Error: User does not exist\n";
				}
				else {
					std::cout << username << " successfully deleted\n";
				}
				log += "02 " + username + " " + file.getAccountType(username) + " " + std::to_string(file.getUserCredit(username)) + "\n";
			}
		}
		else {
			std::cout << "Delete Error: Insufficient permissions\n";
		}
	}

	/*
	User requests the advertise transaction
	*/
	void advertise() {
		std::string itemName;
		float minBid;
		int duration;
		std::string temp;

		//Cannot be buy standard account
		if (user.getType() != "BS") {


			std::cout << "Please enter the item name to advertise:\n";
			std::cin >> itemName;

			if (itemName.length() > 25) {
				std::cout << "Advertise Error: Item name exceeds character limit\n";
			}
			else {

				std::cout << "Please enter the minimum bid for " << itemName << ":\n";
				std::cin >> minBid;

				if (minBid > 999.99) {
					std::cout << "Advertise Error: Item price has exceeded the limit\n";
					std::cin >> duration;
				}
				else {

					std::cout << "Please enter the duration of the auction:\n";
					std::cin >> duration;
					if (!std::cin.good() || duration > 100) {
						std::cin.clear();
						if (duration > 100) {
							std::cout << "Advertise Error: Auction length must be less than 101 days\nTry again:\n";
						}
						else {
							std::cout << "Advertise Error: Auction length must be an integer below 101\n";
							std::cin >> temp;
						}
					}
					else {
						std::cout << itemName << " was successfully posted. Min Bid: $" << minBid << " Duration: " << duration << "d\n";

						log += "03 " + itemName + " " + user.getUsername() + " " + std::to_string(duration) + " " + std::to_string(minBid) + "\n";
					}
				}
			}

		}
		else {
			std::cout << "Advertise Error: Cannot advertise from a buy-standard account\n";
			std::cin >> itemName;
			std::cin >> minBid;
			std::cin >> duration;
		}

	}

	/*
	User requests the bid transaction
	*/
	void bid() {
		//Must not be sell standard accounts
		if (user.getType() != "SS") {
			bool admin = user.getType() == "AA";
			float multiplyFactor = admin ? 1.00 : 1.05;

			std::string username;
			std::string itemName;
			float bid;

			std::cout << "Please enter the user with the item you plan to bid on:\n";
			std::cin >> username;

			if (username == user.getUsername() || !file.logUserIn(username)) {
				if (username == user.getUsername()) std::cout << "Bid Error: Cannot bid on your own item\n";
				if (!file.logUserIn(username)) std::cout << "Bid Error: User does not exist\n";
			}
			else {
				std::cout << "Please enter the item of " << username << " you wish to bid on" << ":\n";
				std::cin >> itemName;

				if (file.itemExists(username, itemName)) {
					float highestBid = file.getCurrentBid(username, itemName);
					std::cout << "Current bid for " << itemName << " is $" << highestBid << ".\n";

					std::cout << "Please enter your bid:\n";
					std::cin >> bid;

					if (bid <= highestBid * multiplyFactor) {
						std::cout << "Bid Error: Bid must be";
						if (!admin) std::cout << " at least 5% ";
						std::cout << "higher than the previous bid.\n";
					}
					else {
						std::cout << "Successfully bid $" << (float)bid << " on " << itemName << "\n";

						log += "04 " + itemName + " " + username + " " + user.getUsername() + " " + std::to_string(bid) + "\n";
					}
				}
				else {
					std::cout << "Bid Error: " << username << " has no item called " << itemName << "\n";
				}
			}
		}
		else {
			std::cout << "Bid Error: Cannot complete transaction from standard-sell account\n";
		}

	}

	/*
	User requests the refund transaction
	*/
	void refund() {
		//Requires admin privileges
		if (user.getType() == "AA") {

			std::string buyer;		//Buyer's username for user to input into
			std::string seller;		//Seller's username for user to input into
			float credit;			//Credit for the user to input into

			std::cout << "Please enter the buyer's username to refund:\n";
			std::cin >> buyer;
			if (!file.logUserIn(buyer)) {
				std::cout << "Refund Error: Buyer does not exist\n";
			}
			else {

				std::cout << "Please enter the seller's username:\n";
				std::cin >> seller;
				if (!file.logUserIn(seller)) {
					std::cout << "Refund Error: Seller does not exist\n";
				}
				else {

					std::cout << "How much credit will be refunded?\n";
					std::cin >> credit;

					std::cout << buyer << " successfully refunded $" << credit << " from " << seller << "\n";

					log += "05 " + buyer + " " + seller + " " + std::to_string(credit) + "\n";
				}
			}

		}
		else {
			std::cout << "Refund Error: Insufficient permission\n";

		}

	}

	//User requests the addcredit transaction
	void addCredit() {
		std::string username;
		float credit;
		std::string temp;

		if (user.getType() == "AA") {
			std::cout << "Please enter the username of the account to add credit to:\n";
			std::cin >> username;
			if (file.logUserIn(username)) {
				std::cout << "Please enter the amount to add:\n";
				std::cin >> credit;
				if (!std::cin.good() || credit > 1000) {
					if (!std::cin.good()) {
						std::cin.clear();
						std::cout << "AddCredit Error: Must add a numerical value\n";
						std::cin >> temp;
					}
					if (credit > 1000) std::cout << "AddCredit Error: Cannot add over $1000.00 in a session\n";
				}
				else {
					std::cout << "$" << credit << " of credit was added successfully to " << username << "\n";
					log += "06 " + username + " " + user.getType() + " " + std::to_string(credit) + "\n";
				}
			}
			else {
				std::cout << "AddCredit Error: username " << username << " does not exist\n";
			}


		}
		else {
			username = user.getUsername();
			std::cout << "Please enter the amount to add:\n";
			std::cin >> credit;
			if (!std::cin.good() || credit > 1000) {
				std::cin.clear();
				if (!std::cin.good()) std::cout << "AddCredit Error: Must add a numerical value\n";
				if (credit > 1000) std::cout << "AddCredit Error: Cannot add over $1000.00 in a session\n";
			}
			else {

				std::cout << "$" << credit << " of credit was added successfully to " << username << "\n";


				log += "06 " + username + " " + user.getType() + " " + std::to_string(credit) + "\n";
			}
		}

	}

	//Function to exit/logout if the user ever puts in the command quit or exit
	//May not be necessary
	bool exitOrQuit(std::string input) {
		return (input == "exit" || input == "quit");
	}
};

