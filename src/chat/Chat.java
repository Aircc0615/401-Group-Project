package chat;

import java.io.Serializable;
import java.time.Instant;

public class Chat implements Serializable {
	private TextMessage[] messages;
	private int numMessages;
	private String[] memberUsernames;
	private int numMembers;
	private final ChatType chatType;
	private final String creatorUsername;
	private static int count = 1;
	private final int chatId;
	private Instant newestUpdate;
	private Object mutexObject;

	public Chat(String creatorUsername, String[] memberUsernames, ChatType type) {
		// Might need to Change this for dynamic arrays
		numMembers = memberUsernames.length;
		this.memberUsernames = memberUsernames;
		//
		this.creatorUsername = creatorUsername;
		chatType = type;
		newestUpdate = Instant.now();
		chatId = count++;
		messages = new TextMessage[50];
		numMessages = 0;
		mutexObject = new Object();
	}

	private Chat(TextMessage[] messages, int numMessages, String[] memberUsernames, 
			int numMembers, ChatType chatType,
			String creatorUsername, int chatId, Instant newestUpdate) {
		this.messages = messages;
		this.numMessages = numMessages;
		this.memberUsernames = memberUsernames;
		this.numMembers = numMembers;
		this.chatType = chatType;
		this.creatorUsername = creatorUsername;
		this.chatId = chatId;
		this.newestUpdate = newestUpdate;
		mutexObject = new Object();
	}

	// Will need to figure out at what level of abstraction to load files
	// public Chat(String file) {}

	// inserts a message at the end of the message array
	public void addMessage(TextMessage message) {
		synchronized (mutexObject) {
			if (numMessages >= messages.length) { // makes space if need be (2x)
				TextMessage[] newMessages = new TextMessage[messages.length * 2];
				for (int i = 0; i < messages.length; i++) {
					newMessages[i] = messages[i];
				}
				messages = newMessages;
			}
			int messageIndex;
			for(messageIndex = 0; messageIndex < numMessages; messageIndex++) {
				if(message.getTimestamp()
						.compareTo(messages[messageIndex].getTimestamp()) <= 0)
					break;
			}
			for(int i = numMessages; i > messageIndex; i--) {
				messages[i] = messages[i-1];
			}
			// inserts the new message and updates the timestamp for the chat
			messages[messageIndex] = message;
			if(message.getTimestamp().compareTo(newestUpdate) > 0) {
				newestUpdate = message.getTimestamp();
			}
			numMessages++;
		}
	}

	public TextMessage getMessage(int messageIndex) {
		synchronized (mutexObject) {
			if (messageIndex >= numMessages || messageIndex < 0)
				throw new IndexOutOfBoundsException();
			return messages[messageIndex];
		}
	}

	// adds a new member to the chat
	public void addMember(String username) {
		synchronized (mutexObject) {
			if (numMembers >= memberUsernames.length) { // makes space if need be (2x)
				String[] newMemberUsernames = new String[memberUsernames.length * 2];
				for (int i = 0; i < memberUsernames.length; i++) {
					newMemberUsernames[i] = memberUsernames[i];
				}
				memberUsernames = newMemberUsernames;
			}
			// insert the member username
			memberUsernames[numMembers++] = username;
		}
	}

	public boolean memberIsInChat(String username) {
		synchronized (mutexObject) {
			for(int i = 0; i < numMembers; i++) {
				if(username == memberUsernames[i])
					return true;
			}
			return false;
		}
	}

	public String[] getMembersInChat() {
		synchronized (mutexObject) {
			String[] membersCopy = new String[numMembers];
			for(int i = 0; i < numMembers; i++) {
				membersCopy[i] = memberUsernames[i];
			}
			return membersCopy;
		}
	}

	// removes the member from the chat
	public void removeMember(String username) {
		synchronized (mutexObject) {
			if (username == creatorUsername)
				throw new IllegalArgumentException();
			int indexInArray = 0;
			while (indexInArray < numMembers) { // attempts to find the member
				if (memberUsernames[indexInArray] == username) {
					break;
				}
				indexInArray++;
			}
			if (indexInArray >= numMembers) {
				return; // return if not found
			}
			// shift array down if found
			for (int i = indexInArray; i < (numMembers - 1); i++) {
				memberUsernames[i] = memberUsernames[i + 1];
			}
			numMembers--; // decrement
		}
	}

	// getters

	public String getCreatorUsername() {
		return creatorUsername;
	}

	public int getChatId() {
		return chatId;
	}

	public ChatType getChatType() {
		return chatType;
	}

	public Instant getNewestUpdate() {
		synchronized (mutexObject) {
			return newestUpdate;
		}
	}

	// returns string in the format:
	// member1_id,member2_id,member3_id
	// chat_type
	// chat_timestamp
	// creator_id
	// message1_userid,message1_username,message1_text,message1_timestamp
	// message2_userid,message2_username,message2_text,message2_timestamp
	// message3_userid,message3_username,message3_text,message3_timestamp
	// ...
	// messageN_userid,messageN_username,messageN_text,messageN_timestamp
	public String toString() {
		synchronized (mutexObject) {
			String retStr = "";
			// member_ids
			for (int i = 0; i < numMembers; i++) {
				if (i != 0)
					retStr += ',';
				retStr += memberUsernames[i];
			}
			retStr += '\n';
			// chat type
			if (chatType == ChatType.PRIVATE) {
				retStr += "PRIVATE";
			}
			if (chatType == ChatType.GROUP) {
				retStr += "GROUP";
			}
			retStr += '\n';
			// chat timestamp
			retStr += newestUpdate;
			retStr += '\n';
			// chat creator id
			retStr += creatorUsername;

			// chat messages
			for (int i = 0; i < numMessages; i++) {
				TextMessage message = messages[i];
				retStr += ('\n' + message.getUserId() + ',' + message.getUsername() + ',' + message.getText() + ','
				    + message.getTimestamp());
			}
			return retStr;
		}
	}

	public int getNumMessages() {
		synchronized (mutexObject) {
			return numMessages;
		}
	}

	public int getNumMembers() {
		synchronized (mutexObject) {
			return numMembers;
		}
	}

	public Chat getCopy() {
		synchronized (mutexObject) {
			TextMessage[] copyMessages = new TextMessage[numMessages];
			for (int i = 0; i < numMessages; i++) {
				copyMessages[i] = messages[i];
			}
			String[] copyMembers = new String[numMembers];
			for (int i = 0; i < numMembers; i++) {
				copyMembers[i] = memberUsernames[i];
			}
			return new Chat(copyMessages, numMessages, copyMembers, numMembers, chatType, creatorUsername, chatId,
			    newestUpdate);
		}
	}
}