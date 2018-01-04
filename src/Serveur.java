import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;

public class Serveur extends Thread {

    private int nbClient;
    public static ArrayList<Service> services;
    private BufferedReader bufferedReader;
    private PrintWriter printWriter;
    private int player;

    @Override
    public void run() {
        try {
            ServerSocket serverSocket = new ServerSocket(1234);
            while (true) {
                Socket socket = serverSocket.accept();
                if (nbClient < 2) {
                    nbClient++;
                    Service service = new Service(socket, nbClient);
                    services.add(service);
                    service.start();
                } else{
                    printWriter = new PrintWriter(socket.getOutputStream(), true);
                    bufferedReader = new BufferedReader(new InputStreamReader(serverSocket.accept().getInputStream()));
                    String etat = bufferedReader.readLine();
                    System.out.println(etat);
                    printWriter.println(etat);
                    printWriter = new PrintWriter(socket.getOutputStream(), true);
                    printWriter.println(etat);
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void switchPlayer() {
        if (player == 0) {
            player = 1;
        } else {
            player = 0;
        }
    }

    public static void main(String[] args) {
        services = new ArrayList<>();
        new Serveur().start();
    }
}