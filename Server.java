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
                System.out.println("Accepted connection from Client " + (i + 1) + ": " + clientSockets[i]);
            }

            // Send city order to clients
            for (int i = 0; i < 4; i++) {
                OutputStream os = clientSockets[i].getOutputStream();
                os.write((i + 1 + "").getBytes());
                os.flush();
                System.out.println("Sent city order to Client " + (i + 1));
            }

            // Receive TSP solution from clients
            int[] tspSolution = new int[4];
            for (int i = 0; i < 4; i++) {
                InputStream is = clientSockets[i].getInputStream();
                StringBuilder tspSolutionStr = new StringBuilder();
                int character;
                while ((character = is.read()) != -1) {
                    if (character == '\n') {
                        break;
                    }
                    tspSolutionStr.append((char) character);
                }
                String[] tspSolutionParts = tspSolutionStr.toString().split(",");
                tspSolution[i] = Integer.parseInt(tspSolutionParts[i]);
                System.out.println("Received TSP solution from Client " + (i + 1) + ": " + tspSolutionStr);
            }

            // Send TSP solution to clients
            for (int i = 0; i < 4; i++) {
                OutputStream os = clientSockets[i].getOutputStream();
                String tspSolutionStr = tspSolution[0] + "," + tspSolution[1] + "," + tspSolution[2] + ","
                        + tspSolution[3] + "\n";
                os.write(tspSolutionStr.getBytes());
                os.flush();
                System.out.println("Sent TSP solution to Client " + (i + 1) + ": " + tspSolutionStr);
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
