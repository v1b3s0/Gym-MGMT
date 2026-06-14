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
import javax.swing.table.DefaultTableModel;

import data.GymDatabase;
import model.WorkoutSchedule;

public class WorkoutSchedulePanel extends JPanel {
    private GymDatabase database;

    private JComboBox<String> workoutTypeBox;
    private JComboBox<String> dayBox;
    private JComboBox<String> timeBox;
    private JComboBox<String> trainerBox;

    private JButton submitButton;
    private JButton clearButton;
    private JButton backButton;

    private DefaultTableModel workoutTableModel;
    private JTable workoutTable;

    public WorkoutSchedulePanel(GymDatabase database) {
        this.database = database;

        setLayout(new BorderLayout());

        JLabel titleLabel = new JLabel("Workout Schedule ", JLabel.CENTER);
        titleLabel.setFont(new Font("Arial", Font.BOLD, 26));

        add(titleLabel, BorderLayout.NORTH);

        JPanel formPanel = new JPanel(new GridLayout(5, 2, 10, 10));
        formPanel.setBorder(BorderFactory.createEmptyBorder(20, 40, 20, 40));

        workoutTypeBox = new JComboBox<>(new String[] {"Cardio", "Strength Training", "Yoga", "Pilates"});
        dayBox = new JComboBox<>(new String[] {"Monday", "Tuesday", "Wednesday", "Thursday", "Friday", "Saturday", "Sunday"});
        timeBox = new JComboBox<>(new String[] {"Morning", "Afternoon", "Evening"});
        trainerBox = new JComboBox<>(new String[] {"Trainer 1", "Trainer 2", "Trainer 3"}););

        submitButton = new JButton("Submit");
        clearButton = new JButton("Clear");
        backButton = new JButton("Back");

        formPanel.add(new JLabel("Workout Type:"));
        formPanel.add(workoutTypeBox);

        formPanel.add(new JLabel("Day:"));
        formPanel.add(dayBox);

        formPanel.add(new JLabel("Time:"));
        formPanel.add(timeBox);

        formPanel.add(new JLabel("Trainer:"));
        formPanel.add(trainerBox);

        buttonPanel.add(submitButton);
        buttonPanel.add(clearButton);
        buttonPanel.add(backButton);

        formPanel.add(new JLabel());
        formPanel.add(buttonPanel);

        String[] columnNames = {"Workout Type", "Day", "Time", "Trainer"};

        workoutTableModel =
         new DefaultTableModel(columnNames, 0);

        workoutTable =
         new JTable(workoutTableModel);

         JScrollPane scrollPane = new JScrollPane(workoutTable);

         Jpanel tablePanel = new JPanel(new BorderLayout(10, 10));

         mainPanel.add(formPanel, BorderLayout.NORTH);
         mainPanel.add(tableScrollPane, BorderLayout.CENTER);

         add(mainPanel, BorderLayout.CENTER);

         submitButton.addActionListener(
            e -> addWorkoutSchedule());

         clearButton.addActionListener(
            e -> clearForm());
 
    }

    private void addWorkoutSchedule() {
        String workoutType = (String) workoutTypeBox.getSelectedItem();
        String day = (String) dayBox.getSelectedItem();
        String time = (String) timeBox.getSelectedItem();
        String trainer = (String) trainerBox.getSelectedItem();

        WorkoutSchedule workoutSchedule = new WorkoutSchedule(workoutType, day, time, trainer);
        database.addWorkoutSchedule(workoutSchedule);

        workoutTableModel.addRow(new Object[]{workoutSchedule.getWorkoutType(), workoutSchedule.getDay(), workoutSchedule.getTime(), workoutSchedule.getTrainer()});

        JOptionPane.showMessageDialog(this, "Workout schedule saved successfully!");

        clearForm();
    }

    private void clearForm() {
        workoutTypeBox.setSelectedIndex(0);
        dayBox.setSelectedIndex(0);
        timeBox.setSelectedIndex(0);
        trainerBox.setSelectedIndex(0);
    }
