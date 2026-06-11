package model;

public class Payment {
    private String memberName;
    private String membershipType;
    private String paymentDate;
    private double amount;

    public Payment(String memberName, String membershipType, String paymentDate, double amount) {
        this.memberName = memberName;
        this.membershipType = membershipType;
        this.paymentDate = paymentDate;
        this.amount = amount;
    }

    public String getMemberName() {
        return memberName;
    }

    public String getMembershipType() {
        return membershipType;
    }

    public String getPaymentDate() {
        return paymentDate;
    }

    public double getAmount() {
        return amount;
    }
}
