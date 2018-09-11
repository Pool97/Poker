package client.ui.frames;

import client.ui.components.Avatar;
import client.ui.dialogs.CreatorDialog;
import client.ui.dialogs.PlayerDialog;
import client.ui.dialogs.PokerDialog;
import utils.GBC;
import utils.Utils;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.File;
import java.io.FilenameFilter;
import java.util.Objects;

import static utils.Utils.*;

public class SelectAvatarFrame extends JFrame {
    private final static String FRAME_TITLE = "Scelta dell'Avatar";
    private final static String AVATARS_FOLDER = "avatars";
    private static Dimension avatarsSize = new Dimension(125, 125);

    private JLabel avatarDescriptor;
    private JPanel avatarsContainer;
    private JPanel avatarDescriptorContainer;
    private JScrollPane scrollBar;
    private PokerDialog dialog;
    private int currentRow;
    private int playerMode;


    public SelectAvatarFrame(int playerMode) {
        this.playerMode = playerMode;

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

    private void setFrameProperties() {
        Utils.setLookAndFeel(Utils.DEFAULT_THEME);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setTitle(FRAME_TITLE);
        pack();
        setLocationRelativeTo(null);
        setVisible(true);
        setResizable(false);
        addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                WelcomeFrame.launchGame();
            }
        });
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
        avatarDescriptorContainer.setLayout(new BorderLayout());
        avatarDescriptorContainer.setBackground(new Color(178, 5, 0));
    }

    private void setAvatarDescriptorProperties() {
        avatarDescriptor.setHorizontalAlignment(SwingConstants.CENTER);
        avatarDescriptor.setPreferredSize(new Dimension(200, 40));
        avatarDescriptor.setFont(new Font(DEFAULT_FONT, Font.BOLD, 20));
        avatarDescriptor.setForeground(Color.WHITE);
    }

    private void setScrollBarProperties() {
        scrollBar.setBorder(BorderFactory.createEmptyBorder());
        scrollBar.getVerticalScrollBar().setUnitIncrement(10);
        scrollBar.setViewportView(avatarsContainer);
    }

    private void setAvatarsContainerProperties() {
        avatarsContainer.setLayout(new GridBagLayout());
        avatarsContainer.setBackground(new Color(0, 178, 61));
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

    @Override
    public Dimension getPreferredSize() {
        return new Dimension(720, 500);
    }

    private void attachAvatars() {
        File avatarDirectory = new File(System.getProperty(WORKING_DIRECTORY) + RES_DIRECTORY + AVATARS_FOLDER);
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

            Avatar avatarV = new Avatar(f.getName());
            avatarV.setOpacity(true);
            avatarV.setPreferredSize(avatarsSize);
            avatarV.addMouseListener(new MyMouseListener(avatarV));
            avatarsContainer.add(avatarV, new GBC(currentColumn, currentRow, 0, 0, 1, 1, GBC.CENTER,
                    GBC.NONE, new Insets(5, 10, 5, 10)));

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
            if (playerMode == 0)
                dialog = new CreatorDialog(avatar);

            else
                dialog = new PlayerDialog(avatar);
            dialog.pack();
            dialog.setLocationRelativeTo(null);
            dialog.setVisible(true);
            dialog.setFocusOnButton();
            dispose();
        }

        public void mouseEntered(MouseEvent event) {
            avatar.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
            avatarDescriptor.setText(avatar.getName().toUpperCase());
            //avatar.setBorder(BorderFactory.createLineBorder(Color.WHITE, 2, true));
        }

        public void mouseExited(MouseEvent event) {
            avatar.setBorder(BorderFactory.createLineBorder(TRANSPARENT, 2));
        }
    }
}


