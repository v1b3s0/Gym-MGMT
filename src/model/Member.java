package model;

/** A gym member and their registration details. Plain data holder (POJO). */
public class Member {
    private String name;
    private int age;
    private String phoneNumber;
    private String email;
    private String gender;
    private String membershipType;
    private String fitnessGoals;
    private String notes;

    public Member(String name, int age, String phoneNumber, String email,
            String gender, String membershipType, String fitnessGoals, String notes) {
        this.name = name;
        this.age = age;
        this.phoneNumber = phoneNumber;
        this.email = email;
        this.gender = gender;
        this.membershipType = membershipType;
        this.fitnessGoals = fitnessGoals;
        this.notes = notes;
    }

    public String getName() {
        return name;
    }

    public int getAge() {
        return age;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public String getEmail() {
        return email;
    }

    public String getGender() {
        return gender;
    }

    public String getMembershipType() {
        return membershipType;
    }

    public String getFitnessGoals() {
        return fitnessGoals;
    }

    public String getNotes() {
        return notes;
    }
}
