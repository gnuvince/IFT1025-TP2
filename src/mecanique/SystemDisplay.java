
package mecanique;



import java.util.ArrayList;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.awt.event.MouseEvent;

import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import javax.swing.ButtonGroup;
import javax.swing.Timer;
import javax.swing.SwingUtilities;

/**
/**
 * Panneau pour afficher un syst&egrave;me physique.
 *
 * @author csuros
 */
public class SystemDisplay extends JPanel
{
    static int SIMULATION_INTERVAL= 10; // en milliseconds

    private PhysicalSystem system;

    private ArrayList<Drawable> objects;

    private long tic=0L; // temps pass&eacute; dans le syst&egrave;me
    private Timer simulation_timer;

    private ArrayList<ObjectPlacement> type_selectors;
    private int active_type_selector;

    public SystemDisplay(){this(new PhysicalSystem());}

    protected SystemDisplay(PhysicalSystem system)
    {
        super();
        this.system = system;
        this.objects = new ArrayList<Drawable>();
        setPreferredSize(system.getSize());
        initComponents();
    }

    /**
     * Initialisation des composants graphiques
     */
    private void initComponents()
    {
        simulation_timer = new Timer(SIMULATION_INTERVAL, new ActionListener()
        {
            @Override
            public void actionPerformed(ActionEvent e)
            {
                system.computeNewPositions(SIMULATION_INTERVAL*0.001);
                tic++;
                repaint();
            }
        });
    }

    protected PhysicalSystem getSystem()
    {
        return this.system;
    }

    /**
     * Ajout d'un &eacute;l&eacute;ment au syst&egrave;me.
     *
     * @param o objet (Drawable, Force et/ou PhysicalObject)
     */
    protected void add(Object o)
    {
        if (o instanceof Force || o instanceof PhysicalObject)
            system.add(o);
        if (o instanceof Drawable)
        {
            objects.add((Drawable)o);
            repaint();
        }
    }

    /**
     * Suppression d'un &eacute;l&eacute;ment au syst&egrave;me.
     *
     * @param o objet (Drawable, Force et/ou PhysicalObject) - rien ne se passe si l'objet n'est pas dans le syst&egrave;me
     */
    protected void remove(Object o)
    {
        if (o instanceof Force || o instanceof PhysicalObject)
            system.remove(o);
        if (o instanceof Drawable)
        {
            objects.remove((Drawable)o);
            repaint();
        }
    }

    @Override
    public void paintComponent(Graphics g)
    {
        super.paintComponent(g);
        Graphics2D g2 = (Graphics2D) g.create();
        for (Drawable o: objects)
            o.draw(g2);
        if (tic != 0L)
        {
            g2.setColor(Color.RED);
            g2.drawString(Long.toString(tic), 30,30);
        }
    }

//    public Timer getSimulationTimer(){ return simulation_timer;}

    /**
     * Lance la simulation.
     */
    public void startSimulation()
    {
        simulation_timer.start();
    }

    /**
     * Arr&ecirc;te la simulation.
     */
    public void stopSimulation()
    {
        simulation_timer.stop();
    }


    /**
     * Classe pour traiter des &eacute;v&eacute;nements de souris.
     * Les &eacute;v&eacute;nements sont transmis vers
     * l'ObjectPlacement actif.
     */
    private class Souris implements MouseListener, MouseMotionListener
    {
        @Override
        public void mouseClicked(MouseEvent e)
        {
            if (!simulation_timer.isRunning())
                type_selectors.get(active_type_selector).mouseClicked(e);
        }

        @Override
        public void mouseEntered(MouseEvent e)
        {
            if (!simulation_timer.isRunning())
                type_selectors.get(active_type_selector).mouseEntered(e);
        }

        @Override
        public void mouseExited(MouseEvent e)
        {
            if (!simulation_timer.isRunning())
                type_selectors.get(active_type_selector).mouseExited(e);
        }

        @Override
        public void mousePressed(MouseEvent e)
        {
            if (!simulation_timer.isRunning())
                type_selectors.get(active_type_selector).mousePressed(e);
        }

        @Override
        public void mouseReleased(MouseEvent e)
        {
            if (!simulation_timer.isRunning())
                type_selectors.get(active_type_selector).mouseReleased(e);
        }

        @Override
        public void mouseDragged(MouseEvent e)
        {
            if (!simulation_timer.isRunning())
                type_selectors.get(active_type_selector).mouseDragged(e);
        }

        @Override
        public void mouseMoved(MouseEvent e)
        {
            if (!simulation_timer.isRunning())
                type_selectors.get(active_type_selector).mouseMoved(e);
        }
    }



