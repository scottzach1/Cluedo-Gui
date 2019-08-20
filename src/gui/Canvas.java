package gui;

import game.*;

import javax.swing.*;
import javax.swing.border.Border;
import javax.swing.border.TitledBorder;
import java.awt.*;
import java.awt.event.ComponentEvent;
import java.awt.event.ComponentListener;
import java.util.ArrayList;
import java.util.HashSet;

public class Canvas extends JPanel {

    // --------------------------------------------------
    // FIELDS
    // --------------------------------------------------

    private final Color accentCol = Color.BLACK;
    private final Color baseCol = Color.WHITE;

    private String borderTitle;
    private GridBagConstraints gc;
    private ArrayList<Component> components;
    private final CluedoGame cluedoGame;
    private final GUI gui;
    private Board board;


    // --------------------------------------------------
    // CONSTRUCTOR
    // --------------------------------------------------

    public Canvas(CluedoGame parent) {
        borderTitle = "CLUEDO GAME";
        components = new ArrayList<>();
        cluedoGame = parent;
        gui = parent.getGui();
        this.board = parent.getBoard();

        // Set the Size of the canvas panel
        setPreferredSize(new Dimension(GUI.SCREEN_WIDTH, GUI.CANVAS_HEIGHT));
        // Create the boarder
        drawBorder();

        // Set layout
        setLayout(new GridBagLayout());
        gc = new GridBagConstraints();
        gc.fill = GridBagConstraints.BOTH;
    }


    // --------------------------------------------------
    // PUBLIC METHODS
    // --------------------------------------------------

    public void mainMenu() {
        Image image = (new ImageIcon("title.png")).getImage();
        image = image.getScaledInstance(getWidth() / 2, getHeight() / 2, java.awt.Image.SCALE_SMOOTH);
        JLabel titleImg = new JLabel(new ImageIcon(image));
        titleImg.setPreferredSize(new Dimension(getWidth() / 2, getHeight() / 2));

        components.add(titleImg);
        revalidateComponents(1);
    }


    public void howManyPlayers() {
        // Create a text instructions for the user to follow
        JTextArea instructions = new JTextArea();

        // Create the font for the text
        Font font = new Font("Arial", Font.BOLD, 40);
        instructions.setFont(font);

        // Add the text to the instructions
        instructions.append("How many players today?");

        // Set the preferred size such that 40pt text can fit
        // (Only thing on screen means that it can take up everything
        instructions.setBackground(null);
        instructions.setEditable(false);

        components.add(instructions);
        revalidateComponents(1);
    }


    public void createUser() {
        // Create a text instructions for the user to follow
        JTextArea instructions = new JTextArea();

        // Create the font for the text
        Font font = new Font("Arial", Font.BOLD, 40);
        instructions.setFont(font);

        // Add the text to the instructions
        instructions.append("Player " + (cluedoGame.getTempUserNum() + 1) + ", what is your preferred name?");

        // Set the preferred size such that 40pt text can fit
        // (Only thing on screen means that it can take up everything
        instructions.setBackground(null);
        instructions.setEditable(false);

        components.add(instructions);
        revalidateComponents(1);
    }


    public void selectCharacter() {
        // Create a text instructions for the user to follow
        JTextArea instructions = new JTextArea();

        // Create the font for the text
        Font font = new Font("Arial", Font.BOLD, 40);
        instructions.setFont(font);

        // Add the text to the instructions
        instructions.append(cluedoGame.getTempUserName() + ", who are you playing as?");

        // Set the preferred size such that 40pt text can fit
        // (Only thing on screen means that it can take up everything
        instructions.setBackground(null);
        instructions.setEditable(false);

        components.add(instructions);
        revalidateComponents(1);
    }

