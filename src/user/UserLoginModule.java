package user;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Scanner;

import networking.ClientHandler;
import networking.Message;

public class UserLoginModule {
	private HashMap<String, User> usernameToUser;
	static Scanner sin = new Scanner(System.in);
	
	public UserLoginModule(HashMap<String, User> usernameToUser) {
		this.usernameToUser = usernameToUser;
	}
	
	public User authenticateUser(User userLoggingIn) {
		for(User user : users) {
			if(user.getUsername().equals(userLoggingIn.getUsername()) && user.getPassword().equals(userLoggingIn.getPassword())) {
				return user;
			}
		}
		return null;
  }
	public boolean authenticateUser(User userLoggingIn) {
		String username = userLoggingIn.getUsername();
		boolean userAuthenticated = false;
		if(usernameToUser.containsKey(username)) {
			userAuthenticated = usernameToUser.get(username).getPassword().equals(userLoggingIn.getPassword());
		}
		
		return userAuthenticated;
	}
	
	public User createUser(Message message) {
		String username = message.getUser().getUsername();
		if(usernameToUser.containsKey(username)){
			return null;
		}
		
		usernameToUser.put(username, message.getUser());
		return message.getUser();
	}


}
