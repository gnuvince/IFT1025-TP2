package mecanique;

import java.awt.geom.Point2D;
/**
 *
 * Abstraction d'objets physiques en 2D.
 *
 * @author csuros
 */
public interface PhysicalObject
{
    /**
     * Position de l'objet
     *
     * @return la position courante de l'objet
     */
    Point2D getPosition();

    /**
     * Affectation de la position
     *
     * @param P la nouvelle position de l'objet
     */
    void setPosition(Point2D P);

    /**
     * Vecteur de v&eacute;locit&eacute; pour l'objet
     *
     * @return vecteur (vitesse X, vitesse Y)
     */
    Point2D getVelocity();


    /**
     * Affectation de la v&eacute;locit&eacute; pour l'objet
     *
     * @param v vecteur (vitesse X, vitesse Y)
     */
    void setVelocity(Point2D v);

    /**
     * Largeur de cet objet
     *
     * @return largeur
     */
    double getWidth();

    /**
     * Hauteur de cet objet
     *
     * @return hauteur
     */
    double getHeight();

    /**
     * Masse de cet objet
     *
     * @return masse
     */
    double getMass();
}
