/*
Main program to run
Handles the main functionality and user input
Errors are currently not handled specifically
*/
#include <iostream>
#include <fstream>
#include "FileIO.h"
#include "Transactions.cpp"
#include "main.h"


//argv[1] userData file, argv[2] availableitems
int main(int argc, char** argv) {
	Transactions transaction;
	std::string command;
	transaction.setIteration(argv[1]);

	if (argc > 1) {
		transaction.setUserData(argv[2]);
 		transaction.setAvailableItems(argv[3]);
	}

	std::cout << "Welcome to eAuction!\n";

	//Infinitely loops until the user types exit or quit
	while (command != "exit" || command != "quit") {
		std::cout << "What will you be doing today?\n"; //Ask user to input ask user to input command
		std::cin >> command;

		//Waits on user to login before accpeting other command options
		if (command == "login") {
			if (transaction.login()) {
				while (true) {
					std::cout << "====Main Menu====\n";
					std::cout << "What will you be doing today?\n"; //Ask user to input ask user to input command
					std::cin >> command;

					if (command == "exit" || command == "quit") {
						break;
					}
					else if (command == "login") { //login command for user will result in error
						std::string tempuser;
						std::cin >> tempuser;
						std::cout << "Login Error: Already logged in\n";
					}
					else if (command == "logout") { //logout command for user
						transaction.logout();
						break;
					}
					else if (command == "create") { //if user inputs create command
						transaction.create();
					}
					else if (command == "delete") { //if user inputs delete command
						transaction.deleteUser();
					}
					else if (command == "advertise") { //if user inputs advertise command.
						transaction.advertise();
					}
					else if (command == "bid") { //if user inputs bid command
						transaction.bid();
					}
					else if (command == "refund") { //if user inputs refund command
						transaction.refund();
					}
					else if (command == "addcredit") { //if user inputs addcredit command
						transaction.addCredit();
					}
					else { //if any random input, it is not recognized.
						std::cout << "Unrecognized command try again:\n";

					}
				}
			}
			if (command == "logout") {
				break;
			}

		}
		else if (command == "logout") {
			std::cout << "Logout Error: Not logged in\n";
		}
		else if (command == "quit" || command == "exit") {
			break;
		}
		else {
			std::cout << "Unrecognized command or not logged in\nTry 'login'\n";
		}
	}

	return 0;
}

