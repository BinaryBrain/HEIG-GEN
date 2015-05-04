
/**
 * **************************************************************
 * Auteur:	Eric Lefrançois * Groupe:	HES_SO Informatique & Télécommunications *
 * Fichier: 1er Octobre 2009 - DEPART	* Projet:	Horloges synchronisées *
 * ***************************************************************
 */
public class Amorce {

    public static void main(String argv[]) {

        Emetteur emetteur = new Emetteur(100);
        emetteur.addObserver(new VueEmetteur());        // Emetteur avec une seconde de 100msec

        // Création d'une pendule, avec une seconde valant 120msec (plus lente que l'emetteur
        Pendule pendule = new Pendule(100);
        pendule.addObserver(new VuePendule("H", 100, 0));
        emetteur.addObserver(pendule);

        for (int i = 0; i < 5; i++) {
            Pendule p = new Pendule((int) (Math.random() * 70) + 70);
            VuePendule vp = new VuePendule("H" + i, 100 + 150 * i + 150, 0);
            p.addObserver(vp);
            vp.addObserver(p);
            emetteur.addObserver(p);
        }
    }
}
