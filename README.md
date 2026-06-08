# Gym Management System

Java Object-Oriented Programming project for a basic **Gym Management System** with a graphical user interface.

## Project Overview

This application simulates a gym management system that can manage:

- Gym members
- Trainers
- Membership plans
- Workout schedules
- Payment history

The project is built for the Object-Oriented Programming course at Al Ain University, College of Engineering.

## Required Features

### 1. Login Screen

- Username field
- Password field
- Login validation using hardcoded credentials
- Successful login opens the main dashboard

### 2. Main Dashboard

The dashboard includes menu options for:

- Member Management
- Trainer Management
- Membership Plans
- Workout Schedule
- Payment History
- Exit

Dashboard requirements:

- Opens in maximized mode
- Uses proper layout management
- Provides navigation between screens

### 3. Member Registration

The member registration form includes:

- Member name
- Age
- Phone number
- Email
- Gender selection using radio buttons
- Membership type using a combo box
- Fitness goals using check boxes
- Additional notes using a text area
- Submit, Clear, and Back buttons

Functionality:

- Validate empty fields
- Store member data
- Display member data in a table

### 4. Trainer Management

- Add trainer information
- Select trainer specialization
- Assign trainers to members

### 5. Workout Schedule

- Select workout type
- Select day and time
- Assign trainer
- Save workout schedule

### 6. Payment History

- Display member payments
- Show membership type
- Show payment date
- Optional search/filter feature

### 7. Exit

- Safely closes the application

## Planned Project Structure

```text
Gym-MGMT/
├── src/
│   ├── Main.java
│   ├── model/
│   │   ├── Member.java
│   │   ├── Trainer.java
│   │   ├── WorkoutSchedule.java
│   │   └── Payment.java
│   ├── ui/
│   │   ├── LoginFrame.java
│   │   ├── DashboardFrame.java
│   │   ├── MemberPanel.java
│   │   ├── TrainerPanel.java
│   │   ├── WorkoutPanel.java
│   │   └── PaymentPanel.java
│   └── data/
│       └── GymDatabase.java
├── screenshots/
├── report/
└── README.md
```

## Technologies

- Java
- Java Swing GUI
- Object-Oriented Programming
- Git and GitHub for collaboration

## OOP Concepts Used

- Classes and objects
- Constructors
- Encapsulation
- Getters and setters
- ArrayLists for storing data
- Separation between model classes, GUI classes, and data management

## Submission Requirements

The final submission should include:

- Java source code files
- Project report
- Screenshots showing functionality
- Demonstration of successful login
- Demonstration of adding user/member data
- Demonstration of viewing different pages

## Important Dates

- Due date: Tuesday, 16 June 2026
- Presentation date: 18 June 2026

## Team Workflow

Before starting work:

```bash
git pull
```

After making changes:

```bash
git add .
git commit -m "Describe the change"
git push
```

Avoid editing the same file at the same time to reduce merge conflicts.
