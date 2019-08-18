package gui;

import game.*;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ComponentEvent;
import java.awt.event.ComponentListener;
import java.util.ArrayList;

public class GUI extends JFrame implements ComponentListener {
    // Nothing important
    private static final long serialVersionUID = 1L;

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

    // Set up stuff
    private final CluedoGame cluedoGame;
    private final Board board;

    // --------------------------------------------------
    // CONSTRUCTOR
    // --------------------------------------------------

    public GUI(CluedoGame aCluedoGame) {
        super("CLUEDO GAME");
        cluedoGame = aCluedoGame;
        board = cluedoGame.getBoard();

        // Create the frame
        setPreferredSize(screenSize.getSize());
        setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);

        addComponentListener(this);

        // Set to visible and resizable
        setResizable(true);
        setVisible(true);
        pack();
    }

    // --------------------------------------------------
    // PUBLIC METHODS
    // --------------------------------------------------

    public void addLayoutComponents() {
        // Create the layout
        canvas = new Canvas(cluedoGame);
        controls = new Controls(cluedoGame);
        menuBar = new MenuOptions(cluedoGame);

        // Add panels to frame
        setJMenuBar(menuBar);
        add(canvas, BorderLayout.CENTER);
        add(controls, BorderLayout.SOUTH);
        redraw();
    }


    // Display the main menu
    public void mainMenu() {
        clear();
        canvas.mainMenu();
        controls.mainMenu();
        redraw();
    }

    // Displays the main menu for each panel
    public void howManyPlayers() {
        clear();
        canvas.howManyPlayers();
        controls.howManyPlayers();
        redraw();
    }

    public void createUser(int tempUserNum) {
        clear();
        canvas.createUser(tempUserNum);
        controls.createUser(tempUserNum);
        redraw();
    }

    public void selectCharacter(String tempUserName) {
        clear();
        canvas.selectCharacter(tempUserName);
        controls.selectCharacter(tempUserName);
        redraw();
    }

    public void gameSetup() {
        clear();
        controls.addContainers();
        redraw();
    }

    public void newPlayer() {
        // Clear everything in the canvas and controls
        clearComponents();
        // Redraw everything
        controls.nextPlayer();
        canvas.renderBoard();
        redraw();
    }

    public void gameMenu() {
        // Clear everything in the canvas and controls
        clearComponents();
        // Redraw everything
        controls.gameMenu();
        canvas.renderBoard();
        redraw();
    }

    public void showHand() {
        clearComponents();
        canvas.showHand(cluedoGame.getCurrentUser());
        controls.backOption();
        redraw();
    }

    public void showDetectiveCards() {
        clearComponents();
        canvas.showDetectiveCards(cluedoGame.getCurrentUser());
        controls.backOption();
        redraw();
    }

    public void accuseOrSuggest(boolean suggestion) {
        clearComponents();
        canvas.renderBoard();
        controls.accuseOrSuggest(suggestion);
        redraw();
    }

    public void printError(String errorMsg) {
        clearComponents();
        controls.printError(errorMsg);
        canvas.renderBoard();
        redraw();
    }

    public void checkAccusationOrSuggestion(Sprite.SpriteAlias s, Weapon.WeaponAlias w, Room.RoomAlias r, boolean suggestion){
		// Get the sprite, weapon and room
		Sprite guessedSprite = cluedoGame.getBoard().getSprites().get(s);
		Weapon guessedWeapon = cluedoGame.getBoard().getWeapons().get(w);
		Room guessedRoom = cluedoGame.getBoard().getRooms().get(r);

		// Build the JDialog box text
		StringBuilder text = new StringBuilder();
		text.append("Confirm your");
		if (suggestion)
			text.append(" suggestion\n");
		else
			text.append(" accusation\n(WARNING: an incorrect guess will mean you lose)\n");

		text.append(guessedSprite.getSpriteAlias().toString()
				+ " used the " + guessedWeapon.getWeaponAlias().toString()
				+ " in the " + guessedRoom.getRoomAlias().toString());

		String[] options = {"Yes, Im sure", "No, Go Back"};
		int choice = JOptionPane.showOptionDialog(null,
				text.toString(),
				"Are you sure?",
				JOptionPane.YES_NO_OPTION,
				JOptionPane.QUESTION_MESSAGE,
				null,
				options,
				options[1]);

		if (choice == JOptionPane.CLOSED_OPTION || choice == 1)
			cluedoGame.getGui().gameMenu();
		else if (suggestion) {
			cluedoGame.checkSuggestion(guessedSprite, guessedWeapon, guessedRoom);
		}
		else {
			cluedoGame.checkAccusation(guessedSprite, guessedWeapon, guessedRoom);
		}
	}

	public void confirmShowHiddenContent(User user){
    	clearComponents();
    	controls.confirmShowHiddenContent();
		canvas.confirmShowHiddenContent(user);
    	redraw();
	}

	public void chooseHiddenPlayerCard(User user, ArrayList<Card> cards){
    	clearComponents();
    	controls.chooseHiddenPlayerCard(cards);
    	canvas.chooseHiddenPlayerCard(user);
    	redraw();
	}

	public void confirmShowOtherPlayerCard(){
    	clearComponents();
    	controls.confirmShowOtherPlayerCard();
    	canvas.confirmShowOtherPlayerCard();
    	redraw();
	}

	public void showUserOtherPlayerCard(){
		clearComponents();
		controls.showUserOtherPlayerCard();
		canvas.showUserOtherPlayerCard();
		redraw();
	}

	public void noMovesLeft(User user){
		clearComponents();
		controls.printError("You can not move there\nYou have " + cluedoGame.getMovesLeft() + " moves left");
		canvas.renderBoard();
		redraw();
	}

	public void skipUser(User user){
		String[] options = {"AWH MAN!"};

		StringBuilder text = new StringBuilder();
		text.append(user.getUserName() + "'s turn is skipped." +
				"\n They have already lost the game.");

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

	public void displayWinner(User user){
		String[] options = {"WOOOOO!!!"};

		StringBuilder text = new StringBuilder();
		text.append(user.getUserName() +" is the best detective." +
				"\n They have won the game!");

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

	public void displayLoser(User user){
		String[] options = {"WHAT! No way!"};

		StringBuilder text = new StringBuilder();
		text.append(user.getUserName() +" did NOT guess correctly." +
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

	public boolean restartGame(){

		String[] options = {"Heck Yeah!", "Nah, that's enough for me."};

		StringBuilder text = new StringBuilder();
		text.append("Would you like to restart the game?");

		int choice = JOptionPane.showOptionDialog(null,
				text.toString(),
				"RESTART?",
				JOptionPane.YES_NO_OPTION,
				JOptionPane.QUESTION_MESSAGE,
				null,
				options,
				options[0]);

		if (choice == JOptionPane.CLOSED_OPTION || choice == 1){
			return false;
		}
		return true;
	}


	public boolean exitGame(){

		String[] options = {"Yes please", "Opps, wrong button"};

		StringBuilder text = new StringBuilder();
		text.append("Would you like to exit the game?");

		int choice = JOptionPane.showOptionDialog(null,
				text.toString(),
				"QUIT?",
				JOptionPane.YES_NO_OPTION,
				JOptionPane.QUESTION_MESSAGE,
				null,
				options,
				options[0]);

		if (choice == JOptionPane.CLOSED_OPTION || choice == 1){
			return false;
		}
		return true;
	}

    // --------------------------------------------------
    // HELPFUL METHODS
    // --------------------------------------------------
    public Canvas getCanvas() {
        return canvas;
    }

    public void setCanvas(Canvas canvas) {
        this.canvas = canvas;
    }

    public Controls getControls() {
        return controls;
    }

    public void setControls(Controls controls) {
        this.controls = controls;
    }

    public void redraw() {
        revalidate();
        repaint();
    }

    public void clear() {
        canvas.clear();
        controls.clear();
    }

    public void clearComponents() {
        canvas.clearComponents();
        controls.clearComponents();
    }

    @Override
    public void componentResized(ComponentEvent e) {
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
