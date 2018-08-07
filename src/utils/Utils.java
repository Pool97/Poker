package utils;

import server.model.CardModel;
import server.model.CardRank;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.net.*;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.Enumeration;
import java.util.stream.Collectors;


public class Utils {
    public static final String RES_DIRECTORY = "/res/";

    public static final String EMPTY = "";
    public static final String WORKING_DIRECTORY = "user.dir";
    public static final Color TRANSPARENT = new Color(0,0,0,0);
    public static final Integer[] POSSIBLE_TOTAL_PLAYERS = new Integer[]{2, 3, 4, 5, 6};
    public static String[] EXTENSIONS = new String[]{"gif", "png", "bmp"};
    public final static String DEFAULT_THEME = "Nimbus";
    public final static String DEFAULT_FONT = "helvetica";
    private static int c1;
    private static int c2;
    private static int c3;
    private static int c4;
    private static int c5;
    private static int pl1;
    private static int pl2;

    /**
     * Permette di assegnare la versione riscalata dell'immagine originale.
     * @param filename Nome del file relativo all'immagine da caricare.
     * @param scaleSize Dimensione dell'immagine riscalata.
     * @return Immagine riscalata.
     */

    public static BufferedImage loadImage(String filename, Dimension scaleSize){
        BufferedImage scaledImage = null;

        try {
            BufferedImage originalImage = ImageIO.read(new File(System.getProperty(WORKING_DIRECTORY) + RES_DIRECTORY + filename));
            scaledImage = Scalr.resize(originalImage, Scalr.Method.ULTRA_QUALITY, Scalr.Mode.BEST_FIT_BOTH, (int)scaleSize.getWidth(), (int)scaleSize.getHeight(), Scalr.OP_DARKER);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return scaledImage;
    }

    public static BufferedImage loadImageByPath(String filename, Dimension scaleSize) {
        BufferedImage scaledImage = null;

        try {
            System.out.println(filename);
            BufferedImage originalImage = ImageIO.read(new File(filename));
            scaledImage = Scalr.resize(originalImage, Scalr.Method.ULTRA_QUALITY, Scalr.Mode.BEST_FIT_BOTH, (int) scaleSize.getWidth(), (int) scaleSize.getHeight(), Scalr.OP_DARKER);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return scaledImage;
    }

    public static Font getCustomFont(int fontStyle, float fontSize){
        File font_file = new File(System.getProperty("user.dir").concat("/poker_kings.ttf"));
        Font derivedFont = null;
        try {
            Font font = Font.createFont(Font.TRUETYPE_FONT, font_file);
            derivedFont = font.deriveFont(fontStyle, fontSize);
        } catch (FontFormatException | IOException e) {
            e.printStackTrace();
        }
        return derivedFont;
    }

    public static <T> T askForAChoice(T[] list, String message) {
        JComboBox<T> comboBox = new JComboBox<>(list);
        JOptionPane.showMessageDialog(null, comboBox, message, JOptionPane.QUESTION_MESSAGE);
        return (T) comboBox.getSelectedItem();
    }

    public static boolean isLinux() {
        return System.getProperty("os.name").equals("Linux");
    }

    public static String getHostAddress() {
        try {
            return Inet4Address.getLocalHost().getHostAddress();
        } catch (UnknownHostException e) {
            e.printStackTrace();
        }
        return null;
    }

    /*private void searchForAddresses() {
        Enumeration<NetworkInterface> interfaces = null;
        try {
            interfaces = NetworkInterface.getNetworkInterfaces();
        } catch (SocketException e) {
            e.printStackTrace();
        }
        while (interfaces.hasMoreElements()) {
            NetworkInterface i = interfaces.nextElement();
            for (Enumeration<InetAddress> addresses = i.getInetAddresses(); addresses.hasMoreElements(); ) {
                InetAddress addr = addresses.nextElement();
                if (addr instanceof Inet4Address) {
                    InterfaceView interfaceView = new InterfaceView(addr.getHostAddress());
                    interfaceView.addMouseListener(new LinuxFrame.MyMouseListener(interfaceView));
                    interfaceView.setAlignmentX(Component.LEFT_ALIGNMENT);
                    netContainer.add(interfaceView);
                }
            }
        }
    }*/

    public static String getIpAddress() {
        Enumeration<NetworkInterface> interfaces = null;
        try {
            interfaces = NetworkInterface.getNetworkInterfaces();
        } catch (SocketException e) {
            e.printStackTrace();
        }

        while (interfaces.hasMoreElements()) {
            NetworkInterface i = interfaces.nextElement();
            for (Enumeration<InetAddress> addresses = i.getInetAddresses(); addresses.hasMoreElements(); ) {
                InetAddress addr = addresses.nextElement();
                if (addr instanceof Inet4Address) {
                    return addr.getHostAddress();
                }
            }
        }
        return "";
    }

    public static RenderingHints getHighQualityRenderingHints() {
        RenderingHints qualityHints = new RenderingHints(RenderingHints.KEY_ANTIALIASING,
                RenderingHints.VALUE_ANTIALIAS_ON);
        qualityHints.put(RenderingHints.KEY_RENDERING, RenderingHints.VALUE_RENDER_QUALITY);
        return qualityHints;
    }
    public static void setLookAndFeel(String theme) {
        try {
            for (UIManager.LookAndFeelInfo info : UIManager.getInstalledLookAndFeels()) {
                if (theme.equals(info.getName())) {
                    UIManager.setLookAndFeel(info.getClassName());
                    break;
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void sortCards(ArrayList<CardModel> cards, boolean isAceMax) {
        CardRank aceValueReversed = isAceMax ? CardRank.ACE : CardRank.ACE_MAX;

        ArrayList<CardModel> orderedCards = cards.stream()
                .filter(card -> card.getValue() != aceValueReversed)
                .collect(Collectors.toCollection(ArrayList::new));

        orderedCards.addAll(cards.stream()
                .filter(card -> card.getValue() == aceValueReversed)
                .map(CardModel::setValue)
                .collect(Collectors.toCollection(ArrayList::new)));

        Comparator<CardModel> comparator = Comparator.comparingInt(card -> card.getValue().ordinal());
        orderedCards.sort(comparator.reversed());
        cards.clear();
        cards.addAll(orderedCards);
    }

    public static void removeDuplicates(ArrayList<CardModel> cards) {
        int k = 1;
        int n = cards.size();
        for (int i = 1; i < n; i++) {
            if (cards.get(i).getValue() != cards.get(i - 1).getValue()) {
                cards.set(k, cards.get(i));
                k++;
            }
        }
    }

    public static int checkNumberOfCouples(ArrayList<CardModel> cardsToExamine, ArrayList<CardModel> finalCards) {
        for (int i = 0; i < cardsToExamine.size(); i++) {
            for (int j = 0; j < cardsToExamine.size(); j++) {
                if (i != j) {
                    if (!finalCards.contains(cardsToExamine.get(j)) && !finalCards.contains(cardsToExamine.get(i))) {
                        if (cardsToExamine.get(i).getValue() == cardsToExamine.get(j).getValue()) {
                            finalCards.add(cardsToExamine.get(i));
                            finalCards.add(cardsToExamine.get(j));
                        }
                    }
                }
            }
        }
        return finalCards.size() / 2;
    }
}
