package networking;
import java.io.*;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

import chat.TextMessage;
import user.User;

public class Client {
	static InputStream serverInputStream = null;
	static ObjectInputStream objectInputStream  = null;
	static OutputStream outputStream = null;
	static ObjectOutputStream objectOutputStream = null;
	static List<Message> messageHistory = new ArrayList<>();
	static Scanner sin = new Scanner(System.in);
	
	
	public static boolean login(User user, ObjectOutputStream objectOutputStream, Socket clientSideSocket) throws IOException, ClassNotFoundException {
        System.out.println(user.getUsername() + "attempting to log in...");
        
        Message loginRequestMessage = new Message(MainType.AUTHENTICATION, SubType.LOGIN, Status.REQUEST, user.getUsername() + "requesting login", user); //login message created
        messageHistory.add(loginRequestMessage); //add the login message to the message history
        
        objectOutputStream.writeObject(loginRequestMessage); //sending the login message to server
        
        serverInputStream = clientSideSocket.getInputStream(); //whatever is coming in from the server
        objectInputStream = new ObjectInputStream(serverInputStream); // we need to reconstruct the message object
        
        Message incomingLoginResponse = (Message) objectInputStream.readObject(); //deSerialized the message
        messageHistory.add(incomingLoginResponse);
        
        if(incomingLoginResponse.status == Status.SUCCESS) {
            System.out.println(incomingLoginResponse.getText() + "\n");
            //List<textMessage> textMessageHistory = new ArrayList<>(); This isnt needed (likely will use this in communication system)
            System.out.println("Enter text to send!\n");
            return true;
        }
        else {
        	System.out.println("Invalid Login. Please try again.");
        	return false;
        }
	}
	
	public static boolean sendMessage(User user) throws IOException {
		Message message;
    	String text = "";
    	boolean logOut = false;
        while(!text.equals("!exitChat")) {
        	text = sin.nextLine(); //read in user input
            TextMessage textMessage = new TextMessage(text, user.getUsername(), user.getId()); //let 0 represent some userID

            if(text.equals("!exitChat")){ //this sends a logout message to the server
                message = new Message(MainType.AUTHENTICATION, SubType.LOGOUT, Status.REQUEST, textMessage.getText(), user);
                messageHistory.add(message);
                logOut = true;
                objectOutputStream.writeObject(message); //where the object gets serialized and sent
                return logOut;
            }
            else { //normal text being sent
                 //user input is used to make textMessage object
                message = new Message(MainType.TEXT, SubType.SEND_TEXT_MESSAGE , Status.REQUEST, textMessage.getText(), user);
                messageHistory.add(message); //the message the user input should be sent
                // textMessageHistory.add(textMessage); add the text message to the text message history array | This isnt needed (likely will use this in communication system)
                objectOutputStream.writeObject(message); //where the object gets serialized and sent
            }
        }
        return logOut;
	}
	
	public static void listenForServerMessages(ObjectInputStream objectInputStream, Socket clientSideSocket) throws ClassNotFoundException, IOException {
        List<Message> incomingServerMessages = (List<Message>) objectInputStream.readObject();
        incomingServerMessages.forEach(msg -> {
            if(msg.subType == SubType.LOGOUT) {
                System.out.println("Logging out!"); //after user logs out we can close the client side socket
                try {
					clientSideSocket.close();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
            }
            else System.out.println("\nServer side echo " + clientSideSocket.getInetAddress().getHostAddress() + ": " + msg.getText() + '\n'); //display message along with who its from
        });
	}
	
    public static void main(String[] args) throws IOException, ClassNotFoundException {
         //cin == sin
        int port = 7777;
        String host = "localhost";

        Socket clientSideSocket = new Socket(host, port); //create a client side socket that connects to server with the host and port specified
        System.out.println("Connected to: " + clientSideSocket.getInetAddress().getHostAddress());

        outputStream = clientSideSocket.getOutputStream(); //output that were sending to server
        objectOutputStream = new ObjectOutputStream(outputStream); //deconstructing the object were sending, this serializes the object

        System.out.println("Enter Login!");
		String username = "username";// sin.nextLine() user would enter login info here
        String password = "password";
        User user = new User(username, password);
        
        boolean authenticatedUser = login(user, objectOutputStream, clientSideSocket);

        while(authenticatedUser) {
        	if(!sendMessage(user))
        		break;
            listenForServerMessages(objectInputStream, clientSideSocket);
//          messageHistory.add(incomingServerMessages);
        }
    }

}
