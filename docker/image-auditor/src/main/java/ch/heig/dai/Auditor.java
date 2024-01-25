package ch.heig.dai;

import com.google.gson.Gson;

import java.io.BufferedWriter;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.net.*;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Objects;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class Auditor {
    private static final String MULTICAST_ADDRESS = "239.255.22.5";
    private static final int UDP_PORT = 9904;
    private static final int TCP_PORT = 2205;

    private static final ArrayList<Message> activeMusicians = new ArrayList<>();

    private static Message processJsonMessage(String jsonMessage) {

        Gson gson = new Gson();
        Message message = gson.fromJson(jsonMessage, Message.class);

        System.out.println("Received JSON message:");
        System.out.println("UUID: " + message.getUuid());
        System.out.println("Instrument: " + message.getInstrument());
        System.out.println("Sound: " + message.getSound());
        System.out.println("Last Activity: " + message.getLastActivity());

        for (Message m : activeMusicians) {
            if (Objects.equals(message.getUuid(), m.getUuid())) {
                activeMusicians.remove(m);
            }
        }
        return message;
    }

    private static void tcp_process() {

        try (ServerSocket serverSocket = new ServerSocket(TCP_PORT)) {

            while (true) {

                try (Socket socket = serverSocket.accept();

                     var out = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream(), StandardCharsets.UTF_8))) {

                    for (Message m : activeMusicians) {
                        if (m.getLastActivity() < System.currentTimeMillis() - 5000) {
                            activeMusicians.remove(m);
                        }
                    }

                    Gson gson = new Gson();
                    String json = gson.toJson(activeMusicians);
                    out.write(json);

                }
                catch (IOException e) {
                    System.out.println(e.getMessage());
                }
            }
        }
        catch (IOException e) {
            System.out.println(e.getMessage());
        }
    }

    private static void udp_process() {

        // Start TCP Server
        try (MulticastSocket socket = new MulticastSocket(UDP_PORT)) {

            var group = new InetSocketAddress(MULTICAST_ADDRESS, UDP_PORT);
            NetworkInterface netif = NetworkInterface.getByName("eth0");
            socket.joinGroup(group, netif);

            System.out.println("Multicast Receiver started. Waiting for messages...");

            try {

                while (true) {

                    byte[] buffer = new byte[1024];
                    DatagramPacket packet = new DatagramPacket(buffer, buffer.length);
                    socket.receive(packet);

                    String message = new String(packet.getData(), 0, packet.getLength(), StandardCharsets.UTF_8);
                    activeMusicians.add(processJsonMessage(message));
                    System.out.println("Received message: " + message + " from " + packet.getAddress() + ", port" + packet.getPort());
                    }
                }
            finally {
                socket.leaveGroup(group, netif);
            }
        }
        catch (IOException e) {
            System.out.println(e.getMessage());
        }
    }

    public static void main(String[] args) {
        try (ExecutorService executor = Executors.newVirtualThreadPerTaskExecutor()) {
            executor.submit(Auditor::tcp_process);
            executor.submit(Auditor::udp_process);
        }
    }
}


