package game;

import extra.CombinedImageIcon;

import javax.swing.*;
import java.awt.*;

public abstract class Card {

    // ------------------------
    // MEMBER VARIABLES
    // ------------------------

    // Game.Card Attributes
    private String name;

    // ------------------------
    // CONSTRUCTOR
    //

    /**
     * Game.Card: Constructor for Game.Card.
     * Stores name of Game.Card, this
     * should be an Enum toString of a subclass.
     *
     * @param name Name on Game.Card.
     */
    public Card(String name) {
        this.name = name;
    }

    // ------------------------
    // INTERFACE
    // ------------------------

    /**
     * getName: Get name on Game.Card.
     *
     * @return Return name on Game.Card.
     */
    public String getName() {
        return name;
    }

    ;

    public static ImageIcon getCard(Enum alias, boolean unseen) {
        ImageIcon icon = new ImageIcon("card_" + alias.toString().toLowerCase() + ".png");
        if (icon.getIconWidth() <= 0) {
            icon = new ImageIcon("card_unseen.png");
        }
        if (!unseen) return icon;
        else return new CombinedImageIcon(icon, new ImageIcon("card_unseen.png"));
    }

    public static ImageIcon getCardSetWidth(Enum alias, boolean unseen, int width) {
        ImageIcon imageIcon = getCard(alias, unseen);
        int height = (int) (0f + width * ((0f + imageIcon.getIconHeight()) / (0f + imageIcon.getIconWidth())));
        return new ImageIcon(imageIcon.getImage().getScaledInstance(width, height, Image.SCALE_SMOOTH));
    }

    public static ImageIcon getCardSetHeight(Enum alias, boolean unseen, int height) {
        ImageIcon imageIcon = getCard(alias, unseen);
        int width = (int) (0f + height * ((0f + imageIcon.getIconWidth()) / (0f + imageIcon.getIconHeight())));
        return new ImageIcon(imageIcon.getImage().getScaledInstance(width, height, Image.SCALE_SMOOTH));
    }
}