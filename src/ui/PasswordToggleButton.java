package ui;

import java.awt.BasicStroke;
import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;

import javax.swing.JButton;
import javax.swing.JPasswordField;

/** Small eye icon that toggles the visibility of a password field. */
public class PasswordToggleButton extends JButton {
    private final JPasswordField field;
    private final char echoChar;
    private boolean visible;

    public PasswordToggleButton(JPasswordField field) {
        this.field = field;
        this.echoChar = field.getEchoChar();

        setPreferredSize(new Dimension(38, 34));
        setContentAreaFilled(false);
        setBorderPainted(false);
        setFocusPainted(false);
        setOpaque(false);
        setCursor(new Cursor(Cursor.HAND_CURSOR));

        addActionListener(e -> {
            visible = !visible;
            field.setEchoChar(visible ? (char) 0 : echoChar);
            repaint();
        });
    }

    @Override
    protected void paintComponent(Graphics g) {
        Graphics2D g2 = (Graphics2D) g.create();
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

        int w = getWidth();
        int h = getHeight();
        int cx = w / 2;
        int cy = h / 2;
        int eyeW = 18;
        int eyeH = 11;

        g2.setColor(AppStyle.GOLD_COLOR);
        g2.setStroke(new BasicStroke(1.6f));

        // Eye outline
        g2.drawOval(cx - eyeW / 2, cy - eyeH / 2, eyeW, eyeH);
        // Pupil
        g2.fillOval(cx - 3, cy - 3, 6, 6);

        // Crossed-out by default (hidden); the slash clears once revealed
        if (!visible) {
            g2.drawLine(cx - eyeW / 2, cy + eyeH / 2, cx + eyeW / 2, cy - eyeH / 2);
        }

        g2.dispose();
    }
}
