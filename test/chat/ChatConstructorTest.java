package test.chat;
import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import src.chat.TextMessage;
import src.chat.ChatType;
import src.chat.Chat;

class ChatConstructorTest {
	private static int[] memberIds = {1, 2, 3, 4, 5, 6};
	private static int creatorId = 1;
	private static Chat chat;

	@BeforeAll
	static void initializeTestConditions() {
		chat = new Chat(creatorId, memberIds, ChatType.GROUP);
	}

	@Test
	void constructorReturnsObject() {
		assertNotNull(chat);
	}

	@Test
	void constructorProperCreatorId() {
		assertEquals(chat.getCreatorId(), creatorId);
	}

	@Test
	void constructorProperChatType() {
		assertEquals(chat.getChatType(), ChatType.GROUP);
	}

	@Test
	void constructorProperChatId() {
		Chat newChat = new Chat(creatorId, memberIds, ChatType.GROUP);
		assertEquals(chat.getChatId() + 1, newChat.getChatId());
	}

	@Test
	void constructorHasNoMessages() {
		assertThrows(IllegalArgumentException.class,
			() -> {chat.getMessage(0);});
	}

	@Test
	void constructorHasValidMemberTotal() {
		assertAll(
		() -> assertDoesNotThrow(() -> {chat.getMemberId(memberIds.length -1);}),
		() -> assertEquals(chat.getMemberId(0), memberIds[0]));
	}

	@Test
	void constructorDoesNotHaveInvalidMemberTotal() {
				assertThrows(IllegalArgumentException.class, 
						() -> {chat.getMemberId(memberIds.length);});
	}

}
