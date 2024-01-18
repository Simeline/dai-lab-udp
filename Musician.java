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

    private static void processJsonMessage(String jsonMessage) {
        Gson gson = new Gson();

        // Assuming a class named Message for the structure of your JSON
        Message message = gson.fromJson(jsonMessage, Message.class);

        // Now you can work with the parsed Java object
        System.out.println("Received JSON message:");
        System.out.println("UUID: " + message.getUuid());
        System.out.println("Instrument: " + message.getInstrument());
        System.out.println("Sound: " + message.getSound());
        // Add more processing as needed
    }

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
//                String sound = instrumentSounds.getOrDefault(instrument, "unknown");
//                String musicianId = UUID.randomUUID().toString();
//                String message = String.format("{\"uuid\":\"%s\",\"sound\":\"%s\"}", musicianId, sound);
                String message = "Hello";

                byte[] payload = message.getBytes(StandardCharsets.UTF_8);
                var dest_address = new InetSocketAddress(MULTICAST_ADDRESS, 44444);
                DatagramPacket packet = new DatagramPacket(payload, payload.length, dest_address);
                socket.send(packet);

                 Thread.sleep(SLEEP_DURATION);
            }
        }

        catch (Exception e) {
            e.printStackTrace();
        }
    }
}
