package client.ui.components;

import client.ui.effects.PulseAnimator;
import utils.GBC;
import utils.Utils;

import javax.swing.*;
import java.awt.*;
import java.awt.geom.RoundRectangle2D;
import java.util.ArrayList;

import static java.awt.Color.WHITE;
import static java.awt.Font.PLAIN;
import static java.awt.GridBagConstraints.*;
import static javax.swing.BoxLayout.X_AXIS;
import static javax.swing.SwingConstants.LEFT;
import static javax.swing.SwingConstants.RIGHT;
import static utils.Utils.DEFAULT_FONT;

public class PlayerBoard extends BorderPanel{
    private Avatar avatar;
    private JLabel chipIndicator;
    private JLabel nickname;
    private JLabel position;
    private JLabel handIndicator;
    private JPanel avatarAndHandContainer;
    private ArrayList<Card> cards;
    private PulseAnimator pulseAnimator;
    private float alpha = 0.0f;
    public static int height;
    private boolean folded;


    public PlayerBoard(String nickname, boolean isCovered, int chips, String avatarDirectoryPath) {
        cards = new ArrayList<>();
        pulseAnimator = new PulseAnimator(this);

        setComponentProperties();

        createAvatar(avatarDirectoryPath);

        createNickname(nickname);
        setComponentProperties(this.nickname);

        createPosition("");
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
        component.setFont(new Font("helvetica", Font.PLAIN, 18));
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
        avatarAndHandContainer = new JPanel();
    }

    private void setAvatarAndCardsContainerProperties() {
        avatarAndHandContainer.setLayout(new BoxLayout(avatarAndHandContainer, X_AXIS));
        avatarAndHandContainer.setOpaque(false);
    }

    private void attachComponents() {
        attachAvatar();
        attachCards();
        add(avatarAndHandContainer, new GBC(0, 0, 1, 0.70, 2, 1, WEST, NONE, new Insets(10, 15, 0, 0)));
        attachNickname();
        attachPosition();
        attachHand();
        attachChips();
    }

    private void attachAvatar() {
        avatarAndHandContainer.add(avatar);
    }

    private void attachCards() {
        avatarAndHandContainer.add(Box.createRigidArea(new Dimension(20, 0)));
        avatarAndHandContainer.add(cards.get(0));
        avatarAndHandContainer.add(Box.createHorizontalStrut(5));
        avatarAndHandContainer.add(cards.get(1));
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

        if(height == 0)
            height =  Math.max(getHeight(), height);

        if (!pulseAnimator.isRunning() && pulseAnimator.isAnimationEnabled()) {
            pulseAnimator.createPulseEffect();
            pulseAnimator.startPulseEffect();
        }

        Shape clipShape = new RoundRectangle2D.Double(strokeSize, strokeSize, getWidth() - shadowGap - strokeSize,
                height - shadowGap - strokeSize, arcs.width, arcs.height);


        if(pulseAnimator.isAnimationEnabled())
            pulseAnimator.drawPulseEffect(g.create(), clipShape, getAlpha());
        else
            pulseAnimator.stop();

        if(folded){
            Graphics2D g2D = (Graphics2D)g.create();
            g2D.setColor(Color.BLACK);
            g2D.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, .5f));
            g2D.fill(clipShape);
        }
    }

    public float getAlpha() {
        return alpha;
    }

    public void setAlpha(float alpha) {
        this.alpha = alpha;
        repaint();
    }

    public void setAnimationEnabled(boolean animationEnabled){
        pulseAnimator.setAnimationEnabled(animationEnabled);
        repaint();
    }

    public void setChipIndicator(int chips) {
        chipIndicator.setText(chips + "$");
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

    public void changeCard(String frontImage, int index) {
        cards.get(index).setFrontImageDirectoryPath(frontImage);
    }

    public void coverCards(boolean cover) {
        cards.forEach(card -> card.setCovered(cover));
    }

    public void setFolded(boolean folded){
        this.folded = folded;
        repaint();
    }

    @Override
    public int getHeight() {
        return height == 0 ? super.getHeight() : height;
    }

    @Override
    public Dimension getPreferredSize() {
        return height == 0 ? super.getPreferredSize() : new Dimension(300, height);
    }

    @Override
    public Dimension getMaximumSize() {
        return height == 0? super.getMaximumSize() : new Dimension(300, height);
    }

    public Avatar getAvatar() { return avatar; }

    public String getChips(){
        return chipIndicator.getText();
    }
}
