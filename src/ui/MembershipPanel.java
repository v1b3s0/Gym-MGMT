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
import javax.swing.JTextField;
import javax.swing.table.DefaultTableModel;

import data.GymDatabase;
import model.MembershipPlan;
public class MembershipPanel extends JPanel{
    private GymDatabase database;

    private JTextField nameField;
    private JTextField priceField;
    private JTextField durationField;

    private JComboBox<String> typeBox;

    private JButton submitButton;
    private JButton clearButton;
    private JButton backButton;

    private DefaultTableModel membershipTableModel;
    private JTable membershipTable;

    public MembershipPanel(GymDatabase database) {
        this.database = database;

        setLayout(new BorderLayout());

        JLabel titleLabel = new JLabel("Membership Plan Management", JLabel.CENTER);
        titleLabel.setFont(new Font("Arial", Font.BOLD, 26));

        add(titleLabel, BorderLayout.NORTH);

        Jpanel formPanel = new JPanel(new GridLayout(5, 2, 10, 10));

        formPanel.setBorder(BorderFactory.createEmptyBorder(20, 40, 20, 40));

        membershipTypeBox = new JComboBox<>(new String[] {"Monthly", "Quarterly", "Yearly"});

        priceField = new JTextField();
        durationField = new JTextField();

        submitButton = new JButton("Add Plan");
        clearButton = new JButton("Clear");
        backButton = new JButton("Back");

        formPanel.add(new JLabel("Membership Type:"));
        formPanel.add(membershipTypeBox);

        formPanel.add(new JLabel("Price:"));
        formPanel.add(priceField);

        formPanel.add(new JLabel("Duration (months):"));
        formPanel.add(durationField);

        JPanel buttonPanel = new JPanel();

        buttonPanel.add(submitButton);
        buttonPanel.add(clearButton);
        buttonPanel.add(backButton);

        formPanel.add(buttonPanel);
        formPanel.add(buttonPanel);

        String[] column = {"Membership Type", "Price", "Duration"};

        membershipTableModel = 
        new DefaultTableModel(column, 0);

        membershipTable =
         new JTable(membershipTableModel);

        JScrollPane tableScrollPane =
         new JScrollPane(membershipTable);

        JPanel centerPanel = new JPanel(new BorderLayout(10,10));
        
        mainPanel.add(formPanel, BorderLayout.NORTH);
        mainPanel.add(tableScrollPane, BorderLayout.CENTER);

        add(centerPanel, BorderLayout.CENTER);

        submitButton.addActionListener(e -> SubmitMembership());

        clearButton.addActionListener(e -> ClearForm()); }

        private void SubmitMembership() {
            String type = (String) membershipTypeBox.getSelectedItem();
            String price = priceField.getText().trim();
            String duration = durationField.getText().trim();

            if (price.isEmpty() || duration.isEmpty()) {
                JOptionPane.showMessageDialog(this, "Please fill in all fields.", "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }

            try {
                double price = Double.parseDouble(price);
                int duration = Integer.parseInt(duration);

                MembershipPlan plan = new MembershipPlan(type, price, duration);
                database.addMembershipPlan(plan);

                membershipTableModel.addRow(new Object[] {plan.getType(), plan.getPrice(), plan.getDuration()});

                JoptionPane.showMessageDialog(this, "Membership plan added successfully!");

                ClearForm();
            }

            private void ClearForm() {
                membershipTypeBox.setSelectedIndex(0);
                priceField.setText("");
                durationField.setText("");
            }
        }



       
        
     
    
}