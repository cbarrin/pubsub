import java.io.Serializable;

/**
 * Created by geddingsbarrineau on 11/30/16.
 */
public class PublishMessage extends Message implements Serializable {

    public PublishMessage() {
        super(MessageType.PUBLISH, "ANY");
    }

    public PublishMessage(String text) {
        super(MessageType.PUBLISH, "ANY", text);
    }

    public PublishMessage(String topic, String text) {
        super(MessageType.PUBLISH, topic, text);
    }
}
