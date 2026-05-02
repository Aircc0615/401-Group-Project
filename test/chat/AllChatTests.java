package chat;

import org.junit.platform.suite.api.SelectClasses;
import org.junit.platform.suite.api.Suite;

@Suite
@SelectClasses({ ChatConstructorTest.class, ChatListTest.class, ChatMemberTest.class, ChatMessageTest.class,
    TextMessageTest.class })
public class AllChatTests {

}
