import com.sun.deploy.util.StringUtils;

import javax.swing.*;
import java.util.ArrayList;
import java.util.Arrays;

public class Plateau {
    private Piece[][] etat;
    private Piece pieceActive;
    private Piece[] piecesBlanc;
    private Piece[] piecesNoir;
    private couleur quiJoue;

    private boolean petitRoqueBlanc;
    private boolean grandRoqueBlanc;
    private boolean petitRoqueNoir;
    private boolean grandRoqueNoir;

    //Partie perso
    private boolean partiePerso;
    private ArrayList<Position> partiePersoPos;
    private boolean partiePersoIsOpen;

    //Partie chrono
    private boolean partieChrono;
    private String input;
    private double restTime;
    private Plateau.couleur tour;

    //Promotion
    private Piece piecePromotion;
    private boolean promotionIsOpen;

    //CustomSkin
    private String currentSkin;

    public enum couleur {
        BLANC, NOIR
    }

    public Plateau() {
        petitRoqueBlanc = true;
        grandRoqueBlanc = true;
        petitRoqueNoir = true;
        grandRoqueNoir = true;

        pieceActive = null;
        piecePromotion = null;
        quiJoue = couleur.BLANC;
        currentSkin = "BlancNoir";

        piecesNoir = new Piece[16];
        piecesNoir[0] = new Tour(new ImageIcon("res/100/BlancNoir/T2.png"), couleur.NOIR, 0, 0, this);
        piecesNoir[1] = new Cavalier(new ImageIcon("res/100/BlancNoir/C2.png"), couleur.NOIR, 1, 0, this);
        piecesNoir[2] = new Fou(new ImageIcon("res/100/BlancNoir/F2.png"), couleur.NOIR, 2, 0, this);
        piecesNoir[3] = new Dame(new ImageIcon("res/100/BlancNoir/D2.png"), couleur.NOIR, 3, 0, this);
        piecesNoir[4] = new Roi(new ImageIcon("res/100/BlancNoir/R2.png"), couleur.NOIR, 4, 0, this);
        piecesNoir[5] = new Fou(new ImageIcon("res/100/BlancNoir/F2.png"), couleur.NOIR, 5, 0, this);
        piecesNoir[6] = new Cavalier(new ImageIcon("res/100/BlancNoir/C2.png"), couleur.NOIR, 6, 0, this);
        piecesNoir[7] = new Tour(new ImageIcon("res/100/BlancNoir/T2.png"), couleur.NOIR, 7, 0, this);
        for (int i = 8; i < 16; i++) {
            piecesNoir[i] = new Pion(new ImageIcon("res/100/BlancNoir/P2.png"), couleur.NOIR, i - 8, 1, this);
        }

        piecesBlanc = new Piece[16];
        piecesBlanc[0] = new Tour(new ImageIcon("res/100/BlancNoir/T1.png"), couleur.BLANC, 0, 7, this);
        piecesBlanc[1] = new Cavalier(new ImageIcon("res/100/BlancNoir/C1.png"), couleur.BLANC, 1, 7, this);
        piecesBlanc[2] = new Fou(new ImageIcon("res/100/BlancNoir/F1.png"), couleur.BLANC, 2, 7, this);
        piecesBlanc[3] = new Dame(new ImageIcon("res/100/BlancNoir/D1.png"), couleur.BLANC, 3, 7, this);
        piecesBlanc[4] = new Roi(new ImageIcon("res/100/BlancNoir/R1.png"), couleur.BLANC, 4, 7, this);
        piecesBlanc[5] = new Fou(new ImageIcon("res/100/BlancNoir/F1.png"), couleur.BLANC, 5, 7, this);
        piecesBlanc[6] = new Cavalier(new ImageIcon("res/100/BlancNoir/C1.png"), couleur.BLANC, 6, 7, this);
        piecesBlanc[7] = new Tour(new ImageIcon("res/100/BlancNoir/T1.png"), couleur.BLANC, 7, 7, this);
        for (int i = 8; i < 16; i++) {
            piecesBlanc[i] = new Pion(new ImageIcon("res/100/BlancNoir/p1.png"), couleur.BLANC, i - 8, 6, this);
        }

        etat = new Piece[8][8];
        for (int i = 0; i < 8; i++) {
            etat[0][i] = piecesNoir[i];
            etat[1][i] = piecesNoir[i + 8];
            etat[7][i] = piecesBlanc[i];
            etat[6][i] = piecesBlanc[i + 8];
        }
    }

