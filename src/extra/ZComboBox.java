package extra;

import gui.UserInterface;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.util.Vector;


/**
 * Stylised JComboBox.
 * @param <T>
 */
public class ZComboBox<T> extends JComboBox<T> implements MouseListener {

    /**
     * Constructs a JComboBox that matches the games design.
     * @param fontSize size of text.
     */
    public ZComboBox(int fontSize) {
        super();
        initialize(fontSize);
    }

    /**
     * Constructs a JComboBox that matches the games design.
     * @param options Generic Vector of objects to display.
     * @param fontSize size of text.
     */
    public ZComboBox(Vector<T> options, int fontSize) {
        super(options);
        initialize(fontSize);

    }

    /**
     * Applies games design to style.
     * @param fontSize size of text.
     */
    private void initialize(int fontSize) {
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
