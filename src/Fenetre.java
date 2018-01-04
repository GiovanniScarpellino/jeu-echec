import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.util.ArrayList;


public class Fenetre extends JFrame{

    private JButton[][] jb;
    private Plateau p;
    private JMenuItem newGameItem, reseauItem,chronoGameItem, trainingGameItem, versionPetit, versionGrand, quitItem, aideItem;

    private JPanel fenetre, plateau;

    //Promotion
    private JButton[] jButtonPromotion;
    private String[] piecesPromotion;
    private JFrame promotionFrame;

    //Partie perso.
    private JFrame partiePersoFrame;
    private ArrayList<JButton> jButtonPartiePerso;
    private ArrayList<String> piecesPartiePerso;
    private JButton lancerPartieJButton, effacerPieceJButton;

    // Partie chrono
    private JLabel timer;
    private JPanel partieChronoPanel;
    private JFrame partieChronoFrame;

    public Fenetre(Plateau p) throws HeadlessException {
        this.p = p;
        this.jb = new JButton[8][8];
        creerFenetre();
        initMenu();
        initPromotion();
        initPartiePerso();
        addToWindow();
        pack();
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    }

    public void creerFenetre(){
        //Création du tableau de JButton
        for (int i = 0; i < 8; i++) {
            for (int j = 0; j < 8; j++) {
                this.jb[i][j] = new JButton();
                this.jb[i][j].setPreferredSize(new Dimension(100,100));
                if (i%2 == 0 && j%2 == 0){
                    this.jb[i][j].setBackground(Color.WHITE);
                } else if (i%2 != 0 && j%2 != 0){
                    this.jb[i][j].setBackground(Color.WHITE);
                } else {
                    this.jb[i][j].setBackground(Color.BLACK);
                }
                this.jb[i][j].setEnabled(false);

                //Placement des pièces
                if (this.p.getEtat()[i][j] != null){
                    this.jb[i][j].setIcon(this.p.getEtat()[i][j].getIcone());
                    this.jb[i][j].setDisabledIcon(this.p.getEtat()[i][j].getIcone());
                    if((i == 7 || i == 6) && p.getEtat()[i][j].estActivable())jb[i][j].setEnabled(true);
                }

                if(p.isPartiePerso()){
                    jb[i][j].setEnabled(true);
                }
            }
        }

        //Création du plateau de jeu
        plateau = new JPanel(new GridLayout(8,8));
        for (int i = 0; i < 8; i++) {
            for (int j = 0; j < 8; j++) {
                plateau.add(this.jb[i][j]);
            }
        }
    }

    public void addToWindow(){
        fenetre = new JPanel();
        fenetre.add(plateau);
        setContentPane(fenetre);
    }

    private void initMenu() {
        JMenuBar barMenu = new JMenuBar();

        JMenu menuOption = new JMenu("Option");

        newGameItem = new JMenuItem("Nouvelle partie");
        reseauItem = new JMenuItem("Réseau");
        menuOption.add(newGameItem);
        menuOption.add(reseauItem);

        JMenu partiePersoSubMenu = new JMenu("Partie personnalisée");

        chronoGameItem = new JMenuItem("Partie chrono");
        trainingGameItem = new JMenuItem("Entrainement");
        partiePersoSubMenu.add(chronoGameItem);
        partiePersoSubMenu.add(trainingGameItem);

        menuOption.add(partiePersoSubMenu);

        JMenu versionSubMenu = new JMenu("Version");
        versionGrand = new JMenuItem("Version grande");
        versionPetit = new JMenuItem("Version petite");

        versionSubMenu.add(versionGrand);
        versionSubMenu.add(versionPetit);
        menuOption.add(versionSubMenu);

        menuOption.addSeparator();

        quitItem = new JMenuItem("Quitter");
        menuOption.add(quitItem);

        aideItem = new JMenuItem("Aide");

        barMenu.add(menuOption);
        barMenu.add(aideItem);
        setJMenuBar(barMenu);
    }

    public void deplacerIcone(Position origine, Position destination){
        jb[destination.getY()][destination.getX()].setIcon(jb[origine.getY()][origine.getX()].getIcon());
        jb[destination.getY()][destination.getX()].setDisabledIcon(jb[origine.getY()][origine.getX()].getIcon());
        jb[origine.getY()][origine.getX()].setIcon(null);
    }

