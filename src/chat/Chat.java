package chat;
import java.time.Instant;

public class Chat {
	private TextMessage[] messages;
	private int numMessages;
	private int[] memberIds;
	private int numMembers;
	private ChatType chatType;
	private int chatCreatorId;
	private static int count = 1;
	private int chatId;
	private Instant newestUpdate;
	
	public Chat(int creatorId, int[] memberIds, ChatType type) {
		//Might need to Change this for dynamic arrays
		numMembers = memberIds.length;
		this.memberIds = memberIds;
		//
		chatCreatorId = creatorId;
		chatType = type;
		newestUpdate = Instant.now();
		chatId = count++;
		messages = new TextMessage[50];
		numMessages = 0;
	}
	
	//Will need to figure out at what level of abstraction to load files
	//public Chat(String file) {}

	//inserts a message at the end of the message array
	public void addMessage(TextMessage message) {
		if(numMessages >= messages.length) { // makes space if need be (2x)
			TextMessage[] newMessages = new TextMessage[messages.length * 2];
			for(int i = 0; i < messages.length; i++) {
				newMessages[i] = messages[i];
			}
			messages = newMessages;
		}
		//inserts the new message and updates the timestamp for the chat
		messages[numMessages++] = message;
		newestUpdate = message.getTimestamp();
	}
	
	public TextMessage getMessage(int messageIndex) {
		return messages[messageIndex];
	}
	

	//adds a new member to the chat
	public void addMember(int memberId) {
		if(numMembers >= memberIds.length) { //makes space if need be (2x)
			int[] newMemberIds = new int[memberIds.length * 2];
			for(int i = 0; i < memberIds.length; i++) {
				newMemberIds[i] = memberIds[i];
			}
			memberIds = newMemberIds;
		}
		//insert the member id
		memberIds[numMembers++] = memberId;
	}
	
	public int getMemberId(int memberIndex) {
		return memberIds[memberIndex];
	}
	

	//removes the member from the chat
	public void removeMember(int memberId) {
		if(memberId == chatCreatorId)
			throw new IllegalArgumentException();
		int indexInArray = 0;
		while(indexInArray < numMembers) { //attempts to find the member
			if(memberIds[indexInArray] == memberId) {
				break;
			}
			indexInArray++;
		}
		if(indexInArray >= numMembers) {
			return; //return if not found
		}
		//shift array down if found
		for(int i = indexInArray; i < (numMembers - 1); i++) {
			memberIds[i] = memberIds[i+1];
		}
		numMembers--; //decrement
	}

	//getters
	public TextMessage getMessage(int messageIndex) {
		if(messageIndex >= numMessages || messageIndex < 0)
			throw new IndexOutOfBoundsException();
		return messages[messageIndex];
	}
	public int getMemberId(int memberIndex) {
		if(memberIndex >= numMembers || memberIndex < 0)
			throw new IndexOutOfBoundsException();
		return memberIds[memberIndex];
	}
	
	public int getCreatorId() {
		return chatCreatorId;
	}
	
	public int getChatId() {
		return chatId;
	}
	
	public ChatType getChatType() {
		return chatType;
	}
	
	public Instant getNewestUpdate() {
		return newestUpdate;
	}
	
	//returns string in the format:
	//member1_id,member2_id,member3_id
	//chat_type
	//chat_timestamp
	//creator_id
	//message1_userid,message1_username,message1_text,message1_timestamp
	//message2_userid,message2_username,message2_text,message2_timestamp
	//message3_userid,message3_username,message3_text,message3_timestamp
	//...
	//messageN_userid,messageN_username,messageN_text,messageN_timestamp
	public String toString() {
		String retStr = "";
		//member_ids
		for(int i = 0; i < numMembers; i++) {
			if(i != 0)
				retStr += ',';
			retStr += memberIds[i];
		}
		retStr += '\n';
		//chat type
		if(chatType == ChatType.PRIVATE) {
			retStr += "PRIVATE";
		}
		if(chatType == ChatType.GROUP) {
			retStr += "GROUP";
		}
		retStr += '\n';
		//chat timestamp
		retStr += newestUpdate;
		retStr += '\n';
		//chat creator id
		retStr += chatCreatorId;
	
		//chat messages
		for(int i = 0; i < numMessages; i++) {
			TextMessage message = messages[i];
			retStr += ('\n' + message.getUserId() + ','
					+ message.getUsername() + ','
					+ message.getText() + ','
					+ message.getTimestamp());
		}
		return retStr;
	}
}