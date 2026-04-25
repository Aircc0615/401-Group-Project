package test.chat;
import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import src.chat.TextMessage;
import src.chat.ChatType;
import src.chat.Chat;
import src.chat.ChatList;

class ChatListTest {
	private ChatList chatList;
	private int creatorId = 1;
	private int[] groupIds = {1, 2, 3, 4, 5, 6};
	private int privateId = 2;
	
	@BeforeEach
	void initializeChatList() {
		chatList = new ChatList();
	}
	
	@Test
	void chatListConstructorNotNull() {
		assertNotNull(chatList);
	}
	
	void chatListAddChat() {
		Chat chat = new Chat(creatorId, groupIds, ChatType.GROUP);
		chatList.addChat(chat);
		assertSame(chat, chatList.getChat(0));
	}

	void chatListInvalidChatIndex() {
		assertThrows(IndexOutOfBoundsException.class,
				() -> chatList.getChat(0));
	}

	void chatListNegativeChatIndex() {
		assertThrows(IndexOutOfBoundsException.class,
				() -> chatList.getChat(-1));
	}

	void chatsSortedByNewestUpdateOnAdd() {
		Chat chat1 = new Chat(creatorId, groupIds, ChatType.GROUP);
		Chat chat2 = new Chat(creatorId, groupIds, ChatType.GROUP);
		chatList.addChat(chat2);
		chatList.addChat(chat1);
		assertAll(
				() -> assertSame(chatList.getChat(0), chat1),
				() -> assertSame(chatList.getChat(1), chat2));
	}

	void chatListNoDuplicates() {
		Chat chat = new Chat(creatorId, groupIds, ChatType.GROUP);
		chatList.addChat(chat);
		chatList.addChat(chat);
		assertThrows(IndexOutOfBoundsException.class,
				() -> chatList.getChat(1));
	}

	
}
