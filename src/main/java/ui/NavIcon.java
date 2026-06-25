package ui;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Component;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;

import javax.swing.Icon;

/**
 * Simple vector icons drawn with Graphics2D so they render on any system
 * (no reliance on emoji/font glyphs). One {@link Kind} per sidebar entry.
 */
public class NavIcon implements Icon {
    public enum Kind { MEMBER, TRAINER, MEMBERSHIP, WORKOUT, FINANCING, MONITOR, LOGOUT, EXIT }

    private final Kind kind;
    private Color color;
    private final int size;

    public NavIcon(Kind kind, Color color) {
        this(kind, color, 24);
    }

    public NavIcon(Kind kind, Color color, int size) {
        this.kind = kind;
        this.color = color;
        this.size = size;
    }

    /** Lets the button recolour the icon for hover/active states. */
    public void setColor(Color color) {
        this.color = color;
    }

    @Override
    public int getIconWidth() {
        return size;
    }

    @Override
    public int getIconHeight() {
        return size;
    }

    @Override
    public void paintIcon(Component c, Graphics g, int x, int y) {
        Graphics2D g2 = (Graphics2D) g.create();
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        g2.setColor(color);
        g2.setStroke(new BasicStroke(1.8f, BasicStroke.CAP_ROUND, BasicStroke.JOIN_ROUND));

        int s = size;
        switch (kind) {
            case MEMBER: // head + shoulders
                g2.fillOval(x + s / 2 - 4, y + 3, 8, 8);
                g2.fillRoundRect(x + s / 2 - 7, y + 12, 14, 8, 8, 8);
                break;
            case TRAINER: // dumbbell
                g2.fillRect(x + 4, y + s / 2 - 1, s - 8, 3);
                g2.fillRoundRect(x + 2, y + s / 2 - 5, 4, 11, 2, 2);
                g2.fillRoundRect(x + s - 6, y + s / 2 - 5, 4, 11, 2, 2);
                break;
            case MEMBERSHIP: { // receipt (like 🧾)
                int left = x + 5;
                int top = y + 2;
                int w = s - 10;
                int h = s - 6;
                g2.drawLine(left, top, left + w, top);          // top edge
                g2.drawLine(left, top, left, top + h);          // left edge
                g2.drawLine(left + w, top, left + w, top + h);  // right edge
                int teeth = 4;
                double stepX = (double) w / teeth;
                int baseY = top + h;
                int px = left;
                int py = baseY;
                for (int i = 0; i < teeth; i++) {               // zig-zag bottom
                    int nx = (int) (left + stepX * (i + 1));
                    int ny = (i % 2 == 0) ? baseY - 4 : baseY;
                    g2.drawLine(px, py, nx, ny);
                    px = nx;
                    py = ny;
                }
                g2.drawLine(left + 3, top + 5, left + w - 3, top + 5);  // text lines
                g2.drawLine(left + 3, top + 9, left + w - 4, top + 9);
                break;
            }
            case WORKOUT: // calendar
                g2.drawRoundRect(x + 3, y + 4, s - 7, s - 7, 3, 3);
                g2.fillRect(x + 3, y + 4, s - 7, 4);
                g2.drawLine(x + 6, y + 2, x + 6, y + 6);
                g2.drawLine(x + s - 6, y + 2, x + s - 6, y + 6);
                break;
            case FINANCING: // coin with a dollar sign
                g2.drawOval(x + 3, y + 3, s - 7, s - 7);
                g2.setFont(new Font(AppStyle.FONT_FAMILY, Font.BOLD, s - 8));
                FontMetrics fm = g2.getFontMetrics();
                String dollar = "$";
                int dw = fm.stringWidth(dollar);
                g2.drawString(dollar, x + (s - dw) / 2,
                        y + (s + fm.getAscent() - fm.getDescent()) / 2 - 1);
                break;
            case MONITOR: // monitor / display
                g2.drawRoundRect(x + 2, y + 3, s - 5, s - 11, 3, 3);
                g2.drawLine(x + s / 2 - 4, y + s - 3, x + s / 2 + 4, y + s - 3); // base
                g2.drawLine(x + s / 2, y + s - 6, x + s / 2, y + s - 3);         // stand
                break;
            case LOGOUT: // door + arrow out
                g2.drawRoundRect(x + 3, y + 3, 8, s - 6, 2, 2);
                int ly = y + s / 2;
                g2.drawLine(x + 9, ly, x + s - 3, ly);
                g2.drawLine(x + s - 3, ly, x + s - 7, ly - 4);
                g2.drawLine(x + s - 3, ly, x + s - 7, ly + 4);
                break;
            case EXIT: // X
                g2.drawLine(x + 5, y + 5, x + s - 5, y + s - 5);
                g2.drawLine(x + s - 5, y + 5, x + 5, y + s - 5);
                break;
            default:
                break;
        }
        g2.dispose();
    }
}
