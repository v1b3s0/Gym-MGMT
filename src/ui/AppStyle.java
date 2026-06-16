package ui;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Insets;
import java.awt.Rectangle;

import javax.swing.BorderFactory;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JViewport;
import javax.swing.Scrollable;
import javax.swing.UIManager;
import javax.swing.border.Border;

public class AppStyle {
    public static final Font TITLE_FONT = new Font("Arial", Font.BOLD, 26);
    public static final Font LARGE_TITLE_FONT = new Font("Arial", Font.BOLD, 28);
    public static final Font LABEL_FONT = new Font("Arial", Font.PLAIN, 14);
    public static final Font FIELD_FONT = new Font("Arial", Font.PLAIN, 14);
    public static final Font BUTTON_FONT = new Font("Arial", Font.BOLD, 14);

    public static final Font CARD_TITLE_FONT = new Font("Arial", Font.BOLD, 17);
    public static final Font CARD_DESCRIPTION_FONT = new Font("Arial", Font.PLAIN, 12);

    public static final Color BACKGROUND_COLOR = new Color(245, 247, 250);
    public static final Color DASHBOARD_PANEL_COLOR = new Color(232, 238, 248);

    public static final Color CARD_COLOR = new Color(255, 255, 255);
    public static final Color CARD_HOVER_COLOR = new Color(238, 244, 255);
    public static final Color CARD_PRESS_COLOR = new Color(220, 232, 255);
    public static final Color CARD_BORDER_COLOR = new Color(190, 200, 220);

    public static final Color TEXT_COLOR = new Color(35, 40, 50);
    public static final Color SUB_TEXT_COLOR = new Color(90, 95, 105);
    public static final Color ACCENT_COLOR = new Color(60, 100, 180);

    public static final int CARD_RADIUS = 32;

    public static final Dimension DASHBOARD_CARD_SIZE = new Dimension(240, 125);
    public static final Dimension DASHBOARD_CARD_MIN_SIZE = new Dimension(180, 95);

    public static final Border DASHBOARD_CONTAINER_PADDING = BorderFactory.createEmptyBorder(25, 25, 25, 25);

    public static final Insets DASHBOARD_CARD_INSETS = new Insets(10, 10, 10, 10);

    public static final Border FORM_PADDING = BorderFactory.createEmptyBorder(20, 40, 20, 40);
    public static final Border PAGE_PADDING = BorderFactory.createEmptyBorder(25, 35, 25, 35);

    public static final Dimension TABLE_VIEWPORT_SIZE = new Dimension(700, 220);

    public static void apply() {
        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        } catch (Exception ex) {
            // If system look and feel fails, Java will use the default one.
        }

        UIManager.put("Label.font", LABEL_FONT);
        UIManager.put("Button.font", BUTTON_FONT);
        UIManager.put("TextField.font", FIELD_FONT);
        UIManager.put("PasswordField.font", FIELD_FONT);
        UIManager.put("ComboBox.font", FIELD_FONT);
        UIManager.put("CheckBox.font", LABEL_FONT);
        UIManager.put("RadioButton.font", LABEL_FONT);
        UIManager.put("Table.font", FIELD_FONT);
        UIManager.put("Table.rowHeight", 24);
        UIManager.put("TableHeader.font", BUTTON_FONT);

        UIManager.put("TextField.margin", new Insets(5, 8, 5, 8));
        UIManager.put("PasswordField.margin", new Insets(5, 8, 5, 8));
        UIManager.put("Button.margin", new Insets(6, 14, 6, 14));

        UIManager.put("Button.focusPainted", false);
    }

    public static JScrollPane createTableScrollPane(JTable table) {
        table.setFillsViewportHeight(true);
        table.setPreferredScrollableViewportSize(TABLE_VIEWPORT_SIZE);

        JScrollPane scrollPane = new JScrollPane(table);
        scrollPane.getVerticalScrollBar().setUnitIncrement(16);
        scrollPane.getHorizontalScrollBar().setUnitIncrement(16);

        return scrollPane;
    }

    public static JScrollPane createPageScrollPane(JPanel panel) {
        panel.setBackground(BACKGROUND_COLOR);

        ScrollablePanel scrollablePanel = new ScrollablePanel(panel);

        JScrollPane scrollPane = new JScrollPane(scrollablePanel);
        scrollPane.setBorder(null);
        scrollPane.setBackground(BACKGROUND_COLOR);
        scrollPane.getViewport().setBackground(BACKGROUND_COLOR);

        scrollPane.getVerticalScrollBar().setUnitIncrement(16);
        scrollPane.getHorizontalScrollBar().setUnitIncrement(16);

        return scrollPane;
    }

    private static class ScrollablePanel extends JPanel implements Scrollable {
        public ScrollablePanel(JPanel panel) {
            setLayout(new BorderLayout());
            setBackground(BACKGROUND_COLOR);
            add(panel, BorderLayout.CENTER);
        }

        @Override
        public Dimension getPreferredScrollableViewportSize() {
            return getPreferredSize();
        }

        @Override
        public int getScrollableUnitIncrement(Rectangle visibleRect, int orientation, int direction) {
            return 16;
        }

        @Override
        public int getScrollableBlockIncrement(Rectangle visibleRect, int orientation, int direction) {
            return 80;
        }

        @Override
        public boolean getScrollableTracksViewportWidth() {
            return true;
        }

        @Override
        public boolean getScrollableTracksViewportHeight() {
            if (getParent() instanceof JViewport viewport) {
                return viewport.getHeight() > getPreferredSize().height;
            }

            return false;
        }
    }
}
