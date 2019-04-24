import java.awt.*;

/**
* This class simplifies the use of the GridBagConstraint class
* */

public class GBC extends GridBagConstraints {
    /**
     * Constructs a GBC with a given gridx and gridy position and all other grid
     * bag constraint values set to the default
     */

    public GBC(int gridx, int gridy){
        this.gridx = gridx;
        this.gridy = gridy;

    }

    /**
     * Constructs a GBC with a given gridx, gridy, gridwidth and gridheight and all
     * other grid bag constraint values set to the default
     */

    public GBC(int gridx, int gridy, int gridwidth, int gridheight){
        this.gridx = gridx;
        this.gridy = gridy;
        this.gridwidth = gridwidth;
        this.gridheight = gridheight;

    }

    /**
     * set the anchor
     */

    public GBC setAnchor(int anchor){
        this.anchor = anchor;
        return this;
    }

    /**
     * Sets the fill direction.
     */

    public GBC setFill(int fill)
    {
        this.fill = fill;
        return this;
    }

/**
 * Sets the cell weights.
 * */

    public GBC setWeight(double weightx, double weighty)
    {
        this.weightx = weightx;
        this.weighty = weighty;
        return this;
    }

    /**
     * Sets the insets of this cell.
     */
    public GBC setInsets(int distance)
    {
        this.insets = new Insets(distance, distance, distance, distance);
        return this;
    }

    /**
     * Sets the insets of this cell.
     */

    public GBC setInsets(int top, int left, int bottom, int right)
    {
        this.insets = new Insets(top, left, bottom, right);
        return this;
    }

    /**
     * Sets the internal padding
     */

    public GBC setIpad(int ipadx, int ipady)
    {
        this.ipadx = ipadx;
        this.ipady = ipady;
        return this;
    }
}
