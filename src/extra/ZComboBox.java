package extra;

import gui.UserInterface;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.util.Vector;

public class ZComboBox<T> extends JComboBox<T> implements MouseListener {

    public void initialize(int fontSize) {
        setBackground(UserInterface.BASE_COL.darker());
        setForeground(Color.WHITE);
        setBorder(null);
        setFont(new Font("Arial", Font.BOLD, fontSize));
        addMouseListener(this);
    }

    public ZComboBox(int fontSize) {
        super();
        initialize(fontSize);
    }

    public ZComboBox(Vector<T> options, int fontSize) {
        super(options);
        initialize(fontSize);

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
