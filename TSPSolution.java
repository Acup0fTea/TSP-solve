import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

public class TSPSolution {
    private static final int BASE_PORT = 8000;
    private static final int NUM_CITIES = 4;

    public static void main(String[] args) {
        // Start the server on City 1
        startServer(1);

        // Connect to the other cities as clients
        for (int city = 2; city <= NUM_CITIES; city++) {
            connectToServer(city);
        }
    }

    private static void startServer(int city) {
        new Thread(() -> {
            try {
                ServerSocket serverSocket = new ServerSocket(BASE_PORT + city);
                System.out.println("City " + city + " is waiting for connections...");

                while (true) {
                    Socket socket = serverSocket.accept();
                    new Thread(new CityHandler(socket, city)).start();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }).start();
    }

    private static void connectToServer(int city) {
        new Thread(() -> {
            try {
                Socket socket = new Socket("localhost", BASE_PORT + city);
                System.out.println("Connected to City " + city);

                DataOutputStream outputStream = new DataOutputStream(socket.getOutputStream());
                outputStream.writeInt(city);

                DataInputStream inputStream = new DataInputStream(socket.getInputStream());
                int receivedValue = inputStream.readInt();

                // Add the city number to the received value and forward it to all cities
                forwardValue(city, receivedValue);

                socket.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }).start();
    }

    private static void forwardValue(int senderCity, int value) {
        if (senderCity <= NUM_CITIES) {
            for (int city = 1; city <= NUM_CITIES; city++) {
                if (city != senderCity) {
                    try {
                        Socket socket = new Socket("127.0.0.1", BASE_PORT + city);
                        DataOutputStream outputStream = new DataOutputStream(socket.getOutputStream());
                        outputStream.writeInt(value + senderCity * 10 + city);
                        socket.close();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }
        } else {
            // Last city, print the final result
            System.out.println("Final result: " + value);
        }
    }

    static class CityHandler implements Runnable {
        private Socket socket;
        private int city;

        public CityHandler(Socket socket, int city) {
            this.socket = socket;
            this.city = city;
        }

        @Override
        public void run() {
            try {
                DataInputStream inputStream = new DataInputStream(socket.getInputStream());
                int receivedValue = inputStream.readInt();

                // Add the city number to the received value and forward it to all cities
                forwardValue(city, receivedValue);

                DataOutputStream outputStream = new DataOutputStream(socket.getOutputStream());
                outputStream.writeInt(receivedValue);
                socket.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}
