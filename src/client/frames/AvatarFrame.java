package client.frames;

import client.AvatarCategory;
import client.view.Avatar;
import utils.GBC;
import utils.Utils;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.File;
import java.io.FilenameFilter;
import java.util.Objects;

import static javax.swing.JOptionPane.showInputDialog;
import static utils.Utils.*;

public class AvatarFrame extends JFrame {
    private final static String FRAME_TITLE = "Scelta dell'Avatar";
    private final static String NICKNAME_INFO = "Inserisci il nickname per iniziare a giocare";
    private final static String CONNECT_TO_A_ROOM = "Inserisci l'IP della stanza a cui vuoi connetterti";

    private final static String TOTAL_PLAYERS = "Inserisci il numero di partecipanti";
    private final static String AVATARS_FOLDER = "/avatars/";
    private static Dimension avatarsSize = new Dimension(125, 125);
    private static String[] EXTENSIONS = new String[]{"gif", "png", "bmp"};
    private static Integer[] POSSIBLE_TOTAL_PLAYERS = new Integer[]{2, 3, 4, 5, 6};
    private JLabel avatarDescriptor;
    private JPanel avatarsContainer;
    private int currentRow;
    private int playerMode;


    public AvatarFrame(int playerMode) {
        this.playerMode = playerMode;
        Utils.setLookAndFeel(Utils.DEFAULT_THEME);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setTitle(FRAME_TITLE);
        pack();
        setLocationRelativeTo(null);
        setVisible(true);
        setResizable(false);
        createGUI();
    }

    private void createGUI() {
        JScrollPane jScrollPane = new JScrollPane();
        jScrollPane.setBorder(BorderFactory.createEmptyBorder());
        jScrollPane.getVerticalScrollBar().setUnitIncrement(10);
        avatarsContainer = new JPanel();
        avatarsContainer.setLayout(new GridBagLayout());
        setBackground(Color.BLACK);
        avatarsContainer.setBackground(Color.BLACK);

        addCategory(AvatarCategory.cinema);
        addCategory(AvatarCategory.celebrity);
        addCategory(AvatarCategory.generic);


        JPanel descrContainer = new JPanel();
        descrContainer.setBackground(Color.BLACK);

        avatarDescriptor = new JLabel();
        avatarDescriptor.setHorizontalAlignment(SwingConstants.CENTER);
        avatarDescriptor.setPreferredSize(new Dimension(200, 40));
        avatarDescriptor.setFont(Utils.getCustomFont(Font.BOLD, 20));
        avatarDescriptor.setForeground(Color.WHITE);
        avatarDescriptor.setBorder(BorderFactory.createLineBorder(Color.WHITE));

        descrContainer.add(avatarDescriptor);

        add(descrContainer, BorderLayout.SOUTH);
        jScrollPane.setViewportView(avatarsContainer);
        add(jScrollPane, BorderLayout.CENTER);
    }

    public void addCategory(AvatarCategory category) {
        GBC gbc = new GBC(0, currentRow);

        gbc.ipadx = 5;
        gbc.ipady = 10;
        gbc.gridwidth = 5;
        gbc.anchor = GBC.CENTER;

        JLabel categoryLabel = new JLabel(category.name(), SwingConstants.CENTER);
        categoryLabel.setForeground(Color.WHITE);
        categoryLabel.setFont(Utils.getCustomFont(Font.PLAIN, 30));
        avatarsContainer.add(categoryLabel, gbc);
        addByCategory(category);
        currentRow++;
    }

    @Override
    public Dimension getPreferredSize() {
        return new Dimension(720, 500);
    }

    private void addByCategory(AvatarCategory category) {
        File avatarDirectory = new File(System.getProperty(WORKING_DIR) + RES + AVATARS_FOLDER + category.name() + "/");
        int currentColumn = 0;
        currentRow++;

        FilenameFilter filter = (dir, name) -> {
            for (String ext : EXTENSIONS)
                if (name.endsWith("." + ext))
                    return true;
            return false;
        };

        for (File f : Objects.requireNonNull(avatarDirectory.listFiles(filter))) {
            GBC gbc = new GBC(currentColumn, currentRow);
            gbc.ipadx = 5;
            gbc.ipady = 10;
            gbc.gridwidth = 1;
            gbc.anchor = GBC.EAST;
            currentColumn++;

            Avatar avatarV = new Avatar(category, f.getName());
            avatarV.setPreferredSize(avatarsSize);
            avatarV.addMouseListener(new MyMouseListener(avatarV));
            avatarsContainer.add(avatarV, gbc);

            if (currentColumn % 5 == 0) {
                currentColumn = 0;
                currentRow++;
            }
        }
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


