package ui;

import java.awt.Component;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Insets;
import java.awt.Rectangle;
import java.awt.RenderingHints;

import javax.swing.BorderFactory;
import javax.swing.DefaultListCellRenderer;
import javax.swing.JButton;
import javax.swing.JList;
import javax.swing.plaf.basic.BasicComboBoxUI;
import javax.swing.JComboBox;

public class RoundedComboBox extends JComboBox<String> {
    public RoundedComboBox(String[] items) {
        super(items);
        init();
    }

    public RoundedComboBox() {
        super();
        init();
    }

    private void init() {
        setOpaque(false);
        setBackground(AppStyle.FIELD_BACKGROUND);
        setForeground(AppStyle.TEXT_COLOR);
        setFont(AppStyle.FIELD_FONT);
        setBorder(new RoundedBorder(AppStyle.GOLD_COLOR, AppStyle.FIELD_RADIUS, 2, new Insets(4, 10, 4, 6)));
        setUI(new RoundedComboBoxUI());
        setRenderer(new ComboRenderer());
    }

    @Override
    protected void paintComponent(Graphics g) {
        Graphics2D g2 = (Graphics2D) g.create();
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        g2.setColor(AppStyle.FIELD_BACKGROUND);
        g2.fillRoundRect(0, 0, getWidth() - 1, getHeight() - 1, AppStyle.FIELD_RADIUS, AppStyle.FIELD_RADIUS);
        g2.dispose();
        super.paintComponent(g);
    }

    private static class ComboRenderer extends DefaultListCellRenderer {
        @Override
        public Component getListCellRendererComponent(JList<?> list, Object value, int index,
                boolean isSelected, boolean cellHasFocus) {
            super.getListCellRendererComponent(list, value, index, isSelected, cellHasFocus);
            setFont(AppStyle.FIELD_FONT);

            if (index == -1) {
                // The value displayed inside the closed box.
                setOpaque(false);
                setForeground(AppStyle.TEXT_COLOR);
                setBorder(BorderFactory.createEmptyBorder(0, 2, 0, 2));
            } else {
                setOpaque(true);
                setBackground(isSelected ? AppStyle.DROPDOWN_SELECTION_COLOR : AppStyle.FIELD_BACKGROUND);
                setForeground(AppStyle.TEXT_COLOR);
                setBorder(BorderFactory.createEmptyBorder(4, 8, 4, 8));
            }
            return this;
        }
    }

    private static class RoundedComboBoxUI extends BasicComboBoxUI {
        @Override
        protected JButton createArrowButton() {
            JButton button = new JButton() {
                @Override
                protected void paintComponent(Graphics g) {
                    Graphics2D g2 = (Graphics2D) g.create();
                    g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                    int w = getWidth();
                    int h = getHeight();
                    int s = 5;
                    int cx = w / 2;
                    int cy = h / 2;
                    int[] xs = { cx - s, cx + s, cx };
                    int[] ys = { cy - s + 1, cy - s + 1, cy + s - 1 };
                    g2.setColor(AppStyle.GOLD_COLOR);
                    g2.fillPolygon(xs, ys, 3);
                    g2.dispose();
                }
            };
            button.setContentAreaFilled(false);
            button.setBorder(BorderFactory.createEmptyBorder());
            button.setFocusable(false);
            button.setOpaque(false);
            return button;
        }

        @Override
        public void paintCurrentValueBackground(Graphics g, Rectangle bounds, boolean hasFocus) {
            // Intentionally empty — the rounded background is painted by the component itself.
        }
    }
}
