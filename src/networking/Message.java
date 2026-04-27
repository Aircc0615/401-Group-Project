package networking;
import java.io.Serializable;
import java.time.Instant;
import java.util.Date;

import user.User;

public class Message implements Serializable { //serialize takes the object and converts into stream of bytes
    private static int count = 0;
    private int id;
    protected MainType mainType;
    protected SubType subType;
    protected Status status;
    protected String text;
    protected Date date;
    protected Instant timeStamp;
    protected User user;
    
    private String username;
    private String password;
    private boolean success;
    private int userId;
    
    private String chatList;
    private String chatUserList;
    private String chatId;
    private String selectedUserId;
    private String messageContent;
    private String chatType;
    private String memberList;
    private String chatLog;

    public Message(){
        id = count++;
        this.mainType = MainType.UNDEFINED;
        this.subType = SubType.UNDEFINED;
        this.status = Status.UNDEFINED;
        this.text = "Undefined";
        date = new Date();
        timeStamp = Instant.now();
        user = null;
        
        username = "";
        password = "";
        success = false;
        userId = -1;
        
        chatList = "";
        chatUserList = "";
        chatId = "";
        selectedUserId = "";
        messageContent = "";
        chatType = "";
        memberList = "";
        chatLog = "";
    }

    public Message(MainType mainType, SubType subType, Status status, String text, User user){
        id = count++;
        this.mainType = mainType;
        this.subType = subType;
        this.status = status;
        this.text = text;
        date = new Date();
        timeStamp = Instant.now();
        this.user = user;
        
        username = "";
        password = "";
        success = false;
        userId = -1;
        
        chatList = "";
        chatUserList = "";
        chatId = "";
        selectedUserId = "";
        messageContent = "";
        chatType = "";
        memberList = "";
        chatLog = "";
    }

    public MainType getType(){
        return mainType;
    }

    public Status getStatus(){
        return status;
    }

    public String getText() {
        return text;
    }

    public int getId(){
        return id;
    }

    public MainType getMainType() { return mainType; }

    public SubType getSubType() { return subType; }

    public Date getDate() { return date; }

    public Instant getTimeStamp() { return timeStamp; }

	public User getUser() {
		return user;
	}
    
    public int getCount() { return count; }
    
    public String getUsername() { return username; }

    public String getPassword() { return password; }

    public boolean getSuccess() { return success; }

    public int getUserId() { return userId; }

    public String getChatList() { return chatList; }

    public String getChatUserList() { return chatUserList; }

    public String getChatId() { return chatId; }

    public String getSelectedUserId() { return selectedUserId; }

    public String getMessageContent() { return messageContent; }

    public String getChatType() { return chatType; }

    public String getMemberList() { return memberList; }

    public String getChatLog() { return chatLog; }
    
}
