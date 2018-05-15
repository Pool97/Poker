package client.components;

import utils.GBC;

import javax.swing.*;
import java.awt.*;

import static java.awt.Color.WHITE;
import static java.awt.Font.PLAIN;
import static java.awt.GridBagConstraints.NONE;
import static java.awt.GridBagConstraints.WEST;
import static javax.swing.BoxLayout.X_AXIS;
import static javax.swing.SwingConstants.LEFT;
import static utils.Utils.*;


public class PlayerBoard extends JPanel {
    private Avatar avatar;
    private JLabel chips;
    private JLabel nickname;
    private JPanel avatarAndCardsContainer;

    public PlayerBoard(String nickname, int chips, String avatarDirectoryPath) {
        setComponentProperties();

        createAvatar(avatarDirectoryPath);

        createNickname(nickname);
        setNicknameProperties();

        createChips(chips);
        setChipsProperties();

        createAvatarAndCardsContainer();
        setAvatarAndCardsContainer();

        attachComponents();
    }

    private void setComponentProperties() {
        setLayout(new GridBagLayout());
        setOpaque(false);
    }

    private void createAvatarAndCardsContainer() {
        avatarAndCardsContainer = new JPanel();
    }

    private void setAvatarAndCardsContainer() {
        avatarAndCardsContainer.setLayout(new BoxLayout(avatarAndCardsContainer, X_AXIS));
        avatarAndCardsContainer.setOpaque(false);
    }

    private void attachAvatar() {
        avatarAndCardsContainer.add(avatar);
    }

    private void attachComponents() {
        attachAvatar();
        attachCards();
        add(avatarAndCardsContainer, new GBC(0, 0, 1, 0.70, 1, 1, WEST, NONE, new Insets(10, 20, 0, 0)));
        attachNickname();
        attachChips();
    }

    private void attachNickname() {
        add(this.nickname, new GBC(0, 1, 1, 0.15, 1, 1, WEST, NONE, new Insets(10, 20, 0, 0)));
    }

    private void attachChips() {
        add(this.chips, new GBC(0, 2, 1, 0.15, 1, 1, WEST, NONE, new Insets(0, 20, 10, 0)));
    }

    private void attachCards() {
        avatarAndCardsContainer.add(Box.createRigidArea(new Dimension(20, 0)));
        avatarAndCardsContainer.add(new Card(System.getProperty(WORKING_DIRECTORY) + RES_DIRECTORY + "3_cuori1.png",
                System.getProperty(WORKING_DIRECTORY) + RES_DIRECTORY + "back.png"));
        avatarAndCardsContainer.add(new Card(System.getProperty(WORKING_DIRECTORY) + RES_DIRECTORY + "3_cuori1.png",
                System.getProperty(WORKING_DIRECTORY) + RES_DIRECTORY + "back.png"));
    }

    private void createAvatar(String avatarDirectoryPath) {
        avatar = new Avatar(avatarDirectoryPath);
    }

    private void createNickname(String nickname) {
        this.nickname = new JLabel(nickname, LEFT);
    }

    private void setNicknameProperties() {
        nickname.setFont(getCustomFont(PLAIN, 20F));
        nickname.setForeground(WHITE);
    }

    private void createChips(int chips) {
        this.chips = new JLabel(Integer.toString(chips), LEFT);
    }

    private void setChipsProperties() {
        this.chips.setFont(new Font(DEFAULT_FONT, PLAIN, 20));
        this.chips.setForeground(WHITE);
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D g2D = (Graphics2D) g;

        g2D.setRenderingHints(getHighQualityRenderingHints());
        drawBackground(g2D);
        drawBorder(g2D);
    }

    private void drawBackground(Graphics2D g2D) {
        g2D.setColor(new Color(191, 54, 12));
        g2D.fillRoundRect(2, 2, getWidth() - 2, getHeight() - 3,
                40, 40);
    }

    private void drawBorder(Graphics2D g2D) {
        g2D.setStroke(new BasicStroke(4f));
        g2D.setColor(WHITE);
        g2D.drawRoundRect(2, 2, getWidth() - 4, getHeight() - 4,
                40, 40);
    }

    public void setAvatar(String avatar) {
        this.avatar.setDirectoryPath(avatar);
    }

    public void setChips(int chips) {
        this.chips.setText(Integer.toString(chips));
    }

    public String getNickname() {
        return nickname.getText();
    }

    public void setNickname(String nickname) {
        this.nickname.setText(nickname);
    }
}
