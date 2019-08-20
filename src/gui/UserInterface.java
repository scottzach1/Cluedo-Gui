package gui;

import extra.ZButton;
import extra.ZComboBox;
import game.*;

import javax.swing.*;
import javax.swing.border.Border;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Vector;

public class UserInterface extends JPanel {


    // --------------------------------------------------
    // FIELDS
    // --------------------------------------------------

    public static final Color BASE_COL = new Color(76, 74, 75);
    public static final int SMALL_FONT = 10;
    public static final int MEDIUM_FONT = 15;
    public static final int BIG_FONT = 20;

    private String borderTitle = "UI";
    private GridBagConstraints gc;
    private Dimension size;
    private final CluedoGame cluedoGame;
    private final Controls controls;


    // --------------------------------------------------
    // CONSTRUCTOR
    // --------------------------------------------------

    /**
     * Sets up the user interface and it's JPanel settings
     *
     * @param aCluedoGame -
     * @param aControls
     */
    public UserInterface(CluedoGame aCluedoGame, Controls aControls) {
        cluedoGame = aCluedoGame;
        controls = aControls;
        // Set the Size of the Control panel
        setPreferredSize(new Dimension((cluedoGame.getGui().getWidth() * 2 / 3) - 3, (cluedoGame.getGui().getHeight())));

        // Create the boarder
        drawBorder();

        // Set layout
        setLayout(new GridBagLayout());
        gc = new GridBagConstraints();
    }

    /**
     * Creates all the buttons for the game menu for each different player
     * - Show Hand
     * - Detective Cards
     * - Suggest
     * - Which PathFinder to use
     * - Accuse
     * - Skip Turn
     */
    protected void mainPlayerMenu() {
        gc = new GridBagConstraints();

        // Make sure the players name is represented
        setParentTitle();

        // Create all the possible buttons
        JButton showHand = new ZButton("Show Hand (h)", BIG_FONT);
        JButton detectivesNotes = new ZButton("Detectives Note (n)", BIG_FONT);
        JButton suggest = new ZButton("Suggest (s)", BIG_FONT);
        JButton pathFinderSettings = new ZButton(CluedoGame.shortestPath ? "Shortest Path (p)" : "  Exact Path (p)", BIG_FONT);
        JButton accuse = new ZButton("Accuse (a)", BIG_FONT);
        JButton skipTurn = new ZButton("Skip Turn (t)", BIG_FONT);

        // add Show Hand action listener
        showHand.addActionListener(e -> cluedoGame.getGui().showHand());
        // add detective notes action listener
        detectivesNotes.addActionListener(e -> cluedoGame.getGui().showDetectiveCards());
        // Add pathFinderSettings action listener
        pathFinderSettings.addActionListener(e ->
                pathFinderSettings.setText((CluedoGame.shortestPath = !CluedoGame.shortestPath) ? "Shortest Path (p)" : "  Exact Path (p)"));
        // Add action listener for accuse and suggest
        suggest.addActionListener(e -> cluedoGame.getGui().accuseOrSuggest(true));
        accuse.addActionListener(e -> cluedoGame.getGui().accuseOrSuggest(false));

        // add skipTurn action listener
        skipTurn.addActionListener(e -> cluedoGame.nextState());


        // base settings
        gc.fill = GridBagConstraints.BOTH;
        gc.anchor = GridBagConstraints.CENTER;

        // FirstRow
        gc.gridy = 0;
        gc.weighty = 1;
        gc.weightx = 1;
        gc.gridwidth = 1;
        gc.gridheight = 1;

        gc.gridx = 0;
        gc.anchor = GridBagConstraints.LINE_END;
        add(showHand, gc);

        gc.gridx = 1;
        gc.anchor = GridBagConstraints.CENTER;
        add(detectivesNotes, gc);

        gc.gridx = 2;
        gc.anchor = GridBagConstraints.LINE_START;
        add(suggest, gc);

        // Second row
        gc.weighty = 0.5;
        gc.gridy = 1;

        gc.gridx = 0;
        gc.anchor = GridBagConstraints.LINE_END;
        add(pathFinderSettings, gc);

        gc.gridx = 1;
        gc.anchor = GridBagConstraints.CENTER;
        add(accuse, gc);

        gc.gridx = 2;
        gc.anchor = GridBagConstraints.LINE_START;
        add(skipTurn, gc);

    }

