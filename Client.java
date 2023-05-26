import java.io.*;
import java.net.*;

public class Client {
    public static void main(String[] args) {
        if (args.length != 8) {
            System.out.println("Invalid number of arguments. Please provide City Order and weights.");
            return;
        }

        try {
            Socket socket = new Socket("localhost", 8080);
            System.out.println("Connected to server.");

            // Send City Order to server
            String cityOrder = args[0] + "," + args[1] + "," + args[2] + "," + args[3];
            OutputStream os = socket.getOutputStream();
            os.write(cityOrder.getBytes());
            os.flush();

            // Send weights to server
            String weights = args[4] + "," + args[5] + "," + args[6] + "," + args[7];
            os.write(weights.getBytes());
            os.flush();

            // Receive TSP solution from server
            InputStream is = socket.getInputStream();
            BufferedReader reader = new BufferedReader(new InputStreamReader(is));
            String line = reader.readLine();
            String[] tspSolution = line.split(",");
            int[] tsp = new int[4];
            for (int i = 0; i < 4; i++) {
                tsp[i] = Integer.parseInt(tspSolution[i]);
            }

            // Print TSP solution
            System.out.println("TSP Solution:");
            for (int i = 0; i < 4; i++) {
                System.out.println("City " + tsp[i] + ": " + getShortestPath(args, tsp[i]));
                System.out.println("Weight: " + getWeight(args, tsp[i]));
            }

            // Close connection
            socket.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static String getShortestPath(String[] args, int city) {
        StringBuilder path = new StringBuilder();
        for (int i = 0; i < 4; i++) {
            if (Integer.parseInt(args[i]) == city) {
                path.append((char) ('A' + i));
                break;
            }
        }
        for (int i = 0; i < 3; i++) {
            int nextCity = Integer.parseInt(args[(i + 1) % 4]);
            path.append(" -> ").append((char) ('A' + nextCity - 1));
        }
        return path.toString();
    }

    private static int getWeight(String[] args, int city) {
        int index = 4 + city - 1;
        return Integer.parseInt(args[index]);
    }
}
