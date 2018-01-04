import javax.swing.*;

public class Pion extends Piece {

    private boolean aAvanceDe2Cases;

    public Pion(ImageIcon icone, Plateau.couleur couleur, int posX, int posY, Plateau p) {
        super(icone, couleur, posX, posY, p);
        this.aAvanceDe2Cases=false;
    }

    @Override
    public Position[] calculMouvement() {
        int nbMvt=0;
        int[][] mvt= new int[2][4];
        int x= this.getPosX();
        int y= this.getPosY();
            if (this.getCouleur() == Plateau.couleur.NOIR) {
                if (y+1<8){
                    if (getP().getEtat()[y + 1][x] == null) {
                        mvt[0][nbMvt] = x;
                        mvt[1][nbMvt] = y + 1;
                        nbMvt++;
                        if (y + 2 < 8) {
                            if (y == 1 && getP().getEtat()[y + 2][x] == null) {
                                mvt[0][nbMvt] = x;
                                mvt[1][nbMvt] = y + 2;
                                nbMvt++;
                            }
                        }
                    }
                }
                if (x+1<8 && y+1<8) {
                    if (getP().getEtat()[y + 1][x + 1] != null && getP().getEtat()[y+1][x+1].getCouleur() != this.getCouleur()) {
                        mvt[0][nbMvt] = x + 1;
                        mvt[1][nbMvt] = y + 1;
                        nbMvt++;
                    }
                }
                if (x-1>=0 && y+1<8) {
                    if (getP().getEtat()[y+ 1][x - 1] != null && getP().getEtat()[y+1][x-1].getCouleur() != this.getCouleur()) {
                        mvt[0][nbMvt] = x - 1;
                        mvt[1][nbMvt] = y + 1;
                        nbMvt++;
                    }
                }
            }
            if (this.getCouleur() == Plateau.couleur.BLANC) {
                if (y - 1 >= 0) {
                    if (getP().getEtat()[y - 1][x]== null) {
                        mvt[0][nbMvt] = x;
                        mvt[1][nbMvt] = y - 1;
                        nbMvt++;
                        if (y - 2 >= 0) {
                            if (y == 6 && getP().getEtat()[y - 2][x] == null) {
                                mvt[0][nbMvt] = x;
                                mvt[1][nbMvt] = y - 2;
                                nbMvt++;
                            }
                        }
                    }
                }
                if (x + 1 < 8 && y - 1 >= 0) {
                    if (getP().getEtat()[y - 1][x + 1] != null && getP().getEtat()[y-1][x+1].getCouleur() != this.getCouleur()) {
                        mvt[0][nbMvt] = x + 1;
                        mvt[1][nbMvt] = y - 1;
                        nbMvt++;
                    }
                }
                if (x - 1 >= 0 && y - 1 >= 0) {
            if (getP().getEtat()[y - 1][x - 1] != null && getP().getEtat()[y-1][x-1].getCouleur() != this.getCouleur()) {
                mvt[0][nbMvt] = x - 1;
                mvt[1][nbMvt] = y - 1;
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

    public void setPosX(int posX){
            if (getPosX()+2==posX || getPosX()-2==posX) aAvanceDe2Cases=true;
            setPosX(posX);
    }

}
