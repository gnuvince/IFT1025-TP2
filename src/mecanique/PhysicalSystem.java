package mecanique;

/**
 *
 * Mod&eacute;lisation du comportement d'un syst&egrave;me physique.
 * Un syst&egrave;me comprend un ensemble d'objets et un ensemble de forces.
 *
 * @author csuros
 */

import java.awt.Dimension;
import java.util.ArrayList;
import java.awt.geom.Point2D;

public class PhysicalSystem
{
    /**
     * largeur
     */
    private int width;
    /**
     * hauteur
     */
    private int height;

    /**
     * Objets sur le bord
     */
    private ArrayList<PhysicalObject> objects;
    /**
     * Forces dans le syst&egrave;me
     */
    private ArrayList<Force> forces;


    /**
     * Instanciation d'un syst&egrave;me avec taille donn&eacute;e
     *
     * @param width largeur
     * @param height hauteur
     */
    public PhysicalSystem(int width, int height)
    {
        this.width = width;
        this.height = height;

        objects = new ArrayList<PhysicalObject>();
        forces = new ArrayList<Force>();
    }

    /**
     * Instanciation avec dimensions par d&eacute;faut
     */
    public PhysicalSystem(){this(600,600);}

    /**
     * Taille de ce syst&egrave;me
     *
     * @return taille du bord
     */
    public Dimension getSize(){return new Dimension(width,height);}

    /**
     * Ajoute un objet
     *
     * @param o objet &agrave; placer sur le bord
     */
    public void add(Object o)
    {
        if (o instanceof PhysicalObject)
            objects.add((PhysicalObject)o);
        if (o instanceof Force)
            forces.add((Force)o);
    }

    /**
     * Enl&grave;ve un objet pr&eacute;sent dans le syst&egrave;me
     *
     * @param o
     */
    public void remove(Object o)
    {
        if (o instanceof PhysicalObject)
            objects.remove((PhysicalObject)o);
        if (o instanceof Force)
            forces.remove((Force)o);
    }

    /**
     * Calcule les nouvelles positions des objets
     *
     * @param delta intervalle de temps (en seconds)
     */
    public void computeNewPositions(double delta)
    {
        // calculer les accelerations
        Point2D[] A = new Point2D[objects.size()];
        for (int idx=0; idx<objects.size(); idx++)
            A[idx]= getAcceleration(idx);
        for (int idx=0; idx<objects.size(); idx++)
           computeNewPosition(idx, A[idx], delta);
    }

    /**
     * Calcule la vecteur d'acc&eacute;l&eacute;rations
     *
     * @param object_idx indice de l'objet
     * @return vecteur (x,y) d'acc&eacute;le&eacute;ration
     */
    private Point2D getAcceleration(int object_idx)
    {
        PhysicalObject objet = objects.get(object_idx);
        double acc_x = 0.0;
        double acc_y = 0.0;
        for (int fidx=0;fidx<forces.size(); fidx++)
        {
            Force F = forces.get(fidx);
            Point2D a = F.getAcceleration(objet);
            acc_x += a.getX();
            acc_y += a.getY();
        }
        return new Point2D.Double(acc_x,acc_y);
    }


    /**
     * Calcule la nouvelle position d'un objet
     *
     * @param object_idx indice de l'objet
     * @param acceleration acc&eacute;l&eacute;ration de l'objet
     * @param delta intervalle de temps
     */
    private void computeNewPosition(int object_idx, Point2D acceleration, double delta)
    {
        PhysicalObject objet = objects.get(object_idx);
        Point2D position = objet.getPosition();
        Point2D velocity = objet.getVelocity();

        double px = position.getX();
        double vx = velocity.getX();
        double ax = acceleration.getX();
        double py = position.getY();
        double vy = velocity.getY();
        double ay = acceleration.getY();

        px += vx*delta;
        vx += ax*delta;
        py += vy*delta;
        vy += ay*delta;

        // verifier si toujours dans le cadre
        double dw = objet.getWidth()*0.5;
        if (px<dw)
        {
            // rebondir
            px = dw;
            vx = Math.abs(vx);
        } else if (px>=width-dw)
        {
            px = width-dw;
            vx = -Math.abs(vx);
        }
        double dh = objet.getHeight()*0.5;
        if (py<dh)
        {
            py = dh;
            vy = Math.abs(vy);
        } else if (py>=height-dh)
        {
            py = height-dh;
            vy = -Math.abs(vy);
        }
        Point2D newpos = new Point2D.Double(px,py);
        objet.setPosition(newpos );
        Point2D newvel = new Point2D.Double(vx,vy);
        objet.setVelocity(newvel);
    }


    /**
     * Trouve l'objet la plus proche &agrave; une position donn&eacute;e.
     *
     * @param x coordonn&eacute; x
     * @param y coordonn&eacute; y
     * @return null, si aucun objet, sinon l'objet ;le plus proche &agrave; x,y
     */
    public PhysicalObject closestObject(double x, double y)
    {
        PhysicalObject closest = null;
        double closest_dist = Double.POSITIVE_INFINITY;
        for (PhysicalObject o: objects)
        {
            double d = o.getPosition().distance(x,y);
            if (closest == null || closest_dist>d)
            {
                closest = o;
                closest_dist = d;
            }
        }
        return closest;
    }

}
