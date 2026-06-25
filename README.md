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

## Download

Grab the latest portable build from the
[Releases](https://github.com/v1b3s0/Gym-MGMT/releases) page:

- **Windows** — `Gym-MGMT-<version>-windows.zip`
- **Linux** — `Gym-MGMT-<version>-linux.zip`

Unzip it anywhere and run the launcher inside the `Gym-MGMT/` folder. The build is
self-contained — it bundles its own trimmed Java runtime, so **no system Java is
required**. Your data is saved to a `data/` folder next to the app, keeping the
whole thing portable.

### Windows SmartScreen note

The first time you run the Windows build, Windows may show a **"Windows protected
your PC"** SmartScreen prompt. This is expected: the app isn't code-signed (signing
certificates cost money and require a registered business or hardware token — not
worth it for a free hobby project). The app is safe to run — click
**More info → Run anyway**.

## Running from source

Requires a JDK 25+ on your machine. From the project root:

```bash
./gradlew run
```

## Packaging

A portable, self-contained build per OS — no system Java required to run it:

- [x] Gradle build (Kotlin DSL)
- [x] Portable data + resource handling (data and assets travel with the app)
- [x] App icon generated from code as a multi-resolution Windows `.ico` and Linux `.png`
- [x] Trimmed runtime + portable app image via `jlink` / `jpackage`
- [x] CI matrix building Windows + Linux portable zips
- [x] `v0.1` GitHub Release with both builds attached

> Native builds can't be cross-compiled, so each OS build is produced on its own
> platform — the GitHub Actions matrix builds Windows on a Windows runner and Linux
> on a Linux runner.

## Project structure

```
src/main/java/        application source (Swing UI, models, CSV storage)
src/main/resources/   bundled assets (background image)
src/tools/java/       build-time tools (icon generator) — not shipped
packaging/            release icon assets (Windows .ico, Linux .png)
.github/workflows/    CI: builds both portable zips and publishes releases
```

## Status

`v0.1` is released — portable Windows and Linux builds are on the
[Releases](https://github.com/v1b3s0/Gym-MGMT/releases) page. The app is fully
functional both from source and as a packaged build.
