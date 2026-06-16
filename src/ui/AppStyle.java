package ui;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Insets;
import java.awt.Rectangle;

import javax.swing.BorderFactory;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.Scrollable;
import javax.swing.UIManager;
import javax.swing.border.Border;

public class AppStyle {
    public static final Font TITLE_FONT = new Font("Arial", Font.BOLD, 26);
    public static final Font LARGE_TITLE_FONT = new Font("Arial", Font.BOLD, 28);
    public static final Font LABEL_FONT = new Font("Arial", Font.PLAIN, 14);
    public static final Font FIELD_FONT = new Font("Arial", Font.PLAIN, 14);
    public static final Font BUTTON_FONT = new Font("Arial", Font.BOLD, 14);

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
        ScrollablePanel scrollablePanel = new ScrollablePanel(panel);

        JScrollPane scrollPane = new JScrollPane(scrollablePanel);
        scrollPane.setBorder(null);
        scrollPane.getVerticalScrollBar().setUnitIncrement(16);
        scrollPane.getHorizontalScrollBar().setUnitIncrement(16);

        return scrollPane;
    }

    private static class ScrollablePanel extends JPanel implements Scrollable {
        public ScrollablePanel(JPanel panel) {
            setLayout(new BorderLayout());
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
            return false;
        }
    }
}
