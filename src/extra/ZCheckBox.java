package extra;

import gui.UserInterface;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

/**
 * Stylised JCheckbox.
 */
public class ZCheckBox extends JCheckBox implements MouseListener {

    /**
     * Constructs a JTextBox that matches the games design.
     * @param label Text to display on label.
     * @param fontSize size of text on label.
     */
    public ZCheckBox(String label, int fontSize) {
        super(label);
        setBackground(UserInterface.BASE_COL.darker());
        setForeground(Color.WHITE);
        setBorder(null);
        setFont(new Font("Arial", Font.BOLD, fontSize));
        addMouseListener(this);
    }

    @Override
    public void mouseClicked(MouseEvent e) {
        setBackground(UserInterface.BASE_COL.brighter().brighter());
    }

    @Override
    public void mousePressed(MouseEvent e) {
        setBackground(UserInterface.BASE_COL.brighter().brighter());
    }

    @Override
    public void mouseReleased(MouseEvent e) {
        setBackground(UserInterface.BASE_COL.brighter());
    }

    @Override
    public void mouseEntered(MouseEvent e) {
        setBackground(UserInterface.BASE_COL.brighter());
    }

    @Override
    public void mouseExited(MouseEvent e) {
        setBackground(UserInterface.BASE_COL.darker());
    }
}
