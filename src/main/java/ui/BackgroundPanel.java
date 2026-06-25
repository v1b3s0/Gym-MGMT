package ui;

import java.awt.Graphics;
import java.awt.LayoutManager;

import javax.swing.JPanel;

/**
 * Panel that paints the shared (blurred, dark-tinted) app background image.
 * Used by the login screen and the dashboard home.
 */
public class BackgroundPanel extends JPanel {
    public BackgroundPanel(LayoutManager layout) {
        super(layout);
        setOpaque(true);
        setBackground(AppStyle.BACKGROUND_COLOR);
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        AppStyle.paintBackground(g, this);
    }
}
