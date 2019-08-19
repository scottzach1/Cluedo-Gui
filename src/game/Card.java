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
     * @return Return name on Game.Card.
     */
    public String getName() {
        return name;
    }

    /**
     * Gets ImageIcon from Enum, accepts either SpriteAlias, RoomAlias or WeaponAlias
     * @param alias Enum, either SpriteAlias, RoomAlias or WeaponAlias
     * @param unseen Whether to apply seen overlay.
     * @return ImageIcon of card, card_unseen if failed to retrieve file.
     */
    private static ImageIcon getCard(Enum alias, boolean unseen) {
        ImageIcon icon = new ImageIcon("card_" + alias.toString().toLowerCase() + ".png");
        if (icon.getIconWidth() <= 0) {
            icon = new ImageIcon("card_unseen.png");
        }
        if (!unseen) return icon;
        else return new CombinedImageIcon(icon, new ImageIcon("card_unseen.png"));
    }

    /**
     * Gets Scaled ImageIcon from Enum, accepts either SpriteAlias, RoomAlias or WeaponAlias.
     * (Keeps Aspect Ratio).
     * @param alias Enum, either SpriteAlias, RoomAlias or WeaponAlias
     * @param unseen Whether to apply unseen overlay.
     * @param width Width to scale to.
     * @return Scaled ImageIcon of card, card_unseen if failed to retrieve file.
     */
    public static ImageIcon getCardSetWidth(Enum alias, boolean unseen, int width) {
        ImageIcon imageIcon = getCard(alias, unseen);
        int height = (int) (0f + width * ((0f + imageIcon.getIconHeight()) / (0f + imageIcon.getIconWidth())));
        return new ImageIcon(imageIcon.getImage().getScaledInstance(width, height, Image.SCALE_SMOOTH));
    }

    /**
     * Gets Scaled ImageIcon from Enum, accepts either SpriteAlias, RoomAlias or WeaponAlias.
     * (Keeps Aspect Ratio).
     * @param alias Enum, either SpriteAlias, RoomAlias or WeaponAlias
     * @param unseen Whether to apply unseen overlay.
     * @param height Height to scale to.
     * @return Scaled ImageIcon of card, card_unseen if failed to retrieve file.
     */
    public static ImageIcon getCardSetHeight(Enum alias, boolean unseen, int height) {
        ImageIcon imageIcon = getCard(alias, unseen);
        int width = (int) (0f + height * ((0f + imageIcon.getIconWidth()) / (0f + imageIcon.getIconHeight())));
        return new ImageIcon(imageIcon.getImage().getScaledInstance(width, height, Image.SCALE_SMOOTH));
    }
}