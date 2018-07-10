package client.frames;

import client.AvatarCategory;
import client.components.Avatar;
import utils.GBC;
import utils.Utils;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.File;
import java.io.FilenameFilter;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;

import static javax.swing.JOptionPane.showInputDialog;
import static utils.Utils.*;

public class SelectAvatarFrame extends JFrame {
    private final static String FRAME_TITLE = "Scelta dell'Avatar";
    private final static String NICKNAME_INFO = "Inserisci il nickname per iniziare a giocare";
    private final static String CONNECT_TO_A_ROOM = "Inserisci l'IP della stanza a cui vuoi connetterti";
    private final static String TOTAL_PLAYERS = "Inserisci il numero di partecipanti";
    private final static String AVATARS_FOLDER = "/avatars/";
    private static Dimension avatarsSize = new Dimension(125, 125);

    private JLabel avatarDescriptor;
    private JPanel avatarsContainer;
    private JPanel avatarDescriptorContainer;
    private JScrollPane scrollBar;
    private List<AvatarCategory> categories;
    private int currentRow;
    private int playerMode;


    public SelectAvatarFrame(int playerMode) {
        this.playerMode = playerMode;
        this.categories = Arrays.asList(AvatarCategory.cinema, AvatarCategory.celebrity, AvatarCategory.generic);

        setFrameProperties();

        createAvatarsContainer();
        setAvatarsContainerProperties();

        createScrollBar();
        setScrollBarProperties();

        createAvatarDescriptor();
        setAvatarDescriptorProperties();

        createAvatarDescriptorContainer();
        setAvatarDescriptorContainerProperties();

        attachScrollBar();
        attachAvatarDescriptor();
        attachAvatarDescriptorContainer();

        attachAvatars();
    }

    private void attachAvatars() {
        categories.forEach(this::addAvatarsCategory);
    }

    private void setFrameProperties() {
        Utils.setLookAndFeel(Utils.DEFAULT_THEME);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setTitle(FRAME_TITLE);
        pack();
        setLocationRelativeTo(null);
        setVisible(true);
        setResizable(false);
    }

    private void createAvatarsContainer() {
        avatarsContainer = new JPanel();
    }

    private void createScrollBar() {
        scrollBar = new JScrollPane();
    }

    private void createAvatarDescriptor() {
        avatarDescriptor = new JLabel();
    }

    private void createAvatarDescriptorContainer() {
        avatarDescriptorContainer = new JPanel();
    }

    private void setAvatarDescriptorContainerProperties() {
        avatarDescriptorContainer.setBackground(new Color(3, 155, 229));
    }

    private void setAvatarDescriptorProperties() {
        avatarDescriptor.setHorizontalAlignment(SwingConstants.CENTER);
        avatarDescriptor.setPreferredSize(new Dimension(200, 40));
        avatarDescriptor.setFont(Utils.getCustomFont(Font.BOLD, 20));
        avatarDescriptor.setForeground(Color.WHITE);
    }

    private void setScrollBarProperties() {
        scrollBar.setBorder(BorderFactory.createEmptyBorder());
        scrollBar.getVerticalScrollBar().setUnitIncrement(10);
        scrollBar.setViewportView(avatarsContainer);
    }

    private void setAvatarsContainerProperties() {
        avatarsContainer.setLayout(new GridBagLayout());
        avatarsContainer.setBackground(new Color(38, 86, 59));
    }

    private void attachAvatarDescriptor() {
        avatarDescriptorContainer.add(avatarDescriptor);
    }

    private void attachAvatarDescriptorContainer() {
        add(avatarDescriptorContainer, BorderLayout.SOUTH);
    }

    private void attachScrollBar() {
        add(scrollBar, BorderLayout.CENTER);
    }

    private void addAvatarsCategory(AvatarCategory category) {
        createAndAttachCategoryDescriptor(category);
        addByCategory(category);

    }

    private void createAndAttachCategoryDescriptor(AvatarCategory category) {
        JLabel categoryLabel = new JLabel(category.name(), SwingConstants.CENTER);

        categoryLabel.setForeground(Color.WHITE);
        categoryLabel.setFont(Utils.getCustomFont(Font.BOLD, 30));
        avatarsContainer.add(categoryLabel, new GBC(0, currentRow, 0, 0, 6, 1, GBC.CENTER, GBC.NONE,
                new Insets(5, 0, 5, 0)));
    }

    @Override
    public Dimension getPreferredSize() {
        return new Dimension(720, 500);
    }

    private void addByCategory(AvatarCategory category) {
        File avatarDirectory = new File(System.getProperty(WORKING_DIRECTORY) + RES_DIRECTORY + AVATARS_FOLDER + category.name() + "/");
        int currentColumn = 0;
        currentRow++;


        FilenameFilter filter = (dir, name) -> {
            for (String ext : Utils.EXTENSIONS)
                if (name.endsWith("." + ext))
                    return true;
            return false;
        };

        for (File f : Objects.requireNonNull(avatarDirectory.listFiles(filter))) {
            currentColumn++;

            Avatar avatarV = new Avatar(category, f.getName());
            avatarV.setPreferredSize(avatarsSize);
            avatarV.addMouseListener(new MyMouseListener(avatarV));
            avatarsContainer.add(avatarV, new GBC(currentColumn, currentRow, 0, 0, 1, 1, GBC.CENTER,
                    GBC.NONE, new Insets(5, 5, 5, 5)));

            if (currentColumn % 5 == 0) {
                currentColumn = 0;
                currentRow++;
            }
        }
        currentRow++;
    }

    class MyMouseListener extends MouseAdapter {
        private Avatar avatar;

        public MyMouseListener(Avatar avatar) {
            this.avatar = avatar;
        }

        public void mouseClicked(MouseEvent event) {
            String nickname = showInputDialog(NICKNAME_INFO);

            if (nickname != null && playerMode == 0) {
                if (!Utils.isLinux())
                    new CreatorGameFrame(nickname, avatar.getDirectoryPath(), Utils.askForAChoice(POSSIBLE_TOTAL_PLAYERS,
                            TOTAL_PLAYERS), Utils.getHostAddress());
                else
                    new LinuxFrame(nickname, avatar.getDirectoryPath());
            } else if (nickname != null && playerMode == 1)
                new GameFrame(nickname, avatar.getDirectoryPath(), JOptionPane.showInputDialog(CONNECT_TO_A_ROOM));

            dispose();
        }

        public void mouseEntered(MouseEvent event) {
            avatar.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
            avatarDescriptor.setText(avatar.getName());
            avatar.setBackground(new Color(255, 255, 255, 70));
            avatar.setOpaque(true);
            avatar.setBorder(BorderFactory.createLineBorder(Color.WHITE, 2, true));
        }

        public void mouseExited(MouseEvent event) {
            avatar.setOpaque(false);
            avatar.setBorder(BorderFactory.createLineBorder(TRANSPARENT, 2));
        }
    }
}


