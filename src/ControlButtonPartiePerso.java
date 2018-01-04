import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class ControlButtonPartiePerso implements ActionListener {

    private Fenetre fenetre;
    private Plateau plateau;

    public static int[][] limite;
    public static int nbrCaseSelected;

    public ControlButtonPartiePerso(Plateau plateau, Fenetre fenetre) {
        this.fenetre = fenetre;
        this.plateau = plateau;
        //Blanc{Dame - Roi - Cavalier - Fou - Pion - Tour
        limite = new int[][]{{9, 1, 10, 10, 8, 10}, {9, 1, 10, 10, 8, 10}};
        fenetre.setControlButtonPartiePerso(this);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getSource() instanceof JButton) {
            //On recupere la position du bouton
            for (int i = 0; i < 8; i++) {
                for (int j = 0; j < 8; j++) {
                    if (e.getSource() == fenetre.getJb()[i][j]) {
                        if (fenetre.getJbX(i, j).getBackground().equals(Color.GREEN)) {
                            if (i % 2 == 0 && j % 2 == 0) {
                                fenetre.getJbX(i, j).setBackground(Color.WHITE);
                            } else if (i % 2 != 0 && j % 2 != 0) {
                                fenetre.getJbX(i, j).setBackground(Color.WHITE);
                            } else {
                                fenetre.getJbX(i, j).setBackground(Color.BLACK);
                            }
                            Position position = new Position(i, j);
                            if (plateau.getPartiePersoPos().contains(position)) {
                                plateau.removePartiePersoPos(position);
                            }
                            nbrCaseSelected--;
                        } else {
                            fenetre.getJbX(i, j).setBackground(Color.GREEN);
                            plateau.addPartiePersoPos(new Position(i, j));
                            nbrCaseSelected++;
                        }
                        updateLimit(plateau, fenetre);
                        disabledJButton();
                        return;
                    }
                }
            }
        }
    }

    //Désactiver les pièces qui ne peuvent pas être placé à certain endroit
    private void disabledJButton(){
        for (int i = 0; i < plateau.getPartiePersoPos().size(); i++) {
            if (plateau.getPartiePersoPos().get(i).getX() == 0) {
                fenetre.getjButtonPartiePerso().get(4).setEnabled(false);
            }
            if (plateau.getPartiePersoPos().get(i).getX() == 7) {
                fenetre.getjButtonPartiePerso().get(10).setEnabled(false);
            }

            for (int j = -1; j < 2; j = j+2) {
                try{
                    if(plateau.getEtat()[plateau.getPartiePersoPos().get(i).getX()+j][plateau.getPartiePersoPos().get(i).getY()] instanceof Roi){
                        fenetre.getjButtonPartiePerso().get(1).setEnabled(false);
                        fenetre.getjButtonPartiePerso().get(7).setEnabled(false);
                        return;
                    }
                    if(plateau.getEtat()[plateau.getPartiePersoPos().get(i).getX()][plateau.getPartiePersoPos().get(i).getY()+j] instanceof Roi){
                        fenetre.getjButtonPartiePerso().get(1).setEnabled(false);
                        fenetre.getjButtonPartiePerso().get(7).setEnabled(false);
                        return;
                    }
                    if(plateau.getEtat()[plateau.getPartiePersoPos().get(i).getX()+j][plateau.getPartiePersoPos().get(i).getY()+j] instanceof Roi){
                        fenetre.getjButtonPartiePerso().get(1).setEnabled(false);
                        fenetre.getjButtonPartiePerso().get(7).setEnabled(false);
                        return;
                    }
                    if(plateau.getEtat()[plateau.getPartiePersoPos().get(i).getX()+j][plateau.getPartiePersoPos().get(i).getY()-j] instanceof Roi){
                        fenetre.getjButtonPartiePerso().get(1).setEnabled(false);
                        fenetre.getjButtonPartiePerso().get(7).setEnabled(false);
                        return;
                    }
                }catch (ArrayIndexOutOfBoundsException ignored){}
            }
        }
    }

    //Désactiver les pièces qui ne peuvent pas être plaçé à cause de la limite de nombre
    private static void disabledJButton(Fenetre fenetre) {
        for (int j = 0; j < 6; j++) {
            if (nbrCaseSelected - 1 == limite[0][j]) fenetre.getjButtonPartiePerso().get(j).setEnabled(false);
            if (nbrCaseSelected - 1 == limite[1][j]) fenetre.getjButtonPartiePerso().get(j + 6).setEnabled(false);
        }
    }

    private static void activeJButton(Fenetre fenetre) {
        for (int j = 0; j < 6; j++) {
            if (nbrCaseSelected - 1 < limite[0][j]) fenetre.getjButtonPartiePerso().get(j).setEnabled(true);
            if (nbrCaseSelected - 1 < limite[1][j]) fenetre.getjButtonPartiePerso().get(j + 6).setEnabled(true);
        }
    }

    public static void updateLimit(Plateau plateau, Fenetre fenetre) {
        limite = new int[][]{{9, 1, 10, 10, 8, 10}, {9, 1, 10, 10, 8, 10}};
        for (int i = 0; i < 8; i++) {
            for (int j = 0; j < 8; j++) {
                Piece piece = plateau.getEtat()[i][j];
                if (piece != null) {
                    int couleur = piece.getCouleur() == Plateau.couleur.BLANC ? 0 : 1;
                    if (piece instanceof Dame) ControlButtonPartiePerso.limite[couleur][0] -= 1;
                    if (piece instanceof Roi) ControlButtonPartiePerso.limite[couleur][1] -= 1;
                    if (piece instanceof Cavalier) ControlButtonPartiePerso.limite[couleur][2] -= 1;
                    if (piece instanceof Fou) ControlButtonPartiePerso.limite[couleur][3] -= 1;
                    if (piece instanceof Pion) ControlButtonPartiePerso.limite[couleur][4] -= 1;
                    if (piece instanceof Tour) ControlButtonPartiePerso.limite[couleur][5] -= 1;
                }
            }
        }
        if (limite[0][1] == 0 && limite[1][1] == 0) {
            fenetre.getLancerPartieJButton().setEnabled(true);
        } else {
            fenetre.getLancerPartieJButton().setEnabled(false);
        }
        activeJButton(fenetre);
        disabledJButton(fenetre);
    }

}
