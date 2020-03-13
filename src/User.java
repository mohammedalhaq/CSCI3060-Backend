
public class User {
	String username;
	String type;
	Float credit;
	
	User(){
		
	}

	//Get the user object's username
	String getUsername() {
		return this.username;
	}

	//Get the user object's account type
	String getType() {
		return this.type;
	}

	//Get the user object's credit amount
	float getCredit() {
		return this.credit;
	}

	//Set the user object's username
	void setUsername(String username) {
		this.username = username;
	}

	//Set the user object's account type
	void setType(String type) {
		this.type=type;
	}

	//Set the user object's credit amount
	void setCredit(float credit) {
		this.credit=credit;
	}

}
