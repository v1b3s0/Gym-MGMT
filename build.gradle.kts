plugins {
    application
    // jlink + jpackage: builds a trimmed runtime and a portable native app-image.
    id("org.beryx.runtime") version "2.0.1"
}

// Drives the release artifact names (e.g. Gym-MGMT-0.1-windows.zip).
version = "0.1"

// jpackage can't cross-compile, so each OS is built on its own CI runner.
// Detect the current OS once; reused for both the icon choice and the zip name.
val osTag: String = System.getProperty("os.name").lowercase().let { os ->
    when {
        os.contains("win") -> "windows"
        os.contains("linux") -> "linux"
        os.contains("mac") -> "macos"
        else -> "unknown"
    }
}

// jpackage wants a different icon format per OS: .ico on Windows, .png on Linux.
val iconFile = if (osTag == "windows")
    file("packaging/windows/icon.ico")
else
    file("packaging/linux/icon.png")

application {
    mainClass = "Main"
}

runtime {
    // The only module our non-modular Swing app needs (java.base is implicit).
    // jlink pulls java.desktop's transitive modules automatically, so this is enough.
    modules = listOf("java.desktop")

    // Trim the bundled runtime: drop debug symbols, headers, man pages, and
    // compress the module image. Keeps the portable build small.
    options = listOf("--strip-debug", "--no-header-files", "--no-man-pages", "--compress", "zip-6")

    jpackage {
        imageName = "Gym-MGMT"
        // Portable app-image only — no .msi installer (that path needs WiX and
        // installs into Program Files, which we deliberately don't want).
        skipInstaller = true
        // Stamp the launcher with the OS-appropriate icon. Absolute path so
        // jpackage resolves it regardless of its working directory.
        imageOptions = listOf("--icon", iconFile.absolutePath)
    }
}

// A separate "tools" source set: build-time utilities (like the icon
// generator) that may USE the app's classes but never ship in the release.
// Same mechanism as the built-in "test" source set — a one-way mirror:
// tools can see main, main can't see tools.
sourceSets {
    create("tools") {
        java.srcDir("src/tools/java")
        // Give tool code access to the compiled app classes, so the
        // generator can call ui.AppStyle.createAppIcon(...).
        compileClasspath += sourceSets["main"].output
        runtimeClasspath += sourceSets["main"].output
    }
}

// Run with:  ./gradlew generateIcon
// Renders the dumbbell to packaging/windows/icon.ico (run only when the icon changes).
tasks.register<JavaExec>("generateIcon") {
    group = "build"
    description = "Renders the app icon to packaging/windows/icon.ico"
    classpath = sourceSets["tools"].runtimeClasspath
    mainClass = "tools.IconGenerator"
    systemProperty("java.awt.headless", "true") // render without a display
}

// Run with:  ./gradlew portableZip
// Packs the whole jpackage app-image folder into one release-ready zip.
tasks.register<Zip>("portableZip") {
    group = "build"
    description = "Packs the portable app-image into a release zip"
    dependsOn("jpackageImage") // make sure the app-image exists first
    from(layout.buildDirectory.dir("jpackage/Gym-MGMT"))
    into("Gym-MGMT") // nest contents under a top-level folder inside the zip
    archiveFileName = "Gym-MGMT-$version-$osTag.zip"
    destinationDirectory = layout.buildDirectory.dir("portable")
}
