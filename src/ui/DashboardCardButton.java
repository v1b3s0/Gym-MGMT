package ui;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.GridLayout;
import java.awt.RenderingHints;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

import javax.swing.BorderFactory;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.SwingConstants;

public class DashboardCardButton extends JPanel {
    private Color currentColor;

    public DashboardCardButton(String title, String description, Runnable action) {
        this.currentColor = AppStyle.CARD_COLOR;

        setLayout(new BorderLayout());
        setPreferredSize(new Dimension(240, 125));
        setMinimumSize(new Dimension(180, 95));

        setOpaque(false);
        setFocusable(true);
        setCursor(new Cursor(Cursor.HAND_CURSOR));
        setBorder(BorderFactory.createEmptyBorder(18, 18, 18, 18));

        JPanel textPanel = new JPanel(new GridLayout(2, 1, 4, 4));
        textPanel.setOpaque(false);

        JLabel titleLabel = new JLabel(title, SwingConstants.CENTER);
        titleLabel.setFont(AppStyle.CARD_TITLE_FONT);
        titleLabel.setForeground(AppStyle.TEXT_COLOR);

        JLabel descriptionLabel = new JLabel(
                "<html><center>" + description + "</center></html>",
                SwingConstants.CENTER);
        descriptionLabel.setFont(AppStyle.CARD_DESCRIPTION_FONT);
        descriptionLabel.setForeground(AppStyle.SUB_TEXT_COLOR);

        textPanel.add(titleLabel);
        textPanel.add(descriptionLabel);

        add(textPanel, BorderLayout.CENTER);

        MouseAdapter mouseHandler = new MouseAdapter() {
            @Override
            public void mouseEntered(MouseEvent e) {
                currentColor = AppStyle.CARD_HOVER_COLOR;
                repaint();
            }

            @Override
            public void mouseExited(MouseEvent e) {
                currentColor = AppStyle.CARD_COLOR;
                repaint();
            }

            @Override
            public void mousePressed(MouseEvent e) {
                currentColor = AppStyle.CARD_PRESS_COLOR;
                repaint();
            }

            @Override
            public void mouseReleased(MouseEvent e) {
                if (contains(e.getPoint())) {
                    currentColor = AppStyle.CARD_HOVER_COLOR;
                    requestFocusInWindow();
                    action.run();
                } else {
                    currentColor = AppStyle.CARD_COLOR;
                }

                repaint();
            }
        };

        addMouseListener(mouseHandler);
        textPanel.addMouseListener(mouseHandler);
        titleLabel.addMouseListener(mouseHandler);
        descriptionLabel.addMouseListener(mouseHandler);

        addKeyListener(new KeyAdapter() {
            @Override
            public void keyPressed(KeyEvent e) {
                if (e.getKeyCode() == KeyEvent.VK_ENTER || e.getKeyCode() == KeyEvent.VK_SPACE) {
                    action.run();
                }
            }
        });
    }

    @Override
    protected void paintComponent(Graphics g) {
        Graphics2D graphics = (Graphics2D) g.create();

        graphics.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

        graphics.setColor(currentColor);
        graphics.fillRoundRect(
                0,
                0,
                getWidth() - 1,
                getHeight() - 1,
                AppStyle.CARD_RADIUS,
                AppStyle.CARD_RADIUS);

        graphics.setColor(AppStyle.CARD_BORDER_COLOR);
        graphics.drawRoundRect(
                0,
                0,
                getWidth() - 1,
                getHeight() - 1,
                AppStyle.CARD_RADIUS,
                AppStyle.CARD_RADIUS);

        graphics.dispose();

        super.paintComponent(g);
    }
}
