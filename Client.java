import java.io.*;
import java.net.*;

public class Client {
    public static void main(String[] args) {
        if (args.length != 1) {
            System.out.println("Invalid number of arguments. Please provide the city order.");
            return;
        }

        int cityOrder = Integer.parseInt(args[0]);

        try {
            Socket socket = new Socket("localhost", 8080);
            System.out.println("Connected to server.");

            // Send City Order to server
            OutputStream os = socket.getOutputStream();
            os.write((cityOrder + "").getBytes());
            os.flush();
            System.out.println("Sent City Order to server.");

            // Receive TSP solution from server
            InputStream is = socket.getInputStream();

            StringBuilder response = new StringBuilder();
            int character;
            while ((character = is.read()) != -1) {
                if (character == '\n') {
                    break;
                }
                response.append((char) character);
            }

            String[] tspSolution = response.toString().split(",");
            int[] tsp = new int[4];
            for (int i = 0; i < 4; i++) {
                tsp[i] = Integer.parseInt(tspSolution[i]);
            }

            // Print TSP solution
            System.out.println("Received TSP Solution from server.");
            System.out.println("TSP Solution for City " + cityOrder + ":");
            System.out.println("City " + tsp[0] + ": " + getShortestPath(tsp[0]));
            System.out.println("City " + tsp[1] + ": " + getShortestPath(tsp[1]));
            System.out.println("City " + tsp[2] + ": " + getShortestPath(tsp[2]));
            System.out.println("City " + tsp[3] + ": " + getShortestPath(tsp[3]));

            // Close connection
            socket.close();
            System.out.println("Connection closed.");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static String getShortestPath(int city) {
        String[] cities = { "A", "B", "C", "D" };
        int[] weights = { 2, 3, 4, 1 };
        StringBuilder path = new StringBuilder();
        for (int i = 0; i < 4; i++) {
            if (i == city - 1) {
                path.append(cities[i]);
                break;
            }
        }
        for (int i = 0; i < 3; i++) {
            int nextCity = (city + i) % 4 + 1;
            path.append(" -> ").append(cities[nextCity - 1]).append(" (Weight: ").append(weights[nextCity - 1])
                    .append(")");
        }
        return path.toString();
    }
}
