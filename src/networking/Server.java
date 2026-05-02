package networking;
import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;
import java.util.HashMap;

import chat.Chat;
import chat.ChatList;
import chat.ChatType;
import chat.TextMessage;
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
	private HashMap<String, ClientHandler> mapUsernameToClient; //string is username
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
    
    public User authenticateUser(User userToAuthenticate, ClientHandler handler) {
    	User user = userLoginModule.authenticateUser(userToAuthenticate);
    	if(user != null)
    		mapUsernameToClient.put(user.getUsername(), handler);
    	return user;
    }
    
    public void createNewUser() {
    	userLoginModule.createNewUser();
    }
    
    public void sendToClients(List<Message> messages) throws IOException {
    	for(ClientHandler client : currentClients) {
    		client.sendToClient(messages);
    	}
    }

    public void sendToClients(List<Message> messages, String[] usernames) throws IOException {
    	for(String username : usernames) {
    		ClientHandler client = mapUsernameToClient.get(username);
    		client.sendToClient(messages);
    	}
    }

    public void sendToClient(List<Message> messages, String username) throws IOException {
    	ClientHandler client = mapUsernameToClient.get(username);
    	client.sendToClient(messages);
    }
    
	// MESSAGE: MainType.TEXT    
	// SubType.SEND_TEXT_MESSAGE 
    public void handleSendText(String text, String username, int chatId) throws IOException {
    		User user = usernameToUser.get(username);
    		TextMessage txtMsg = new TextMessage(text, username, user.getId());
    		try {
    			chats.addChatMessage(chatId, txtMsg);
    		} catch (IndexOutOfBoundsException e) {
    			System.err.println("Invalid Chat Id of " + chatId + " detected from user: " + username);
    			Message failedText = new Message(MainType.TEXT, SubType.SEND_TEXT_MESSAGE, Status.FAILED, txtMsg, chatId);
    			List<Message> failureMessages = new ArrayList<>();
    			failureMessages.add(failedText);
    			sendToClient(failureMessages, username);
    			return;
    		}
    		String[] usernames = chats.getChatMembers(chatId);
    		for(String name : usernames) {
    			User otherUser = usernameToUser.get(name);
    			otherUser.updateChatOrder(chatId);
    		}
    	
        Message msgToSend = new Message(MainType.TEXT, SubType.SEND_TEXT_MESSAGE ,Status.SUCCESS, txtMsg, chatId);
        List<Message> messagesToSend = new ArrayList<>();
        messagesToSend.add(msgToSend);
        sendToClients(messagesToSend);
    }
   
	// MESSAGE: MainType.CHAT_OPERATIONs    
	// SubType.CREATE_GC
	public void handleCreateChat(Message message, ClientHandler clientHandler) {
		String usersToBeAddedToChat = message.getUser().getUsername() + ", "+ message.getText();
		String[] memberUsernames = usersToBeAddedToChat.split(","); //the usernames will be passed as a single string so we split
		for(int i = 0; i < memberUsernames.length; i++) {
			memberUsernames[i] = memberUsernames[i].trim();
		}
		
		Chat newChat = null;
		if(memberUsernames.length == 2) {
			newChat = new Chat(message.getUser().getUsername(), memberUsernames, ChatType.PRIVATE);
		}
		else {
			newChat = new Chat(message.getUser().getUsername(), memberUsernames, ChatType.GROUP);
		}
		chats.addChat(newChat);
	}
	
	// SubType.ADD_USER_TO_GC
	public void handleAddUserToChat(Message message, ClientHandler clientHandler) {
		
		
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
    
    
    //can someone clarify how to handle the message types below
    //i did not add any client side operations to handle this message type
	public void handleChatList(Message message, ClientHandler clientHandler) {
		// TODO Auto-generated method stub
		
	}

	public void handleOpenChat(Message message, ClientHandler clientHandler) {
		// TODO Auto-generated method stub
		
	}

	public void handleChatUser(Message message, ClientHandler clientHandler) {
		// TODO Auto-generated method stub
		
	}

}


