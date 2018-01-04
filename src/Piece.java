import javax.swing.*;
import java.awt.*;
import java.util.Arrays;


public abstract class Piece {

    protected ImageIcon icone;
    protected Plateau.couleur couleur;
    protected Position pos;
    protected Plateau p;

    public Piece(ImageIcon icone, Plateau.couleur couleur, int posX, int posY, Plateau p) {
        this.icone = icone;
        this.couleur = couleur;
        pos=new Position(posX,posY);
        this.p=p;
    }

    public abstract Position[] calculMouvement();

        // si le roi est en échec ne permet que les mouvements qui annule l'échec
        //regarde si le mouvement de cette piece met en échec, si oui ne permet que les mouvement selon l'axe



    
    /* Partie Baptiste */

    /*
    *Renvoie un tableau de postion filtré, les positions qui mettent en echec le Roi sont supprimés
    *Prend en paramètre la couleur de la pièce, la position de Roi et un tableau de position et la position de la pièce
    */
    public Position[] EchecSiDeplacement(Position[] deplacements){

        Plateau.couleur couleurPiece=this.couleur;
        Position posRoi = null;
        Roi king = null;

        if (couleurPiece== Plateau.couleur.BLANC){
            king = ((Roi)p.getPiecesBlanc()[4]);
        }
        else king = ((Roi)p.getPiecesNoir()[4]);

        Position posPiece = this.pos;

        Position[] positionPossible = new Position[deplacements.length];

        int compteur = 0;

        p.getEtat()[this.getPosY()][this.getPosX()] = null;

        if (this instanceof Roi) posRoi = this.pos;

        for (int i = 0; i < deplacements.length; i++) {
            Piece pieceTampon = p.getEtat()[deplacements[i].getY()][deplacements[i].getX()];
            p.getEtat()[deplacements[i].getY()][deplacements[i].getX()] = this;
            if (this instanceof Roi) pos=new Position(deplacements[i].getX(),deplacements[i].getY());
            if (!king.estEnEchec()){
                positionPossible[compteur] = deplacements[i];
                compteur++;
            }
            p.getEtat()[deplacements[i].getY()][deplacements[i].getX()] = pieceTampon;
        }

        if (this instanceof Roi) pos = posRoi;

        p.getEtat()[this.getPosY()][this.getPosX()] = this;

        Position[] PositionVerif = new Position[compteur];

        for (int i = 0; i < compteur; i++) {
            PositionVerif[i] = positionPossible[i];
        }
        return PositionVerif;
    }

/* Fin partie Baptiste */

    /*
    Si la piece n'a aucun mouvement possible elle ne s'active pas.
     */
    public boolean estActivable(){
        if (calculMouvement().length==0) return false;
        return true;
    }

    public Plateau getP() {
        return p;
    }

    public Plateau.couleur getCouleur() {
        return couleur;
    }

    public ImageIcon getIcone() {
        return icone;
    }

    public int getPosX() {
        return pos.getX();
    }

    public void setPosX(int posX) {
        this.pos.setX(posX);
    }

    public int getPosY() {
        return pos.getY();
    }

    public void setPosY(int posY) {
        this.pos.setY(posY);
    }

    public void setPos(Position pos) {
        this.pos = pos;
    }

    public Position getPos() {
        return pos;
    }

    // Déplace la piece active sur la case sélectionnée, si il y a une piece sur cette case met sa position a null
    public void deplacement (Position pos){
        Piece[][] e=this.p.getEtat();
        if(e[pos.getY()][pos.getX()]!=null){
            e[pos.getY()][pos.getX()].pos=null;
        }
        e[this.getPosY()][this.getPosX()]=null;
        this.pos=pos;
        e[pos.getY()][pos.getX()]=this;
    }
}