    /**
     * Create a general button that allows the user to go back to
     * the game menu. Used when looking at their cards to go back.
     */
    void backOption() {
        // Clear all previous settings
        gc = new GridBagConstraints();

        // Make sure the players name is represented
        setParentTitle();


        // Clear all previous settings
        JButton back = new ZButton("Back", SMALL_FONT);
        back.setPreferredSize(new Dimension(getWidth() / 10, getHeight() / 5));
        back.setFont(new Font("Arial", Font.BOLD, Math.min(Math.min(getWidth() / 2, getHeight() / 2), 20)));

        back.addActionListener(e -> {
            cluedoGame.getGui().gameMenu();
            MenuOptions.setExitRulesFalse();
        });

        add(back, gc);


    }

    /**
     * Creates three JComboBoxes to make a suggestion from. Set those Combo boxes
     * up with their respective cards. A JButton is also added to confirm their selection
     * @param suggestion
     */
    void accuseOrSuggest(boolean suggestion) {
        // Clear all previous settings
        gc = new GridBagConstraints();

        // If the player is not in a room, making a suggestion
        if (cluedoGame.getCurrentUser().getSprite().getPosition().getType() != Cell.Type.ROOM && suggestion) {
            cluedoGame.getGui().setErrorMsg("You are not currently in a room");
            cluedoGame.getGui().printError();
            return;
        }

        // Sprite options
        JComboBox<Sprite.SpriteAlias> spriteOptions = new ZComboBox<>(new Vector<>(cluedoGame.getBoard().getSprites().keySet()), MEDIUM_FONT);
        spriteOptions.setPreferredSize(new Dimension(getWidth() / 6, getHeight() / 6));
        // weapon options
        JComboBox<Weapon.WeaponAlias> weaponOptions = new ZComboBox<>(new Vector<>(cluedoGame.getBoard().getWeapons().keySet()), MEDIUM_FONT);
        weaponOptions.setPreferredSize(new Dimension(getWidth() / 6, getHeight() / 6));
        // Room options if an accusation, sets box if a suggestion
        JComboBox<Room.RoomAlias> roomOptions = new ZComboBox<>(new Vector<>(cluedoGame.getBoard().getRooms().keySet()), MEDIUM_FONT);
        roomOptions.setPreferredSize(new Dimension(getWidth() / 6, getHeight() / 6));
        if (suggestion) {
            roomOptions.setSelectedItem(cluedoGame.getCurrentUser().getSprite().getPosition().getRoom().getRoomAlias());
            roomOptions.setEnabled(false);
        }
        // Create a confirm button
        JButton confirm = new ZButton("Confirm", BIG_FONT);
        confirm.setPreferredSize(new Dimension(getWidth() / 4, getHeight() / 6));
        // Add an action listener to the JButton
        confirm.addActionListener(e -> cluedoGame.getGui().checkAccusationOrSuggestion((Sprite.SpriteAlias) spriteOptions.getSelectedItem(),
                (Weapon.WeaponAlias) weaponOptions.getSelectedItem(), (Room.RoomAlias) roomOptions.getSelectedItem(), suggestion));
        // Create a back button
        JButton back = new ZButton("Back", BIG_FONT);
        back.setPreferredSize(new Dimension(getWidth() / 4, getHeight() / 6));
        // Add an action listener to the back button
        back.addActionListener(e -> cluedoGame.getGui().gameMenu());


        // Add all the components to this panel
        gc.anchor = GridBagConstraints.CENTER;
        gc.insets = new Insets(0, 5, 0, 5);
        gc.gridy = 0;
        gc.gridx = 0;
        add(spriteOptions, gc);

        gc.gridx = 1;
        add(weaponOptions, gc);

        gc.gridx = 2;
        add(roomOptions, gc);

        gc.gridy = 1;
        gc.anchor = GridBagConstraints.LINE_END;
        gc.insets = new Insets(10, 5, 10, 5);
        gc.gridx = 1;
        add(confirm, gc);

        gc.gridx = 2;
        add(back, gc);

    }

