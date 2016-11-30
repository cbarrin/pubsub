import java.io.Serializable;

/**
 * Created by geddingsbarrineau on 11/28/16.
 */

public abstract class Message implements Serializable {

    private MessageType messageType;
    private String topic;
    private String text;

    public Message(MessageType messageType, String topic, String text) {
        this.messageType = messageType;
        this.topic = topic;
        this.text = text;
    }

    public MessageType getMessageType() {
        return messageType;
    }

    public String getTopic() {
        return topic;
    }

    public void setTopic(String topic) {
        this.topic = topic;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public Message(MessageType messageType, String topic) {
        this.messageType = messageType;
        this.topic = topic;
        this.text = "";
    }

    @Override
    public String toString() {
        return "Message{" +
                "messageType=" + messageType +
                ", topic='" + topic + '\'' +
                '}';
    }
}
