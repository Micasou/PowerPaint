/**
 * 
 */

package control;

import java.awt.AlphaComposite;
import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.geom.*;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.RenderingHints;
import java.awt.Shape;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.util.ArrayList;
import java.util.Iterator;

import javax.swing.JComponent;
import javax.swing.JMenuItem;

/**
 * @author ao1013
 * @version 11/7/15 TCSS 305
 */
public class Canvas extends JComponent implements MouseMotionListener, MouseListener {
    /**
     * Auto Generated Serial Version.
     */
    private static final long serialVersionUID = -4358260754220693631L;
    /** The default size for this JPanel. */
    private static final Dimension DEFAULT_SIZE = new Dimension(400, 200);
    /**
     * Integer to control the grid size.
     */
    private static final int MY_GRID_SIZE = 10;
    /**
     * an array of drawing actions to determine what object needs to be drawn.
     */
    private final DrawAction[] myDrawingActions = new DrawAction[] {new DrawAction() {
        public Shape draw(final Graphics2D theDrawer) {
            return drawPencil(theDrawer);
        }
    }, new DrawAction() {
        public Shape draw(final Graphics2D theDrawer) {
            return drawLine(theDrawer);
        }
    }, new DrawAction() {
        public Shape draw(final Graphics2D theDrawer) {
            return drawCircle(theDrawer);
        }
    }, new DrawAction() {
        public Shape draw(final Graphics2D theDrawer) {
            return drawSquare(theDrawer);
        }
    }};
    /**
     * Determines the currently selected tool.
     */
    private int myCurrentToolIndex;
    /**
     * Graphics2D object to draw.
     */
    private Graphics2D myGraphics;
    /**
     * Begin point for mouse action.
     */
    private Point myStartPoint;
    /**
     * End point for mouse action.
     */
    private Point myEndPoint;
    /**
     * Color object to determine the color to be painted with.
     */
    private Color myColor;
    /**
     * Int to hold the pen size that is being drawn with.
     */
    private int myPenSize = 1;
    /**
     * Determines if the grid should be drawn.
     */
    private boolean myGridOn;
    /**
     * Keeps track of where we are in the linked list of graphics.
     */
    private int myCurrentImageIndex;
    /**
     * Array list to store all the shapes.
     */
    private ArrayList<Shape> myShapes = new ArrayList<Shape>();
    /**
     * Array list to store all the colors.
     */
    private ArrayList<Color> myShapeStroke = new ArrayList<Color>();
    /**
     * Array list to store all the penSizes.
     */
    private ArrayList<Integer> myPenSizes = new ArrayList<Integer>();

    /**
     * Constructor for the canvas class, sets up all elements to have the
     * program run smoothly.
     */
    public Canvas() {
        super();
        addMouseMotionListener(this);
        addMouseListener(this);
        setDoubleBuffered(false);
        myStartPoint = null;
        myColor = Color.BLACK;
        myEndPoint = new Point();
        this.setPreferredSize(DEFAULT_SIZE);
        this.setBackground(Color.WHITE);
    }

