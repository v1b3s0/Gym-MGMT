package ui;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;

import javax.swing.BorderFactory;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.SwingConstants;

import data.GymDatabase;

public class DashboardFrame extends JFrame {
    private JPanel contentPanel;
    private GymDatabase database;

    public DashboardFrame(GymDatabase database) {
        this.database = database;

        setTitle("Gym Management System - Dashboard");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(1100, 750);
        setMinimumSize(new Dimension(900, 600));
        setLocationRelativeTo(null);
        setLayout(new BorderLayout());

        contentPanel = new JPanel(new BorderLayout());
        contentPanel.setBackground(AppStyle.BACKGROUND_COLOR);

        add(contentPanel, BorderLayout.CENTER);

        showWelcomePage();
    }

    private void showWelcomePage() {
        contentPanel.removeAll();

        JPanel dashboardPanel = new JPanel(new BorderLayout(20, 20));
        dashboardPanel.setBorder(AppStyle.PAGE_PADDING);
        dashboardPanel.setBackground(AppStyle.BACKGROUND_COLOR);

        JPanel headerPanel = new JPanel(new BorderLayout(10, 10));
        headerPanel.setBackground(AppStyle.BACKGROUND_COLOR);

        JLabel titleLabel = new JLabel("Gym Management System", SwingConstants.CENTER);
        titleLabel.setFont(AppStyle.LARGE_TITLE_FONT);
        titleLabel.setForeground(AppStyle.TEXT_COLOR);

        JLabel subtitleLabel = new JLabel("Choose a module to manage gym records", SwingConstants.CENTER);
        subtitleLabel.setFont(AppStyle.LABEL_FONT);
        subtitleLabel.setForeground(AppStyle.SUB_TEXT_COLOR);

        headerPanel.add(titleLabel, BorderLayout.NORTH);
        headerPanel.add(subtitleLabel, BorderLayout.CENTER);

        JPanel centerWrapper = new JPanel(new GridBagLayout());
        centerWrapper.setBackground(AppStyle.BACKGROUND_COLOR);

        JPanel moduleContainer = new JPanel(new BorderLayout());
        moduleContainer.setBackground(AppStyle.DASHBOARD_PANEL_COLOR);
        moduleContainer.setBorder(BorderFactory.createEmptyBorder(25, 25, 25, 25));

        JPanel cardPanel = new JPanel(new GridBagLayout());
        cardPanel.setOpaque(false);

        GridBagConstraints constraints = new GridBagConstraints();
        constraints.fill = GridBagConstraints.BOTH;
        constraints.insets = new Insets(10, 10, 10, 10);
        constraints.weighty = 1;

        addDashboardCard(cardPanel, constraints,
                "Member Management",
                "Register members and view records",
                0, 0, 2,
                () -> showMemberPanel());

        addDashboardCard(cardPanel, constraints,
                "Trainer Management",
                "Add trainers and assign members",
                2, 0, 1,
                () -> showTrainerPanel());

        addDashboardCard(cardPanel, constraints,
                "Membership Plans",
                "View available gym plans",
                0, 1, 1,
                () -> showMembershipPanel());

        addDashboardCard(cardPanel, constraints,
                "Workout Schedule",
                "Assign workouts, days, and times",
                1, 1, 2,
                () -> showWorkoutSchedulePanel());

        addDashboardCard(cardPanel, constraints,
                "Payment History",
                "Record and view payments",
                0, 2, 2,
                () -> showPaymentPanel());

        addDashboardCard(cardPanel, constraints,
                "Exit",
                "Close the application safely",
                2, 2, 1,
                () -> confirmExit());

        moduleContainer.add(cardPanel, BorderLayout.CENTER);
        centerWrapper.add(moduleContainer);

        dashboardPanel.add(headerPanel, BorderLayout.NORTH);
        dashboardPanel.add(centerWrapper, BorderLayout.CENTER);

        contentPanel.add(AppStyle.createPageScrollPane(dashboardPanel), BorderLayout.CENTER);

        contentPanel.revalidate();
        contentPanel.repaint();
    }

    private void addDashboardCard(JPanel panel, GridBagConstraints constraints,
            String title, String description, int x, int y, int width, Runnable action) {

        constraints.gridx = x;
        constraints.gridy = y;
        constraints.gridwidth = width;
        constraints.weightx = width;

        DashboardCardButton cardButton = new DashboardCardButton(title, description, action);

        panel.add(cardButton, constraints);
    }

    private void confirmExit() {
        int choice = JOptionPane.showConfirmDialog(
                this,
                "Are you sure you want to exit?",
                "Confirm Exit",
                JOptionPane.YES_NO_OPTION);

        if (choice == JOptionPane.YES_OPTION) {
            System.exit(0);
        }
    }

    private void showMemberPanel() {
        contentPanel.removeAll();

        MemberPanel memberPanel = new MemberPanel(database, () -> showWelcomePage());
        contentPanel.add(memberPanel, BorderLayout.CENTER);

        contentPanel.revalidate();
        contentPanel.repaint();
    }

    private void showTrainerPanel() {
        contentPanel.removeAll();

        TrainerPanel trainerPanel = new TrainerPanel(database, () -> showWelcomePage());
        contentPanel.add(trainerPanel, BorderLayout.CENTER);

        contentPanel.revalidate();
        contentPanel.repaint();
    }

    private void showMembershipPanel() {
        contentPanel.removeAll();

        MembershipPanel membershipPanel = new MembershipPanel(() -> showWelcomePage());
        contentPanel.add(membershipPanel, BorderLayout.CENTER);

        contentPanel.revalidate();
        contentPanel.repaint();
    }

    private void showWorkoutSchedulePanel() {
        contentPanel.removeAll();

        WorkoutSchedulePanel workoutSchedulePanel = new WorkoutSchedulePanel(database, () -> showWelcomePage());
        contentPanel.add(workoutSchedulePanel, BorderLayout.CENTER);

        contentPanel.revalidate();
        contentPanel.repaint();
    }

    private void showPaymentPanel() {
        contentPanel.removeAll();

        PaymentPanel paymentPanel = new PaymentPanel(database, () -> showWelcomePage());
        contentPanel.add(paymentPanel, BorderLayout.CENTER);

        contentPanel.revalidate();
        contentPanel.repaint();
    }
}
