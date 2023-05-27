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

            // Send city order to clients
            for (int i = 0; i < 4; i++) {
                OutputStream os = clientSockets[i].getOutputStream();
                os.write((i + 1 + "").getBytes());
            }

            // Receive updated city order from clients
            String[] cityOrder = new String[4];
            for (int i = 0; i < 4; i++) {
                InputStream is = clientSockets[i].getInputStream();
                BufferedReader reader = new BufferedReader(new InputStreamReader(is));
                cityOrder[i] = reader.readLine();
            }

            // Calculate TSP solution
            int[] tspSolution = new int[4];
            for (int i = 0; i < 4; i++) {
                tspSolution[i] = Integer.parseInt(cityOrder[i]) - 10;
            }

            // Send TSP solution to clients
            for (int i = 0; i < 4; i++) {
                OutputStream os = clientSockets[i].getOutputStream();
                os.write((tspSolution[0] + "," + tspSolution[1] + "," + tspSolution[2] + "," + tspSolution[3])
                        .getBytes());
            }

            // Close connections
            for (int i = 0; i < 4; i++) {
                clientSockets[i].close();
            }
            serverSocket.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
