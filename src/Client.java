import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;

public class Client extends Thread {

    private Socket socket;
    private int port;
    private String host;
    private Plateau p;
    private Fenetre f;

    public Client(String host, int port) throws IOException {
        socket = new Socket(host, port);
    }

    public Client(String host, int port, Plateau p, Fenetre f) throws IOException {
        socket = new Socket(host, port);
        this.p = p;
        this.f = f;
    }

    @Override
    public void run() {
        while (true) {
            try {
                ControlButtonReseau.etat = new BufferedReader(new InputStreamReader(socket.getInputStream())).readLine();
            } catch (IOException e) {
                e.printStackTrace();
            }
            break;
        }
        Position[] positions = p.convertStringToEtatForReseau(ControlButtonReseau.etat);
        f.deplacerIcone(positions[0], positions[1]);
        for (int i = 0; i < p.getEtat().length; i++) {
            for (int j = 0; j < p.getEtat().length; j++) {
                if(p.getEtat()[i][j] != null && p.getEtat()[i][j].getCouleur() == p.getCouleurJoueur()){
                    f.getJb()[i][j].setEnabled(true);
                }
            }
        }
    }

    public void envoyer(Plateau plateau) {
        try {
            new PrintWriter(socket.getOutputStream(), true).println(plateau.convertEtatToStringForReseau());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public String ecouter() throws IOException {
        String etat = "";
        while (etat.equals("")) {
            etat = new BufferedReader(new InputStreamReader(socket.getInputStream())).readLine();
        }
        return etat;
    }
}
