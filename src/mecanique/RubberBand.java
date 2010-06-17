package mecanique;

import java.awt.Graphics2D;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.geom.Point2D;

import javax.swing.SwingUtilities;

public class RubberBand implements Drawable, Force {
    private static final double LENGTH = 100.0;
    private static final double RIGOR = 0.1;
    
    private PhysicalSystem system = null;
    
    private Point2D left_point = null;
    private Point2D right_point = null;
    private PhysicalObject left_object = null;
    private PhysicalObject right_object = null;
    
    public RubberBand(Point2D l, Point2D r, PhysicalSystem system) {
        this.system = system;
        setLeft_point(new Point2D.Double(l.getX(), l.getY()));
        setRight_point(new Point2D.Double(r.getX(), r.getY()));
    }
       
    @Override
    public void draw(Graphics2D G) {
        Graphics2D g2 = (Graphics2D) G.create();
        g2.drawLine(
                (int) getLeft_point().getX(),
                (int) getLeft_point().getY(),
                (int) getRight_point().getX(),
                (int) getRight_point().getY());
    }

    @Override
    public Point2D getAcceleration(PhysicalObject object) {
        return new Point2D.Double();
    }
    
    
    public void setLeft_point(Point2D left_point) {
        this.left_point = left_point;
        this.left_object = system.closestObject(
                left_point.getX(), left_point.getY());
    }

    public Point2D getLeft_point() {
        return left_point;
    }


    public void setRight_point(Point2D right_point) {
        this.right_point = right_point;
        this.right_object = system.closestObject(
                right_point.getX(), right_point.getY());
    }

    public Point2D getRight_point() {
        return right_point;
    }
    
    private void fixPoints() {
        if (left_object != null && right_object != null) {
            setLeft_point(left_object.getPosition());
            setRight_point(right_object.getPosition());
        }
    }



    public static class Placement extends MouseAdapter implements ObjectPlacement {
        private SystemDisplay panneau;
        private RubberBand rb;

        protected Placement(SystemDisplay panneau)
        {
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
        
        @Override
        public void mouseClicked(MouseEvent e) {
            // verifier le bouton du souris
            if (SwingUtilities.isRightMouseButton(e)) { 
                clear();
            } 
            else {
                if (rb == null) {
                    rb = new RubberBand(
                            e.getPoint(),
                            e.getPoint(),
                            panneau.getSystem());
                    panneau.add(rb);
                } 
                else {
                    rb.setRight_point(e.getPoint());
                    
                    // Garder l'élastique seulement s'il est attaché à deux objets
                    // différents.
                    if (rb.left_object == null ||
                        rb.right_object == null ||
                        rb.left_object == rb.right_object) {
                        clear();
                    }
                    else {
                        rb.fixPoints();
                    }
                    
                    rb = null;
                }
                panneau.repaint();
            }
        }
        
        @Override
        public void mouseMoved(MouseEvent e) {
            if (rb != null) {
                rb.setRight_point(e.getPoint());
                panneau.repaint();
            }
        }
        
    }

}
