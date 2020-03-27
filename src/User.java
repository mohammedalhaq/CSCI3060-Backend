
public class User {
	String username;
	String type;
	Float credit;
	
	User(){
		
	}

	User(String username, String type, float credit){
		this.username = username;
		this.type = type;
		this.credit = credit;
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
}
