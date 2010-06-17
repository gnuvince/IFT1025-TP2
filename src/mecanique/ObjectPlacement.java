
package mecanique;

import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;

/**
 * Interface pour le placement d'&eacute;l&eacute;ments
 * sur le bord.
 *
 * @author csuros
 */
public interface ObjectPlacement extends MouseListener, MouseMotionListener
{
    /**
     * Nom de ce type d'objet affich&eacute; p[ar le bouton associ&eacute;
     *
     * @return nome du type d'&eacute;l&eacute;ment
     */
    String getName();

    /**
     * Annulation du placement de ce type. Appel&eacute;
     * quand la s&eacute;lection de types change.
     */
    void clear();
}
