package gui;

import game.*;

import javax.swing.*;
import javax.swing.border.Border;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Vector;

public class UserInterface extends JPanel {

    public static enum ERROR_TYPE {
        FALSE_SUGGESTION, FALSE_ACCUSATION, BACK;
    }

    // --------------------------------------------------
    // FIELDS
    // --------------------------------------------------

    private final Color baseCol = new Color(76, 74, 75);

    private String borderTitle = "UI";
    private GridBagConstraints gc;
    private Dimension size;
    private final CluedoGame cluedoGame;
    private final Controls parent;


    // --------------------------------------------------
    // CONSTRUCTOR
    // --------------------------------------------------

    public UserInterface(CluedoGame aCluedoGame, Controls aParent) {
        cluedoGame = aCluedoGame;
        parent = aParent;
        // Set the Size of the Control panel
        setPreferredSize(new Dimension((cluedoGame.getGui().getWidth() * 2 / 3) - 3, (cluedoGame.getGui().getHeight())));

        // Create the boarder
        drawBorder();

        // Set layout
        setLayout(new GridBagLayout());
        gc = new GridBagConstraints();
    }

    protected void mainPlayerMenu() {
        gc = new GridBagConstraints();

        // Make sure the players name is represented
        setParentTitle();

        // Create all the possible buttons
        JButton showHand = new JButton("Show Hand");
        JButton detectivesNotes = new JButton("Detectives Note");
        JButton suggest = new JButton("Suggest");
        JButton pathFinderSettings = new JButton("Shortest Path");
        JButton accuse = new JButton("Accuse (Solve)");
        JButton skipTurn = new JButton("Skip Turn");

        // add Show Hand action listener
        showHand.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                cluedoGame.getGui().showHand();
            }
        });
        // add detective notes action listener
        detectivesNotes.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                cluedoGame.getGui().showDetectiveCards();
            }
        });
        // Add pathFinderSettings action listener
		pathFinderSettings.addActionListener(e ->
				pathFinderSettings.setText((CluedoGame.shortestPath = !CluedoGame.shortestPath) ? "Shortest Path" : "  Exact Path "));
        // add skipTurn action listener
        skipTurn.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                cluedoGame.nextState();
            }
        });


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

    protected void backOption() {
        // Clear all previous settings
        gc = new GridBagConstraints();

        // Make sure the players name is represented
        setParentTitle();


        // Clear all previous settings
        JButton back = new JButton("Back");
        back.setPreferredSize(new Dimension(getWidth() / 10, getHeight() / 5));
        back.setFont(new Font("Arial", Font.BOLD, Math.min(Math.min(getWidth() / 2, getHeight() / 2), 20)));

        back.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                cluedoGame.getGui().gameMenu();
            }
        });

        add(back, gc);


    }

    protected void accuseOrSuggest(boolean suggestion) {
		// Clear all previous settings
		gc = new GridBagConstraints();

		// If the player is not in a room, making a suggestion
        if (cluedoGame.getCurrentUser().getSprite().getPosition().getType() != Cell.Type.ROOM && suggestion) {
            cluedoGame.getGui().printError("You are not currently in a room", ERROR_TYPE.BACK);
            return;
        }

        // Sprite options
        JComboBox spriteOptions = new JComboBox(new Vector<Sprite.SpriteAlias>(cluedoGame.getBoard().getSprites().keySet()));
        spriteOptions.setPreferredSize(new Dimension(getWidth() / 6, getHeight() / 6));
        // weapon options
        JComboBox weaponOptions = new JComboBox(new Vector<Weapon.WeaponAlias>(cluedoGame.getBoard().getWeapons().keySet()));
        spriteOptions.setPreferredSize(new Dimension(getWidth() / 6, getHeight() / 6));
        // Room options if an accusation, sets box if a suggestion
        JComboBox roomOptions = new JComboBox(new Vector<Room.RoomAlias>(cluedoGame.getBoard().getRooms().keySet()));
		spriteOptions.setPreferredSize(new Dimension(getWidth() / 6, getHeight() / 6));
        if (suggestion) {
        	roomOptions.setSelectedItem((Room.RoomAlias) cluedoGame.getCurrentUser().getSprite().getPosition().getRoom().getRoomAlias());
        	roomOptions.setEnabled(false);
        }
        // Create a confirm button
		JButton confirm = new JButton("Confirm");
        confirm.setPreferredSize(new Dimension(getWidth() / 4, getHeight() / 3));
        // Add an action listener to the JButton
		confirm.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				StringBuilder text = new StringBuilder();
				text.append("Confirm your");
				if (suggestion)
					text.append(" suggestion\n");
				else
					text.append(" accusation\n(WARNING: an incorrect guess will mean you lose)\n");

				text.append(((Sprite.SpriteAlias) spriteOptions.getSelectedItem()).toString()
						+ " used the " + ((Weapon.WeaponAlias) weaponOptions.getSelectedItem()).toString()
						+ " in the " + ((Room.RoomAlias) roomOptions.getSelectedItem()).toString());
			}
		});

    }

    protected void falseSuggestion() {

    }

    protected void falseAccusation() {

    }

    private void drawBorder() {
        Border b1 = BorderFactory.createRaisedBevelBorder();
        Border b2 = BorderFactory.createLoweredBevelBorder();
        Border b3 = BorderFactory.createCompoundBorder(b1, b2);
        setBorder(b3);
        setBackground(baseCol);
    }

    private void setParentTitle() {
        User currentUser = cluedoGame.getCurrentUser();
        parent.setBorderTitle(currentUser.getUserName() + "'s turn");
        parent.setBaseCol(currentUser.getSprite().getSpriteColor());
        parent.setAccentCol(currentUser.getSprite().getOpposingColor());
    }

    // --------------------------------------------------
    // PUBLIC METHODS
    // --------------------------------------------------

    public void clear() {
        removeAll();
    }
}
