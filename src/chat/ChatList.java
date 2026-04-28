package chat;

import java.time.Instant;

//might need to add a semaphore for addchat/deletechat synchronization
public class ChatList {
	private Chat[] chats;
	private int numChats;
	private Object writeMutex;

	public ChatList() {
		// default chat size
		chats = new Chat[8];
		numChats = 0;
		writeMutex = new Object();
	}

	// inserts a chat in the array based on the order of the timestamp
	public void addChat(Chat chat) {
		synchronized (writeMutex) {
			Chat[] tempChats = getCopyOfChats();
			// makes more space if needed (2x)
			if (numChats >= tempChats.length) {
				Chat[] newChats = new Chat[tempChats.length * 2];
				for (int i = 0; i < tempChats.length; i++) {
					newChats[i] = tempChats[i];
				}
				tempChats = newChats;
			}

			tempChats[numChats] = chat;
			reorderList(tempChats, numChats);
			chats = tempChats;
			numChats++;
		}
	}

	//Methods for server to call when server chat list invites users to a chat
	//Insert chat to 1 list
	public void insertChatToOneList(ChatList otherChatList, int chatId) {
		int chatIndex = parseId(chats, chatId);
		if(chatIndex == -1)
			throw new IndexOutOfBoundsException();
		otherChatList.addChat(chats[chatIndex]);
	}
	//Insert chat to multiple lists
	public void insertChatToMultipleLists(ChatList[] otherChatLists, int chatId) {
		int chatIndex = parseId(chats, chatId);
		if(chatIndex == -1)
			throw new IndexOutOfBoundsException();
		for(ChatList otherChatList : otherChatLists) {
			if(otherChatList == null)
				break;
			otherChatList.addChat(chats[chatIndex]);
		}
	}
	//

	// getters
	public TextMessage getChatMessage(int chatId, int messageIndex) {
		int chatIndex = parseId(chats, chatId);
		if(chatIndex == -1)
			throw new IndexOutOfBoundsException();
		return chats[chatIndex].getMessage(messageIndex);
	}

	public int getChatMemberId(int chatId, int memberIndex) {
		int chatIndex = parseId(chats, chatId);
		if(chatIndex == -1)
			throw new IndexOutOfBoundsException();
		return chats[chatIndex].getMemberId(memberIndex);
	}

	public Instant getChatNewestUpdate(int chatId) {
		int chatIndex = parseId(chats, chatId);
		if(chatIndex == -1)
			throw new IndexOutOfBoundsException();
		return chats[chatIndex].getNewestUpdate();
	}

	// add message
	public void addChatMessage(int chatId, TextMessage message) {
		synchronized (writeMutex) {
			Chat[] tempChats = getCopyOfChats();
			int chatIndex = parseId(tempChats, chatId);
			if(chatIndex == -1)
				throw new IndexOutOfBoundsException();
			tempChats[chatIndex].addMessage(message);
			reorderList(tempChats, chatIndex);
			chats = tempChats;
		}
	}

	// attempt to add a member to a chat
	public void addChatMember(int chatId, int memberId, int fromId) {
		// confirm that the user is the chat owner in a group chat
		int chatIndex = parseId(chats, chatId);
		if(chatIndex == -1)
			throw new IndexOutOfBoundsException();
		Chat chat = chats[chatIndex];
		if (chat.getChatType() == ChatType.PRIVATE)
			return;
		if (chat.getCreatorId() != fromId)
			return;
		chat.addMember(memberId); // add member
	}

	// attempt to remove a member from a chat
	public void removeChatMember(int chatId, int memberId, int fromId) {
		// confirm that the user is the chat owner in a group chat
		int chatIndex = parseId(chats, chatId);
		if(chatIndex == -1)
			throw new IndexOutOfBoundsException();
		Chat chat = chats[chatIndex];
		if (chat.getChatType() == ChatType.PRIVATE)
			return;
		if (chat.getCreatorId() != fromId)
			return;
		chat.removeMember(memberId); // remove member
	}

	// Attempts the delete the chat with id "chatId"
	public void deleteChat(int chatId, int fromId) {
		synchronized (writeMutex) {
			Chat[] tempChats = getCopyOfChats();
			int indexInArray = parseId(tempChats, chatId);
			if (indexInArray == -1) {
				return; // Do nothing if not in array
			}
			// confirm that the user is the chat owner in a group chat
			Chat chat = tempChats[indexInArray];
			if (chat.getChatType() == ChatType.PRIVATE)
				return;
			if (chat.getCreatorId() != fromId)
				return;
			// remove the chat
			for (int i = indexInArray; i < (numChats - 1); i++) {
				tempChats[i] = tempChats[i + 1]; // shift array down 1
			}
			chats = tempChats;
			numChats--; // decrement numchats
		}
	}

	// returns a string of all chat ids in the list separated by ','
	public String toString() {
		String retStr = "";
		Chat[] tempChats = chats;
		for (int i = 0; i < numChats; i++) {
			if (i != 0)
				retStr += ',';
			retStr += tempChats[i].getChatId();
		}
		return retStr;
	}

	public int[] getChatIds() {
		int[] chatIds = new int[numChats];
		Chat[] tempChats = chats;
		for (int i = 0; i < numChats; i++) {
			chatIds[i] = tempChats[i].getChatId();
		}
		return chatIds;
	}

	public void updateOrder(int chatId) {
		synchronized (writeMutex) {
			Chat[] tempChats = getCopyOfChats();
			int chatIndex = parseId(tempChats, chatId);
			if(chatIndex == -1)
				return;
			reorderList(tempChats, chatIndex);
			chats = tempChats;
		}
	}

	private void reorderList(Chat[] tempChats, int updatedChatIndex) {
		if (updatedChatIndex == 0)
			return;
		int i;
		for (i = 0; i < updatedChatIndex; i++) {
			if (tempChats[i].getNewestUpdate().compareTo(tempChats[updatedChatIndex].getNewestUpdate()) <= 0)
				break;
		}
		if (i == updatedChatIndex)
			return;
		Chat tempChat = tempChats[updatedChatIndex];
		for (int j = updatedChatIndex; j > i; j--) {
			tempChats[j] = tempChats[j - 1];
		}
		tempChats[i] = tempChat;
	}

	private Chat[] getCopyOfChats() {
		Chat[] newChats = new Chat[chats.length];
		for (int i = 0; i < numChats; i++) {
			newChats[i] = chats[i];
		}
		return newChats;
	}

	private int parseId(Chat[] chatsToParse, int chatId) {
		for(int i = 0; i < numChats; i++) {
			if(chatsToParse[i].getChatId() == chatId) {
				return i;
			}
		}
		return -1;
	}
}
