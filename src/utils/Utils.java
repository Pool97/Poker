package utils;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.net.Inet4Address;
import java.net.UnknownHostException;


public class Utils {
    public static final String RES_DIRECTORY = "/res/";

    public static final String EMPTY = "";
    public static final String WORKING_DIRECTORY = "user.dir";
    public static final Color TRANSPARENT = new Color(0,0,0,0);
    public final static String DEFAULT_THEME = "Nimbus";
    public final static String DEFAULT_FONT = "helvetica";

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
        } catch (FontFormatException e) {
            e.printStackTrace();
        } catch (IOException e) {
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
}
