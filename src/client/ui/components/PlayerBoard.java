package client.ui.components;

import client.ColorTintFilter;
import client.GraphicsUtilities;
import org.jdesktop.animation.timing.Animator;
import org.jdesktop.animation.timing.interpolation.PropertySetter;
import utils.GBC;
import utils.Utils;

import javax.swing.*;
import java.awt.*;
import java.awt.geom.RoundRectangle2D;
import java.awt.image.BufferedImage;
import java.awt.image.BufferedImageOp;
import java.util.ArrayList;

import static java.awt.Color.WHITE;
import static java.awt.Font.PLAIN;
import static java.awt.GridBagConstraints.*;
import static javax.swing.BoxLayout.X_AXIS;
import static javax.swing.SwingConstants.LEFT;
import static javax.swing.SwingConstants.RIGHT;
import static utils.Utils.DEFAULT_FONT;
import static utils.Utils.getGaussianBlurFilter;

public class PlayerBoard extends BorderPanel{
    private Avatar avatar;
    private JLabel chipIndicator;
    private JLabel nickname;
    private JLabel position;
    private JLabel handIndicator;
    private JPanel avatarAndCardsContainer;
    private ArrayList<Card> cards;
    private boolean waitState;
    private Animator animator;
    private BufferedImage glow;
    private float alpha = 0.0f;
    private static int height;


    public PlayerBoard(String nickname, String position, boolean isCovered, int chips, String avatarDirectoryPath) {
        super();
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
        Graphics2D g2;
        if(height == 0)
            height =  Math.max(getHeight(), height);
        if (glow == null && waitState) {
            glow = new BufferedImage(getWidth(), height, BufferedImage.TYPE_INT_ARGB);
            glow = GraphicsUtilities.createCompatibleImage(getWidth(), height);
            g2 = glow.createGraphics();
            g2.dispose();

            BufferedImageOp filter = getGaussianBlurFilter(20, true);
            glow = filter.filter(glow, null);
            filter = getGaussianBlurFilter(20, false);
            glow = filter.filter(glow, null);

            filter = new ColorTintFilter(Color.LIGHT_GRAY, 1.0f);
            glow = filter.filter(glow, null);
            startAnimator();
        }

        if(waitState) {
            g2 = (Graphics2D) g.create();
            g2.setClip(new RoundRectangle2D.Double(strokeSize, strokeSize, getWidth() - shadowGap - strokeSize,
                    height - shadowGap - strokeSize, arcs.width, arcs.height));
            g2.setComposite(AlphaComposite.SrcOver.derive(getAlpha()));
            g2.drawImage(glow, 0, 0, getWidth(), height, null);
            g2.setComposite(AlphaComposite.SrcOver);
            g2.dispose();
        }else{
            if(animator != null && animator.isRunning()){
                animator.stop();
                glow = null;
            }
        }

    }

    private void startAnimator() {
        PropertySetter setter = new PropertySetter(this, "alpha", 0.0f, 1.0f);
        animator = new Animator(600, Animator.INFINITE,
                Animator.RepeatBehavior.REVERSE, setter);
        animator.start();
    }

    public float getAlpha() {
        return alpha;
    }

    public void setAlpha(float alpha) {
        this.alpha = alpha;
        repaint();
    }

    public void setWaitState(boolean waitState){
        this.waitState = waitState;
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

    @Override
    public Dimension getPreferredSize() {
        System.out.println("CIAONE" + height);
        return height == 0 ? super.getPreferredSize() : new Dimension(300, height);
    }

    @Override
    public Dimension getMaximumSize() {
        return height == 0? super.getMaximumSize() : new Dimension(300, height);
    }
}
