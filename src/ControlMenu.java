import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.Socket;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.Arrays;

public class ControlMenu implements ActionListener {

    private Plateau plateau;
    private Fenetre fenetre;
    private Controleur controleur;

    public ControlMenu(Plateau plateau, Fenetre fenetre, Controleur controleur) {
        this.plateau = plateau;
        this.fenetre = fenetre;
        this.controleur = controleur;
        fenetre.setControlMenu(this);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getSource().equals(fenetre.getNewGameItem())) {
            if (plateau.isPromotionIsOpen()) fenetre.getPromotionFrame().dispose();
            if (plateau.isPartiePersoIsOpen()) fenetre.getPartiePersoFrame().dispose();
            Plateau plateau = new Plateau();
            fenetre.setP(plateau);
            fenetre.creerFenetre();
            fenetre.initPromotion();
            fenetre.getFenetre().removeAll();
            fenetre.getFenetre().revalidate();
            fenetre.getFenetre().repaint();
            fenetre.addToWindow();
            controleur.newGame(plateau, fenetre);
            fenetre.pack();
            fenetre.getFenetre().setVisible(true);
        } else if (e.getSource().equals(fenetre.getReseauItem())) {
            System.out.println("Réseau");
        } else if (e.getSource().equals(fenetre.getChronoGameItem())) {
            if (plateau.isPromotionIsOpen()) fenetre.getPromotionFrame().dispose();
            if (plateau.isPartiePersoIsOpen()) fenetre.getPartiePersoFrame().dispose();
            Plateau plateau = new Plateau();
            plateau.setPartieChrono(true);
            fenetre.setP(plateau);
            fenetre.creerFenetre();
            fenetre.initPromotion();
            fenetre.getFenetre().removeAll();
            fenetre.getFenetre().revalidate();
            fenetre.getFenetre().repaint();
            fenetre.addToWindow();
            controleur.newGame(plateau, fenetre);
            fenetre.pack();
            fenetre.getFenetre().setVisible(true);
        } else if (e.getSource().equals(fenetre.getTrainingGameItem())) {
            if (plateau.isPromotionIsOpen()) fenetre.getPromotionFrame().dispose();
            if (plateau.isPartiePersoIsOpen()) fenetre.getPartiePersoFrame().dispose();
            Plateau plateau = new Plateau("Partie perso");
            fenetre.setP(plateau);
            fenetre.creerFenetre();
            fenetre.initPartiePerso();
            fenetre.partiePersoBox();
            fenetre.getFenetre().removeAll();
            fenetre.getFenetre().revalidate();
            fenetre.getFenetre().repaint();
            fenetre.addToWindow();
            controleur.newGame(plateau, fenetre);
            fenetre.pack();
            fenetre.getFenetre().setVisible(true);
        } else if (e.getSource().equals(fenetre.getVersionGrand())) {
            for (int i = 0; i < fenetre.getJb().length; i++) {
                for (int j = 0; j < fenetre.getJb().length; j++) {
                    fenetre.getJb()[i][j].setPreferredSize(new Dimension(100, 100));
                    updateIcon(i, j, "100", plateau.getCurrentSkin());
                }
            }
            fenetre.pack();
        } else if (e.getSource().equals(fenetre.getVersionPetit())) {
            for (int i = 0; i < fenetre.getJb().length; i++) {
                for (int j = 0; j < fenetre.getJb().length; j++) {
                    fenetre.getJb()[i][j].setPreferredSize(new Dimension(50, 50));
                    updateIcon(i, j, "50", plateau.getCurrentSkin());
                }
            }
            fenetre.pack();
        } else if (e.getSource().equals(fenetre.getQuitItem())) {
            System.exit(0);
        }
    }

    public void setPlateau(Plateau plateau) {
        this.plateau = plateau;
    }

    public void setFenetre(Fenetre fenetre) {
        this.fenetre = fenetre;
    }

    private boolean isInstance(Object o, Class c) {
        return c.isAssignableFrom(o.getClass());
    }

    private void updateIcon(int i, int j, String size, String currentColor){
        if(plateau.getEtat()[i][j] != null){
            char type = plateau.getEtat()[i][j].getClass().toString().charAt(6);
            switch (plateau.getEtat()[i][j].getCouleur()){
                case BLANC:
                    fenetre.getJb()[i][j].setIcon(new ImageIcon("res/"+ size + "/"+ currentColor + "/"+ type + "1.png"));
                    fenetre.getJb()[i][j].setDisabledIcon(new ImageIcon("res/"+ size + "/"+ currentColor + "/"+ type + "1.png"));
                    break;
                case NOIR:
                    fenetre.getJb()[i][j].setIcon(new ImageIcon("res/"+ size + "/"+ currentColor + "/"+ type + "2.png"));
                    fenetre.getJb()[i][j].setDisabledIcon(new ImageIcon("res/"+ size + "/" + currentColor + "/" + type + "2.png"));
                    break;
            }
        }
    }
}
