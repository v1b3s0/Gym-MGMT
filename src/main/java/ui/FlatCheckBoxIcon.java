package ui;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Component;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;

import javax.swing.AbstractButton;
import javax.swing.Icon;

/** Flat white checkbox with a golden fill when selected. */
public class FlatCheckBoxIcon implements Icon {
    private static final int SIZE = 18;

    @Override
    public int getIconWidth() {
        return SIZE;
    }

    @Override
    public int getIconHeight() {
        return SIZE;
    }

    @Override
    public void paintIcon(Component c, Graphics g, int x, int y) {
        Graphics2D g2 = (Graphics2D) g.create();
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

        g2.setColor(Color.WHITE);
        g2.fillRoundRect(x, y, SIZE, SIZE, 5, 5);

        g2.setColor(new Color(140, 140, 140));
        g2.setStroke(new BasicStroke(1f));
        g2.drawRoundRect(x, y, SIZE - 1, SIZE - 1, 5, 5);

        boolean selected = (c instanceof AbstractButton) && ((AbstractButton) c).isSelected();
        if (selected) {
            g2.setColor(AppStyle.GOLD_COLOR);
            g2.fillRoundRect(x + 3, y + 3, SIZE - 6, SIZE - 6, 3, 3);
        }

        g2.dispose();
    }
}