    public void showHand() {
        // Clear previous settings
        gc = new GridBagConstraints();
        gc.fill = GridBagConstraints.HORIZONTAL;

        int amountOfSprites = 0;
        int amountOfWeapons = 0;
        int amountOfRooms = 0;
        ArrayList<Sprite> spriteCards = new ArrayList<>();
        ArrayList<Weapon> weaponCards = new ArrayList<>();
        ArrayList<Room> roomCards = new ArrayList<>();

        // Go through this users hand and separate all the cards
        for (Card c : cluedoGame.getCurrentUser().getHand()) {
            if (c instanceof Sprite) {
                amountOfSprites++;
                spriteCards.add((Sprite) c);
            } else if (c instanceof Weapon) {
                amountOfWeapons++;
                weaponCards.add((Weapon) c);
            } else if (c instanceof Room) {
                amountOfRooms++;
                roomCards.add((Room) c);
            }
        }


        // Draw all the cards
        int currentGridX = 0;
        gc.gridy = 1;
        gc.weightx = 1;
        gc.weighty = 2;
        gc.gridwidth = 1;
        gc.gridx = 0;
        int cardHeight = getHeight() / 2;
        int cardWidth = getWidth() / 6;
        boolean useWidth = cardHeight > cardWidth;


        for (Sprite s : spriteCards) {
            gc.gridx = currentGridX;
            Sprite.SpriteAlias sa = s.getSpriteAlias();
            JLabel lab = new JLabel(useWidth ?
                    Card.getCardSetWidth(sa, false, cardWidth) :
                    Card.getCardSetHeight(sa, false, cardHeight));
            lab.setToolTipText(s.getSpriteAlias().toString());
            add(lab, gc);
            currentGridX++;
        }
        for (Weapon w : weaponCards) {
            gc.gridx = currentGridX + 1;
            Weapon.WeaponAlias wa = w.getWeaponAlias();
            JLabel lab = new JLabel(useWidth ?
                    Card.getCardSetWidth(wa, false, cardWidth) :
                    Card.getCardSetHeight(wa, false, cardHeight));
            lab.setToolTipText(w.getWeaponAlias().toString());
            add(lab, gc);
            currentGridX++;
        }
        for (Room r : roomCards) {
            gc.gridx = currentGridX + 2;
            Room.RoomAlias ra = r.getRoomAlias();
            JLabel lab = new JLabel(useWidth ?
                    Card.getCardSetWidth(ra, false, cardWidth) :
                    Card.getCardSetHeight(ra, false, cardHeight));
            lab.setToolTipText(r.getRoomAlias().toString());
            add(lab, gc);
            currentGridX++;
        }

        // Create a label for each of the card types
        Font font = new Font("Arial", Font.BOLD, 20);
        JLabel sprites = new JLabel("Sprites");
        sprites.setFont(font);
        JLabel weapons = new JLabel("Weapons");
        weapons.setFont(font);
        JLabel rooms = new JLabel("Rooms");
        rooms.setFont(font);

        // Place the label if there is any cards for it
        currentGridX = 0;
        gc.gridy = 0;
        gc.weighty = 1;
        gc.anchor = GridBagConstraints.CENTER;

        if (amountOfSprites > 0) {
            gc.gridx = currentGridX;
            gc.gridwidth = amountOfSprites;
            add(sprites, gc);
            currentGridX += amountOfSprites;
        }
        if (amountOfWeapons > 0) {
            gc.gridx = currentGridX;
            gc.gridwidth = amountOfWeapons;
            add(weapons, gc);
            currentGridX += amountOfWeapons;
        }
        if (amountOfRooms > 0) {
            gc.gridx = currentGridX + 1;
            gc.gridwidth = amountOfRooms;
            add(rooms, gc);
            currentGridX += amountOfRooms;
        }

        gc.fill = GridBagConstraints.VERTICAL;
        gc.anchor = GridBagConstraints.LINE_START;
        gc.gridx = amountOfSprites;
        gc.gridy = 1;
        gc.weighty = 2;
        gc.weightx = 0.1;
        gc.gridheight = 2;
        JSeparator sep1 = new JSeparator(JSeparator.VERTICAL);
        add(sep1, gc);
        gc.gridx = amountOfSprites + amountOfWeapons + 1;
        JSeparator sep2 = new JSeparator(JSeparator.VERTICAL);
        add(sep2, gc);
    }

