package extra;

import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;

/**
 * https://stackoverflow.com/questions/17088599/how-to-combine-two-icons-in-java
 */

public class CombinedImageIcon extends ImageIcon {

    public CombinedImageIcon(ImageIcon base, ImageIcon top) {
        super();
        BufferedImage combinedImage = new BufferedImage(base.getIconWidth(), base.getIconHeight(), BufferedImage.TYPE_INT_ARGB);
        Graphics2D g = combinedImage.createGraphics();
        g.drawImage(base.getImage(),0,0,null);
        g.drawImage(top.getImage(),0,0,null);
        g.dispose();
        setImage(combinedImage);
    }
}
