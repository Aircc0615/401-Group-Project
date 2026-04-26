package networking;
import java.io.Serializable;
import java.time.Instant;
import java.util.Date;

import user.User;

public class Message implements Serializable { //serialize takes the object and converts into stream of bytes
    private static int count = 0;
    private int id;
    protected MainType mainType;
    protected SubType subType;
    protected Status status;
    protected String text;
    protected Date date;
    protected Instant timeStamp;
    protected User user;

    public Message(){
        id = count++;
        this.mainType = MainType.UNDEFINED;
        this.subType = SubType.UNDEFINED;
        this.status = Status.UNDEFINED;
        this.text = "Undefined";
        date = new Date();
        timeStamp = Instant.now();
        user = null;
    }

    public Message(MainType mainType, SubType subType, Status status, String text, User user){
        id = count++;
        this.mainType = mainType;
        this.subType = subType;
        this.status = status;
        this.text = text;
        date = new Date();
        timeStamp = Instant.now();
        this.user = user;
    }

    public MainType getType(){
        return mainType;
    }

    public Status getStatus(){
        return status;
    }

    public String getText() {
        return text;
    }

    public int getId(){
        return id;
    }

    public MainType getMainType() { return mainType; }

    public SubType getSubType() { return subType; }

    public Date getDate() { return date; }

    public Instant getTimeStamp() { return timeStamp; }
}
