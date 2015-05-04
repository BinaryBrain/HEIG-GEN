
/**
 * **************************************************************
 * Auteur:	Eric Lefrançois * Groupe:	HES_SO Informatique & Télécommunications *
 * Fichier: Emetteur.java * Date :	1er Octobre 2009 - Départ * Projet:	Horloges
 * synchronisées *
 * ***************************************************************
 */
import java.util.Observable;
import java.util.Timer;
import java.util.TimerTask;

class Emetteur extends Observable {

    private int secondes = 0;	// Compteur de secondes
    private Timer timer;

// Constructeur
    public Emetteur(int dureeSeconde) {
        timer = new Timer();
        timer.schedule(new TimerTask() {

            @Override
            public void run() {
                heureMettreAJour();
                setChanged();
                notifyObservers(secondes);
            }
        }, 0, dureeSeconde);
    }

    private void heureMettreAJour() {
        secondes = ++secondes % 60;
        if (secondes == 0) {
            setChanged();
            notifyObservers("sync");
        }
    }
}
