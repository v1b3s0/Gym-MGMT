package ui;

import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Insets;
import java.awt.RenderingHints;

import javax.swing.JTextField;

/** Text field with a rounded gold border and dark fill, matching the theme. */
public class RoundedTextField extends JTextField {
    public RoundedTextField() {
        super();
        init();
    }

    public RoundedTextField(String text) {
        super(text);
        init();
    }

    private void init() {
        setOpaque(false);
        setForeground(AppStyle.TEXT_COLOR);
        setCaretColor(AppStyle.TEXT_COLOR);
        setFont(AppStyle.FIELD_FONT);
        setBorder(new RoundedBorder(AppStyle.GOLD_COLOR, AppStyle.FIELD_RADIUS, 2, new Insets(6, 10, 6, 10)));
    }

    @Override
    protected void paintComponent(Graphics g) {
        // Paint the rounded fill ourselves (the component is non-opaque), then
        // let JTextField draw the text/caret on top.
        Graphics2D g2 = (Graphics2D) g.create();
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        g2.setColor(AppStyle.FIELD_BACKGROUND);
        g2.fillRoundRect(0, 0, getWidth() - 1, getHeight() - 1, AppStyle.FIELD_RADIUS, AppStyle.FIELD_RADIUS);
        g2.dispose();
        super.paintComponent(g);
    }
}
