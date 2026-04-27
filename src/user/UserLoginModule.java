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
	
	public boolean authenticateUser(User user) {
		
		return false;
	}
	
	public void createNewUser() {
		System.out.println("Enter new username");
		String username = sin.nextLine();
		
		System.out.println("Enter new password");
		String password = sin.nextLine();
		User newUser = new User(username, password);
		
		users.add(newUser);
	}
}
