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
	public void addMessage(TextMessage message) {
		if(numMessages >= messages.length) {
			TextMessage[] newMessages = new TextMessage[messages.length * 2];
			for(int i = 0; i < messages.length; i++) {
				newMessages[i] = messages[i];
			}
			messages = newMessages;
		}
		messages[numMessages++] = message;
		newestUpdate = message.getTimestamp();
	}
	public TextMessage getMessage(int messageIndex) {
		return messages[messageIndex];
	}
	public void addMember(int memberId) {
		if(numMembers >= memberIds.length) {
			int[] newMemberIds = new int[memberIds.length * 2];
			for(int i = 0; i < memberIds.length; i++) {
				newMemberIds[i] = memberIds[i];
			}
			memberIds = newMemberIds;
		}
		memberIds[numMembers++] = memberId;
	}
	public int getMemberId(int memberIndex) {
		return memberIds[memberIndex];
	}
	public void removeMember(int memberId) {
		int indexInArray = 0;
		while(indexInArray < numMembers) {
			if(memberIds[indexInArray] == memberId) {
				break;
			}
			indexInArray++;
		}
		if(indexInArray >= numMembers) {
			return;
		}
		for(int i = indexInArray; i < (numMembers - 1); i++) {
			memberIds[i] = memberIds[i+1];
		}
		numMembers--;
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
	public String toString() {
		String retStr = "";
		for(int i = 0; i < numMembers; i++) {
			if(i != 0)
				retStr += ',';
			retStr += memberIds[i];
		}
		retStr += '\n';
		if(chatType == ChatType.PRIVATE) {
			retStr += "PRIVATE";
		}
		if(chatType == ChatType.GROUP) {
			retStr += "GROUP";
		}
		retStr += '\n';
		retStr += newestUpdate;
		retStr += '\n';
		retStr += chatCreatorId;
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