
package pkg2048like;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Container;
import java.awt.Font;
import java.awt.GridLayout;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.border.MatteBorder;

public class Main2048  extends JPanel implements KeyListener {

    private static final int SIZE = 100; // grandeur d'une cellule en pixel
    Vecteur vals;    // valeurs a compresser 
    private JLabel points;             // affichage des points
    // couleurs des tuiles et du fond du jeu qui approxime celles du jeu original
    private Color fond = new Color(0xC3, 0xB4, 0xA3);
    private Color bordure = new Color(0xAE, 0x9D, 0x8D);
    private Color tuile = new Color(0x64, 0x5A, 0x51);
    private Color[] couleurs;
    private MatteBorder mBorder = new MatteBorder(5, 5, 5, 5, bordure);
    private static Font fonte = new Font("Arial", Font.BOLD, 40); // police pour les nombres
    // cellules du vecteur
    private JLabel[][] places;
    // afficher le contenu des cellules
    private void repaintPlaces() {
        for (int i = 0; i < 4; i++) {
            for (int j = 0; j < 4; j++) {
                //on place les objets 4*4
                int tij = vals.getIJ(i, j);
                JLabel lij = places[i][j];
                lij.setBackground(tij == 0 ? fond : couleurs[tij]);
                lij.setText(tij == 0 ? "" : ("" + tij));
            }
        }
    }

    private void creerObjets() {
        // initialisation des couleurs des fonds des tuiles
        couleurs = new Color[2049];
        couleurs[0] = Color.BLACK;
        couleurs[2] = new Color(0xEB, 0xDD, 0xD0);
        couleurs[4] = new Color(0xEA, 0xDA, 0xB9);
        couleurs[8] = new Color(0xFA, 0x9E, 0x5E);
        couleurs[16] = new Color(0xFC, 0x7B, 0x44);
        couleurs[32] = new Color(0xFC, 0x58, 0x40);
        couleurs[64] = new Color(0xFB, 0x2D, 0x11);
        couleurs[128] = new Color(0xEB, 0xC9, 0x57);
        couleurs[256] = new Color(0xEB, 0xC6, 0x45);
        couleurs[512] = new Color(0xEC, 0xC1, 0x32);
        couleurs[1024] = new Color(0xED, 0xBE, 0x1F);
        couleurs[2048] = Color.MAGENTA; 

        vals = new Vecteur();
        //la grille est 4*4
        places = new JLabel[4][4];
        

        points = new JLabel();
        points.setFont(fonte);
        points.setHorizontalAlignment(JLabel.CENTER);
        points.setText("0");
    }

    private void disposerObjets(Container c) {
        c.add(BorderLayout.NORTH, points);
        c.add(BorderLayout.CENTER, this);
        setLayout(new GridLayout(4, 4));
        for (int i = 0; i < 4; i++) {
            for (int j = 0; j < 4; j++) {
                JLabel l = new JLabel();
                l.setFont(fonte);
                l.setForeground(tuile);
                l.setHorizontalAlignment(JLabel.CENTER);
                l.setBorder(mBorder);
                l.setOpaque(true);
                
                places[i][j] = l;
                this.add(l);
            }
        }
        repaintPlaces();
    }

    public void keyPressed(KeyEvent key) {
        //on effectue une copie de la grille dans le but de la comparer ensuite
        //afin de déterminer si l'on doit ou pas rajouter un chiffre
        vals.copie();
        
        //condition pour jouer, réaliser un coup inférieur à 2048
        if (vals.getSomme() < 2048) {

            switch (key.getKeyCode()) {
                /*PRINCIPE :
                Le joueur presse une touche et fait déplacer tous les chiffres
                et nombres dans la même direction, le total des points est ensuite
                récupéré puis préparer pour l'affiche.
                Comme expliqué tout en bas, pour rajouter un chiffre il faut 
                que le joueur réussisse un shift, si il est bloqué dans une direction
                il ne se passe rien               
                */
               
                case KeyEvent.VK_LEFT:
                    vals.shiftLeft();
                    points.setText("" + vals.getPts());
                    //s'il reste un emplacement libre ET
                    //si la copie des emplacements réalisés avant
                    //l'interaction du joueur est DIFFERENTE à la grille après
                    //le nouveau mouvement (vals.shiftLeft())
                    //alors on en déduit que l'on peut rajouter un chiffre dans la grille
                    if (vals.espaceLibre() && vals.compareCopie()) {
                        vals.addCarreau();
                    }
                    repaintPlaces();
                    break;
                case KeyEvent.VK_RIGHT:
                    vals.shiftRight();
                    points.setText("" + vals.getPts());
                    if (vals.espaceLibre() && vals.compareCopie()) {
                        vals.addCarreau();
                    }
                    repaintPlaces();
                    break;
                case KeyEvent.VK_UP:
                    vals.shiftUp();
                    points.setText("" + vals.getPts());
                    if (vals.espaceLibre() && vals.compareCopie()) {
                        vals.addCarreau();
                    }
                    repaintPlaces();
                    break;
                case KeyEvent.VK_DOWN:
                    vals.shiftDown();
                    points.setText("" + vals.getPts());
                    if (vals.espaceLibre() && vals.compareCopie()) {
                        vals.addCarreau();
                    }
                    repaintPlaces();
                    break;
                
                case KeyEvent.VK_R:
                   vals.reset();
                   points.setText("" + vals.getPts());
                   repaintPlaces();
                   break;
            }

        //donc si on fait un score de série supérieur ou = à 2048 on gagne
        } else {
            System.out.println("gagné !!  R pour recommencer");
        }

        //condition d'arrêt du jeu
        /*
        si aucun mouvement n'est possible (pas de chiffre identique cote à cote
        si la copie est identique (le joueur a joué mais cela à rien produit)
        et si aucun espace libre est disponible
        alors le joueur est bloqué et il a perdu
        */
        if (!vals.canMove() && !vals.compareCopie() && !vals.espaceLibre()) {
            System.out.println("perdu !!  R pour recommencer");
        }
        
    }

    @Override
    public void keyReleased(KeyEvent arg0) {
    }

    @Override
    public void keyTyped(KeyEvent arg0) {
    }

    private void ecouterObjets(JFrame frame) {
        frame.addKeyListener(this);
    }

    public static void main(String[] args) {
        JFrame f = new JFrame();
        f.setTitle("Serie 2048 en Java");
        Container c = f.getContentPane();
        c.setLayout(new BorderLayout());
        Main2048 jeu = new Main2048();
        jeu.creerObjets();
        jeu.disposerObjets(c);
        jeu.ecouterObjets(f);
        // on multiplie la hauteur par 4 pour avoir suffisamment d'espace
        f.setSize(4 * SIZE + 10, 4 * SIZE + 10 + 50);
        f.setLocation(100, 100);
        f.setVisible(true);
        f.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    }
}

/*
dans cet exemple le joueur n'a pas d'autre solutions que de presser la touche droite
sans la comparaison avec la copie de la grille je n'aurai pas pu déceler qu'aucun coup
n'était jouable par la gauche, et par conséquent, un nouveau chiffre serai apparu 
à la place des 0, ce qui est contraire à la règle du jeu
{2, 0, 0, 0},
{4, 0, 0, 0},
{8, 0, 0, 0},
{16, 0, 0, 0}

*/