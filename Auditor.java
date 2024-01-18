import java.io.IOException;
import java.io.PrintWriter;
import java.net.*;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.Map;

public class Auditor {
    private static final String MULTICAST_ADDRESS = "239.255.22.5";
    private static final int PORT = 9904;
    private static final int TCP_PORT = 2205;

//    private static final Map<String, String> instrumentSounds = new HashMap<>();
//    private static final Map<String, Long> activeMusicians = new HashMap<>();
//
//    static {
//        instrumentSounds.put("piano", "ti-ta-ti");
//        instrumentSounds.put("trumpet", "pouet");
//        instrumentSounds.put("flute", "trulu");
//        instrumentSounds.put("violin", "gzi-gzi");
//        instrumentSounds.put("drum", "boum-boum");
//    }

    public static void main(String[] args) {

        // Start TCP Server
        try (MulticastSocket socket = new MulticastSocket(PORT)) {
            var group = new InetSocketAddress(MULTICAST_ADDRESS, PORT);
            NetworkInterface netif = NetworkInterface.getByName("eth0");
            socket.joinGroup(group, netif);

            System.out.println("Multicast Receiver started. Waiting for messages...");

            while (true) {

                byte[] buffer = new byte[1024];
                DatagramPacket packet = new DatagramPacket(buffer, buffer.length);
                socket.receive(packet);

                String message = new String(packet.getData(), 0, packet.getLength(), StandardCharsets.UTF_8);
                System.out.println("Received message: " + message + " from " + packet.getAddress() + ", port" + packet.getPort());
                socket.leaveGroup(group, netif);
            }
        } catch (IOException e) {
            System.out.println(e.getMessage());
        }
    }
}


