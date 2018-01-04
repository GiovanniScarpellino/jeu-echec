import javafx.geometry.Pos;

import javax.swing.*;
import java.util.Arrays;

public class Roi extends Piece {

    public Roi(ImageIcon icone, Plateau.couleur couleur, int posX, int posY, Plateau p) {
        super(icone, couleur, posX, posY, p);
    }

    @Override
    public Position[] calculMouvement() {
        int nbMvt=0;
        int[][] mvt= new int[2][8];
        int x;
        int y;
        for (int i=0; i<8; i++){
            x= this.getPosX();
            y= this.getPosY();
            switch (i){
                case 0: y++; break;
                case 1: x++; y++; break;
                case 2: x++; break;
                case 3: x++; y--; break;
                case 4: y--; break;
                case 5: x--; y--; break;
                case 6: x--; break;
                case 7: x--; y++; break;
            }
            if (x<8 && x>=0 && y<8 && y>=0) {
                if (getP().getEtat()[y][x] != null) {
                    if (getP().getEtat()[y][x].getCouleur() != this.getCouleur()) {
                        mvt[0][nbMvt] = x;
                        mvt[1][nbMvt] = y;
                        nbMvt++;
                    }
                } else {
                    mvt[0][nbMvt] = x;
                    mvt[1][nbMvt] = y;
                    nbMvt++;
                }
            }
        }
        Position[] mvtPossible= new Position[nbMvt];
        for (int i=0; i<nbMvt; i++){
            mvtPossible[i]= new Position(mvt[0][i],mvt[1][i]);
        }

        //return mvtPossible;
        if(couleur == Plateau.couleur.NOIR && (p.peutGrandRoqueNoir() || p.peutPetitRoqueNoir()))
            return peutRoque(EchecSiDeplacement(mvtPossible));
        else if(couleur == Plateau.couleur.BLANC && (p.peutGrandRoqueBlanc() || p.peutPetitRoqueBlanc()))
            return peutRoque(EchecSiDeplacement(mvtPossible));
        else
            return EchecSiDeplacement(mvtPossible);
    }

