package mecanique;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.geom.Point2D;


public class BalleVeloce extends Ball {
    private boolean showVelocity = false;
    
    protected BalleVeloce(double x, double y, Point2D velo) {
        super(x, y);
        setVelocity(velo);
    }
    
    public void draw(Graphics2D g) {
        g.setColor(Color.DARK_GRAY);
        int x = (int)position.getX();
        int y = (int)position.getY();
        int dxdt = x + (int)velocity.getX();
        int dydt = y + (int)velocity.getY(); 
        g.fillOval(x, y, DIAMETER, DIAMETER);
        if (showVelocity) {
            g.drawLine(x + DIAMETER/2, y + DIAMETER/2, dxdt, dydt);
        }
    }
    
    
    public static class Placement extends MouseAdapter implements ObjectPlacement {
        private SystemDisplay panneau;
        private BalleVeloce bv;
        
        public Placement(SystemDisplay p) {
            panneau = p;
        }
        
        public String getName() {
            return "Balle véloce";
        }
        
        public void clear() {}
        
        @Override
        public void mouseClicked(MouseEvent e) {
            int x = e.getX();
            int y = e.getY();
            if (bv == null) {
                bv = new BalleVeloce(x, y, new Point2D.Double());
                bv.showVelocity = true;
                panneau.add(bv);
            }
            else {
                bv.setVelocity(computeInitialVelocity(x, y));
                bv.showVelocity = false;
                bv = null;
            }
            panneau.repaint();
        }
        
        @Override
        public void mouseMoved(MouseEvent e) {
            if (bv != null) {
                bv.setVelocity(computeInitialVelocity(e.getX(), e.getY()));
                panneau.repaint();
            }
        }
        
        private Point2D computeInitialVelocity(int x, int y) {
            return new Point2D.Double(
                    x - bv.getPosition().getX(),
                    y - bv.getPosition().getY()
            );
        }
    }
  
    
}
