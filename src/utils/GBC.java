package utils;

import java.awt.*;

/**
 * Classe che gestisce i constraints per il GridBagLayout.
 */

public class GBC extends GridBagConstraints {

    public GBC(int gridx, int gridy, double weightx, double weighty, int gridwidth, int gridheight, int anchor, int fill, Insets insets) {
        this.gridx = gridx;
        this.gridy = gridy;
        this.gridwidth = gridwidth;
        this.gridheight = gridheight;
        this.weightx = weightx;
        this.weighty = weighty;
        this.anchor = anchor;
        this.fill = fill;
        this.insets = insets;
    }

    public GBC(int gridx, int gridy){
        this.gridx = gridx;
        this.gridy = gridy;
    }
}
