package user;

import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class UserLoginModule {
	private List<User> users;
	static Scanner sin = new Scanner(System.in);
	
	public UserLoginModule(List<User> users) {
		this.users = users;
	}
	
	public boolean authenticateUser(User userLoggingIn) {
		for(User user : users) {
			if(user.getUsername().equals(userLoggingIn.getUsername()) && user.getPassword().equals(userLoggingIn.getPassword())) {
				return true;
			}
		}
		return false;
	}
	
	public void createNewUser() {
		System.out.println("Enter new username");
		String username = sin.nextLine();
		
		for(User user : users) {
			if(user.getUsername().equals(username)) {
				System.out.println("That username is already in use. Please try again!");
				return;
			}
		}
		
		System.out.println("Enter new password");
		String password = sin.nextLine();
		User newUser = new User(username, password);
		
		users.add(newUser);
	}
	
	
}