    public Plateau(String typePartie) {
        switch (typePartie) {
            case "Partie perso":
                petitRoqueBlanc = true;
                grandRoqueBlanc = true;
                petitRoqueNoir = true;
                grandRoqueNoir = true;
                partiePerso = true;
                partiePersoIsOpen = true;

                currentSkin = "BlancNoir";

                pieceActive = null;
                piecePromotion = null;
                quiJoue = null;

                partiePersoPos = new ArrayList<>();

                piecesNoir = new Piece[16];
                piecesBlanc = new Piece[16];

                etat = new Piece[8][8];
                break;
        }
    }

    // Active les pieces restantes du joueurs
    public Position[] getPieceActivable(couleur j) {
        Position[] pos = new Position[16];
        int cpt = 0;
        for (int i = 0; i < 16; i++) {
            if (j == couleur.NOIR) {
                if (piecesNoir[i] != null && piecesNoir[i].getPos() != null) {
                    if (piecesNoir[i].estActivable()) {
                        pos[cpt] = piecesNoir[i].getPos();
                        cpt++;
                    }
                }
            } else {
                if (piecesBlanc[i] != null && piecesBlanc[i].getPos() != null) {
                    if (piecesBlanc[i].estActivable()) {
                        pos[cpt] = piecesBlanc[i].getPos();
                        cpt++;
                    }
                }
            }
        }
        Position[] posPieceActivable = new Position[cpt];
        for (int i = 0; i < cpt; i++) {
            posPieceActivable[i] = pos[i];
        }
        return posPieceActivable;
    }

    public void updatePieceActivable(couleur j) {

        switch (j) {
            case BLANC:
                for (int i = 0; i < piecesBlanc.length; i++) {
                    if (piecesBlanc[i].getPos() != null) {
                        if (piecesBlanc[i].getPos().equals(piecePromotion.getPos())) {
                            piecesBlanc[i] = piecePromotion;
                            return;
                        }
                    }
                }
                break;
            case NOIR:
                for (int i = 0; i < piecesNoir.length; i++) {
                    if (piecesNoir[i].getPos() != null) {
                        if (piecesNoir[i].getPos().equals(piecePromotion.getPos())) {
                            piecesNoir[i] = piecePromotion;
                            return;
                        }
                    }
                }
                break;
        }
    }

    public void addPieceActivable(Piece piece) {
        switch (piece.getCouleur()) {
            case BLANC:
                if (piece instanceof Roi) {
                    piecesBlanc[4] = piece;
                } else {
                    for (int i = 0; i < piecesBlanc.length; i++) {
                        if (piecesBlanc[i] == null && i != 4) {
                            piecesBlanc[i] = piece;
                            return;
                        }
                    }
                }
                break;
            case NOIR:
                if (piece instanceof Roi) {
                    piecesNoir[4] = piece;
                } else {
                    for (int i = 0; i < piecesNoir.length; i++) {
                        if (piecesNoir[i] == null && i != 4) {
                            piecesNoir[i] = piece;
                            return;
                        }
                    }
                }
                break;
        }
    }

    public void removePieceActivable(Piece piece) {
        if (piece.getCouleur().equals(couleur.BLANC)) {
            for (int i = 0; i < piecesBlanc.length; i++) {
                if (piecesBlanc[i] != null) {
                    if (piecesBlanc[i].equals(piece)) {
                        piecesBlanc[i] = null;
                        return;
                    }
                }
            }
        } else {
            for (int i = 0; i < piecesNoir.length; i++) {
                if (piecesNoir[i] != null) {
                    if (piecesNoir[i].equals(piece)) {
                        piecesNoir[i] = null;
                        return;
                    }
                }
            }
        }
    }

    public boolean estEchecEtMaths(couleur col) {
        if (col == couleur.BLANC) {
            for (int i = 0; i < 16; i++) {
                if (piecesBlanc[i] != null) {
                    if (piecesBlanc[i].getPos() != null) {
                        if (piecesBlanc[i].estActivable()) {
                            return false;
                        }
                    }
                }
            }
        } else {
            for (int i = 0; i < 16; i++) {
                if (piecesNoir[i] != null) {
                    if (piecesNoir[i].getPos() != null) {
                        if (piecesNoir[i].estActivable()) {
                            return false;
                        }
                    }
                }
            }
        }
        return true;
    }

    public Piece[][] getEtat() {
        return etat;
    }

    public void setEtat(Piece[][] etat) {
        this.etat = etat;
    }

    public Piece getPieceActive() {
        return pieceActive;
    }

