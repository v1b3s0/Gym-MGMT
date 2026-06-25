# Gym Management System

A desktop application for managing a gym's members, trainers, schedules, and
payments — built with pure Java Swing and a custom-themed UI.

## About this project

This started life as the GUI project for my Object-Oriented Programming class.
After the course wrapped up, I kept it as a **personal learning project** — not
to add more features, but to learn how to take a plain Java desktop app and
**package it as a portable native build for both Windows and Linux**, the way a
real shipped desktop app would be distributed.

So the interesting part of this repo isn't just the app — it's the toolchain
around it: migrating to Gradle, trimming a custom runtime with `jlink`, and
producing self-contained, portable builds with `jpackage`.

## Features

- **Members** — add, edit, and track gym members and their details
- **Membership plans** — manage membership types and pricing
- **Trainers** — manage trainers and their member assignments
- **Workout schedules** — assign workouts to members and trainers
- **Financing** — record and review payments
- **Login** — simple authentication gate before the dashboard
- **Custom UI** — hand-styled Swing components (rounded fields, buttons,
  scrollbars), a blurred background, and an app icon drawn entirely in code

## Tech stack

- **Java 25** (Eclipse Temurin)
- **Swing** — pure JDK, **zero external dependencies**
- **Gradle** with the Kotlin DSL build script
- **CSV** flat-file storage (no database) — data is written to a portable
  `data/` folder so the app stays self-contained

## Running from source

Requires a JDK 25+ on your machine. From the project root:

```bash
./gradlew run
```

## Packaging (in progress)

The goal is a portable, self-contained build per OS — no system Java required to
run it:

- [x] Gradle build (Kotlin DSL)
- [x] Portable data + resource handling (data and assets travel with the app)
- [x] App icon generated as a multi-resolution Windows `.ico`
- [ ] Trimmed runtime + portable app image via `jlink` / `jpackage`
- [ ] CI matrix building Windows + Linux portable zips
- [ ] `v0.1` GitHub Release with both builds attached

> Native installers can't be cross-compiled, so each OS build is produced on its
> own platform.

## Project structure

```
src/main/java/        application source (Swing UI, models, CSV storage)
src/main/resources/   bundled assets (background image)
src/tools/java/       build-time tools (icon generator) — not shipped
packaging/            release assets (Windows .ico)
```

## Status

Early personal project — `v0.1` packaging work is ongoing. The app itself is
fully functional from source.
