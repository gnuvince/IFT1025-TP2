package mecanique;

/**
 *
 * Interface pour tout ce qui peut être dessiné
 *
 * @author csuros
 */

import java.awt.Graphics2D;

public interface Drawable
{
    /**
     * C'est la méthode pour dessiner un tel objet
     *
     * @param G contexte graphique pour le dessin: la méthode n'a pas le droit de le changer
     */
    void draw(Graphics2D G);
}
