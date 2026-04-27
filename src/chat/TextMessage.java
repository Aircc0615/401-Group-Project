package chat;
import java.time.Instant;

public class TextMessage {
	private String text;
	private String username;
	private int userId;
	private Instant timestamp;

	//Constructor for existing textmessages
	public TextMessage(String text, String username, int userId, Instant timestamp) {
		this.text = text;
		this.username = username;
		this.userId = userId;
	}
	

	//Constructor for new textmessages
	public TextMessage(String text, String username, int userId) {
		this.text = text;
		this.username = username;
		this.userId = userId;
		this.timestamp = Instant.now();
	}

	//getters
	public String getText() {
		return text;
	}
	
	public String getUsername() {
		return username;
	}
	
	public int getUserId() {
		return userId;
	}
	
	public Instant getTimestamp() {
		return timestamp;
	}
}	