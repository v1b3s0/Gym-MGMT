package data;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;

import model.Member;
import model.Payment;
import model.Trainer;
import model.WorkoutSchedule;

/**
 * Reads and writes the application's data as CSV files in a {@code data} folder
 * next to the packaged application (or a local {@code data/} folder when run
 * from
 * source). Each save rewrites the whole file; quotes/commas are CSV-escaped.
 */
public class FileManager {
    private static final Path DATA_DIR = resolveDataDir();

    private static final Path MEMBERS_FILE = DATA_DIR.resolve("members.csv");
    private static final Path TRAINERS_FILE = DATA_DIR.resolve("trainers.csv");
    private static final Path WORKOUTS_FILE = DATA_DIR.resolve("workouts.csv");
    private static final Path PAYMENTS_FILE = DATA_DIR.resolve("payments.csv");

    public static void saveMembers(ArrayList<Member> members) {
        try {
            Files.createDirectories(MEMBERS_FILE.getParent());

            ArrayList<String> lines = new ArrayList<>();
            lines.add("Name,Age,Phone,Email,Gender,Membership,Fitness Goals,Notes");

            for (Member member : members) {
                lines.add(
                        escape(member.getName()) + ","
                                + member.getAge() + ","
                                + escape(member.getPhoneNumber()) + ","
                                + escape(member.getEmail()) + ","
                                + escape(member.getGender()) + ","
                                + escape(member.getMembershipType()) + ","
                                + escape(member.getFitnessGoals()) + ","
                                + escape(member.getNotes()));
            }

            Files.write(MEMBERS_FILE, lines);
        } catch (IOException ex) {
            System.out.println("Could not save members file.");
        }
    }

    // Called by the GymDatabase constructor; turns each CSV row into a
    // model.Member.
    public static ArrayList<Member> loadMembers() {
        ArrayList<Member> members = new ArrayList<>();

        if (!Files.exists(MEMBERS_FILE)) {
            return members;
        }

        try {
            List<String> lines = Files.readAllLines(MEMBERS_FILE);

            for (int i = 1; i < lines.size(); i++) {
                String line = lines.get(i);

                if (line.trim().isEmpty()) {
                    continue;
                }

                ArrayList<String> values = parseCsvLine(line);

                if (values.size() != 8) {
                    continue;
                }

                String name = values.get(0);
                int age = Integer.parseInt(values.get(1));
                String phone = values.get(2);
                String email = values.get(3);
                String gender = values.get(4);
                String membership = values.get(5);
                String goals = values.get(6);
                String notes = values.get(7);

                Member member = new Member(name, age, phone, email, gender, membership, goals, notes); // model.Member
                members.add(member);
            }
        } catch (IOException | NumberFormatException ex) {
            System.out.println("Could not load members file.");
        }

        return members;
    }

    public static void saveTrainers(ArrayList<Trainer> trainers) {
        try {
            Files.createDirectories(TRAINERS_FILE.getParent());

            ArrayList<String> lines = new ArrayList<>();
            lines.add("Name,Phone,Email,Specialization,Assigned Member");

            for (Trainer trainer : trainers) {
                lines.add(
                        escape(trainer.getName()) + ","
                                + escape(trainer.getPhoneNumber()) + ","
                                + escape(trainer.getEmail()) + ","
                                + escape(trainer.getSpecialization()) + ","
                                + escape(trainer.getAssignedMemberName()));
            }

            Files.write(TRAINERS_FILE, lines);
        } catch (IOException ex) {
            System.out.println("Could not save trainers file.");
        }
    }

    public static ArrayList<Trainer> loadTrainers() {
        ArrayList<Trainer> trainers = new ArrayList<>();

        if (!Files.exists(TRAINERS_FILE)) {
            return trainers;
        }

        try {
            List<String> lines = Files.readAllLines(TRAINERS_FILE);

            for (int i = 1; i < lines.size(); i++) {
                String line = lines.get(i);

                if (line.trim().isEmpty()) {
                    continue;
                }

                ArrayList<String> values = parseCsvLine(line);

                if (values.size() != 5) {
                    continue;
                }

                String name = values.get(0);
                String phone = values.get(1);
                String email = values.get(2);
                String specialization = values.get(3);
                String assignedMember = values.get(4);

                Trainer trainer = new Trainer(name, phone, email, specialization, assignedMember);
                trainers.add(trainer);
            }
        } catch (IOException ex) {
            System.out.println("Could not load trainers file.");
        }

        return trainers;
    }

