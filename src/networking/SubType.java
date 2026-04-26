package networking;
public enum SubType {
    LOGIN, LOGOUT, LOGIN_RESPONSE, //authentication
    OPEN_CHAT, CHAT_LIST, CHAT_USER, CREATE_GC, ADD_USER_TO_GC, REMOVE_USER_FROM_GC, DELETE_GC, //chat operations
    SEND_TEXT_MESSAGE, // Text
    ACTUAL_CHAT, USER_STATE, //display
    ENTER_AUDIT_MODE, SELECT_USER, VIEW_CHATS, EXPORT_CHAT_LOG, //Audit operations
    UNDEFINED //in case the operation for whatever reason is not specified
}
