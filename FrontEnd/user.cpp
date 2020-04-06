/*
Class for user
Should be complete as is
Will make a user object on login with all the data
Or create a user object on account creation
*/
#include<iostream>

class User {
	std::string username;	//The actual username of the user
	std::string type;		//The type of user (FS, BS, SS, Admin)
	float credit;			//The amount of credit in the user's account

public:
	//Default constructor for the user
	User() {	}

	//Constructor for the user
	User(std::string username, std::string type, float credit) {
		this->username = username;
		this->type = type;
		this->credit = credit;
	}

	//Get the user object's username
	std::string getUsername() {
		return this->username;
	}

	//Get the user object's account type
	std::string getType() {
		return this->type;
	}

	//Get the user object's credit amount
	float getCredit() {
		return this->credit;
	}

	//Get the user object's username
	void setUsername(std::string username) {
		this->username = username;
	}

	//Get the user object's account type
	void setType(std::string type) {
		this->type=type;
	}

	//Get the user object's credit amount
	void setCredit(float credit) {
		this->credit=credit;
	}



};