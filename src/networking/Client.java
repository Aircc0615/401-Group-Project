package networking;
import java.io.*;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

import chat.TextMessage;
import user.User;

public class Client {
    public static void main(String[] args) throws IOException, ClassNotFoundException {
        Scanner sin = new Scanner(System.in); //cin == sin
        int port = 7777;
        String host = "localhost";

        Socket clientSideSocket = new Socket(host, port); //create a client side socket that connects to server with the host and port specified
        System.out.println("Connected to: " + clientSideSocket.getInetAddress().getHostAddress());

        OutputStream outputStream = clientSideSocket.getOutputStream(); //output that were sending to server
        ObjectOutputStream objectOutputStream = new ObjectOutputStream(outputStream); //deconstructing the object were sending, this serializes the object

        System.out.println("Enter Login!");
        
        String username = "Gary";// sin.nextLine() user would enter login info here
        String password = "password";
        User user = new User(username, password);

        Message loginRequestMessage = new Message(MainType.AUTHENTICATION, SubType.LOGIN, Status.REQUEST, user.getUsername() + "requesting login"); //login message created

        List<Message> messageHistory = new ArrayList<>();
        messageHistory.add(loginRequestMessage); //add the login message to the message history

        objectOutputStream.writeObject(loginRequestMessage); //sending the login message to server

        InputStream serverInputStream = clientSideSocket.getInputStream(); //whatever is coming in from the server
        ObjectInputStream objectInputStream = new ObjectInputStream(serverInputStream); // we need to reconstruct the message object
        Message incomingLoginResponse = (Message) objectInputStream.readObject(); //deSerialized the message

        Message message;
        
        if(incomingLoginResponse.status == Status.SUCCESS) {
            System.out.println(incomingLoginResponse.getText() + "\n");
            String text = "";
            //List<textMessage> textMessageHistory = new ArrayList<>(); This isnt needed (likely will use this in communication system)
            System.out.println("Enter text to send!\n");
            while(!text.equals("!exitChat")) {

                text = sin.nextLine(); //read in user input
                TextMessage textMessage = new TextMessage(text, user.getUsername(), 0); //let 0 represent some userID

                if(text.equals("!exitChat")){ //this sends a logout message to the server
                    message = new Message(MainType.AUTHENTICATION, SubType.LOGOUT, Status.REQUEST, textMessage.getText());
                    messageHistory.add(message);
                }
                else { //normal text being sent
                     //user input is used to make textMessage object
                    message = new Message(MainType.TEXT, SubType.SEND_TEXT_MESSAGE , Status.REQUEST, textMessage.getText());
                    messageHistory.add(message); //the message the user input should be sent
                    // textMessageHistory.add(textMessage); add the text message to the text message history array | This isnt needed (likely will use this in communication system)
                }

                objectOutputStream.writeObject(message); //where the object gets serialized and sent

                Message incomingServerMessages = (Message) objectInputStream.readObject();
                System.out.println("\nServer side echo " + clientSideSocket.getInetAddress().getHostAddress() + ": " + incomingServerMessages.getText() + '\n'); //display message along with who its from
                messageHistory.add(incomingServerMessages);
            }

            message = (Message) objectInputStream.readObject();
            if(message.subType == SubType.LOGOUT) {
                System.out.println("Logging out!"); //after user logs out we can close the client side socket
                clientSideSocket.close();
            }
            else System.out.println("We got a bug");
        }
    }
}
