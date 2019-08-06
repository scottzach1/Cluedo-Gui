import java.awt.*;

public class CluedoGame extends GUI {
    @Override
    protected void redraw(Graphics g) {
        g.drawString("Hello", 15,15);
    }

    public static void main(String[] args) {
        CluedoGame cluedoGame = new CluedoGame();
    }
}
