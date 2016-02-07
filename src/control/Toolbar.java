/**
 * 
 */
package control;

import java.awt.BorderLayout;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;

import javax.swing.ButtonGroup;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JCheckBoxMenuItem;
import javax.swing.JColorChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JRadioButtonMenuItem;
import javax.swing.JSlider;
import javax.swing.JToolBar;
import javax.swing.KeyStroke;
import javax.swing.WindowConstants;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import javax.swing.event.MenuEvent;
import javax.swing.event.MenuListener;

/**
 * @author ao1013
 * @version 11/7/15 TCSS 305
 */
public class Toolbar {
    /**
     * String for title icon, used multiple times in program.
     */
    private static final String TITLE_ICON = "./images/w.gif";
    /**
     * String array to store pencil information.
     */
    private static final String[] PENCIL_STRING = {"pencil", "./images/pencil.gif"};
    /**
     * String array to store line information.
     */
    private static final String[] LINE_STRING = {"Line", "./images/line.gif"};
    /**
     * String array to store rectangle information.
     */
    private static final String[] RECTANGLE_STRING = {"Rectangle", "./images/rectangle.gif"};
    /**
     * String array to store ellipse information.
     */
    private static final String[] ELLIPSE_STRING = {"Ellipse", "./images/ellipse.gif"};
   /**
    * The Menu Bar for the frame.
    */
    private JMenuBar myMenuBar;
    /**
     * The frame of the paint program that holds all components.
     */
    private JFrame myFrame;
    /**
     * The dragable tool bar.
     */
    private JToolBar myMovableTool;
    /**
     * The color object to hold the selected color.
     */
    private java.awt.Color myCurrentColor;
    /**
     * 
     */
    private int myPenThickness;
    /**
     * The canvas object, where all painting occurs.
     */
    private Canvas myCanvas;
    /**
     * Button group to hold the buttons in the main menu.
     */
    private final ButtonGroup myGroup = new ButtonGroup();
    /**
     * Button group to hold the buttons in the movable tool bar.
     */
    private final ButtonGroup myMovableGroup = new ButtonGroup();
    /**
     * Default constructor to initialize all components.
     */
    public Toolbar() {
        myCurrentColor = java.awt.Color.BLACK;
        myFrame = new JFrame("PowerPaint");
        final ImageIcon tempIcon = new ImageIcon(TITLE_ICON);
        myFrame.setIconImage(tempIcon.getImage());
        myCanvas = new Canvas();
        myFrame.add(myCanvas, BorderLayout.CENTER);
        myMovableTool = new JToolBar();
        myMenuBar = new JMenuBar();
    }
   /**
    * The start method to begin the frame with all elements.
    */
    public void start() {
        this.setupFile();
        this.setupOptions();
        this.setupToolsInMenu();
        this.setupHelp();
        myFrame.pack();
        myFrame.setVisible(true);
        myFrame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
    }
    /**
     * Initializer method to setup the File within the main menu.
     */
    void setupFile() {
        final JMenu fileMenu = new JMenu("File");
        fileMenu.setMnemonic(KeyEvent.VK_F);
        final JMenuItem exit = new JMenuItem("Exit", KeyEvent.VK_X);
        exit.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(final ActionEvent theEvent) {
                myFrame.setVisible(false); 
                myFrame.dispose(); 
            }
        });
        final JMenuItem undoLast = new JMenuItem("Undo", KeyEvent.VK_N);
        final JMenuItem redoLast = new JMenuItem("Redo", KeyEvent.VK_R);
        final JMenuItem undoAll = new JMenuItem("Undo all changes", KeyEvent.VK_U);
        undoAll.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(final ActionEvent theEvent) {
                myCanvas.clearScreen();
                undoAll.setEnabled(false);
                redoLast.setEnabled(false);
                undoLast.setEnabled(false);
            }
        });
        undoLast.setAccelerator(KeyStroke.getKeyStroke
                                (KeyEvent.VK_Z, 
                                 Toolkit.getDefaultToolkit().getMenuShortcutKeyMask()));
        undoLast.addActionListener(new ActionListener() {
            public void actionPerformed(final ActionEvent theEvent) {
                myCanvas.undoScreen();
                if (myCanvas.isEmpty()) {
                    undoLast.setEnabled(false);
                } else {
                    undoLast.setEnabled(true);
                }
                redoLast.setEnabled(true);
            }
        });
        redoLast.setAccelerator(KeyStroke.getKeyStroke
                                (KeyEvent.VK_Y,
                                 Toolkit.getDefaultToolkit().getMenuShortcutKeyMask()));
        redoLast.addActionListener(new ActionListener() {
            public void actionPerformed(final ActionEvent theEvent) {
                myCanvas.redoScreen(redoLast);
                undoLast.setEnabled(true);
            }
        });
        
        fileMenu.addMenuListener(new MenuListener() {
            @Override
            public void menuSelected(final MenuEvent theEvent) {
                if (!myCanvas.isEmpty()) {
                    undoLast.setEnabled(true);
                    undoAll.setEnabled(true);
                }
            }
            @Override
            public void menuDeselected(final MenuEvent theEvent) {
            }

            @Override
            public void menuCanceled(final MenuEvent theEvent) {
            }
        });
        undoLast.setEnabled(false);
        redoLast.setEnabled(false);
        undoAll.setEnabled(false);
        fileMenu.add(undoLast);
        fileMenu.add(redoLast);
        fileMenu.add(undoAll);
        fileMenu.addSeparator();
        fileMenu.add(exit);
        myMenuBar.add(fileMenu);
    }
    /**
     * Initializer method to setup the Options within the main menu.
     */
    void setupOptions() {
        final int min = 0;
        final int max = 20;
        final int majorTickSpacing = 5;
        final JMenu options = new JMenu("Options");
        options.setMnemonic(KeyEvent.VK_O);
        final JCheckBoxMenuItem grid = new JCheckBoxMenuItem("Grid");
        grid.addActionListener(new ActionListener() {
            public void actionPerformed(final ActionEvent theEvent) {
                myCanvas.switchGrid(grid.isSelected());
            }
        });
        grid.setMnemonic(KeyEvent.VK_G);
        final JMenu thickness = new JMenu("Thickness");
        thickness.setMnemonic(KeyEvent.VK_T);
        final JSlider slider = new JSlider(JSlider.HORIZONTAL, min, max, 1);
        slider.setMajorTickSpacing(majorTickSpacing);
        slider.setMinorTickSpacing(1);
        slider.setPaintTicks(true);
        slider.setPaintLabels(true);
        slider.addChangeListener(new ChangeListener() {
            public void stateChanged(final ChangeEvent theEvent) { 
                myPenThickness = slider.getValue();
                myCanvas.setTools(myCurrentColor, myPenThickness);
            }
        });
        thickness.add(slider);
        final ColorSwatch swatch = new ColorSwatch();
        final JMenuItem color = new JMenuItem("Color...", swatch);
        color.setMnemonic(KeyEvent.VK_C);
        color.addActionListener(new ActionListener() {
            public void actionPerformed(final ActionEvent theEvent) {
                myCurrentColor = JColorChooser.showDialog(myFrame, 
                                                          "Choose a color", myCurrentColor);
                myCanvas.setTools(myCurrentColor, myPenThickness);
                swatch.setColor(myCurrentColor);
                color.setIcon(swatch);
            }
        });
        options.add(grid);
        options.addSeparator();
        options.add(thickness);
        options.addSeparator();
        options.add(color);
        myMenuBar.add(options);
    }
    /**
     * Initializer method to setup the Tools within the main menu.
     */
    void setupToolsInMenu() {
        final JMenu tools = new JMenu("Tools");
        tools.setMnemonic(KeyEvent.VK_T);
        final JRadioButtonMenuItem pencil = new JRadioButtonMenuItem(PENCIL_STRING[0],
                                                    new ImageIcon(PENCIL_STRING[1]));
        pencil.addActionListener(new ActionListener() {
            public void actionPerformed(final ActionEvent theEvent) {
                myCanvas.setTools(0);
            }
        });
      
        pencil.setSelected(true);
        pencil.setMnemonic(KeyEvent.VK_P);
        final JRadioButtonMenuItem line = new JRadioButtonMenuItem(LINE_STRING[0],
                                                    new ImageIcon(LINE_STRING[1]));
        line.addActionListener(new ActionListener() {
            public void actionPerformed(final ActionEvent theEvent) {
                myCanvas.setTools(1);
            }
        });
        line.setMnemonic(KeyEvent.VK_L);
        final JRadioButtonMenuItem rectangle = new JRadioButtonMenuItem(RECTANGLE_STRING[0],
                                                    new ImageIcon(RECTANGLE_STRING[1]));
        rectangle.addActionListener(new ActionListener() {
            public void actionPerformed(final ActionEvent theEvent) {
                myCanvas.setTools(3);
            }
        });
        rectangle.setMnemonic(KeyEvent.VK_R);
        final JRadioButtonMenuItem ellipse = new JRadioButtonMenuItem(ELLIPSE_STRING[0],
                                                    new ImageIcon(ELLIPSE_STRING[1]));
        ellipse.addActionListener(new ActionListener() {
            public void actionPerformed(final ActionEvent theEvent) {
                myCanvas.setTools(2);
            }
        });
        ellipse.setMnemonic(KeyEvent.VK_E);
        
        
        myGroup.add(pencil);
        myGroup.add(line);
        myGroup.add(rectangle);
        myGroup.add(ellipse);
        tools.add(pencil);
        tools.addSeparator();
        tools.add(line);
        tools.addSeparator();
        tools.add(rectangle);
        tools.addSeparator();
        tools.add(ellipse);
        myMenuBar.add(tools, BorderLayout.NORTH);
       
        final JButton mPencil = new JButton(PENCIL_STRING[0],
                                            new ImageIcon(PENCIL_STRING[1]));
        mPencil.addActionListener(new ActionListener() {
            public void actionPerformed(final ActionEvent theEvent) {
                pencil.setSelected(true);
                myCanvas.setTools(0);
            }
        });
        mPencil.setSelected(true);
        final JButton mLine = new JButton(LINE_STRING[0],
                                          new ImageIcon(LINE_STRING[1]));
        mLine.addActionListener(new ActionListener() {
            public void actionPerformed(final ActionEvent theEvent) {
                line.setSelected(true);
                myCanvas.setTools(1);
            }
        });
        final JButton mRectangle = new JButton(RECTANGLE_STRING[0],
                                               new ImageIcon(RECTANGLE_STRING[1]));
        mRectangle.addActionListener(new ActionListener() {
            public void actionPerformed(final ActionEvent theEvent) {
                rectangle.setSelected(true);
                myCanvas.setTools(3);
            }
        });
        final JButton mEllipse = new JButton(ELLIPSE_STRING[0],
                                             new ImageIcon(ELLIPSE_STRING[1]));
        mEllipse.addActionListener(new ActionListener() {
            public void actionPerformed(final ActionEvent theEvent) {
                ellipse.setSelected(true);
                myCanvas.setTools(2);
            }
        });
        
        myMovableGroup.add(mPencil);
        myMovableGroup.add(mLine);
        myMovableGroup.add(mRectangle);
        myMovableGroup.add(mEllipse);
        myMovableTool.add(mPencil);
        myMovableTool.add(mLine);
        myMovableTool.add(mRectangle);
        myMovableTool.add(mEllipse);
        myFrame.add(myMovableTool, BorderLayout.SOUTH); 
    }
    /**
     * Initializer method to setup the Help within the main menu.
     */
    void setupHelp() {
        final JMenu help = new JMenu("Help");
        help.setMnemonic(KeyEvent.VK_H);
        final JMenuItem about = new JMenuItem("About...", KeyEvent.VK_A);
        about.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(final ActionEvent theEvent) {
                final JFrame aboutFrame = new JFrame("DECEPTION");
                final ImageIcon tempIcon = new ImageIcon(TITLE_ICON);
                aboutFrame.setIconImage(tempIcon.getImage());
                aboutFrame.setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
                
                aboutFrame.add(new JLabel("<html>TCSS 305 PowerPaint, Autumn 2015<br>"
                                + " Some help would probably be nice huh? <br>"
                                + " Enjoy this pointless text, MUWHAHAHA</html>"),
                               BorderLayout.CENTER);
                aboutFrame.pack();
                aboutFrame.setVisible(true);
              
            }
        });
        help.add(about);
        myMenuBar.add(help);
        myFrame.setJMenuBar(myMenuBar);
        
    }
}
