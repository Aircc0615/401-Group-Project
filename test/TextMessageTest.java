package test;
import static org.junit.jupiter.api.Assertions.*;

import java.time.Instant;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import src.chat.TextMessage;

class TextMessageTest {
	private static int userId;
	private static String username;
	private static String text;
	private static Instant time;
	private static TextMessage textMessage;

	@BeforeAll
	static void Initialize() {
		text = "test message";
		username = "test";
		userId = 1;
		time = Instant.now();
		textMessage = new TextMessage(text, username, userId, time);
	}
	@Test
	void textMessageConstructor() {
		assertNotNull(textMessage);
		assertEquals(textMessage.getText().compareTo(text), 0);
		assertTrue(textMessage.getUsername().compareTo(username) == 0);
		assertTrue(textMessage.getUserId() == userId);
		assertTrue(textMessage.getTimestamp().compareTo(time) == 0);
	}

}
