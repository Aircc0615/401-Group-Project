package networking;

import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.OutputStream;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;

public class ClientHandler implements Runnable{
    private final Socket clientSocket;
    private Server server;
    private static List<Message> messageList = new ArrayList<>();
    
    ObjectInputStream objectInputStream;
    ObjectOutputStream objectOutputStream;

    public ClientHandler(Socket socket, Server server){
        this.clientSocket = socket;
        this.server = server;
    }

    //server driver operations
    public void run() {
        try {
            System.out.println("Connection to client established from " + clientSocket.getInetAddress().getHostAddress() + "!\n");

            InputStream clientInputStream = clientSocket.getInputStream(); //allows us to receive an input stream from client/whatever data is coming from the client
            objectInputStream = new ObjectInputStream(clientInputStream); //whatever the client sent will be displayed as output on the servers console

            OutputStream outputStream = clientSocket.getOutputStream();
            objectOutputStream = new ObjectOutputStream(outputStream); //this allows us to send stuff out to the client

            
            Message message = (Message) objectInputStream.readObject(); //incoming message from client gets deserialized
            messageList.add(message); //add the client message to the array of messages on the server side

            if (message.mainType == MainType.AUTHENTICATION) { //if its a login
                boolean authenticatedUser = performAuthenticationOperation(clientSocket, clientInputStream, message, messageList); //returns true if its a valid user/false if not

                if (authenticatedUser) { //if they're a valid user they can go ahead and send messages
                    while (message.subType != SubType.LOGOUT) {
                        message = (Message) objectInputStream.readObject(); //read the incoming object

                        messageList.add(message); //add the incoming messages to the array

                        performMessageOperation(clientSocket, clientInputStream, message, messageList); //this would perform the appropriate operation depending on the message Main and Sub types
                    }

                    Message logoutSuccess = new Message(MainType.AUTHENTICATION, SubType.LOGOUT, Status.SUCCESS, "Logout successful", null);
                    messageList.add(logoutSuccess);
                    objectOutputStream.writeObject(logoutSuccess);


                    System.out.println("Closing Client Socket.");
                    clientSocket.close();
                }
            }
            else System.out.println("Please enter a valid username and password");
        } catch (Exception e){
            e.printStackTrace();
        }
    }
    
    public boolean performAuthenticationOperation(Socket clientSocket, InputStream clientInputStream, Message message, List<Message> messageList) throws IOException {
        //if the login is successful we perform the next step, otherwise we send a failed response
        if(message.subType == SubType.LOGIN) {
        	successfulLogin(); //at the moment the code will always send a successful respondse
        	return true;
        	/* this works but the client doesnt support creating new users just yet
            boolean authenticated = server.authenticateUser(message.getUser());
        	if(authenticated) {
        		successfulLogin(); //this function at the moment just sends the user a successful login response message and adds the message to our message list array
				return true; //if successful/this needs to be changed if false but for the current version set to true
        	}
	        else {
	        	failedLoginAttempt();
	        }
	        */
        }

        //this could be moved into its own function, i just have it here since it was listed as a subtype, i assumed it was associated to our authentication module but
        else if (message.subType == SubType.LOGOUT){
            //logout function here
            // if(successfulLogout()) return true;
            // else false
         }

        return false; //same here might need to be changed
    }

    //facade/wrapper function that calls the function corresponding to the message types
    public void performMessageOperation(Socket clientSocket, InputStream clientInputStream, Message message, List<Message> messageList) throws IOException { //mainType:  AUTHENTICATION, DISPLAY, TEXT, CHAT_OPERATION, AUDIT_OPERATION
        if (message.mainType == MainType.DISPLAY) {
            switch (message.subType){
                case SubType.ACTUAL_CHAT:
                    //actual chat function here
                    break;
                case SubType.USER_STATE:
                    //user state function here
                    break;
                default:
                    System.out.println("Message Object Constructed Incorrectly");
            }
        }

        else if (message.mainType == MainType.TEXT){
            switch (message.subType) {
                case SubType.SEND_TEXT_MESSAGE:
                	System.out.println("From " + clientSocket.getInetAddress().getHostAddress() + ": " + message.getText()); //display message along with who its from
                    server.handleSendText(message);
                    break;
                default:
                    System.out.println("Message Object Constructed Incorrectly");
            }
        }

        else if(message.mainType == MainType.CHAT_OPERATION){
            switch (message.subType) {
                case SubType.OPEN_CHAT:    //can someone clarify how to handle this message type
                    server.handleOpenChat(message, this);
                    break;
                case SubType.CHAT_LIST:    //can someone clarify how to handle this message type
                	server.handleChatList(message, this);
                    break;
                case SubType.CHAT_USER:    //can someone clarify how to handle this message type
                	server.handleChatUser(message, this);
                    break;
                case SubType.CREATE_GC:
                    server.handleCreateChat(message, this);
                    break;
                case SubType.ADD_USER_TO_GC:
                    server.handleAddUserToChat(message, this);
                    break;
                case SubType.REMOVE_USER_FROM_GC:
                	server.handleRemoveUserFromChat(message, this);
                    break;
                case SubType.DELETE_GC:
                	server.handleDeleteGC(message, this);
                    break;
                default:
                    System.out.println("Message Object Constructed Incorrectly");
            }
        }
        else if(message.mainType == MainType.AUDIT_OPERATION){
            switch (message.subType) {
                case SubType.ENTER_AUDIT_MODE:
                	server.handleEnterAuditMode(message, this);
                    break;
                case SubType.SELECT_USER:
                	server.handleAuditSelectUser(message, this);
                    break;
                case SubType.VIEW_CHATS:
                	server.handleAuditViewChats(message, this);
                    break;
                case SubType.EXPORT_CHAT_LOG:
                	server.handleAuditExportChat(message, this);
                    break;
                default:
                    System.out.println("Message Object Constructed Incorrectly");
            }
        }
        else if(message.mainType == MainType.UNDEFINED){
            System.out.println("error");
        }
    }

    
    //helper functions
    public void successfulLogin() throws IOException {
        Message loginSuccess = new Message(MainType.AUTHENTICATION, SubType.LOGIN_RESPONSE , Status.SUCCESS, "Login successful", null); //create a login success message to send to the user
        objectOutputStream.writeObject(loginSuccess); //sends back the successful login message
        messageList.add(loginSuccess); //login message that is sent out from server to client gets added to the array
    }
    
    public void failedLoginAttempt() throws IOException{
    	Message loginFailed = new Message(MainType.AUTHENTICATION, SubType.LOGIN_RESPONSE , Status.SUCCESS, "Invalid username/password. Please try again.", null); //create a login success message to send to the user
        objectOutputStream.writeObject(loginFailed); //sends back the successful login message
        messageList.add(loginFailed); //login message that is sent out from server to client gets added to the array
    }

	public void sendToClient(List<Message> messages) throws IOException {
		objectOutputStream.writeObject(messages);
	}
}