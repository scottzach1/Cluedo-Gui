package extra;

import game.Sprite;
import gui.Controls;

import javax.swing.*;
import java.awt.*;

/**
 * Stylized JRadioButton
 */
public class ZRadioButton extends JRadioButton {

    /**
     * Constructs a JTextBox that matches the games design.
     * @param s sprite
     * @param fontSize fontsize
     * @param enabled enabled
     */
    public ZRadioButton(Sprite s, int fontSize, boolean enabled) {
        super(s.toString());
        setFont(new Font("Arial", Font.PLAIN, fontSize));
        setBackground(Controls.getBaseCol());
        setForeground(Color.WHITE);
        setActionCommand(s.toString());
        setEnabled(enabled);
    }
}
