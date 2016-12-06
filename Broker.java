import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.ObjectInput;
import java.io.ObjectInputStream;
import java.net.*;
import java.util.*;

/**
 * Created by geddingsbarrineau on 11/28/16.
 */
public class Broker {

    private InetAddress address;
    private int port = 57;
    private DatagramSocket datagramSocket;
    private Map<String, Set<SocketAddress>> subscribers;

    public Broker() {
        try {
            String localHostAddress = InetAddress.getLocalHost().getHostAddress();
            address = InetAddress.getByName(localHostAddress);
            datagramSocket = new DatagramSocket(port);
        } catch (UnknownHostException | SocketException e) {
            e.printStackTrace();
        }
        subscribers = new HashMap<>();
    }

    public static Object createObjectFromBytes(byte[] yourBytes) {
        ByteArrayInputStream bis = new ByteArrayInputStream(yourBytes);
        ObjectInput in = null;
        try {
            in = new ObjectInputStream(bis);
            return in.readObject();
        } catch (IOException | ClassNotFoundException e) {
            e.printStackTrace();
        } finally {
            try {
                if (in != null) {
                    in.close();
                }
            } catch (IOException ex) {
            }
        }
        return null;
    }

    public void startListening() {
        try {
            byte[] receiveData = new byte[1000];

            System.out.printf("Listening on udp:%s:%d%n",
                    address, port);
            DatagramPacket receivePacket = new DatagramPacket(receiveData,
                    receiveData.length);

            while (true) {
                datagramSocket.receive(receivePacket);
                Optional<Message> message = Optional.ofNullable((Message) createObjectFromBytes(receivePacket.getData()));
                message.ifPresent(message1 -> handleMessage(message1, receivePacket.getSocketAddress()));
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        // should close serverSocket in finally block
    }

    public void handleMessage(Message message, SocketAddress socketAddress) {
        DatagramPacket sendPacket;
        switch (message.getMessageType()) {
            case SUBSCRIBE:
//                System.out.println(message.getMessageType());
//                String sendString = "Subscribed to " + message.getTopic();
                addSubscriber(message.getTopic(), socketAddress);
//                byte[] sendData = sendString.getBytes();
//                sendPacket = new DatagramPacket(sendData, sendData.length, socketAddress);
//                try {
//                    datagramSocket.send(sendPacket);
//                } catch (IOException e) {
//                    e.printStackTrace();
//                }
                break;
            case PUBLISH:
//                System.out.println(message.getMessageType());
//                System.out.println(safe(subscribers.get(message.getTopic())));
                for (SocketAddress sa : safe(subscribers.get(message.getTopic()))) {
                    try {
                        String sendString2 = message.getText();
                        byte[] sendData2 = sendString2.getBytes();
                        sendPacket = new DatagramPacket(sendData2, sendData2.length, sa);
                        datagramSocket.send(sendPacket);
                    } catch (NullPointerException | IOException e) {
                        e.printStackTrace();
                    }
                }
                break;
            case CONNECT:
            case DISCONNECT:
            case UNSUBSCRIBE:
            case NONE:
            default:
                System.out.println("Message handling not yet implemented for message type " + message.getMessageType());
                break;
        }
    }

    public void addSubscriber(String topic, SocketAddress subscriber) {
        if (!subscribers.containsKey(topic)) {
            subscribers.put(topic, new HashSet<>());
        }
        subscribers.get(topic).add(subscriber);
    }

    public static <T> Set<T> safe( Set<T> other ) {
        return other == null ? Collections.emptySet() : other;
    }

    public static void main(String[] args) {
        Broker broker = new Broker();
        broker.startListening();
    }
}
