import java.io.IOException;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;

public class Server {
    public static void main(String[] args) {
        try {
            InetAddress serverAddress = InetAddress.getByName("192.168.1.136");
            ServerSocket serverSocket = new ServerSocket(8080, 0, serverAddress);

            System.out.println("Server started. Waiting for connections...");

            // Accept client connections
            for (int i = 0; i < 4; i++) {
                System.out.println("Waiting for client " + (i + 1) + " connection...");
                Socket clientSocket = serverSocket.accept();
                System.out.println("Client " + (i + 1) + " connected.");

                // Create a new thread to handle each client
                Thread clientThread = new Thread(new ClientHandler(clientSocket, i + 1));
                clientThread.start();
            }

        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
