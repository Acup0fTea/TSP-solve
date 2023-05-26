import java.io.*;
import java.net.*;

public class Client {
    public static void main(String[] args) {
        try {
            Socket socket = new Socket("localhost", 8080);
            System.out.println("Connected to server.");

            // Receive order of cities from server
            InputStream is = socket.getInputStream();
            BufferedReader reader = new BufferedReader(new InputStreamReader(is));
            String line = reader.readLine();
            int cityOrder = Integer.parseInt(line);

            // Update city order
            int[] updatedOrder = new int[] { 10 + cityOrder, 20 + cityOrder, 30 + cityOrder, 40 + cityOrder };

            // Send updated order of cities back to server
            String message = updatedOrder[0] + "," + updatedOrder[1] + "," + updatedOrder[2] + "," + updatedOrder[3];
            OutputStream os = socket.getOutputStream();
            os.write(message.getBytes());

            // Receive TSP solution from server
            line = reader.readLine();
            String[] tspSolution = line.split(",");
            int[] tsp = new int[4];
            for (int i = 0; i < 4; i++) {
                tsp[i] = Integer.parseInt(tspSolution[i]);
            }

            // Print TSP solution
            System.out.println("TSP Solution:");
            for (int i = 0; i < 4; i++) {
                System.out.println("City " + (i + 1) + ": " + tsp[i]);
            }

            // Close connection
            socket.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
