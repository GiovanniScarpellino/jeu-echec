import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Arrays;

public class ControlPromotion implements ActionListener {

    private Plateau p;
    private Fenetre f;

    public ControlPromotion(Plateau p, Fenetre f) {
        this.p = p;
        this.f = f;
        f.setControlPromotion(this);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        JButton jButton = (JButton)e.getSource();
        Plateau.couleur couleur = p.getQuiJoue().equals(Plateau.couleur.BLANC) ? Plateau.couleur.NOIR : Plateau.couleur.BLANC;
        int posY = p.getQuiJoue().equals(Plateau.couleur.BLANC) ? 7 : 0;
        int posX = getPosX(posY);

        String[] cases = {"D1", "T1", "F1", "C1", "D2", "T2", "F2", "C2"};

        int i;
        for(i = 0; i < cases.length; i++)
            if(jButton.getIcon().toString().contains(cases[i])) break;

        switch(i) {
            case 0:
                p.setPiecePromotion(new Dame((ImageIcon) jButton.getIcon(), Plateau.couleur.BLANC, posX, posY, p));
                break;
            case 1:
                p.setPiecePromotion(new Tour((ImageIcon) jButton.getIcon(), Plateau.couleur.BLANC, posX, posY, p));
                break;
            case 2:
                p.setPiecePromotion(new Fou((ImageIcon) jButton.getIcon(), Plateau.couleur.BLANC, posX, posY, p));
                break;
            case 3:
                p.setPiecePromotion(new Cavalier((ImageIcon) jButton.getIcon(), Plateau.couleur.BLANC, posX, posY, p));
                break;
            case 4:
                p.setPiecePromotion(new Dame((ImageIcon) jButton.getIcon(), Plateau.couleur.NOIR, posX, posY, p));
                break;
            case 5:
                p.setPiecePromotion(new Tour((ImageIcon) jButton.getIcon(), Plateau.couleur.NOIR, posX, posY, p));
                break;
            case 6:
                p.setPiecePromotion(new Fou((ImageIcon) jButton.getIcon(), Plateau.couleur.NOIR, posX, posY, p));
                break;
            case 7:
                p.setPiecePromotion(new Cavalier((ImageIcon) jButton.getIcon(), Plateau.couleur.NOIR, posX, posY, p));
                break;
        }
        p.getEtat()[posY][posX] = p.getPiecePromotion();
        p.updatePieceActivable(couleur);
        f.getJbX(posY, posX).setIcon(p.getPiecePromotion().getIcone());
        f.getJbX(posY, posX).setDisabledIcon(p.getPiecePromotion().getIcone());
        f.getPromotionFrame().dispose();
        p.setPromotionIsOpen(false);
    }

    private int getPosX(int posY) {
        for (int i = 0; i < 8; i++) {
            if(p.getEtat()[posY][i] instanceof Pion) return i;
        }
        return -1;
    }

    public void setP(Plateau p) {
        this.p = p;
    }
}
