import javax.swing.*;
import javax.swing.border.Border;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

/**
 * Created by khan on 01/06/17.
 */

public class ControlPartieChrono extends JFrame implements ActionListener {

    // Attributs
    private Fenetre f;
    private Plateau p;
    private Timer t;

    // Constantes
    private final int DELAY = 10;
    private final int NB_MIN = 60;
    private final int NB_SEC = 60;
    private final int NB_CSEC = 100;
    private final int MAX_CHAR = 2;
    private final int LIMIT = 0;
    private final int MAX_TIME = 120;


    //Constructeur de la classe
    public ControlPartieChrono(Fenetre f, Plateau p){
        this.t = new Timer (DELAY, this); // On initialise le timer
        this.f = f;
        this.p = p;
        boolean dialog = true;
        while (dialog) { // On demande a definir le temps entre chaque tour
            this.p.setInput(JOptionPane.showInputDialog("Temps en seconde entre chaque coup"));
            if(this.p.getInput()==null || this.p.getInput().isEmpty()) this.p.setInput("NULL");
            if (testStringInt(this.p.getInput()) && (Integer.parseInt(this.p.getInput())>LIMIT && Integer.parseInt(this.p.getInput())<=MAX_TIME)) {
                dialog = false;
            } else {
                JOptionPane.showMessageDialog(null, "Veuillez rentrer un nombre de seconde entre 1 et 120 merci.", "Alert", JOptionPane.INFORMATION_MESSAGE);
                dialog = true;
            }
        }
        this.p.setRestTime(Integer.parseInt(this.p.getInput())*NB_CSEC); // On mets en centiseconde le nombre de seconde choisit
        this.t.start(); // On demarre le timer
        this.p.setTour(Plateau.couleur.BLANC); // Les blancs commencent toujours
        this.f.chronoBox(); // On initialise la frame du chrono
        this.p.setPartieChrono(true); // Sécurité
    }
    

    @Override
    public void actionPerformed(ActionEvent actionEvent) {
        if(p.estEchecEtMaths(Plateau.couleur.BLANC) || p.estEchecEtMaths(Plateau.couleur.NOIR) || !p.getPartieChrono()) { this.t.stop(); } // Si c'est gagné ou la fenetre est férmé on arrete
        if(this.p.getRestTime()>LIMIT){
            this.f.getTimer().setText("Temps restant: "+getMinute()+":"+getSecondes()+":"+getCentiSecondes()); // Affiche le temps restant
            this.p.setRestTime((int) (this.p.getRestTime()-1)); // Décrémente toutes les centisecondes
            if (this.p.getTour() != p.getQuiJoue()) {this.p.setRestTime(Integer.parseInt(this.p.getInput()) * NB_CSEC); this.p.setTour(p.getQuiJoue());} // Si quelqu'un a joué recharge le chrono
            repaint(); // Actualise le chrono toutes les centisecondes
        } else {
            this.t.stop(); // Le temps est écoulé on arrete
            if (p.getQuiJoue()== Plateau.couleur.BLANC) { // On verifie qui a rater son tour
                this.f.getTimer().setText("Temps écoulé aux noirs de jouer");
                f.messageToUser("Temps écoulé aux noirs de jouer");
                p.setQuiJoue(Plateau.couleur.NOIR); // C'est au noirs de jouer
            } else if (p.getQuiJoue()== Plateau.couleur.NOIR) {
                this.f.getTimer().setText("Temps écoulé aux blancs de jouer");
                f.messageToUser("Temps écoulé aux blancs de jouer");
                p.setQuiJoue(Plateau.couleur.BLANC); // C'est au blancs de jouer
            }
            clear(); // desactive les JButtons
            update(p.getPieceActivable(this.p.getQuiJoue())); // On actives les JButton du joueur a qui est le tour
            this.p.setRestTime(Integer.parseInt(this.p.getInput())*NB_CSEC); // On recharge
            this.t.start(); // Et on lance le chrono
            p.setPieceActive(null);
        }
    }

