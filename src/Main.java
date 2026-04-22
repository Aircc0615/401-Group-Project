import java.time.Instant;

import chat.Chat;
import chat.ChatType;
import chat.TextMessage;

public class Main {

	public static void main(String[] args) {
	    User user1 = new User("mukshar", "mukshar");
	    User user2 = new User("bloom12", "bloom12");
	    User user3 = new User("moooon", "moooon");
	    User user4 = new User("bloom14", "bloom14");

	    Chat chat1 = new Chat(
	        user1.getId(),
	        new int[]{user1.getId(), user2.getId()},
	        ChatType.PRIVATE
	    );

	    Chat chat2 = new Chat(
	        user1.getId(),
	        new int[]{user1.getId(), user3.getId()},
	        ChatType.PRIVATE
	    );

	    Chat chat3 = new Chat(
	        user1.getId(),
	        new int[]{user1.getId(), user4.getId()},
	        ChatType.PRIVATE
	    );

	    Instant now = Instant.now();

	    chat1.addMessage(new TextMessage("hey", "mukshar", user1.getId(), now));
	    chat1.addMessage(new TextMessage("hi", "bloom12", user2.getId(), now.plusSeconds(10)));
	    chat1.addMessage(new TextMessage("how are you? jgguyguyyuvuyvuvuyvyuvuvyu", "mukshar", user1.getId(), now.plusSeconds(20)));

	    chat2.addMessage(new TextMessage("yo", "mukshar", user1.getId(), now.plusSeconds(30)));
	    chat2.addMessage(new TextMessage("what's up", "moooon", user3.getId(), now.plusSeconds(40)));

	    chat3.addMessage(new TextMessage("hello", "bloom14", user4.getId(), now.plusSeconds(50)));
	    chat3.addMessage(new TextMessage("hey there", "mukshar", user1.getId(), now.plusSeconds(60)));
	    chat3.addMessage(new TextMessage("testing chat", "bloom14", user4.getId(), now.plusSeconds(70)));

	    user1.getChatList().addChat(chat1);
	    user1.getChatList().addChat(chat2);
	    user1.getChatList().addChat(chat3);

	    

	    new GUI(user1);
	}
	

}
