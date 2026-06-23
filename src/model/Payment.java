package model;

/**
 * A financing record: a payment (positive amount) or a refund (negative
 * amount) made by a member.
 */
public class Payment {
    private String memberName;
    private String type;
    private String membershipType;
    private String paymentDate;
    private double amount;

    public Payment(String memberName, String type, String membershipType, String paymentDate, double amount) {
        this.memberName = memberName;
        this.type = type;
        this.membershipType = membershipType;
        this.paymentDate = paymentDate;
        this.amount = amount;
    }

    public String getMemberName() {
        return memberName;
    }

    public String getType() {
        return type;
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
