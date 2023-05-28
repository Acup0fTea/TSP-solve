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
                // Introduce a delay before accepting each client connection
                Thread.sleep(1000);
                clientSockets[i] = serverSocket.accept();
                System.out.println("Accepted connection from Client " + (i + 1) + ": " + clientSockets[i]);
            }

            // Receive city order from clients
            int[] cityOrder = new int[4];
            for (int i = 0; i < 4; i++) {
                BufferedReader reader = new BufferedReader(new InputStreamReader(clientSockets[i].getInputStream()));
                String cityOrderStr = reader.readLine();
                cityOrder[i] = Integer.parseInt(cityOrderStr);
                System.out.println("Received city order from Client " + (i + 1) + ": " + cityOrderStr);
            }

            // Send city order to clients
            for (int i = 0; i < 4; i++) {
                BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(clientSockets[i].getOutputStream()));
                writer.write(cityOrder[i] + "\n");
                writer.flush();
                System.out.println("Sent city order to Client " + (i + 1));
            }

            // Receive TSP solution from clients
            int[] tspSolution = new int[4];
            for (int i = 0; i < 4; i++) {
                BufferedReader reader = new BufferedReader(new InputStreamReader(clientSockets[i].getInputStream()));
                String tspSolutionStr = reader.readLine();
                tspSolution[i] = Integer.parseInt(tspSolutionStr);
                System.out.println("Received TSP solution from Client " + (i + 1) + ": " + tspSolutionStr);
            }

            // Send TSP solution to clients
            for (int i = 0; i < 4; i++) {
                BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(clientSockets[i].getOutputStream()));
                String tspSolutionStr = tspSolution[0] + "," + tspSolution[1] + "," + tspSolution[2] + ","
                        + tspSolution[3] + "\n";
                writer.write(tspSolutionStr);
                writer.flush();
                System.out.println("Sent TSP solution to Client " + (i + 1) + ": " + tspSolutionStr);
            }

            // Close connections
            for (int i = 0; i < 4; i++) {
                clientSockets[i].close();
            }
            serverSocket.close();
        } catch (IOException | InterruptedException e) {
            e.printStackTrace();
        }
    }
}
