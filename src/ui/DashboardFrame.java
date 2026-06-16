package ui;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.Font;

import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
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

        JMenuBar menuBar = new JMenuBar();

        JMenuItem memberMenu = new JMenuItem("Member Management");
        JMenuItem trainerMenu = new JMenuItem("Trainer Management");
        JMenuItem membershipMenu = new JMenuItem("Membership Plans");
        JMenuItem workoutMenu = new JMenuItem("Workout Schedule");
        JMenuItem paymentMenu = new JMenuItem("Payment History");
        JMenuItem exitMenu = new JMenuItem("Exit");

        menuBar.add(memberMenu);
        menuBar.add(trainerMenu);
        menuBar.add(membershipMenu);
        menuBar.add(workoutMenu);
        menuBar.add(paymentMenu);
        menuBar.add(exitMenu);

        setJMenuBar(menuBar);

        memberMenu.addActionListener(e -> showMemberPanel());
        trainerMenu.addActionListener(e -> showTrainerPanel());
        membershipMenu.addActionListener(e -> showMembershipPanel());
        workoutMenu.addActionListener(e -> showWorkoutSchedulePanel());
        paymentMenu.addActionListener(e -> showPaymentPanel());

        exitMenu.addActionListener(e -> System.exit(0));

        contentPanel = new JPanel(new BorderLayout());
        add(contentPanel, BorderLayout.CENTER);

        showWelcomePage();
    }

    private void showWelcomePage() {
        contentPanel.removeAll();

        JLabel welcomeLabel = new JLabel("Welcome to the Gym Management System", SwingConstants.CENTER);
        welcomeLabel.setFont(new Font("Arial", Font.BOLD, 28));

        contentPanel.add(welcomeLabel, BorderLayout.CENTER);

        contentPanel.revalidate();
        contentPanel.repaint();
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
