package client.frames;

import client.view.AvatarView;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.File;
import java.io.FilenameFilter;
import java.util.Objects;

import static utils.Utils.*;

public class AvatarFrame extends JFrame {
    private final static String NICKNAME_INFO = "Inserire il nickname";
    private final static String FRAME_TITLE = "Scegli l'Avatar";
    private static Dimension avatarsSize = new Dimension(100, 100);


    public AvatarFrame() {

        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setTitle(FRAME_TITLE);
        pack();
        setLocationRelativeTo(null);
        setVisible(true);
        setResizable(false);
        avatarNorth();
        avatarCenter();

    }

    private void avatarNorth() {
        JPanel panelAv = new JPanel();
        panelAv.setLayout(new GridBagLayout());

        GridBagConstraints cC = new GridBagConstraints();

        cC.insets = new Insets(2, 2, 2, 2); // insets for all components
        cC.ipadx = 5; // increases components width by 10 pixels
        cC.ipady = 5; // increases components height by 10 pixels

        cC.gridx = 3;
        cC.gridy = 0;

        add(panelAv, BorderLayout.NORTH);
    }

    private void avatarCenter() {
        JPanel panelAv = new JPanel();
        panelAv.setLayout(new GridBagLayout());

        GridBagConstraints cC = new GridBagConstraints();

        cC.insets = new Insets(2, 2, 2, 2); // insets for all components
        cC.ipadx = 5; // increases components width by 10 pixels
        cC.ipady = 5; // increases components height by 10 pixels

        int x = 1, y = 1;
        String[] EXTENSIONS = new String[]{"gif", "png", "bmp"};
        FilenameFilter filter = (dir, name) -> {
            for (String ext : EXTENSIONS)
                if (name.startsWith("av") && name.endsWith("." + ext))
                    return true;
            return false;
        };

        File avatarDirectory = new File(System.getProperty(WORKING_DIR) + RES);

        for (File f : Objects.requireNonNull(avatarDirectory.listFiles(filter))) {
            cC.gridx = x; // column 0
            cC.gridy = y; // row 0
            AvatarView avatarV = new AvatarView(avatarsSize, f.getName());
            avatarV.addMouseListener(new MyMouseListener(avatarV));
            panelAv.add(avatarV, cC);
            if (x % 5 == 0) {
                x = 0;
                y++;
            }
            x++;
        }

        add(panelAv, BorderLayout.CENTER);
    }

    @Override
    public Dimension getPreferredSize() {
        return new Dimension(600, 500);
    }

    class MyMouseListener extends MouseAdapter {
        private AvatarView av;

        public MyMouseListener(AvatarView av) {
            this.av = av;
        }

        public void mouseClicked(MouseEvent event) {
            String nickname = JOptionPane.showInputDialog(null, NICKNAME_INFO);
            if (nickname != null) {
                new GameFrame(nickname, av.getName());
                dispose();
            }
        }

        public void mouseEntered(MouseEvent event) {
            av.setOpaque(true);
            av.setBorder(BorderFactory.createLineBorder(Color.CYAN, 2, true));

        }

        public void mouseExited(MouseEvent event) {
            av.setOpaque(false);
            av.setBorder(BorderFactory.createLineBorder(TRANSPARENT, 2));
        }
    }
}


