package ui;

import java.awt.BorderLayout;

import javax.swing.JFrame;

public class DashboardFrame extends JFrame {

    public DashboardFrame() {
        setTitle("Gym Management System - Dashboard");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setExtendedState(JFrame.MAXIMIZED_BOTH);
        setLayout(new BorderLayout());
    }
}
