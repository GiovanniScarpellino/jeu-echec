import javax.swing.*;

public class Cavalier extends Piece {

    public Cavalier(ImageIcon icone, Plateau.couleur couleur, int posX, int posY, Plateau p) {
        super(icone, couleur, posX, posY, p);
    }

    @Override
    public Position[] calculMouvement() {
        int nbMvt=0;
        int[][] mvt= new int[2][8];
        int y;
        int x;
        for (int i=0; i<8; i++){
            x= this.getPosX();
            y= this.getPosY();
            switch (i){
                case 0: x++; y+=2; break;
                case 1: x+=2; y++; break;
                case 2: x+=2; y--; break;
                case 3: x++; y-=2; break;
                case 4: x--; y-=2; break;
                case 5: x-=2; y--; break;
                case 6: x-=2; y++; break;
                case 7: x--; y+=2; break;
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
        return EchecSiDeplacement(mvtPossible);
    }
}
