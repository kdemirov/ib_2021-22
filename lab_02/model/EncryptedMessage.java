package lab_02.model;


public class EncryptedMessage {
    private User from;
    private User to;
    private byte[] message;

    public EncryptedMessage(User from, User to, byte[] message) {
        this.from = from;
        this.to = to;
        this.message = message;
    }

    public User getFrom() {
        return from;
    }

    public User getTo() {
        return to;
    }

    public byte[] getMessage() {
        return message;
    }

    @Override
    public String toString() {
        StringBuilder message = new StringBuilder();
        for (byte b : this.message) {
            message.append(String.format("%02X",b));
        }
        return "EncryptedMessage{" +
                "from=" + from +
                ", to=" + to +
                ", message=" + message.toString() +
                '}';
    }
}
