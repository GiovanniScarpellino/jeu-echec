import java.io.IOException;
import java.net.Socket;

public class Controleur {

    private ControlMenu controlMenu;

    public Controleur(Plateau p) {
        Fenetre f = new Fenetre(p);
        new ControlButton(p, f);
        controlMenu = new ControlMenu(p, f, this);
        new ControlPromotion(p, f);
        f.setVisible(true);
    }

    public void newGame(Plateau p, Fenetre f) {
        if (p.isPartiePerso()) {
            new ControlButtonPartiePerso(p, f);
            new ControlPartiePerso(p, f);
        }else if(p.getPartieChrono()){
            new ControlPartieChrono(f,p);
            new ControlButton(p, f);
            new ControlPromotion(p, f);
        }else{
            new ControlButton(p, f);
            new ControlPromotion(p, f);
        }
        controlMenu.setFenetre(f);
        controlMenu.setPlateau(p);
    }
}
