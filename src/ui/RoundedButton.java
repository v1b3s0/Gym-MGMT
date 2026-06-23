package ui;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Cursor;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;

import javax.swing.JButton;
import javax.swing.border.EmptyBorder;

/**
 * Material-style rounded button: dark fill, gold outline, white text.
 */
public class RoundedButton extends JButton {
    private final Color baseColor;
    private final Color hoverColor;
    private final Color pressColor;

    public RoundedButton(String text) {
        this(text, AppStyle.BUTTON_COLOR, AppStyle.BUTTON_HOVER_COLOR, AppStyle.BUTTON_PRESS_COLOR);
    }

    public RoundedButton(String text, Color baseColor, Color hoverColor, Color pressColor) {
        super(text);
        this.baseColor = baseColor;
        this.hoverColor = hoverColor;
        this.pressColor = pressColor;

        setFont(AppStyle.BUTTON_FONT);
        setForeground(AppStyle.TEXT_COLOR);
        setContentAreaFilled(false);
        setFocusPainted(false);
        setOpaque(false);
        setBorder(new EmptyBorder(7, 18, 7, 18));
        setCursor(new Cursor(Cursor.HAND_CURSOR));
    }

    @Override
    protected void paintComponent(Graphics g) {
        Graphics2D g2 = (Graphics2D) g.create();
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

        Color background;
        if (!isEnabled()) {
            background = AppStyle.BUTTON_DISABLED_COLOR;
        } else if (getModel().isPressed()) {
            background = pressColor;
        } else if (getModel().isRollover()) {
            background = hoverColor;
        } else {
            background = baseColor;
        }

        g2.setColor(background);
        g2.fillRoundRect(0, 0, getWidth() - 1, getHeight() - 1, AppStyle.BUTTON_RADIUS, AppStyle.BUTTON_RADIUS);

        g2.setColor(isEnabled() ? AppStyle.GOLD_COLOR : AppStyle.BUTTON_DISABLED_BORDER_COLOR);
        g2.setStroke(new BasicStroke(1.4f));
        g2.drawRoundRect(0, 0, getWidth() - 1, getHeight() - 1, AppStyle.BUTTON_RADIUS, AppStyle.BUTTON_RADIUS);

        g2.dispose();

        super.paintComponent(g);
    }
}
