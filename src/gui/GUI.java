package gui;

import game.*;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ComponentEvent;
import java.awt.event.ComponentListener;


/**
 * gui.GUI:
 * - Extends JFrame and Implements Component Listener.
 * - The GUI controls the flow and state of the in-game mechanics.
 * - CluedoGame controls setup as user info and player numbers are needed there.
 */
public class GUI extends JFrame implements ComponentListener {
    // Nothing important
    private static final long serialVersionUID = 1L;

    /**
     * GUIState:
     * - Enum defining the states this GUI can be in.
     * Allows for the frame to be resized and redisplay its current state
     */
    public static enum GUIState {
        ACCUSE, CHOOSE_HIDDEN_PLAYER_CARD, CONFIRM_SHOW_HIDDEN_CONTENT, CONFIRM_SHOW_OTHER_PLAYER_CARD,
        DISPLAY_RULES, GAME_MENU, INFEASIBLE_MOVE, MAIN_MENU, NEW_PLAYER, NO_SUGGESTIONS,
        PRINT_ERROR, SHOW_DETECTIVE_CARDS, SHOW_HAND, SHOW_USER_OTHER_PLAYER_CARD, SUGGEST;
    }


    // --------------------------------------------------
    // FIELDS
    // --------------------------------------------------

    // Dimension of the frame, based on screen size
    private static Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
    public static final int CANVAS_HEIGHT = screenSize.height * 2 / 3;
    public static final int CONTROLS_HEIGHT = screenSize.height / 3;
    public static final int SCREEN_HEIGHT = screenSize.height;
    public static final int SCREEN_WIDTH = screenSize.width;

    // Fields: All the contents of this container
    private Canvas canvas;
    private Controls controls;
    private JMenuBar menuBar;
    private GUIState guiState;
    private String errorMsg;

    // Set up stuff
    private final CluedoGame cluedoGame;

    // --------------------------------------------------
    // CONSTRUCTOR
    // --------------------------------------------------

    /**
     * GUI:
     * - Constructs a JFrame adding a component listener and setting up the size and
     * stability of the game frame.
     * - Holds a private final of CluedoGame to parse UI entered information to and from the
     * games main controller
     *
     * @param aCluedoGame - The CluedoGame Object that created this GUI JFrame.
     */
    public GUI(CluedoGame aCluedoGame) {
        super("CLUEDO GAME");
        cluedoGame = aCluedoGame;

        //addLayoutComponents();

        // Create the frame
        setPreferredSize(screenSize.getSize());
        setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);

        addComponentListener(this);

