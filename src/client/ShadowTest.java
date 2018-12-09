
package client;

import utils.Utils;

import javax.swing.*;
import java.awt.*;

public class ShadowTest {

    private JFrame frame;

    public ShadowTest() {
        initComponents();
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                new ShadowTest();
            }
        });
    }

    private void initComponents() {
        frame = new JFrame();
        frame.setTitle("Example");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);//app exited when frame closes
        frame.setResizable(false);
        frame.setLayout(new GridBagLayout());
        GridBagConstraints gc = new GridBagConstraints();
        gc.fill = GridBagConstraints.HORIZONTAL;
        gc.insets = new Insets(10, 10, 10, 10);
        frame.add(new RoundedPanel(), gc);
        //pack frame (size components to preferred size)
        frame.setSize(new Dimension(600, 600));
        //frame.pack();
        frame.setVisible(true);//make frame visible

    }
}

class RoundedPanel extends JPanel {

    /**
     * Stroke size. it is recommended to set it to 1 for better view
     */
    protected int strokeSize = 3;
    /**
     * Color of shadow
     */
    protected Color shadowColor = new Color(0, 40, 5);
    /**
     * Sets if it drops shadow
     */
    protected boolean shady = true;
    /**
     * Sets if it has an High Quality view
     */
    protected boolean highQuality = true;
    /**
     * Double values for Horizontal and Vertical radius of corner arcs
     */
    protected Dimension arcs = new Dimension(40, 40);
    //protected Dimension arcs = new Dimension(20, 20);//creates curved borders and panel
    /**
     * Distance between shadow border and opaque panel border
     */
    protected int shadowGap = 8;
    /**
     * The offset of shadow.
     */
    protected int shadowOffset = 7;
    /**
     * The transparency value of shadow. ( 0 - 255)
     */
    protected int shadowAlpha = 180;
    int width = 200, height =150;

    public RoundedPanel() {
        super();
        setOpaque(false);
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        Color shadowColorA = new Color(shadowColor.getRed(),
                shadowColor.getGreen(), shadowColor.getBlue(), shadowAlpha);
        Graphics2D graphics = (Graphics2D) g;

        //Sets antialiasing if HQ.
        if (highQuality) {
            graphics.setRenderingHints(Utils.getHighQualityRenderingHints());
        }

        //Draws shadow borders if any.
        if (shady) {
            graphics.setColor(shadowColorA);
            graphics.fillRoundRect(
                    shadowOffset,// X position
                    shadowOffset,// Y position
                    width - strokeSize - shadowOffset, // width
                    height - strokeSize - shadowOffset, // height
                    arcs.width, arcs.height);// arc Dimension
        } else {
            shadowGap = 1;
        }

        //Draws the rounded opaque panel with borders.
        graphics.setColor(new Color(0, 80, 5));
        graphics.fillRoundRect(strokeSize, strokeSize, width - shadowGap - strokeSize,
                height - shadowGap - strokeSize, arcs.width, arcs.height);
        graphics.setColor(new Color(0, 40, 5));
        graphics.setStroke(new BasicStroke(strokeSize));
        graphics.drawRoundRect(strokeSize, strokeSize, width - shadowGap - strokeSize,
                height - shadowGap - strokeSize, arcs.width, arcs.height);

        //Sets strokes to default, is better.
    }

    @Override
    public Dimension getPreferredSize() {
        return new Dimension(width, height);
    }
}