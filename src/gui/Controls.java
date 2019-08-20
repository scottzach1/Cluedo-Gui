package gui;

import extra.ZButton;
import extra.ZRadioButton;
import game.CluedoGame;
import game.Sprite;

import javax.swing.*;
import javax.swing.border.Border;
import javax.swing.border.TitledBorder;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class Controls extends JPanel {

    // --------------------------------------------------
    // FIELDS
    // --------------------------------------------------

    private Color accentCol = Color.WHITE;
    private static Color baseCol = Color.DARK_GRAY;

    private String borderTitle;
    private Console console;
    private UserInterface userInterface;
    private GridBagConstraints gc;
    private final CluedoGame cluedoGame;
    private int dieOne = 0, dieTwo = 0;

    // --------------------------------------------------
    // CONSTRUCTOR
    // --------------------------------------------------

    /**
     * Sets up this JPanel with it's preferred size and
     * draws its own border
     *
     * @param parent - The cluedo game object that creates this
     */
    public Controls(CluedoGame parent) {
        borderTitle = "CONTROLS";
        cluedoGame = parent;

        // Set the Size of the Control panel
        setPreferredSize(new Dimension(GUI.SCREEN_WIDTH, GUI.CONTROLS_HEIGHT));

        drawBorder();

        // Set the layout
        setLayout(new GridBagLayout());
        gc = new GridBagConstraints();
    }

    // --------------------------------------------------
    // PUBLIC METHODS
    // --------------------------------------------------

    /**
     * Creates a button to continue to start the game
     */
    protected void mainMenu() {
        gc = new GridBagConstraints();
        drawBorder();

        gc.gridx = 0;
        gc.gridy = 0;

        int fontSize = Math.min(Math.min(getWidth() / 5, getHeight() / 5), 20);
        JButton play = new ZButton("PLAY", fontSize);
        play.setPreferredSize(new Dimension(getWidth() / 5, getHeight() / 5));
        play.addActionListener(e -> cluedoGame.nextState());

        add(play, gc);
    }

    /**
     * Creates a custom JCheckBox with our own icons for
     * normal and selected boxes. These boxes are placed in a
     * Button group as only one can be selected.
     */
    protected void howManyPlayers() {
        gc = new GridBagConstraints();
        drawBorder();

        // Set up the button group and placement
        ButtonGroup group = new ButtonGroup();
        gc.weightx = 2;
        gc.weighty = 2;
        gc.gridy = 0;

        int checkBoxSize = Math.min(Math.min(getWidth() / 4, getHeight() / 3), 50);

        for (int i = 0; i < 4; i++) {
            // NORMAL IMAGE
            Image image = (new ImageIcon("normal_check_box.png")).getImage();
            image = image.getScaledInstance(checkBoxSize, checkBoxSize, java.awt.Image.SCALE_SMOOTH);
            ImageIcon normal = new ImageIcon(image);
            // SELECTED IMAGE
            Image image2 = (new ImageIcon("selected_check_box.png")).getImage();
            image2 = image2.getScaledInstance(checkBoxSize, checkBoxSize, java.awt.Image.SCALE_SMOOTH);
            ImageIcon selected = new ImageIcon(image2);

            JCheckBox b = new JCheckBox((i + 3) + "");
            b.setIcon(normal);
            b.setSelectedIcon(selected);
            b.setFont(new Font("Arial", Font.PLAIN, Math.min(Math.min(getWidth() / 4, getHeight() / 3), 40)));
            b.setBackground(null);
            b.setForeground(Color.WHITE);
            b.setActionCommand((i + 3) + "");

            group.add(b);

            gc.gridx = i;
            gc.anchor = GridBagConstraints.CENTER;
            add(b, gc);
        }

        // Create the submit button
        gc.fill = GridBagConstraints.BOTH;
        gc.weighty = 1;
        gc.gridx = 0;
        gc.gridy = 1;
        gc.gridwidth = 4;

        ZButton submit = new ZButton("SUBMIT", UserInterface.BIG_FONT);

        submit.addActionListener(arg -> {
            try {
                int players = Integer.parseInt(group.getSelection().getActionCommand());
                cluedoGame.setPlayerAmount(players);
                cluedoGame.nextState();
            } catch (Exception e) {
            }
        });

        add(submit, gc);

    }

    /**
     * Creates a text field such that the current user can
     * generate their own name. A JLabel is added to
     * show the user they are meant to add their name there
     */
    protected void createUser() {
        gc = new GridBagConstraints();
        drawBorder();

        // Create label and text field
        JLabel name = new JLabel("NAME: ");
        JTextField nameField = new JTextField(20);

        // Setup the components
        name.setFont(new Font("Arial", Font.BOLD, Math.min(Math.min(getWidth() / 4, getHeight() / 3), 30)));
        name.setForeground(accentCol);
        nameField.setBackground(null);

        nameField.setFont(new Font("Arial", Font.BOLD, Math.min(Math.min(getWidth() / 4, getHeight() / 3), 30)));
        nameField.setForeground(accentCol);
        nameField.setCaretColor(accentCol);
        nameField.setBackground(baseCol);

        // Add the nameField
        nameField.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent arg0) {
                cluedoGame.setTempUserName(nameField.getText().length() > 0 ? nameField.getText() : "Player " + (cluedoGame.getTempUserNum() + 1));
                cluedoGame.nextState();
            }
        });

        // Add the name
        gc.anchor = GridBagConstraints.LINE_END;
        gc.weightx = 0.5;
        gc.weighty = 0.5;
        gc.insets = new Insets(0, 0, 0, 5);

        gc.gridx = 0;
        gc.gridy = 0;
        add(name, gc);

        // add the field
        gc.anchor = GridBagConstraints.LINE_START;
        gc.insets = new Insets(0, 5, 0, 0);
        gc.gridx = 1;
        add(nameField, gc);

    }

    /**
     * Create a radio button to select a sprite from and
     * a confirmation button so that the current user can set their character
     */
    protected void selectCharacter() {
        gc = new GridBagConstraints();
        drawBorder();

        // Set up the button group and placement
        ButtonGroup group = new ButtonGroup();
        gc.weightx = 2;
        gc.weighty = 2;
        gc.gridy = 0;
        gc.gridx = 0;
        int xPos = 0;

        for (Sprite s : cluedoGame.getBoard().getSprites().values()) {
            Sprite.SpriteAlias sa = s.getSpriteAlias();
            ZRadioButton b = new ZRadioButton(s, UserInterface.BIG_FONT, cluedoGame.getAvailableSprites().contains(sa));

            group.add(b);

            gc.gridx = xPos;
            gc.anchor = GridBagConstraints.CENTER;
            add(b, gc);
            xPos++;
        }

        // Create the submit button
        gc.fill = GridBagConstraints.BOTH;
        gc.weighty = 1;
        gc.gridx = 0;
        gc.gridy = 1;
        gc.gridwidth = 6;

        ZButton submit = new ZButton("SUBMIT", UserInterface.BIG_FONT);

        submit.addActionListener(arg -> {
            try {
                Sprite.SpriteAlias sa = Sprite.SpriteAlias.valueOf(group.getSelection().getActionCommand());
                cluedoGame.setTempSprite(sa);
                cluedoGame.removeAvailableSprite(sa);
                cluedoGame.nextTempUserNum();
            } catch (Exception e) {
            }
        });

        add(submit, gc);

    }

    /**
     * Adds the console and user interface to the controls panel
     */
    protected void addContainers() {
        gc = new GridBagConstraints();
        drawBorder();

        // Create and Add the two panels
        console = new Console(cluedoGame);
        userInterface = new UserInterface(cluedoGame, this);

        gc.fill = GridBagConstraints.BOTH;
        gc.weighty = 1;

        gc.gridy = 0;
        gc.gridx = 0;
        gc.weightx = 1;
        gc.gridwidth = 1;
        gc.insets = new Insets(5, 5, 0, 5);
        add(console, gc);

        gc.gridx = 1;
        gc.weightx = 2;
        gc.gridwidth = 2;
        gc.insets = new Insets(5, 0, 0, 5);
        add(userInterface, gc);
    }

    /**
     * Rerolls the die, sets the moves for the user this turn,
     * calls the gameMenu to display the players main menu
     */
    protected void nextPlayer() {
        dieOne = cluedoGame.rollDie();
        dieTwo = cluedoGame.rollDie();
        cluedoGame.setMovesThisTurn(dieOne + dieTwo);
        gameMenu();
    }

    /**
     * Calls the console and userInterface to setup
     * their own game menu. The Console, draws the dice
     * and the userInterface displays the buttons for the players
     * main menu
     */
    protected void gameMenu() {
        console.drawDice(dieOne, dieTwo);
        userInterface.mainPlayerMenu();
        drawBorder();
    }

    /**
     * Calls the userInterface's backOption,
     * If the user interface is not created or being used,
     * Creates a button that resumes the current state of the game
     */
    protected void backOption() {
        if (userInterface != null)
            userInterface.backOption();
        else {
            // Clear all previous settings
            gc = new GridBagConstraints();

            // Clear all previous settings
            JButton back = new JButton("Back");
            back.setPreferredSize(new Dimension(getWidth() / 10, getHeight() / 5));
            back.setFont(new Font("Arial", Font.BOLD, Math.min(Math.min(getWidth() / 2, getHeight() / 2), 20)));

            back.addActionListener(arg0 -> {
                cluedoGame.gameController();
                MenuOptions.setExitRulesFalse();
            });

            add(back, gc);
        }
        drawBorder();
    }

    /**
     * Calls the userInterface's accuseOrSuggest method
     * Console is blank
     * @param suggestion suggestion true / false
     */
    protected void accuseOrSuggest(boolean suggestion) {
        userInterface.accuseOrSuggest(suggestion);
        drawBorder();
    }

    /**
     * Calls the userInterface's confirmShowHiddenContent method
     * Console is blank
     */
    protected void confirmShowHiddenContent() {
        userInterface.confirmShowHiddenContent();
        drawBorder();
    }

    /**
     * Calls the userInterface's chooseHiddenPlayerCard method
     * Console is blank
     */
    protected void chooseHiddenPlayerCard() {
        userInterface.chooseHiddenPlayerCard();
        drawBorder();
    }

    /**
     * Calls the userInterface's confirmShowOtherPlayerCard method
     * Console is blank
     */
    protected void confirmShowOtherPlayerCard() {
        userInterface.confirmShowOtherPlayerCard();
        drawBorder();
    }

    /**
     * Calls the userInterface's showUserOtherPlayerCard method
     * Console is blank
     */
    protected void showUserOtherPlayerCard() {
        userInterface.showUserOtherPlayerCard();
        drawBorder();
    }

    /**
     * Calls the user interface's no suggestion method set up.
     * Console is blank here
     */
    protected void noSuggestions() {
        userInterface.noSuggestions();
        drawBorder();
    }

    /**
     * Calls the user interface and console to
     * their print error set ups
     */
    protected void printError() {
        console.printError();
        userInterface.backOption();
    }

    /**
     * Redraws the dice on the console panel
     * No need to roll the dice again
     */
    protected void redrawDice() {
        console.clear();
        console.drawDice(dieOne, dieTwo);
    }

    // --------------------------------------------------
    // HELPFUL METHODS
    // --------------------------------------------------

    /**
     * Sets the text represented up the top of the border
     * @param borderTitle Title of border
     */
    public void setBorderTitle(String borderTitle) {
        this.borderTitle = borderTitle;
    }

    /**
     * sets the base color (the background color of the controls panel)
     *
     * @param c - The color to set the background color to
     */
    public void setBaseCol(Color c) {
        baseCol = c;
    }

    /**
     * sets the controls accent color (the color of the bar and title
     * up the top)
     *
     * @param c - The color to set the accent color
     */
    public void setAccentCol(Color c) {
        accentCol = c;
    }

    /**
     * Draws the border for this JPanel, it also sets it's title
     */
    private void drawBorder() {
        // Create the boarder
        Border border = BorderFactory.createTitledBorder(BorderFactory.createMatteBorder(2, 0, 0, 0, accentCol), borderTitle,
                TitledBorder.CENTER, TitledBorder.DEFAULT_POSITION, new Font("Serif", Font.BOLD, Math.min(Math.min(getWidth() / 2, getHeight() / 2), 20)), accentCol);
        setBorder(border);
        setBackground(baseCol);
    }

    /**
     * clears the components from the the Canvas and Controls if they exist
     * else remove all the components from this JPanel.
     */
    public void clearComponents() {
        if (console != null)
            console.clear();
        if (userInterface != null)
            userInterface.clear();
        if (console == null && userInterface == null)
            removeAll();
    }

    public static Color getBaseCol() {
        return baseCol;
    }


}
