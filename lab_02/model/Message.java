package lab_02.model;

public class Message {
    private User from;
    private String message;

    public Message(User from,String message){
        this.from= from;
        this.message=message;
    }



    public String getMessage() {
        return message;
    }

    @Override
    public String toString() {
        return "Message{" +
                "from=" + from +
                ", message='" + message + '\'' +
                '}';
    }
}
