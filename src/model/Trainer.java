package model;

/** A trainer, their specialization, and the member they are assigned to. */
public class Trainer {
    private String name;
    private String phoneNumber;
    private String email;
    private String specialization;
    private String assignedMemberName;

    public Trainer(String name, String phoneNumber, String email,
            String specialization, String assignedMemberName) {
        this.name = name;
        this.phoneNumber = phoneNumber;
        this.email = email;
        this.specialization = specialization;
        this.assignedMemberName = assignedMemberName;
    }

    public String getName() {
        return name;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public String getEmail() {
        return email;
    }

    public String getSpecialization() {
        return specialization;
    }

    public String getAssignedMemberName() {
        return assignedMemberName;
    }
}
