package edu.mfldclin.mcrf.deepsparktext.gui;

import java.awt.Image;
import java.awt.image.BufferedImage;
import java.io.IOException;
import javax.imageio.ImageIO;
import javax.swing.Icon;
import javax.swing.ImageIcon;

/**
 *
 * @author Ehsun Behravesh <ehsun.behravesh@openet.com>
 */
public class GuiUtils {

    private static final String INFO_ICON_PNG_FILE = "Info-64.png";
    private static final String WARN_ICON_PNG_FILE = "Alert-64.png";
    private static final String ERROR_ICON_PNG_FILE = "Error-64.png";
    private static final String APP_ICON = "logo_small.jpg";

    public static Icon getResIcon(String resource) {
        BufferedImage img = getResImage(resource);
        return new ImageIcon(img);
    }

    public static BufferedImage getResImage(String resource) {
        try {
            BufferedImage img = ImageIO.read(Thread.currentThread().getContextClassLoader().getResourceAsStream(resource));
            return img;
        } catch (IOException ex) {
            ex.printStackTrace();
            return null;
        }
    }

    public static Icon getInfoIcon() {
        return getResIcon(INFO_ICON_PNG_FILE);
    }

    public static Icon getWarningIcon() {
        return getResIcon(WARN_ICON_PNG_FILE);
    }

    public static Icon getErrorIcon() {
        return getResIcon(ERROR_ICON_PNG_FILE);
    }

    public static Image getIcon() {
        return getResImage(APP_ICON);
    }

}
