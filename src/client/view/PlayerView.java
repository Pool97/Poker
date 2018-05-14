package client.view;

import utils.GBC;
import utils.Utils;

import javax.swing.*;
import java.awt.*;


public class PlayerView extends JPanel {
    private Avatar avatar;
    private JLabel chips;
    private JLabel nickname;
    private JPanel topPanel;

    public PlayerView(String nickname, int chips, String avatarDirectoryPath) {
        setLayout(new GridBagLayout());
        createTopPanel(avatarDirectoryPath);
        createLabels(nickname, chips);
        addComponents();
        setOpaque(false);
    }

    public void addComponents() {
        add(topPanel, new GBC(0, 0, 1, 0.70, 1, 1, GBC.WEST, GBC.NONE, new Insets(10, 20, 0, 0)));
        add(this.nickname, new GBC(0, 1, 1, 0.15, 1, 1, GBC.WEST, GBC.NONE, new Insets(10, 20, 0, 0)));
        add(this.chips, new GBC(0, 2, 1, 0.15, 1, 1, GBC.WEST, GBC.NONE, new Insets(0, 20, 10, 0)));
    }

    public void createTopPanel(String avatarDirectoryPath) {
        topPanel = new JPanel();
        topPanel.setLayout(new BoxLayout(topPanel, BoxLayout.X_AXIS));
        topPanel.setOpaque(false);

        avatar = new Avatar(avatarDirectoryPath);
        topPanel.add(avatar);
        topPanel.add(Box.createRigidArea(new Dimension(20, 0)));
        topPanel.add(new Card(System.getProperty(Utils.WORKING_DIR) + Utils.RES + "3_cuori1.png",
                System.getProperty(Utils.WORKING_DIR) + Utils.RES + "back.png"));
        topPanel.add(new Card(System.getProperty(Utils.WORKING_DIR) + Utils.RES + "3_cuori1.png",
                System.getProperty(Utils.WORKING_DIR) + Utils.RES + "back.png"));
    }

    public void createLabels(String nickname, int chips) {
        this.nickname = new JLabel(nickname, SwingConstants.LEFT);
        this.nickname.setFont(Utils.getCustomFont(Font.PLAIN, 20F));
        this.nickname.setForeground(Color.WHITE);

        this.chips = new JLabel(Integer.toString(chips), SwingConstants.LEFT);
        this.chips.setFont(new Font("helvetica", Font.PLAIN, 20));
        this.chips.setForeground(Color.WHITE);
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D g2 = (Graphics2D)g;
        RenderingHints qualityHints = new RenderingHints(RenderingHints.KEY_ANTIALIASING,
                RenderingHints.VALUE_ANTIALIAS_ON);
        qualityHints.put(RenderingHints.KEY_RENDERING, RenderingHints.VALUE_RENDER_QUALITY);
        g2.setRenderingHints(qualityHints);

        g2.setColor(new Color(191, 54, 12));
        g2.fillRoundRect(2, 2, getWidth() - 2, getHeight() - 3,
                40, 40);

        g2.setStroke(new BasicStroke(4f));
        g2.setColor(Color.WHITE);
        g2.drawRoundRect(2, 2, getWidth() - 4, getHeight() - 4,
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
