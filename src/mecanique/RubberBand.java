package mecanique;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.geom.Point2D;

import javax.swing.SwingUtilities;

public class RubberBand implements Drawable, Force {
    // Longueur et rigueur de l'élastique telles que spécifiées dans l'énoncé.
    protected static final double LENGTH = 100.0;
    protected static final double RIGOR = 0.1;
    
    // Les deux objets attachés à l'élastique.
    protected PhysicalObject left_object = null;
    protected PhysicalObject right_object = null;
    
    // La position temporaire de la deuxième extrémité de l'élastique
    protected Point2D position = null;
    
    /**
     * Constructeur qui donne un élastique attaché à deux PhysicalObjects.
     * @param l objet de "gauche"
     * @param r objet de "droite"
     */
    public RubberBand(PhysicalObject l, PhysicalObject r) {
        left_object = l;
        right_object = r;
       
        // Donner une valeur initiale à la position du curseur de souris.
        position = left_object.getPosition();
    }

    
    // Accesseurs pour obtenir la position de l'objet de gauche.
    protected double getX1() { return left_object.getPosition().getX(); }
    protected double getY1() { return left_object.getPosition().getY(); }
    
    // Accesseurs pour obtenir la position de l'objet de droite.
    // Si le deuxième objet n'est pas encore sélectionné (null), la position de 
    // X2 et  Y2 est la position du curseur de la souris (qui se trouve 
    // dans position)
    protected double getX2() {
        if (right_object == null)
            return position.getX();
        else 
            return right_object.getPosition().getX();
    }
    protected double getY2() {
        if (right_object == null)
            return position.getY();
        else 
            return right_object.getPosition().getY();
    }
    
    
    /**
     * Couleur de l'élastique lorsqu'il est tendu
     * @return Une couleur
     */
    protected Color getTenseColor() {
        return Color.RED;
    }
    
    
    /**
     * Couleur de l'élastique lorsqu'il n'est pas tendu
     * @return Une couleur
     */
    protected Color getNormalColor() {
        return Color.BLACK;
    }
       
    
    /**
     * Afficher l'élastique avec la bonne couleur
     */
    @Override
    public void draw(Graphics2D G) {
        Graphics2D g2 = (Graphics2D) G.create();
        if (distance() > LENGTH)
            g2.setColor(getTenseColor());
        else
            g2.setColor(getNormalColor());
        g2.drawLine((int) getX1(), (int) getY1(), (int) getX2(), (int) getY2());
    }
    
    /**
     * Calculer la distance entre l'objet de gauche et l'objet de droite.
     */
    protected double distance() {
        double a = getX1() - getX2();
        double b = getY1() - getY2();
        return Math.sqrt(a*a + b*b);
    }

    
    /**
     * Formule pour calculer l'accélération d'un élastique.
     */
    @Override
    public Point2D getAcceleration(PhysicalObject object) {
        double d = distance();
        double f = d < LENGTH ? 0 : Math.abs(RIGOR * (d - LENGTH));
        double theta = Math.atan2(getY2() - getY1(), getX2() - getX1());
        
        int direction;
        // Sens "droit" si objet de gauche
        if (object == left_object)
            direction = 1;
        // Sens inverse si objet de droit
        else if (object == right_object)
            direction = -1;
        // Ignorer les objets qui ne sont pas attachés à l'élastique
        else 
            direction = 0;
        
        return new Point2D.Double(
            direction * f * Math.cos(theta),
            direction * f * Math.sin(theta)
        );
    }



    /**
     * Classe permettant d'effectuer le placement des élastiques. 
     */
    public static class Placement extends MouseAdapter implements ObjectPlacement {
        protected SystemDisplay panneau;
        protected RubberBand rb;

        protected Placement(SystemDisplay panneau) {
            this.panneau = panneau;
            rb = null;
        }

        /**
         * Méthode qui retourne le nom de l'objet (élastique)
         */
        @Override
        public String getName() {
            return "Élastique";
        }
        

        /**
         * Méthode pour effacer l'élastique
         */
        @Override
        public void clear() {
            panneau.remove(rb);
            rb = null;
            panneau.repaint();
        }
        
        
        /**
         * Méthode pour trouver l'objet le plus proche du clic de souris,
         * seulement si le clic de souris a été fait dans l'enceinte d'un
         * objet physique.
         * @param x position horizontale
         * @param y position verticale
         * @return l'objet sélectionné ou null si aucun
         */
        public PhysicalObject getObjectAt(int x, int y) {
            PhysicalObject po = panneau.getSystem().closestObject(x, y);
            if (po == null)
                return null;
            int poX = (int) po.getPosition().getX();
            int poY = (int) po.getPosition().getY();
            
            // S'assurer qu'on se trouve dans l'enceinte de l'objet.
            if (x >= poX && x <= poX + po.getWidth() &&
                y >= poY && y <= poY + po.getHeight())
                return po;
            else
                return null;
        }
        
        
        /**
         * Retourner une instance de RubberBand avec seulement
         * l'objet de gauche de défini.
         * @param left_po un PhysicalObject
         * @return un RubberBand
         */
        protected RubberBand getInstance(PhysicalObject left_po) {
            return new RubberBand(left_po, null);
        }
        
        
        /**
         * Clic droit: effacer l'élastique en cours de création
         * Premier clic: si l'utilisateur clique sur un objet, attacher
         *               l'élastique à cet objet, sinon créer une nouvelle Ball
         * Deuxième clic: si l'utilisateur clique sur un objet, attacher
         *                l'élastique à cet objet, sinon créer une nouvelle Ball
         */
        @Override
        public void mouseClicked(MouseEvent e) {
            // verifier le bouton du souris
            if (SwingUtilities.isRightMouseButton(e)) { 
                clear();
            } 
            else {
                int x = e.getX();
                int y = e.getY();
                PhysicalObject po = getObjectAt(x, y);
                
                // Si aucun objet physique est sélectionné, créer une boule
                // et l'ajouter au panneau.
                if (po == null) {
                    po = new Ball(x, y);
                    panneau.add(po);
                }
                
                // Lors du premier clic, créer un objet RubberBand
                if (rb == null) {
                    rb = getInstance(po);
                    panneau.add(rb);
                } 
                else {
                    rb.right_object = po;
                    
                    // Garder l'élastique seulement s'il est attaché à deux objets
                    // différents.
                    if (rb.left_object == rb.right_object) {
                        clear();
                    }
                    
                    rb = null;
                }
                panneau.repaint();
            }
        }
        
        
        /**
         * Modifier la position du curseur de souris de l'élastique
         * quand la souris bouge.
         */
        @Override
        public void mouseMoved(MouseEvent e) {
            if (rb != null) {
                rb.position = e.getPoint();
                panneau.repaint();
            }
        }
        
    }

}