    public static void saveWorkoutSchedules(ArrayList<WorkoutSchedule> workoutSchedules) {
        try {
            Files.createDirectories(WORKOUTS_FILE.getParent());

            ArrayList<String> lines = new ArrayList<>();
            lines.add("Member,Trainer,Workout Type,Day,Time");

            for (WorkoutSchedule workoutSchedule : workoutSchedules) {
                lines.add(
                        escape(workoutSchedule.getMemberName()) + ","
                                + escape(workoutSchedule.getTrainerName()) + ","
                                + escape(workoutSchedule.getWorkoutType()) + ","
                                + escape(workoutSchedule.getDay()) + ","
                                + escape(workoutSchedule.getTime()));
            }

            Files.write(WORKOUTS_FILE, lines);
        } catch (IOException ex) {
            System.out.println("Could not save workout schedules file.");
        }
    }

    public static ArrayList<WorkoutSchedule> loadWorkoutSchedules() {
        ArrayList<WorkoutSchedule> workoutSchedules = new ArrayList<>();

        if (!Files.exists(WORKOUTS_FILE)) {
            return workoutSchedules;
        }

        try {
            List<String> lines = Files.readAllLines(WORKOUTS_FILE);

            for (int i = 1; i < lines.size(); i++) {
                String line = lines.get(i);

                if (line.trim().isEmpty()) {
                    continue;
                }

                ArrayList<String> values = parseCsvLine(line);

                if (values.size() != 5) {
                    continue;
                }

                String memberName = values.get(0);
                String trainerName = values.get(1);
                String workoutType = values.get(2);
                String day = values.get(3);
                String time = values.get(4);

                WorkoutSchedule workoutSchedule = new WorkoutSchedule(
                        memberName,
                        trainerName,
                        workoutType,
                        day,
                        time);

                workoutSchedules.add(workoutSchedule);
            }
        } catch (IOException ex) {
            System.out.println("Could not load workout schedules file.");
        }

        return workoutSchedules;
    }

    public static void savePayments(ArrayList<Payment> payments) {
        try {
            Files.createDirectories(PAYMENTS_FILE.getParent());

            ArrayList<String> lines = new ArrayList<>();
            lines.add("Member,Type,Membership Type,Payment Date,Amount");

            for (Payment payment : payments) {
                lines.add(
                        escape(payment.getMemberName()) + ","
                                + escape(payment.getType()) + ","
                                + escape(payment.getMembershipType()) + ","
                                + escape(payment.getPaymentDate()) + ","
                                + payment.getAmount());
            }

            Files.write(PAYMENTS_FILE, lines);
        } catch (IOException ex) {
            System.out.println("Could not save payments file.");
        }
    }

    public static ArrayList<Payment> loadPayments() {
        ArrayList<Payment> payments = new ArrayList<>();

        if (!Files.exists(PAYMENTS_FILE)) {
            return payments;
        }

        try {
            List<String> lines = Files.readAllLines(PAYMENTS_FILE);

            for (int i = 1; i < lines.size(); i++) {
                String line = lines.get(i);

                if (line.trim().isEmpty()) {
                    continue;
                }

                ArrayList<String> values = parseCsvLine(line);

                if (values.size() != 5) {
                    continue;
                }

                String memberName = values.get(0);
                String type = values.get(1);
                String membershipType = values.get(2);
                String paymentDate = values.get(3);
                double amount = Double.parseDouble(values.get(4));

                Payment payment = new Payment(memberName, type, membershipType, paymentDate, amount);
                payments.add(payment);
            }
        } catch (IOException | NumberFormatException ex) {
            System.out.println("Could not load payments file.");
        }

        return payments;
    }

    private static String escape(String value) {
        if (value == null) {
            return "";
        }

        value = value.replace("\n", " ").replace("\r", " ");

        if (value.contains(",") || value.contains("\"")) {
            value = value.replace("\"", "\"\"");
            return "\"" + value + "\"";
        }

        return value;
    }

    private static Path resolveDataDir() {
        String appPath = System.getProperty("jpackage.app-path");

        if (appPath != null) {
            Path exePath = Path.of(appPath);
            return exePath.getParent().resolve("data");
        }
        return Path.of("data");
    }

    private static ArrayList<String> parseCsvLine(String line) {
        ArrayList<String> values = new ArrayList<>();

        String currentValue = "";
        boolean insideQuotes = false;

        for (int i = 0; i < line.length(); i++) {
            char currentChar = line.charAt(i);

            if (currentChar == '"') {
                if (insideQuotes && i + 1 < line.length() && line.charAt(i + 1) == '"') {
                    currentValue += '"';
                    i++;
                } else {
                    insideQuotes = !insideQuotes;
                }
            } else if (currentChar == ',' && !insideQuotes) {
                values.add(currentValue);
                currentValue = "";
            } else {
                currentValue += currentChar;
            }
        }

        values.add(currentValue);

        return values;
    }
}
