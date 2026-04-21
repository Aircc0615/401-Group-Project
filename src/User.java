import java.io.Serializable;
import java.util.Date;

public class User implements Serializable {
	private static int count = 0;
    private static final int minLength= 6;
    private static final int maxLength = 20;
    private static final int maxChats = 100;

    private int id;
    private String username;
    private String password;
    private boolean online;
    private boolean isITUser;
    private boolean auditMode;
    private String sessionToken;
    private Date lastLogin;
    private int[] chatIds;
    private int chatCount;
    private int[] unreadChatIds;
    private int unreadChatCount;

    public User() {
        this.id = count++;
        this.username = "undefined";
        this.password = "undefined";
        this.online = false;
        this.isITUser = false;
        this.auditMode = false;
        this.sessionToken = "";
        this.lastLogin = null;
        this.chatIds = new int[maxChats];
        this.chatCount = 0;
        this.unreadChatIds = new int[maxChats];
        this.unreadChatCount = 0;
    }

    public User(String username, String password) {
        this.id = count++;
        this.username = username;
        this.password = password;
        this.online = false;
        this.isITUser = false;
        this.auditMode = false;
        this.sessionToken = "";
        this.lastLogin = null;
        this.chatIds = new int[maxChats];
        this.chatCount = 0;
        this.unreadChatIds = new int[maxChats];
        this.unreadChatCount = 0;
    }

    public User(String username, String password, boolean isITUser) {
        id = count++;
        this.username = username;
        this.password = password;
        this.online = false;
        this.isITUser = isITUser;
        this.auditMode = false;
        this.sessionToken = "";
        this.lastLogin = null;
        this.chatIds = new int[maxChats];
        this.chatCount = 0;
        this.unreadChatIds = new int[maxChats];
        this.unreadChatCount = 0;
    }

   
    
    
    
    
    
    
    
    
    
    
    
    public boolean authenticateLogin(String username, String password) {
        if (checkFormat(username) == false|| checkFormat(password) == false) {
            return false;
        }

        if (this.username.equals(username) && this.password.equals(password)) {
            online = true;
            auditMode = false;
            sessionToken = "SESSION" + id + "-" + System.currentTimeMillis();
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

   
    
    
    
    
    public boolean ViewChat(int chatId) {
        int i;

        if (isITUser == true && auditMode == true) {
            return true;
        }

        for (i = 0; i < chatCount; i++) {
            if (chatIds[i] == chatId) {
                return true;
            }
        }

        return false;
    }

    
   
    
    
    
    
    
    public void addChat(int chatId) {
        int i;

        for (i = 0; i < chatCount; i++) {
            if (chatIds[i] == chatId) {
                return;
            }
        }

        if (chatCount < maxChats) {
            chatIds[chatCount] = chatId;
            chatCount++;
        }
    }

   
    
    
    
    
    
    public void removeChat(int chatId) {
        removeChatFrom(chatIds, chatId, true);
        removeChatFrom(unreadChatIds, chatId, false);
    }

    
    
    
    
    
    
    
    
    public void markChatAsUnread(int chatId) {
        int i;

        if (ViewChat(chatId)==false) {
            return;
        }

        for (i = 0; i < unreadChatCount; i++) {
            if (unreadChatIds[i] == chatId) {
                return;
            }
        }

        if (unreadChatCount < maxChats) {
            unreadChatIds[unreadChatCount] = chatId;
            unreadChatCount++;
        }
    }

    
    
    
    
    
    public void markChatAsRead(int chatId) {
        removeChatFrom(unreadChatIds, chatId, false);
    }

    
    
    
    
    public boolean hasUnreadMessages() {
        return unreadChatCount > 0;
    }

   
    
    
    
    
    
    public boolean hasUnreadChat(int chatId) {
        int i;

        for (i = 0; i < unreadChatCount; i++) {
            if (unreadChatIds[i] == chatId) {
                return true;
            }
        }

        return false;
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

   
    
    
    
    
    private void removeChatFrom(int[] array, int chatId, boolean chatType) {
        int i;
        int limit;

        if (chatType == true) {
            limit = chatCount;
        } else {
            limit = unreadChatCount;
        }

        for (i = 0; i < limit; i++) {
            if (array[i] == chatId) {
                while (i < limit - 1) {
                    array[i] = array[i + 1];
                    i++;
                }

                array[limit - 1] = 0;

                if (chatType == true) {
                    chatCount--;
                } else {
                    unreadChatCount--;
                }

                return;
            }
        }
    }

  
    
    
    
    
    
    
    
    public int getId() {
        return this.id;
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

    public int[] getChats() {
        int[] chats = new int[chatCount];
        int i;

        for (i = 0; i < chatCount; i++) {
            chats[i] = chatIds[i];
        }

        return chats;
    }

    public int getChatCount() {
        return this.chatCount;
    }

    public int[] getUnreadChats() {
        int[] chats = new int[unreadChatCount];
        int i;

        for (i = 0; i < unreadChatCount; i++) {
            chats[i] = unreadChatIds[i];
        }

        return chats;
    }

    public int getUnreadChatCount() {
        return this.unreadChatCount;
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
}
