import java.io.Serializable;

/**
 * Created by geddingsbarrineau on 11/28/16.
 *
 */
public class Message implements Serializable {

    private MessageType messageType;

    public Message(MessageType messageType) {
        this.messageType = messageType;
    }

    @Override
    public String toString() {
        return "Message{" +
                "messageType=" + messageType +
                '}';
    }
}
