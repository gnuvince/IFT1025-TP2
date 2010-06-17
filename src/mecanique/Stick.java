
package mecanique;

import java.awt.Graphics2D;
import java.awt.geom.Point2D;

import java.awt.event.MouseEvent;
import java.awt.event.MouseAdapter;
import javax.swing.SwingUtilities;

/**
 *
 * Un b&aciric;ton
 *
 * @author csuros
 */
public class Stick implements Drawable, PhysicalObject
{

    private Point2D pos_left;
    private Point2D pos_right;
    private Point2D velocity;

    /**
     * Instanciation avec les deux bouts
     *
     * @param pos_left bout gauche
     * @param pos_right bout droit
     */
    public Stick(Point2D pos_left, Point2D pos_right)
    {
        this.pos_left = new Point2D.Double(pos_left.getX(), pos_left.getY());
        this.pos_right = new Point2D.Double(pos_right.getX(), pos_right.getY());
        velocity = new Point2D.Double();
    }

    @Override
    public void draw(Graphics2D g)
    {
        int xg = (int) pos_left.getX();
        int yg = (int) pos_left.getY();
        int xd = (int) pos_right.getX();
        int yd = (int) pos_right.getY();
        g.drawLine(xg, yg, xd, yd);
        Point2D ctr = getPosition();
        g.drawOval(-1+(int)ctr.getX(),-1+(int)ctr.getY(), 2, 2);
    }

    /**
     * Position du centre.
     * 
     * @return position du b&acirc;ton
     */
    @Override
    public Point2D getPosition()
    {
        double x_ctr = (pos_left.getX()+pos_right.getX())/2.0;
        double y_ctr = (pos_left.getY()+pos_right.getY())/2.0;
        return new Point2D.Double(x_ctr, y_ctr);
    }

    /**
     * D&eacute;placement du b&acirc;ton
     *
     * @param P nouvelle position du centre
     */
    @Override
    public void setPosition(Point2D P)
    {
        double x_ctr = (pos_left.getX()+pos_right.getX())/2.0;
        double y_ctr = (pos_left.getY()+pos_right.getY())/2.0;
        double dx = P.getX()-x_ctr;
        double dy = P.getY()-y_ctr;
        pos_left = new Point2D.Double(pos_left.getX()+dx, pos_left.getY()+dy);
        pos_right = new Point2D.Double(pos_right.getX()+dx, pos_right.getY()+dy);
    }

    @Override
    public Point2D getVelocity()
    {
        return velocity;
    }

    @Override
    public void setVelocity(Point2D v)
    {
        this.velocity = v;
    }

    @Override
    public double getWidth()
    {
        return Math.abs(pos_left.getX()-pos_right.getX());
    }

    @Override
    public double getHeight()
    {
        return Math.abs(pos_left.getY()-pos_right.getY());
    }

    @Override
    public double getMass()
    {
        return 1.0;
    }

    /**
     * Classe pour le placement du b&acirc;ton.
     * Premier clic choisit le bout gauche, deuxi&egrave;me clic choisit
     * le bout droit.
     */
    static class Placement extends MouseAdapter implements ObjectPlacement
    {
        protected Placement(SystemDisplay panneau)
        {
            this.panneau = panneau;
            this.currently_placed = null;
        }
        private SystemDisplay panneau;
        private Stick currently_placed;

        @Override
        public String getName()
        {
            return "Bâton";
        }

        @Override
        public void mouseMoved(MouseEvent e)
        {
            if (currently_placed != null)
            {
                currently_placed.pos_right = e.getPoint();
                panneau.repaint();
            }
        }

        @Override
        public void mouseClicked(MouseEvent e)
        {
            // verifier le bouton du souris
            if (SwingUtilities.isRightMouseButton(e))
            { // annuler le dessin
                clear();
            } else
            {
                // il faut verifier si c'est le cote gauche ou droit d'un baton
                if (currently_placed == null)
                {

                    currently_placed = new Stick(e.getPoint(), e.getPoint());
                    panneau.add(currently_placed);
                } else
                {
                    currently_placed.pos_right = e.getPoint();
                    currently_placed = null;
                }
                panneau.repaint();
            }
        }

        @Override
        public void clear()
        {
            if (currently_placed != null)
            {
                panneau.remove(currently_placed);
                currently_placed = null;
                panneau.repaint();
            }
        }
    }
}
