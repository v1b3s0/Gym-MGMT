package ui;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.GridBagConstraints;
import java.awt.Image;
import java.awt.Insets;
import java.awt.Rectangle;
import java.awt.RenderingHints;
import java.awt.image.BufferedImage;
import java.awt.image.ConvolveOp;
import java.awt.image.Kernel;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

import javax.imageio.ImageIO;
import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JViewport;
import javax.swing.Scrollable;
import javax.swing.SwingConstants;
import javax.swing.UIManager;
import javax.swing.border.Border;
import javax.swing.table.JTableHeader;

public class AppStyle {
    // ---- Fonts (JetBrains Mono, with graceful fallback) ----
    public static final String FONT_FAMILY = resolveFontFamily();

    public static final Font TITLE_FONT = font(Font.BOLD, 28);
    public static final Font LABEL_FONT = font(Font.PLAIN, 14);
    public static final Font FIELD_FONT = font(Font.PLAIN, 14);
    public static final Font BUTTON_FONT = font(Font.BOLD, 14);
    public static final Font FORM_LABEL_FONT = font(Font.BOLD, 16);
    public static final Font TABLE_HEADER_FONT = font(Font.BOLD, 15);

    public static Font font(int style, int size) {
        return new Font(FONT_FAMILY, style, size);
    }

    private static String resolveFontFamily() {
        String[] preferred = { "JetBrains Mono", "JetBrainsMono", "Consolas", Font.MONOSPACED };
        java.util.Set<String> available = new java.util.HashSet<>(java.util.Arrays.asList(
                java.awt.GraphicsEnvironment.getLocalGraphicsEnvironment().getAvailableFontFamilyNames()));
        for (String family : preferred) {
            if (available.contains(family)) {
                return family;
            }
        }
        return Font.MONOSPACED;
    }

    // ---- Core palette ----
    // Neutrals lean slightly cool (purple) so they read as a contrast against
    // the warm gold accents rather than just a darker gold.
    public static final Color BACKGROUND_COLOR = new Color(0x4D, 0x4C, 0x4F);
    public static final Color GOLD_COLOR = new Color(0xFC, 0xCA, 0x5D);
    public static final Color TEXT_COLOR = Color.WHITE;
    public static final Color DARK_TEXT_COLOR = new Color(0x2A, 0x29, 0x30);
    public static final Color SUB_TEXT_COLOR = new Color(200, 198, 206);

    // Payment / refund colours
    public static final Color PAYMENT_GREEN = new Color(0x3C, 0xBC, 0x0D);
    public static final Color REFUND_RED = new Color(0xBC, 0x3E, 0x0D);

    // ---- Surfaces (cool / purple-tinted) ----
    public static final Color FIELD_BACKGROUND = new Color(0x42, 0x41, 0x47);
    // Much darker than the general background, for table bodies and plan cards
    public static final Color DARK_SURFACE = new Color(0x26, 0x25, 0x2B);
    public static final Color TABLE_BACKGROUND = DARK_SURFACE;
    // Header in the dashboard's gold, a shade darker, with dark text
    public static final Color TABLE_HEADER_BACKGROUND = new Color(0xE0, 0xB0, 0x4C);
    public static final Color TABLE_GRID_COLOR = new Color(0x45, 0x44, 0x4B);

    // Sidebar: opaque when pinned, translucent when unpinned (matches the
    // dark table tone, a touch darker than the general background)
    public static final Color SIDEBAR_COLOR = new Color(0x20, 0x1F, 0x24, 205);
    public static final Color SIDEBAR_OPAQUE_COLOR = new Color(0x20, 0x1F, 0x24);

    // Image dark overlay
    public static final Color OVERLAY_COLOR = new Color(0x35, 0x34, 0x3C, 150);

    // Dropdown / list highlight (white text stays readable on this)
    public static final Color DROPDOWN_SELECTION_COLOR = new Color(0x5E, 0x52, 0x22);

    // Flat scrollbars (cool greys)
    public static final Color SCROLL_TRACK_COLOR = new Color(0x3E, 0x3D, 0x44);
    public static final Color SCROLL_THUMB_COLOR = new Color(0x6C, 0x6B, 0x73);
    public static final Color SCROLL_ARROW_COLOR = new Color(0xA0, 0x9E, 0xA6);

