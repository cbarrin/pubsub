import java.io.Serializable;

/**
 * Created by geddingsbarrineau on 11/30/16.
 */
public class SubscribeMessage extends Message implements Serializable {

    public SubscribeMessage() {
        super(MessageType.SUBSCRIBE, "ANY");
    }

    public SubscribeMessage(String topic) {
        super(MessageType.SUBSCRIBE, topic);
    }

}
