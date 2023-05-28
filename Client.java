import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.concurrent.TimeUnit;

public class Client implements Runnable {
    private int nodeNumber;
    private int port;
    private String message;

    public Client(int nodeNumber, int port) {
        this.nodeNumber = nodeNumber;
        this.port = port;
        this.message = Integer.toString(nodeNumber);
    }

    @Override
    public void run() {
        System.out.println("Node " + nodeNumber + " is running on port " + port);

        while (true) {
            try (ServerSocket serverSocket = new ServerSocket(port)) {
                Socket socket = serverSocket.accept();
                try (BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()))) {
                    String receivedMessage = in.readLine();

                    System.out.println("Node " + nodeNumber + " received: " + receivedMessage);

                    message += " " + receivedMessage;

                    for (int i = 1; i <= 4; i++) {
                        if (i != nodeNumber) {
                            try (Socket sendSocket = new Socket("localhost", 8080 + i);
                                    PrintWriter out = new PrintWriter(sendSocket.getOutputStream(), true)) {
                                out.println(message);
                            }
                        }
                    }

                    TimeUnit.SECONDS.sleep(2); // Sleep for 2 seconds between rounds
                }
            } catch (IOException | InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    public static void main(String[] args) {
        for (int i = 1; i <= 4; i++) {
            Client node = new Client(i, 8080 + i);
            new Thread(node).start();
        }
    }
}