    /**
     * Creates a button to confirm that the other user
     * is ready to select and show their cards
     */
    protected void confirmShowHiddenContent() {
        // Clear all previous settings
        gc = new GridBagConstraints();

        JButton confirm = new ZButton("Confirm", BIG_FONT);
        confirm.setPreferredSize(new Dimension(getWidth() / 6, getHeight() / 6));
        confirm.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                cluedoGame.getGui().chooseHiddenPlayerCard();
            }
        });

        gc.gridx = 0;
        gc.gridy = 0;
        add(confirm, gc);
    }

    /**
     * Creates a JComboBox with all the other players cards
     * and a JButton to confirm which card they wish to show the current
     * player
     */
    protected void chooseHiddenPlayerCard() {
        // Clear all previous settings
        gc = new GridBagConstraints();

        // Create a combo box for all the cards to choose from
        // and a button to confirm the selection
        JComboBox<Card> cardsToChooseFrom = new ZComboBox<Card>(MEDIUM_FONT);
        cardsToChooseFrom.setPreferredSize(new Dimension(getWidth() / 6, getHeight() / 6));
        cluedoGame.getOtherPlayerHand().forEach(cardsToChooseFrom::addItem);

        JButton confirm = new ZButton("Confirm", BIG_FONT);
        confirm.setPreferredSize(new Dimension(getWidth() / 6, getHeight() / 6));
        confirm.addActionListener(e -> {
            Card showCard = cardsToChooseFrom.getItemAt(cardsToChooseFrom.getSelectedIndex());

            // Add the card to another player, if still null, then a non card was selected
            if (showCard == null) return;

            cluedoGame.setShowOtherPlayerCard(showCard);
            cluedoGame.getGui().confirmShowOtherPlayerCard();
        });

        gc.gridx = 0;
        gc.gridy = 0;
        add(cardsToChooseFrom, gc);

        gc.gridx = 1;
        add(confirm, gc);

    }

    /**
     * Creates a JButton to continue to show the current player
     * the card the other player is showing them
     */
    protected void confirmShowOtherPlayerCard() {
        // Clear all previous settings
        gc = new GridBagConstraints();

        JButton confirm = new ZButton("Confirm", BIG_FONT);
        confirm.setPreferredSize(new Dimension(getWidth() / 6, getHeight() / 6));
        confirm.addActionListener(e -> cluedoGame.getGui().showUserOtherPlayerCard());

        gc.gridx = 0;
        gc.gridy = 0;
        add(confirm, gc);
    }

    /**
     * Creates a JButton to go to the next user after a successful suggest
     * and also add to their observed cards
     */
    protected void showUserOtherPlayerCard() {
        // Clear all previous settings
        gc = new GridBagConstraints();

        JButton confirm = new ZButton("Cool", BIG_FONT);
        confirm.setPreferredSize(new Dimension(getWidth() / 6, getHeight() / 6));
        confirm.addActionListener(e -> {
            cluedoGame.getCurrentUser().addToObservedCards(cluedoGame.getOtherPlayerCard());
            cluedoGame.nextState();
        });

        gc.gridx = 0;
        gc.gridy = 0;
        add(confirm, gc);
    }

    /**
     * Creates a JButton to go back to the game menu with
     * after a suggestion has returned no results
     */
    protected void noSuggestions() {
        // Clear all previous settings
        gc = new GridBagConstraints();

        JButton confirm = new ZButton("Hmmm...", BIG_FONT);
        confirm.setPreferredSize(new Dimension(getWidth() / 6, getHeight() / 6));
        confirm.addActionListener(e -> cluedoGame.nextState());

        gc.gridx = 0;
        gc.gridy = 0;
        add(confirm, gc);
    }


    /**
     * Draws the JPanels border and sets it's color
     */
    private void drawBorder() {
        Border b1 = BorderFactory.createRaisedBevelBorder();
        Border b2 = BorderFactory.createLoweredBevelBorder();
        Border b3 = BorderFactory.createCompoundBorder(b1, b2);
        setBorder(b3);
        setBackground(BASE_COL);
    }

    /**
     * Sets the controls title, and colors based on the current player
     */
    private void setParentTitle() {
        User currentUser = cluedoGame.getCurrentUser();
        controls.setBorderTitle(currentUser.getUserName() + "'s turn");
        controls.setBaseCol(currentUser.getSprite().getSpriteColor());
        controls.setAccentCol(currentUser.getSprite().getOpposingColor());
    }

    // --------------------------------------------------
    // PUBLIC METHODS
    // --------------------------------------------------

    public void clear() {
        removeAll();
    }

}
