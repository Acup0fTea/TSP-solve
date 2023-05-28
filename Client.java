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
            BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream()));
            writer.write(cityOrder + "\n");
            writer.flush();
            System.out.println("Sent City Order to server.");

            // Receive City Order from server
            BufferedReader reader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            String receivedCityOrderStr = reader.readLine();
            int receivedCityOrder = Integer.parseInt(receivedCityOrderStr);
            System.out.println("Received City Order from server: " + receivedCityOrder);

            // Calculate TSP solution
            int tspSolution = calculateTSP(cityOrder);

            // Send TSP solution to server
            writer.write(tspSolution + "\n");
            writer.flush();
            System.out.println("Sent TSP solution to server.");

            // Receive complete TSP solution from server
            String tspSolutionStr = reader.readLine();
            String[] tspSolutionParts = tspSolutionStr.split(",");
            int[] completeSolution = new int[4];
            for (int i = 0; i < tspSolutionParts.length; i++) {
                completeSolution[i] = Integer.parseInt(tspSolutionParts[i]);
            }
            System.out.println("Received complete TSP solution from server: " + tspSolutionStr);

            // Print TSP solution
            System.out.println("TSP Solution for City " + cityOrder + ":");
            for (int i = 0; i < completeSolution.length; i++) {
                System.out.println("City " + (i + 1) + ": " + getShortestPath(completeSolution[i]));
            }

            // Close connection
            socket.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static int calculateTSP(int cityOrder) {
        // Replace this with your actual TSP calculation logic
        return cityOrder;
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
