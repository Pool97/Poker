package graphics;

import java.awt.*;

/**
 * Classe che gestisce i constraints per il GridBagLayout
 */

public class GBC extends GridBagConstraints {

    public GBC(int gridx, int gridy, int gridwidth, int gridheight, int weightx, int weighty){
        this.gridx = gridx;
        this.gridy = gridy;
        this.gridwidth = gridwidth;
        this.gridheight = gridheight;
        this.weightx = weightx;
        this.weighty = weighty;
    }

    public GBC(int gridx, int gridy){
        this.gridx = gridx;
        this.gridy = gridy;
    }

    public void setFill(int fill){
        this.fill = fill;
    }

    public void setAnchor(int anchor){
        this.anchor = anchor;
    }
}
