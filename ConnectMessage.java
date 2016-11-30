import java.io.Serializable;

/**
 * Created by geddingsbarrineau on 11/30/16.
 */
public class ConnectMessage extends Message implements Serializable {
    private static final long serialVersionUID = 1L;

    public ConnectMessage() {
        super(MessageType.CONNECT, "");
    }
}
