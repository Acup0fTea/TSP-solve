import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

public class Server {
    private static final int NUM_CITIES = 4;
    private static final int[] PORTS = { 8081, 8082, 8083, 8084 };

    public static void main(String[] args) {
        for (int i = 0; i < NUM_CITIES; i++) {
            int port = PORTS[i];
            Node node = new Node(i + 1, port);
            node.start();
        }
    }

    static class Node extends Thread {
        private final int city;
        private final int port;
        private final List<String> neighbors;
        private boolean isSent;
        private List<String> receivedMessages;

        public Node(int city, int port) {
            this.city = city;
            this.port = port;
            this.neighbors = new ArrayList<>();
            this.receivedMessages = new ArrayList<>();
            this.isSent = false;
            for (int i = 0; i < NUM_CITIES; i++) {
                if (i + 1 != city) {
                    this.neighbors.add("node " + (i + 1));
                }
            }
        }

        @Override
        public void run() {
            try {
                ServerSocket serverSocket = new ServerSocket(port);
                System.out.println("Node " + city + " is running on port " + port);

                if (!isSent) {
                    for (String neighbor : neighbors) {
                        sendMessage(neighbor, String.valueOf(city));
                    }
                    isSent = true;
                }

                while (true) {
                    Socket clientSocket = serverSocket.accept();
                    BufferedReader reader = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
                    String message = reader.readLine();

                    System.out.println("Received: " + message);
                    receivedMessages.add(message);

                    if (receivedMessages.size() == 3) {
                        System.out.println("Node " + city + " received: " + receivedMessages);
                        calculateDistances();
                        break;
                    }
                }

                serverSocket.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        private void sendMessage(String neighbor, String message) {
            try {
                String[] neighborInfo = neighbor.split(" ");
                String neighborHost = "localhost";
                int neighborPort = PORTS[Integer.parseInt(neighborInfo[1]) - 1];
                Socket socket = new Socket(neighborHost, neighborPort);

                PrintWriter writer = new PrintWriter(socket.getOutputStream(), true);
                writer.println(message);

                socket.close();
                TimeUnit.SECONDS.sleep(2);
            } catch (IOException | InterruptedException e) {
                e.printStackTrace();
            }
        }

        private void calculateDistances() {
            StringBuilder sb = new StringBuilder();
            int distance = city;
            for (int i = 0; i < NUM_CITIES - 1; i++) {
                distance += neighbors.size() + 1;
                sb.append("node ").append(city).append(": ").append(distance).append("\n");
            }
            System.out.println("Distance: " + sb.toString());
        }
    }
}
