package utils;

import server.algorithm.HandEvaluator;
import server.model.cards.CardModel;
import server.model.cards.CardRank;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.net.Inet4Address;
import java.net.InetAddress;
import java.net.NetworkInterface;
import java.net.SocketException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.Enumeration;
import java.util.stream.Collectors;


public class Utils {
    public static final String RES_DIRECTORY = "/res/";

    public static final String EMPTY = "";
    public static final String WORKING_DIRECTORY = "user.dir";
    public static final Color TRANSPARENT = new Color(0,0,0,0);
    public static String[] EXTENSIONS = new String[]{"gif", "png", "bmp"};
    public final static String DEFAULT_THEME = "Nimbus";
    public final static String DEFAULT_FONT = "helvetica";

    public static Image loadImageByPath(String filename, Dimension scaleSize) {
        Image scaledImage = null;

        try {
            BufferedImage originalImage = ImageIO.read(new File(filename));
            scaledImage = originalImage.getScaledInstance(scaleSize.width, scaleSize.height, Image.SCALE_SMOOTH);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return scaledImage;
    }

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
                if ((addr instanceof Inet4Address) && !addr.isLoopbackAddress()) {
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

    public static int couplesNumber(ArrayList<CardModel> cards) {
        int tmpCounter = 0;
        for (int i = 1; i < cards.size(); i++) {
            if (i != cards.size() - 1 && cards.get(i).getValue().equals(cards.get(i - 1).getValue()) && cards.get(i).getValue().equals(cards.get(i + 1).getValue()))
                tmpCounter = 3;
            else if (cards.get(i).getValue().equals(cards.get(i - 1).getValue()) && tmpCounter != 3)
                tmpCounter++;

        }
        return tmpCounter;
    }

    public static int[] riordina(ArrayList<HandEvaluator> p, int s) {
        int[] tmp = new int[s];
        for (int i = 0; i < p.size(); i++) {
            tmp[i] = p.get(i).getPlayerPoint();
        }

        Arrays.sort(tmp);
        return tmp;
    }

    public static ArrayList<CardModel> removeCard(ArrayList<CardModel> cards, CardModel card) {
        ArrayList<CardModel> tmp = new ArrayList<>();
        int k = 0;
        int n = cards.size();
        for (int i = 0; i < n; i++) {
            if (cards.get(i).getValue() != card.getValue()) {
                tmp.add(cards.get(i));
                k++;
            }
        }

        return tmp;
    }

}
