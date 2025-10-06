package org.buildozers.mvnjava21.examples;

import static org.buildozers.mvnjava21.examples.ConsoleColors.blank;
import static org.buildozers.mvnjava21.examples.ConsoleColors.blue;
import static org.buildozers.mvnjava21.examples.ConsoleColors.green;
import static org.buildozers.mvnjava21.examples.ConsoleColors.magentaBold;
import static org.buildozers.mvnjava21.examples.ConsoleColors.multiColor;
import static org.buildozers.mvnjava21.examples.ConsoleColors.separator;
import static org.buildozers.mvnjava21.examples.ConsoleColors.yellow;
import static org.buildozers.mvnjava21.examples.ConsoleColors.yellowBold;
import static org.fusesource.jansi.Ansi.Color.CYAN;
import static org.fusesource.jansi.Ansi.Color.GREEN;
import static org.fusesource.jansi.Ansi.Color.WHITE;

import org.eclipse.collections.impl.factory.Lists;
import org.fusesource.jansi.Ansi;

/**
 * 🔍 Runtime Version Detector Demo
 * 
 * This class demonstrates how the generic RuntimeVersionDetector
 * can detect versions for different Java libraries at runtime.
 */
public class RuntimeVersionDetectorDemo {

    public static class LibraryInfo {
        private final String name;
        private final String version;
        private final String detectionMethod;
        
        public LibraryInfo(String name, String version, String detectionMethod) {
            this.name = name;
            this.version = version;
            this.detectionMethod = detectionMethod;
        }
        
        // Getters
        public String getName() { return name; }
        public String getVersion() { return version; }
        public String getDetectionMethod() { return detectionMethod; }
    }

    public static void main(String[] args) {
        showHeader();
        demonstrateVersionDetection();
        showFooter();
    }

    private static void showHeader() {
        separator();
        yellowBold("🔍 Runtime Version Detector - Generic Library Version Detection");
        green("✨ Demonstrating dynamic version detection for multiple Java libraries");
        separator();
        blank();
    }

    private static void demonstrateVersionDetection() {
        magentaBold("📚 Library Version Detection Results");
        blue("─────────────────────────────────────");

        // Eclipse Collections
        String eclipseCollectionsVersion = RuntimeVersionDetector.detectVersion(
            Lists.class, "eclipse-collections"
        );
        displayLibraryInfo("Eclipse Collections", eclipseCollectionsVersion, "JAR path analysis");

        // Jansi
        String jansiVersion = RuntimeVersionDetector.detectVersion(
            Ansi.class, "jansi"
        );
        displayLibraryInfo("Jansi", jansiVersion, "Package metadata");

        // Lombok (using lombok.extern.slf4j.Slf4j which is runtime available)
        try {
            Class<?> lombokClass = Class.forName("lombok.extern.slf4j.Slf4j");
            String lombokVersion = RuntimeVersionDetector.detectVersion(
                lombokClass, "lombok"
            );
            displayLibraryInfo("Lombok", lombokVersion, "Classpath inspection");
        } catch (ClassNotFoundException e) {
            displayLibraryInfo("Lombok", "not available", "class not found");
        }

        // JUnit (if available)
        try {
            Class<?> junitClass = Class.forName("org.junit.jupiter.api.Test");
            String junitVersion = RuntimeVersionDetector.detectVersion(
                junitClass, "junit-jupiter"
            );
            displayLibraryInfo("JUnit Jupiter", junitVersion, "Runtime reflection");
        } catch (ClassNotFoundException e) {
            displayLibraryInfo("JUnit Jupiter", "not available", "class not found");
        }

        blank();
    }

    private static void displayLibraryInfo(String libraryName, String version, String method) {
        multiColor(
            "📦 " + libraryName + ": ", WHITE,
            "v" + version, GREEN,
            " (detected via " + method + ")", CYAN
        );
    }

    private static void showFooter() {
        separator();
        green("✅ Runtime version detection completed successfully!");
        yellow("🎯 The RuntimeVersionDetector is now generic and works with any Java library");
        separator();
    }
}
