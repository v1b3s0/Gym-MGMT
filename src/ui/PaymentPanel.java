package ui;

import java.awt.BorderLayout;
import java.awt.GridLayout;
import java.time.LocalDate;

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
import model.Payment;
import util.ValidationUtil;

public class PaymentPanel extends JPanel {
    private GymDatabase database;

    private JComboBox<String> memberBox;
    private JComboBox<String> membershipBox;
    private JTextField paymentDateField;
    private JTextField amountField;

    private JButton submitButton;
    private JButton clearButton;
    private JButton backButton;

    private DefaultTableModel paymentTableModel;
    private JTable paymentTable;

    public PaymentPanel(GymDatabase database, Runnable backAction) {
        this.database = database;

        setLayout(new BorderLayout());

        JLabel titleLabel = new JLabel("Payment History", SwingConstants.CENTER);
        titleLabel.setFont(AppStyle.TITLE_FONT);

        add(titleLabel, BorderLayout.NORTH);

        JPanel formPanel = new JPanel(new GridLayout(5, 2, 10, 10));
        formPanel.setBorder(AppStyle.FORM_PADDING);

        memberBox = new JComboBox<>();
        loadMembersIntoComboBox();

        membershipBox = new JComboBox<>(new String[] {
                "Monthly",
                "Quarterly",
                "Yearly"
        });

        paymentDateField = new JTextField(LocalDate.now().toString());
        amountField = new JTextField();

        submitButton = new JButton("Add Payment");
        clearButton = new JButton("Clear");
        backButton = new JButton("Back");

        formPanel.add(new JLabel("Member:"));
        formPanel.add(memberBox);

        formPanel.add(new JLabel("Membership Type:"));
        formPanel.add(membershipBox);

        formPanel.add(new JLabel("Payment Date:"));
        formPanel.add(paymentDateField);

        formPanel.add(new JLabel("Amount:"));
        formPanel.add(amountField);

        JPanel buttonPanel = new JPanel();
        buttonPanel.add(submitButton);
        buttonPanel.add(clearButton);
        buttonPanel.add(backButton);

        formPanel.add(new JLabel());
        formPanel.add(buttonPanel);

        String[] columns = {
                "Member",
                "Membership Type",
                "Payment Date",
                "Amount"
        };

        paymentTableModel = new DefaultTableModel(columns, 0);
        paymentTable = new JTable(paymentTableModel);

        loadExistingPaymentsIntoTable();

        JScrollPane tableScrollPane = AppStyle.createTableScrollPane(paymentTable);

        JPanel mainPanel = new JPanel(new BorderLayout(10, 10));
        mainPanel.add(formPanel, BorderLayout.NORTH);
        mainPanel.add(tableScrollPane, BorderLayout.CENTER);

        submitButton.addActionListener(e -> submitPayment());
        clearButton.addActionListener(e -> clearForm());
        backButton.addActionListener(e -> backAction.run());

        add(AppStyle.createPageScrollPane(mainPanel), BorderLayout.CENTER);
    }

    private void loadMembersIntoComboBox() {
        memberBox.addItem("Select Member");

        for (Member member : database.getMembers()) {
            memberBox.addItem(member.getName());
        }
    }

    private void loadExistingPaymentsIntoTable() {
        for (Payment payment : database.getPayments()) {
            paymentTableModel.addRow(new Object[] {
                    payment.getMemberName(),
                    payment.getMembershipType(),
                    payment.getPaymentDate(),
                    payment.getAmount()
            });
        }
    }

    private void submitPayment() {
        String memberName = String.valueOf(memberBox.getSelectedItem());
        String membershipType = String.valueOf(membershipBox.getSelectedItem());
        String paymentDate = paymentDateField.getText().trim();
        String amountText = amountField.getText().trim();

        if (memberName.equals("Select Member")) {
            JOptionPane.showMessageDialog(this, "Please select a member.");
            return;
        }

        if (ValidationUtil.isEmpty(paymentDate) || ValidationUtil.isEmpty(amountText)) {
            JOptionPane.showMessageDialog(this, "Please fill in all required fields.");
            return;
        }

        if (!ValidationUtil.isValidDate(paymentDate)) {
            JOptionPane.showMessageDialog(this, "Payment date must be in YYYY-MM-DD format.");
            return;
        }

        if (!ValidationUtil.isPositiveDouble(amountText)) {
            JOptionPane.showMessageDialog(this, "Amount must be a positive number.");
            return;
        }

        double amount = Double.parseDouble(amountText);

        Payment payment = new Payment(memberName, membershipType, paymentDate, amount);

        database.addPayment(payment);

        paymentTableModel.addRow(new Object[] {
                payment.getMemberName(),
                payment.getMembershipType(),
                payment.getPaymentDate(),
                payment.getAmount()
        });

        JOptionPane.showMessageDialog(this, "Payment added successfully.");

        clearForm();
    }

    private void clearForm() {
        memberBox.setSelectedIndex(0);
        membershipBox.setSelectedIndex(0);
        paymentDateField.setText(LocalDate.now().toString());
        amountField.setText("");
    }
}
