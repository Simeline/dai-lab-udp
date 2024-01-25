package ch.heig.dai;

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

    // Instrument list with their sound
    private final static Map<String, String> sounds = Map.of(
            "piano", "ti-ta-ti",
            "trumpet", "pouet",
            "flute", "trulu",
            "violin", "gzi-gzi",
            "drum", "boum-boum");

    // Attributes for the instrument
    private static class Instrument{
        private final String uuid = UUID.randomUUID().toString();
        private final String sound;

        public Instrument(String instrument){
            sound = sounds.get(instrument);
            if (sound == null) {
                throw new IllegalArgumentException("Error : Unknown instrument");
            }
        }

        public String getSound(){
            return sound;
        }

        public String getUuid(){
            return uuid;
        }
    }

public static void main(String[] args) {
    System.out.println("Starting Musician...");

    // Check if the args are valid
    if (args == null || args.length != 1) {
        System.out.println("Exit. Error with instrument argument");
        System.out.println("Usage: docker run -d dai/musician <instrument>");
        return;
    }

    // Get the instrument sound
    Instrument instrument = null;
    try {
        instrument = new Instrument(args[0]);
    } catch (Exception e) {
        System.out.println("Error : Unknown instrument");
        return;
    }

    try (DatagramSocket socket = new DatagramSocket()) {
        System.out.println("Multicast Sender (Musician) started. Waiting for messages...");

        // Create the payload
        String jsonMessage = new Gson().toJson(instrument);
        byte[] payload = jsonMessage.getBytes(StandardCharsets.UTF_8);
        System.out.println("Payload was build. Message is the following : \n" + jsonMessage);

        while (true) {
            // Send the packet
            InetAddress group = InetAddress.getByName(MULTICAST_ADDRESS);
            DatagramPacket packet = new DatagramPacket(payload, payload.length, group, PORT);
            socket.send(packet);
            System.out.println("Payload has been sent.");

            // Sleeping
            Thread.sleep(SLEEP_DURATION);
        }
    } catch (Exception e) {
        // TODO : Change error processing after debugging
        e.printStackTrace();
    }
}
}