        // Set to visible and resizable
        setResizable(true);
        setMinimumSize(new Dimension(SCREEN_WIDTH / 5, SCREEN_HEIGHT / 5));
        setVisible(true);
        pack();
    }

    // --------------------------------------------------
    // PUBLIC METHODS
    // --------------------------------------------------

    /**
     * addLayoutComponents:
     * - Creates a Canvas and Control JPanels, adding them to the JFrame.
     */
    public void addLayoutComponents() {
        // Create the canvas and set it's preferred size
        canvas = new Canvas(cluedoGame);
        canvas.setPreferredSize(new Dimension(getWidth(), getHeight() * 2 / 3));
        // Create the controls and set it's preferred size
        controls = new Controls(cluedoGame);
        controls.setPreferredSize(new Dimension(getWidth(), getHeight() / 3));
        // Create the menu bar
        menuBar = new MenuOptions(cluedoGame);
        // Canvas and Controls are updated on screen resize
        // Thus, we need to pack them in before adding anything to the frame, else
        // upon adding, they will refresh and be unable to display.
        //pack();

        // Add panels and menu to frame
        setJMenuBar(menuBar);
        add(canvas, BorderLayout.CENTER);
        add(controls, BorderLayout.SOUTH);
        redraw();
    }


    /**
     * mainMenu:
     * - Displays the main menu by calling the canvas and
     * controls to represent the necessary components to do so.
     */
    public void mainMenu() {
        // Clears anything on the components, canvas and controls
        clearComponents();
        // Invokes the same methods down the chain of components
        canvas.mainMenu();
        controls.mainMenu();
        redraw();
    }

    /**
     * howManyPlayers:
     * - Displays the first part of the game, how many players
     * are playing. This invokes canvas and controls to display
     * relevant information.
     */
    public void howManyPlayers() {
        // Clears anything on the components, canvas and controls
        clearComponents();
        // Invokes the same methods down the chain of components
        canvas.howManyPlayers();
        controls.howManyPlayers();
        // Redraws the new state
        redraw();
    }

    /**
     * createUser:
     * - Displays the user creation menu for each player by invoking
     * the relevant components to show on the canvas and controls.
     */
    public void createUser() {
        // Clears anything on the components, canvas and controls
        clearComponents();
        // Invokes the same methods down the chain of components
        canvas.createUser();
        controls.createUser();
        // Redraws the new state
        redraw();
    }

    /**
     * selectCharacter:
     * - Invoked after gui.createUser has run. Links the created
     * user to a character in the game. This method invokes the
     * canvas and controls to display relevant information.
     */
    public void selectCharacter() {
        // Clears anything on the components, canvas and controls
        clearComponents();
        // Invokes the same methods down the chain of components
        canvas.selectCharacter();
        controls.selectCharacter();
        // Redraws the new state
        redraw();
    }

    /**
     * gameSetup:
     * - This is the last state invoked from the parent CludeoGame.
     * gameSetup invokes the controls panel to add extra control panels to
     * it finalise the creation of the game. Players are set and the game can
     * start afterwards.
     */
    public void gameSetup() {
        // First state to state the game
        guiState = GUIState.NEW_PLAYER;
        // Clears anything on the components, canvas and controls
        clearComponents();
        // Invokes the same methods down the chain of components
        controls.addContainers();
        // Redraws the new state
        redraw();
    }

    /**
     * runGUI:
     * - This state is now the only state being run in game.CluedoGame.
     * it controls which method to run (and thus display) based on the
     * GUIState of this GUI. From here the GUI is now independent of the
     * game.CluedoGame and dictates the displayed screen based on the
     * actions it encounters. This also means upon resizing the screen,
     * the GUI will refresh and resize at it's last known method to run.
     */
    public void runGUI() {
        switch (guiState) {
            case ACCUSE:
                accuseOrSuggest(false);
                break;
            case CHOOSE_HIDDEN_PLAYER_CARD:
                chooseHiddenPlayerCard();
                break;
            case CONFIRM_SHOW_HIDDEN_CONTENT:
                confirmShowHiddenContent();
                break;
            case CONFIRM_SHOW_OTHER_PLAYER_CARD:
                confirmShowOtherPlayerCard();
                break;
            case DISPLAY_RULES:
                displayRules();
                break;
            case GAME_MENU:
                gameMenu();
                break;
            case INFEASIBLE_MOVE:
                infeasibleMove();
                break;
            case MAIN_MENU:
                mainMenu();
                break;
            case NEW_PLAYER:
                newPlayer();
                break;
            case NO_SUGGESTIONS:
                noSuggestions();
                break;
            case PRINT_ERROR:
                printError();
                break;
            case SHOW_DETECTIVE_CARDS:
                showDetectiveCards();
                break;
            case SHOW_HAND:
                showHand();
                break;
            case SHOW_USER_OTHER_PLAYER_CARD:
                showUserOtherPlayerCard();
                break;
            case SUGGEST:
                accuseOrSuggest(true);
                break;
        }
    }

    /**
     * newPlayer:
     * - Called when a players turn has ended, will refresh the screen
     * so that the next player can start their turn.
     */
    public void newPlayer() {
        // Set the state to this method
        guiState = GUIState.NEW_PLAYER;
        // Clears anything on the components, canvas and controls
        clearComponents();
        // Invokes the same methods down the chain of components
        controls.nextPlayer();
        canvas.renderBoard();
        // Redraws the new state
        redraw();
    }

    /**
     * gameMenu:
     * - Called when the player hasn't finished their turn but is returning
     * to their game menu. Similar to the gui.newPlayer method, however
     * this maintains the current player
     */
    public void gameMenu() {
        // Set the state to this method
        guiState = GUIState.GAME_MENU;
        // Clears anything on the components, canvas and controls
        clearComponents();
        // Invokes the same methods down the chain of components
        controls.gameMenu();
        canvas.renderBoard();
        // Redraws the new state
        redraw();
    }

    /**
     * showHand:
     * - Invokes the canvas to represent the players hand they've been delt.
     * Will call the controls backOption to give the user a chance to go back
     * to their game menu.
     */
    public void showHand() {
        // Set the state to this method
        guiState = GUIState.SHOW_HAND;
        // Clears anything on the components, canvas and controls
        clearComponents();
        // Invokes the same methods down the chain of components
        canvas.showHand();
        controls.backOption();
        // Redraws the new state
        redraw();
    }

    /**
     * showDetectiveCards:
     * - Invokes the canvas to represent a view of all the cards, and highlights the
     * which cards the player has seen.
     */
    public void showDetectiveCards() {
        // Set the state to this method
        guiState = GUIState.SHOW_DETECTIVE_CARDS;
        // Clears anything on the components, canvas and controls
        clearComponents();
        // Invokes the same methods down the chain of components
        canvas.showDetectiveCards();
        controls.backOption();
        // Redraws the new state
        redraw();
    }

    /**
     * accuseOrSuggest:
     * - Invokes the canvas and controls to allow the player to make a suggestion
     * or accusation.
     *
     * @param suggestion - boolean to determine if a suggestion of accusation.
     */
    public void accuseOrSuggest(boolean suggestion) {
        // Set the state to this method
        if (suggestion)
            guiState = GUIState.SUGGEST;
        else
            guiState = GUIState.ACCUSE;


        // Clears anything on the components, canvas and controls
        clearComponents();
        // Invokes the same methods down the chain of components
        canvas.renderBoard();
        controls.accuseOrSuggest(suggestion);
        // Redraws the new state
        redraw();
    }

    /**
     * printError:
     * Prints an error that the user has made,
     * i.e trying to move to a place they can not.
     */
    public void printError() {
        // Set the state to this method
        guiState = GUIState.PRINT_ERROR;
        // Clears anything on the components, canvas and controls
        clearComponents();
        // Invokes the same methods down the chain of components
        controls.printError();
        canvas.renderBoard();
        // Redraws the new state
        redraw();
    }

    /**
     * redrawDice:
     * - Invoked to update the displayed players moves after they have
     * made a move.
     */
    public void redrawDice() {
        // Invokes the same methods down the chain of components
        controls.redrawDice();
        // Redraws the new state
        redraw();
    }

    /**
     * confirmShowHiddenContent:
     * - Step 1/4: Confirms that another users cards will be shown
     * to refute a claim/suggestion. This invokes the canvas and
     * controls relevant methods.
     */
    public void confirmShowHiddenContent() {
        // Set the state to this method
        guiState = GUIState.CONFIRM_SHOW_HIDDEN_CONTENT;
        // Clears anything on the components, canvas and controls
        clearComponents();
        // Invokes the same methods down the chain of components
        controls.confirmShowHiddenContent();
        canvas.confirmShowHiddenContent(cluedoGame.getOtherPlayer());
        // Redraws the new state
        redraw();
    }

    /**
     * chooseHiddenPlayerCard:
     * - Step 2/4: Gets the card from the other player to be shown
     * to the accuser. This invokes the canvas and controls relevant methods.
     */
    public void chooseHiddenPlayerCard() {
        // Set the state to this method
        guiState = GUIState.CHOOSE_HIDDEN_PLAYER_CARD;
        // Clears anything on the components, canvas and controls
        clearComponents();
        // Invokes the same methods down the chain of components
        controls.chooseHiddenPlayerCard(cluedoGame.getOtherPlayerHand());
        canvas.chooseHiddenPlayerCard(cluedoGame.getOtherPlayer());
        // Redraws the new state
        redraw();
    }

    /**
     * confirmShowOtherPlayerCard:
     * - Step 3/4: Confirm that the card the other player has chosen
     * is ready to be seen by the right player. This invokes the canvas
     * and controls relevant methods.
     */
    public void confirmShowOtherPlayerCard() {
        // Set the state to this method
        guiState = GUIState.CONFIRM_SHOW_OTHER_PLAYER_CARD;
        // Clears anything on the components, canvas and controls
        clearComponents();
        // Invokes the same methods down the chain of components
        controls.confirmShowOtherPlayerCard();
        canvas.confirmShowOtherPlayerCard();
        // Redraws the new state
        redraw();
    }

    /**
     * showUserOtherPlayerCard:
     * - Step 4/4: Show the accuser the card that refutes their suggestion.
     * This invokes the canvas and controls relevant methods.
     */
    public void showUserOtherPlayerCard() {
        // Set the state to this method
        guiState = GUIState.SHOW_USER_OTHER_PLAYER_CARD;
        // Clears anything on the components, canvas and controls
        clearComponents();
        // Invokes the same methods down the chain of components
        controls.showUserOtherPlayerCard();
        canvas.showUserOtherPlayerCard();
        // Redraws the new state
        redraw();
    }

    /**
     * noSuggestions:
     * - Invoked when this player has made a claim that can not be refuted
     * by any other player. This invokes the canvas and controls relevant methods.
     */
    public void noSuggestions() {
        // Set the state to this method
        guiState = GUIState.NO_SUGGESTIONS;
        // Clears anything on the components, canvas and controls
        clearComponents();
        // Invokes the same methods down the chain of components
        controls.noSuggestions();
        canvas.noSuggestions();
        // Redraws the new state
        redraw();

    }

    /**
     * infeasibleMove:
     * - Invoked when the player is attempting a move that is not
     * allowed due to some rule of the game:
     * 1) Can not retrace your steps this turn.
     * 2) Must move within distance of you dice roll.
     * 3) You can not land or walk over a player, or weapon.
     */
    public void infeasibleMove() {
        // Set the state to this method
        guiState = GUIState.INFEASIBLE_MOVE;
        // Clears anything on the components, canvas and controls
        clearComponents();
        // Invokes the same methods down the chain of components
        setErrorMsg("You can not move there\nYou have " + cluedoGame.getMovesLeft() + " moves left");
        controls.printError();
        canvas.renderBoard();
        // Redraws the new state
        redraw();
    }

    /**
     * displayRules:
     * - Invoked when a user selects 'Rules' from the menu bar.
     */
    public void displayRules() {
        // Set the state to this method
        guiState = GUIState.DISPLAY_RULES;
        // Clears anything on the components, canvas and controls
        clearComponents();
        // Invokes the same methods down the chain of components
        canvas.displayRules();
        controls.backOption();
        // Redraws the new state
        redraw();
    }

    /**
     * checkAccusationOrSuggestion:
     * - Creates a popup dialog that confirms the players suggestion
     *
     * @param s          - The Sprite's Alias being accused
     * @param w          - The Weapons's Alias being accused
     * @param r          - The Rooms's Alias being accused
     * @param suggestion - (True) a suggestion, (False) an accusation
     */
    public void checkAccusationOrSuggestion(Sprite.SpriteAlias s, Weapon.WeaponAlias w, Room.RoomAlias r, boolean suggestion) {
        // Get the sprite, weapon and room
        Sprite guessedSprite = cluedoGame.getBoard().getSprites().get(s);
        Weapon guessedWeapon = cluedoGame.getBoard().getWeapons().get(w);
        Room guessedRoom = cluedoGame.getBoard().getRooms().get(r);

        // Build the JDialog box text
        StringBuilder text = new StringBuilder();
        text.append("Confirm your");
        // If a suggestion, simply say they're making a suggestion
        if (suggestion)
            text.append(" suggestion\n");
            // Else if it is an accusation, print a warning that if they are wrong they will lose.
        else
            text.append(" accusation\n(WARNING: an incorrect guess will mean you lose)\n");

        // Combine the text with the accusation
        text.append(guessedSprite.getSpriteAlias().toString()
                + " used the " + guessedWeapon.getWeaponAlias().toString()
                + " in the " + guessedRoom.getRoomAlias().toString());


        // Button text
        String[] options = {"Yes, Im sure", "No, Go Back"};
        // Create the JOptionPane to confirm or deny their request.
        int choice = JOptionPane.showOptionDialog(null,
                text.toString(),
                "Are you sure?",
                JOptionPane.YES_NO_OPTION,
                JOptionPane.QUESTION_MESSAGE,
                null,
                options,
                options[1]);

        // If they close the window or select no, send them back to their game menu
        if (choice == JOptionPane.CLOSED_OPTION || choice == 1)
            cluedoGame.getGui().gameMenu();
            // If we get here they want to make their claim, check if it is a
            // Suggestion or a Accusation and make the correct steps to invoke the method.
        else if (suggestion) {
            cluedoGame.checkSuggestion(guessedSprite, guessedWeapon, guessedRoom);
        } else {
            cluedoGame.checkAccusation(guessedSprite, guessedWeapon, guessedRoom);
        }
    }

    /**
     * skipUser:
     * - When a player has lost the game, upon their turn, display
     * via a pop up dialog that their turn has been skipped.
     *
     * @param user - The user who's turn is skipped
     */
    public void skipUser(User user) {
        // Button text
        String[] options = {"AWH MAN!"};

        // Generate the text to display
        StringBuilder text = new StringBuilder();
        text.append(user.getUserName() + "'s turn is skipped." +
                "\n They have already lost the game.");

        // Create and display the dialog box to say the players turn has been skipped
        int choice = JOptionPane.showOptionDialog(null,
                text.toString(),
                "LOSER!",
                JOptionPane.OK_OPTION,
                JOptionPane.INFORMATION_MESSAGE,
                null,
                options,
                options[0]);
        cluedoGame.nextState();
    }


    /**
     * displayWinner:
     * - Displays a pop up window when a user has won the game.
     */
    public void displayWinner() {
        // Button options
        String[] options = {"WOOOOO!!!"};

        // Build the string to be put in the window
        StringBuilder text = new StringBuilder();
        text.append(cluedoGame.getCurrentUser().getUserName() + " is the best detective." +
                "\n They have won the game!");

        // Create and display a JOptionPane window with the users name who won
        int choice = JOptionPane.showOptionDialog(null,
                text.toString(),
                "WINNER!",
                JOptionPane.OK_OPTION,
                JOptionPane.INFORMATION_MESSAGE,
                null,
                options,
                options[0]);
        cluedoGame.confirmRestartGame();
    }

    /**
     * displayLoser:
     * - Invoke when a player incorrectly makes an accusation.
     * Creates a pop up window to inform that user they have lost
     */
    public void displayLoser() {
        String[] options = {"WHAT! No way!"};

        StringBuilder text = new StringBuilder();
        text.append(cluedoGame.getCurrentUser().getUserName() + " did NOT guess correctly." +
                "\n They are now unable to win." +
                "\n However, they can still show their cards");

        int choice = JOptionPane.showOptionDialog(null,
                text.toString(),
                "LOSER!",
                JOptionPane.OK_OPTION,
                JOptionPane.INFORMATION_MESSAGE,
                null,
                options,
                options[0]);
        cluedoGame.nextState();
    }

    /**
     * restartGame:
     * - Invokes a pop up menu to confirm restarting the game
     *
     * @return boolean - whether to restart the
     */
    public boolean restartGame() {
        // Button options
        String[] options = {"Heck Yeah!", "Nah, that's enough for me."};

        // Build the text for the JOptionPane
        StringBuilder text = new StringBuilder();
        text.append("Would you like to restart the game?");

        // Create and display the JOptionPane
        int choice = JOptionPane.showOptionDialog(null,
                text.toString(),
                "RESTART?",
                JOptionPane.YES_NO_OPTION,
                JOptionPane.QUESTION_MESSAGE,
                null,
                options,
                options[0]);

        // Return false if they choose not to exit the game, else return true
        if (choice == JOptionPane.CLOSED_OPTION || choice == 1) {
            return false;
        }
        return true;
    }


    /**
     * exitGame:
     * - Invokes a pop up menu to confirm the player wants to exit the game.
     *
     * @return boolean - whether to exit or not
     */
    public boolean exitGame() {

        // Button options
        String[] options = {"Yes please", "Opps, wrong button"};

        // Build the text to be displayed
        StringBuilder text = new StringBuilder();
        text.append("Would you like to exit the game?");

        // Create and display the JOptionPane
        int choice = JOptionPane.showOptionDialog(null,
                text.toString(),
                "QUIT?",
                JOptionPane.YES_NO_OPTION,
                JOptionPane.QUESTION_MESSAGE,
                null,
                options,
                options[0]);

        // (True) = Exit Game, (False) = Don't Exit Game
        if (choice == JOptionPane.CLOSED_OPTION || choice == 1) {
            return false;
        }
        return true;
    }

    // --------------------------------------------------
    // HELPFUL METHODS
    // --------------------------------------------------

    /**
     * setErrorMsg:
     * - Sets the error message to be displayed
     *
     * @param str - The message
     */
    public void setErrorMsg(String str) {
        errorMsg = str;
    }

    /**
     * getErrorMsg:
     * - Gets the string saved in the errorMsg
     *
     * @return String - errorMsg
     */
    public String getErrorMsg() {
        return errorMsg;
    }

    /**
     * setGuiState:
     * - Sets what state the GUI is in
     *
     * @param g - what state to set this GUI to
     */
    public void setGuiState(GUIState g) {
        guiState = g;
    }

    /**
     * redraw():
     * - Invokes Revalidate and repaint
     */
    public void redraw() {
        revalidate();
        repaint();
    }

    /**
     * clearComponents:
     * - If the canvas and controls are not nul, then call their
     * respective clearComponents methods, we dont ever want to
     * removeAll() here as canvas and controls are crucial to the
     * games ability to run and never disappear
     */
    private void clearComponents() {
        if (canvas != null)
            canvas.clearComponents();
        if (controls != null)
            controls.clearComponents();
    }

    /**
     * componentResized:
     * - Invokes clearComponents(), redraw(), and as long as the game is not in
     * the state SETUP_GAME_DESIGN (where adding components to this) cludeoGame.gameController()
     *
     * @param e - The ComponentEvent
     */
    @Override
    public void componentResized(ComponentEvent e) {
        clearComponents();
        if (guiState != null) {
            cluedoGame.gameController();
        }
        // Redraws the new state
        redraw();
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


}
