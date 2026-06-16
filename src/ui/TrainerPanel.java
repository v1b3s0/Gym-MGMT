package ui;

import java.awt.BorderLayout;
import java.awt.GridLayout;

import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.SwingConstants;
import javax.swing.table.DefaultTableModel;

import data.GymDatabase;
import model.Member;
import model.Trainer;

public class TrainerPanel extends JPanel {
    private GymDatabase database;

    private JTextField nameField;
    private JTextField phoneField;
    private JTextField emailField;

    private JComboBox<String> specializationBox;
    private JComboBox<String> assignedMemberBox;

    private JButton submitButton;
    private JButton clearButton;
    private JButton backButton;

    private DefaultTableModel trainerTableModel;
    private JTable trainerTable;

    public TrainerPanel(GymDatabase database, Runnable backAction) {
        this.database = database;

        setLayout(new BorderLayout());

        JLabel titleLabel = new JLabel("Trainer Management", SwingConstants.CENTER);
        titleLabel.setFont(AppStyle.TITLE_FONT);

        add(titleLabel, BorderLayout.NORTH);

        JPanel formPanel = new JPanel(new GridLayout(6, 2, 10, 10));
        formPanel.setBorder(AppStyle.FORM_PADDING);

        nameField = new JTextField();
        phoneField = new JTextField();
        emailField = new JTextField();

        specializationBox = new JComboBox<>(new String[] {
                "Weight Loss",
                "Muscle Gain",
                "Cardio",
                "Strength Training",
                "General Fitness"
        });

        assignedMemberBox = new JComboBox<>();
        loadMembersIntoComboBox();

        submitButton = new JButton("Submit");
        clearButton = new JButton("Clear");
        backButton = new JButton("Back");

        formPanel.add(new JLabel("Trainer Name:"));
        formPanel.add(nameField);

        formPanel.add(new JLabel("Phone Number:"));
        formPanel.add(phoneField);

        formPanel.add(new JLabel("Email:"));
        formPanel.add(emailField);

        formPanel.add(new JLabel("Specialization:"));
        formPanel.add(specializationBox);

        formPanel.add(new JLabel("Assign to Member:"));
        formPanel.add(assignedMemberBox);

        JPanel buttonPanel = new JPanel();
        buttonPanel.add(submitButton);
        buttonPanel.add(clearButton);
        buttonPanel.add(backButton);

        formPanel.add(new JLabel());
        formPanel.add(buttonPanel);

        String[] columns = {
                "Name",
                "Phone",
                "Email",
                "Specialization",
                "Assigned Member"
        };

        trainerTableModel = new DefaultTableModel(columns, 0);
        trainerTable = new JTable(trainerTableModel);

        loadExistingTrainersIntoTable();

        JScrollPane tableScrollPane = AppStyle.createTableScrollPane(trainerTable);

        JPanel mainPanel = new JPanel(new BorderLayout(10, 10));
        mainPanel.add(formPanel, BorderLayout.NORTH);
        mainPanel.add(tableScrollPane, BorderLayout.CENTER);

        submitButton.addActionListener(e -> submitTrainer());
        clearButton.addActionListener(e -> clearForm());
        backButton.addActionListener(e -> backAction.run());

        add(AppStyle.createPageScrollPane(mainPanel), BorderLayout.CENTER);
    }

    private void loadMembersIntoComboBox() {
        assignedMemberBox.addItem("None");

        for (Member member : database.getMembers()) {
            assignedMemberBox.addItem(member.getName());
        }
    }

    private void loadExistingTrainersIntoTable() {
        for (Trainer trainer : database.getTrainers()) {
            trainerTableModel.addRow(new Object[] {
                    trainer.getName(),
                    trainer.getPhoneNumber(),
                    trainer.getEmail(),
                    trainer.getSpecialization(),
                    trainer.getAssignedMemberName()
            });
        }
    }

    private void submitTrainer() {
        String name = nameField.getText().trim();
        String phone = phoneField.getText().trim();
        String email = emailField.getText().trim();

        if (name.isEmpty() || phone.isEmpty() || email.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Please fill in all required fields.");
            return;
        }

        String specialization = String.valueOf(specializationBox.getSelectedItem());
        String assignedMember = String.valueOf(assignedMemberBox.getSelectedItem());

        Trainer trainer = new Trainer(name, phone, email, specialization, assignedMember);

        database.addTrainer(trainer);

        trainerTableModel.addRow(new Object[] {
                trainer.getName(),
                trainer.getPhoneNumber(),
                trainer.getEmail(),
                trainer.getSpecialization(),
                trainer.getAssignedMemberName()
        });

        JOptionPane.showMessageDialog(this, "Trainer added successfully.");

        clearForm();
    }

    private void clearForm() {
        nameField.setText("");
        phoneField.setText("");
        emailField.setText("");

        specializationBox.setSelectedIndex(0);
        assignedMemberBox.setSelectedIndex(0);
    }
}
