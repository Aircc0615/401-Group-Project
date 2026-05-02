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
		String username = userLoggingIn.getUsername();
		String password = userLoggingIn.getPassword();
		if(usernameToUser.containsKey(username)) {
			User userInMap = usernameToUser.get(username);
			if(userInMap.getUsername().equals(username) && userInMap.getPassword().equals(password)) {
				return userInMap;
			}
		}
		return null;
  }
	
	public User createUser(User user) {
		String username = user.getUsername();
		if(usernameToUser.containsKey(username)){
			return null;
		}
		
		usernameToUser.put(username, user);
		return user;
	}


}
