package test.chat;
import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import src.chat.TextMessage;
import src.chat.ChatType;
import src.chat.Chat;

class ChatMessageTest{
	private static int[] memberIds = {1, 2, 3, 4, 5, 6};
	private static int creatorId = 1;
	private static Chat chat;

	@BeforeEach
	static void initializeTestConditions() {
		chat = new Chat(creatorId, memberIds, ChatType.GROUP);
	}

	@Test
	void chatAddMessageToChat() {
		TextMessage message = new TextMessage("test text", "user1", creatorId);
		chat.addMessage(message);
		assertSame(message, chat.getMessage(0));
	}

	@Test
	void chatAdd50MessagesToChat() {
		TextMessage[] messageList = new TextMessage[50];
		TextMessage mes;
		for(int i = 0; i < 50; i++) {
			mes = new TextMessage("test text" + i, "user1", creatorId);
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
			messageList[i] = new TextMessage("test text" + i, "user1", creatorId);
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
			messageList[i] = new TextMessage("test text" + i, "user1", creatorId);
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
		chat.addMessage(new TextMessage("test text", "user1", creatorId));
		assertThrows(IllegalArgumentException.class, 
				() -> chat.getMessage(1));
	}

	@Test
	void chatMessageNegataiveMessageIndex() {
		assertThrows(IllegalArgumentException.class, 
				() -> chat.getMessage(-1));
	}
}
