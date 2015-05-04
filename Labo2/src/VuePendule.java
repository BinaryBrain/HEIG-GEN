
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.event.ActionEvent;
import java.util.Observable;
import java.util.Observer;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;

public class VuePendule extends Observable implements Observer {

    private static int TAILLE = 50; // Taille de la demi-fenétre
    private ToileGraphique toile;
    private int minutes = 0;       	// Compteurs de la pendule
    private int secondes = 0;
    private int heures = 0;
    
    JFrame frame;

    public VuePendule(String nom, int posX, int posY) {
       frame =  new JFrame();
        toile = new ToileGraphique();
        frame.setTitle(nom);
        frame.add(toile, BorderLayout.CENTER);

        frame.pack();
        frame.setResizable(false);
        frame.setLocation(posX, posY);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setVisible(true);
    }

    @Override
    public void update(Observable o, Object arg) {
        int[] a = (int[]) arg;
        secondes = a[0];
        minutes = a[1];
        heures = a[2];
        toile.repaint();
    }

    class ToileGraphique extends JPanel {

        private JLabel label;
        private JButton button;

        public ToileGraphique() {
            setBackground(Color.white);
            label = new JLabel();
            button = new JButton("+");
            button.addActionListener((ActionEvent e) -> {
                setChanged();
                notifyObservers();
                //minutes++; //TODO
            });

            frame.getContentPane().add(label, BorderLayout.SOUTH);
            frame.getContentPane().add(button, BorderLayout.NORTH);
        }

        @Override
        public void paintComponent(Graphics g) {
            super.paintComponent(g);
            dessinerAiguilles(g);
            label.setText(heures + ":" + minutes + ":" + secondes);
        }

        @Override
        public Dimension getPreferredSize() {
            return new Dimension(2 * TAILLE, 2 * TAILLE);
        }

        public void dessinerAiguilles(Graphics g) {
            // calculer les coordonnées des aiguilles
            int cosxm = (int) (TAILLE + (TAILLE / 2)
                    * Math.cos(2 * ((double) minutes / 60 * Math.PI - Math.PI / 4)));
            int sinym = (int) (TAILLE + (TAILLE / 2)
                    * Math.sin(2 * ((double) minutes / 60 * Math.PI - Math.PI / 4)));
            int cosxh = (int) (TAILLE + (TAILLE / 4)
                    * Math.cos(2 * ((double) heures / 12 * Math.PI - Math.PI / 4)));
            int sinyh = (int) (TAILLE + (TAILLE / 4)
                    * Math.sin(2 * ((double) heures / 12 * Math.PI - Math.PI / 4)));

            g.setColor(Color.red);
            g.drawLine(TAILLE, TAILLE,
                    (int) (TAILLE + (TAILLE - 20.0)
                    * Math.cos(2 * ((double) secondes / 60 * Math.PI - Math.PI / 4))),
                    (int) (TAILLE + (TAILLE - 20)
                    * Math.sin(2 * ((double) secondes / 60 * Math.PI - Math.PI / 4))));

            g.setColor(Color.blue);
            g.drawLine(TAILLE, TAILLE, cosxm, sinym);
            g.drawLine(TAILLE, TAILLE, cosxh, sinyh);
        }
    }
}