    // ---- Buttons ----
    public static final Color BUTTON_COLOR = new Color(0x45, 0x44, 0x4C);
    public static final Color BUTTON_HOVER_COLOR = new Color(0x55, 0x4C, 0x28);
    public static final Color BUTTON_PRESS_COLOR = new Color(0x6E, 0x60, 0x22);
    public static final Color BUTTON_DISABLED_COLOR = new Color(0x40, 0x3F, 0x46);
    public static final Color BUTTON_DISABLED_BORDER_COLOR = new Color(0x6A, 0x69, 0x70);

    // Faded red exit button
    public static final Color EXIT_COLOR = new Color(0x5E, 0x2E, 0x2A);
    public static final Color EXIT_HOVER_COLOR = new Color(0x7A, 0x38, 0x32);
    public static final Color EXIT_PRESS_COLOR = new Color(0x8C, 0x3E, 0x36);

    // Faded gold logout button (a blend of the dark background and gold)
    public static final Color LOGOUT_COLOR = new Color(0x6E, 0x65, 0x3A);
    public static final Color LOGOUT_HOVER_COLOR = new Color(0x8C, 0x7E, 0x40);
    public static final Color LOGOUT_PRESS_COLOR = new Color(0xA0, 0x8E, 0x44);

    // ---- Radii ----
    public static final int FIELD_RADIUS = 14;
    public static final int BUTTON_RADIUS = 18;

    // ---- Layout ----
    public static final Border FORM_PADDING = BorderFactory.createEmptyBorder(15, 45, 20, 45);
    public static final Border PAGE_PADDING = BorderFactory.createEmptyBorder(25, 35, 25, 35);
    public static final Dimension TABLE_VIEWPORT_SIZE = new Dimension(700, 220);

    private static BufferedImage backgroundImage;