    @Override
    public void paintComponent(final Graphics theGraphics) {

        super.paintComponent(theGraphics);

        myGraphics = (Graphics2D) theGraphics;
        myGraphics.setRenderingHint(RenderingHints.KEY_ANTIALIASING,
                                    RenderingHints.VALUE_ANTIALIAS_ON);

        final Iterator<Color> strokeCounter = myShapeStroke.iterator();
        final Iterator<Integer> sizeCounter = myPenSizes.iterator();
        myGraphics.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, 1.0f));

        for (int i = 0; i < myCurrentImageIndex; i++) {
            final Shape s = myShapes.get(i);
            myGraphics.setPaint(strokeCounter.next());
            myGraphics.setStroke(new BasicStroke(sizeCounter.next()));
            myGraphics.draw(s);
        }
        if (myStartPoint != null && myEndPoint != null) {
            // myGraphics.setComposite(AlphaComposite.getInstance(
            // AlphaComposite.SRC_OVER, 0.40f));
            myGraphics.setPaint(myColor);
            myGraphics.setStroke(new BasicStroke(myPenSize));
            final Shape shape = myDrawingActions[myCurrentToolIndex].draw(myGraphics);
            myGraphics.draw(shape);
        }

        if (myGridOn) {
            this.drawGrid();
        }
    }

    @Override
    public void mouseClicked(final MouseEvent theMouseEvent) {
    }

    @Override
    public void mouseEntered(final MouseEvent theMouseEvent) {
        final Cursor changeCursor = new Cursor(Cursor.CROSSHAIR_CURSOR);
        this.setCursor(changeCursor);
    }

    @Override
    public void mousePressed(final MouseEvent theMouseEvent) {
        if (myPenSize != 0) {
            myStartPoint = theMouseEvent.getPoint();
            myEndPoint = myStartPoint;
        }
        repaint();

    }

    @Override
    public void mouseReleased(final MouseEvent theMouseEvent) {
        if (myPenSize != 0) {
            myEndPoint = theMouseEvent.getPoint();

            for (int i = myShapes.size() - 1; myCurrentImageIndex < i; i--) {
                myShapes.remove(i);
                myPenSizes.remove(i);
                myShapeStroke.remove(i);
            }
            this.storeItem();
        }
        repaint();
    }

    /**
     * Function to store the shape after it's been drawn.
     */
    private void storeItem() {
        final Shape aShape = myDrawingActions[myCurrentToolIndex].draw(myGraphics);

        myCurrentImageIndex++;
        myShapes.add(aShape);
        myPenSizes.add(myPenSize);
        myShapeStroke.add(myColor);
        myStartPoint = null;
        myEndPoint = null;
    }

    @Override
    public void mouseDragged(final MouseEvent theMouseEvent) {
        myEndPoint = theMouseEvent.getPoint();
        repaint();
    }

    @Override
    public void mouseMoved(final MouseEvent theMouseEvent) {
    }

    @Override
    public void mouseExited(final MouseEvent theEvent) {
    }

    /**
     * Function that will set the current tool being used.
     * 
     * @param theSelection Variable to change the current tool index.
     */
    public void setTools(final int theSelection) {
        myCurrentToolIndex = theSelection;
    }

    /**
     * Over loaded function to set color and the pen size.
     * 
     * @param theColor changes the color.
     * @param thePenSize changes the pen size.
     */
    public void setTools(final Color theColor, final int thePenSize) {
        myPenSize = thePenSize;
        myColor = theColor;

    }

    /**
     * determines if the canvas is emnpty.
     * 
     * @return Allows the GUI to talk between parts.
     */
    public boolean isEmpty() {
        return myCurrentImageIndex == 0;
    }

    /**
     * Controls the grid control.
     * 
     * @param theOnorOff The button state is passed to determine the grid.
     */
    void switchGrid(final boolean theOnorOff) {
        myGridOn = theOnorOff;
    }

    /**
     * A function to make the grid appear.
     */
    void drawGrid() {
        myGraphics.setPaint(Color.GRAY);
        myGraphics.setStroke(new BasicStroke(1));
        for (int i = MY_GRID_SIZE; i < this.getBounds().getHeight(); i += MY_GRID_SIZE) {
            myGraphics.draw(new Line2D.Double(i, 0, i, this.getHeight()));
        }
        for (int i = MY_GRID_SIZE; i < this.getBounds().getWidth(); i += MY_GRID_SIZE) {
            myGraphics.draw(new Line2D.Double(0, i, this.getWidth(), i));
        }
    }

    /**
     * A function to draw with a pencil tool.
     * 
     * @param theDrawer The object that will draw to the screen.
     * @return A shape is returned to be stored in an array list.
     */
    private Shape drawPencil(final Graphics2D theDrawer) {
        final Shape temp = new Line2D.Double(myEndPoint.getX(), myEndPoint.getY(),
                                             myEndPoint.getX() + 1, myEndPoint.getY() + 1);
        myCurrentImageIndex++;
        theDrawer.draw(temp);
        myPenSizes.add(myPenSize);
        myShapeStroke.add(myColor);
        myShapes.add(temp);
        return temp;
    }

    /**
     * A function to draw with a square tool.
     * 
     * @param theDrawer The object that will draw to the screen.
     * @return A shape is returned to be stored in an array list.
     */
    private Shape drawSquare(final Graphics2D theDrawer) {
        int startX = (int) myStartPoint.getX();
        int startY = (int) myStartPoint.getY();
        int width = (int) (myEndPoint.getX() - myStartPoint.getX());
        if (width < 0) {
            width *= -1;
        }
        int height = (int) (myEndPoint.getY() - myStartPoint.getY());
        if (height < 0) {
            height *= -1;
        }
        if (myEndPoint.getX() < myStartPoint.getX()) {
            startX = (int) (myStartPoint.getX() - width);
        }
        if (myEndPoint.getY() < myStartPoint.getY()) {
            startY = (int) (myStartPoint.getY() - height);
        }
        theDrawer.drawRect(startX, startY, width, height);
        return new Rectangle2D.Float(startX, startY, width, height);
    }

    /**
     * A function to draw with a circle tool.
     * 
     * @param theDrawer The object that will draw to the screen.
     * @return A shape is returned to be stored in an array list.
     */
    private Shape drawCircle(final Graphics2D theDrawer) {
        int startX = (int) myStartPoint.getX();
        int startY = (int) myStartPoint.getY();
        int width = (int) (myEndPoint.getX() - myStartPoint.getX());
        if (width < 0) {
            width *= -1;
        }
        int height = (int) (myEndPoint.getY() - myStartPoint.getY());
        if (height < 0) {
            height *= -1;
        }
        if (myEndPoint.getX() < myStartPoint.getX()) {
            startX = (int) (myStartPoint.getX() - width);
        }
        if (myEndPoint.getY() < myStartPoint.getY()) {
            startY = (int) (myStartPoint.getY() - height);
        }

        theDrawer.drawOval(startX, startY, width, height);
        return new Ellipse2D.Float(startX, startY, width, height);
    }

    /**
     * A function to draw with a line tool.
     * 
     * @param theDrawer The object that will draw to the screen.
     * @return A shape is returned to be stored in an array list.
     */
    private Shape drawLine(final Graphics2D theDrawer) {
        theDrawer.drawLine((int) myStartPoint.getX(), (int) myStartPoint.getY(),
                           (int) myEndPoint.getX(), (int) myEndPoint.getY());
        return new Line2D.Double(myStartPoint.getX(), myStartPoint.getY(), myEndPoint.getX(),
                                 myEndPoint.getY());
    }

    /**
     * Undoes the last action.
     */
    public void undoScreen() {
        myCurrentImageIndex--;
        repaint();
    }

    /**
     * Re-does the last action.
     * 
     * @param theRedoButton changes the state of the redo button.
     */
    public void redoScreen(final JMenuItem theRedoButton) {
        myCurrentImageIndex++;
        if (myCurrentImageIndex == myShapes.size()) {
            theRedoButton.setEnabled(false);
        }
        repaint();
    }

    /**
     * Clears the screen.
     */
    public void clearScreen() {
        myCurrentImageIndex = 0;
        myShapes.clear();
        myPenSizes.clear();
        myShapeStroke.clear();
        repaint();
    }

    /**
     * Interface to easily select the tools being drawn.
     * 
     * @author Shikari
     * @version 11/19/15 Autumn TCSS305
     */
    interface DrawAction {
        /**
         * Template draw function, to be implemented within inherited class.
         * 
         * @param theDrawer graphics object to draw with.
         * @return A shape is returned to be stored in an array list.
         */
        Shape draw(final Graphics2D theDrawer);
    }

}
