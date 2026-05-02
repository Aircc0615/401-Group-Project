package networking;
import java.io.*;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

import chat.Chat;
import chat.TextMessage;
import user.User;

public class Client {
	static InputStream serverInputStream = null;
	static ObjectInputStream objectInputStream  = null;
	static OutputStream outputStream = null;
	static ObjectOutputStream objectOutputStream = null;
	static Socket clientSideSocket = null;
	static List<Message> messageHistory = new ArrayList<>();
	static Scanner sin = new Scanner(System.in);
	private static User user;
	
	
    public static void main(String[] args) throws IOException, ClassNotFoundException {    	
    	clientSideSocket = connectToServer();
    	
        System.out.println("Please enter your username!");
		String username = sin.nextLine();
		System.out.println("Please enter your password!");
        String password = sin.nextLine();
        user = new User(username, password);
        
        User authenticatedUser = login(user); //if the user we passed is authenticated it returns the same value otherwise it returns null

        if (authenticatedUser != null) {
        	Thread serverListener = new Thread(new Runnable() {
        		public void run() {
        			try {
						listenForServerMessages();
					} catch (ClassNotFoundException | IOException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}     
        		}
    		});
             
            serverListener.start();
            String text;
            while(!clientSideSocket.isClosed()) {
            	text = sin.nextLine();
            	sendMessage(user, text);
            }
        }
    }
	
    // Client Side Server Operations
	// Allows Client to connect to server and returns the socket 
	public static Socket connectToServer() throws UnknownHostException, IOException {
        int port = 7777;
        String host = "localhost"; //need to update to actual host

        clientSideSocket = new Socket(host, port); //create a client side socket that connects to server with the host and port specified
        System.out.println("Connected to: " + clientSideSocket.getInetAddress().getHostAddress());

        outputStream = clientSideSocket.getOutputStream(); //output that were sending to server
        objectOutputStream = new ObjectOutputStream(outputStream); //deconstructing the object were sending, this serializes the object
		return clientSideSocket;
	}
	
