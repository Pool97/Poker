package client.ui.components;

import client.ui.table.PokerTable;
import client.ui.userboard.ActionBoard;
import layout.TableLayout;
import layout.TableLayoutConstraints;
import utils.GBC;
import utils.Utils;

import javax.swing.*;
import java.awt.*;

import static java.awt.GridBagConstraints.*;
import static layout.TableLayoutConstants.CENTER;
import static layout.TableLayoutConstants.*;

public class GameBoard extends JPanel {
    public final static String BACKGROUND_IMAGE = "board.png";
    private JPanel middlePart;
    private JPanel bottomPart;

    public GameBoard() {
        setComponentProperties();

        createMiddlePart();
        setMiddlePartProperties();
        attachMiddlePart();

        createBottomPart();
        setBottomPartProperties();
        attachBottomPart();
        setOpaque(false);
    }

    private void createMiddlePart() {
        middlePart = new JPanel();
    }

    private void setComponentProperties() {
        double[][] layoutSize = {{TableLayout.FILL}, {0.20, 0.30, 0.20, 0.30}};
        setLayout(new TableLayout(layoutSize));
        setBackground(Color.DARK_GRAY);
    }

    private void setMiddlePartProperties() {
        double[][] layoutSize = {{0.3, 0.4, 0.3}, {TableLayout.FILL}};
        middlePart.setLayout(new TableLayout(layoutSize));
        middlePart.setBackground(Utils.TRANSPARENT);
    }

    private void createBottomPart() {
        bottomPart = new JPanel();
    }

    private void setBottomPartProperties() {
        bottomPart.setLayout(new GridBagLayout());
        bottomPart.setBackground(Utils.TRANSPARENT);
        bottomPart.setOpaque(false);
    }

    private void attachMiddlePart() {
        add(middlePart, new TableLayoutConstraints(0, 1, 0, 1, FULL, CENTER));
    }

    private void attachBottomPart() {
        add(bottomPart, new TableLayoutConstraints(0, 3, 0, 3, FULL, BOTTOM));
    }

    public void attach(PokerTable pokerTable) {
        add(pokerTable.getTopSide(), new TableLayoutConstraints(0, 0, 0, 0, CENTER, CENTER));
        add(pokerTable.getBottomSide(), new TableLayoutConstraints(0, 2, 0, 2, CENTER, BOTTOM));
        middlePart.add(pokerTable.getCommunityCardsBoard(), new TableLayoutConstraints(1, 0, 1, 0, FULL, TOP));
        middlePart.add(pokerTable.getLeftSide(), new TableLayoutConstraints(0, 0, 0, 0, CENTER, CENTER));
        middlePart.add(pokerTable.getRightSide(), new TableLayoutConstraints(2, 0, 2, 0, CENTER, CENTER));
    }

    public void attach(ActionBoard actionBoard, MatchBoard matchBoard) {
        bottomPart.add(Box.createGlue(), new GBC(0, 0, 25, 1, 1, 1, NORTHEAST, BOTH,
                new Insets(0, 10, 30, 10)));

        bottomPart.add(actionBoard, new GBC(1, 0, 50, 1, 1, 1, GBC.CENTER,
                VERTICAL, new Insets(0, 10, 30, 10)));

        bottomPart.add(matchBoard, new GBC(3, 0, 10, 1, 1, 1, NORTHEAST,
                VERTICAL, new Insets(0, 10, 30, 10)));
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D graphics = (Graphics2D)g;
        graphics.setRenderingHints(Utils.getHighQualityRenderingHints());
        graphics.setRenderingHint(RenderingHints.KEY_DITHERING, RenderingHints.VALUE_DITHER_ENABLE);

        Paint oldPaint = graphics.getPaint();

        LinearGradientPaint linear = new LinearGradientPaint(0, 0, getWidth(), getHeight(),
                new float[]{0.0f, 0.5f, 1f}, new Color[]{new Color(36, 36, 36), new Color(64, 64, 64), new Color(36,36,36)});
        graphics.setPaint(linear);
        graphics.fillRect(0,0, getWidth(), getHeight());
        graphics.setPaint(oldPaint);
        graphics.setColor(new Color(64,32,0));
        graphics.fillOval(3, 3, getWidth()- 6, getHeight() - 6);
        graphics.setColor(new Color(139,69,19));
        graphics.fillOval(5, 5, getWidth()- 10, getHeight() - 10);
        graphics.setColor(new Color(64,32,0));
        graphics.fillOval(38, 38, getWidth()- 76, getHeight() - 76);
        LinearGradientPaint gradient = new LinearGradientPaint(0, 0, getWidth()- 80, getHeight()-80,
                new float[]{0.0f, 0.5f, 1f}, new Color[]{new Color(0, 60, 20), new Color(0, 100, 20), new Color(0, 60, 20)}
                , MultipleGradientPaint.CycleMethod.REFLECT);

        graphics.setPaint(gradient);
        graphics.fillOval(40, 40, getWidth() - 80, getHeight() - 80);
        //graphics.fillRoundRect(50,50, getWidth() - 50, getHeight() - 50, 200, 200);

        //g.drawImage(Utils.loadImage(BACKGROUND_IMAGE, getSize()), 0, 0, getWidth(), getHeight(), null);
    }
}