    /**
     * Ajout des boutons pour lancer ou arr&acirc;ter la simulation.
     *
     * @param frame c'est le frame parent
     * @param panneau le panneau pour afficher le syst&egrave;me physique
     */
    private static void addSimulationButtons(JFrame frame, final SystemDisplay panneau)
    {
        Box button_box = new Box(BoxLayout.LINE_AXIS);
        final JButton Bstart = new JButton("Commencer la simulation");
        final JButton Bstop = new JButton("Arrêter la simulation");
        Bstop.setEnabled(false);
        Bstart.addActionListener(new ActionListener()
        {
            public void actionPerformed(ActionEvent ignored)
            {
                Bstart.setEnabled(false);
                Bstop.setEnabled(true);
                panneau.startSimulation();
            }
        });
        Bstop.addActionListener(new ActionListener()
        {
            public void actionPerformed(ActionEvent ignored)
            {
                Bstop.setEnabled(false);
                Bstart.setEnabled(true);
                panneau.stopSimulation();
            }
        });
        button_box.add(Box.createHorizontalGlue());
        button_box.add(Bstart);
        button_box.add(Box.createHorizontalGlue());
        button_box.add(Bstop);
        button_box.add(Box.createHorizontalGlue());
        frame.add(button_box, BorderLayout.SOUTH);
    }

    /**
     * Liste d'&eacute;l&eacute;ments diff&eacute;rents
     * impoant&eacute;s dans le syst&egrave;me.
     *
     * @param panneau affichage du syst&egrave;me
     * @return liste de tous les types
     */
    private static ArrayList<ObjectPlacement> getTypes(final SystemDisplay panneau)
    {
        ArrayList<ObjectPlacement> type_selectors = new ArrayList<ObjectPlacement>();

        type_selectors.add(new Ball.Placement(panneau));
        type_selectors.add(new RubberBand.Placement(panneau));
        type_selectors.add(new Spring.Placement(panneau));
        type_selectors.add(new BlackHole.Placement(panneau));
        type_selectors.add(new Stick.Placement(panneau));
        type_selectors.add(new BalleVeloce.Placement(panneau));
        type_selectors.add(new Peg.Placement(panneau));

        return type_selectors;
    }

    /**
     * Ajout des boutons radio et les &eacute;couteurs
     * pour le placement d'objets de types diff&eacute;rents.
     *
     * @param frame composant parent
     * @param panneau affichage du syst&egrave;me physique
     */
    private static void addObjectPlacement(JFrame frame, final SystemDisplay panneau)
    {
        Box box = new Box(BoxLayout.PAGE_AXIS);
        ButtonGroup types = new ButtonGroup();

        // recupérer la liste des éléments connus
        final ArrayList<ObjectPlacement> selectors = getTypes(panneau);
        // écouteur pour dispatcher les événements de souris
        // vers les instances d'ObjectPlacement
        ActionListener type_selection_listener = new ActionListener()
            {
                @Override
                public void actionPerformed(ActionEvent e)
                {
                    for (ObjectPlacement s: selectors)
                        s.clear();
                    int idx = Integer.parseInt(e.getActionCommand()); // commande d'action donne l'indice
                    panneau.active_type_selector = idx;
                    panneau.repaint();
                }
            };
        // ajouter un bouton radio pour chaque type
        for (int idx=0; idx<selectors.size(); idx++)
        {
            ObjectPlacement S = selectors.get(idx);
            JRadioButton bouton = new JRadioButton(S.getName());
            bouton.setSelected(idx==0); // séléctionner le premier
            bouton.setActionCommand(Integer.toString(idx)); // commande d'action donne l'indice
            bouton.addActionListener(type_selection_listener);
            types.add(bouton);
            box.add(bouton);
        }

        // activation de l'écouteur principal
        panneau.type_selectors = selectors;
        panneau.active_type_selector = 0;
        Souris listener = panneau.new Souris();
        panneau.addMouseListener(listener);
        panneau.addMouseMotionListener(listener);

        // addition du boîte contenant les boutons radio
        box.add(Box.createVerticalGlue());
        frame.add(box, BorderLayout.WEST);
    }

    /**
     * Lancement de l'interface graphique
     */
    private static void showGUI()
    {
        final SystemDisplay panneau = new SystemDisplay();
        panneau.setBorder(BorderFactory.createEtchedBorder());
        JFrame frame = new JFrame("Mécanique newtonienne");
        // l'exécution est finie quand la fenêtre est fermée
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.add(panneau, BorderLayout.CENTER);

        addSimulationButtons(frame, panneau);
        addObjectPlacement(frame, panneau);

        // faire calculer les dimensions du JFrame
        frame.pack();
        // afficher
        frame.setVisible(true);


    }

    public static void main(String[] args)
    {
        // lancer l'interface graphique à partir
        // d'Event Dispatch Thread
        SwingUtilities.invokeLater(new Runnable() 
        {
            @Override
            public void run() {
                showGUI();
            }
        });
    }


}
