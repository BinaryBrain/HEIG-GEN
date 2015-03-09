/*
Fichier:  Noel.java
     
Exemple d'utilisation du modèle Observer/Observable
Exécuter le programme en application autonome
Ou exécuter le programme en mode Applet en ouvrant une fenêtre de 315*315

Le répertoire contenant les classes à exécuter doit contenir les 3 fichiers image:
"Lune.gif", "Montagne.gif" et "PNoel.gif"


Date :	  Eric Lefrançois, Août 2011
*/

import javax.swing.*;
import java.awt.*;
import java.awt.image.ImageObserver;
import java.util.*;


// -----------------------------------------------------------------------------
class Globals {
    // Constantes globales:  taille des composants

    public static int PG_X = 315;            // Taille en X du panneau graphique
    public static int PG_Y = 315;            // Taille en Y du panneau graphique
    public static int MAX_FLOCONS = 300;        // Nombre max. de flocons

}

// -----------------------------------------------------------------------------
interface SousVue {
    public void dessiner(Graphics g);
}

// -----------------------------------------------------------------------------
class ChuteNeige implements Runnable {
// Controleur et modele rassembles

    private Flocon[] flocons = new Flocon[Globals.MAX_FLOCONS];
    private Thread activite;

    private PanneauGraphique vueParente;

    public ChuteNeige(PanneauGraphique vue) {
        this.vueParente = vue;

        activite = new Thread(this);
        activite.start();
    }

    public void run() {
        // Activite limitee à la creation des flocons
        int nbFlocons = 0;    // Nombre de flocons crees
        while (nbFlocons < Globals.MAX_FLOCONS) {
            try {
                Thread.sleep(100);
            } catch (InterruptedException e) {
            }
            flocons[nbFlocons] = new Flocon(vueParente); // Creer un nouveau flocon
            nbFlocons++;
        }
    }
}

// -----------------------------------------------------------------------------
class Flocon extends Observable implements Runnable {
    // Controleur, modele et vue pour un flocon
    private int x, y;            // Coordonnees courantes du flocon
    private int largeur;        // largeur du flocon (en pixels)
    private int hauteur;        // hauteur du flocon (en pixels)
    private int vitesse;        // DeltaT entre deux deplacements en Y

    private VueFlocon vue;

    private Thread activite;

    // Un generateur de nombres aleatoires
    public static Random rdGen = new Random();

    public Flocon(PanneauGraphique vueParente) {
        vue = new VueFlocon(vueParente, this);

        x = rdGen.nextInt(Globals.PG_X);
        y = 0;
        largeur = 1 + rdGen.nextInt(3);        // Generation nb aleat. entre 1 et 4
        hauteur = largeur + rdGen.nextInt(2);
        vitesse = hauteur;                   // Vitesse directement fonction de la taille

        activite = new Thread(this);
        activite.start();
    }

    public void run() {
        while (true) {
            try {
                Thread.sleep(50 * (7 - vitesse));
            } catch (InterruptedException e) {
            }

            deplacer();
        }
    }

    public void deplacer() {
        boolean aDroite = rdGen.nextInt(2) == 1;
        x = aDroite ? x + 1 : x - 1;
        y += vitesse;
        if (y + hauteur > Globals.PG_Y + 10) {
            x = rdGen.nextInt(Globals.PG_X);
            y = 0;
        }

        notifyObservers();
    }

    public void dessiner(Graphics g) {
        g.setColor(Color.white);
        g.fillRect(x, y, largeur, hauteur);
        g.setColor(Color.lightGray);
        g.drawRect(x, y, largeur, hauteur);
    }
}

class VueFlocon implements SousVue, Observer {
    private Flocon flocon;
    private PanneauGraphique vueParente;

    public VueFlocon(PanneauGraphique vueParente, Flocon flocon) {
        this.vueParente = vueParente;
        this.flocon = flocon;

        vueParente.ajouterSousVue(this);

        flocon.addObserver(this);
    }

    @Override
    public void dessiner(Graphics g) {
        flocon.dessiner(g);
    }

    @Override
    public void update(Observable observable, Object o) {
        dessiner(vueParente.getGraphics());
        vueParente.rafraichir(this);
    }
}

// -----------------------------------------------------------------------------
class CtrPereNoel implements Runnable {
    // Controleur du pere Noel
    private Thread activite;
    private PereNoel modele;
    private VuePereNoel vue;

    public CtrPereNoel(PanneauGraphique parent) {
        modele = new PereNoel();
        vue = new VuePereNoel(parent, modele);

        activite = new Thread(this);
        activite.start();

    }

    public void run() {
        while (true) {
            try {
                Thread.sleep(50);
            } catch (InterruptedException e) {
            }
            modele.deplacer();
        }
    }
}

class PereNoel extends Observable {
    // Modele du pere Noel
    private int x = 0;
    private int y = Globals.PG_Y / 4;
    ;

    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }

    public void deplacer() {
        x += 2;
        if (x > Globals.PG_X + 100) x = -200;
        setChanged();
        notifyObservers();
    }
}