    public static void apply() {
        try {
            UIManager.setLookAndFeel(UIManager.getCrossPlatformLookAndFeelClassName());
        } catch (Exception ex) {
            // fall back to default look and feel
        }

        loadBackground();

        // Fonts
        UIManager.put("Label.font", LABEL_FONT);
        UIManager.put("Button.font", BUTTON_FONT);
        UIManager.put("TextField.font", FIELD_FONT);
        UIManager.put("PasswordField.font", FIELD_FONT);
        UIManager.put("ComboBox.font", FIELD_FONT);
        UIManager.put("CheckBox.font", LABEL_FONT);
        UIManager.put("RadioButton.font", LABEL_FONT);
        UIManager.put("Table.font", FIELD_FONT);
        UIManager.put("TableHeader.font", BUTTON_FONT);

        // Labels & panels
        UIManager.put("Label.foreground", TEXT_COLOR);
        UIManager.put("Panel.background", BACKGROUND_COLOR);
        UIManager.put("CheckBox.background", BACKGROUND_COLOR);
        UIManager.put("CheckBox.foreground", TEXT_COLOR);
        UIManager.put("RadioButton.background", BACKGROUND_COLOR);
        UIManager.put("RadioButton.foreground", TEXT_COLOR);

        // Text components
        UIManager.put("TextField.background", FIELD_BACKGROUND);
        UIManager.put("TextField.foreground", TEXT_COLOR);
        UIManager.put("TextField.caretForeground", TEXT_COLOR);
        UIManager.put("PasswordField.background", FIELD_BACKGROUND);
        UIManager.put("PasswordField.foreground", TEXT_COLOR);
        UIManager.put("PasswordField.caretForeground", TEXT_COLOR);
        UIManager.put("TextArea.background", FIELD_BACKGROUND);
        UIManager.put("TextArea.foreground", TEXT_COLOR);
        UIManager.put("TextArea.caretForeground", TEXT_COLOR);

        // ComboBox + its popup list
        UIManager.put("ComboBox.background", FIELD_BACKGROUND);
        UIManager.put("ComboBox.foreground", TEXT_COLOR);
        UIManager.put("ComboBox.selectionBackground", DROPDOWN_SELECTION_COLOR);
        UIManager.put("ComboBox.selectionForeground", TEXT_COLOR);
        UIManager.put("List.background", FIELD_BACKGROUND);
        UIManager.put("List.foreground", TEXT_COLOR);
        UIManager.put("List.selectionBackground", DROPDOWN_SELECTION_COLOR);
        UIManager.put("List.selectionForeground", TEXT_COLOR);

        // Buttons (plain ones, e.g. dialogs)
        UIManager.put("Button.background", BUTTON_COLOR);
        UIManager.put("Button.foreground", TEXT_COLOR);
        UIManager.put("Button.focusPainted", false);

        // Table
        UIManager.put("Table.background", TABLE_BACKGROUND);
        UIManager.put("Table.foreground", TEXT_COLOR);
        UIManager.put("Table.selectionBackground", GOLD_COLOR);
        UIManager.put("Table.selectionForeground", DARK_TEXT_COLOR);
        UIManager.put("Table.gridColor", TABLE_GRID_COLOR);
        UIManager.put("TableHeader.background", TABLE_HEADER_BACKGROUND);
        UIManager.put("TableHeader.foreground", GOLD_COLOR);

        // Scroll
        UIManager.put("ScrollPane.background", BACKGROUND_COLOR);
        UIManager.put("Viewport.background", BACKGROUND_COLOR);

        // Dialogs
        UIManager.put("OptionPane.background", BACKGROUND_COLOR);
        UIManager.put("OptionPane.messageForeground", TEXT_COLOR);
        UIManager.put("Panel.foreground", TEXT_COLOR);

        // Flat scrollbars
        UIManager.put("ScrollBar.width", 14);
        UIManager.put("ScrollBar.background", SCROLL_TRACK_COLOR);
        UIManager.put("ScrollBar.track", SCROLL_TRACK_COLOR);
        UIManager.put("ScrollBar.thumb", SCROLL_THUMB_COLOR);
        UIManager.put("ScrollBarUI", "ui.FlatScrollBarUI");

        // Flat white check boxes / radio buttons with golden fill
        UIManager.put("CheckBox.icon", new FlatCheckBoxIcon());
        UIManager.put("RadioButton.icon", new FlatRadioIcon());

        // Menu bar + menus
        UIManager.put("MenuBar.background", SIDEBAR_OPAQUE_COLOR);
        UIManager.put("MenuBar.foreground", TEXT_COLOR);
        UIManager.put("MenuBar.border", BorderFactory.createMatteBorder(0, 0, 1, 0, GOLD_COLOR));
        UIManager.put("Menu.background", SIDEBAR_OPAQUE_COLOR);
        UIManager.put("Menu.foreground", TEXT_COLOR);
        UIManager.put("Menu.selectionBackground", GOLD_COLOR);
        UIManager.put("Menu.selectionForeground", DARK_TEXT_COLOR);
        UIManager.put("Menu.font", BUTTON_FONT);
        UIManager.put("MenuItem.background", FIELD_BACKGROUND);
        UIManager.put("MenuItem.foreground", TEXT_COLOR);
        UIManager.put("MenuItem.selectionBackground", GOLD_COLOR);
        UIManager.put("MenuItem.selectionForeground", DARK_TEXT_COLOR);
        UIManager.put("MenuItem.font", FIELD_FONT);
        UIManager.put("PopupMenu.background", FIELD_BACKGROUND);
        UIManager.put("PopupMenu.border", BorderFactory.createLineBorder(GOLD_COLOR, 1));
    }

