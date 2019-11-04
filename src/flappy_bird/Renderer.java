package flappy_bird;

import javax.swing.*;
import java.awt.*;
import java.net.MalformedURLException;

public class Renderer extends JPanel {

    private static final long serialVersionID = 1;

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        try {
            Main.repaint(g);
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }
    }
}
