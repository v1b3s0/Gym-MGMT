package ui;

import java.awt.BorderLayout;
import java.awt.Font;
import java.awt.GridLayout;
import java.awt.Insets;

import javax.swing.BorderFactory;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.SwingConstants;

/** Read-only screen showing the available membership plans and their prices. */
public class MembershipPanel extends JPanel {
    private RoundedButton backButton;

    public MembershipPanel(Runnable backAction) {
        setLayout(new BorderLayout());
        setBackground(AppStyle.BACKGROUND_COLOR);

        add(AppStyle.createPageTitle("Membership Plans"), BorderLayout.NORTH);

        JPanel plansPanel = new JPanel(new GridLayout(1, 3, 20, 20));
        plansPanel.setOpaque(false);
        plansPanel.setBorder(AppStyle.PAGE_PADDING);

        // Build one card per plan defined in model.MembershipPlan (same source the
        // Financing screen uses for default amounts).
        for (model.MembershipPlan plan : model.MembershipPlan.values()) {
            plansPanel.add(createPlanCard(plan.getLabel(),
                    (long) plan.getPrice() + " AED", plan.getDescription()));
        }

        backButton = new RoundedButton("Back");
        backButton.addActionListener(e -> backAction.run());

        JPanel bottomPanel = new JPanel();
        bottomPanel.setOpaque(false);
        bottomPanel.setBorder(BorderFactory.createEmptyBorder(0, 0, 20, 0));
        bottomPanel.add(backButton);

        add(plansPanel, BorderLayout.CENTER);
        add(bottomPanel, BorderLayout.SOUTH);

        AppStyle.installEscBehavior(this, backAction);
    }

    private JPanel createPlanCard(String planName, String price, String description) {
        RoundedPanel card = new RoundedPanel(AppStyle.DARK_SURFACE, 18);
        card.setLayout(new GridLayout(3, 1, 8, 8));
        card.setBorder(BorderFactory.createCompoundBorder(
                new RoundedBorder(AppStyle.GOLD_COLOR, 18, 1, new Insets(0, 0, 0, 0)),
                BorderFactory.createEmptyBorder(30, 18, 30, 18)));

        JLabel nameLabel = new JLabel(planName + " Plan", SwingConstants.CENTER);
        nameLabel.setFont(AppStyle.font(Font.BOLD, 22));
        nameLabel.setForeground(AppStyle.TEXT_COLOR);

        JLabel priceLabel = new JLabel(price, SwingConstants.CENTER);
        priceLabel.setFont(AppStyle.font(Font.BOLD, 34));
        priceLabel.setForeground(AppStyle.GOLD_COLOR);

        JLabel descriptionLabel = new JLabel(description, SwingConstants.CENTER);
        descriptionLabel.setFont(AppStyle.LABEL_FONT);
        descriptionLabel.setForeground(AppStyle.SUB_TEXT_COLOR);

        card.add(nameLabel);
        card.add(priceLabel);
        card.add(descriptionLabel);
        return card;
    }
}