    public void showDetectiveCards() {
        // Clear previous settings
        gc = new GridBagConstraints();
        gc.fill = GridBagConstraints.HORIZONTAL;

        HashSet<Sprite> knownSpriteCards = new HashSet<>();
        HashSet<Weapon> knownWeaponCards = new HashSet<>();
        HashSet<Room> knownRoomCards = new HashSet<>();

        // Go through this users observed cards and separate all the cards by type
        for (Card c : cluedoGame.getCurrentUser().getObservedCards()) {
            if (c instanceof Sprite) {
                knownSpriteCards.add((Sprite) c);
            } else if (c instanceof Weapon) {
                knownWeaponCards.add((Weapon) c);
            } else if (c instanceof Room) {
                knownRoomCards.add((Room) c);
            }
        }


        // Draw all the cards
        int currentGridX = 0;
        gc.gridy = 1;
        gc.weightx = 1;
        gc.weighty = 2;
        gc.gridwidth = 1;
        gc.gridx = 0;
        int cardHeight = getHeight() / 4;
        int cardWidth = getWidth() / 6;
        boolean useWidth = cardHeight > cardWidth;

        // Display all the Sprite cards, highlighting those NOT in out observed cards
        for (Sprite s : cluedoGame.getBoard().getSprites().values()){
            gc.gridx = currentGridX % 3;
            gc.gridy = (currentGridX / 3) + 1;
            Sprite.SpriteAlias sa = s.getSpriteAlias();
            JLabel lab = new JLabel(useWidth ?
                    Card.getCardSetWidth(sa, !knownSpriteCards.contains(s), cardWidth) :
                    Card.getCardSetHeight(sa, !knownSpriteCards.contains(s), cardHeight));
            lab.setToolTipText(s.getSpriteAlias().toString());
            add(lab, gc);
            currentGridX++;
        }


        // Display all the Weapon cards, highlighting those NOT in out observed cards
        currentGridX = 0;
        for (Weapon w : cluedoGame.getBoard().getWeapons().values()){
            gc.gridx = (currentGridX % 3) + 4;
            gc.gridy = (currentGridX / 3) + 1;
            Weapon.WeaponAlias wa = w.getWeaponAlias();
            JLabel lab = new JLabel(useWidth ?
                    Card.getCardSetWidth(wa, !knownWeaponCards.contains(w), cardWidth) :
                    Card.getCardSetHeight(wa, !knownWeaponCards.contains(w), cardHeight));
            lab.setToolTipText(w.getWeaponAlias().toString());
            add(lab, gc);
            currentGridX++;
        }

        // Display all the Room cards, highlighting those NOT in out observed cards
        currentGridX = 0;
        for (Room r : cluedoGame.getBoard().getRooms().values()){
            gc.gridx = (currentGridX % 5) + 8;
            gc.gridy = (currentGridX / 5) + 1;
            Room.RoomAlias ra = r.getRoomAlias();
            JLabel lab = new JLabel(useWidth ?
                    Card.getCardSetWidth(ra, !knownRoomCards.contains(r), cardWidth) :
                    Card.getCardSetHeight(ra, !knownRoomCards.contains(r), cardHeight));
            lab.setToolTipText(r.getRoomAlias().toString());
            add(lab, gc);
            currentGridX++;
        }

        // Create a label for each of the card types
        Font font = new Font("Arial", Font.BOLD, 20);
        JLabel sprites = new JLabel("Sprites");
        sprites.setFont(font);
        JLabel weapons = new JLabel("Weapons");
        weapons.setFont(font);
        JLabel rooms = new JLabel("Rooms");
        rooms.setFont(font);

        // Place the label if there is any cards for it
        currentGridX = 0;
        gc.gridy = 0;
        gc.weighty = 0.5;
        gc.anchor = GridBagConstraints.LINE_END;
        int amountOfSprites = 3;
        int amountOfWeapons = 3;
        int amountOfRooms = 5;

        if (amountOfSprites > 0) {
            gc.gridx = currentGridX;
            gc.gridwidth = amountOfSprites;
            add(sprites, gc);
            currentGridX += amountOfSprites;
        }
        if (amountOfWeapons > 0) {
            gc.gridx = currentGridX;
            gc.gridwidth = amountOfWeapons;
            add(weapons, gc);
            currentGridX += amountOfWeapons;
        }
        if (amountOfRooms > 0) {
            gc.gridx = currentGridX + 1;
            gc.gridwidth = amountOfRooms;
            add(rooms, gc);
            currentGridX += amountOfRooms;
        }

        gc.fill = GridBagConstraints.VERTICAL;
        gc.anchor = GridBagConstraints.LINE_START;
        gc.gridx = (cluedoGame.getBoard().getSprites().size() / 2) + 1;
        gc.gridy = 1;
        gc.weighty = 2;
        gc.weightx = 0.1;
        gc.gridheight = 2;
        JSeparator sep1 = new JSeparator(JSeparator.VERTICAL);
        add(sep1, gc);
        gc.gridx = (cluedoGame.getBoard().getSprites().size() / 2) + (cluedoGame.getBoard().getWeapons().size() / 2) + 1;
        JSeparator sep2 = new JSeparator(JSeparator.VERTICAL);
        add(sep2, gc);
    }