    /**
     * Esc behaviour: if a text field / combo is focused, drop focus back to the
     * page (so Tab can continue navigating); otherwise go back to the dashboard.
     */
    public static void installEscBehavior(JComponent panel, Runnable backAction) {
        panel.setFocusable(true);
        panel.getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW)
                .put(javax.swing.KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_ESCAPE, 0), "escAction");
        panel.getActionMap().put("escAction", new javax.swing.AbstractAction() {
            @Override
            public void actionPerformed(java.awt.event.ActionEvent e) {
                java.awt.Component focusOwner = java.awt.KeyboardFocusManager
                        .getCurrentKeyboardFocusManager().getFocusOwner();
                if (focusOwner instanceof javax.swing.text.JTextComponent
                        || focusOwner instanceof javax.swing.JComboBox) {
                    panel.requestFocusInWindow();
                } else {
                    backAction.run();
                }
            }
        });
    }

    // ---- Background image ----
    private static void loadBackground() {
        try (InputStream in = AppStyle.class.getResourceAsStream("/assets/background.jpg")) {
            if (in == null) {
                backgroundImage = null;
                return;
            }
            backgroundImage = blur(ImageIO.read(in));
        } catch (Exception ex) {
            backgroundImage = null;
        }
    }

    /**
     * Soft blur at full resolution (a Gaussian convolution), so the image stays
     * crisp/non-pixelated and is only gently softened.
     */
    private static BufferedImage blur(BufferedImage src) {
        // Normalise to a plain RGB image first (ConvolveOp dislikes some types).
        BufferedImage rgb = new BufferedImage(src.getWidth(), src.getHeight(), BufferedImage.TYPE_INT_RGB);
        Graphics2D gg = rgb.createGraphics();
        gg.drawImage(src, 0, 0, null);
        gg.dispose();

        int radius = 4; // gentle softening, keeps full resolution
        int len = radius * 2 + 1;
        float[] kernel = gaussianKernel(len, radius / 2f);
        ConvolveOp op = new ConvolveOp(new Kernel(len, len, kernel), ConvolveOp.EDGE_NO_OP, null);
        return op.filter(rgb, null);
    }

    private static float[] gaussianKernel(int len, float sigma) {
        float[] data = new float[len * len];
        int half = len / 2;
        float sum = 0f;
        int i = 0;
        for (int y = -half; y <= half; y++) {
            for (int x = -half; x <= half; x++) {
                float v = (float) Math.exp(-(x * x + y * y) / (2f * sigma * sigma));
                data[i++] = v;
                sum += v;
            }
        }
        for (int k = 0; k < data.length; k++) {
            data[k] /= sum;
        }
        return data;
    }

    /** Paints the background image scaled to fill (cover), with a dark tint. */
    public static void paintBackground(Graphics g, Component c) {
        int w = c.getWidth();
        int h = c.getHeight();

        Graphics2D g2 = (Graphics2D) g.create();
        g2.setColor(BACKGROUND_COLOR);
        g2.fillRect(0, 0, w, h);

        if (backgroundImage != null) {
            double imageRatio = (double) backgroundImage.getWidth() / backgroundImage.getHeight();
            double areaRatio = (double) w / h;
            int dw;
            int dh;
            if (areaRatio > imageRatio) {
                dw = w;
                dh = (int) Math.ceil(w / imageRatio);
            } else {
                dh = h;
                dw = (int) Math.ceil(h * imageRatio);
            }
            int x = (w - dw) / 2;
            int y = (h - dh) / 2;
            g2.setRenderingHint(RenderingHints.KEY_INTERPOLATION, RenderingHints.VALUE_INTERPOLATION_BICUBIC);
            g2.setRenderingHint(RenderingHints.KEY_RENDERING, RenderingHints.VALUE_RENDER_QUALITY);
            g2.drawImage(backgroundImage, x, y, dw, dh, null);
        }

        g2.setColor(OVERLAY_COLOR);
        g2.fillRect(0, 0, w, h);
        g2.dispose();
    }

    // ---- App / taskbar icon (a gold dumbbell, drawn in code) ----
    public static List<Image> createAppIcons() {
        List<Image> icons = new ArrayList<>();
        for (int size : new int[] { 16, 24, 32, 48, 64 }) {
            icons.add(createAppIcon(size));
        }
        return icons;
    }

    private static BufferedImage createAppIcon(int s) {
        BufferedImage img = new BufferedImage(s, s, BufferedImage.TYPE_INT_ARGB);
        Graphics2D g = img.createGraphics();
        g.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

        // Rounded dark background so the icon reads on any taskbar colour
        g.setColor(SIDEBAR_OPAQUE_COLOR);
        g.fillRoundRect(0, 0, s - 1, s - 1, s / 4, s / 4);

        // Gold dumbbell
        g.setColor(GOLD_COLOR);
        int barH = Math.max(2, Math.round(s * 0.12f));
        g.fillRect(Math.round(s * 0.30f), (s - barH) / 2, Math.round(s * 0.40f), barH);
        int weightW = Math.max(3, Math.round(s * 0.13f));
        int weightH = Math.round(s * 0.42f);
        g.fillRoundRect(Math.round(s * 0.17f), (s - weightH) / 2, weightW, weightH, weightW / 2, weightW / 2);
        g.fillRoundRect(Math.round(s * 0.70f), (s - weightH) / 2, weightW, weightH, weightW / 2, weightW / 2);

        g.dispose();
        return img;
    }

    // ---- Shared widgets ----
    public static JLabel createPageTitle(String text) {
        JLabel label = new JLabel(text, SwingConstants.CENTER);
        label.setFont(TITLE_FONT);
        label.setForeground(GOLD_COLOR);
        label.setBorder(BorderFactory.createEmptyBorder(18, 0, 6, 0));
        return label;
    }

    public static JLabel createFormLabel(String text) {
        JLabel label = new JLabel(text);
        label.setFont(FORM_LABEL_FONT);
        label.setForeground(TEXT_COLOR);
        return label;
    }

    /**
     * A left-aligned row of buttons whose left edge lines up exactly with the
     * form's input fields (no leading gap), with even spacing between buttons.
     */
    public static JPanel createButtonRow(JComponent... buttons) {
        JPanel row = new JPanel(new FlowLayout(FlowLayout.LEFT, 0, 0));
        row.setOpaque(false);
        for (int i = 0; i < buttons.length; i++) {
            if (i > 0) {
                row.add(Box.createHorizontalStrut(10));
            }
            row.add(buttons[i]);
        }
        return row;
    }

    /** Adds a "label + field" row to a GridBagLayout form panel. */
    public static void addFormRow(JPanel form, int row, String labelText, JComponent field) {
        GridBagConstraints labelConstraints = new GridBagConstraints();
        labelConstraints.gridx = 0;
        labelConstraints.gridy = row;
        labelConstraints.anchor = GridBagConstraints.WEST;
        labelConstraints.insets = new Insets(8, 8, 8, 22);
        form.add(createFormLabel(labelText), labelConstraints);

        GridBagConstraints fieldConstraints = new GridBagConstraints();
        fieldConstraints.gridx = 1;
        fieldConstraints.gridy = row;
        fieldConstraints.fill = GridBagConstraints.HORIZONTAL;
        fieldConstraints.weightx = 1.0;
        fieldConstraints.insets = new Insets(8, 0, 8, 8);
        form.add(field, fieldConstraints);
    }

    /** Adds a full-width row (used for the buttons) aligned with the fields. */
    public static void addFormFullRow(JPanel form, int row, JComponent component) {
        GridBagConstraints constraints = new GridBagConstraints();
        constraints.gridx = 1;
        constraints.gridy = row;
        constraints.fill = GridBagConstraints.HORIZONTAL;
        constraints.weightx = 1.0;
        constraints.anchor = GridBagConstraints.WEST;
        constraints.insets = new Insets(16, 0, 8, 8);
        form.add(component, constraints);
    }

    public static JScrollPane createTableScrollPane(JTable table) {
        styleTable(table);
        table.setFillsViewportHeight(true);
        table.setPreferredScrollableViewportSize(TABLE_VIEWPORT_SIZE);

        JScrollPane scrollPane = new JScrollPane(table);
        scrollPane.getVerticalScrollBar().setUnitIncrement(16);
        scrollPane.getHorizontalScrollBar().setUnitIncrement(16);
        scrollPane.setBorder(BorderFactory.createLineBorder(TABLE_GRID_COLOR));
        scrollPane.setBackground(TABLE_BACKGROUND);
        scrollPane.getViewport().setBackground(TABLE_BACKGROUND);
        scrollPane.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);

        // Columns fit their content (never shrinking below it); when the window
        // is wide enough they grow to fill the table, otherwise a horizontal
        // scrollbar appears. Recomputed on data changes and on resize.
        Runnable relayout = () -> fitColumns(table, scrollPane.getViewport().getWidth());
        table.getModel().addTableModelListener(e -> relayout.run());
        scrollPane.getViewport().addComponentListener(new java.awt.event.ComponentAdapter() {
            @Override
            public void componentResized(java.awt.event.ComponentEvent e) {
                relayout.run();
            }
        });
        javax.swing.SwingUtilities.invokeLater(relayout);
        return scrollPane;
    }

    public static void styleTable(JTable table) {
        table.setBackground(TABLE_BACKGROUND);
        table.setForeground(TEXT_COLOR);
        table.setSelectionBackground(GOLD_COLOR);
        table.setSelectionForeground(DARK_TEXT_COLOR);
        table.setGridColor(TABLE_GRID_COLOR);
        table.setRowHeight(26);
        table.setFont(FIELD_FONT);
        table.setShowGrid(true);
        table.setAutoResizeMode(JTable.AUTO_RESIZE_OFF);

        JTableHeader header = table.getTableHeader();
        header.setBackground(TABLE_HEADER_BACKGROUND);
        header.setForeground(DARK_TEXT_COLOR);
        header.setFont(TABLE_HEADER_FONT);
        header.setReorderingAllowed(false);
        Dimension headerSize = header.getPreferredSize();
        header.setPreferredSize(new Dimension(headerSize.width, 32));
    }

    /**
     * Fits columns to their content (header/cells + buffer, capped). If the
     * viewport is wider than the total content, the spare width is shared out so
     * the columns fill the table; otherwise each column keeps its content width
     * and the horizontal scrollbar takes over. Columns never shrink below their
     * content width.
     */
    public static void fitColumns(JTable table, int viewportWidth) {
        int columnCount = table.getColumnCount();
        if (columnCount == 0) {
            return;
        }

        table.setAutoResizeMode(JTable.AUTO_RESIZE_OFF);
        int buffer = 22;
        int maxColumnWidth = 400;

        int[] minWidths = new int[columnCount];
        int totalMin = 0;

        for (int col = 0; col < columnCount; col++) {
            javax.swing.table.TableColumn column = table.getColumnModel().getColumn(col);

            javax.swing.table.TableCellRenderer headerRenderer = column.getHeaderRenderer();
            if (headerRenderer == null) {
                headerRenderer = table.getTableHeader().getDefaultRenderer();
            }
            Component headerComp = headerRenderer.getTableCellRendererComponent(
                    table, column.getHeaderValue(), false, false, 0, col);
            int width = headerComp.getPreferredSize().width;

            for (int row = 0; row < table.getRowCount(); row++) {
                Component cell = table.prepareRenderer(table.getCellRenderer(row, col), row, col);
                width = Math.max(width, cell.getPreferredSize().width);
            }

            minWidths[col] = Math.min(width + buffer, maxColumnWidth);
            totalMin += minWidths[col];
        }

        if (viewportWidth > totalMin) {
            int extra = viewportWidth - totalMin;
            int assigned = 0;
            for (int col = 0; col < columnCount; col++) {
                int columnWidth;
                if (col < columnCount - 1) {
                    int share = (int) ((long) extra * minWidths[col] / totalMin);
                    assigned += share;
                    columnWidth = minWidths[col] + share;
                } else {
                    columnWidth = minWidths[col] + (extra - assigned);
                }
                table.getColumnModel().getColumn(col).setPreferredWidth(columnWidth);
            }
        } else {
            for (int col = 0; col < columnCount; col++) {
                table.getColumnModel().getColumn(col).setPreferredWidth(minWidths[col]);
            }
        }

        table.revalidate();
        table.repaint();
    }

    public static JScrollPane createPageScrollPane(JPanel panel) {
        panel.setOpaque(false);

        ScrollablePanel scrollablePanel = new ScrollablePanel(panel);

        JScrollPane scrollPane = new JScrollPane(scrollablePanel);
        scrollPane.setBorder(null);
        scrollPane.setOpaque(false);
        scrollPane.getViewport().setOpaque(false);

        scrollPane.getVerticalScrollBar().setUnitIncrement(16);
        scrollPane.getHorizontalScrollBar().setUnitIncrement(16);
        return scrollPane;
    }

    private static class ScrollablePanel extends JPanel implements Scrollable {
        public ScrollablePanel(JPanel panel) {
            setLayout(new BorderLayout());
            setOpaque(false);
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
