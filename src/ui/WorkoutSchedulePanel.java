package ui;

import java.awt.BorderLayout;
import java.awt.Font;
import java.awt.GridLayout;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.SwingConstants;
import javax.swing.table.DefaultTableModel;

import data.GymDatabase;
import model.Member;
import model.Trainer;
import model.WorkoutSchedule;

public class WorkoutSchedulePanel extends JPanel {
    private GymDatabase database;
    private Runnable backAction;

    private JComboBox<String> memberBox;
    private JComboBox<String> trainerBox;
    private JComboBox<String> workoutTypeBox;
    private JComboBox<String> dayBox;
    private JComboBox<String> timeBox;

    private JButton submitButton;
    private JButton clearButton;
    private JButton backButton;

    private DefaultTableModel workoutTableModel;
    private JTable workoutTable;

    public WorkoutSchedulePanel(GymDatabase database, Runnable backAction) {
        this.database = database;
        this.backAction = backAction;

        setLayout(new BorderLayout());

        JLabel titleLabel = new JLabel("Workout Schedule", SwingConstants.CENTER);
        titleLabel.setFont(new Font("Arial", Font.BOLD, 26));

        add(titleLabel, BorderLayout.NORTH);

        JPanel formPanel = new JPanel(new GridLayout(6, 2, 10, 10));
        formPanel.setBorder(BorderFactory.createEmptyBorder(20, 40, 20, 40));

        memberBox = new JComboBox<>();
        trainerBox = new JComboBox<>();

        loadMembersIntoComboBox();
        loadTrainersIntoComboBox();

        workoutTypeBox = new JComboBox<>(new String[] {
                "Cardio",
                "Strength Training",
                "Weight Loss",
                "Muscle Gain",
                "General Fitness"
        });

        dayBox = new JComboBox<>(new String[] {
                "Monday",
                "Tuesday",
                "Wednesday",
                "Thursday",
                "Friday",
                "Saturday",
                "Sunday"
        });

        timeBox = new JComboBox<>(new String[] {
                "Morning",
                "Afternoon",
                "Evening"
        });

        submitButton = new JButton("Save Schedule");
        clearButton = new JButton("Clear");
        backButton = new JButton("Back");

        formPanel.add(new JLabel("Member:"));
        formPanel.add(memberBox);

        formPanel.add(new JLabel("Trainer:"));
        formPanel.add(trainerBox);

        formPanel.add(new JLabel("Workout Type:"));
        formPanel.add(workoutTypeBox);

        formPanel.add(new JLabel("Day:"));
        formPanel.add(dayBox);

        formPanel.add(new JLabel("Time:"));
        formPanel.add(timeBox);

        JPanel buttonPanel = new JPanel();
        buttonPanel.add(submitButton);
        buttonPanel.add(clearButton);
        buttonPanel.add(backButton);

        formPanel.add(new JLabel());
        formPanel.add(buttonPanel);

        String[] columns = {
                "Member",
                "Trainer",
                "Workout Type",
                "Day",
                "Time"
        };

        workoutTableModel = new DefaultTableModel(columns, 0);
        workoutTable = new JTable(workoutTableModel);

        JScrollPane tableScrollPane = new JScrollPane(workoutTable);

        JPanel mainPanel = new JPanel(new BorderLayout(10, 10));
        mainPanel.add(formPanel, BorderLayout.NORTH);
        mainPanel.add(tableScrollPane, BorderLayout.CENTER);

        submitButton.addActionListener(e -> submitWorkoutSchedule());
        clearButton.addActionListener(e -> clearForm());
        backButton.addActionListener(e -> backAction.run());

        add(mainPanel, BorderLayout.CENTER);
    }

    private void loadMembersIntoComboBox() {
        memberBox.addItem("Select Member");

        for (Member member : database.getMembers()) {
            memberBox.addItem(member.getName());
        }
    }

    private void loadTrainersIntoComboBox() {
        trainerBox.addItem("Select Trainer");

        for (Trainer trainer : database.getTrainers()) {
            trainerBox.addItem(trainer.getName());
        }
    }

    private void submitWorkoutSchedule() {
        String memberName = String.valueOf(memberBox.getSelectedItem());
        String trainerName = String.valueOf(trainerBox.getSelectedItem());
        String workoutType = String.valueOf(workoutTypeBox.getSelectedItem());
        String day = String.valueOf(dayBox.getSelectedItem());
        String time = String.valueOf(timeBox.getSelectedItem());

        if (memberName.equals("Select Member")) {
            JOptionPane.showMessageDialog(this, "Please select a member.");
            return;
        }

        if (trainerName.equals("Select Trainer")) {
            JOptionPane.showMessageDialog(this, "Please select a trainer.");
            return;
        }

        WorkoutSchedule workoutSchedule = new WorkoutSchedule(
                memberName,
                trainerName,
                workoutType,
                day,
                time);

        database.addWorkoutSchedule(workoutSchedule);

        workoutTableModel.addRow(new Object[] {
                workoutSchedule.getMemberName(),
                workoutSchedule.getTrainerName(),
                workoutSchedule.getWorkoutType(),
                workoutSchedule.getDay(),
                workoutSchedule.getTime()
        });

        JOptionPane.showMessageDialog(this, "Workout schedule saved successfully.");

        clearForm();
    }

    private void clearForm() {
        memberBox.setSelectedIndex(0);
        trainerBox.setSelectedIndex(0);
        workoutTypeBox.setSelectedIndex(0);
        dayBox.setSelectedIndex(0);
        timeBox.setSelectedIndex(0);
    }
}
