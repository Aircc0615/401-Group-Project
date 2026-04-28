package chat;
import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

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
	
	@Test
	void chatListAddChat() {
		Chat chat = new Chat(creatorId, groupIds, ChatType.GROUP);
		chatList.addChat(chat);
		assertEquals(chatList.toString(), Integer.toString(chat.getChatId()));
	}

	@Test
	void chatListInvalidChatIndex() {
		assertThrows(IndexOutOfBoundsException.class,
				() -> chatList.getChatNewestUpdate(-1));
	}

	@Test
	void chatsSortedByNewestUpdateOnAdd() {
		Chat chat1 = new Chat(creatorId, groupIds, ChatType.GROUP);
		Chat chat2 = new Chat(creatorId, groupIds, ChatType.GROUP);
		Chat chat3 = new Chat(creatorId, groupIds, ChatType.GROUP);
		chatList.addChat(chat2);
		chatList.addChat(chat1);
		chatList.addChat(chat3);
		int[] orderArray = new int[3];
		orderArray[0] = chat3.getChatId();
		orderArray[1] = chat2.getChatId();
		orderArray[2] = chat1.getChatId();
		int[] returnArray = chatList.getChatIds();
		assertArrayEquals(orderArray, returnArray);
	}

	@Test
	void chatListNoDuplicates() {
		Chat chat = new Chat(creatorId, groupIds, ChatType.GROUP);
		chatList.addChat(chat);
		chatList.addChat(chat);
		chatList.deleteChat(chat.getChatId(), creatorId);
		assertThrows(IndexOutOfBoundsException.class,
				() -> chatList.getChatNewestUpdate(chat.getChatId()));
	}

	@Test
	void chatsSortedByNewestUpdateOnMessage() {
		Chat chat1 = new Chat(creatorId, groupIds, ChatType.GROUP);
		Chat chat2 = new Chat(creatorId, groupIds, ChatType.GROUP);
		Chat chat3 = new Chat(creatorId, groupIds, ChatType.GROUP);
		Chat chat4 = new Chat(creatorId, groupIds, ChatType.GROUP);
		Chat chat5 = new Chat(creatorId, groupIds, ChatType.GROUP);
		chatList.addChat(chat1);
		chatList.addChat(chat2);
		chatList.addChat(chat3);
		chatList.addChat(chat4);
		chatList.addChat(chat5);
		int[] startingIds = chatList.getChatIds();
		chatList.addChatMessage(chat2.getChatId(), new TextMessage("test", "test_user", 1));
		int[] modifiedIds = chatList.getChatIds();
		assertAll(
				() -> assertEquals(startingIds[0], modifiedIds[1]),
				() -> assertEquals(startingIds[1], modifiedIds[2]),
				() -> assertEquals(startingIds[2], modifiedIds[3]),
				() -> assertEquals(startingIds[3], modifiedIds[0]),
				() -> assertEquals(startingIds[4], modifiedIds[4]));
	}

	@Test
	void chatListRemoveChat() {
		Chat chat1 = new Chat(creatorId, groupIds, ChatType.GROUP);
		Chat chat2 = new Chat(creatorId, groupIds, ChatType.GROUP);
		chatList.addChat(chat1);
		chatList.addChat(chat2);

		chatList.deleteChat(chat1.getChatId(), creatorId);;

		assertThrows(IndexOutOfBoundsException.class,
				() -> chatList.getChatNewestUpdate(chat1.getChatId()));
	}
}
