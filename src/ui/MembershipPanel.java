package ui;

import java.awt.BorderLayout;
import java.awt.Font;
import java.awt.GridLayout;

import javax.swing.BorderFactory;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.SwingConstants;

public class MembershipPanel extends JPanel {

    public MembershipPanel() {
        setLayout(new BorderLayout());

        JLabel titleLabel = new JLabel("Membership Plans", SwingConstants.CENTER);
        titleLabel.setFont(new Font("Arial", Font.BOLD, 26));

        add(titleLabel, BorderLayout.NORTH);

        JPanel plansPanel = new JPanel(new GridLayout(1, 3, 20, 20));
        plansPanel.setBorder(BorderFactory.createEmptyBorder(40, 40, 40, 40));

        plansPanel.add(createPlanPanel("Monthly", "100 AED", "Access for 1 month"));
        plansPanel.add(createPlanPanel("Quarterly", "250 AED", "Access for 3 months"));
        plansPanel.add(createPlanPanel("Yearly", "900 AED", "Access for 12 months"));

        add(plansPanel, BorderLayout.CENTER);
    }

    private JPanel createPlanPanel(String planName, String price, String description) {
        JPanel panel = new JPanel(new GridLayout(3, 1, 10, 10));
        panel.setBorder(BorderFactory.createTitledBorder(planName));

        JLabel nameLabel = new JLabel(planName + " Plan", SwingConstants.CENTER);
        nameLabel.setFont(new Font("Arial", Font.BOLD, 20));

        JLabel priceLabel = new JLabel(price, SwingConstants.CENTER);
        priceLabel.setFont(new Font("Arial", Font.BOLD, 18));

        JLabel descriptionLabel = new JLabel(description, SwingConstants.CENTER);

        panel.add(nameLabel);
        panel.add(priceLabel);
        panel.add(descriptionLabel);

        return panel;
    }
}
