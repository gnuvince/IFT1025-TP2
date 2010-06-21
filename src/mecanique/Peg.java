package mecanique;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.geom.Point2D;

public class Peg implements Drawable, PhysicalObject {
    protected Point2D position;
    protected Point2D velocity;
    protected final static int DIAMETER = 17;
    protected final static int MASS = 0;
    
    protected Peg(double x, double y) {
        position = new Point2D.Double(x, y);
        velocity = new Point2D.Double();
    }

 
    @Override
    public void draw(Graphics2D G) {
        Graphics GCopy = G.create();
        GCopy.setColor(Color.GRAY);
        GCopy.fillOval((int)position.getX(), (int)position.getY(), DIAMETER, DIAMETER);
    }
    
    
    public static class Placement extends MouseAdapter implements ObjectPlacement {
        private SystemDisplay panneau;
        
        protected Placement(SystemDisplay panneau)
        {
            this.panneau = panneau;
        }
        
        public String getName() {
            return "Pieux";
        }
        
        public void clear() {
        }
        
        @Override
        public void mouseClicked(MouseEvent e) {
            int x = e.getX();
            int y = e.getY();
            
            panneau.add(new Peg(x, y));
        }
    }


    @Override
    public double getHeight() {
        return DIAMETER;
    }

    @Override
    public double getMass() {
        return MASS;
    }

    @Override
    public Point2D getPosition() {
        return position;
    }

    @Override
    public Point2D getVelocity() {
        return new Point2D.Double();
    }

    @Override
    public double getWidth() {
        return DIAMETER;
    }

    @Override
    public void setPosition(Point2D P) {
        position = P;
    }

    @Override
    public void setVelocity(Point2D v) {
        //velocity = v;
    }
    
    
    

}
