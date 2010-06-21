package mecanique;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.geom.Point2D;

import javax.swing.SwingUtilities;

public class Spring implements Drawable, Force {
    protected static double length;
    protected static final double RIGOR = 0.1;
    
    protected PhysicalObject left_object = null;
    protected PhysicalObject right_object = null;
    
    // La position temporaire de la deuxième extrémité de l'élastique
    protected Point2D position = null;
    
    public Spring(PhysicalObject l, PhysicalObject r) {
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
        double f = Math.abs(RIGOR * (d - length));
        int repulsivity = d < length ? -1 : 1;
        //double f = d < LENGTH ? -(RIGOR * (d - LENGTH)) : RIGOR * (d - LENGTH);
        double
        	x1 = getX1(),
        	y1 = getY1(),
        	x2 = getX2(),
        	y2 = getY2();
        double theta = Math.atan2(y2 - y1, x2 - x1);
        //double theta = Math.atan2(getY2() - getY1(), getX2() - getX1());
        int direction = object == left_object ? 1 : -1;
        
        double x = repulsivity * direction * f * Math.cos(theta);
        double y = repulsivity * direction * f * Math.sin(theta);
        
        return new Point2D.Double(x, y);
    }



    public static class Placement extends MouseAdapter implements ObjectPlacement {
        protected SystemDisplay panneau;
        protected Spring rb;

        protected Placement(SystemDisplay panneau) {
            this.panneau = panneau;
            rb = null;
        }
        
        public String getName() {
            return "Ressort";
        }
        
        public void clear() {
            panneau.remove(rb);
            rb = null;
            panneau.repaint();
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
                
                // Lors du premier clic, créer un objet Spring
                if (rb == null) {
                    rb = new Spring(po, null);
                    panneau.add(rb);
                } 
                else {
                    rb.right_object = po;
                    
                    // Garder le ressort seulement s'il est attaché à deux objets
                    // différents.
                    if (rb.left_object == rb.right_object) {
                        clear();
                    } else {
                    	double dist = rb.distance();
                    	length =  1.5 * dist;	// length = dist + 50%
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
