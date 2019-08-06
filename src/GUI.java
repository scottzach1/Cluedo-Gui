import javax.swing.*;
import javax.swing.border.Border;
import javax.swing.text.DefaultCaret;
import java.awt.*;
import java.awt.event.*;


public abstract class GUI {
    protected abstract void redraw(Graphics g);

    private static final int DEFAULT_WINDOW_HEIGHT = 600;
    private static final int DEFAULT_WINDOW_WIDTH = 600;
    private static final int TEXT_OUTPUT_ROWS = 5;

    private JFrame frame;

    private JPanel controls;
    private JComponent canvas;
    private JTextArea console;

    public GUI() { initialise(); }

    public Dimension getDrawingAreaDimension() {
        return canvas.getSize();
    }

    private void initialise() {
        JButton quit = new JButton("Quit");
        quit.addActionListener((ev) -> System.exit(0));

        controls = new JPanel();
        controls.setLayout(new BoxLayout(controls, BoxLayout.LINE_AXIS));

        Border edge = BorderFactory.createEmptyBorder(5, 5, 5, 5);
        controls.setBorder(edge);

        controls.add(quit);

        canvas = new JComponent() {
            protected void paintComponent(Graphics g) {
                redraw(g);
            }
        };

        canvas.setPreferredSize(new Dimension(DEFAULT_WINDOW_WIDTH, DEFAULT_WINDOW_HEIGHT));
        canvas.setVisible(true);

        canvas.addMouseMotionListener(new MouseMotionAdapter() {
            @Override
            public void mouseDragged(MouseEvent e) {
                super.mouseDragged(e);
            }
        });

        canvas.addMouseWheelListener(new MouseAdapter() {
            public void mouseWheelMoved(MouseWheelEvent e) {
            }
        });

        console = new JTextArea(TEXT_OUTPUT_ROWS, 0);
        console.setLineWrap(true);
        console.setWrapStyleWord(true);
        console.setEditable(false);
        JScrollPane scrollBar = new JScrollPane(console);
        DefaultCaret defaultCaret = (DefaultCaret) console.getCaret();
        defaultCaret.setUpdatePolicy(DefaultCaret.ALWAYS_UPDATE);

        JSplitPane splitPane = new JSplitPane(JSplitPane.VERTICAL_SPLIT);
        splitPane.setDividerSize(5);
        splitPane.setContinuousLayout(true);
        splitPane.setResizeWeight(1);
        splitPane.setTopComponent(canvas);
        splitPane.setBorder(BorderFactory.createEmptyBorder());
        splitPane.setContinuousLayout(true);
        splitPane.setBottomComponent(scrollBar);

        frame = new JFrame("CluedoGame");
        frame.addComponentListener(new ComponentAdapter() {
            public void componentResized(ComponentEvent componentEvent) {
                redraw();
            }
        });

        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setLayout(new BorderLayout());
        frame.add(controls, BorderLayout.NORTH);
        frame.add(splitPane, BorderLayout.CENTER);

        frame.pack();
        frame.setVisible(true);
    }

    private void redraw() { frame.repaint(); }

}