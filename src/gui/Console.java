package gui;

import game.CluedoGame;

import javax.swing.*;
import javax.swing.border.Border;
import java.awt.*;

public class Console extends JPanel {

    // --------------------------------------------------
    // FIELDS
    // --------------------------------------------------

    private final Color baseCol = new Color(100, 0, 30);

    private GridBagConstraints gc;
    private Dimension size;
    private final CluedoGame cluedoGame;
    private final ImageIcon[] dice = {
            new ImageIcon("dice1.png"),
            new ImageIcon("dice2.png"),
            new ImageIcon("dice3.png"),
            new ImageIcon("dice4.png"),
            new ImageIcon("dice5.png"),
            new ImageIcon("dice6.png")
    };


    // --------------------------------------------------
    // CONSTRUCTOR
    // --------------------------------------------------

    public Console(CluedoGame parent) {
        cluedoGame = parent;

        // Set the Size of the Control panel
        setPreferredSize(new Dimension((cluedoGame.getGui().getWidth() / 3) - 3, (cluedoGame.getGui().getHeight())));

        // Create the boarder
        drawBorder();

        // Set layout
        setLayout(new GridBagLayout());
        gc = new GridBagConstraints();
    }

    protected void drawDice(int dieOne, int dieTwo) {

        gc = new GridBagConstraints();

        // Create the labels for representing them
        JLabel dieOneLabel = new JLabel();
        JLabel dieTwoLabel = new JLabel();
        JTextField movesLeft = new JTextField();

        // Get the image icon for each die
        ImageIcon dieOneIcon = dice[dieOne - 1];
        ImageIcon dieTwoIcon = dice[dieTwo - 1];

        // Get the image for each die
        Image diceOneImg = dieOneIcon.getImage();
        Image dieTwoImg = dieTwoIcon.getImage();

        // Create a dynamic size for the die
        int dieSize = Math.min(getWidth() / 4, getHeight() / 4);

        // resize the image size
        diceOneImg = diceOneImg.getScaledInstance(dieSize, dieSize, java.awt.Image.SCALE_SMOOTH);
        dieTwoImg = dieTwoImg.getScaledInstance(dieSize, dieSize, java.awt.Image.SCALE_SMOOTH);

        // Set the text field text
        movesLeft.setFont(new Font("Arial", Font.BOLD, Math.min(Math.min(getWidth() / 2, getHeight() / 2), 20)));
        movesLeft.setText("Moves Left: " + cluedoGame.getMovesLeft());
        movesLeft.setBackground(baseCol);
        movesLeft.setForeground(Color.WHITE);
        movesLeft.setBorder(null);
        movesLeft.setEditable(false);

        // Add the components
        gc.weightx = 1;
        gc.weighty = 1;

        gc.gridx = 0;
        gc.gridy = 0;
        gc.anchor = GridBagConstraints.LAST_LINE_END;
        gc.insets = new Insets(0, 0, 0, 5);
        add(new JLabel(new ImageIcon(diceOneImg)), gc);

        gc.gridx = 1;
        gc.gridy = 0;
        gc.anchor = GridBagConstraints.LAST_LINE_START;
        gc.insets = new Insets(0, 5, 0, 0);
        add(new JLabel(new ImageIcon(dieTwoImg)), gc);

        gc.weighty = 2;
        gc.gridx = 0;
        gc.gridy = 1;
        gc.gridwidth = 2;
        gc.anchor = GridBagConstraints.CENTER;
        gc.insets = new Insets(0, 0, 0, 0);
        add(movesLeft, gc);

    }

    protected void printError(String errorMsg) {

        gc = new GridBagConstraints();
        gc.anchor = GridBagConstraints.CENTER;
        gc.weighty = 1;
        gc.weightx = 1;
        gc.insets = new Insets(0,10,0,0);

        JTextArea error = new JTextArea(3, 10);
        error.setBackground(null);
        error.setForeground(Color.WHITE);
        error.setBorder(null);
		error.setLineWrap(true);
		error.setWrapStyleWord(true);
        error.setFont(new Font("Arial", Font.PLAIN, 20));
        error.setText(errorMsg);
        error.setEditable(false);

        gc.gridx = 0;
        gc.gridy = 0;
        add(error, gc);
    }


    private void drawBorder() {
        Border b1 = BorderFactory.createRaisedBevelBorder();
        Border b2 = BorderFactory.createLoweredBevelBorder();
        Border b3 = BorderFactory.createCompoundBorder(b1, b2);
        setBorder(b3);
        setBackground(baseCol);
    }

    // --------------------------------------------------
    // PUBLIC METHODS
    // --------------------------------------------------


    public void clear() {
        removeAll();
    }

    protected void redraw(){
        for(Component c : getComponents()){
            c.revalidate();
            c.repaint();
        }
        revalidate();
        repaint();
    }

}