	// Allows client to listen for incoming messages
	public static void listenForServerMessages() throws ClassNotFoundException, IOException {
        while(!clientSideSocket.isClosed()) {
			List<Message> incomingServerMessages = (List<Message>) objectInputStream.readObject();
	        incomingServerMessages.forEach(msg -> {
	            if(msg.subType == SubType.LOGOUT) {
	                System.out.println("Logging out!"); //after user logs out we can close the client side socket
	                try {
						clientSideSocket.close(); //once the server actually sends the logout message the socket can close
					} catch (IOException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
	            }
	            else { 
	            	if(msg.getUser() != null)
	            		System.out.println("\n" + msg.getUser().getUsername() + ": " + msg.getText() + '\n'); //display message along with who its from
	            	else {
	            		System.out.println("\nServer: " + msg.getText() + '\n');
	            	}
	            }
            });
        }
	}

	
    // MESSAGE: MainType.AUTHENTICATION
    // SubType.LOGIN
	public static User login(User user) throws IOException, ClassNotFoundException {
        System.out.println(user.getUsername() + " attempting to log in...");
        
        Message loginRequestMessage = new Message(MainType.AUTHENTICATION, SubType.LOGIN, Status.REQUEST, user.getUsername() + "requesting login", user); //login message created
        messageHistory.add(loginRequestMessage); //add the login message to the message history
        
        objectOutputStream.writeObject(loginRequestMessage); //sending the login message to server
        
        serverInputStream = clientSideSocket.getInputStream(); //whatever is coming in from the server
        objectInputStream = new ObjectInputStream(serverInputStream); // we need to reconstruct the message object
        
        Message incomingLoginResponse = (Message) objectInputStream.readObject(); //deSerialized the message
        messageHistory.add(incomingLoginResponse);
        
        if(incomingLoginResponse.status == Status.SUCCESS) {
            System.out.println(incomingLoginResponse.getText() + "\n");
            System.out.println("Enter text to send!\n");
            return user;
        }
        else {
        	System.out.println("Invalid Login. Please try again.");
        	return null;
        }
	}
	
    // SubType.LOGOUT
	public static void logout() throws IOException, ClassNotFoundException {
		Message logOutRequest = new Message(MainType.AUTHENTICATION, SubType.LOGOUT , Status.REQUEST, user.getUsername() + "Requesting logout...\n", user);
		messageHistory.add(logOutRequest); //store operation in history
		objectOutputStream.writeObject(logOutRequest);
	}
	
	
	
	// MESSAGE: MainType.TEXT
	// SubType.SEND_TEXT_MESSAGE
	public static void sendMessage(User user, String text) throws IOException {
		TextMessage textMessage = new TextMessage(text, user.getUsername(), user.getId()); //let 0 represent some userID
        Message message = new Message(MainType.TEXT, SubType.SEND_TEXT_MESSAGE , Status.REQUEST, textMessage.getText(), user);
        messageHistory.add(message); //the message the user input should be sent
        objectOutputStream.writeObject(message); //where the object gets serialized and sent     
	}

	
	
	// MESSAGE: MainType.DISPLAY
	// SubType.ACTUAL_CHAT
	public static void requestActualChat() throws IOException, ClassNotFoundException {
		Message actualChatRequest = new Message(MainType.DISPLAY, SubType.ACTUAL_CHAT , Status.REQUEST, null, user);
		messageHistory.add(actualChatRequest); //store operation in history
		objectOutputStream.writeObject(actualChatRequest);

	}
	
	// SubType.USER_STATE
	public static void getUserState() throws IOException, ClassNotFoundException {
		Message userStateRequest = new Message(MainType.DISPLAY, SubType.USER_STATE , Status.REQUEST, null, user);
		messageHistory.add(userStateRequest); //store operation in history
		objectOutputStream.writeObject(userStateRequest);
	}
	
	
	// MESSAGE: MainType.CHAT_OPERATIONs
	// CREATE_GC 	||       this will work for making either a DM or GC
	public void createChat(String usernames) throws IOException {
		Message createGC= new Message(MainType.CHAT_OPERATION, SubType.CREATE_GC , Status.REQUEST, usernames, user);
		messageHistory.add(createGC); //store operation in history
		objectOutputStream.writeObject(createGC);
	}
	
	// SubType.ADD_USER_TO_GC
	public void addUserToChat(String username) throws IOException {
		Message addUserToGC = new Message(MainType.CHAT_OPERATION, SubType.ADD_USER_TO_GC , Status.REQUEST, username, user);
		messageHistory.add(addUserToGC); //store operation in history
		objectOutputStream.writeObject(addUserToGC);
	}
	
	// SubType.REMOVE_USER_FROM_GC
	public void removeUserFromChat(String username) throws IOException {
		Message removeUserFromGC = new Message(MainType.CHAT_OPERATION, SubType.REMOVE_USER_FROM_GC , Status.REQUEST, username, user);
		messageHistory.add(removeUserFromGC); //store operation in history
		objectOutputStream.writeObject(removeUserFromGC);
	}
	
	// SubType.DELETE_GC
	public void DeleteChat(String chatID) throws IOException {
		//need a chatID to perform, most likely the chat were hovering over/clicking on
		Message chatToDelete = new Message(MainType.CHAT_OPERATION, SubType.DELETE_GC , Status.REQUEST, chatID, user);
		messageHistory.add(chatToDelete); //store operation in history
		objectOutputStream.writeObject(chatToDelete);
	}	
		
	
	
	// MESSAGE: MainType.AUDIT_OPERATION
	// SubType.ENTER_AUDIT_MODE
	public void enterAuditMode() throws IOException {
		Message enterAuditMode = new Message(MainType.AUDIT_OPERATION, SubType.ENTER_AUDIT_MODE , Status.REQUEST, null, user);
		messageHistory.add(enterAuditMode); //store operation in history
		objectOutputStream.writeObject(enterAuditMode);
	}
	
	// SubType.SELECT_USER
	public void audit_SelectUser(String username) throws IOException {
		Message selectedUser = new Message(MainType.AUDIT_OPERATION, SubType.SELECT_USER , Status.REQUEST, username, user);
		messageHistory.add(selectedUser); //store operation in history
		objectOutputStream.writeObject(selectedUser);
	}
	
	// SubType.VIEW_CHATS
	public void audit_ViewChats() throws IOException {
		Message viewChatsRequest = new Message(MainType.AUDIT_OPERATION, SubType.VIEW_CHATS , Status.REQUEST, null, user);
		messageHistory.add(viewChatsRequest); //store operation in history
		objectOutputStream.writeObject(viewChatsRequest);
	}
	
	// SubType.EXPORT_CHAT_LOG
	public void audit_ExportChatLog() throws IOException {
		Message exportLogRequest = new Message(MainType.AUDIT_OPERATION, SubType.EXPORT_CHAT_LOG , Status.REQUEST, null, user);
		messageHistory.add(exportLogRequest); //store operation in history
		objectOutputStream.writeObject(exportLogRequest);
	}
	

}
