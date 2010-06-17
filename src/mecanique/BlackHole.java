
package mecanique;

import java.awt.geom.Point2D;
import java.awt.Graphics2D;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

/**
 *
 * Force gravitationnelle: trou noir.
 *
 * @author csuros
 */
public class BlackHole implements Drawable, Force
{
    private static final int DIAMETER =6;
    private static final double FORCE_MULTIPLIER = 8e4;

    /**
     * Instanciation avec la position.
     *
     * @param x coordonn&eacute;e x
     * @param y coordonn&eacute;e y
     */
    public BlackHole(double x, double y)
    {
        this(new Point2D.Double(x,y));
    }

    /**
     * Instanciation avec la position.
     *
     * @param position position du trou noir
     */
    public BlackHole(Point2D position)
    {
        this.position = position;
    }

    private Point2D position;

    @Override
    public void draw(Graphics2D g)
    {
        int x = (int)position.getX();
        int y = (int)position.getY();
        g.fillOval(x-DIAMETER/2, y-DIAMETER/2, DIAMETER, DIAMETER);
        for (int d = 2; d<6; d+=3)
            g.drawOval(x-DIAMETER/2-d, y-DIAMETER/2-d, DIAMETER+2*d, DIAMETER+2*d);
    }

    /**
     * La force est proportionnelle &agrave; l'inverse de la distance 
     * euclid&eacute;enne carr&eacute;e. 
     * 
     * @param o objet sur lequel on calcule l'acc&eacute;l&eacute;ration
     * @return vecteur d'acc&eacute;l&eacute;ration
     */
    @Override
    public Point2D getAcceleration(PhysicalObject o)
    {
        double mass = o.getMass();
        if (mass!=0.0 && !Double.isInfinite(mass)) // objets bizarres...
        {
            Point2D opos = o.getPosition();
            double d = position.distance(opos);
            if (d<DIAMETER/2)
            {
                o.setVelocity(new Point2D.Double(0.,0.));
                o.setPosition(position);
                return new Point2D.Double(0.0,0.0);
            }
            double F = FORCE_MULTIPLIER / (d*d);
            double dx = opos.getX()-position.getX();
            double dy = opos.getY()-position.getY();

            double theta = Math.atan2(dy, dx);
            double ax = -F*Math.cos(theta);
            double ay = -F*Math.sin(theta);

//            System.out.println("#**BH.gA o: "+ax+", "+ay);
            return new Point2D.Double(ax,ay);
        } else
            return new Point2D.Double(0,0);
    }

    /**
     * Placement d'un trou noir: un clic suffit.
     */
    static class Placement extends MouseAdapter implements ObjectPlacement
    {
        protected Placement(SystemDisplay panneau)
        {
            this.panneau = panneau;
        }
        private SystemDisplay panneau;

        @Override
        public String getName()
        {
            return "Trou noir";
        }
        @Override
        public void clear()
        {}
        @Override
        public void mouseClicked(MouseEvent e)
        {
            int x = e.getX();
            int y = e.getY();
            PhysicalObject closest_object = panneau.getSystem().closestObject(x,y);

            // ajout d'un trou noir 
            // si clic n'est pas trop proche d'un objet physique
            if (closest_object == null ||
                    (Math.abs(closest_object.getPosition().getX()-x)>closest_object.getWidth()*0.5
                    &&Math.abs(closest_object.getPosition().getY()-y)>closest_object.getHeight()*0.5))

            {
                panneau.add(new BlackHole(x,y));
            }
        }
    }
}
