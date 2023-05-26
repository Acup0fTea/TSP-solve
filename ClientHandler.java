import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;

public class ClientHandler implements Runnable {
    private final Socket clientSocket;
    private final int cityId;

    public ClientHandler(Socket clientSocket, int cityId) {
        this.clientSocket = clientSocket;
        this.cityId = cityId;
    }

    @Override
    public void run() {
        try {
            InputStream inputStream = clientSocket.getInputStream();
            int value = inputStream.read();

            // Increment the value
            value += (cityId * 10);

            System.out.println("Received value " + value + " in City " + cityId);

            // Forward the incremented value to other cities
            for (int i = 2; i <= 4; i++) {
                Socket forwardSocket = new Socket("192.168.1.136", 8080 + i - 1);
                OutputStream outputStream = forwardSocket.getOutputStream();
                outputStream.write(value);
                outputStream.flush();
                System.out.println("Sent value " + value + " from City " + cityId + " to City " + i);
                forwardSocket.close();
            }

            clientSocket.close();
            System.out.println("Client " + cityId + " finished.");

        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
