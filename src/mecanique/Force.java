package mecanique;

import java.awt.geom.Point2D;

/**
 * Source de forces
 *
 * @author csuros
 */
public interface Force
{
    /**
     * Calcul de la vecteur d'acc&eacute;l&eacute;ration
     * d'un objet casu&eacute; par cette force.
     *
     * @param object un objet physique
     * @return vecteur d'accélération selon les lois de Newton (force/masse)
     */
    Point2D getAcceleration(PhysicalObject object);
}
