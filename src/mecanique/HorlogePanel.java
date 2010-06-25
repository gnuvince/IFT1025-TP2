package mecanique;

import java.awt.Graphics;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.geom.Point2D;

import javax.swing.JPanel;
import javax.swing.Timer;


/**
 * Panneau représentant une horloge analogique.
 * @author vince
 *
 */
public class HorlogePanel extends JPanel {
    private int heure;
    private int minute;
    private int seconde;
    
    private Timer timerSeconde;
    
    /*
     * Le diamètre de l'horloge, la longueur des aiguilles par rapport
     * au diamètre et la durée d'une seconde dans l'horloge.
     */
    public static final int DIAMETER = 40;
    private static final int HEURE_LONGUEUR = (int)(DIAMETER * 0.3);
    private static final int MINUTE_LONGUEUR = (int)(DIAMETER * 0.375);
    private static final int SECONDE_LONGUEUR = (int)(DIAMETER * 0.425);
    private static final int SECOND_PERIOD = 15;
    
    /*
     * x: position horizontale du coin supérieur gauche
     * y: position verticale du coin supérieur gauche
     * centerX: position horizontale du centre de l'horloge
     * centerY: position verticale du centre de l'horloge
     */
    private int x;
    private int y;
    private int centerX;
    private int centerY;
    
    
    /**
     * Créer un nouvel horloge
     * @param h heure
     * @param m minute
     * @param s seconde
     * @param x position horizontale
     * @param y position verticale
     */
    public HorlogePanel(int h, int m, int s, int x, int y) {
        setHeure(h);
        setMinute(m);
        setSeconde(s);
        this.x = x;
        this.y = y;
        centerX = x + DIAMETER/2;
        centerY = y + DIAMETER/2;
        
        /*
         * Mettre les secondes à jour, ainsi que les minutes et les heures
         * si nécessaire.  Ré-afficher l'horloge.
         */
        timerSeconde = new Timer(SECOND_PERIOD, new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                setSeconde((getSeconde()+1) % 60);
                
                if (getSeconde() == 0) {
                    setMinute((getMinute()+1) % 60);
                    
                    if (getMinute() == 0) {
                        setHeure((getHeure()+1) % 24);
                    }
                }                    
                
                HorlogePanel.this.repaint();
            }
        });
        
        timerSeconde.start();
    }
    
    /**
     * Obtenir la position de l'horlige
     * @return Point2D de la position du coin supérieur gauche
     */
    public Point2D getPosition() {
        return new Point2D.Double(x, y);
    }
    
    
    /**
     * Modifier la position de l'horloge
     * @param p Point2D où se trouvera le coin supérieur gauche de l'horloge
     */
    public void setPosition(Point2D p) {
        x = (int)p.getX();
        y = (int)p.getY();
    }


    /**
     * Modifier l'heure
     * @param heure nouvelle heure
     */
    public void setHeure(int heure) {
        this.heure = heure;
    }

    /**
     * Obtenir l'heure
     * @return l'heure actuelle
     */
    public int getHeure() {
        return heure;
    }

    
    /**
     * Modifier la minute
     * @param minute nouvelle minute
     */
    public void setMinute(int minute) {
        this.minute = minute;
    }

    
    /**
     * Obtenir la minute
     * @return la minute actuelle
     */
    public int getMinute() {
        return minute;
    }


    /**
     * Modifier la seconde
     * @param seconde nouvelle seconde
     */
    public void setSeconde(int seconde) {
        this.seconde = seconde;
    }

    

    /**
     * Obtenir la seconde
     * @return la seconde actuelle
     */
    public int getSeconde() {
        return seconde;
    }

    /**
     * Afficher l'horloge
     */
    public void paintComponent(Graphics g) {
        super.paintComponent(g);
        g.drawOval(x, y, DIAMETER, DIAMETER);
        g.drawLine(centerX, centerY, heureXPos(), heureYPos());
        g.drawLine(centerX, centerY, minuteXPos(), minuteYPos());
        g.drawLine(centerX, centerY, secondeXPos(), secondeYPos());
    }
    
    
    /**
     * Calcule la position horizontale d'une aiguille
     * @param current la seconde/minute/heure courante
     * @param max la seconde/minute/heure maximimale (60, 60, 24)
     * @param length la longueur de l'aguille
     * @return la position x de l'aguille
     */
    private int xpos(int current, int max, int length) {
        return (int)(Math.cos(current / (double)max * 2 * Math.PI - Math.PI/2.0) 
                * length) + centerX;
    }
    
    /**
     * Calcule la position verticale d'une aiguille
     * @param current la seconde/minute/heure courante
     * @param max la seconde/minute/heure maximimale (60, 60, 24)
     * @param length la longueur de l'aguille
     * @return la position y de l'aguille
     */
    private int ypos(int current, int max, int length) {
        return (int)(Math.sin(current / (double)max * 2 * Math.PI - Math.PI/2.0) 
                * length) + centerY;
    }

    
    /*
     * Méthodes poure obtenir les positions x et y des aiguilles des heures,
     * minutes et secondes.
     */
    private int heureXPos()   { return xpos(getHeure(),   24, HEURE_LONGUEUR); }
    private int heureYPos()   { return ypos(getHeure(),   24, HEURE_LONGUEUR); }
    private int minuteXPos()  { return xpos(getMinute(),  60, MINUTE_LONGUEUR); }
    private int minuteYPos()  { return ypos(getMinute(),  60, MINUTE_LONGUEUR); }
    private int secondeXPos() { return xpos(getSeconde(), 60, SECONDE_LONGUEUR); }
    private int secondeYPos() { return ypos(getSeconde(), 60, SECONDE_LONGUEUR); }
}
