package ui;

import java.awt.Font;
import java.awt.Insets;

import javax.swing.BorderFactory;
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
}
