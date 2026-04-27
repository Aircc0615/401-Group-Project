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
	private static List<ClientHandler> currentClients = new ArrayList<>();
	private int numCurrentClients;
	private HashMap<Integer, ClientHandler> mapIdtoClient; //int is id
	
	
    public static void main(String[] args) throws IOException, ClassNotFoundException {
    	Server server = new Server();
    	server.startServer();
    }
    
    public void startServer() {
    	ServerSocket serverSocket = null;
        try {
            serverSocket = new ServerSocket(7777);
            System.out.println("Server is now awaiting a new connection");

            while (true) {
                Socket socket = serverSocket.accept(); //blocks until a client connects
                ClientHandler client = new ClientHandler(socket, this);
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
    
    public void sendToClients(List<Message> messages) throws IOException {
    	for(ClientHandler client: currentClients) {
    		client.sendToClient(messages);
    	}
    }
   
    public void addUser(User user) {
    	
    }
    

   

	private void failedLoginAttempt() {
		// TODO Auto-generated method stub
		
	}


	private void successfulLogin(ObjectOutputStream objectOutputStream, List<Message> messageList) {
		// TODO Auto-generated method stub
		
	}
}


