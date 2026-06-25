package ui;

import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.awt.GridBagLayout;

import javax.swing.ButtonGroup;
import javax.swing.JCheckBox;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextArea;
import javax.swing.ListSelectionModel;
import javax.swing.table.DefaultTableModel;

import data.GymDatabase;
import model.Member;
import model.MembershipPlan;
import util.ValidationUtil;

/**
 * Member registration screen: a validated form plus a table of members.
 * Selecting a table row loads it into the form for editing or deletion.
 */
public class MemberPanel extends JPanel {
    private GymDatabase database;

    private RoundedTextField nameField;
    private RoundedTextField ageField;
    private RoundedTextField phoneField;
    private RoundedTextField emailField;

    private JRadioButton maleButton;
    private JRadioButton femaleButton;
    private ButtonGroup genderGroup;

    private RoundedComboBox membershipBox;

    private JCheckBox weightLossBox;
    private JCheckBox muscleGainBox;
    private JCheckBox cardioBox;

    private JTextArea notesArea;

    private RoundedButton submitButton;
    private RoundedButton deleteButton;
    private RoundedButton clearButton;
    private RoundedButton backButton;

    private DefaultTableModel memberTableModel;
    private JTable memberTable;

    private int selectedRow = -1;

    public MemberPanel(GymDatabase database, Runnable backAction) {
        this.database = database;

        setLayout(new BorderLayout());
        setBackground(AppStyle.BACKGROUND_COLOR);

        add(AppStyle.createPageTitle("Member Management"), BorderLayout.NORTH);

        JPanel formPanel = new JPanel(new GridBagLayout());
        formPanel.setOpaque(false);
        formPanel.setBorder(AppStyle.FORM_PADDING);

        nameField = new RoundedTextField();
        ageField = new RoundedTextField();
        phoneField = new RoundedTextField();
        emailField = new RoundedTextField();

        maleButton = themedRadio("Male");
        femaleButton = themedRadio("Female");
        genderGroup = new ButtonGroup();
        genderGroup.add(maleButton);
        genderGroup.add(femaleButton);

        JPanel genderPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 12, 0));
        genderPanel.setOpaque(false);
        genderPanel.add(maleButton);
        genderPanel.add(femaleButton);

        membershipBox = new RoundedComboBox(MembershipPlan.labels());

        weightLossBox = themedCheck("Weight Loss");
        muscleGainBox = themedCheck("Muscle Gain");
        cardioBox = themedCheck("Cardio");

        JPanel goalsPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 12, 0));
        goalsPanel.setOpaque(false);
        goalsPanel.add(weightLossBox);
        goalsPanel.add(muscleGainBox);
        goalsPanel.add(cardioBox);

        notesArea = new JTextArea(3, 20);
        notesArea.setBackground(AppStyle.FIELD_BACKGROUND);
        notesArea.setForeground(AppStyle.TEXT_COLOR);
        notesArea.setCaretColor(AppStyle.TEXT_COLOR);
        notesArea.setFont(AppStyle.FIELD_FONT);
        notesArea.setLineWrap(true);
        notesArea.setWrapStyleWord(true);
        notesArea.setBorder(javax.swing.BorderFactory.createEmptyBorder(6, 8, 6, 8));

        JScrollPane notesScroll = new JScrollPane(notesArea);
        notesScroll.setOpaque(false);
        notesScroll.getViewport().setBackground(AppStyle.FIELD_BACKGROUND);
        notesScroll.setBorder(new RoundedBorder(AppStyle.GOLD_COLOR, AppStyle.FIELD_RADIUS, 2,
                new java.awt.Insets(2, 2, 2, 2)));

        submitButton = new RoundedButton("Add Member");
        deleteButton = new RoundedButton("Delete");
        clearButton = new RoundedButton("Clear");
        backButton = new RoundedButton("Back");
        deleteButton.setEnabled(false);

        AppStyle.addFormRow(formPanel, 0, "Member Name:", nameField);
        AppStyle.addFormRow(formPanel, 1, "Age:", ageField);
        AppStyle.addFormRow(formPanel, 2, "Phone Number:", phoneField);
        AppStyle.addFormRow(formPanel, 3, "Email:", emailField);
        AppStyle.addFormRow(formPanel, 4, "Gender:", genderPanel);
        AppStyle.addFormRow(formPanel, 5, "Membership Type:", membershipBox);
        AppStyle.addFormRow(formPanel, 6, "Fitness Goals:", goalsPanel);
        AppStyle.addFormRow(formPanel, 7, "Additional Notes:", notesScroll);

        AppStyle.addFormFullRow(formPanel, 8,
                AppStyle.createButtonRow(submitButton, deleteButton, clearButton, backButton));

        String[] columns = { "Name", "Age", "Phone", "Email", "Gender", "Membership", "Goals", "Notes" };
        memberTableModel = new DefaultTableModel(columns, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
        memberTable = new JTable(memberTableModel);
        memberTable.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        memberTable.getSelectionModel().addListSelectionListener(e -> {
            if (!e.getValueIsAdjusting()) {
                int row = memberTable.getSelectedRow();
                if (row >= 0) {
                    populateFormFromRow(row);
                }
            }
        });

        JScrollPane tableScrollPane = AppStyle.createTableScrollPane(memberTable);
        loadExistingMembersIntoTable();

        JPanel mainPanel = new JPanel(new BorderLayout(10, 10));
        mainPanel.setOpaque(false);
        mainPanel.add(formPanel, BorderLayout.NORTH);
        mainPanel.add(tableScrollPane, BorderLayout.CENTER);

        submitButton.addActionListener(e -> submitMember());
        deleteButton.addActionListener(e -> deleteMember());
        clearButton.addActionListener(e -> clearForm());
        backButton.addActionListener(e -> backAction.run());

        add(AppStyle.createPageScrollPane(mainPanel), BorderLayout.CENTER);

        AppStyle.installEscBehavior(this, backAction);
    }

    private JRadioButton themedRadio(String text) {
        JRadioButton button = new JRadioButton(text);
        button.setOpaque(false);
        button.setForeground(AppStyle.TEXT_COLOR);
        button.setFocusPainted(false);
        return button;
    }

    private JCheckBox themedCheck(String text) {
        JCheckBox box = new JCheckBox(text);
        box.setOpaque(false);
        box.setForeground(AppStyle.TEXT_COLOR);
        box.setFocusPainted(false);
        return box;
    }

    private void populateFormFromRow(int row) {
        selectedRow = row;

        nameField.setText(String.valueOf(memberTableModel.getValueAt(row, 0)));
        ageField.setText(String.valueOf(memberTableModel.getValueAt(row, 1)));
        phoneField.setText(String.valueOf(memberTableModel.getValueAt(row, 2)));
        emailField.setText(String.valueOf(memberTableModel.getValueAt(row, 3)));

        String gender = String.valueOf(memberTableModel.getValueAt(row, 4));
        if (gender.equals("Male")) {
            maleButton.setSelected(true);
        } else {
            femaleButton.setSelected(true);
        }

        membershipBox.setSelectedItem(memberTableModel.getValueAt(row, 5));

        String goals = String.valueOf(memberTableModel.getValueAt(row, 6));
        weightLossBox.setSelected(goals.contains("Weight Loss"));
        muscleGainBox.setSelected(goals.contains("Muscle Gain"));
        cardioBox.setSelected(goals.contains("Cardio"));

        notesArea.setText(String.valueOf(memberTableModel.getValueAt(row, 7)));

        submitButton.setText("Save Changes");
        deleteButton.setEnabled(true);
    }

    private void loadExistingMembersIntoTable() {
        for (Member member : database.getMembers()) { // GymDatabase.getMembers (data loaded from CSV)
            memberTableModel.addRow(rowFor(member));
        }
    }

    private Object[] rowFor(Member member) {
        return new Object[] {
                member.getName(), member.getAge(), member.getPhoneNumber(), member.getEmail(),
                member.getGender(), member.getMembershipType(), member.getFitnessGoals(), member.getNotes()
        };
    }

    private void submitMember() {
        String name = nameField.getText().trim();
        String ageText = ageField.getText().trim();
        String phone = phoneField.getText().trim();
        String email = emailField.getText().trim();

        // All checks below call util.ValidationUtil.
        if (ValidationUtil.isEmpty(name) || ValidationUtil.isEmpty(ageText)
                || ValidationUtil.isEmpty(phone) || ValidationUtil.isEmpty(email)) {
            JOptionPane.showMessageDialog(this, "Please fill in all required fields.");
            return;
        }

        if (!ValidationUtil.isPositiveInteger(ageText)) {
            JOptionPane.showMessageDialog(this, "Age must be a positive number.");
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

        int age = Integer.parseInt(ageText);

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
        if (weightLossBox.isSelected()) goals += "Weight Loss ";
        if (muscleGainBox.isSelected()) goals += "Muscle Gain ";
        if (cardioBox.isSelected()) goals += "Cardio ";
        goals = goals.trim();

        String notes = notesArea.getText().trim();

        Member member = new Member(name, age, phone, email, gender, membershipType, goals, notes); // model.Member

        if (selectedRow >= 0) {
            // Editing the selected row -> GymDatabase.updateMember (re-saves the CSV)
            database.updateMember(selectedRow, member);
            Object[] row = rowFor(member);
            for (int col = 0; col < row.length; col++) {
                memberTableModel.setValueAt(row[col], selectedRow, col);
            }
            JOptionPane.showMessageDialog(this, "Member updated successfully.");
        } else {
            // New member -> GymDatabase.addMember (appends and re-saves the CSV)
            database.addMember(member);
            memberTableModel.addRow(rowFor(member));
            JOptionPane.showMessageDialog(this, "Member added successfully.");
        }

        clearForm();
    }

    private void deleteMember() {
        if (selectedRow < 0) {
            return;
        }

        if (!ConfirmDialog.show(this, "Are you sure?")) {
            return;
        }

        database.removeMember(selectedRow); // -> GymDatabase.removeMember (re-saves the CSV)
        memberTableModel.removeRow(selectedRow);
        clearForm();
    }

    private void clearForm() {
        selectedRow = -1;

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

        memberTable.clearSelection();
        submitButton.setText("Add Member");
        deleteButton.setEnabled(false);
    }
}
