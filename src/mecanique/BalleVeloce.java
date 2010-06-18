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

//public class BalleVeloce implements Drawable, PhysicalObject {
//	private final static int DIAMETER = 10;
//    private final static int MASS = 1;
//    private Point2D position;
//    private Point2D velocity;
//    private boolean velocityVisible = false;
//    
//    protected BalleVeloce(double x, double y) {
//    	this(x, y, 0.0, 0.0);
//    }
//
//    protected BalleVeloce(double x, double y, double dxdt, double dydt) {
//        position = new Point2D.Double(x, y);
//        velocity = new Point2D.Double(dxdt, dydt);
//    }
//
//    @Override
//    public void draw(Graphics2D g) {
//    	g.setColor(Color.GREEN);
//    	int x = (int)position.getX();
//    	int y = (int)position.getY();
//    	int dxdt = (int)velocity.getX();
//    	int dydt = (int)velocity.getY(); 
//    	g.fillOval(x, y, DIAMETER, DIAMETER);
//    	if (velocityVisible) {
//    		g.drawLine(x, y, dxdt, dydt);
//    	}
//    }
//    
//    public static class Placement extends MouseAdapter implements ObjectPlacement {
//        private SystemDisplay panneau;
//        private BalleVeloce balleVeloce = null;
//        private int x, y, dxdt, dydt;
//        
//        protected Placement(SystemDisplay panneau)
//        {
//            this.panneau = panneau;
//        }
//        
//        public String getName() {
//            return "Boule Véloce";
//        }
//        
//        public void clear() {
//        	balleVeloce.setVelocityInvisible();
//        	balleVeloce = null;
//        	panneau.repaint();
//        }
//        
//        @Override
//        public void mouseMoved(MouseEvent e)
//        {
//            if (balleVeloce != null)
//            {
//            	dxdt = e.getX() - x;
//        		dydt = e.getY() - y;
//        		balleVeloce.setVelocity(new Point2D.Double(dxdt, dydt));
//                panneau.repaint();
//            }
//        }
//        
//        @Override
//        public void mouseClicked(MouseEvent e) {
//        	// verifier le bouton de la souris
//            if (SwingUtilities.isRightMouseButton(e))
//            	clear();
//            else {
//            	if (balleVeloce == null) {
//            		x = e.getX();
//            		y = e.getY();
//            		balleVeloce = new BalleVeloce(x, y);
//            		balleVeloce.toggleVelocityVisible();
//            		panneau.add(balleVeloce);        		
//            	} else {
//            		dxdt = e.getX() - x;
//            		dydt = e.getY() - y;
//            		balleVeloce.setVelocity(new Point2D.Double(dxdt, dydt));
//            		balleVeloce.toggleVelocityVisible();
//            		balleVeloce = null;
//            	}
//            }
//        	
//        	panneau.repaint();
//        }
//    }
//
//    public void toggleVelocityVisible() {
//    	velocityVisible = !velocityVisible;
//    }
//    
//    public void setVelocityVisible() {
//    	velocityVisible = true;
//    }
//    
//    public void setVelocityInvisible() {
//    	velocityVisible = false;
//    }
//    
//    public boolean getVelocityVisibility() {
//    	return velocityVisible;
//	}
//
//	@Override
//    public double getHeight() {
//        return DIAMETER;
//    }
//
//    @Override
//    public double getMass() {
//        return MASS;
//    }
//
//    @Override
//    public Point2D getPosition() {
//        return position;
//    }
//
//    @Override
//    public Point2D getVelocity() {
//        return velocity;
//    }
//
//    @Override
//    public double getWidth() {
//        return DIAMETER;
//    }
//
//    @Override
//    public void setPosition(Point2D P) {
//        position = P;
//    }
//
//    @Override
//    public void setVelocity(Point2D v) {
//        velocity = v;
//    }
//}