    public void confirmShowHiddenContent() {
        // Create a text instructions for the user to follow
        JTextArea instructions = new JTextArea();

        // Create the font for the text
        Font font = new Font("Arial", Font.BOLD, 40);
        instructions.setFont(font);

        // Add the text to the instructions
        instructions.append(cluedoGame.getOtherPlayer().getUserName() + ", you must refute the suggestion");

        // Set the preferred size such that 40pt text can fit
        // (Only thing on screen means that it can take up everything
        instructions.setBackground(null);
        instructions.setEditable(false);

        components.add(instructions);
        revalidateComponents(1);
    }

    public void chooseHiddenPlayerCard() {
        // Create a text instructions for the user to follow
        JTextArea instructions = new JTextArea();

        // Create the font for the text
        Font font = new Font("Arial", Font.BOLD, 40);
        instructions.setFont(font);

        // Add the text to the instructions
        instructions.append(cluedoGame.getOtherPlayer().getUserName() + ", pick a card to show");

        // Set the preferred size such that 40pt text can fit
        // (Only thing on screen means that it can take up everything
        instructions.setBackground(null);
        instructions.setEditable(false);

        components.add(instructions);
        revalidateComponents(1);
    }

    public void confirmShowOtherPlayerCard() {
        // Create a text instructions for the user to follow
        JTextArea instructions = new JTextArea();

        // Create the font for the text
        Font font = new Font("Arial", Font.BOLD, 40);
        instructions.setFont(font);

        // Add the text to the instructions
        instructions.append(cluedoGame.getCurrentUser().getUserName() + ", are you ready to have your suggestion debunked?");

        // Set the preferred size such that 40pt text can fit
        // (Only thing on screen means that it can take up everything
        instructions.setBackground(null);
        instructions.setEditable(false);

        components.add(instructions);
        revalidateComponents(1);
    }

    public void showUserOtherPlayerCard() {
        // Create a text instructions for the user to follow
        JTextArea instructions = new JTextArea();

        // Create the font for the text
        Font font = new Font("Arial", Font.BOLD, 40);
        instructions.setFont(font);

        // Add the text to the instructions
        instructions.append(cluedoGame.getOtherPlayer().getUserName() + " has shown you " + cluedoGame.getOtherPlayerCard().getName());

        // Set the preferred size such that 40pt text can fit
        // (Only thing on screen means that it can take up everything
        instructions.setBackground(null);
        instructions.setEditable(false);

        components.add(instructions);
        revalidateComponents(1);
    }

