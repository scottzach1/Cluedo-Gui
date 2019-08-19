package gui;

import game.*;

import javax.swing.*;
import javax.swing.border.Border;
import javax.swing.border.TitledBorder;
import java.awt.*;
import java.awt.event.ComponentEvent;
import java.awt.event.ComponentListener;
import java.util.ArrayList;

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


    public void createUser(int playerNum) {
        // Create a text instructions for the user to follow
        JTextArea instructions = new JTextArea();

        // Create the font for the text
        Font font = new Font("Arial", Font.BOLD, 40);
        instructions.setFont(font);

        // Add the text to the instructions
        instructions.append("Player " + (playerNum + 1) + ", what is your preferred name?");

        // Set the preferred size such that 40pt text can fit
        // (Only thing on screen means that it can take up everything
        instructions.setBackground(null);
        instructions.setEditable(false);

        components.add(instructions);
        revalidateComponents(1);
    }


    public void selectCharacter(String userName) {
        // Create a text instructions for the user to follow
        JTextArea instructions = new JTextArea();

        // Create the font for the text
        Font font = new Font("Arial", Font.BOLD, 40);
        instructions.setFont(font);

        // Add the text to the instructions
        instructions.append(userName + ", who are you playing as?");

        // Set the preferred size such that 40pt text can fit
        // (Only thing on screen means that it can take up everything
        instructions.setBackground(null);
        instructions.setEditable(false);

        components.add(instructions);
        revalidateComponents(1);
    }

    public void showHand(User user) {
        // Clear previous settings
        gc = new GridBagConstraints();
        gc.fill = GridBagConstraints.HORIZONTAL;

        int amountOfSprites = 0;
        int amountOfWeapons = 0;
        int amountOfRooms = 0;
        ArrayList<Sprite> spriteCards = new ArrayList<>();
        ArrayList<Weapon> weaponCards = new ArrayList<>();
        ArrayList<Room> roomCards = new ArrayList<>();

        // Go through this users hand and seperate all the cards
        for (Card c : cluedoGame.getCurrentUser().getHand()){
            if (c instanceof Sprite) {
                amountOfSprites++;
                spriteCards.add((Sprite) c);
            }
            else if (c instanceof Weapon) {
                amountOfWeapons++;
                weaponCards.add((Weapon)c);
            }
            else if (c instanceof Room) {
                amountOfRooms++;
                roomCards.add((Room)c);
            }
        }

        // Create a label for each of the card types
        Font font = new Font("Arial", Font.BOLD, 20);
        JLabel sprites = new JLabel("Sprites");
        sprites.setFont(font);
        JLabel weapons = new JLabel("Weapons");
        weapons.setFont(font);
        JLabel rooms = new JLabel("Rooms");
        rooms.setFont(font);

        // Draw all the cards
        int currentGridX = 0;
        gc.gridy = 1;
        gc.weightx = 1;
        gc.weighty = 2;
        gc.gridwidth = 1;
        gc.gridx = 0;
        int cardHeight = getHeight() / 2;
        int cardWidth = getWidth() / 8;

        for (Sprite s : spriteCards){
            gc.gridx = currentGridX;
            Image img = Card.getCard(s.getSpriteAlias(), false).getImage();
            img = img.getScaledInstance(cardWidth, cardHeight, java.awt.Image.SCALE_SMOOTH);
            JLabel lab = new JLabel(new ImageIcon(img));
            add(lab, gc);
            System.out.println(s.getSpriteAlias().toString() + " " + currentGridX);
            currentGridX++;
        }
        for (Weapon w : weaponCards){
            gc.gridx = currentGridX;
            Image img = Card.getCard(w.getWeaponAlias(), false).getImage();
            img = img.getScaledInstance(cardWidth, cardHeight, java.awt.Image.SCALE_SMOOTH);
            JLabel lab = new JLabel(new ImageIcon(img));
            add(lab, gc);
            System.out.println(w.getWeaponAlias().toString() + " " + currentGridX);
            currentGridX++;
        }
        for (Room r : roomCards){
            gc.gridx = currentGridX;
            Image img = Card.getCard(r.getRoomAlias(), false).getImage();
            img = img.getScaledInstance(cardWidth, cardHeight, java.awt.Image.SCALE_SMOOTH);
            JLabel lab = new JLabel(new ImageIcon(img));
            add(lab, gc);
            System.out.println(r.getRoomAlias().toString() + " " + currentGridX);
            currentGridX++;
        }


        // Place the label if there is any cards for it
        currentGridX = 0;
        gc.gridy = 0;
        gc.weighty = 1;
        gc.anchor = GridBagConstraints.CENTER;

        if (amountOfSprites > 0) {
            gc.gridx = currentGridX;
            gc.gridwidth = amountOfSprites;
            add(sprites, gc);
            System.out.println("Sprite " + gc.gridx + gc.gridwidth);
            currentGridX += amountOfSprites;
        }
        if (amountOfWeapons > 0) {
            gc.gridx = currentGridX;
            gc.gridwidth = amountOfWeapons;
            add(weapons, gc);
            System.out.println("Weapons " + gc.gridx + gc.gridwidth);
            currentGridX += amountOfWeapons;
        }
        if (amountOfRooms > 0) {
            gc.gridx = currentGridX;
            gc.gridwidth = amountOfRooms;
            add(rooms, gc);
            System.out.println("Rooms " + gc.gridx + gc.gridwidth);
            currentGridX += amountOfRooms;
        }



    }

    public void showDetectiveCards(User user) {

    }

    public void confirmShowHiddenContent(User user) {
        // Create a text instructions for the user to follow
        JTextArea instructions = new JTextArea();

        // Create the font for the text
        Font font = new Font("Arial", Font.BOLD, 40);
        instructions.setFont(font);

        // Add the text to the instructions
        instructions.append(user.getUserName() + ", you must refute the suggestion");

        // Set the preferred size such that 40pt text can fit
        // (Only thing on screen means that it can take up everything
        instructions.setBackground(null);
        instructions.setEditable(false);

        components.add(instructions);
        revalidateComponents(1);
    }

    public void chooseHiddenPlayerCard(User user) {
        // Create a text instructions for the user to follow
        JTextArea instructions = new JTextArea();

        // Create the font for the text
        Font font = new Font("Arial", Font.BOLD, 40);
        instructions.setFont(font);

        // Add the text to the instructions
        instructions.append(user.getUserName() + ", pick a card to show");

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
        instructions.append(cluedoGame.getOtherPlayer().getUserName() + " has shown you " + cluedoGame.getShowOtherPlayerCard().getName());

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


    public void setBorderTitle(String borderTitle) {
        this.borderTitle = borderTitle;
    }

    public void clearComponents() {
        components.clear();
        removeAll();
    }

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

    public static void main(String[] args) {
        CluedoGame cluedoGame = new CluedoGame();
        Board b = cluedoGame.getBoard();

        // Highlight some cells to test
        b.highlightedCells.add(b.getCell("O2"));
        b.highlightedCells.add(b.getCell("P2"));
        b.highlightedCells.add(b.getCell("Q2"));
        b.highlightedCells.add(b.getCell("Q3"));
        b.highlightedCells.add(b.getCell("R3"));
        b.highlightedCells.add(b.getCell("R4"));
        b.highlightedCells.add(b.getCell("R5"));
        b.highlightedCells.add(b.getCell("R6"));
        b.highlightedCells.add(b.getCell("S6"));
        b.highlightedCells.addAll(b.getRooms().get(Room.RoomAlias.CONSERVATORY).getCells());

        Canvas c = new Canvas(cluedoGame);

        c.addComponentListener(new ComponentListener() {
            @Override
            public void componentResized(ComponentEvent e) {
                c.clearComponents();
                c.renderBoard();
            }

            @Override
            public void componentMoved(ComponentEvent e) {
            }

            @Override
            public void componentShown(ComponentEvent e) {
            }

            @Override
            public void componentHidden(ComponentEvent e) {
            }
        });

        JFrame frame = new JFrame("Board Demo");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.getContentPane().add(c, BorderLayout.CENTER);

        frame.setResizable(true);
        frame.pack();
        frame.setVisible(true);

        c.renderBoard();
    }
}
