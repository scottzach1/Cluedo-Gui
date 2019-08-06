import java.awt.*;

public class CluedoGame extends GUI {
    @Override
    protected void redraw(Graphics g) {
        Position center = new Position(getDrawingAreaDimension().width / 2, getDrawingAreaDimension().height / 2);

        g.drawRect(center.x - 5, center.y - 5, 10, 10);
    }

    public static void main(String[] args) {
        CluedoGame cluedoGame = new CluedoGame();
    }
}
