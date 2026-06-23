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
import util.ValidationUtil;

/**
 * Trainer management screen: add/edit/delete trainers, choose a specialization,
 * and assign each trainer to a member.
 */
public class TrainerPanel extends JPanel {
    private GymDatabase database;

    private RoundedTextField nameField;
    private RoundedTextField phoneField;
    private RoundedTextField emailField;

    private RoundedComboBox specializationBox;
    private RoundedComboBox assignedMemberBox;

    private RoundedButton submitButton;
    private RoundedButton deleteButton;
    private RoundedButton clearButton;
    private RoundedButton backButton;

    private DefaultTableModel trainerTableModel;
    private JTable trainerTable;

    private int selectedRow = -1;

    public TrainerPanel(GymDatabase database, Runnable backAction) {
        this.database = database;

        setLayout(new BorderLayout());
        setBackground(AppStyle.BACKGROUND_COLOR);

        add(AppStyle.createPageTitle("Trainer Management"), BorderLayout.NORTH);

        JPanel formPanel = new JPanel(new GridBagLayout());
        formPanel.setOpaque(false);
        formPanel.setBorder(AppStyle.FORM_PADDING);

        nameField = new RoundedTextField();
        phoneField = new RoundedTextField();
        emailField = new RoundedTextField();

        specializationBox = new RoundedComboBox(new String[] {
                "Weight Loss", "Muscle Gain", "Cardio", "Strength Training", "General Fitness"
        });

        assignedMemberBox = new RoundedComboBox();
        loadMembersIntoComboBox();

        submitButton = new RoundedButton("Add Trainer");
        deleteButton = new RoundedButton("Delete");
        clearButton = new RoundedButton("Clear");
        backButton = new RoundedButton("Back");
        deleteButton.setEnabled(false);

        AppStyle.addFormRow(formPanel, 0, "Trainer Name:", nameField);
        AppStyle.addFormRow(formPanel, 1, "Phone Number:", phoneField);
        AppStyle.addFormRow(formPanel, 2, "Email:", emailField);
        AppStyle.addFormRow(formPanel, 3, "Specialization:", specializationBox);
        AppStyle.addFormRow(formPanel, 4, "Assign to Member:", assignedMemberBox);

        AppStyle.addFormFullRow(formPanel, 5,
                AppStyle.createButtonRow(submitButton, deleteButton, clearButton, backButton));

        String[] columns = { "Name", "Phone", "Email", "Specialization", "Assigned Member" };
        trainerTableModel = new DefaultTableModel(columns, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
        trainerTable = new JTable(trainerTableModel);
        trainerTable.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        trainerTable.getSelectionModel().addListSelectionListener(e -> {
            if (!e.getValueIsAdjusting()) {
                int row = trainerTable.getSelectedRow();
                if (row >= 0) {
                    populateFormFromRow(row);
                }
            }
        });

        loadExistingTrainersIntoTable();
        JScrollPane tableScrollPane = AppStyle.createTableScrollPane(trainerTable);

        JPanel mainPanel = new JPanel(new BorderLayout(10, 10));
        mainPanel.setOpaque(false);
        mainPanel.add(formPanel, BorderLayout.NORTH);
        mainPanel.add(tableScrollPane, BorderLayout.CENTER);

        submitButton.addActionListener(e -> submitTrainer());
        deleteButton.addActionListener(e -> deleteTrainer());
        clearButton.addActionListener(e -> clearForm());
        backButton.addActionListener(e -> backAction.run());

        add(AppStyle.createPageScrollPane(mainPanel), BorderLayout.CENTER);

        AppStyle.installEscBehavior(this, backAction);
    }

    private void loadMembersIntoComboBox() {
        assignedMemberBox.addItem("None");
        for (Member member : database.getMembers()) {
            assignedMemberBox.addItem(member.getName());
        }
    }

    private void loadExistingTrainersIntoTable() {
        for (Trainer trainer : database.getTrainers()) {
            trainerTableModel.addRow(rowFor(trainer));
        }
    }

    private Object[] rowFor(Trainer trainer) {
        return new Object[] {
                trainer.getName(), trainer.getPhoneNumber(), trainer.getEmail(),
                trainer.getSpecialization(), trainer.getAssignedMemberName()
        };
    }

    private void populateFormFromRow(int row) {
        selectedRow = row;
        nameField.setText(String.valueOf(trainerTableModel.getValueAt(row, 0)));
        phoneField.setText(String.valueOf(trainerTableModel.getValueAt(row, 1)));
        emailField.setText(String.valueOf(trainerTableModel.getValueAt(row, 2)));
        specializationBox.setSelectedItem(trainerTableModel.getValueAt(row, 3));
        assignedMemberBox.setSelectedItem(trainerTableModel.getValueAt(row, 4));

        submitButton.setText("Save Changes");
        deleteButton.setEnabled(true);
    }

    private void submitTrainer() {
        String name = nameField.getText().trim();
        String phone = phoneField.getText().trim();
        String email = emailField.getText().trim();

        // Field checks use util.ValidationUtil.
        if (ValidationUtil.isEmpty(name) || ValidationUtil.isEmpty(phone) || ValidationUtil.isEmpty(email)) {
            JOptionPane.showMessageDialog(this, "Please fill in all required fields.");
            return;
        }

        if (!ValidationUtil.isValidPhone(phone)) {
            JOptionPane.showMessageDialog(this, "Phone number must contain 7 to 15 digits.");
            return;
        }

        if (!ValidationUtil.isValidEmail(email)) {
            JOptionPane.showMessageDialog(this, "Please enter a valid email address.");
            return;
        }

        String specialization = String.valueOf(specializationBox.getSelectedItem());
        String assignedMember = String.valueOf(assignedMemberBox.getSelectedItem());

        Trainer trainer = new Trainer(name, phone, email, specialization, assignedMember); // model.Trainer

        if (selectedRow >= 0) {
            database.updateTrainer(selectedRow, trainer); // -> GymDatabase.updateTrainer (re-saves the CSV)
            Object[] row = rowFor(trainer);
            for (int col = 0; col < row.length; col++) {
                trainerTableModel.setValueAt(row[col], selectedRow, col);
            }
            JOptionPane.showMessageDialog(this, "Trainer updated successfully.");
        } else {
            database.addTrainer(trainer); // -> GymDatabase.addTrainer (re-saves the CSV)
            trainerTableModel.addRow(rowFor(trainer));
            JOptionPane.showMessageDialog(this, "Trainer added successfully.");
        }

        clearForm();
    }

    private void deleteTrainer() {
        if (selectedRow < 0) {
            return;
        }

        if (!ConfirmDialog.show(this, "Are you sure?")) {
            return;
        }

        database.removeTrainer(selectedRow); // -> GymDatabase.removeTrainer (re-saves the CSV)
        trainerTableModel.removeRow(selectedRow);
        clearForm();
    }

    private void clearForm() {
        selectedRow = -1;
        nameField.setText("");
        phoneField.setText("");
        emailField.setText("");
        specializationBox.setSelectedIndex(0);
        assignedMemberBox.setSelectedIndex(0);

        trainerTable.clearSelection();
        submitButton.setText("Add Trainer");
        deleteButton.setEnabled(false);
    }
}
