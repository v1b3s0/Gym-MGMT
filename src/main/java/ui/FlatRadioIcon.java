package ui;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Component;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;

import javax.swing.AbstractButton;
import javax.swing.Icon;

/** Flat white radio button with a golden dot when selected. */
public class FlatRadioIcon implements Icon {
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
        g2.fillOval(x, y, SIZE, SIZE);

        g2.setColor(new Color(140, 140, 140));
        g2.setStroke(new BasicStroke(1f));
        g2.drawOval(x, y, SIZE - 1, SIZE - 1);

        boolean selected = (c instanceof AbstractButton) && ((AbstractButton) c).isSelected();
        if (selected) {
            g2.setColor(AppStyle.GOLD_COLOR);
            g2.fillOval(x + 4, y + 4, SIZE - 8, SIZE - 8);
        }

        g2.dispose();
    }
}
