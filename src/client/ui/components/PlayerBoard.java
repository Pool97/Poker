package client.ui.components;

import utils.GBC;
import utils.Utils;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;

import static java.awt.Color.WHITE;
import static java.awt.Font.PLAIN;
import static java.awt.GridBagConstraints.*;
import static javax.swing.BoxLayout.X_AXIS;
import static javax.swing.SwingConstants.LEFT;
import static javax.swing.SwingConstants.RIGHT;
import static utils.Utils.DEFAULT_FONT;
import static utils.Utils.getHighQualityRenderingHints;

public class PlayerBoard extends JPanel{
    private Avatar avatar;
    private JLabel chipIndicator;
    private JLabel nickname;
    private JLabel position;
    private JLabel handIndicator;
    private JPanel avatarAndCardsContainer;
    private ArrayList<Card> cards;

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

    public PlayerBoard(String nickname, String position, boolean isCovered, int chips, String avatarDirectoryPath) {
        setVisible(false);
        cards = new ArrayList<>();
        setComponentProperties();

        createAvatar(avatarDirectoryPath);

        createNickname(nickname);
        setComponentProperties(this.nickname);

        createPosition(position);
        setComponentProperties(this.position);

        createHand();
        setComponentProperties(this.handIndicator);

        createChips(chips);
        setChipsProperties();

        createAvatarAndCardsContainer();
        setAvatarAndCardsContainerProperties();
        createCards(isCovered);
        attachComponents();
        setOpaque(false);
        setVisible(true);
    }

    private void setComponentProperties() {
        setLayout(new GridBagLayout());
    }

    private void createAvatar(String avatarDirectoryPath) {
        avatar = new Avatar(avatarDirectoryPath);
    }

    private void createNickname(String nickname) {
        this.nickname = new JLabel(nickname, LEFT);
    }

    private void createPosition(String position) {
        this.position = new JLabel(position, RIGHT);
    }

    private void createHand() {
        this.handIndicator = new JLabel(Utils.EMPTY, RIGHT);
    }

    private void createCards(boolean isCovered) {
        cards.add(Card.createCard(isCovered));
        cards.add(Card.createCard(isCovered));
    }

    private void setComponentProperties(JComponent component) {
        component.setFont(new Font("helvetica", Font.PLAIN, 20));
        component.setForeground(WHITE);
    }

    private void createChips(int chips) {
        this.chipIndicator = new JLabel(Integer.toString(chips), LEFT);
    }

    private void setChipsProperties() {
        chipIndicator.setFont(new Font(DEFAULT_FONT, PLAIN, 20));
        chipIndicator.setForeground(WHITE);
    }

    private void createAvatarAndCardsContainer() {
        avatarAndCardsContainer = new JPanel();
    }

    private void setAvatarAndCardsContainerProperties() {
        avatarAndCardsContainer.setLayout(new BoxLayout(avatarAndCardsContainer, X_AXIS));
        avatarAndCardsContainer.setOpaque(false);
    }

    private void attachComponents() {
        attachAvatar();
        attachCards();
        add(avatarAndCardsContainer, new GBC(0, 0, 1, 0.70, 2, 1, WEST, NONE, new Insets(10, 15, 0, 0)));
        attachNickname();
        attachPosition();
        attachHand();
        attachChips();
    }

    private void attachAvatar() {
        avatarAndCardsContainer.add(avatar);
    }

    private void attachCards() {
        avatarAndCardsContainer.add(Box.createRigidArea(new Dimension(20, 0)));
        avatarAndCardsContainer.add(cards.get(0));
        avatarAndCardsContainer.add(Box.createHorizontalStrut(5));
        avatarAndCardsContainer.add(cards.get(1));
    }

    private void attachNickname() {
        add(nickname, new GBC(0, 1, 1, 0.15, 1, 1, WEST, NONE, new Insets(10, 20, 0, 0)));
    }

    private void attachPosition() {
        add(position, new GBC(1, 1, 1, 0.15, 1, 1, EAST, NONE, new Insets(5, 0, 0, 20)));
    }

    private void attachHand() {
        add(handIndicator, new GBC(1, 2, 1, 0.15, 1, 1, EAST, NONE, new Insets(0, 0, 15, 20)));
    }

    private void attachChips() {
        add(chipIndicator, new GBC(0, 2, 1, 0.15, 1, 1, WEST, NONE, new Insets(0, 20, 15, 0)));
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        Color shadowColorA = new Color(shadowColor.getRed(),
                shadowColor.getGreen(), shadowColor.getBlue(), shadowAlpha);
        Graphics2D graphics = (Graphics2D) g;
        graphics.setRenderingHints(getHighQualityRenderingHints());

        //Draws shadow borders if any.
        if (shady) {
            graphics.setColor(shadowColorA);
            graphics.fillRoundRect(
                    shadowOffset,// X position
                    shadowOffset,// Y position
                    getWidth() - strokeSize - shadowOffset, // width
                    getHeight() - strokeSize - shadowOffset, // height
                    arcs.width, arcs.height);// arc Dimension
        } else {
            shadowGap = 1;
        }
        Paint paint = graphics.getPaint();
        LinearGradientPaint gradient = new LinearGradientPaint(strokeSize, strokeSize, getWidth()- shadowGap - strokeSize, getHeight()-shadowGap-strokeSize,
                new float[]{0.0f, 0.5f, 1f}, new Color[]{new Color(0, 130, 178), new Color(0, 115, 178), new Color(0, 100, 178)});
        graphics.setPaint(gradient);
        //Draws the rounded opaque panel with borders.
        graphics.fillRoundRect(strokeSize, strokeSize, getWidth() - shadowGap - strokeSize,
                getHeight() - shadowGap - strokeSize, arcs.width, arcs.height);
        graphics.setPaint(paint);
        graphics.setColor(WHITE);
        graphics.setStroke(new BasicStroke(strokeSize));
        graphics.drawRoundRect(strokeSize, strokeSize, getWidth() - shadowGap - strokeSize,
                getHeight() - shadowGap - strokeSize, arcs.width, arcs.height);
    }

    public void setChipIndicator(int chips) {
        chipIndicator.setText(Integer.toString(chips));
    }

    public String getNickname() {
        return nickname.getText();
    }

    public void setNickname(String nickname) {
        this.nickname.setText(nickname);
    }

    public void setPosition(String position) {
        this.position.setText(position);
    }

    public void setHandIndicator(String hand) {
        handIndicator.setText(hand);
    }

    public void setNicknameColor(Color color) {
        nickname.setForeground(color);
    }


    public void assignNewCards(String frontImage1, String frontImage2) {
        cards.get(0).setFrontImageDirectoryPath(frontImage1);
        cards.get(1).setFrontImageDirectoryPath(frontImage2);
        cards.forEach(Card::loadImage);
        cards.forEach(Card::repaint);
    }

    public void coverCards(boolean cover) {
        cards.forEach(card -> card.setCovered(cover));
        cards.forEach(Card::getDirectoryPathImageToLoad);
        cards.forEach(Card::loadImage);
        cards.forEach(Card::repaint);
    }
}