    public void setPieceActive(Piece pieceActive) {
        this.pieceActive = pieceActive;
    }

    public Piece[] getPiecesBlanc() {
        return piecesBlanc;
    }

    public void setPiecesBlanc(Piece[] piecesBlanc) {
        this.piecesBlanc = piecesBlanc;
    }

    public Piece[] getPiecesNoir() {
        return piecesNoir;
    }

    public void setPiecesNoir(Piece[] piecesNoir) {
        this.piecesNoir = piecesNoir;
    }

    public boolean pieceExiste(couleur col, Position pos) {
        switch (col) {
            case NOIR:
                for (Piece p : piecesNoir) {
                    if (p != null) {
                        if (p.getPos() != null && p.getPos().equals(pos))
                            return true;
                    }
                }
                break;
            case BLANC:
                for (Piece p : piecesBlanc) {
                    if (p != null) {
                        if (p.getPos() != null && p.getPos().equals(pos))
                            return true;
                    }
                }
                break;
        }
        return false;
    }

    public void setPiecePromotion(Piece piecePromotion) {
        this.piecePromotion = piecePromotion;
    }

    public Piece getPiecePromotion() {
        return piecePromotion;
    }

    /* --- ROQUE GETTERS SETTERS --- */
    public boolean peutPetitRoqueBlanc() {
        return petitRoqueBlanc;
    }

    public void setPetitRoqueBlanc(boolean petitRoqueBlanc) {
        this.petitRoqueBlanc = petitRoqueBlanc;
    }

    public boolean peutGrandRoqueBlanc() {
        return grandRoqueBlanc;
    }

    public void setGrandRoqueBlanc(boolean grandRoqueBlanc) {
        this.grandRoqueBlanc = grandRoqueBlanc;
    }

    public boolean peutPetitRoqueNoir() {
        return petitRoqueNoir;
    }

    public void setPetitRoqueNoir(boolean petitRoqueNoir) {
        this.petitRoqueNoir = petitRoqueNoir;
    }

    public boolean peutGrandRoqueNoir() {
        return grandRoqueNoir;
    }

    public void setGrandRoqueNoir(boolean grandRoqueNoir) {
        this.grandRoqueNoir = grandRoqueNoir;
    }

    public couleur getQuiJoue() {
        return quiJoue;
    }

    public void setQuiJoue(couleur quiJoue) {
        this.quiJoue = quiJoue;
    }

    //Convertis l'état en string
    public String convertEtatForHistory() {
        StringBuilder etat = new StringBuilder();
        for (int i = 0; i < this.etat.length; i++) {
            for (int j = 0; j < this.etat.length; j++) {
                try {
                    etat.append((this.etat[i][j].getClass().toString().split(" ")[1]).charAt(0));
                    etat.append(this.etat[i][j].getCouleur().toString().charAt(0));
                } catch (NullPointerException e) {
                    etat.append("-");
                }
                etat.append(" ");
            }
        }
        return etat.toString();
    }

    /* --- PARTIE PERSO --- */
    public boolean isPartiePerso() {
        return partiePerso;
    }

    public ArrayList<Position> getPartiePersoPos() {
        return partiePersoPos;
    }

    public void addPartiePersoPos(Position position) {
        partiePersoPos.add(position);
    }

    public void removePartiePersoPos(Position position) {
        partiePersoPos.remove(position);
    }

    public boolean isPartiePersoIsOpen() {
        return partiePersoIsOpen;
    }

    public void setPartiePersoIsOpen(boolean partiePersoIsOpen) {
        this.partiePersoIsOpen = partiePersoIsOpen;
    }

    public boolean isPromotionIsOpen() {
        return promotionIsOpen;
    }

    public void setPromotionIsOpen(boolean promotionIsOpen) {
        this.promotionIsOpen = promotionIsOpen;
    }

    //Partie chrono
    public void setInput(String input) {
        this.input = input;
    }

    public String getInput() {
        return input;
    }

    public void setRestTime(int restTime) {
        this.restTime = restTime;
    }

    public void setTour(couleur tour) {
        this.tour = tour;
    }

    public void setPartieChrono(boolean partieChrono) {
        this.partieChrono = partieChrono;
    }

    public double getRestTime() {
        return restTime;
    }

    public couleur getTour() {
        return tour;
    }

    public boolean getPartieChrono() {
        return partieChrono;
    }

    //Custom skin
    public String getCurrentSkin() {
        return currentSkin;
    }

    public void setCurrentSkin(String currentSkin) {
        this.currentSkin = currentSkin;
    }
}
