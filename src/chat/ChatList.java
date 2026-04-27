
	package chat;
import java.time.Instant;

public class ChatList {
	private Chat[] chats;
	private int numChats;
	
	public ChatList() {
		//default chat size
		chats = new Chat[8];
		numChats = 0;
	}
	

	//inserts a chat in the array based on the order of the timestamp
	public void addChat(Chat chat) {
		//makes more space if needed (2x)
		if(numChats >= chats.length) {
			Chat[] newChats = new Chat[chats.length * 2];
			for(int i = 0; i <  chats.length; i++) {
				newChats[i] = chats[i];
			}
			chats = newChats;
		}
		//find the index based on the chat timestamp order
		int arrayIndex;
		for(arrayIndex = 0; arrayIndex < numChats; arrayIndex++) {
			Chat checkChat = chats[arrayIndex];
			if(checkChat.getNewestUpdate().compareTo(chat.getNewestUpdate()) <= 0)
				break;
		}
		//shift array up
		for(int i = numChats; i > arrayIndex; i--) {
			chats[i] = chats[i-1];
		}
		//insert chat and increment num chats
		chats[arrayIndex] = chat;
		numChats++;
	}
	

	//getters
	public TextMessage getChatMessage(int chatIndex, int messageIndex) {
		return chats[chatIndex].getMessage(messageIndex);
	}
	
	public int getChatMemberId(int chatIndex, int memberIndex) {
		return chats[chatIndex].getMemberId(memberIndex);
	}
	
	public Instant getChatNewestUpdate(int chatIndex) {
		return chats[chatIndex].getNewestUpdate();
	}
	
	public Chat getChat(int chatIndex) {
		return chats[chatIndex];
	}
	

	//add message
	public void addChatMessage(int chatIndex, TextMessage message) {
		chats[chatIndex].addMessage(message);
	}

	//attempt to add a member to a chat
	public void addChatMember(int chatIndex, int memberId, int fromId) {
		//confirm that the user is the chat owner in a group chat
		Chat chat = chats[chatIndex];
		if(chat.getChatType() == ChatType.PRIVATE)
			return;
		if(chat.getCreatorId() != fromId)
			return;
		chat.addMember(memberId); //add member
	}
	

	//attempt to remove a member from a chat
	public void removeChatMember(int chatIndex, int memberId, int fromId) {
		//confirm that the user is the chat owner in a group chat
		Chat chat = chats[chatIndex];
		if(chat.getChatType() == ChatType.PRIVATE)
			return;
		if(chat.getCreatorId() != fromId)
			return;
		chat.removeMember(memberId); //remove member
	}
	

	//Attempts the delete the chat with id "chatId"
	public void deleteChat(int chatId, int fromId) {
		int indexInArray = 0; // attempts to find the chat to delete
		while(indexInArray < numChats) {
			if(chats[indexInArray].getChatId() == chatId) {
				break; // break if find
			}
			indexInArray++;
		}
		if(indexInArray >= numChats) {
			return; // Do nothing if not in array
		}
		//confirm that the user is the chat owner in a group chat
		Chat chat = chats[indexInArray];
		if(chat.getChatType() == ChatType.PRIVATE)
			return;
		if(chat.getCreatorId() != fromId)
			return;
		//remove the chat
		for(int i = indexInArray; i < (numChats - 1); i++) {
			chats[i] = chats[i+1]; // shift array down 1
		}
		numChats--; // decrement numchats
	}
	

	//returns a string of all chat ids in the list separated by ','
	public String toString() {
		String retStr = "";
		for(int i = 0; i < numChats; i++) {
			if(i != 0)
				retStr += ',';
			retStr += chats[i].getChatId();
		}
		return retStr;
	}
}