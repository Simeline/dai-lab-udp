import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;
import com.google.gson.Gson;

public class Musician {
    private static final String MULTICAST_ADDRESS = "239.255.22.5";
    private static final int PORT = 9904;
    private static final int SLEEP_DURATION = 1000; // millisecondes

//    private static final Map<String, String> instrumentSounds = new HashMap<>();
//
//    static {
//        instrumentSounds.put("piano", "ti-ta-ti");
//        instrumentSounds.put("trumpet", "pouet");
//        instrumentSounds.put("flute", "trulu");
//        instrumentSounds.put("violin", "gzi-gzi");
//        instrumentSounds.put("drum", "boum-boum");
//    }



    public static void main(String[] args) {

//        // on doit passer au moins un argument ?
//        if (args.length != 1) {
//            System.err.println("Usage: java Musician <instrument>");
//            System.exit(1);
//        }
//
//        String instrument = args[0];

        try (DatagramSocket socket = new DatagramSocket()) {

            System.out.println("Multicast Sender (Musiciant) started. Waiting for messages...");

            while (true) {
                String musicianId = UUID.randomUUID().toString();
                String instrument = "piano";
                long timestamp = System.currentTimeMillis();

                // Créez une instance de la classe Message
                Message message = new Message(musicianId, instrument, timestamp);
                String jsonMessage = new Gson().toJson(message);

                byte[] payload = jsonMessage.getBytes(StandardCharsets.UTF_8);
                InetAddress group = InetAddress.getByName(MULTICAST_ADDRESS);
                DatagramPacket packet = new DatagramPacket(payload, payload.length, group, PORT);
                socket.send(packet);

                Thread.sleep(SLEEP_DURATION);
            }
        }

        catch (Exception e) {
            e.printStackTrace();
        }
    }
}
