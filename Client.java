import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectOutput;
import java.io.ObjectOutputStream;
import java.net.*;
import java.util.Optional;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * Created by geddingsbarrineau on 11/28/16.
 *
 */
public class Client {

    private final ExecutorService pool;

    private InetAddress brokerAddress;
    private int brokerPort = 57;
    private DatagramSocket datagramSocket;

    private Client() {
        try {
            String localHostAddress = InetAddress.getLocalHost().getHostAddress();
            brokerAddress = InetAddress.getByName(localHostAddress);
            datagramSocket = new DatagramSocket();
        } catch (UnknownHostException | SocketException e) {
            e.printStackTrace();
        }
        pool = Executors.newCachedThreadPool();
        pool.execute(new SubscriptionsHandler(datagramSocket));
    }

    private void connectToBroker() {
        Message connectMessage = new ConnectMessage();
        createDatagramWithMessage(connectMessage)
                .ifPresent(this::sendDatagram);
    }

    private void disconnectFromBroker() {
        Message disconnectMessage = new DisconnectMessage();
        createDatagramWithMessage(disconnectMessage)
                .ifPresent(this::sendDatagram);
    }

    private void subscribeToPublisher() {
        Message subscribeMessage = new SubscribeMessage();
        createDatagramWithMessage(subscribeMessage)
                .ifPresent(this::sendDatagram);
    }

    private void unsubscribeFromPublisher() {
        Message unsubscribeMessage = new UnsubscribeMessage();
        createDatagramWithMessage(unsubscribeMessage)
                .ifPresent(this::sendDatagram);
    }

    private void publish() {
        Message publishMessage = new PublishMessage();
        createDatagramWithMessage(publishMessage)
                .ifPresent(this::sendDatagram);
    }

    private void publish(String text) {
        Message publishMessage = new PublishMessage(text);
        createDatagramWithMessage(publishMessage)
                .ifPresent(this::sendDatagram);
    }

    private void sendDatagram(DatagramPacket datagram) {
        try {
            datagramSocket.send(datagram);
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    private Optional<DatagramPacket> createDatagramWithMessage(Message message) {
        try {
            ByteArrayOutputStream bos = new ByteArrayOutputStream();
            ObjectOutput out = new ObjectOutputStream(bos);
            out.writeObject(message);
            byte[] buffer = bos.toByteArray();
            DatagramPacket packet = new DatagramPacket(
                    buffer, buffer.length, brokerAddress, brokerPort
            );
            bos.close();
            return Optional.of(packet);
        } catch (IOException e) {
            e.printStackTrace();
            return Optional.empty();
        }
    }

    class SubscriptionsHandler implements Runnable {
        private final DatagramSocket socket;
        SubscriptionsHandler(DatagramSocket socket) { this.socket = socket; }
        public void run() {
            try {
                byte[] receiveData = new byte[10000];
                System.out.println("Listening for subscriber messages..");
                DatagramPacket receivePacket = new DatagramPacket(receiveData,
                        receiveData.length);

                while (true) {
                    datagramSocket.receive(receivePacket);
                    byte[] sentence = receivePacket.getData();
                    System.out.println("RECEIVED: " + new String(sentence));
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public static void main(String[] args) throws IOException {
        Client client1 = new Client();
        client1.subscribeToPublisher();

        Client client2 = new Client();
        client2.subscribeToPublisher();

        Client client3 = new Client();
        client3.publish("This is my first published message....");
    }
}
