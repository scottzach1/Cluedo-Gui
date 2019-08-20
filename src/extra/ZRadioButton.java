package extra;

import game.Sprite;
import gui.Controls;

import javax.swing.*;
import java.awt.*;

public class ZRadioButton extends JRadioButton {

    public ZRadioButton(Sprite s, int fontSize, boolean enabled) {
        super(s.toString());
        setFont(new Font("Arial", Font.PLAIN, fontSize));
        setBackground(Controls.getBaseCol());
        setForeground(Color.WHITE);
        setActionCommand(s.toString());
        setEnabled(enabled);
    }
}
