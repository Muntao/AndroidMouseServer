package MouseServer;

import java.awt.AWTException;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author Muntao
 */

public class MouseServer {
    public static void main(String[] args) throws IOException {
        new Thread(new ServerThread()).start();
    }
}

class ServerThread implements Runnable {

    private ServerSocket serverSocket;
    private static final int PORT = 6000;
    private Socket socket = null;
    private final Parser parser = new Parser();

    @Override
    public void run() {
        try {
            serverSocket = new ServerSocket(PORT);
        } catch (IOException e) {
            e.printStackTrace();
        }
        while (!Thread.currentThread().isInterrupted()) {
            if (socket == null) {
                try {
                    System.out.println("WAITING...");
                    socket = serverSocket.accept();
                    System.out.println("ACCEPTED...");
                    BufferedReader input = new BufferedReader(new InputStreamReader(this.socket.getInputStream()));
                    String read;
                    while ((read = input.readLine()) != null) {
                        parser.parse(read);
                    }
                    socket = null;
                    System.out.println("DISCONNECTED...");
                } catch (IOException e) {
                    e.printStackTrace();
                } catch (AWTException ex) {
                    Logger.getLogger(ServerThread.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
        }
    }
}
