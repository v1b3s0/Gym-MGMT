package ui;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Component;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Insets;
import java.awt.RenderingHints;

import javax.swing.border.AbstractBorder;

/**
 * A simple rounded line border used across all themed inputs.
 */
public class RoundedBorder extends AbstractBorder {
    private final Color color;
    private final int radius;
    private final int thickness;
    private final Insets insets;

    public RoundedBorder(Color color, int radius, int thickness, Insets insets) {
        this.color = color;
        this.radius = radius;
        this.thickness = thickness;
        this.insets = insets;
    }

    @Override
    public void paintBorder(Component c, Graphics g, int x, int y, int width, int height) {
        Graphics2D g2 = (Graphics2D) g.create();
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        g2.setColor(color);
        g2.setStroke(new BasicStroke(thickness));
        g2.drawRoundRect(x + thickness / 2, y + thickness / 2,
                width - 1 - thickness, height - 1 - thickness, radius, radius);
        g2.dispose();
    }

    @Override
    public Insets getBorderInsets(Component c) {
        return new Insets(insets.top, insets.left, insets.bottom, insets.right);
    }

    @Override
    public Insets getBorderInsets(Component c, Insets i) {
        i.top = insets.top;
        i.left = insets.left;
        i.bottom = insets.bottom;
        i.right = insets.right;
        return i;
    }
}
