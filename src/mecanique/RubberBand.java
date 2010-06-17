package mecanique;

import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.geom.Point2D;

public class RubberBand implements Drawable, Force {
    private static final double LENGTH = 100.0;
    private static final double RIGOR = 0.1;
    
    public PhysicalObject left = null;
    public PhysicalObject right = null;
    public boolean finishedPlacing = false;
    
       
    @Override
    public void draw(Graphics2D G) {

    }

    @Override
    public Point2D getAcceleration(PhysicalObject object) {
        // TODO Auto-generated method stub
        return null;
    }
    
    public static class Placement extends MouseAdapter implements ObjectPlacement {
        private SystemDisplay panneau;
        private Point2D start = null;
        private Point2D finish = null;
        private RubberBand rubberband;

        protected Placement(SystemDisplay panneau)
        {
            this.panneau = panneau;
            rubberband = new RubberBand();
            this.panneau.add(rubberband);
        }
        
        public String getName() {
            return "Élastique";
        }
        
        public void clear() {
        }
        
        @Override
        public void mouseClicked(MouseEvent e) {
        }
        
        @Override
        public void mouseMoved(MouseEvent e) {
        }
        
    }

}
