import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Arrays;

public class ControlPartiePerso implements ActionListener {

    private Plateau p;
    private Fenetre f;

    private int posX;
    private int posY;

    public ControlPartiePerso(Plateau plateau, Fenetre fenetre) {
        p = plateau;
        f = fenetre;
        fenetre.setControlPartiePerso(this);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getSource() instanceof JButton) {
            JButton jButton = (JButton) e.getSource();

            if (jButton.equals(f.getLancerPartieJButton())) {
                onButtonPressed();
                f.getPartiePersoFrame().dispose();
                p.setPartiePersoIsOpen(false);
                parcoursPlateau();
                new ControlButton(p, f);
                new ControlPromotion(p, f);
                return;
            }

            if (jButton.equals(f.getEffacerPieceJButton())) {
                for (int i = 0; i < p.getPartiePersoPos().size(); i++) {
                    posX = p.getPartiePersoPos().get(i).getX();
                    posY = p.getPartiePersoPos().get(i).getY();
                    if (p.getEtat()[posX][posY] != null) {
                        p.removePieceActivable(p.getEtat()[posX][posY]);
                        p.getEtat()[posX][posY] = null;
                        f.getJbX(posX, posY).setIcon(null);
                        f.getJbX(posX, posY).setDisabledIcon(null);
                    }
                }
                onButtonPressed();
                return;
            }

            for (int i = 0; i < p.getPartiePersoPos().size(); i++) {
                posX = p.getPartiePersoPos().get(i).getX();
                posY = p.getPartiePersoPos().get(i).getY();
                if(p.getEtat()[posX][posY] != null) p.removePieceActivable(p.getEtat()[posX][posY]);

                String[] cases = {"D1", "R1", "T1", "F1", "C1", "P1", "D2", "R2", "T2", "F2", "C2", "P2"};
                int j;
                for(j = 0; j < cases.length; j++)
                    if(jButton.getIcon().toString().contains(cases[j])) break;
                switch(i) {
                    case 0:
                        p.getEtat()[posX][posY] = new Dame((ImageIcon) jButton.getIcon(), Plateau.couleur.BLANC, posY, posX, p);
                        break;
                    case 1:
                        p.getEtat()[posX][posY] = new Roi((ImageIcon) jButton.getIcon(), Plateau.couleur.BLANC, posY, posX, p);
                        break;
                    case 2:
                        p.getEtat()[posX][posY] = new Tour((ImageIcon) jButton.getIcon(), Plateau.couleur.BLANC, posY, posX, p);
                        break;
                    case 3:
                        p.getEtat()[posX][posY] = new Fou((ImageIcon) jButton.getIcon(), Plateau.couleur.BLANC, posY, posX, p);
                        break;
                    case 4:
                        p.getEtat()[posX][posY] = new Cavalier((ImageIcon) jButton.getIcon(), Plateau.couleur.BLANC, posY, posX, p);
                        break;
                    case 5:
                        p.getEtat()[posX][posY] = new Pion((ImageIcon) jButton.getIcon(), Plateau.couleur.BLANC, posY, posX, p);
                        break;
                    case 6:
                        p.getEtat()[posX][posY] = new Dame((ImageIcon) jButton.getIcon(), Plateau.couleur.NOIR, posY, posX, p);
                        break;
                    case 7:
                        p.getEtat()[posX][posY] = new Roi((ImageIcon) jButton.getIcon(), Plateau.couleur.NOIR, posY, posX, p);
                        break;
                    case 8:
                        p.getEtat()[posX][posY] = new Tour((ImageIcon) jButton.getIcon(), Plateau.couleur.NOIR, posY, posX, p);
                        break;
                    case 9:
                        p.getEtat()[posX][posY] = new Fou((ImageIcon) jButton.getIcon(), Plateau.couleur.NOIR, posY, posX, p);
                        break;
                    case 10:
                        p.getEtat()[posX][posY] = new Cavalier((ImageIcon) jButton.getIcon(), Plateau.couleur.NOIR, posY, posX, p);
                        break;
                    case 11:
                        p.getEtat()[posX][posY] = new Pion((ImageIcon) jButton.getIcon(), Plateau.couleur.NOIR, posY, posX, p);
                        break;
                }
                p.addPieceActivable(p.getEtat()[posX][posY]);
                f.getJbX(posX, posY).setIcon(p.getEtat()[posX][posY].getIcone());
                f.getJbX(posX, posY).setDisabledIcon(p.getEtat()[posX][posY].getIcone());
            }
            onButtonPressed();
        }
    }

    private void clear() {
        for (int i = 0; i < 8; i++) {
            for (int j = 0; j < 8; j++) {
                if (i % 2 == 0 && j % 2 == 0) {
                    f.getJbX(i, j).setBackground(Color.WHITE);
                } else if (i % 2 != 0 && j % 2 != 0) {
                    f.getJbX(i, j).setBackground(Color.WHITE);
                } else {
                    f.getJbX(i, j).setBackground(Color.BLACK);
                }
            }
        }
    }

    private void onButtonPressed() {
        ControlButtonPartiePerso.updateLimit(p, f);
        ControlButtonPartiePerso.nbrCaseSelected = 0;
        clear();
        p.getPartiePersoPos().clear();
    }

    private void parcoursPlateau() {
        for (int i = 0; i < f.getJb().length; i++) {
            for (int j = 0; j < f.getJb().length; j++) {
                f.getJbX(i, j).removeActionListener(f.getJbX(i, j).getActionListeners()[0]);
                if (p.getEtat()[i][j] == null) f.getJbX(i, j).setEnabled(false);
                if (p.getEtat()[i][j] instanceof Roi && p.getEtat()[i][j].getCouleur() == Plateau.couleur.BLANC && (p.getEtat()[i][j].getPosX() != 4 || p.getEtat()[i][j].getPosY() != 7)) {
                    p.setPetitRoqueBlanc(false);
                    p.setGrandRoqueBlanc(false);
                }
                if (p.getEtat()[i][j] instanceof Roi && p.getEtat()[i][j].getCouleur() == Plateau.couleur.NOIR && (p.getEtat()[i][j].getPosX() != 4 || p.getEtat()[i][j].getPosY() != 0)) {
                    p.setPetitRoqueNoir(false);
                    p.setGrandRoqueNoir(false);
                }
            }
        }
        for (int i = 0; i < f.getjButtonPromotion().length; i++) {
            f.getjButtonPromotion()[i].removeActionListener(f.getjButtonPromotion()[i].getActionListeners()[0]);
        }
    }
}
