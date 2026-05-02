package user;

import java.io.Serializable;
import java.util.Date;
import chat.Chat;
import chat.ChatList;

public class User implements Serializable {
    private static int count = 0;
    private int id;
    private static final int minLength= 6;
    private static final int maxLength = 20;

    private String username;
    private String password;
    private boolean online;
    private boolean isITUser;
    private boolean auditMode;
    private String sessionToken;
    private Date lastLogin;
    
    private ChatList chatList;
    private ChatList unreadChatList;


    public User() {
        id = count++;
        this.username = "undefined";
        this.password = "undefined";
        this.online = false;
        this.isITUser = false;
        this.auditMode = false;
        this.sessionToken = "";
        this.lastLogin = null;
        this.chatList = new ChatList();
        this.unreadChatList = new ChatList();
    }

    public User(String username, String password) {
    	id = count++;
        this.username = username;
        this.password = password;
        this.online = false;
        this.isITUser = false;
        this.auditMode = false;
        this.sessionToken = "";
        this.lastLogin = null;
        this.chatList = new ChatList();
        this.unreadChatList = new ChatList();
    }

    public User(String username, String password, boolean isITUser) {
        this.username = username;
        this.password = password;
        this.online = false;
        this.isITUser = isITUser;
        this.auditMode = false;
        this.sessionToken = "";
        this.lastLogin = null;
        this.chatList = new ChatList();
        this.unreadChatList = new ChatList();
    }

    
    
    public boolean authenticateLogin(String username, String password) {
        if (checkFormat(username) == false|| checkFormat(password) == false) {
            return false;
        }

        if (this.username.equals(username) && this.password.equals(password)) {
            online = true;
            auditMode = false;
            sessionToken = "SESSION" + username + "-" + System.currentTimeMillis();
            lastLogin = new Date();
            return true;
        }

        return false;
    }

    
    
    
    
    
    public void logout() {
        online = false;
        auditMode = false;
        sessionToken = "";
    }

   
    
    
    
    
    
    public boolean enableAuditMode(String sessionToken) {
        if (isITUser ==false|| online == false) {
            return false;
        }

        if (this.sessionToken.equals(sessionToken)) {
            setAuditMode(true);
            return true;
        }

        return false;
    }

  
    
    
    
    
    public void setAuditMode(boolean auditMode) {
        if (auditMode ==true && isITUser == true && online == true) {
            this.auditMode = true;
        } else {
            this.auditMode = false;
        }
    }

   
    
    
    
    
    public boolean ViewChat(Chat chat) {
        if(chat == null){
            return false;
        }

       
       
        if (isITUser == true && auditMode == true) {
            return true;
        }


        return hasChat(chat);
    }

    
   
    
    
    
    
    
    public boolean hasChat(Chat chat) {
        if(chat == null){
            return false;
        }

        return hasChat(chat.getChatId());
    }

    public boolean hasChat(int chatId){
        return containsChat(chatList,chatId);
    }

   
    public void addChat(Chat chat){
        if(chat == null){
            return;
        }
        if(hasChat(chat)==true){
            return;
        }
        chatList.addChat(chat);
    }
    
    
    
    
    public void removeChat(Chat chat, String fromUsername) {
        if(chat == null){
            return;
        }
        removeChat(chat.getChatId(), fromUsername);
    }

    public void removeChat(int chatId, String fromUsername) {
        chatList.deleteChat(chatId, fromUsername);
        unreadChatList.deleteChat(chatId, true);
    }

    
    
    
    
    
    
    
    
    public void markChatAsUnread(Chat chat) {
      if(chat==null){
        return;
      }
      if(ViewChat(chat)==false){
        return;
      }
      if(hasUnreadChat(chat)==true){
        return;
      }
      markChatAsUnread(chat.getChatId());
    }

   
    
    public void markChatAsUnread(int chatId) {
    	chatList.insertChatToOneList(unreadChatList, chatId);
    }
    
    
    
    
    
    public void markChatAsRead(Chat chat) {
        if(chat == null){
            return;
        }
        markChatAsRead(chat.getChatId());
    }

     public void markChatAsRead(int chatId) {
        unreadChatList.deleteChat(chatId, true);
    }

    
    
    
    
    public boolean hasUnreadMessages() {
        return getUnreadChatCount() > 0;
    }

   
    
    
    
    
    
    public boolean hasUnreadChat(Chat chat) {
        if(chat == null){
            return false;
        }

        return hasUnreadChat(chat.getChatId());
    }

    public boolean hasUnreadChat(int chatId) {
        return containsChat(unreadChatList,chatId);
    }
    
    public void removeChatMember(Chat chat,User member, String fromUsername){
        if(chat == null || member == null){
            return;
        }

        removeChatMember(chat.getChatId(), member.getUsername(), fromUsername);
    }

    public void removeChatMember(int chatId, String memberUsername, String fromUsername){
        chatList.removeChatMember(chatId,memberUsername, fromUsername);
    }
    
    
    
    public boolean hasValidCredential() {
        return checkFormat(username) && checkFormat(password);
    }

    
    
    
    
    
    
    
    
    
    public static boolean checkFormat(String credential) {
        int i;

        if (credential == null) {
            return false;
        }

        if (credential.length() < minLength || credential.length() > maxLength) {
            return false;
        }

        for (i = 0; i < credential.length(); i++) {
            if (Character.isLetterOrDigit(credential.charAt(i))==false) {
                return false;
            }
        }

        return true;
    }

   
    
    
    
    private boolean containsChat(ChatList list, int chatId){
        int[] chatIds;
        int i;

        if (list == null) {
            return false;
        }

        chatIds = list.getChatIds();

        for (i = 0; i < chatIds.length; i++) {
            if (chatIds[i] == chatId) {
                return true;
            }
        }

        return false;
    }

    



  
    
    
    
    
    
    
    

    public String getUsername() {
        return this.username;
    }

    public String getPassword() {
        return this.password;
    }

    public boolean isOnline() {
        return this.online;
    }

    public boolean isInformationTechnologyUser() {
        return this.isITUser;
    }

    public boolean isAuditMode() {
        return this.auditMode;
    }

    public String getSessionToken() {
        return this.sessionToken;
    }

    public Date getLastLogin() {
        return this.lastLogin;
    }

    public String getRole() {
        if (this.isITUser == true) {
            return "IT";
        }

        return "USER";
    }

    public ChatList getChatList() {
        return this.chatList;
    }


    public ChatList getUnreadChatList() {
        return this.unreadChatList;
    }

    public int getChatCount(){
        return chatList.getChatIds().length;
    }



    public int getUnreadChatCount() {
        return unreadChatList.getChatIds().length;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public void setOnline(boolean online) {
        this.online = online;

        if (online == false) {
            auditMode = false;
            sessionToken = "";
        }
    }

    public void setITUser(boolean isITUser) {
        this.isITUser = isITUser;

        if (isITUser == false) {
            auditMode = false;
        }
    }

	public int getId() {
		// TODO Auto-generated method stub
		return id;
	}
}
