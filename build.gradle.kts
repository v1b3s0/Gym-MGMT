plugins {
    application
    // jlink + jpackage: builds a trimmed runtime and a portable native app-image.
    id("org.beryx.runtime") version "2.0.1"
}

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
        // Stamp the .exe with our generated icon. Absolute path so jpackage
        // resolves it regardless of its working directory.
        imageOptions = listOf("--icon", file("packaging/windows/icon.ico").absolutePath)
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
