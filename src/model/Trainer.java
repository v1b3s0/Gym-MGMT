package model;

public class Trainer {
    private String name;
    private String phoneNumber;
    private String email;
    private String specialization;

    public Trainer(String name, String phoneNumber, String email, String specialization) {
        this.name = name;
        this.phoneNumber = phoneNumber;
        this.email = email;
        this.specialization = specialization;
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
}
