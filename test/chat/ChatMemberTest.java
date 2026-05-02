package chat;
import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Disabled;
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
		String[] membersList = chat.getMembersInChat();
		boolean inList = false;
		for(int i = 0; i < membersList.length; i++) {
			if(membersList[i] == "7")
				inList = true;
		}
		assertTrue(inList);
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
		assertEquals(chat.getNumMembers(), 56);
	}

	@Test
	void chatCreatorIsImmutable() {
		for(int i = 0; i < 500; i++) {
			chat.addMember("" + (i + 7));
		}
		assertEquals(creatorId, chat.getCreatorUsername());
	}

	@Disabled
	void chatMembersSorted() {
		for(int i = 0; i < 500; i++) {
			chat.addMember("" + (i + 7));
		}
		/*assertAll(
			() -> {
				for(int i = 0; i < 506; i++) {
					assertTrue(Integer.parseInt(chat.getMemberUsername(i)) < Integer.parseInt(chat.getMemberUsername(i + 1)));
				}
			});*/
	}

	@Test
	void chatRemoveMember() {
		String[] membersOld = chat.getMembersInChat();
		chat.removeMember("2");
		String[] membersNew = chat.getMembersInChat();
		assertNotEquals(membersOld.length, membersNew.length);
	}

	@Test
	void chatCannotRemoveCreator() {
		assertThrows(IllegalArgumentException.class,
				() -> chat.removeMember(creatorId));
	}

}