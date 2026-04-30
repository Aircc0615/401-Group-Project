package chat;
import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class ChatConstructorTest {
	private static String[] memberIds = {"1", "2", "3","4", "5", "6"};
	private static String creatorId = "1";
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
		assertEquals(chat.getCreatorUsername(), creatorId);
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
		() -> assertDoesNotThrow(() -> {chat.getMemberUsername(memberIds.length -1);}),
		() -> assertEquals(chat.getMemberUsername(0), memberIds[0]));
	}

	@Test
	void constructorDoesNotHaveInvalidMemberTotal() {
				assertThrows(IllegalArgumentException.class, 
						() -> {chat.getMemberUsername(memberIds.length);});
	}

	@Test
	void constructorToString() {
		String toStr = "";
		for(int i = 0; i < memberIds.length; i++) {
			if(i != 0)
				toStr += ',';
			toStr += memberIds[i];
		}
		toStr += ("\nGROUP\n" + chat.getNewestUpdate() + '\n' + creatorId);
		assertEquals(chat.toString(), toStr);
	}

}
