package mecanique;

import java.awt.Color;
import java.awt.geom.Point2D;


/**
 * Classe implémentant un ressort.  Un ressort est une sous-classe de RubberBand
 * où le calcul de l'accélération est différent.
 */
public class Spring extends RubberBand {
    // Le ressort a une rigueur plus élevé que l'élastique
    protected static final double RIGOR = 0.8666;
    
    public Spring(PhysicalObject l, PhysicalObject r) {
        super(l, r);
    }
    
    // Utiliser des couleurs différentes pour qu'un ressort soit visuellement
    // distinct d'un élastique.
    @Override
    protected Color getTenseColor() {
        return Color.PINK;
    }
    
    @Override
    protected Color getNormalColor() {
        return Color.GRAY;
    }
    
    
    /**
     * Calcul de l'accélération en utilisant la formule donnée dans l'énoncé.
     */
    @Override
    public Point2D getAcceleration(PhysicalObject object) {
        double d = distance();
        double f = Math.abs(RIGOR * (d - LENGTH));
        double theta = Math.atan2(getY2() - getY1(), getX2() - getX1());
        int invert = d < LENGTH ? -1 : 1;
        int direction;
        if (object == left_object)
            direction = 1;
        else if (object == right_object)
            direction = -1;
        // Ignorer les objets qui ne sont pas attachés à l'élastique
        else 
            direction = 0;
        
        return new Point2D.Double(
            invert * direction * f * Math.cos(theta),
            invert * direction * f * Math.sin(theta)
        );
    }
    
    
    /**
     * Classe servant à placer un ressort.
     */
    public static class Placement extends RubberBand.Placement {
        protected Placement(SystemDisplay panneau) {
            super(panneau);
        }
        
        
        /**
         * Retourne une instance d'un ressort où le premier objet est fixé.
         */
        @Override
        protected RubberBand getInstance(PhysicalObject left_po) {
            return new Spring(left_po, null);
        }
        
        @Override
        public String getName() {
            return "Ressort";
        }
    }
}
