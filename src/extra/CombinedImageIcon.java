package extra;

import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.util.List;

/**
 * CombinedImageIcon: Helpful class for stacking icons.
 * https://stackoverflow.com/questions/17088599/how-to-combine-two-icons-in-java
 */
public class CombinedImageIcon extends ImageIcon {

    /**
     * CombinedImageIcon: Create an ImageIcon from a base Icon underneath layers of
     * icons.
     * @param base Bottom layer.
     * @param layers Layers to stack.
     */
    public CombinedImageIcon(ImageIcon base, List<ImageIcon> layers) {
        super();
        BufferedImage combinedImage = new BufferedImage(base.getIconWidth(), base.getIconHeight(), BufferedImage.TYPE_INT_ARGB);
        Graphics2D g = combinedImage.createGraphics();
        layers.add(0, base);
        layers.forEach(imageIcon -> g.drawImage(imageIcon.getImage(), 0, 0, null));
        g.dispose();
        setImage(combinedImage);
    }


    /**
     * CompinedImageIcon: Create an ImageIcon from a base icon and an overlay icon.
     * @param base Bottom icon.
     * @param top Top icon.
     */
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
