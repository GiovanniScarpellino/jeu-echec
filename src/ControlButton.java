import javax.swing.*;
import javax.swing.border.Border;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Scanner;

public class ControlButton implements ActionListener {
    private Fenetre f;
    private Plateau p;
    private ArrayList<String> historique;

    /* Constructeur */
    public ControlButton(Plateau p, Fenetre f) {
        this.f = f;
        this.p = p;
        historique = new ArrayList<>();
        f.setControlButton(this);
    }


    public void actionPerformed(ActionEvent e) {
        //On recupere la position du bouton
        int posXButton = 0;
        int posYButton = 0;
        for (int i = 0; i < 8; i++) {
            for (int j = 0; j < 8; j++) {
                if (e.getSource() == f.getJb()[i][j]) {
                    posXButton = j;
                    posYButton = i;
                }
            }
        }

        if(p.isPartiePerso() && p.getQuiJoue() == null){
            p.setQuiJoue(p.getEtat()[posYButton][posXButton].getCouleur());
        }


        // Regarde si il y a une piece Active et qu'on clic sur une case avec une piece de la même couleur (pour changer la piece que l'on veut bouger)
        if (p.getPieceActive() != null && p.getEtat()[posYButton][posXButton] != null && p.getPieceActive().getCouleur() == p.getEtat()[posYButton][posXButton].getCouleur()) {
            p.setPieceActive(null);
        }

        clear(); // desactive les jb
        // on cherche le jbutton correspondant
        if (p.getPieceActive() == null) {
            if (e.getSource() == f.getJb()[posYButton][posXButton]) {
                p.setPieceActive(p.getEtat()[posYButton][posXButton]);
                update(p.getPieceActive().calculMouvement());
                update(p.getPieceActivable(p.getPieceActive().getCouleur()));
            }
        } else {
            if (e.getSource() == f.getJb()[posYButton][posXButton]) {

                //Regarde si une des pieces à annulé un des roques possible
                if (p.getPieceActive() instanceof Roi) {
                    switch (p.getPieceActive().getCouleur()) {
                        case BLANC:
                            p.setGrandRoqueBlanc(false);
                            p.setPetitRoqueBlanc(false);
                            break;
                        case NOIR:
                            p.setGrandRoqueNoir(false);
                            p.setPetitRoqueNoir(false);
                            break;
                    }
                }
                if (p.getPieceActive() instanceof Tour) {
                    if (p.getPieceActive().getPosX() == 0) {
                        switch (p.getPieceActive().getCouleur()) {
                            case BLANC:
                                p.setGrandRoqueBlanc(false);
                                break;
                            case NOIR:
                                p.setGrandRoqueNoir(false);
                                break;
                        }
                    }
                    if (p.getPieceActive().getPosX() == 7) {
                        switch (p.getPieceActive().getCouleur()) {
                            case BLANC:
                                p.setPetitRoqueBlanc(false);
                                break;
                            case NOIR:
                                p.setPetitRoqueNoir(false);
                                break;
                        }
                    }
                }

                //DEPLACE LA TOUR EN CAS DE ROQUE
                if (p.getPieceActive() instanceof Roi) {
                    switch (p.getPieceActive().getCouleur()) {
                        case NOIR:
                            if (p.getPieceActive().getPosX() - posXButton == 3) { //GRAND ROQUE NOIR
                                Position origineTour = p.getPiecesNoir()[0].getPos();
                                Position destinationTour = new Position(origineTour.getX() + 2, origineTour.getY());
                                f.deplacerIcone(origineTour, destinationTour);
                                p.getPiecesNoir()[0].deplacement(destinationTour);
                            } else if (p.getPieceActive().getPosX() - posXButton == -2) { //PETIT ROQUE NOIR
                                Position origineTour = p.getPiecesNoir()[7].getPos();
                                Position destinationTour = new Position(origineTour.getX() - 2, origineTour.getY());
                                f.deplacerIcone(origineTour, destinationTour);
                                p.getPiecesNoir()[7].deplacement(destinationTour);
                            }
                            break;
                        case BLANC:
                            if (p.getPieceActive().getPosX() - posXButton == 3) { //GRAND ROQUE BLANC
                                Position origineTour = p.getPiecesBlanc()[0].getPos();
                                Position destinationTour = new Position(origineTour.getX() + 2, origineTour.getY());
                                f.deplacerIcone(origineTour, destinationTour);
                                p.getPiecesBlanc()[0].deplacement(destinationTour);
                            } else if (p.getPieceActive().getPosX() - posXButton == -2) { //PETIT ROQUE BLANC
                                Position origineTour = p.getPiecesBlanc()[7].getPos();
                                Position destinationTour = new Position(origineTour.getX() - 2, origineTour.getY());
                                f.deplacerIcone(origineTour, destinationTour);
                                p.getPiecesBlanc()[7].deplacement(destinationTour);
                            }
                            break;
                    }
                }

                // Deplace la piece
                f.deplacerIcone(p.getPieceActive().getPos(), new Position(posXButton, posYButton)); // Deplace dans la vue
                p.getPieceActive().deplacement(new Position(posXButton, posYButton)); // Deplace dans le modele

                // Regarde si le joueur en cours a gagné sinon active les pieces du joueurs adverses
                if (p.getPieceActive().getCouleur() == Plateau.couleur.BLANC) {
                    if (p.estEchecEtMaths(Plateau.couleur.NOIR)) {
                        f.messageToUser("le joueur Blanc a gagné");
                    } else {
                        p.setQuiJoue(Plateau.couleur.NOIR);
                        update(p.getPieceActivable(Plateau.couleur.NOIR));
                    }
                } else {
                    if (p.estEchecEtMaths(Plateau.couleur.BLANC)) {
                        f.messageToUser("le joueur Noir a gagné");
                    } else {
                        p.setQuiJoue(Plateau.couleur.BLANC);
                        update(p.getPieceActivable(Plateau.couleur.BLANC));
                    }
                }

                //Active la fenêtre pour la promotion
                if (p.getPieceActive() instanceof Pion) promotion();
                // cherche les pieces activables et actives les jb correspondant
                p.setPieceActive(null);
                updateHistory();
                System.out.println(p);
            }

        }
    }

    /*
  * Méthode pour enlever les point d'interrogation et les bordure des case selectionnable ( "?" )
  * ne prend aucun parametre et ne renvois rien
   */

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

    /*
    * Méthode pour afficher les case selectionné en fonction d'un tableau de position
    * Prend en paramètre un tableau de position
     */
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

    //Vérifie si le l'état du jeu est présent 3 fois
    private void updateHistory() {
        int same = 0;
        for (int i = 0; i < historique.size(); i++) {
            if (historique.get(i).equals(p.convertEtatForHistory())) same++;
            if (same == 3) f.messageToUser("Vous pouvez demander le nul !");
        }
        historique.add(p.convertEtatForHistory());
    }

    private void promotion() {
        switch (p.getPieceActive().getCouleur()) {
            case BLANC:
                if (p.getPieceActive().getPosY() == 0) {
                    f.choixPieceBox("1");
                    p.setPromotionIsOpen(true);
                }
                break;
            case NOIR:
                if (p.getPieceActive().getPosY() == 7) {
                    f.choixPieceBox("2");
                    p.setPromotionIsOpen(true);
                }
                break;
        }
    }

    public void setP(Plateau p) {
        this.p = p;
    }
}
