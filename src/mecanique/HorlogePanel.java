package mecanique;

import java.awt.Graphics;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.geom.Point2D;

import javax.swing.JPanel;
import javax.swing.Timer;

public class HorlogePanel extends JPanel {
    private int heure;
    private int minute;
    private int seconde;
    
    private Timer timerSeconde;
    
    public static final int DIAMETER = 40;
    private static final int HEURE_LONGUEUR = (int)(DIAMETER * 0.3);
    private static final int MINUTE_LONGUEUR = (int)(DIAMETER * 0.375);
    private static final int SECONDE_LONGUEUR = (int)(DIAMETER * 0.425);
    private static final int SECOND_PERIOD = 15;
    
    private int x;
    private int y;
    private int centerX;
    private int centerY;
    
    public HorlogePanel(int h, int m, int s, int x, int y) {
        setHeure(h);
        setMinute(m);
        setSeconde(s);
        this.x = x;
        this.y = y;
        centerX = x + DIAMETER/2;
        centerY = y + DIAMETER/2;
        
        timerSeconde = new Timer(SECOND_PERIOD, new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                setSeconde((getSeconde()+1) % 60);
                
                if (getSeconde() == 0) {
                    setMinute((getMinute()+1) % 60);
                    
                    if (getMinute() == 0) {
                        setHeure((getHeure()+1) % 24);
                    }
                }                    
                
                HorlogePanel.this.repaint();
            }
        });
        
        timerSeconde.start();
    }
    
    public Point2D getPosition() {
        return new Point2D.Double(x, y);
    }
    
    public void setPosition(Point2D p) {
        x = (int)p.getX();
        y = (int)p.getY();
    }


    public void setHeure(int heure) {
        this.heure = heure;
    }

    public int getHeure() {
        return heure;
    }

    public void setMinute(int minute) {
        this.minute = minute;
    }

    public int getMinute() {
        return minute;
    }

    public void setSeconde(int seconde) {
        this.seconde = seconde;
    }

    public int getSeconde() {
        return seconde;
    }

    
    public void paintComponent(Graphics g) {
        super.paintComponent(g);
        g.drawOval(x, y, DIAMETER, DIAMETER);
        g.drawLine(centerX, centerY, heureXPos(), heureYPos());
        g.drawLine(centerX, centerY, minuteXPos(), minuteYPos());
        g.drawLine(centerX, centerY, secondeXPos(), secondeYPos());
    }
    
    
    private int xpos(int current, int max, int length) {
        return (int)(Math.cos(current / (double)max * 2 * Math.PI - Math.PI/2.0) 
                * length) + centerX;
    }
    
    private int ypos(int current, int max, int length) {
        return (int)(Math.sin(current / (double)max * 2 * Math.PI - Math.PI/2.0) 
                * length) + centerY;
    }
    
    private int heureXPos()   { return xpos(getHeure(),   24, HEURE_LONGUEUR); }
    private int heureYPos()   { return ypos(getHeure(),   24, HEURE_LONGUEUR); }
    private int minuteXPos()  { return xpos(getMinute(),  60, MINUTE_LONGUEUR); }
    private int minuteYPos()  { return ypos(getMinute(),  60, MINUTE_LONGUEUR); }
    private int secondeXPos() { return xpos(getSeconde(), 60, SECONDE_LONGUEUR); }
    private int secondeYPos() { return ypos(getSeconde(), 60, SECONDE_LONGUEUR); }
}
