
/**
 * ******************************************************************
 * Auteur:	Eric Lefrançois * Groupe:	HES_SO/EIG Informatique & Télécommunication
 * * Fichier: Pendule.java * Date :	1er Octobre 2009 - DEPART * Projet:	Horloges
 * synchronisées *
 * *******************************************************************
 */
import java.util.Observable;
import java.util.Observer;
import java.util.Timer;
import java.util.TimerTask;

public class Pendule extends Observable implements Observer {
//Classe qui décrit une montre avec un affichage des aiguilles

    private int dureeSeconde;       // Durée de la seconde en msec.
    private int minutes = 0;       	// Compteurs de la pendule
    private int secondes = 0;
    private int heures = 0;
    private Timer timer;
    private boolean cancelled = false;

    //------------------------------------------------------------------------
    //------------------------------------------------------------------------
    public Pendule(int valSeconde) {
        dureeSeconde = valSeconde;

        timer = new Timer();
        timer.schedule(new TimerTask() {

            @Override
            public void run() {
                incrementerSecondes();
                setChanged();
                notifyObservers(new int[]{secondes, minutes, heures});
            }
        }, 0, dureeSeconde);

    }

    public synchronized void incrementerSecondes() {
        secondes++;
        if (secondes == 60) {
            secondes = 0;
            timer.cancel();
            cancelled = true;
            //incrementerMinutes();
        }
    }

    public void incrementerMinutes() {
        minutes = ++minutes % 60;
        if (minutes == 0) {
            heures = ++heures % 24;
        }
    }

    @Override
    public void update(Observable o, Object arg) {
        if (arg instanceof String) {
            if (!cancelled) {
                secondes = 0;
                timer.cancel();
            }

            incrementerMinutes();
            timer = new Timer();
            timer.schedule(new TimerTask() {

                @Override
                public void run() {
                    incrementerSecondes();
                    setChanged();
                    notifyObservers(new int[]{secondes, minutes, heures});
                }
            }, 0, dureeSeconde);
            cancelled = false;
        }
        if (o instanceof VuePendule) {
            minutes++;
        }
    }
}
