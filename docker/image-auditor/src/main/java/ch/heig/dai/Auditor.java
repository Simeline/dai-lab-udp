package ch.heig.dai;

import com.google.gson.Gson;
import com.google.gson.JsonObject;

import java.io.BufferedWriter;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.net.*;
import java.nio.charset.StandardCharsets;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class Auditor {
    private static final String MULTICAST_ADDRESS = "239.255.22.5";
    private static final int UDP_PORT = 9904;
    private static final int TCP_PORT = 2205;

    private static final Map<String, Musician> activeMusicians = new ConcurrentHashMap<>();

    private static class Musician {
        private final static Map<String, String> instruments = Map.of(
                "ti-ta-ti", "piano",
                "pouet", "trumpet",
                "trulu", "flute",
                "gzi-gzi", "violin",
                "boum-boum", "drum");
        private final String uuid;
        private final String instrument;
        private final long lastActivity;

        public Musician(String uuid, String sound) {
            instrument = instruments.get(sound);
            if (instrument == null) {
                throw new IllegalArgumentException("Error : Unknown sound");
            }
            this.uuid = uuid;
            this.lastActivity = System.currentTimeMillis();
        }

        public String getUuid() {
            return uuid;
        }

        public String getInstrument() {
            return instrument;
        }

        public long getLastActivity() {
            return lastActivity;
        }
    }

    private static void processJsonMessage(String jsonMessage) {
        Gson gson = new Gson();
        JsonObject jsonObject = gson.fromJson(jsonMessage, JsonObject.class);

        String uuid = jsonObject.get("uuid").getAsString();
        String sound = jsonObject.get("sound").getAsString();

        Musician newMusician = new Musician(uuid, sound);
        activeMusicians.put(uuid, newMusician);
    }

    private static void tcp_process() {
        try (ServerSocket serverSocket = new ServerSocket(TCP_PORT)) {
            System.out.println("Auditor started. Listening on port " + TCP_PORT);
            while (true) {
                try (Socket socket = serverSocket.accept();
                     var out = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream(), StandardCharsets.UTF_8))) {

                    System.out.println("New client connection");
                    activeMusicians.entrySet().removeIf(entry -> entry.getValue().getLastActivity() < System.currentTimeMillis() - 5000);

                    Gson gson = new Gson();
                    String json = gson.toJson(activeMusicians.values());
                    out.write(json);

                } catch (IOException e) {
                    System.out.println(e.getMessage());
                }
            }
        } catch (IOException e) {
            System.out.println(e.getMessage());
        }
    }

    private static void udp_process() {
        // Start TCP Server
        try (MulticastSocket socket = new MulticastSocket(UDP_PORT)) {
            var group = new InetSocketAddress(MULTICAST_ADDRESS, UDP_PORT);
            NetworkInterface netif = NetworkInterface.getByName("eth0"); // /!\ May vary for different configuration. i.e eth0 or en0
            socket.joinGroup(group, netif);

            System.out.println("Multicast Receiver started. Waiting for messages...");
            try {
                while (true) {
                    byte[] buffer = new byte[1024];
                    DatagramPacket packet = new DatagramPacket(buffer, buffer.length);
                    socket.receive(packet);

                    String message = new String(packet.getData(), 0, packet.getLength(), StandardCharsets.UTF_8);
                    processJsonMessage(message);
                    System.out.println("RECEIVED FROM <" + packet.getAddress() + "> ON  PORT <" + packet.getPort() + "> MESSAGE : " + message);
                }
            } catch (IOException e) {
                System.out.println(e.getMessage());
            } finally {
                socket.leaveGroup(group, netif);
            }
        } catch (IOException e) {
            System.out.println(e.getMessage());
        }
    }

    public static void main(String[] args) {
        try (ExecutorService executor = Executors.newVirtualThreadPerTaskExecutor()) {
            executor.submit(Auditor::tcp_process);
            executor.submit(Auditor::udp_process);
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }
}


