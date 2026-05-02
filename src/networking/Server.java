package networking;
import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;
import java.util.HashMap;
import chat.ChatList;
import user.User;
import user.UserLoginModule;

public class Server {
	private List<User> users = new ArrayList<>();
	private int numUsers;
	private ChatList chats;
	private List<User> onlineUsers = new ArrayList<>();
	private int numOnlineUsers;
	private static List<ClientHandler> currentClients = new ArrayList<>();
	private int numCurrentClients;
	private HashMap<Integer, ClientHandler> mapIdtoClient; //int is id, at the moment not being used but could be use for more efficient message routing
	private UserLoginModule userLoginModule = new UserLoginModule(users); 
	private HashMap<String, User> usernameToUser = new HashMap();
	
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
    
    public boolean authenticateUser(User userToAuthenticate) {
    	return userLoginModule.authenticateUser(userToAuthenticate);
    }
    
    public void createNewUser() {
    	userLoginModule.createNewUser();
    }
    
    public void sendToClients(List<Message> messages) throws IOException {
    	for(ClientHandler client: currentClients) {
    		client.sendToClient(messages);
    	}
    }
   
    
    public void handleAuditExportChat(Message message, ClientHandler clientHandler) {
		// TODO Auto-generated method stub
		
	}

    public void handleAuditViewChats(Message message, ClientHandler clientHandler) {
		// TODO Auto-generated method stub
		
	}

    public void handleAuditSelectUser(Message message, ClientHandler clientHandler) {
		// TODO Auto-generated method stub
		
	}

    public void handleEnterAuditMode(Message message, ClientHandler clientHandler) {
		// TODO Auto-generated method stub
		
	}

	public void handleDeleteGC(Message message, ClientHandler clientHandler) {
		// TODO Auto-generated method stub
		
	}

	public void handleRemoveUserFromChat(Message message, ClientHandler clientHandler) {
		// TODO Auto-generated method stub
		
	}

	public void handleAddUserToChat(Message message, ClientHandler clientHandler) {
		// TODO Auto-generated method stub
		
	}

	public void handleCreateGC(Message message, ClientHandler clientHandler) {
		// TODO Auto-generated method stub
		
	}
   

}


