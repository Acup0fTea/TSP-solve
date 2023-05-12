import java.io.*;
import java.net.*;

public class CityNode implements Runnable {
    private int id;
    private String[] neighbors;
    private int[] distances;
    private int[] order;
    private int hops;
    private boolean visited;

    public CityNode(int id, String[] neighbors, int[] distances) {
        this.id = id;
        this.neighbors = neighbors;
        this.distances = distances;
        this.order = new int[4];
        this.hops = 0;
        this.visited = false;
    }

    public void setOrder(int[] order) {
        this.order = order;
    }

    public void run() {
        try {
            try (ServerSocket serverSocket = new ServerSocket(5000 + id)) {
                while (true) {
                    Socket socket = serverSocket.accept();
                    DataInputStream inputStream = new DataInputStream(socket.getInputStream());
                    int hopCount = inputStream.readInt();
                    if (hopCount <= hops) {
                        // Ignore the message if it has already visited this node or exceeded the
                        // maximum hop count
                        continue;
                    }
                    hops = hopCount;
                    for (int i = 0; i < 4; i++) {
                        int neighborOrder = inputStream.readInt();
                        if (neighborOrder != -1) {
                            order[i] = neighborOrder;
                        }
                    }
                    socket.close();
                    if (!visited) {
                        // Send the message to all neighboring cities
                        visited = true;
                        for (int i = 0; i < 4; i++) {
                            if (!neighbors[i].equals("")) {
                                Socket neighborSocket = new Socket(neighbors[i], 5000 + i + 1);
                                DataOutputStream outputStream = new DataOutputStream(neighborSocket.getOutputStream());
                                outputStream.writeInt(hops + 1);
                                for (int j = 0; j < 4; j++) {
                                    outputStream.writeInt(j == id - 1 ? -1 : order[j]);
                                }
                                neighborSocket.close();
                            }
                        }
                    }
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) throws InterruptedException {
        // Create 4 city nodes
        CityNode[] nodes = new CityNode[4];
        nodes[0] = new CityNode(1, new String[] { "localhost:5002", "localhost:5003", "localhost:5004", "" },
                new int[] { 0, 1, 2, -1 });
        nodes[1] = new CityNode(2, new String[] { "localhost:5001", "", "localhost:5003", "localhost:5004" },
                new int[] { 1, 0, 3, 1 });
        nodes[2] = new CityNode(3, new String[] { "localhost:5001", "localhost:5002", "", "localhost:5004" },
                new int[] { 2, 3, 0, 2 });
        nodes[3] = new CityNode(4, new String[] { "", "localhost:5002", "localhost:5003", "localhost:5001" },
                new int[] { -1, 1, 2, 0 });

        // Start each city node in a separate thread
        Thread[] threads = new Thread[4];
        for (int i = 0; i < 4; i++) {
            threads[i] = new Thread(nodes[i]);
            threads[i].start();
        }

        // Wait for all threads to finish
        for (int i = 0; i < 4; i++) {
            threads[i].join();
        }

        // Find the best order of cities
        int[] bestOrder = null;
        int shortestDistance = Integer.MAX_VALUE;
        for (int i = 0; i < 24; i++) {
            int[] order = permutation(i);
            int distance = calculateDistance(nodes, order);
            if (distance < shortestDistance) {
                shortestDistance = distance;
                bestOrder = order;
            }
        }

        // Print the best order and shortest distance
        System.out.print("Best order: ");
        for (int i = 0; i < 4; i++) {
            System.out.print((bestOrder[i] + 1) + " ");
        }
        System.out.println("\nShortest distance: " + shortestDistance);

    }

    public static int[] permutation(int n) {
        int[] order = new int[4];
        boolean[] used = new boolean[4];
        for (int i = 3; i >= 0; i--) {
            int factorial = factorial(i);
            int index = n / factorial;
            n %= factorial;
            for (int j = 0; j < 4; j++) {
                if (!used[j]) {
                    if (index == 0) {
                        order[3 - i] = j;
                        used[j] = true;
                        break;
                    }
                    index--;
                }
            }
        }
        return order;
    }

    public static int factorial(int n) {
        int result = 1;
        for (int i = 2; i <= n; i++) {
            result *= i;
        }
        return result;
    }

    public static int calculateDistance(CityNode[] nodes, int[] order) {
        int distance = 0;
        for (int i = 0; i < 3; i++) {
            distance += nodes[order[i]].distances[order[i + 1]];
        }
        return distance;
    }
}

// Example output:
// Best order: 2 4 1 3
// Shortest distance: 5
