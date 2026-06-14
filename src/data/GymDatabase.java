    package data;

import java.util.ArrayList;

import model.Member;
import model.Payment;
import model.Trainer;
import model.WorkoutSchedule;

public class GymDatabase {
    private ArrayList<Member> members;
    private ArrayList<Trainer> trainers;
    private ArrayList<WorkoutSchedule> workoutSchedules;
    private ArrayList<Payment> payments;

    public GymDatabase() {
        members = new ArrayList<>();
        trainers = new ArrayList<>();
        workoutSchedules = new ArrayList<>();
        payments = new ArrayList<>();
    }

    public void addMember(Member member) {
        members.add(member);
    }

    public void addTrainer(Trainer trainer) {
        trainers.add(trainer);
    }

    public void addWorkoutSchedule(WorkoutSchedule workoutSchedule) {
        workoutSchedules.add(workoutSchedule);
    }

    public void addPayment(Payment payment) {
        payments.add(payment);
    }

    public ArrayList<Member> getMembers() {
        return members;
    }

    public ArrayList<Trainer> getTrainers() {
        return trainers;
    }

    public ArrayList<WorkoutSchedule> getWorkoutSchedules() {
        return workoutSchedules;
    }

    public ArrayList<Payment> getPayments() {
        return payments;
    }
}
