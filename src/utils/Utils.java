package utils;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import static graphics.CardView.RES;
import static graphics.CardView.WORKING_DIR;

public class Utils {
    /**
     * Permette di assegnare la versione riscalata dell'immagine originale.
     * @param filename Nome del file relativo all'immagine da caricare.
     * @param scaleSize Dimensione dell'immagine riscalata.
     * @return Immagine riscalata.
     */

    public static BufferedImage loadImage(String filename, Dimension scaleSize){
        BufferedImage scaledImage = null;

        try {
            BufferedImage originalImage = ImageIO.read(new File(System.getProperty(WORKING_DIR) + RES + filename));
            scaledImage = Scalr.resize(originalImage, Scalr.Method.ULTRA_QUALITY, Scalr.Mode.BEST_FIT_BOTH, (int)scaleSize.getWidth(), (int)scaleSize.getHeight(), Scalr.OP_DARKER);
        } catch (IOException e) {
            e.printStackTrace();
        }

        return scaledImage;
    }

    public static Font getCustomFont(int fontStyle, float fontSize){
        File font_file = new File(System.getProperty("user.dir").concat("/rotis.ttf"));
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
}