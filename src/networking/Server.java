package src.networking;
import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;

public class Server {
    public static void main(String[] args) throws IOException, ClassNotFoundException {
        ServerSocket serverSocket = null;
        try {
            serverSocket = new ServerSocket(7777);
            System.out.println("Server is now awaiting a new connection");

            while (true) {
                Socket socket = serverSocket.accept(); //blocks until a client connects
                ClientHandler client = new ClientHandler(socket);
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

    private static class ClientHandler implements Runnable{
        private final Socket clientSocket;

        public ClientHandler(Socket socket){
            this.clientSocket = socket;
        }

        //helper functions
        public void successfulLogin(ObjectOutputStream objectOutputStream, List<Message> messageList) throws IOException {
            Message loginSuccess = new Message(MainType.AUTHENTICATION, SubType.LOGIN_RESPONSE , Status.SUCCESS, "Login successful"); //create a login success message to send to the user
            objectOutputStream.writeObject(loginSuccess); //sends back the successful login message
            messageList.add(loginSuccess); //login message that is sent out from server to client gets added to the array
        }
        public void failedLoginAttempt(){
            System.out.println("");
        }
        public void sendText(Socket clientSocket, Message message, ObjectOutputStream objectOutputStream) throws IOException {
            System.out.println("From " + clientSocket.getInetAddress().getHostAddress() + ": " + message.getText()); //display message along with who its from
            String toUpperMsg = message.getText().toUpperCase();
            Message msgReceipt = new Message(MainType.TEXT, SubType.SEND_TEXT_MESSAGE ,Status.SUCCESS, toUpperMsg); //At the moment this just echoes and doesnt handle sending to other clients
            objectOutputStream.writeObject(msgReceipt);
        }

        //facade/wrapper function that calls the function corresponding to the message types
        public void performMessageOperation(Socket clientSocket, InputStream clientInputStream, ObjectOutputStream objectOutputStream, Message message, List<Message> messageList) throws IOException { //mainType:  AUTHENTICATION, DISPLAY, TEXT, CHAT_OPERATION, AUDIT_OPERATION

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
                        sendText(clientSocket, message, objectOutputStream);
                        break;
                    default:
                        System.out.println("Message Object Constructed Incorrectly");
                }
            }

            else if(message.mainType == MainType.CHAT_OPERATION){
                switch (message.subType) {
                    case SubType.OPEN_CHAT:
                        //open chat function here
                        break;
                    case SubType.CHAT_LIST:
                        //chat list response function here
                        break;
                    case SubType.CHAT_USER:
                        //chat user function here
                        break;
                    case SubType.CREATE_GC:
                        //create gc function here
                        break;
                    case SubType.ADD_USER_TO_GC:
                        //add user to gc function here
                        break;
                    case SubType.REMOVE_USER_FROM_GC:
                        //remove user from gc function here
                        break;
                    case SubType.DELETE_GC:
                        //delete gc function here
                        break;
                    default:
                        System.out.println("Message Object Constructed Incorrectly");
                }
            }
            else if(message.mainType == MainType.AUDIT_OPERATION){
                switch (message.subType) {
                    case SubType.ENTER_AUDIT_MODE:
                        //audit mode function here
                        break;
                    case SubType.SELECT_USER:
                        //user selection function here
                        break;
                    case SubType.VIEW_CHATS:
                        //view chat function here
                        break;
                    case SubType.EXPORT_CHAT_LOG:
                        //export chat function here
                        break;
                    default:
                        System.out.println("Message Object Constructed Incorrectly");
                }
            }
            else if(message.mainType == MainType.UNDEFINED){
                System.out.println("error");
            }
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

        //server driver operations
        public void run() {
            try {
                System.out.println("Connection to client established from " + clientSocket.getInetAddress().getHostAddress() + "!\n");

                InputStream clientInputStream = clientSocket.getInputStream(); //allows us to receive an input stream from client/whatever data is coming from the client
                ObjectInputStream objectInputStream = new ObjectInputStream(clientInputStream); //whatever the client sent will be displayed as output on the servers console

                OutputStream outputStream = clientSocket.getOutputStream();
                ObjectOutputStream objectOutputStream = new ObjectOutputStream(outputStream); //this allows us to send stuff out to the client

                List<Message> messageList = new ArrayList<>();
                Message message = (Message) objectInputStream.readObject(); //incoming message from client gets deserialized
                messageList.add(message); //add the client message to the array of messages on the server side

                if (message.mainType == MainType.AUTHENTICATION) { //if its a login
                    boolean authenticatedUser = performAuthenticationOperation(clientSocket, clientInputStream, objectOutputStream, message, messageList); //returns true if its a valid user/false if not

                    if (authenticatedUser) { //if theyre a valid user they can go ahead and send messages
                        while (message.subType != SubType.LOGOUT) {
                            message = (Message) objectInputStream.readObject();

                            messageList.add(message); //add the incoming messages to the array

                            performMessageOperation(clientSocket, clientInputStream, objectOutputStream, message, messageList); //this would perform the appropriate operation depending on the message Main and Sub types
                        }

                        Message logoutSuccess = new Message(MainType.AUTHENTICATION, SubType.LOGOUT, Status.SUCCESS, "Logout successful");
                        messageList.add(logoutSuccess);
                        objectOutputStream.writeObject(logoutSuccess);

                        //printing each message in messageList for each message we print id, type, status and text
//                    messageList.forEach(msg -> {
//                        System.out.println("ID: " + msg.getId() + "\tType: " + msg.getType() + "\tStatus: " + msg.getStatus());
//                        System.out.println("Text: " + msg.getText());
//                        System.out.println();
//                    });

                        System.out.println("Closing Client Socket.");

                        clientSocket.close();
                    }
                }
                else System.out.println("Please enter a valid username and password");
            } catch (Exception e){
                e.printStackTrace();
            }
        }
    }
}


