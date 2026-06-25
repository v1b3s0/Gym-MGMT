package ui;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;

import javax.swing.JPanel;

public class RoundedPanel extends JPanel {
    private Color backgroundColor;
    private int cornerRadius;

    public RoundedPanel(Color backgroundColor, int cornerRadius) {
        this.backgroundColor = backgroundColor;
        this.cornerRadius = cornerRadius;

        setOpaque(false);
    }

    @Override
    protected void paintComponent(Graphics g) {
        Graphics2D graphics = (Graphics2D) g.create();

        graphics.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        graphics.setColor(backgroundColor);

        graphics.fillRoundRect(
                0,
                0,
                getWidth(),
                getHeight(),
                cornerRadius,
                cornerRadius);

        graphics.dispose();

        super.paintComponent(g);
    }
}
