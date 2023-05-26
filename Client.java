import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;

public class Client {
    public static void main(String[] args) {
        int cityId = Integer.parseInt(args[0]);

        try {
            Socket socket = new Socket("192.168.1.136", 8080);
            System.out.println("Connected to server. Sending city ID: " + cityId);

            OutputStream outputStream = socket.getOutputStream();
            outputStream.write(cityId);
            outputStream.flush();

            InputStream inputStream = socket.getInputStream();
            int value = inputStream.read();
            System.out.println("Received value: " + value);

            // Increment the value
            value += (cityId * 10);

            // Forward the incremented value to other cities
            for (int i = 2; i <= 4; i++) {
                Socket clientSocket = new Socket("192.168.1.136", 8080 + i - 1);
                OutputStream clientOutputStream = clientSocket.getOutputStream();
                clientOutputStream.write(value);
                clientOutputStream.flush();
                System.out.println("Sent value " + value + " to City " + i);
                clientSocket.close();
            }

            socket.close();
            System.out.println("Client " + cityId + " finished.");

        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
