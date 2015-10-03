package pkg2048like;


import java.util.Random;

public class Vecteur {

    private int[][] vs = new int[4][4]; //tableau à 2 dimensions de la grille
    private int[][] laCopie = new int[4][4];
    private int pts;
    private int sommeCoups = 0;
    //initialisation à 0 de chaque élément de la grille
    private static int[][] vecteurInit = new int[][]{
        {0, 0, 0, 0},
        {0, 0, 0, 0},
        {0, 0, 0, 0},
        {0, 0, 0, 0}
    };

    //constructeurs
    Vecteur(int[][] vs) {
        this.vs = vs;
    }

    Vecteur() {
        this(vecteurInit);
        addCarreau();
        addCarreau();
        //comme l'initialisation de la grille met des 0 partout
        //on exécute 2 fois la même fonction pour rajouter 2 chiffres
    }

    public int chiffre() {//produit aléatoirement un chiffre en 0 et 3 inclu
        Random rand = new Random();
        return rand.nextInt((3 - 0) + 1) + 0;
        //int randomNum = rand.nextInt((max - min) + 1) + min;
    }

    //realise une copie du vecteur VS, explication en bas du fichier TestSerie
    public void copie() {
        for (int i = 0; i < 4; i++) {
            for (int j = 0; j < 4; j++) {
                laCopie[i][j] = vs[i][j];
            }
        }
    }

    //retourne vrai si la copie est differente
    public boolean compareCopie() {
        for (int i = 0; i < 4; i++) {
            for (int j = 0; j < 4; j++) {
                if (laCopie[i][j] != vs[i][j]) {
                    return true;
                }
            }
        }
        return false;
    }

    //parametres pour recommencer une partie
    public void reset(){
        vs = new int[4][4];  
        pts=0;
        laCopie = new int[4][4];
        sommeCoups = 0;
        addCarreau();
        addCarreau();
    }
    
    //rajoute aléatoirement sur la grille un chiffre (2 ou 4)
    public void addCarreau() {
        //la grille a des coordonnées X et Y
        int abs = 0;
        int ord = 0;
        
        //on cherche un 0 dans la grille au hasard
        /* approche à améliorer */
        do {
            abs = chiffre();
            ord = chiffre();
        } while (vs[abs][ord] != 0); 

        //on place 2 ou 4 sur l'emplacement trouvé
        vs[abs][ord] = Math.random() < 0.9 ? 2 : 4;
    }

    //retourne vrai d'un qu'un espace libre est trouvé dans la grille
    public boolean espaceLibre() {
        for (int i = 0; i < 4; i++) {
            for (int j = 0; j < 4; j++) {
                if (vs[i][j] == 0) {
                    return true;
                }
            }
        }
        return false;
    }

    //déplacement à gauche
    public int shiftLeft() {
        Serie s = new Serie();
        //on initialise la somme des points à 0
        sommeCoups = 0;
        for (int i = 0; i < 4; i++) {
            for (int j = 0; j < 4; j++) {
                s.ajouter(vs[i][j]);
            }
            //à chaque shift on récupère les points obtenus
            sommeCoups += s.shift();
            for (int j = 0; j < 4; j++) {
                vs[i][j] = s.enlever();
            }
            //on repart sur une nouvelle série vierge pour recommencer sur la ligne suivante
            s = new Serie();
        }
        //le compteur de point correspond aux anciens points + les nouveaux
        pts += sommeCoups;
        return pts;
    }

    public int shiftRight() {
        Serie s = new Serie();
        sommeCoups = 0;
        for (int i = 3; i >= 0; i--) {
            for (int j = 3; j >= 0; j--) {
                s.ajouter(vs[i][j]);
            }
            sommeCoups += s.shift();
            for (int j = 3; j >= 0; j--) {
                vs[i][j] = s.enlever();
            }
            s = new Serie();
        }
        pts += sommeCoups;
        return pts;
    }

    public int shiftUp() {
        Serie s = new Serie();
        sommeCoups = 0;
        for (int j = 0; j < 4; j++) {
            for (int i = 0; i < 4; i++) {
                s.ajouter(vs[i][j]);
            }
            sommeCoups += s.shift();
            for (int i = 0; i < 4; i++) {
                vs[i][j] = s.enlever();
            }
            s = new Serie();
        }
        pts += sommeCoups;
        return pts;
    }

    public int shiftDown() {
        Serie s = new Serie();
        sommeCoups = 0;
        for (int j = 3; j >= 0; j--) {
            for (int i = 3; i >= 0; i--) {
                s.ajouter(vs[i][j]);
            }
            sommeCoups += s.shift();
            for (int i = 3; i >= 0; i--) {
                vs[i][j] = s.enlever();
            }
            s = new Serie();
        }
        pts += sommeCoups;
        return pts;
    }

    //retourne la valeur du vecteur avec des coordonnées précises
    public int getIJ(int i, int j) {
        return vs[i][j];
    }
    //retourne le nombre de points
    public int getPts() {
        return pts;
    }
    //retourne la somme des points par série
    public int getSomme() {
        return sommeCoups;
    }

    //retourne vrai s'il existe encore au moins un coup à jouer 
    public boolean canMove() {
        for (int x = 0; x < 4; x++) {
            for (int y = 0; y < 4; y++) {
                if ((x < 3 && vs[x][y] == vs[x + 1][y])
                        || ((y < 3) && vs[x][y] == vs[x][y + 1])) {
                    return true;
                }
            }
        }
        return false;
    }

}
