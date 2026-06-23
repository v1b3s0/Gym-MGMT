package model;

/**
 * The gym's membership plans and their static prices.
 * Shared by the Membership and Financing screens so prices live in one place.
 */
public enum MembershipPlan {
    MONTHLY("Monthly", 100, "Access for 1 month"),
    QUARTERLY("Quarterly", 250, "Access for 3 months"),
    YEARLY("Yearly", 900, "Access for 12 months");

    private final String label;
    private final double price;
    private final String description;

    MembershipPlan(String label, double price, String description) {
        this.label = label;
        this.price = price;
        this.description = description;
    }

    public String getLabel() {
        return label;
    }

    public double getPrice() {
        return price;
    }

    public String getDescription() {
        return description;
    }

    public static String[] labels() {
        MembershipPlan[] plans = values();
        String[] result = new String[plans.length];
        for (int i = 0; i < plans.length; i++) {
            result[i] = plans[i].label;
        }
        return result;
    }

    public static double priceFor(String label) {
        for (MembershipPlan plan : values()) {
            if (plan.label.equals(label)) {
                return plan.price;
            }
        }
        return 0;
    }
}
