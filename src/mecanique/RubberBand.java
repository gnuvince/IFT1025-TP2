package mecanique;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.geom.Point2D;

import javax.swing.SwingUtilities;

public class RubberBand implements Drawable, Force {
    protected static final double LENGTH = 100.0;
    protected static final double RIGOR = 0.1;
    
    protected PhysicalObject left_object = null;
    protected PhysicalObject right_object = null;
    
    // La position temporaire de la deuxième extrémité de l'élastique
    protected Point2D position = null;
    
    public RubberBand(PhysicalObject l, PhysicalObject r) {
        left_object = l;
        right_object = r;
        position = left_object.getPosition();
    }
    
    protected double getX1() { return left_object.getPosition().getX(); }
    protected double getY1() { return left_object.getPosition().getY(); }
    
    // Si le deuxième objet n'est pas encore sélectionné, la position de X2 et
    // Y2 est la position du curseur de la souris (qui se trouve dans position)
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
    
       
    @Override
    public void draw(Graphics2D G) {
        Graphics2D g2 = (Graphics2D) G.create();
        if (distance() > LENGTH)
            g2.setColor(Color.RED);
        else
            g2.setColor(Color.BLACK);
        g2.drawLine((int) getX1(), (int) getY1(), (int) getX2(), (int) getY2());
    }
    
    protected double distance() {
        double a = getX1() - getX2();
        double b = getY1() - getY2();
        return Math.sqrt(a*a + b*b);
    }

    @Override
    public Point2D getAcceleration(PhysicalObject object) {
        double d = distance();
        double f;
        if (d <= LENGTH) {
            f = 0;
        }
        else {
            f = RIGOR * (d - LENGTH);
        }
        double theta = Math.atan2(getY2() - getY1(), getX2() - getX1());
        
        if (object == left_object) {
            return new Point2D.Double(
                    f * Math.cos(theta),
                    f * Math.sin(theta)
            );
        }
        else {
            return new Point2D.Double(
                    -f * Math.cos(theta),
                    -f * Math.sin(theta)
            );
        }
    }



    public static class Placement extends MouseAdapter implements ObjectPlacement {
        protected SystemDisplay panneau;
        protected RubberBand rb;

        protected Placement(SystemDisplay panneau) {
            this.panneau = panneau;
            rb = null;
        }
        
        public String getName() {
            return "Élastique";
        }
        
        public void clear() {
            if (rb != null) {
                panneau.remove(rb);
                rb = null;
                panneau.repaint();
            }
        }
        
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
                    rb = new RubberBand(po, null);
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
        
        @Override
        public void mouseMoved(MouseEvent e) {
            if (rb != null) {
                rb.position = e.getPoint();
                panneau.repaint();
            }
        }
        
    }

}
