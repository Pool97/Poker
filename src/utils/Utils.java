package utils;

import javafx.util.Pair;
import server.model.CardRank;
import server.model.CardSuit;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.net.Inet4Address;
import java.net.UnknownHostException;
import java.util.ArrayList;


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

    public static String cardName(Pair<CardSuit, CardRank> p) {
        String nameCard = p.getValue().toString() + "_" + p.getKey().toString() + ".png";
        return nameCard;
    }


    public static int controllo(Pair<CardSuit, CardRank> p) {
        if (p.getValue().toString().equals("ACE"))
            return 14;
        else if (p.getValue().toString().equals("TWO"))
            return 2;
        else if (p.getValue().toString().equals("THREE"))
            return 3;
        else if (p.getValue().toString().equals("FOUR"))
            return 4;
        else if (p.getValue().toString().equals("FIVE"))
            return 5;
        else if (p.getValue().toString().equals("SIX"))
            return 6;
        else if (p.getValue().toString().equals("SEVEN"))
            return 7;
        else if (p.getValue().toString().equals("EIGHT"))
            return 8;
        else if (p.getValue().toString().equals("NINE"))
            return 9;
        else if (p.getValue().toString().equals("TEN"))
            return 10;
        else if (p.getValue().toString().equals("JACK"))
            return 11;
        else if (p.getValue().toString().equals("QUEEN"))
            return 12;
        else if (p.getValue().toString().equals("KING"))
            return 13;

        return 0;


    }

    public static int controlloA14(Pair<CardSuit, CardRank> p) {
        if (p.getValue().toString().equals("ACE"))
            return 14;
        else if (p.getValue().toString().equals("TWO"))
            return 2;
        else if (p.getValue().toString().equals("THREE"))
            return 3;
        else if (p.getValue().toString().equals("FOUR"))
            return 4;
        else if (p.getValue().toString().equals("FIVE"))
            return 5;
        else if (p.getValue().toString().equals("SIX"))
            return 6;
        else if (p.getValue().toString().equals("SEVEN"))
            return 7;
        else if (p.getValue().toString().equals("EIGHT"))
            return 8;
        else if (p.getValue().toString().equals("NINE"))
            return 9;
        else if (p.getValue().toString().equals("TEN"))
            return 10;
        else if (p.getValue().toString().equals("JACK"))
            return 11;
        else if (p.getValue().toString().equals("QUEEN"))
            return 12;
        else if (p.getValue().toString().equals("KING"))
            return 13;

        return 0;


    }

    public static int controlloA1(Pair<CardSuit, CardRank> p) {
        if (p.getValue().toString().equals("ACE"))
            return 1;
        else if (p.getValue().toString().equals("TWO"))
            return 2;
        else if (p.getValue().toString().equals("THREE"))
            return 3;
        else if (p.getValue().toString().equals("FOUR"))
            return 4;
        else if (p.getValue().toString().equals("FIVE"))
            return 5;
        else if (p.getValue().toString().equals("SIX"))
            return 6;
        else if (p.getValue().toString().equals("SEVEN"))
            return 7;
        else if (p.getValue().toString().equals("EIGHT"))
            return 8;
        else if (p.getValue().toString().equals("NINE"))
            return 9;
        else if (p.getValue().toString().equals("TEN"))
            return 10;
        else if (p.getValue().toString().equals("JACK"))
            return 11;
        else if (p.getValue().toString().equals("QUEEN"))
            return 12;
        else if (p.getValue().toString().equals("KING"))
            return 13;

        return 0;


    }


    public static String controlloS(Pair<CardSuit, CardRank> p) {

        if (p.getKey().toString().equals("CLUBS"))
            return "C";
        else if (p.getKey().toString().equals("DIAMONDS"))
            return "D";
        else if (p.getKey().toString().equals("HEARTS"))
            return "H";
        else if (p.getKey().toString().equals("SPADES"))
            return "S";


        return "SCEMO";


    }

    public static int[] cardArray(Pair<CardSuit, CardRank> com1, Pair<CardSuit, CardRank> com2, Pair<CardSuit, CardRank> com3, Pair<CardSuit, CardRank> com4, Pair<CardSuit, CardRank> com5, Pair<CardSuit, CardRank> ply1, Pair<CardSuit, CardRank> ply2) {

        c1 = controllo(com1);
        c2 = controllo(com2);
        c3 = controllo(com3);
        c4 = controllo(com4);
        c5 = controllo(com5);
        pl1 = controllo(ply1);
        pl2 = controllo(ply2);

        int[] cardsC = {c1, c2, c3, c4, c5, pl1, pl2};

        return cardsC;
    }

    public static ArrayList<Integer> cardArrayA14(ArrayList<Pair<CardSuit, CardRank>> a) {

        int n = a.size();
        ArrayList<Integer> cardsC = new ArrayList<>();
        for (int i = 0; i < n; i++) {
            cardsC.add(controlloA14(a.get(i)));
        }

        return cardsC;
    }

    public static ArrayList<Integer> cardArrayA1(ArrayList<Pair<CardSuit, CardRank>> a) {

        int n = a.size();
        ArrayList<Integer> cardsC = new ArrayList<>();
        for (int i = 0; i < n; i++) {
            cardsC.add(controlloA1(a.get(i)));
        }

        return cardsC;
    }


    public static ArrayList<String> cardArrayString(ArrayList<Pair<CardSuit, CardRank>> a) {

        int n = a.size();
        ArrayList<String> cardsC = new ArrayList<>();
        for (int i = 0; i < n; i++) {
            cardsC.add(controlloS(a.get(i)));
        }

        return cardsC;
    }

    public static ArrayList<Pair<CardSuit, CardRank>> cardArrayPair(Pair<CardSuit, CardRank> com1, Pair<CardSuit, CardRank> com2, Pair<CardSuit, CardRank> com3, Pair<CardSuit, CardRank> com4, Pair<CardSuit, CardRank> com5, Pair<CardSuit, CardRank> ply1, Pair<CardSuit, CardRank> ply2) {

        ArrayList<Pair<CardSuit, CardRank>> cardsC = new ArrayList<>();
        cardsC.add(com1);
        cardsC.add(com2);
        cardsC.add(com3);
        cardsC.add(com4);
        cardsC.add(com5);
        cardsC.add(ply1);
        cardsC.add(ply2);

        return cardsC;
    }
}
