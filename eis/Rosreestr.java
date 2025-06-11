import java.io.*;
import java.net.*;

public class Rosreestr {

    public static void main(String[] args) {
        int port = 8140;
        try (ServerSocket serverSocket = new ServerSocket(port)) {
            System.out.println("Rosreestr started on port: " + port);

            while (true) {
                Socket clientSocket = serverSocket.accept();
                new Thread(() -> handleClient(clientSocket)).start();
            }
        }
        catch (IOException ex) {
            ex.printStackTrace();
        }
    }

    private static void handleClient(Socket clientSocket) {
        try (
                BufferedReader in = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
                PrintWriter out = new PrintWriter(clientSocket.getOutputStream(), true)
        ) {
            String request = in.readLine();
            System.out.println("Received: " + request);
            out.println("Rosreestr response to: " + request);
        }
        catch (IOException e) {
            e.printStackTrace();
        }
    }

}