    //Getters menu
    public JMenuItem getNewGameItem() {
        return newGameItem;
    }

    public JMenuItem getChronoGameItem() {
        return chronoGameItem;
    }

    public JMenuItem getTrainingGameItem() {
        return trainingGameItem;
    }

    public JMenuItem getQuitItem() {
        return quitItem;
    }

    public JMenuItem getReseauItem() {
        return reseauItem;
    }

    public JMenuItem getAideItem() {
        return aideItem;
    }

    public JMenuItem getVersionPetit() {
        return versionPetit;
    }

    public JMenuItem getVersionGrand() {
        return versionGrand;
    }

    public void setControlMenu(ControlMenu controlMenu){
        newGameItem.addActionListener(controlMenu);
        chronoGameItem.addActionListener(controlMenu);
        trainingGameItem.addActionListener(controlMenu);
        versionPetit.addActionListener(controlMenu);
        versionGrand.addActionListener(controlMenu);
        reseauItem.addActionListener(controlMenu);
        quitItem.addActionListener(controlMenu);
    }

    public void setControlButton(ActionListener cb){
        for (int i=0; i<8;i++){
            for (int j=0; j<8; j++) {
                jb[i][j].addActionListener(cb);
            }
        }
    }

    public JButton[][] getJb() {
        return jb;
    }

    public void setJb(JButton[][] jb) {
        this.jb = jb;
    }

    public Plateau getP() {
        return p;
    }

    public JPanel getFenetre() {
        return fenetre;
    }

    public void setP(Plateau p) {
        this.p = p;
    }

    public JButton getJbX(int x, int y){return this.jb[x][y];} // Getter qui permet de retourner  JButton du tableau

    public void setControlPromotion(ControlPromotion controlPromotion){
        for (JButton aJButtonPromotion : jButtonPromotion) {
            aJButtonPromotion.addActionListener(controlPromotion);
        }
    }

    public void messageToUser(String message){
        JOptionPane jop1 = new JOptionPane();
        jop1.showMessageDialog(null, message, "Fin de Partie", JOptionPane.INFORMATION_MESSAGE);
    }

    //Création de la fenêtre pour la promotion
    public void choixPieceBox(String color){
        promotionFrame = new JFrame();
        JPanel jPanel = new JPanel();
        for (int i = 0; i < jButtonPromotion.length; i++) {
            jButtonPromotion[i].setIcon(new ImageIcon("res/100/"+p.getCurrentSkin() + "/"+piecesPromotion[i]+color+".png"));
            jPanel.add(jButtonPromotion[i]);
        }
        Dimension dim = Toolkit.getDefaultToolkit().getScreenSize();
        promotionFrame.setLocation(dim.width/2-promotionFrame.getSize().width/2, dim.height/2-promotionFrame.getSize().height/2);
        promotionFrame.setResizable(false);
        promotionFrame.setContentPane(jPanel);
        promotionFrame.pack();
        promotionFrame.setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
        promotionFrame.setAlwaysOnTop(true);
        promotionFrame.setVisible(true);
    }

    //Création de la fenêtre pour la partie perso
    public void partiePersoBox(){
        p.setPartiePersoIsOpen(true);
        partiePersoFrame = new JFrame();
        JPanel jPanel = new JPanel(new GridLayout(3, 6));
        JPanel jPanelBlanc = new JPanel();
        JPanel jPanelNoir = new JPanel();
        JPanel jPanelButton = new JPanel();
        for (int i = 0; i < jButtonPartiePerso.size(); i++) {
            if(i < 6){
                jButtonPartiePerso.get(i).setIcon(new ImageIcon("res/100/"+p.getCurrentSkin()+ "/" +piecesPartiePerso.get(i)+"1"+".png"));
                jPanelBlanc.add(jButtonPartiePerso.get(i));
            }else{
                jButtonPartiePerso.get(i).setIcon(new ImageIcon("res/100/"+p.getCurrentSkin()+ "/"+piecesPartiePerso.get(i-6)+"2"+".png"));
                jPanelNoir.add(jButtonPartiePerso.get(i));
            }
        }
        lancerPartieJButton.setEnabled(false);
        jPanelButton.add(effacerPieceJButton);
        jPanelButton.add(lancerPartieJButton);
        Dimension dim = Toolkit.getDefaultToolkit().getScreenSize();
        partiePersoFrame.setLocation(dim.width/2-partiePersoFrame.getSize().width/2, dim.height/2-partiePersoFrame.getSize().height/2);
        partiePersoFrame.setResizable(false);
        jPanel.add(jPanelBlanc);
        jPanel.add(jPanelNoir);
        jPanel.add(jPanelButton);
        partiePersoFrame.setContentPane(jPanel);
        partiePersoFrame.pack();
        partiePersoFrame.setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
        partiePersoFrame.setAlwaysOnTop(true);
        partiePersoFrame.setVisible(true);
    }