    public boolean estEnEchec(){
        int start;
        Plateau.couleur couleurAdversaire = (getCouleur()==Plateau.couleur.NOIR) ? Plateau.couleur.BLANC : Plateau.couleur.NOIR;
        boolean piecePresente = false; //Regarde si une piece est presente sur le chemin
        Piece pieceActuelle; //Variable permettant de récupérer une Piece du tableau etat

        // --- Dame & Fou | Diagonale Haut-Gauche ---
        start = (getPosX()>getPosY()) ? getPosY() : getPosX();
        if(start - 1 >= 0){
            for (int i = 1 ; i<=start && !piecePresente ; i++){
                pieceActuelle = p.getEtat()[getPosY() - i][getPosX() - i];
                if(pieceActuelle != null){
                    piecePresente = true;
                    if(pieceActuelle.getCouleur() == couleurAdversaire && (pieceActuelle instanceof Dame || pieceActuelle instanceof Fou))
                        return true;
                }
            }
        }

        // --- Dame & Fou | Diagonale Haut-Droite ---
        piecePresente = false;
        start = ((7-getPosX()) > getPosY()) ? getPosY() : 7-getPosX();
        if(start - 1 >= 0){
            for (int i = 1 ; i<=start && !piecePresente ; i++){
                pieceActuelle = p.getEtat()[getPosY() - i][getPosX() + i];
                if(pieceActuelle != null){
                    piecePresente = true;
                    if(pieceActuelle.getCouleur() == couleurAdversaire && (pieceActuelle instanceof Dame || pieceActuelle instanceof Fou))
                        return true;
                }
            }
        }

        // --- Dame & Fou | Diagonale Bas-Gauche ---
        piecePresente = false;
        start = (getPosX() > (7-getPosY())) ? 7-getPosY() : getPosX();
        if(start - 1 >= 0){
            for (int i = 1 ; i<=start && !piecePresente ; i++){
                pieceActuelle = p.getEtat()[getPosY() + i][getPosX() - i];
                if(pieceActuelle != null){
                    piecePresente = true;
                    if(pieceActuelle.getCouleur() == couleurAdversaire && (pieceActuelle instanceof Dame || pieceActuelle instanceof Fou))
                        return true;
                }
            }
        }

        // --- Dame & Fou | Diagonale Bas-Droite ---
        piecePresente = false;
        start = ((7-getPosX()) > (7-getPosY())) ? 7-getPosY() : 7-getPosX();
        if(start - 1 >= 0){
            for (int i = 1 ; i<=start && !piecePresente ; i++){
                pieceActuelle = p.getEtat()[getPosY() + i][getPosX() + i];
                if(pieceActuelle != null){
                    piecePresente = true;
                    if(pieceActuelle.getCouleur() == couleurAdversaire && (pieceActuelle instanceof Dame || pieceActuelle instanceof Fou))
                        return true;
                }
            }
        }


        // --- Dame & Tour | Ligne Haut ---
        piecePresente = false;
        if(getPosY() > 0){
            for(int i = 1 ; i<=getPosY() && !piecePresente ; i++){
                pieceActuelle = p.getEtat()[getPosY() - i][getPosX()];
                if(pieceActuelle != null){
                    piecePresente = true;
                    if(pieceActuelle.getCouleur() == couleurAdversaire && (pieceActuelle instanceof Dame || pieceActuelle instanceof Tour))
                        return true;
                }
            }
        }

        // --- Dame & Tour | Ligne Droite ---
        piecePresente = false;
        if(getPosX() < 7){
            for(int i = 1 ; i<=(7-getPosX()) && !piecePresente ; i++){
                pieceActuelle = p.getEtat()[getPosY()][getPosX() + i];
                if(pieceActuelle != null){
                    piecePresente = true;
                    if(pieceActuelle.getCouleur() == couleurAdversaire && (pieceActuelle instanceof Dame || pieceActuelle instanceof Tour))
                        return true;
                }
            }
        }

        // --- Dame & Tour | Ligne Bas ---
        piecePresente = false;
        if(getPosY() < 7){
            for(int i = 1 ; i<=(7-getPosY()) && !piecePresente ; i++){
                pieceActuelle = p.getEtat()[getPosY() + i][getPosX()];
                if(pieceActuelle != null){
                    piecePresente = true;
                    if(pieceActuelle.getCouleur() == couleurAdversaire && (pieceActuelle instanceof Dame || pieceActuelle instanceof Tour))
                        return true;
                }
            }
        }

        // --- Dame & Tour | Ligne Gauche ---
        piecePresente = false;
        if(getPosX() > 0){
            for(int i = 1 ; i<=getPosX() && !piecePresente ; i++){
                pieceActuelle = p.getEtat()[getPosY()][getPosX() - i];
                if(pieceActuelle != null){
                    piecePresente = true;
                    if(pieceActuelle.getCouleur() == couleurAdversaire && (pieceActuelle instanceof Dame || pieceActuelle instanceof Tour))
                        return true;
                }
            }
        }


        // --- Cavalier | L Droite-Haut ---
        if(getPosX() + 2 <= 7 && getPosY() - 1 >= 0){
            pieceActuelle = p.getEtat()[getPosY() - 1][getPosX() + 2];
            if(pieceActuelle != null && pieceActuelle.getCouleur() == couleurAdversaire && pieceActuelle instanceof Cavalier)
                return true;
        }

        // --- Cavalier | L Droite-Bas ---
        if(getPosX() + 2 <= 7 && getPosY() + 1 <= 7){
            pieceActuelle = p.getEtat()[getPosY() + 1][getPosX() + 2];
            if(pieceActuelle != null && pieceActuelle.getCouleur() == couleurAdversaire && pieceActuelle instanceof Cavalier)
                return true;
        }

        // --- Cavalier | L Bas-Droite ---
        if(getPosX() + 1 <= 7 && getPosY() + 2 <= 7){
            pieceActuelle = p.getEtat()[getPosY() + 2][getPosX() + 1];
            if(pieceActuelle != null && pieceActuelle.getCouleur() == couleurAdversaire && pieceActuelle instanceof Cavalier)
                return true;
        }

        // --- Cavalier | L Bas-Gauche ---
        if(getPosX() - 1 >= 0 && getPosY() + 2 <= 7){
            pieceActuelle = p.getEtat()[getPosY() + 2][getPosX() - 1];
            if(pieceActuelle != null && pieceActuelle.getCouleur() == couleurAdversaire && pieceActuelle instanceof Cavalier)
                return true;
        }

        // --- Cavalier | L Gauche-Bas ---
        if(getPosX() - 2 >= 0 && getPosY() + 1 <= 7){
            pieceActuelle = p.getEtat()[getPosY() + 1][getPosX() - 2];
            if(pieceActuelle != null && pieceActuelle.getCouleur() == couleurAdversaire && pieceActuelle instanceof Cavalier)
                return true;
        }

        // --- Cavalier | L Gauche-Haut ---
        if(getPosX() - 2 >= 0 && getPosY() - 1 >= 0){
            pieceActuelle = p.getEtat()[getPosY() - 1][getPosX() - 2];
            if(pieceActuelle != null && pieceActuelle.getCouleur() == couleurAdversaire && pieceActuelle instanceof Cavalier)
                return true;
        }

        // --- Cavalier | L Haut-Gauche ---
        if(getPosX() - 1 >= 0 && getPosY() - 2 >= 0){
            pieceActuelle = p.getEtat()[getPosY() - 2][getPosX() - 1];
            if(pieceActuelle != null && pieceActuelle.getCouleur() == couleurAdversaire && pieceActuelle instanceof Cavalier)
                return true;
        }

        // --- Cavalier | L Haut-Droite ---
        if(getPosX() + 1 <= 7 && getPosY() - 2 >= 0){
            pieceActuelle = p.getEtat()[getPosY() - 2][getPosX() + 1];
            if(pieceActuelle != null && pieceActuelle.getCouleur() == couleurAdversaire && pieceActuelle instanceof Cavalier)
                return true;
        }


        // --- Pion | Roi Blanc ---
        if(getCouleur() == Plateau.couleur.BLANC && getPosY() >= 2){
            // Haut-Gauche
            if(getPosY() - 1 >= 0 && getPosX() - 1 >= 0){
                pieceActuelle = p.getEtat()[getPosY() - 1][getPosX() - 1];
                if(pieceActuelle != null && pieceActuelle.getCouleur() == couleurAdversaire && pieceActuelle instanceof Pion)
                    return true;
            }

            // Haut-Droite
            if(getPosY() - 1 >= 0 && getPosX() + 1 <= 7){
                pieceActuelle = p.getEtat()[getPosY() - 1][getPosX() + 1];
                if(pieceActuelle != null && pieceActuelle.getCouleur() == couleurAdversaire && pieceActuelle instanceof Pion)
                    return true;
            }
        }

        // --- Pion | Roi Noir ---
        if(getCouleur() == Plateau.couleur.NOIR && getPosY() <= 5){
            // Bas-Gauche
            if(getPosY() + 1 <= 7 && getPosX() - 1 >= 0){
                pieceActuelle = p.getEtat()[getPosY() + 1][getPosX() - 1];
                if(pieceActuelle != null && pieceActuelle.getCouleur() == couleurAdversaire && pieceActuelle instanceof Pion)
                    return true;
            }

            // Bas-Droite
            if(getPosY() + 1 <= 7 && getPosX() + 1 <= 7){
                pieceActuelle = p.getEtat()[getPosY() + 1][getPosX() + 1];
                if(pieceActuelle != null && pieceActuelle.getCouleur() == couleurAdversaire && pieceActuelle instanceof Pion)
                    return true;
            }
        }

        // --- Roi | Autour ---
        for (int i = -1; i <= 1; i++) {
            for (int j = -1; j <= 1; j++) {
                if(i != 0 || j != 0){
                    if(getPosX() + j >= 0 && getPosX() + j <= 7 && getPosY() + i >= 0 && getPosY() + i <= 7){
                        pieceActuelle = p.getEtat()[getPosY() + i][getPosX() + j];
                        if(pieceActuelle != null && pieceActuelle instanceof Roi)
                            return true;
                    }
                }
            }
        }

        // Ici, aucune pièce de l'adversaire ne peut capturer le roi => Pas en échec
        return false;
    }

