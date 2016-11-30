import java.io.*;
import java.net.*;
import java.nio.ByteBuffer;
import java.nio.charset.Charset;

/**
 * Created by geddingsbarrineau on 11/28/16.
 *
 */
public class Client {

    public void connectToBroker() {

    }

    public static void sendMessageWithType(MessageType type) {
        ByteArrayOutputStream bos = new ByteArrayOutputStream();
        ObjectOutput out = null;
        try {
            out = new ObjectOutputStream(bos);
            out.writeObject(new Message(type));
            out.close();
            byte[] buffer = bos.toByteArray();
            String localHostAddress = InetAddress.getLocalHost().getHostAddress();
            InetAddress address = InetAddress.getByName(localHostAddress);
            DatagramPacket packet = new DatagramPacket(
                    buffer, buffer.length, address, 57
            );
            DatagramSocket datagramSocket = new DatagramSocket();
            datagramSocket.send(packet);
            System.out.println(address);
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                bos.close();
            } catch (IOException ex) {
                // ignore close exception
            }
        }

    }


    public static void main(String[] args) throws IOException {
        sendMessageWithType(MessageType.CONNECT);
    }
}
