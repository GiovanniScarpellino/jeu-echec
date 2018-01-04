import javax.swing.*;

public class Tour extends Piece {

    public Tour(ImageIcon icone, Plateau.couleur couleur, int posX, int posY, Plateau p) {
        super(icone, couleur, posX, posY, p);
    }

    @Override
    public Position[] calculMouvement() {
        int nbMvt=0;
        int[][] mvt= new int[2][14];
        boolean peutContinuer;
        int y;
        int x;
        for (int i=0; i<4; i++){
            peutContinuer=true;
            x= this.getPosX();
            y= this.getPosY();
            switch (i){
                case 0: y++; break;
                case 1: x++; break;
                case 2: y--; break;
                case 3: x--; break;
            }
                while (peutContinuer && (x<8 && x>=0 && y<8 && y>=0)) {
                    if (getP().getEtat()[y][x] != null) {
                        if (getP().getEtat()[y][x].getCouleur() != this.getCouleur()) {
                            mvt[0][nbMvt] = x;
                            mvt[1][nbMvt] = y;
                            nbMvt++;
                        }
                        peutContinuer = false;
                    }
                    else {
                        mvt[0][nbMvt] = x;
                        mvt[1][nbMvt] = y;
                        nbMvt++;
                        switch (i) {
                            case 0:
                                y++;
                                break;
                            case 1:
                                x++;
                                break;
                            case 2:
                                y--;
                                break;
                            case 3:
                                x--;
                                break;
                        }
                    }
                }

        }
        Position[] mvtPossible= new Position[nbMvt];
        for (int i=0; i<nbMvt; i++){
            mvtPossible[i]= new Position(mvt[0][i],mvt[1][i]);
        }
        //return mvtPossible;
        return EchecSiDeplacement(mvtPossible);
    }
}
