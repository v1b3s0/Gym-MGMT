package ui;

import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.GridBagLayout;
import java.time.LocalDate;

import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.SwingConstants;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;

import data.GymDatabase;
import model.Member;
import model.MembershipPlan;
import model.Payment;
import util.ValidationUtil;

/**
 * Financing (payment history) screen: records payments and refunds. Selecting a
 * membership type auto-fills the default amount; the table shows payments in
 * green and refunds in red.
 */
public class FinancingPanel extends JPanel {
    private GymDatabase database;

    private RoundedComboBox memberBox;
    private RoundedComboBox typeBox;
    private RoundedComboBox membershipBox;
    private RoundedTextField dateField;
    private RoundedTextField amountField;

    private RoundedButton submitButton;
    private RoundedButton clearButton;
    private RoundedButton backButton;

    private DefaultTableModel financingTableModel;
    private JTable financingTable;

    public FinancingPanel(GymDatabase database, Runnable backAction) {
        this.database = database;

        setLayout(new BorderLayout());
        setBackground(AppStyle.BACKGROUND_COLOR);

        add(AppStyle.createPageTitle("Financing"), BorderLayout.NORTH);

        JPanel formPanel = new JPanel(new GridBagLayout());
        formPanel.setOpaque(false);
        formPanel.setBorder(AppStyle.FORM_PADDING);

        memberBox = new RoundedComboBox();
        loadMembersIntoComboBox();

        typeBox = new RoundedComboBox(new String[] { "Payment", "Refund" });
        membershipBox = new RoundedComboBox(MembershipPlan.labels());

        dateField = new RoundedTextField(LocalDate.now().toString());
        amountField = new RoundedTextField();

        submitButton = new RoundedButton("Add Entry");
        clearButton = new RoundedButton("Clear");
        backButton = new RoundedButton("Back");

        AppStyle.addFormRow(formPanel, 0, "Member:", memberBox);
        AppStyle.addFormRow(formPanel, 1, "Transaction Type:", typeBox);
        AppStyle.addFormRow(formPanel, 2, "Membership Type:", membershipBox);
        AppStyle.addFormRow(formPanel, 3, "Date:", dateField);
        AppStyle.addFormRow(formPanel, 4, "Amount:", amountField);

        AppStyle.addFormFullRow(formPanel, 5,
                AppStyle.createButtonRow(submitButton, clearButton, backButton));

        String[] columns = { "Member", "Type", "Membership Type", "Date", "Amount" };
        financingTableModel = new DefaultTableModel(columns, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
        financingTable = new JTable(financingTableModel);

        loadExistingEntriesIntoTable();
        JScrollPane tableScrollPane = AppStyle.createTableScrollPane(financingTable);
        financingTable.getColumnModel().getColumn(4).setCellRenderer(new AmountRenderer());

        JPanel mainPanel = new JPanel(new BorderLayout(10, 10));
        mainPanel.setOpaque(false);
        mainPanel.add(formPanel, BorderLayout.NORTH);
        mainPanel.add(tableScrollPane, BorderLayout.CENTER);

        // Selecting a membership type (or switching payment/refund) prefills the
        // default amount from the plan's static price.
        membershipBox.addActionListener(e -> updateDefaultAmount());
        typeBox.addActionListener(e -> updateDefaultAmount());

        submitButton.addActionListener(e -> submitEntry());
        clearButton.addActionListener(e -> clearForm());
        backButton.addActionListener(e -> backAction.run());

        add(AppStyle.createPageScrollPane(mainPanel), BorderLayout.CENTER);

        AppStyle.installEscBehavior(this, backAction);

        updateDefaultAmount();
    }

    private void loadMembersIntoComboBox() {
        memberBox.addItem("Select Member");
        for (Member member : database.getMembers()) {
            memberBox.addItem(member.getName());
        }
    }

    private void loadExistingEntriesIntoTable() {
        for (Payment payment : database.getPayments()) {
            financingTableModel.addRow(rowFor(payment));
        }
    }

    private Object[] rowFor(Payment payment) {
        return new Object[] {
                payment.getMemberName(), payment.getType(), payment.getMembershipType(),
                payment.getPaymentDate(), payment.getAmount()
        };
    }

    private void updateDefaultAmount() {
        // Pull the plan's price from model.MembershipPlan (shared with MembershipPanel).
        double price = MembershipPlan.priceFor(String.valueOf(membershipBox.getSelectedItem()));
        boolean refund = "Refund".equals(typeBox.getSelectedItem());
        double signed = refund ? -price : price;
        amountField.setText(formatAmount(signed));
    }

    private String formatAmount(double value) {
        if (value == Math.floor(value)) {
            return String.valueOf((long) value);
        }
        return String.valueOf(value);
    }

    private void submitEntry() {
        String memberName = String.valueOf(memberBox.getSelectedItem());
        String type = String.valueOf(typeBox.getSelectedItem());
        String membershipType = String.valueOf(membershipBox.getSelectedItem());
        String date = dateField.getText().trim();
        String amountText = amountField.getText().trim();

        if (memberName.equals("Select Member")) {
            JOptionPane.showMessageDialog(this, "Please select a member.");
            return;
        }

        // Date/amount checks below use util.ValidationUtil; a payment must be
        // positive and a refund negative.
        if (ValidationUtil.isEmpty(date) || ValidationUtil.isEmpty(amountText)) {
            JOptionPane.showMessageDialog(this, "Please fill in all required fields.");
            return;
        }

        if (!ValidationUtil.isValidDate(date)) {
            JOptionPane.showMessageDialog(this, "Date must be in YYYY-MM-DD format.");
            return;
        }

        if (type.equals("Payment") && !ValidationUtil.isPositiveDouble(amountText)) {
            JOptionPane.showMessageDialog(this, "A payment amount must be a positive number.");
            return;
        }

        if (type.equals("Refund") && !ValidationUtil.isNegativeDouble(amountText)) {
            JOptionPane.showMessageDialog(this, "A refund amount must be a negative number (e.g. -100).");
            return;
        }

        double amount = Double.parseDouble(amountText);

        Payment payment = new Payment(memberName, type, membershipType, date, amount); // model.Payment
        database.addPayment(payment); // -> GymDatabase.addPayment (re-saves data/payments.csv)
        financingTableModel.addRow(rowFor(payment));

        JOptionPane.showMessageDialog(this, type + " recorded successfully.");
        clearForm();
    }

    private void clearForm() {
        memberBox.setSelectedIndex(0);
        typeBox.setSelectedIndex(0);
        membershipBox.setSelectedIndex(0);
        dateField.setText(LocalDate.now().toString());
        updateDefaultAmount();
    }

    /** Renders the amount in green for payments and red for refunds. */
    private static class AmountRenderer extends DefaultTableCellRenderer {
        AmountRenderer() {
            setHorizontalAlignment(SwingConstants.RIGHT);
        }

        @Override
        public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected,
                boolean hasFocus, int row, int column) {
            super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);

            double amount = 0;
            try {
                amount = Double.parseDouble(String.valueOf(value));
            } catch (NumberFormatException ignored) {
                // leave as 0
            }

            setForeground(amount < 0 ? AppStyle.REFUND_RED : AppStyle.PAYMENT_GREEN);
            return this;
        }
    }
}
