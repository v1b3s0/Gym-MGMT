plugins {
    application
}

application {
    mainClass = "Main"
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
