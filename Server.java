import java.io.*;
import java.net.*;

public class Server {
    public static void main(String[] args) {
        try {
            ServerSocket serverSocket = new ServerSocket(8080);
            System.out.println("Server started. Waiting for clients...");

            // Accept four client connections
            Socket[] clientSockets = new Socket[4];
            for (int i = 0; i < 4; i++) {
                clientSockets[i] = serverSocket.accept();
                System.out.println("Client " + (i + 1) + " connected.");
            }

            // Send order of cities to all clients
            for (int i = 0; i < 4; i++) {
                String message = String.valueOf(i + 1);
                OutputStream os = clientSockets[i].getOutputStream();
                os.write(message.getBytes());
            }

            // Receive updated order of cities and weights from clients
            int[][] cityOrders = new int[4][4];
            int[][] weights = new int[4][4];
            for (int i = 0; i < 4; i++) {
                InputStream is = clientSockets[i].getInputStream();
                BufferedReader reader = new BufferedReader(new InputStreamReader(is));

                // Receive updated order of cities
                String line = reader.readLine();
                String[] order = line.split(",");
                for (int j = 0; j < 4; j++) {
                    cityOrders[i][j] = Integer.parseInt(order[j]);
                }

                // Receive weights
                line = reader.readLine();
                String[] weightValues = line.split(",");
                for (int j = 0; j < 4; j++) {
                    weights[i][j] = Integer.parseInt(weightValues[j]);
                }
            }

            // Calculate TSP solution with weights
            int[] tspSolution = calculateTSP(cityOrders, weights);

            // Send TSP solution back to clients
            for (int i = 0; i < 4; i++) {
                String message = "";
                for (int j = 0; j < 4; j++) {
                    message += tspSolution[j] + ",";
                }
                OutputStream os = clientSockets[i].getOutputStream();
                os.write(message.getBytes());
            }

            // Close server socket
            serverSocket.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static int[] calculateTSP(int[][] cityOrders, int[][] weights) {
        // Perform TSP calculation here and return the solution
        // You can use any suitable algorithm for solving TSP
        // Modify the code below to show the TSP calculation
    
        int[] tspSolution = new int[] { 1, 2, 3, 4 };
    
        System.out.println("TSP Calculation:");
        System.out.println("City Orders: ");
        for (int i = 0; i < 4; i++) {
            for (int j = 0; j < 4; j++) {
                System.out.print(cityOrders[i][j] + " ");
            }
            System.out.println();
        }
        System.out.println("Weights: ");
        for (int i = 0; i < 4; i++) {
            for (int j = 0; j < 4; j++) {
                System.out.print(weights[i][j] + " ");
            }
            System.out.println();
        }
    
        // Add your TSP algorithm implementation here
        // Display intermediate steps as needed
    
        System.out.println("Intermediate Steps:");
        // Add your print statements to show intermediate steps
    
        // Final TSP solution
        System.out.println("TSP Solution:");
        for (int i = 0; i < 4; i++) {
            System.out.println("City " + (i + 1) + ": " + tspSolution[i]);
            System.out.println("Shortest Path: " + getShortestPath(cityOrders[i], tspSolution[i]));
            System.out.println("Weight: " + getWeight(weights[i], tspSolution[i]));
            System.out.println();
        }
    
        return tspSolution;
    }
    
    private static String getShortestPath(int[] cityOrder, int city) {
        StringBuilder path = new StringBuilder();
        for (int i = 0; i < 4; i++) {
            if (cityOrder[i] == city) {
                path.append((char) ('A' + i));
                break;
            }
        }
        for (int i = 0; i < 3; i++) {
            int nextCity = cityOrder[(i + 1) % 4];
            path.append(" -> ").append((char) ('A' + nextCity - 1));
        }
        return path.toString();
    }
    
    private static int getWeight(int[] weight, int city) {
        return weight[city - 1];
    }
}
