/*
Class for every listed item of a user.
Should be complete as is, but subject to change
//TODO should be associated with user but may not be required to be
*/
#include "FileIO.h"
#include<iostream>

class Auction {
	std::string itemName;	//Name of the item being sold
	float price;			//Price of the item listed
	int duration;			//Length of auction in days

public:
	Auction() { }

	//Constructor for the auction
	Auction(std::string itemName, float price, int duration) {
		this->itemName = itemName;
		this->price = price;
		this->duration = duration;
	}

	//Get item name of auction
	std::string getItemName() {
		return this->itemName;
	}

	//Get bid of the auction
	float getPrice() {
		return this->price;
	}

	//Get duration of the auction
	int getDuration() {
		return this->duration;
	}

};