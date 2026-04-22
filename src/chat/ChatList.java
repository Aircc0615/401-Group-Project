package chat;
import java.time.Instant;

public class ChatList {
	private Chat[] chats;
	private int numChats;
	public ChatList() {
		//Change this
		chats = new Chat[8];
		numChats = 0;
	}
	public void addChat(Chat chat) {
		if(numChats >= chats.length) {
			Chat[] newChats = new Chat[chats.length * 2];
			for(int i = 0; i <  chats.length; i++) {
				newChats[i] = chats[i];
			}
			chats = newChats;
		}
		int arrayIndex;
		for(arrayIndex = 0; arrayIndex < numChats; arrayIndex++) {
			Chat checkChat = chats[arrayIndex];
			if(checkChat.getNewestUpdate().compareTo(chat.getNewestUpdate()) >= 0)
				break;
		}
		for(int i = numChats; i > arrayIndex; i--) {
			chats[i] = chats[i-1];
		}
		chats[arrayIndex] = chat;
		numChats++;
	}
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
	public void addChatMessage(int chatIndex, TextMessage message) {
		chats[chatIndex].addMessage(message);
	}
	public void addChatMember(int chatIndex, int memberId, int fromId) {
		Chat chat = chats[chatIndex];
		if(chat.getChatType() == ChatType.PRIVATE)
			return;
		if(chat.getCreatorId() != fromId)
			return;
		chat.addMember(memberId);
	}
	public void removeChatMember(int chatIndex, int memberId, int fromId) {
		Chat chat = chats[chatIndex];
		if(chat.getChatType() == ChatType.PRIVATE)
			return;
		if(chat.getCreatorId() != fromId)
			return;
		chat.removeMember(memberId);
	}
	public void deleteChat(int chatId, int fromId) {
		int indexInArray = 0;
		while(indexInArray < numChats) {
			if(chats[indexInArray].getChatId() == chatId) {
				break;
			}
			indexInArray++;
		}
		if(indexInArray >= numChats) {
			return;
		}
		for(int i = indexInArray; i < (numChats - 1); i++) {
			chats[i] = chats[i+1];
		}
		numChats--;
	}
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