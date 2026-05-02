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
   
	// MESSAGE: MainType.CHAT_OPERATIONs
	// CREATE_GC
	public void handleCreateChat(Message message, ClientHandler clientHandler) {
		// TODO Auto-generated method stub
		
	}
	
	// SubType.ADD_USER_TO_GC
	public void handleAddUserToChat(Message message, ClientHandler clientHandler) {
		// TODO Auto-generated method stub
		
	}
	
	// SubType.REMOVE_USER_FROM_GC
	public void handleRemoveUserFromChat(Message message, ClientHandler clientHandler) {
		// TODO Auto-generated method stub
		
	}
	
	// SubType.DELETE_GC
	public void handleDeleteGC(Message message, ClientHandler clientHandler) {
		// TODO Auto-generated method stub
		
	}
    
    // MESSAGE: MainType.AUDIT_OPERATION
    // SubType.ENTER_AUDIT_MODE
    public void handleEnterAuditMode(Message message, ClientHandler clientHandler) {
		// TODO Auto-generated method stub
		
	}
    
	// SubType.SELECT_USER
    public void handleAuditSelectUser(Message message, ClientHandler clientHandler) {
		// TODO Auto-generated method stub
		
	}
    
    // SubType.VIEW_CHATS
    public void handleAuditViewChats(Message message, ClientHandler clientHandler) {
		// TODO Auto-generated method stub
		
	}
    
    // SubType.EXPORT_CHAT_LOG
    public void handleAuditExportChat(Message message, ClientHandler clientHandler) {
		// TODO Auto-generated method stub
		
	}

}


