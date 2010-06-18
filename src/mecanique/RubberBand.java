package mecanique;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.geom.Point2D;

import javax.swing.SwingUtilities;

public class RubberBand implements Drawable, Force {
    private static final double LENGTH = 100.0;
    private static final double RIGOR = 0.1;
    
    private PhysicalObject left_object = null;
    private PhysicalObject right_object = null;
    
    public RubberBand(PhysicalObject l, PhysicalObject r) {
        left_object = l;
        right_object = r;
    }
    
    private double getX1() { return left_object.getPosition().getX(); }
    private double getY1() { return left_object.getPosition().getY(); }
    private double getX2() { return right_object.getPosition().getX(); }
    private double getY2() { return right_object.getPosition().getY(); }
       
    @Override
    public void draw(Graphics2D G) {
        Graphics2D g2 = (Graphics2D) G.create();
        if (distance() > LENGTH)
            g2.setColor(Color.RED);
        else
            g2.setColor(Color.BLACK);
        g2.drawLine(
                (int) left_object.getPosition().getX(),
                (int) left_object.getPosition().getY(),
                (int) right_object.getPosition().getX(),
                (int) right_object.getPosition().getY()
               );
    }
    
    private double distance() {
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
        private SystemDisplay panneau;
        private RubberBand rb;

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
        
        private PhysicalObject getClosestObject(Point2D position) {
            PhysicalObject po = panneau.getSystem().closestObject(position.getX(), position.getY());
            return po;
        }
        
        @Override
        public void mouseClicked(MouseEvent e) {
            // verifier le bouton du souris
            if (SwingUtilities.isRightMouseButton(e)) { 
                clear();
            } 
            else {
                PhysicalObject po = getClosestObject(e.getPoint());
                if (rb == null) {
                    rb = new RubberBand(po, po);
                    panneau.add(rb);
                } 
                else {
                    rb.right_object = po;
                    
                    // Garder l'élastique seulement s'il est attaché à deux objets
                    // différents.
                    if (rb.left_object == null ||
                        rb.right_object == null ||
                        rb.left_object == rb.right_object) {
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
                PhysicalObject po = getClosestObject(e.getPoint());
                rb.right_object = po;
                panneau.repaint();
            }
        }
        
    }

}
