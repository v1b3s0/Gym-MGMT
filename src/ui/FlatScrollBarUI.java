package ui;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.awt.RenderingHints;

import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.plaf.ComponentUI;
import javax.swing.plaf.basic.BasicScrollBarUI;

/**
 * Flat, modern scrollbar: grey thumb on a grey track, flat arrow buttons.
 */
public class FlatScrollBarUI extends BasicScrollBarUI {
    public static ComponentUI createUI(JComponent c) {
        return new FlatScrollBarUI();
    }

    @Override
    protected void configureScrollBarColors() {
        thumbColor = AppStyle.SCROLL_THUMB_COLOR;
        trackColor = AppStyle.SCROLL_TRACK_COLOR;
    }

    @Override
    protected JButton createDecreaseButton(int orientation) {
        return new ArrowButton(orientation);
    }

    @Override
    protected JButton createIncreaseButton(int orientation) {
        return new ArrowButton(orientation);
    }

    @Override
    protected void paintTrack(Graphics g, JComponent c, Rectangle trackBounds) {
        g.setColor(AppStyle.SCROLL_TRACK_COLOR);
        g.fillRect(trackBounds.x, trackBounds.y, trackBounds.width, trackBounds.height);
    }

    @Override
    protected void paintThumb(Graphics g, JComponent c, Rectangle thumbBounds) {
        if (thumbBounds.isEmpty() || !scrollbar.isEnabled()) {
            return;
        }
        Graphics2D g2 = (Graphics2D) g.create();
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        g2.setColor(AppStyle.SCROLL_THUMB_COLOR);
        g2.fillRoundRect(thumbBounds.x + 2, thumbBounds.y + 2,
                thumbBounds.width - 4, thumbBounds.height - 4, 8, 8);
        g2.dispose();
    }

    private static class ArrowButton extends JButton {
        private final int orientation;

        ArrowButton(int orientation) {
            this.orientation = orientation;
            setPreferredSize(new Dimension(14, 14));
            setFocusable(false);
            setBorder(null);
            setContentAreaFilled(false);
        }

        @Override
        protected void paintComponent(Graphics g) {
            Graphics2D g2 = (Graphics2D) g.create();
            g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

            int w = getWidth();
            int h = getHeight();
            g2.setColor(AppStyle.SCROLL_TRACK_COLOR);
            g2.fillRect(0, 0, w, h);

            g2.setColor(AppStyle.SCROLL_ARROW_COLOR);
            int cx = w / 2;
            int cy = h / 2;
            int s = 3;
            int[] xs;
            int[] ys;
            if (orientation == javax.swing.SwingConstants.NORTH) {
                xs = new int[] { cx - s, cx + s, cx };
                ys = new int[] { cy + s - 1, cy + s - 1, cy - s };
            } else if (orientation == javax.swing.SwingConstants.SOUTH) {
                xs = new int[] { cx - s, cx + s, cx };
                ys = new int[] { cy - s + 1, cy - s + 1, cy + s };
            } else if (orientation == javax.swing.SwingConstants.WEST) {
                xs = new int[] { cx + s - 1, cx + s - 1, cx - s };
                ys = new int[] { cy - s, cy + s, cy };
            } else {
                xs = new int[] { cx - s + 1, cx - s + 1, cx + s };
                ys = new int[] { cy - s, cy + s, cy };
            }
            g2.fillPolygon(xs, ys, 3);
            g2.dispose();
        }
    }
}
