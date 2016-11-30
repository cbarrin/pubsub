import java.io.Serializable;

/**
 * Created by geddingsbarrineau on 11/30/16.
 */
public class UnsubscribeMessage extends Message implements Serializable {

    public UnsubscribeMessage() {
        super(MessageType.UNSUBSCRIBE, "ANY");
    }

    public UnsubscribeMessage(String topic) {
        super(MessageType.UNSUBSCRIBE, topic);
    }
}
