package control;

import java.awt.Color;
import java.awt.Component;
import java.awt.Graphics;

import javax.swing.Icon;

/**
 * @author ao1013
 * @version 11/7/15 TCSS 305
 */
public class ColorSwatch implements Icon {
    /**
     * The square size of the swatch.
     */
    private static final int SWATCH_SIZE = 10;
    /**
     * The default Color.
     */
    private Color mySwatchColor = Color.black;
    /**
     * The default Color of the border.
     */
    private Color myBorderColor = Color.black;
  

  /**
   * Creates a standard 14 x 14 swatch with a black border and white background.
   */
    public ColorSwatch() {
    }
  /**
   * Sets the color of this swatch's border.
   * @param theColor changes the color of the border.
   */
    public void setBorderColor(final Color theColor) {
        myBorderColor = theColor;
    }
  /**
   * Sets the color that this swatch represents.
   * @param theColor 
   */
    public void setColor(final Color theColor) {
        mySwatchColor = theColor;
    }

   /**
  * returns the swatch size.
  * @return returns the size of the icon.
  */
    public int getIconWidth() {
        return SWATCH_SIZE;
    }

  /**
   * Returns the height of this Icon.
   * @return returns the size of the icon.
   */
    public int getIconHeight() {
        return SWATCH_SIZE;
    }

  /**
   * Paints this Icon into the provided graphics context.
   * @param theComp the component.
   * @param theGraphics the graphics to draw the icon.
   * @param theWidth the x dimension.
   * @param theHeight the y dimension.
   */
    public void paintIcon(final Component theComp, final Graphics theGraphics,
                          final int theWidth, final int theHeight) {
    
        final Color oldColor = theGraphics.getColor();
        if (mySwatchColor != null) {
            theGraphics.setColor(mySwatchColor);
            theGraphics.fillRect(theWidth, theHeight, SWATCH_SIZE, SWATCH_SIZE);
        } 
        theGraphics.setColor(myBorderColor);
        theGraphics.drawRect(theWidth, theHeight, SWATCH_SIZE, SWATCH_SIZE);
        
        theGraphics.setColor(oldColor);
    }

}
