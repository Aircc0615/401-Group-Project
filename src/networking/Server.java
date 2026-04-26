package networking;
import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;
import java.util.HashMap;
import chat.ChatList;
import user.User;

public class Server {
	private User[] users;
	private int numUsers;
	private ChatList chats;
	private User[] onlineUsers;
	private int numOnlineUsers;
	private static List<ClientHandler> currentClients;
	private int numCurrentClients;
	private HashMap<Integer, ClientHandler> mapIdtoClient; //int is id
	
	
    public static void main(String[] args) throws IOException, ClassNotFoundException {
        ServerSocket serverSocket = null;
        try {
            serverSocket = new ServerSocket(7777);
            System.out.println("Server is now awaiting a new connection");

            while (true) {
                Socket socket = serverSocket.accept(); //blocks until a client connects
                ClientHandler client = new ClientHandler(socket);
                currentClients.add(client);
                (new Thread(client)).start();
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (serverSocket != null) {
                try {
                    serverSocket.close();
                } catch (IOException err) {
                    err.printStackTrace();
                }
            }
        }

    }
    
    
    public void addUser(User user) {
    	
    }
    
    public boolean performAuthenticationOperation(Socket clientSocket, InputStream clientInputStream, ObjectOutputStream objectOutputStream, Message message, List<Message> messageList) throws IOException {
        //if the login is successful we perform the next step, otherwise we send a failed response
        if(message.subType == SubType.LOGIN) {
            //at the moment everything is a valid login so this is just the default
            if (true) { //this is just a placeholder, we would likely call a function here that checks if their credentials are valid
                successfulLogin(objectOutputStream, messageList); //this function at the moment just sends the user a successful login response message and adds the message to our message list array
                return true; //if successful/this needs to be changed if false but for the current version set to true
            } else {
                failedLoginAttempt();
                return false;
            }
        }

        //this could be moved into its own function, i just have it here since it was listed as a subtype, i assumed it was associated to our authentication module but
        else if (message.subType == SubType.LOGOUT){
            //logout function here
            // if(successfulLogout()) return true;
            // else false
         }

        return false; //same here might need to be changed
    }
}