    public void clear() {
        Border emptyBorder = BorderFactory.createEmptyBorder(); // Création de bordure vide
        for (int i = 0; i < 8; i++) {
            for (int j = 0; j < 8; j++) {
                f.getJbX(i, j).setBorder(emptyBorder); //f.getJbX() retourne le JButton a la position x,y
                f.getJb()[i][j].setEnabled(false);
                if (i % 2 == 0 && j % 2 == 0) {
                    f.getJb()[i][j].setBackground(Color.WHITE);
                } else if (i % 2 != 0 && j % 2 != 0) {
                    f.getJb()[i][j].setBackground(Color.WHITE);
                } else {
                    f.getJb()[i][j].setBackground(Color.BLACK);
                }
            }

        }
    }
    // Donnes le nombre d'heure en fonction des centisecondes
    public String getHeure(){
        if(String.valueOf((int)Math.floor(this.p.getRestTime()/(NB_MIN*NB_SEC*NB_CSEC))).length()<MAX_CHAR) return String.valueOf(LIMIT)+String.valueOf((int)Math.floor(this.p.getRestTime()/(NB_MIN*NB_SEC*NB_CSEC)));
        return String.valueOf((int)Math.floor(this.p.getRestTime()/(NB_MIN*NB_SEC*NB_CSEC)));
    }
    // Donnes le nombre minute en fonction des centisecondes
    public String getMinute(){
        if(String.valueOf((int)Math.floor((this.p.getRestTime()/(NB_SEC*NB_CSEC))-(Integer.parseInt(getHeure())*NB_MIN))).length()<MAX_CHAR) return String.valueOf(LIMIT)+String.valueOf((int)Math.floor((this.p.getRestTime()/(NB_SEC*NB_CSEC))-(Integer.parseInt(getHeure())*60)));
        return String.valueOf((int)Math.floor((this.p.getRestTime()/(NB_SEC*NB_CSEC))-(Integer.parseInt(getHeure())*NB_MIN)));
    }
    // Donnes le nombre secondes en fonction des centisecondes
    public String getSecondes(){
        if(String.valueOf((int)Math.floor((this.p.getRestTime()/(NB_CSEC))-(Integer.parseInt(getMinute())*NB_SEC)-(Integer.parseInt(getHeure())*NB_MIN*NB_SEC))).length()<MAX_CHAR) return String.valueOf(LIMIT)+String.valueOf((int)Math.floor((this.p.getRestTime()/(NB_CSEC))-(Integer.parseInt(getMinute())*NB_SEC)-(Integer.parseInt(getHeure())*NB_MIN*NB_SEC)));
        return String.valueOf((int)Math.floor((this.p.getRestTime()/(NB_CSEC))-(Integer.parseInt(getMinute())*NB_SEC)-(Integer.parseInt(getHeure())*NB_MIN*NB_SEC)));
    }
    // Donnes centisecondes
    public String getCentiSecondes(){
        if(String.valueOf((int)Math.floor((this.p.getRestTime())-(Integer.parseInt(getSecondes())*NB_CSEC)-(Integer.parseInt(getMinute())*NB_SEC*NB_CSEC)-(Integer.parseInt(getHeure())*NB_MIN*NB_SEC*NB_CSEC))).length()<MAX_CHAR) return String.valueOf(LIMIT)+String.valueOf((int)Math.floor((this.p.getRestTime())-(Integer.parseInt(getSecondes())*NB_CSEC)-(Integer.parseInt(getMinute())*NB_SEC*NB_CSEC)-(Integer.parseInt(getHeure())*NB_MIN*NB_SEC*NB_CSEC)));
        return String.valueOf((int)Math.floor((this.p.getRestTime())-(Integer.parseInt(getSecondes())*NB_CSEC)-(Integer.parseInt(getMinute())*NB_SEC*NB_CSEC)-(Integer.parseInt(getHeure())*NB_MIN*NB_SEC*NB_CSEC)));
    }

    // Methode pour verifier qu'un String est un int
    public static boolean testStringInt(String str) {
        for (char k : str.toCharArray())
        {
            if (!Character.isDigit(k)) return false;
        }
        return true;
    }

    public void update(Position[] tableauPosition) {
        Position positionTemporaire;
        for (int i = 0; i < tableauPosition.length; i++) { // Parcour du tableau de position
            positionTemporaire = tableauPosition[i]; // On met la position i du tableau dans une variable pour plus de lisibilité
            if (p.getEtat()[positionTemporaire.getY()][positionTemporaire.getX()] == null) {
                f.getJbX(positionTemporaire.getY(), positionTemporaire.getX()).setBackground(Color.GRAY);
                f.getJbX(positionTemporaire.getY(), positionTemporaire.getX()).setBorder(BorderFactory.createLineBorder(Color.BLACK, 2)); // Bordure noir// On récupere le JButton qui est a la position recupéré dans le tableau et on lui applique un traitement
            } else if (p.getEtat()[positionTemporaire.getY()][positionTemporaire.getX()].getCouleur() == p.getQuiJoue()) {
                f.getJbX(positionTemporaire.getY(), positionTemporaire.getX()).setBorder(BorderFactory.createLineBorder(Color.GREEN, 3)); // Bordure noir
            } else {
                f.getJbX(positionTemporaire.getY(), positionTemporaire.getX()).setBorder(BorderFactory.createLineBorder(Color.RED, 3)); // Bordure noir
                f.getJbX(positionTemporaire.getY(), positionTemporaire.getX()).setBackground(Color.GRAY);
            }


            f.getJb()[tableauPosition[i].getY()][tableauPosition[i].getX()].setEnabled(true);
        }
    }


}