    public void initPromotion(){
        piecesPromotion = new String[]{
                "D", "T", "F", "C"
        };
        jButtonPromotion = new JButton[piecesPromotion.length];
        for (int i = 0; i < jButtonPromotion.length; i++) {
            jButtonPromotion[i] = new JButton();
        }
    }

    public void initPartiePerso(){
        jButtonPartiePerso = new ArrayList<>();
        piecesPartiePerso = new ArrayList<>();
        piecesPartiePerso.add("D");
        piecesPartiePerso.add("R");
        piecesPartiePerso.add("C");
        piecesPartiePerso.add("F");
        piecesPartiePerso.add("P");
        piecesPartiePerso.add("T");
        for (int i = 0; i < 12; i++) {
            jButtonPartiePerso.add(new JButton());
        }
        lancerPartieJButton = new JButton("Lancer la partie !");
        effacerPieceJButton = new JButton("Effacer");
    }

    public JFrame getPromotionFrame() {
        return promotionFrame;
    }

    //Controleur pour la partie perso
    public void setControlButtonPartiePerso(ControlButtonPartiePerso controlButtonPartiePerso) {
        for (int i=0; i<8;i++){
            for (int j=0; j<8; j++) {
                jb[i][j].addActionListener(controlButtonPartiePerso);
            }
        }
    }

    //Controleur pour gérer la fenêtre de la partie perso
    public void setControlPartiePerso(ControlPartiePerso controlPartiePerso) {
        for (JButton aJButtonPartiePerso : jButtonPartiePerso) {
            aJButtonPartiePerso.addActionListener(controlPartiePerso);
        }
        lancerPartieJButton.addActionListener(controlPartiePerso);
        effacerPieceJButton.addActionListener(controlPartiePerso);
    }

    public JFrame getPartiePersoFrame() {
        return partiePersoFrame;
    }

    public JButton getLancerPartieJButton() {
        return lancerPartieJButton;
    }

    public JButton getEffacerPieceJButton() {
        return effacerPieceJButton;
    }

    public ArrayList<JButton> getjButtonPartiePerso() {
        return jButtonPartiePerso;
    }

    public JButton[] getjButtonPromotion() {
        return jButtonPromotion;
    }

    //Partie chrono

    //Création du chrono
    public void chronoBox(){
        this.partieChronoFrame = new JFrame();
        this.partieChronoPanel = new JPanel();
        this.timer = new JLabel("Temps restant: 00:00:00:00");
        this.partieChronoFrame.pack();
        this.partieChronoFrame.setSize(300, 150);
        this.partieChronoFrame.setVisible(true);
        this.partieChronoFrame.setTitle("Chrono");
        this.partieChronoPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        this.partieChronoPanel.setLayout(new GridBagLayout());
        this.partieChronoPanel.add(this.timer);
        this.partieChronoFrame.setContentPane(this.partieChronoPanel);
        this.partieChronoFrame.setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
        this.partieChronoFrame.addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent event) {
                partieChronoFrame.setVisible(false);
                partieChronoFrame.dispose();
                p.setPartieChrono(false);
                fenetre.removeAll();
                fenetre.revalidate();
                JOptionPane.showMessageDialog(null, "Fin de votre partie", "Alert", JOptionPane.INFORMATION_MESSAGE);
            }
        });
    }

    public JLabel getTimer() {
        return timer;
    }
}
