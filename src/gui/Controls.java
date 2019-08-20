package gui;

import extra.ZButton;
import extra.ZComboBox;
import game.Card;
import game.CluedoGame;
import game.Sprite;

import javax.swing.*;
import javax.swing.border.Border;
import javax.swing.border.TitledBorder;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.Vector;

public class Controls extends JPanel {

    // --------------------------------------------------
    // FIELDS
    // --------------------------------------------------

    private Color accentCol = Color.WHITE;
    private Color baseCol = Color.DARK_GRAY;

    private String borderTitle;
    private Console console;
    private UserInterface userInterface;
    private GridBagConstraints gc;
    private final CluedoGame cluedoGame;
    private int dieOne = 0, dieTwo = 0;

    // --------------------------------------------------
    // CONSTRUCTOR
    // --------------------------------------------------

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
     * mainMenu:
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
     * howManyPlayers:
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

        JButton submit = new ZButton("SUBMIT", 20);

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
                cluedoGame.setTempUserName(nameField.getText());
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

    protected void selectCharacter() {
        gc = new GridBagConstraints();
        drawBorder();

        // Create drop down menu
        JComboBox<Sprite.SpriteAlias> spriteOptions = new ZComboBox<>(new Vector<>(cluedoGame.getAvailableSprites()), 20);
        spriteOptions.setPreferredSize(new Dimension(getWidth() / 6, getHeight() / 6));

        // Create submit button
        JButton submit = new ZButton("SUBMIT", 20);
        submit.setPreferredSize(new Dimension(getWidth() / 6, getHeight() / 6));

        submit.addActionListener(e -> {
            cluedoGame.setTempSprite((Sprite.SpriteAlias) spriteOptions.getSelectedItem());
            cluedoGame.removeAvailableSprite((Sprite.SpriteAlias) spriteOptions.getSelectedItem());
            cluedoGame.nextTempUserNum();
        });

        gc.weightx = 1;
        gc.weighty = 1;
        // add the components to their locations
        gc.gridx = 0;
        gc.gridy = 0;
        gc.insets = new Insets(0, 0, 0, 10);
        gc.anchor = GridBagConstraints.LINE_END;
        add(spriteOptions, gc);
        gc.gridx = 1;
        gc.gridy = 0;
        gc.insets = new Insets(0, 10, 0, 0);
        gc.anchor = GridBagConstraints.LINE_START;
        add(submit, gc);

    }

    /**
     * addContainers:
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
     *
     */
    protected void nextPlayer() {
        dieOne = cluedoGame.rollDie();
        dieTwo = cluedoGame.rollDie();
        cluedoGame.setMovesThisTurn(dieOne + dieTwo);
        console.drawDice(dieOne, dieTwo);
        userInterface.mainPlayerMenu();
        drawBorder();

    }

    protected void gameMenu() {
        console.drawDice(dieOne, dieTwo);
        userInterface.mainPlayerMenu();
        drawBorder();
    }

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

            back.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    cluedoGame.getGui().mainMenu();
                    MenuOptions.setExitRulesFalse();
                }
            });

            add(back, gc);
        }
        drawBorder();
    }

    protected void accuseOrSuggest(boolean suggestion) {
        userInterface.accuseOrSuggest(suggestion);
        drawBorder();
    }

    protected void confirmShowHiddenContent() {
        userInterface.confirmShowHiddenContent();
        drawBorder();
    }

    protected void chooseHiddenPlayerCard() {
        userInterface.chooseHiddenPlayerCard();
        drawBorder();
    }

    protected void confirmShowOtherPlayerCard() {
        userInterface.confirmShowOtherPlayerCard();
        drawBorder();
    }

    protected void showUserOtherPlayerCard() {
        userInterface.showUserOtherPlayerCard();
        drawBorder();
    }

    protected  void noSuggestions(){
        userInterface.noSuggestions();
        drawBorder();
    }

    protected void printError() {
        console.printError();
        userInterface.backOption();
    }

    protected void redrawDice() {
        console.clear();
        console.drawDice(dieOne, dieTwo);
    }

    // --------------------------------------------------
    // HELPFUL METHODS
    // --------------------------------------------------

    /**
     * setBorderTitle:
     */
    public void setBorderTitle(String borderTitle) {
        this.borderTitle = borderTitle;
    }

    public void setBaseCol(Color c) {
        baseCol = c;
    }

    public void setAccentCol(Color c) {
        accentCol = c;
    }

    private void drawBorder() {
        // Create the boarder
        Border border = BorderFactory.createTitledBorder(BorderFactory.createMatteBorder(2, 0, 0, 0, accentCol), borderTitle,
                TitledBorder.CENTER, TitledBorder.DEFAULT_POSITION, new Font("Serif", Font.BOLD, Math.min(Math.min(getWidth() / 2, getHeight() / 2), 20)), accentCol);
        setBorder(border);
        setBackground(baseCol);
    }

    /**
     * clear:
     */
    public void clearComponents() {
        if (console != null)
            console.clear();
        if (userInterface != null)
            userInterface.clear();
        if (console == null && userInterface == null)
            removeAll();
    }

}