    private Position[] peutRoque(Position[] mvtPossible){
        if(p.getPieceActive() == null)
            return mvtPossible;

        boolean peutPetitRoque = false;
        boolean peutGrandRoque = false;

        /* --- VERIFIE SI IL PEUT FAIRE DES ROQUES EN FONCTION DE LA COULEUR --- */
        switch(p.getPieceActive().getCouleur()){
            case NOIR:
                /* --- VERIFIE LA CAPACITE DE FAIRE DES ROQUES --- */
                if(!p.peutPetitRoqueNoir() && !p.peutGrandRoqueNoir())
                    return mvtPossible;

                /* --- PETIT ROQUE --- */
                if(p.peutPetitRoqueNoir() && p.getEtat()[0][7] instanceof Tour && p.getEtat()[0][7].getPos() != null){
                    if(! p.pieceExiste(Plateau.couleur.NOIR,new Position(getPosX()+1, getPosY()))
                            && ! p.pieceExiste(Plateau.couleur.NOIR,new Position(getPosX()+2, getPosY()))){
                        Position[] mouvementRoi = new Position[3];
                        for (int i = 0; i < 3; i++) {
                            mouvementRoi[i] = new Position(getPosX() + i, getPosY());
                        }
                        peutPetitRoque = EchecSiDeplacement(mouvementRoi).length == mouvementRoi.length;
                    }
                }

                /* --- GRAND ROQUE --- */
                if(p.peutGrandRoqueNoir() && p.getEtat()[0][0] instanceof Tour && p.getEtat()[0][0].getPos() != null){
                    if(! p.pieceExiste(Plateau.couleur.NOIR, new Position(getPosX()-1, getPosY()))
                            && ! p.pieceExiste(Plateau.couleur.NOIR, new Position(getPosX()-2, getPosY()))
                            && ! p.pieceExiste(Plateau.couleur.NOIR, new Position(getPosX()-3, getPosY()))){
                        Position[] mouvementRoi = new Position[4];
                        for (int i = 0; i < 4; i++) {
                            mouvementRoi[i] = new Position(getPosX() - i, getPosY());
                        }
                        peutGrandRoque = EchecSiDeplacement(mouvementRoi).length == mouvementRoi.length;
                    }
                }
                break;
            case BLANC:
                /* --- VERIFIE LA CAPACITE DE FAIRE DES ROQUES --- */
                if(!p.peutPetitRoqueBlanc() && !p.peutGrandRoqueBlanc())
                    return mvtPossible;

                /* --- PETIT ROQUE --- */
                if(p.peutPetitRoqueBlanc() && p.getEtat()[7][7] instanceof Tour && p.getEtat()[7][7].getPos() != null){
                    if(! p.pieceExiste(Plateau.couleur.BLANC,new Position(getPosX()+1, getPosY()))
                            && ! p.pieceExiste(Plateau.couleur.BLANC,new Position(getPosX()+2, getPosY()))){
                        Position[] mouvementRoi = new Position[3];
                        for (int i = 0; i < 3; i++) {
                            mouvementRoi[i] = new Position(getPosX() + i, getPosY());
                        }
                        peutPetitRoque = EchecSiDeplacement(mouvementRoi).length == mouvementRoi.length;
                    }
                }

                /* --- GRAND ROQUE --- */
                if(p.peutGrandRoqueBlanc() && p.getEtat()[7][0] instanceof Tour && p.getEtat()[7][0].getPos() != null){
                    if(! p.pieceExiste(Plateau.couleur.BLANC, new Position(getPosX()-1, getPosY()))
                            && ! p.pieceExiste(Plateau.couleur.BLANC, new Position(getPosX()-2, getPosY()))
                            && ! p.pieceExiste(Plateau.couleur.BLANC, new Position(getPosX()-3, getPosY()))){
                        Position[] mouvementRoi = new Position[4];
                        for (int i = 0; i < 4; i++) {
                            mouvementRoi[i] = new Position(getPosX() - i, getPosY());
                        }
                        peutGrandRoque = EchecSiDeplacement(mouvementRoi).length == mouvementRoi.length;
                    }
                }
                break;
        }

        /* --- ON AJOUTE LES POSITIONS AU TABLEAU DE POSITION POUR EFFECTUER LE ROQUE --- */
        Position[] mvtPossibleAvecRoque;
        if(peutPetitRoque && peutGrandRoque){
            mvtPossibleAvecRoque = new Position[mvtPossible.length + 2];
            for(int i = 0; i < mvtPossibleAvecRoque.length - 1; i++){
                if(i < mvtPossible.length){
                    mvtPossibleAvecRoque[i] = mvtPossible[i];
                }
                else{
                    mvtPossibleAvecRoque[i] = new Position(getPosX() + 2,getPosY());
                    mvtPossibleAvecRoque[i+1] = new Position(getPosX() - 3,getPosY());
                }
            }
        }
        else if(peutPetitRoque){
            mvtPossibleAvecRoque = new Position[mvtPossible.length + 1];
            for(int i = 0; i < mvtPossibleAvecRoque.length ; i++){
                if(i < mvtPossible.length){
                    mvtPossibleAvecRoque[i] = mvtPossible[i];
                }
                else{
                    mvtPossibleAvecRoque[i] = new Position(getPosX() + 2,getPosY());
                }
            }
        }
        else if(peutGrandRoque){
            mvtPossibleAvecRoque = new Position[mvtPossible.length + 1];
            for(int i = 0; i < mvtPossibleAvecRoque.length ; i++){
                if(i < mvtPossible.length){
                    mvtPossibleAvecRoque[i] = mvtPossible[i];
                }
                else{
                    mvtPossibleAvecRoque[i] = new Position(getPosX() - 3,getPosY());
                }
            }
        }
        else{
            mvtPossibleAvecRoque = mvtPossible;
        }

        return mvtPossibleAvecRoque;
    }
}
