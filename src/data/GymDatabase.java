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
        members = FileManager.loadMembers();
        trainers = FileManager.loadTrainers();
        workoutSchedules = FileManager.loadWorkoutSchedules();
        payments = FileManager.loadPayments();
    }

    public void addMember(Member member) {
        members.add(member);
        FileManager.saveMembers(members);
    }

    public void addTrainer(Trainer trainer) {
        trainers.add(trainer);
        FileManager.saveTrainers(trainers);
    }

    public void addWorkoutSchedule(WorkoutSchedule workoutSchedule) {
        workoutSchedules.add(workoutSchedule);
        FileManager.saveWorkoutSchedules(workoutSchedules);
    }

    public void addPayment(Payment payment) {
        payments.add(payment);
        FileManager.savePayments(payments);
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
