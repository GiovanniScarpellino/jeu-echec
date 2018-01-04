import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;

public class Service extends Thread {

    private Socket socket;
    private int nbClient;
    private BufferedReader bufferedReader;
    private PrintWriter printWriter;

    public Service(Socket socket, int nbClient) {
        super();
        this.socket = socket;
        this.nbClient = nbClient;
    }

    @Override
    public void run() {
        try {
            bufferedReader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            printWriter = new PrintWriter(socket.getOutputStream(), true);
            switch (nbClient){
                case 1:
                    printWriter.println("B");
                    break;
                case 2:
                    printWriter.println("N");
                    break;
                default:
                    printWriter.println("A");
                    break;
            }
            bufferedReader.close();
            printWriter.close();
            socket.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        System.out.println("Connexion du client " + nbClient);
    }

    public Socket getSocket() {
        return socket;
    }

    public void setSocket(Socket socket) {
        this.socket = socket;
    }
}
