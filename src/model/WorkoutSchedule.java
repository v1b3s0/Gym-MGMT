package model;

public class WorkoutSchedule {
    private String memberName;
    private String trainerName;
    private String workoutType;
    private String day;
    private String time;

    public WorkoutSchedule(String memberName, String trainerName, String workoutType,
            String day, String time) {
        this.memberName = memberName;
        this.trainerName = trainerName;
        this.workoutType = workoutType;
        this.day = day;
        this.time = time;
    }

    public String getMemberName() {
        return memberName;
    }

    public String getTrainerName() {
        return trainerName;
    }

    public String getWorkoutType() {
        return workoutType;
    }

    public String getDay() {
        return day;
    }

    public String getTime() {
        return time;
    }
}
