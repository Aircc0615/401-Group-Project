package chat;
import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class ChatMessageTest{
	private static String[] memberIds = {"1", "2", "3","4", "5", "6"};
	private static String creatorId = "1";
	private Chat chat;

	@BeforeEach
	void initializeTestConditions() {
		chat = new Chat(creatorId, memberIds, ChatType.GROUP);
	}

	@Test
	void chatAddMessageToChat() {
		TextMessage message = new TextMessage("test text", "user1", Integer.parseInt(creatorId));
		chat.addMessage(message);
		assertSame(message, chat.getMessage(0));
	}

	@Test
	void chatAdd50MessagesToChat() {
		TextMessage[] messageList = new TextMessage[50];
		TextMessage mes;
		for(int i = 0; i < 50; i++) {
			mes = new TextMessage("test text" + i, "user1", Integer.parseInt(creatorId));
			messageList[i] = mes;
			chat.addMessage(mes);
		}
		assertAll(
			() -> {
				for(int i = 0; i < 50; i++) {
					assertSame(messageList[i], chat.getMessage(i));
				}
			});
	}

	@Test
	void chatSavesMostRecentTimestamp() {
		TextMessage[] messageList = new TextMessage[5];
		for(int i = 0; i < 5; i++) {
			messageList[i] = new TextMessage("test text" + i, "user1", Integer.parseInt(creatorId));
		}
		chat.addMessage(messageList[0]);
		chat.addMessage(messageList[3]);
		chat.addMessage(messageList[2]);
		chat.addMessage(messageList[4]);
		chat.addMessage(messageList[1]);
		assertEquals(chat.getNewestUpdate(), messageList[4].getTimestamp());
	}

	@Test
	void chatMessagesSortedByTime() {
		TextMessage[] messageList = new TextMessage[5];
		for(int i = 0; i < 5; i++) {
			messageList[i] = new TextMessage("test text" + i, "user1", Integer.parseInt(creatorId));
		}
		chat.addMessage(messageList[0]);
		chat.addMessage(messageList[3]);
		chat.addMessage(messageList[2]);
		chat.addMessage(messageList[4]);
		chat.addMessage(messageList[1]);
		assertAll(
			() -> {
				for(int i = 0; i < 4; i++) {
					assertTrue(chat.getMessage(i).getTimestamp()
							.compareTo(chat.getMessage(i + 1).getTimestamp()) < 0);
				}
			});
	}

	@Test
	void chatMessageInvalidMessageIndex() {
		chat.addMessage(new TextMessage("test text", "user1", Integer.parseInt(creatorId)));
		assertThrows(IndexOutOfBoundsException.class, 
				() -> chat.getMessage(1));
	}

	@Test
	void chatMessageNegativeMessageIndex() {
		assertThrows(IndexOutOfBoundsException.class, 
				() -> chat.getMessage(-1));
	}
}
