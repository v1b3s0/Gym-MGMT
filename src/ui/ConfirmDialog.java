package ui;

import java.awt.Color;
import java.awt.Component;
import java.awt.Dialog;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Window;
import java.awt.event.ActionEvent;
import java.awt.event.KeyEvent;

import javax.swing.AbstractAction;
import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JComponent;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JRootPane;
import javax.swing.KeyStroke;
import javax.swing.SwingUtilities;

/**
 * Borderless, themed yes/no confirmation dialog.
 * Enter activates the focused button (Yes by default); Left/Right move between
 * the buttons; Esc cancels (No).
 */
public class ConfirmDialog {

    public static boolean show(Component parent, String message) {
        Window owner = parent == null ? null : SwingUtilities.getWindowAncestor(parent);
        JDialog dialog = new JDialog(owner, Dialog.ModalityType.APPLICATION_MODAL);
        dialog.setUndecorated(true);

        final boolean[] result = { false };

        RoundedPanel content = new RoundedPanel(new Color(0x3A, 0x39, 0x40), 18);
        content.setLayout(new BoxLayout(content, BoxLayout.Y_AXIS));
        content.setBorder(BorderFactory.createCompoundBorder(
                new RoundedBorder(AppStyle.GOLD_COLOR, 18, 1, new java.awt.Insets(0, 0, 0, 0)),
                BorderFactory.createEmptyBorder(18, 24, 14, 24)));

        JLabel messageLabel = new JLabel(message);
        messageLabel.setFont(AppStyle.font(java.awt.Font.BOLD, 18));
        messageLabel.setForeground(AppStyle.TEXT_COLOR);
        messageLabel.setAlignmentX(JComponent.CENTER_ALIGNMENT);

        RoundedButton yesButton = new RoundedButton("Yes");
        RoundedButton noButton = new RoundedButton("No");
        Dimension buttonSize = new Dimension(96, 38);
        yesButton.setPreferredSize(buttonSize);
        noButton.setPreferredSize(buttonSize);

        yesButton.addActionListener(e -> {
            result[0] = true;
            dialog.dispose();
        });
        noButton.addActionListener(e -> {
            result[0] = false;
            dialog.dispose();
        });

        JPanel buttons = new JPanel(new FlowLayout(FlowLayout.CENTER, 16, 0));
        buttons.setOpaque(false);
        buttons.setAlignmentX(JComponent.CENTER_ALIGNMENT);
        buttons.add(yesButton);
        buttons.add(noButton);

        content.add(messageLabel);
        content.add(Box.createVerticalStrut(16));
        content.add(buttons);

        // Enter activates whichever button is focused.
        bindEnter(yesButton);
        bindEnter(noButton);

        JRootPane rootPane = dialog.getRootPane();
        rootPane.getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW)
                .put(KeyStroke.getKeyStroke(KeyEvent.VK_LEFT, 0), "focusNo");
        rootPane.getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW)
                .put(KeyStroke.getKeyStroke(KeyEvent.VK_RIGHT, 0), "focusYes");
        rootPane.getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW)
                .put(KeyStroke.getKeyStroke(KeyEvent.VK_ESCAPE, 0), "cancel");
        rootPane.getActionMap().put("focusNo", new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent e) {
                noButton.requestFocusInWindow();
            }
        });
        rootPane.getActionMap().put("focusYes", new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent e) {
                yesButton.requestFocusInWindow();
            }
        });
        rootPane.getActionMap().put("cancel", new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent e) {
                result[0] = false;
                dialog.dispose();
            }
        });

        dialog.setContentPane(content);
        try {
            dialog.setBackground(new Color(0, 0, 0, 0));
        } catch (Exception ignored) {
            // translucency unsupported — square corners are fine
        }
        dialog.pack();
        dialog.setSize(Math.max(dialog.getWidth(), 290), Math.max(dialog.getHeight(), 125));
        dialog.setLocationRelativeTo(owner);

        SwingUtilities.invokeLater(yesButton::requestFocusInWindow);
        dialog.setVisible(true);

        return result[0];
    }

    private static void bindEnter(JComponent button) {
        button.getInputMap(JComponent.WHEN_FOCUSED)
                .put(KeyStroke.getKeyStroke(KeyEvent.VK_ENTER, 0), "press");
        button.getActionMap().put("press", new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent e) {
                ((javax.swing.AbstractButton) button).doClick();
            }
        });
    }
}
