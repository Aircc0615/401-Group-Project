package chat;
import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class ChatMemberTest {
	private static String[] memberIds = {"1", "2", "3","4", "5", "6"};
	private static String creatorId = "1";
	private Chat chat;

	@BeforeEach
	void initializeTestConditions() {
		chat = new Chat(creatorId, memberIds, ChatType.GROUP);
	}

	@Test
	void chatAddMemberToChat() {
		chat.addMember("7");
		assertEquals("7", chat.getMemberUsername(6));
	}

	@Test
	void chatAdd50MembersToChat() {
		String[] membersList = new String[56];
		for(int i = 0; i < 6; i++)
			membersList[i] = memberIds[i];
		for(int i = 6; i < 56; i++) {
			String username = "" + i;
			membersList[i] = username;
			chat.addMember(username);
		}
		assertAll(
			() -> {
				for(int i = 0; i < 56; i++) {
					assertEquals(membersList[i], chat.getMemberUsername(i));
				}
			});
	}

	@Test
	void chatCreatorIsImmutable() {
		for(int i = 0; i < 500; i++) {
			chat.addMember("" + (i + 7));
		}
		assertEquals(creatorId, chat.getCreatorUsername());
	}

	@Test
	void chatMembersSorted() {
		for(int i = 0; i < 500; i++) {
			chat.addMember("" + (i + 7));
		}
		assertAll(
			() -> {
				for(int i = 0; i < 506; i++) {
					assertTrue(Integer.parseInt(chat.getMemberUsername(i)) < Integer.parseInt(chat.getMemberUsername(i + 1)));
				}
			});
	}

	@Test
	void chatRemoveMember() {
		chat.removeMember("2");
		assertAll(
				() -> {
					for(int i = 0; i < 5; i++)
						assertNotEquals(chat.getMemberUsername(i), "2");
				});
	}

	@Test
	void chatCannotRemoveCreator() {
		assertThrows(IllegalArgumentException.class,
				() -> chat.removeMember(creatorId));
	}

	@Test
	void chatMemberInvalidMemberIndex() {
		chat.addMember("7");
		assertThrows(IndexOutOfBoundsException.class, 
				() -> chat.getMemberUsername(7));
	}

	@Test
	void chatMemberNegativeMemberIndex() {
		assertThrows(IndexOutOfBoundsException.class, 
				() -> chat.getMemberUsername(-1));
	}
}