package gui;

import game.*;

import javax.swing.*;
import javax.swing.border.Border;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.Vector;

public class UserInterface extends JPanel {


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
				pathFinderSettings.setText((cluedoGame.shortestPath = !cluedoGame.shortestPath) ? "Shortest Path" : "  Exact Path "));
		// Add action listener for accuse and suggest
		suggest.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				cluedoGame.getGui().accuseOrSuggest(true);
			}
		});
		accuse.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				cluedoGame.getGui().accuseOrSuggest(false);
			}
		});

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
                MenuOptions.setExitRulesFalse();
            }
        });

        add(back, gc);


    }

    protected void accuseOrSuggest(boolean suggestion) {
		// Clear all previous settings
		gc = new GridBagConstraints();

		// If the player is not in a room, making a suggestion
        if (cluedoGame.getCurrentUser().getSprite().getPosition().getType() != Cell.Type.ROOM && suggestion) {
            cluedoGame.getGui().printError("You are not currently in a room");
            return;
        }

        // Sprite options
        JComboBox spriteOptions = new JComboBox(new Vector<Sprite.SpriteAlias>(cluedoGame.getBoard().getSprites().keySet()));
        spriteOptions.setPreferredSize(new Dimension(getWidth() / 6, getHeight() / 6));
        // weapon options
        JComboBox weaponOptions = new JComboBox(new Vector<Weapon.WeaponAlias>(cluedoGame.getBoard().getWeapons().keySet()));
		weaponOptions.setPreferredSize(new Dimension(getWidth() / 6, getHeight() / 6));
        // Room options if an accusation, sets box if a suggestion
        JComboBox roomOptions = new JComboBox(new Vector<Room.RoomAlias>(cluedoGame.getBoard().getRooms().keySet()));
		roomOptions.setPreferredSize(new Dimension(getWidth() / 6, getHeight() / 6));
        if (suggestion) {
        	roomOptions.setSelectedItem((Room.RoomAlias) cluedoGame.getCurrentUser().getSprite().getPosition().getRoom().getRoomAlias());
        	roomOptions.setEnabled(false);
        }
        // Create a confirm button
		JButton confirm = new JButton("Confirm");
        confirm.setPreferredSize(new Dimension(getWidth() / 4, getHeight() / 6));
        // Add an action listener to the JButton
		confirm.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				cluedoGame.getGui().checkAccusationOrSuggestion((Sprite.SpriteAlias) spriteOptions.getSelectedItem(),
						(Weapon.WeaponAlias) weaponOptions.getSelectedItem(), (Room.RoomAlias) roomOptions.getSelectedItem(), suggestion);
			}
		});
		// Create a back button
		JButton back = new JButton("Back");
		back.setPreferredSize(new Dimension(getWidth() / 4, getHeight() / 6));
		// Add an action listener to the back button
		back.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				cluedoGame.getGui().gameMenu();
			}
		});


		// Add all the components to this panel
		gc.anchor = GridBagConstraints.CENTER;
		gc.insets = new Insets(0, 5,0,5);
		gc.gridy = 0;
		gc.gridx = 0;
		add(spriteOptions, gc);

		gc.gridx = 1;
		add(weaponOptions, gc);

		gc.gridx = 2;
		add(roomOptions, gc);

		gc.gridy = 1;
		gc.anchor = GridBagConstraints.LINE_END;
		gc.insets = new Insets(10, 5,10,5);
		gc.gridx = 1;
		add(confirm, gc);

		gc.gridx = 2;
		add(back, gc);

    }

    protected void confirmShowHiddenContent(){
		// Clear all previous settings
		gc = new GridBagConstraints();

		JButton confirm = new JButton("Confirm");
		confirm.setPreferredSize(new Dimension(getWidth()/6, getHeight()/6));
		confirm.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				cluedoGame.chooserHiddenCard();
			}
		});

		gc.gridx = 0;
		gc.gridy = 0;
		add(confirm, gc);
	}

    protected void chooseHiddenPlayerCard(ArrayList<Card> cards){
		// Clear all previous settings
		gc = new GridBagConstraints();

    	// Create a combo box for all the cards to choose from
		// and a button to confirm the selection
		JComboBox cardsToChooseFrom = new JComboBox(new Vector<Card>(cards));
		cardsToChooseFrom.setPreferredSize(new Dimension(getWidth()/6, getHeight()/6));

		JButton confirm = new JButton("Confirm");
		confirm.setPreferredSize(new Dimension(getWidth()/6, getHeight()/6));
		confirm.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				Card showCard = (Card) cardsToChooseFrom.getSelectedItem();
				cluedoGame.setShowOtherPlayerCard(showCard);
				cluedoGame.getGui().confirmShowOtherPlayerCard();
			}
		});

		gc.gridy = 0;
		gc.gridy = 0;
		add(cardsToChooseFrom, gc);

		gc.gridx = 1;

	}

	protected void confirmShowOtherPlayerCard(){
		// Clear all previous settings
		gc = new GridBagConstraints();

		JButton confirm = new JButton("Confirm");
		confirm.setPreferredSize(new Dimension(getWidth()/6, getHeight()/6));
		confirm.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				cluedoGame.getGui().showUserOtherPlayerCard();
			}
		});

		gc.gridx = 0;
		gc.gridy = 0;
		add(confirm, gc);
	}

	protected  void showUserOtherPlayerCard(){
		// Clear all previous settings
		gc = new GridBagConstraints();

		JButton confirm = new JButton("Cool");
		confirm.setPreferredSize(new Dimension(getWidth()/6, getHeight()/6));
		confirm.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				cluedoGame.getCurrentUser().addToObservedCards(cluedoGame.getShowOtherPlayerCard());
				cluedoGame.nextState();
			}
		});

		gc.gridx = 0;
		gc.gridy = 0;
		add(confirm, gc);
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
