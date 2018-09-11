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

public class PlayerBoard extends BorderPanel {
    private Avatar avatar;
    private JLabel chipIndicator;
    private JLabel nickname;
    private JLabel position;
    private JLabel handIndicator;
    private JPanel avatarAndCardsContainer;
    private ArrayList<Card> cards;

    private final long animationDuration = 2000;
    private Timer tm;
    private long animStartTime;
    private Color colorStart = Color.WHITE;
    private Color colorEnd = new Color(41, 121, 255);
    private Color currentColor;

    public PlayerBoard(String nickname, String position, boolean isCovered, int chips, String avatarDirectoryPath) {
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
        //setBorder(new RoundBorder(40));
    }

    private void setComponentProperties() {
        setLayout(new GridBagLayout());
        //setBorder(new RoundBorder(25));
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
        add(avatarAndCardsContainer, new GBC(0, 0, 1, 0.70, 2, 1, WEST, NONE, new Insets(15, 20, 0, 0)));
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
        cards.forEach(card -> avatarAndCardsContainer.add(card));
    }

    private void attachNickname() {
        add(nickname, new GBC(0, 1, 1, 0.15, 1, 1, WEST, NONE, new Insets(10, 20, 0, 0)));
    }

    private void attachPosition() {
        add(position, new GBC(1, 1, 1, 0.15, 1, 1, EAST, NONE, new Insets(10, 0, 0, 20)));
    }

    private void attachHand() {
        add(handIndicator, new GBC(1, 2, 1, 0.15, 1, 1, EAST, NONE, new Insets(0, 0, 10, 20)));
    }

    private void attachChips() {
        add(chipIndicator, new GBC(0, 2, 1, 0.15, 1, 1, WEST, NONE, new Insets(0, 20, 10, 0)));
    }

    @Override
    protected void drawBackground(Graphics2D g2D) {
        g2D.setColor(new Color(0, 117, 178));
        g2D.fillRoundRect(6, 6, getWidth() - 10, getHeight() - 10,
                30, 30);
    }

    @Override
    protected void drawBorder(Graphics2D g2D, Color color) {
        g2D.setStroke(new BasicStroke(6f));
        g2D.setColor(color);
        g2D.drawRoundRect(6, 6, getWidth() - 10, getHeight() - 10,
                40, 40);
    }

    public void activateColorTransition() {
        tm = new Timer(50, e -> {
            long currentTime = System.nanoTime() / 1000000;
            long totalTime = currentTime - animStartTime;
            if (totalTime > animationDuration) {
                animStartTime = currentTime;
            }
            float fraction = (float) totalTime / animationDuration;
            fraction = Math.min(1.0f, fraction);
            int red = (int) (fraction * colorEnd.getRed() +
                    (1 - fraction) * colorStart.getRed());
            int green = (int) (fraction * colorEnd.getGreen() +
                    (1 - fraction) * colorStart.getGreen());
            int blue = (int) (fraction * colorEnd.getBlue() +
                    (1 - fraction) * colorStart.getBlue());
            currentColor = new Color(red, green, blue);
            borderColor = currentColor;
            repaint();
        });
        tm.setInitialDelay(1000);
        animStartTime = 1000 + System.nanoTime() / 1000000;
        animStartTime = 1000 + System.nanoTime() / 1000000;
        tm.start();
    }

    public void disableColorTransition() {
        if (tm != null && tm.isRunning()) {
            tm.stop();
            borderColor = Color.WHITE;
            repaint();
        }

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
