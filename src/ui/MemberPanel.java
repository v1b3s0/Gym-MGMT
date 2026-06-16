package ui;

import java.awt.BorderLayout;
import java.awt.GridLayout;

import javax.swing.ButtonGroup;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.SwingConstants;
import javax.swing.table.DefaultTableModel;

import data.GymDatabase;
import model.Member;

public class MemberPanel extends JPanel {
    private GymDatabase database;

    private JTextField nameField;
    private JTextField ageField;
    private JTextField phoneField;
    private JTextField emailField;

    private JRadioButton maleButton;
    private JRadioButton femaleButton;

    private JComboBox<String> membershipBox;

    private JCheckBox weightLossBox;
    private JCheckBox muscleGainBox;
    private JCheckBox cardioBox;

    private JTextArea notesArea;

    private JButton submitButton;
    private JButton clearButton;
    private JButton backButton;

    private DefaultTableModel memberTableModel;
    private JTable memberTable;

    private ButtonGroup genderGroup;

    public MemberPanel(GymDatabase database, Runnable backAction) {
        this.database = database;

        setLayout(new BorderLayout());

        JLabel titleLabel = new JLabel("Member Management", SwingConstants.CENTER);
        titleLabel.setFont(AppStyle.TITLE_FONT);

        add(titleLabel, BorderLayout.NORTH);

        JPanel formPanel = new JPanel(new GridLayout(9, 2, 10, 10));
        formPanel.setBorder(AppStyle.FORM_PADDING);

        nameField = new JTextField();
        ageField = new JTextField();
        phoneField = new JTextField();
        emailField = new JTextField();

        maleButton = new JRadioButton("Male");
        femaleButton = new JRadioButton("Female");

        genderGroup = new ButtonGroup();
        genderGroup.add(maleButton);
        genderGroup.add(femaleButton);

        JPanel genderPanel = new JPanel();
        genderPanel.add(maleButton);
        genderPanel.add(femaleButton);

        membershipBox = new JComboBox<>(new String[] {
                "Monthly",
                "Quarterly",
                "Yearly"
        });

        weightLossBox = new JCheckBox("Weight Loss");
        muscleGainBox = new JCheckBox("Muscle Gain");
        cardioBox = new JCheckBox("Cardio");

        JPanel goalsPanel = new JPanel();
        goalsPanel.add(weightLossBox);
        goalsPanel.add(muscleGainBox);
        goalsPanel.add(cardioBox);

        notesArea = new JTextArea(3, 20);
        JScrollPane notesScrollPane = new JScrollPane(notesArea);

        submitButton = new JButton("Submit");
        clearButton = new JButton("Clear");
        backButton = new JButton("Back");

        formPanel.add(new JLabel("Member Name:"));
        formPanel.add(nameField);

        formPanel.add(new JLabel("Age:"));
        formPanel.add(ageField);

        formPanel.add(new JLabel("Phone Number:"));
        formPanel.add(phoneField);

        formPanel.add(new JLabel("Email:"));
        formPanel.add(emailField);

        formPanel.add(new JLabel("Gender:"));
        formPanel.add(genderPanel);

        formPanel.add(new JLabel("Membership Type:"));
        formPanel.add(membershipBox);

        formPanel.add(new JLabel("Fitness Goals:"));
        formPanel.add(goalsPanel);

        formPanel.add(new JLabel("Additional Notes:"));
        formPanel.add(notesScrollPane);

        JPanel buttonPanel = new JPanel();
        buttonPanel.add(submitButton);
        buttonPanel.add(clearButton);
        buttonPanel.add(backButton);

        formPanel.add(new JLabel());
        formPanel.add(buttonPanel);

        String[] columns = {
                "Name",
                "Age",
                "Phone",
                "Email",
                "Gender",
                "Membership",
                "Goals",
                "Notes"
        };

        memberTableModel = new DefaultTableModel(columns, 0);
        memberTable = new JTable(memberTableModel);

        JScrollPane tableScrollPane = AppStyle.createTableScrollPane(memberTable);

        loadExistingMembersIntoTable();

        JPanel mainPanel = new JPanel(new BorderLayout(10, 10));
        mainPanel.add(formPanel, BorderLayout.NORTH);
        mainPanel.add(tableScrollPane, BorderLayout.CENTER);

        submitButton.addActionListener(e -> submitMember());
        clearButton.addActionListener(e -> clearForm());
        backButton.addActionListener(e -> backAction.run());

        add(AppStyle.createPageScrollPane(mainPanel), BorderLayout.CENTER);
    }

    private void loadExistingMembersIntoTable() {
        for (Member member : database.getMembers()) {
            memberTableModel.addRow(new Object[] {
                    member.getName(),
                    member.getAge(),
                    member.getPhoneNumber(),
                    member.getEmail(),
                    member.getGender(),
                    member.getMembershipType(),
                    member.getFitnessGoals(),
                    member.getNotes()
            });
        }
    }

    private void submitMember() {
        String name = nameField.getText().trim();
        String ageText = ageField.getText().trim();
        String phone = phoneField.getText().trim();
        String email = emailField.getText().trim();

        if (name.isEmpty() || ageText.isEmpty() || phone.isEmpty() || email.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Please fill in all required fields.");
            return;
        }

        int age;

        try {
            age = Integer.parseInt(ageText);
        } catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(this, "Age must be a number.");
            return;
        }

        String gender;

        if (maleButton.isSelected()) {
            gender = "Male";
        } else if (femaleButton.isSelected()) {
            gender = "Female";
        } else {
            JOptionPane.showMessageDialog(this, "Please select a gender.");
            return;
        }

        String membershipType = String.valueOf(membershipBox.getSelectedItem());

        String goals = "";

        if (weightLossBox.isSelected()) {
            goals += "Weight Loss ";
        }

        if (muscleGainBox.isSelected()) {
            goals += "Muscle Gain ";
        }

        if (cardioBox.isSelected()) {
            goals += "Cardio ";
        }

        goals = goals.trim();

        String notes = notesArea.getText().trim();

        Member member = new Member(name, age, phone, email, gender, membershipType, goals, notes);

        database.addMember(member);

        memberTableModel.addRow(new Object[] {
                member.getName(),
                member.getAge(),
                member.getPhoneNumber(),
                member.getEmail(),
                member.getGender(),
                member.getMembershipType(),
                member.getFitnessGoals(),
                member.getNotes()
        });

        JOptionPane.showMessageDialog(this, "Member added successfully.");

        clearForm();
    }

    private void clearForm() {
        nameField.setText("");
        ageField.setText("");
        phoneField.setText("");
        emailField.setText("");

        genderGroup.clearSelection();

        membershipBox.setSelectedIndex(0);

        weightLossBox.setSelected(false);
        muscleGainBox.setSelected(false);
        cardioBox.setSelected(false);

        notesArea.setText("");
    }
}
