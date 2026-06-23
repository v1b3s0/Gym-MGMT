package ui;

import java.awt.BorderLayout;
import java.awt.GridBagLayout;

import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.ListSelectionModel;
import javax.swing.table.DefaultTableModel;

import data.GymDatabase;
import model.Member;
import model.Trainer;
import model.WorkoutSchedule;

/**
 * Workout schedule screen: assign a member, trainer, workout type, day and time,
 * then save/edit/delete entries shown in the table.
 */
public class WorkoutSchedulePanel extends JPanel {
    private GymDatabase database;

    private RoundedComboBox memberBox;
    private RoundedComboBox trainerBox;
    private RoundedComboBox workoutTypeBox;
    private RoundedComboBox dayBox;
    private RoundedComboBox timeBox;

    private RoundedButton submitButton;
    private RoundedButton deleteButton;
    private RoundedButton clearButton;
    private RoundedButton backButton;

    private DefaultTableModel workoutTableModel;
    private JTable workoutTable;

    private int selectedRow = -1;

    public WorkoutSchedulePanel(GymDatabase database, Runnable backAction) {
        this.database = database;

        setLayout(new BorderLayout());
        setBackground(AppStyle.BACKGROUND_COLOR);

        add(AppStyle.createPageTitle("Workout Schedule"), BorderLayout.NORTH);

        JPanel formPanel = new JPanel(new GridBagLayout());
        formPanel.setOpaque(false);
        formPanel.setBorder(AppStyle.FORM_PADDING);

        memberBox = new RoundedComboBox();
        trainerBox = new RoundedComboBox();
        loadMembersIntoComboBox();
        loadTrainersIntoComboBox();

        workoutTypeBox = new RoundedComboBox(new String[] {
                "Cardio", "Strength Training", "Weight Loss", "Muscle Gain", "General Fitness"
        });

        dayBox = new RoundedComboBox(new String[] {
                "Monday", "Tuesday", "Wednesday", "Thursday", "Friday", "Saturday", "Sunday"
        });

        timeBox = new RoundedComboBox(new String[] { "Morning", "Afternoon", "Evening" });

        submitButton = new RoundedButton("Save Schedule");
        deleteButton = new RoundedButton("Delete");
        clearButton = new RoundedButton("Clear");
        backButton = new RoundedButton("Back");
        deleteButton.setEnabled(false);

        AppStyle.addFormRow(formPanel, 0, "Member:", memberBox);
        AppStyle.addFormRow(formPanel, 1, "Trainer:", trainerBox);
        AppStyle.addFormRow(formPanel, 2, "Workout Type:", workoutTypeBox);
        AppStyle.addFormRow(formPanel, 3, "Day:", dayBox);
        AppStyle.addFormRow(formPanel, 4, "Time:", timeBox);

        AppStyle.addFormFullRow(formPanel, 5,
                AppStyle.createButtonRow(submitButton, deleteButton, clearButton, backButton));

        String[] columns = { "Member", "Trainer", "Workout Type", "Day", "Time" };
        workoutTableModel = new DefaultTableModel(columns, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
        workoutTable = new JTable(workoutTableModel);
        workoutTable.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        workoutTable.getSelectionModel().addListSelectionListener(e -> {
            if (!e.getValueIsAdjusting()) {
                int row = workoutTable.getSelectedRow();
                if (row >= 0) {
                    populateFormFromRow(row);
                }
            }
        });

        loadExistingWorkoutSchedulesIntoTable();
        JScrollPane tableScrollPane = AppStyle.createTableScrollPane(workoutTable);

        JPanel mainPanel = new JPanel(new BorderLayout(10, 10));
        mainPanel.setOpaque(false);
        mainPanel.add(formPanel, BorderLayout.NORTH);
        mainPanel.add(tableScrollPane, BorderLayout.CENTER);

        submitButton.addActionListener(e -> submitWorkoutSchedule());
        deleteButton.addActionListener(e -> deleteWorkoutSchedule());
        clearButton.addActionListener(e -> clearForm());
        backButton.addActionListener(e -> backAction.run());

        add(AppStyle.createPageScrollPane(mainPanel), BorderLayout.CENTER);

        AppStyle.installEscBehavior(this, backAction);
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

    private void loadExistingWorkoutSchedulesIntoTable() {
        for (WorkoutSchedule schedule : database.getWorkoutSchedules()) {
            workoutTableModel.addRow(rowFor(schedule));
        }
    }

    private Object[] rowFor(WorkoutSchedule schedule) {
        return new Object[] {
                schedule.getMemberName(), schedule.getTrainerName(),
                schedule.getWorkoutType(), schedule.getDay(), schedule.getTime()
        };
    }

    private void populateFormFromRow(int row) {
        selectedRow = row;
        memberBox.setSelectedItem(workoutTableModel.getValueAt(row, 0));
        trainerBox.setSelectedItem(workoutTableModel.getValueAt(row, 1));
        workoutTypeBox.setSelectedItem(workoutTableModel.getValueAt(row, 2));
        dayBox.setSelectedItem(workoutTableModel.getValueAt(row, 3));
        timeBox.setSelectedItem(workoutTableModel.getValueAt(row, 4));

        submitButton.setText("Save Changes");
        deleteButton.setEnabled(true);
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

        WorkoutSchedule schedule = new WorkoutSchedule(memberName, trainerName, workoutType, day, time); // model.WorkoutSchedule

        if (selectedRow >= 0) {
            database.updateWorkoutSchedule(selectedRow, schedule); // -> GymDatabase.updateWorkoutSchedule (re-saves the CSV)
            Object[] row = rowFor(schedule);
            for (int col = 0; col < row.length; col++) {
                workoutTableModel.setValueAt(row[col], selectedRow, col);
            }
            JOptionPane.showMessageDialog(this, "Workout schedule updated successfully.");
        } else {
            database.addWorkoutSchedule(schedule); // -> GymDatabase.addWorkoutSchedule (re-saves the CSV)
            workoutTableModel.addRow(rowFor(schedule));
            JOptionPane.showMessageDialog(this, "Workout schedule saved successfully.");
        }

        clearForm();
    }

    private void deleteWorkoutSchedule() {
        if (selectedRow < 0) {
            return;
        }

        if (!ConfirmDialog.show(this, "Are you sure?")) {
            return;
        }

        database.removeWorkoutSchedule(selectedRow); // -> GymDatabase.removeWorkoutSchedule (re-saves the CSV)
        workoutTableModel.removeRow(selectedRow);
        clearForm();
    }

    private void clearForm() {
        selectedRow = -1;
        memberBox.setSelectedIndex(0);
        trainerBox.setSelectedIndex(0);
        workoutTypeBox.setSelectedIndex(0);
        dayBox.setSelectedIndex(0);
        timeBox.setSelectedIndex(0);

        workoutTable.clearSelection();
        submitButton.setText("Save Schedule");
        deleteButton.setEnabled(false);
    }
}
