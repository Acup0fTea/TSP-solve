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

            // Receive updated order of cities from clients
            int[][] cityOrders = new int[4][4];
            for (int i = 0; i < 4; i++) {
                InputStream is = clientSockets[i].getInputStream();
                BufferedReader reader = new BufferedReader(new InputStreamReader(is));
                String line = reader.readLine();
                String[] order = line.split(",");
                for (int j = 0; j < 4; j++) {
                    cityOrders[i][j] = Integer.parseInt(order[j]);
                }
            }

            // Calculate TSP solution
            int[] tspSolution = calculateTSP(cityOrders);

            // Send TSP solution back to clients
            for (int i = 0; i < 4; i++) {
                String message = "";
                for (int j = 0; j < 4; j++) {
                    message += tspSolution[j] + ",";
                }
                OutputStream os = clientSockets[i].getOutputStream();
                os.write(message.getBytes());
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static int[] calculateTSP(int[][] cityOrders) {
        // Perform TSP calculation here and return the solution
        // You can use any suitable algorithm for solving TSP
        // and update the code accordingly
        // For simplicity, this example just returns the original order

        int[] tspSolution = new int[] { 1, 2, 3, 4 };
        return tspSolution;
    }
}