class VuePereNoel implements SousVue, Observer {
    // Vue du pere Noel
    private PanneauGraphique vueParente;
    private PereNoel pereNoel;
    private Image imagePereNoel;

    public VuePereNoel(PanneauGraphique parent, PereNoel modele) {
        vueParente = parent;
        vueParente.ajouterSousVue(this);
        pereNoel = modele;
        imagePereNoel = Toolkit.getDefaultToolkit().getImage("PNoel.gif");
        pereNoel.addObserver(this);
    }

    public void dessiner(Graphics g) {
        g.drawImage(imagePereNoel, pereNoel.getX(), pereNoel.getY(), (ImageObserver) vueParente);
    }

    @Override
    public void update(Observable observable, Object o) {
        dessiner(vueParente.getGraphics());
        vueParente.rafraichir(this);
    }
}

class CoucheNeige extends Observable implements Observer {
    private PanneauGraphique vueParente;
    private int hauteur[] = new int[Globals.PG_X];

    public CoucheNeige(PanneauGraphique parent) {
        this.vueParente = parent;
    }

    public void dessiner(Graphics g) {
        for (int x = 0; x < Globals.PG_X; x++) {
            for (int y = Globals.PG_Y; y < Globals.PG_Y - hauteur[x]; y--){
                g.setColor(Color.white);
                g.fillRect(x, y, 1, 1);
            }
        }
    }

    @Override
    public void update(Observable observable, Object o) {
        // dessiner();
    }
}

//------------------------------------------------------------------------------
class PanneauGraphique extends JPanel {
// Vue passive, pour affichage uniquement

    private Image imageDeFond, imageLune;         // Images de fond

    private ArrayList<SousVue> sousVues = new ArrayList<SousVue>();

    // Constructeur
    public PanneauGraphique() {
        setBackground(new Color(0, 0, 65));
        // Chargement des images Montagne.gif, et Lune.gif, situees dans le repertoire
        // des classes.
        imageDeFond = Toolkit.getDefaultToolkit().getImage("Montagne.gif");
        imageLune = Toolkit.getDefaultToolkit().getImage("Lune.gif");
    }

    public synchronized void ajouterSousVue(SousVue ssVue) {
        // Synchronisation necessaire-La liste des sous-vues est accedee simultanement
        // par paintComponent et au moment de la creation des flocons (ajout des sous-vues)
        sousVues.add(ssVue);
    }

    public void paintComponent(Graphics g) {
        super.paintComponent(g);
        g.drawImage(imageDeFond, 0, Globals.PG_Y - 44, this);
        g.drawImage(imageLune, Globals.PG_X - 100, Globals.PG_Y / 4, this);

		/*Note .......
		Le "this", 4eme parametre de drawImage represente "l'image observer"".
		Cet objet controle le chargement de l'image en memoire (chargee
		habituellement depuis un fichier).  Il est responsable de dessiner
		cette image de maniere asynchrone au reste du programme, au fur et à
		mesure que l'image se charge.
		Ainsi, le programmeur peut donner l'ordre de charger une image ("getImage"),
		puis il peut la dessiner aussitet (drawImage), sans attendre qu'elle
		soit chargee.  La procedure drawImage retourne aussitot.
		L'image observer est implemente par la classe Component (dont herite
		la classe JPanel).
		Le cas echeant, il est possible de redefinir cet objet, ce qui permettrait
		de controler le chargement de l'image, d'attendre qu'elle soit entierement
		chargee avant de l'afficher, etc...
		*/

        // Affichage des sous-vues (Pere Noel et flocons)
        synchronized (this) {
            Iterator<SousVue> i = sousVues.iterator();
            while (i.hasNext()) {
                SousVue sv = (SousVue) i.next();
                sv.dessiner(g);
            }
        }
    }

    public void rafraichir(Object ssvue) {
        // Une sous-vue demande a etre rafraichie
        this.repaint();
    }

    public Dimension getPreferredSize() {
        // Retourne la taille souhaitee pour le composant (remplace le "getSize")
        return new Dimension(Globals.PG_X, Globals.PG_Y);
    }
}

//------------------------------------------------------------------------------
public class Noel extends JApplet {
// Controleur principal
// Creation des "modeles", des "vues"
// Associations diverses


    public Noel() {
    }

    public void init() {
        getContentPane().setLayout(new BorderLayout());

        // Creation de la vue principale
        PanneauGraphique panneauGraphique = new PanneauGraphique();

        CoucheNeige coucheNeige = new CoucheNeige(panneauGraphique);

        // Positionnement de la vue principale au centre de la fenetre
        getContentPane().add(panneauGraphique, BorderLayout.CENTER);


        new CtrPereNoel(panneauGraphique);
        new ChuteNeige(panneauGraphique);
    }

    public void start() {
    }

    public static void main(String[] arg) {
        // Point d'entree du programme
        JFrame f = new JFrame();
        f.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        f.setTitle("Chablon");
        Noel a = new Noel();
        f.getContentPane().add(a, BorderLayout.CENTER);
        a.init();
        f.setResizable(false);
        f.setVisible(true);
        f.pack();
        a.start();
    }
}
//------------------------------------------------------------------------------


