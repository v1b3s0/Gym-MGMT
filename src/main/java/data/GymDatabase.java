package data;

import java.util.ArrayList;

import model.Member;
import model.Payment;
import model.Trainer;
import model.WorkoutSchedule;

/**
 * In-memory store for all records. Loaded from CSV on startup and saved back
 * through {@link FileManager} after every change, so data persists between runs.
 */
public class GymDatabase {
    private ArrayList<Member> members;
    private ArrayList<Trainer> trainers;
    private ArrayList<WorkoutSchedule> workoutSchedules;
    private ArrayList<Payment> payments;

    public GymDatabase() {
        // Each load* call reads one CSV file in data.FileManager and returns a list.
        members = FileManager.loadMembers();
        trainers = FileManager.loadTrainers();
        workoutSchedules = FileManager.loadWorkoutSchedules();
        payments = FileManager.loadPayments();
    }

    // Members — add/update/remove then re-write data/members.csv via FileManager.saveMembers
    public void addMember(Member member) {
        members.add(member);
        FileManager.saveMembers(members); // -> data.FileManager.saveMembers
    }

    public void updateMember(int index, Member updated) {
        members.set(index, updated);
        FileManager.saveMembers(members);
    }

    public void removeMember(int index) {
        members.remove(index);
        FileManager.saveMembers(members);
    }

    // Trainers
    public void addTrainer(Trainer trainer) {
        trainers.add(trainer);
        FileManager.saveTrainers(trainers);
    }

    public void updateTrainer(int index, Trainer updated) {
        trainers.set(index, updated);
        FileManager.saveTrainers(trainers);
    }

    public void removeTrainer(int index) {
        trainers.remove(index);
        FileManager.saveTrainers(trainers);
    }

    // Workout schedules
    public void addWorkoutSchedule(WorkoutSchedule workoutSchedule) {
        workoutSchedules.add(workoutSchedule);
        FileManager.saveWorkoutSchedules(workoutSchedules);
    }

    public void updateWorkoutSchedule(int index, WorkoutSchedule updated) {
        workoutSchedules.set(index, updated);
        FileManager.saveWorkoutSchedules(workoutSchedules);
    }

    public void removeWorkoutSchedule(int index) {
        workoutSchedules.remove(index);
        FileManager.saveWorkoutSchedules(workoutSchedules);
    }

    // Payments (history only — add)
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
