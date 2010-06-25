package mecanique;

/**
 * Object physique horloge.
 */

import java.awt.Graphics2D;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.geom.Point2D;

public class Horloge implements PhysicalObject, Drawable {
    private HorlogePanel horloge;
    
    /**
     * Crée un nouvel horloge placé en (x, y)
     * @param x position horizontale de l'horloge
     * @param y position verticale de l'horloge
     */
    public Horloge(double x, double y) {
        horloge = new HorlogePanel(0, 0, 0, (int)x, (int)y);
    }
    
    
    
    /**
     * La hauteur de l'horloge est son diamètre
     */
    @Override
    public double getHeight() {
        return HorlogePanel.DIAMETER;
    }

    
    /**
     * L'horloge a une masse infinie pour n'être affecté par aucune force.
     */
    @Override
    public double getMass() {
        return Double.POSITIVE_INFINITY;
    }

    /**
     * Obtenir la position (coin supérieur gauche) de l'horloge.
     */
    @Override
    public Point2D getPosition() {
        return horloge.getPosition();
    }

    /**
     * L'horloge a toujours une vélocité nulle.
     */
    @Override
    public Point2D getVelocity() {
        return new Point2D.Double();
    }

    
    /**
     * La largeur de l'horloge est son diamètre.
     */
    @Override
    public double getWidth() {
        return HorlogePanel.DIAMETER;
    }

    
    /**
     * Modifier la position de l'horloge
     */
    @Override
    public void setPosition(Point2D P) {
        horloge.setPosition(P);
    }

    
    /**
     * L'horloge n'a jamais de vélocité, donc on ignore les requêtes pour 
     * la modifier.
     */
    @Override
    public void setVelocity(Point2D v) {
        // Ignore
    }

    /**
     * On affiche une horloge en affichant son HorlogePanel.
     */
    @Override
    public void draw(Graphics2D G) {
        horloge.paintComponent(G);
    }
    
    
    
    /**
     * Classe de placement d'une horloge.
     */
    public static class Placement extends MouseAdapter implements ObjectPlacement {
        private SystemDisplay panneau;
        
        protected Placement(SystemDisplay panneau)
        {
            this.panneau = panneau;
        }
        
        public String getName() {
            return "Horloge";
        }
        
        public void clear() {
        }
        
        @Override
        public void mouseClicked(MouseEvent e) {
            int x = e.getX();
            int y = e.getY();
            
            panneau.add(new Horloge(x, y));
        }
    }

}
