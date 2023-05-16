import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;

public class TSPSolution {
    private static final int NUM_CITIES = 4;
    private static final int BASE_PORT = 5000;

    public static void main(String[] args) {
        // Create and start nodes for each city
        Node[] nodes = new Node[NUM_CITIES];
        for (int i = 0; i < NUM_CITIES; i++) {
            nodes[i] = new Node(i + 1);
            nodes[i].start();
        }

        // Wait for all nodes to finish
        for (Node node : nodes) {
            try {
                node.join();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }

        // Display the final distances
        for (Node node : nodes) {
            System.out.println("City " + node.getCity() + " - Total Distance: " + node.getTotalDistance());
        }

        // Delay before the CMD window closes
        try {
            Thread.sleep(5000); // 5 seconds
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    static class Node extends Thread {
        private int city;
        private int totalDistance;

        public Node(int city) {
            this.city = city;
        }

        public int getCity() {
            return city;
        }

        public int getTotalDistance() {
            return totalDistance;
        }

        @Override
        public void run() {
            try {
                ServerSocket serverSocket = new ServerSocket(BASE_PORT + city);
                for (int i = 1; i <= NUM_CITIES; i++) {
                    if (i != city) {
                        Socket clientSocket = serverSocket.accept();
                        PrintWriter out = new PrintWriter(clientSocket.getOutputStream(), true);
                        BufferedReader in = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
                        String received = in.readLine();
                        int receivedValue = Integer.parseInt(received) + city * 10;
                        totalDistance += receivedValue;
                        out.println(receivedValue);
                        clientSocket.close();
                    }
                }
                serverSocket.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}
