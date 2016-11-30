import java.io.Serializable;

/**
 * Created by geddingsbarrineau on 11/30/16.
 */
public class DisconnectMessage extends Message implements Serializable {
    public DisconnectMessage() {
        super(MessageType.DISCONNECT, "");
    }
}
