package test.chat;
import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import src.chat.TextMessage;
import src.chat.ChatType;
import src.chat.Chat;

class ChatMemberTest{
	private static int[] memberIds = {1, 2, 3, 4, 5, 6};
	private static int creatorId = 1;
	private Chat chat;

	@BeforeEach
	void initializeTestConditions() {
		chat = new Chat(creatorId, memberIds, ChatType.GROUP);
	}

	@Test
	void chatAddMemberToChat() {
		chat.addMember(7);
		assertSame(7, chat.getMemberId(6));
	}

	@Test
	void chatAdd50MembersToChat() {
		int[] membersList = new int[56];
		for(int i = 0; i < 6; i++)
			membersList[i] = memberIds[i];
		for(int i = 6; i < 56; i++) {
			membersList[i] = i;
			chat.addMember(i);
		}
		assertAll(
			() -> {
				for(int i = 0; i < 56; i++) {
					assertEquals(membersList[i], chat.getMemberId(i));
				}
			});
	}

	@Test
	void chatCreatorIsImmutable() {
		for(int i = 0; i < 500; i++) {
			chat.addMember(i + 7);
		}
		assertEquals(creatorId, chat.getCreatorId());
	}

	@Test
	void chatMembersSorted() {
		for(int i = 0; i < 500; i++) {
			chat.addMember(i + 7);
		}
		assertAll(
			() -> {
				for(int i = 0; i < 506; i++) {
					assertTrue(chat.getMemberId(i) < chat.getMemberId(i + 1));
				}
			});
	}

	@Test
	void chatRemoveMember() {
		chat.removeMember(2);
		assertAll(
				() -> {
					for(int i = 0; i < 5; i++)
						assertNotEquals(chat.getMemberId(i), 2);
				});
	}

	@Test
	void chatCannotRemoveCreator() {
		assertThrows(IllegalArgumentException.class,
				() -> chat.removeMember(creatorId));
	}

	@Test
	void chatMemberInvalidMemberIndex() {
		chat.addMember(7);
		assertThrows(IndexOutOfBoundsException.class, 
				() -> chat.getMemberId(7));
	}

	@Test
	void chatMemberNegativeMemberIndex() {
		assertThrows(IndexOutOfBoundsException.class, 
				() -> chat.getMemberId(-1));
	}
}