    public void noSuggestions() {
        // Create a text instructions for the user to follow
        JTextArea instructions = new JTextArea();

        // Create the font for the text
        Font font = new Font("Arial", Font.BOLD, 40);
        instructions.setFont(font);

        // Add the text to the instructions
        instructions.append("Unfortunately, no other player has a card to refute this..... ;)");

        // Set the preferred size such that 40pt text can fit
        // (Only thing on screen means that it can take up everything
        instructions.setBackground(null);
        instructions.setEditable(false);

        components.add(instructions);
        revalidateComponents(1);
    }


    private void drawBorder() {
        Border b = BorderFactory.createTitledBorder(BorderFactory.createMatteBorder(2, 0, 0, 0, accentCol), borderTitle,
                TitledBorder.CENTER, TitledBorder.DEFAULT_POSITION, new Font("Serif", Font.BOLD, 18), accentCol);
        setBorder(b);
        setBackground(baseCol);
    }


    // --------------------------------------------------
    // HELPFUL METHODS
    // --------------------------------------------------


    /**
     * Sets title Text of the Canvas.
     * @param borderTitle new Title
     */
    public void setBorderTitle(String borderTitle) {
        this.borderTitle = borderTitle;
    }

    /**
     * Clears all components wihtin the Canvas.
     */
    public void clearComponents() {
        components.clear();
        removeAll();
    }

    /**
     * Renders all cells within the board.
     * Scales board Icons if change in window size is detected.
     */
    public void renderBoard() {
        int newCellSize = -1 + (Math.min(getWidth() / board.getCols(), getHeight() / board.getRows()));
        board.scaleIcons(newCellSize);
        board.getStream().forEach(cell -> components.add(cell.render()));
        revalidateComponents(board.getCols());
        repaint();
    }

    private void revalidateComponents(int cols) {
        gc = new GridBagConstraints();
        if (components.size() > 0) {
            gc.gridy = 0;
            for (int i = 0; i < components.size(); i++) {
                gc.gridx = i % cols;
                gc.gridy = i / cols;
                if (components.get(i) != null)
                    add(components.get(i), gc);
            }
        } else {
            this.removeAll();
        }
    }

    /**
     * Displays the rules of the game for the user.
     * Can be accessed from the GUI in the Menu Bar.
     */
    protected void displayRules() {
        // Create a text instructions for the user to follow
        JTextArea instructions = new JTextArea();

        // Create the font for the text
        Font font = new Font("Arial", Font.PLAIN, 20);
        instructions.setFont(font);

        // Add the text to the instructions
        instructions.append("Aim:\t\tFigure out the mystery to who murdered the butler, what weapon they used, and in what room."
                + "\nGame:\t\tThe game is turn based. Upon starting, each player is dealt a hand of cards. These cards are secret"
                + "\n\t\tand give you evidence as to who, what and where the murder DIDN'T take place."
                + "\n\t\tOn your turn, you roll two dice and navigate your sprite around the map. To find out what other players have,"
                + "\n\t\tyou will need to make a suggestion about the weapon, room and person. You can only make a suggestion in"
                + "\n\t\ta room, using said room as your piece of evidence. You will also need to suggest a person and weapon which will be "
                + "\n\t\ttransported to that room. If you are accused of a crime, you will be transported to the room of which your accuser is in."
                + "\n\t\tOnce a suggestion has been made, going clockwise, each player is asked to provide ONE piece of evidence to dispute the claim."
                + "\n\t\tIf a piece of evidence is shown, no more evidence is needed to dispute the claim. You can not hold back evidence."
                + "\n\t\tAccusations are used when you would like to solve the murder mystery. Careful though, if you are wrong you"
                + "\n\t\twill be kicked from the game. At this point, all you can do is refute others claims."
                + "\nNavigation:\t\tOn your turn, use the buttons in the 'Control' panel to make actions."
                + "\n\t\tHover over board to see a highlighted path of where you can go, click to then move there.");

        // Set the preferred size such that 40pt text can fit
        // (Only thing on screen means that it can take up everything
        instructions.setBackground(null);
        instructions.setEditable(false);

        components.add(instructions);
        revalidateComponents(1);

    }

}
