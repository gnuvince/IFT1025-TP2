package mecanique;

import java.awt.Graphics2D;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.geom.Point2D;

public class Horloge implements PhysicalObject, Drawable {
    private HorlogePanel horloge;
    
    public Horloge(double x, double y) {
        horloge = new HorlogePanel(0, 0, 0, (int)x, (int)y);
    }
    
    @Override
    public double getHeight() {
        return HorlogePanel.DIAMETER;
    }

    @Override
    public double getMass() {
        return Double.POSITIVE_INFINITY;
    }

    @Override
    public Point2D getPosition() {
        return horloge.getPosition();
    }

    @Override
    public Point2D getVelocity() {
        return new Point2D.Double();
    }

    @Override
    public double getWidth() {
        return HorlogePanel.DIAMETER;
    }

    @Override
    public void setPosition(Point2D P) {
        horloge.setPosition(P);
    }

    @Override
    public void setVelocity(Point2D v) {
        // Ignore
    }

    @Override
    public void draw(Graphics2D G) {
        horloge.paintComponent(G);
    }
    
    
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
