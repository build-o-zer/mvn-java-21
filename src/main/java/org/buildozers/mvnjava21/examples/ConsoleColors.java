package org.buildozers.mvnjava21.examples;

import static org.fusesource.jansi.Ansi.ansi;
import static org.fusesource.jansi.AnsiConsole.out;

import org.fusesource.jansi.Ansi;

/**
 * 🎨 Console Colors Utility
 * 
 * Simplifies colorful console output by eliminating repetitive Jansi boilerplate.
 * Instead of: ansi().fg(COLOR).a("text").reset()
 * Use: ConsoleColors.cyan("text") or ConsoleColors.green("text")
 */
public final class ConsoleColors {
    
    private ConsoleColors() {
        // Utility class - prevent instantiation
    }
    
    // Basic color methods
    public static void cyan(String text) {
        out().println(ansi().fg(Ansi.Color.CYAN).a(text).reset());
    }
    
    public static void green(String text) {
        out().println(ansi().fg(Ansi.Color.GREEN).a(text).reset());
    }
    
    public static void yellow(String text) {
        out().println(ansi().fg(Ansi.Color.YELLOW).a(text).reset());
    }
    
    public static void blue(String text) {
        out().println(ansi().fg(Ansi.Color.BLUE).a(text).reset());
    }
    
    public static void magenta(String text) {
        out().println(ansi().fg(Ansi.Color.MAGENTA).a(text).reset());
    }
    
    public static void white(String text) {
        out().println(ansi().fg(Ansi.Color.WHITE).a(text).reset());
    }
    
    public static void red(String text) {
        out().println(ansi().fg(Ansi.Color.RED).a(text).reset());
    }
    
    // Bold variations
    public static void yellowBold(String text) {
        out().println(ansi().fg(Ansi.Color.YELLOW).bold().a(text).reset());
    }
    
    public static void magentaBold(String text) {
        out().println(ansi().fg(Ansi.Color.MAGENTA).bold().a(text).reset());
    }
    
    public static void whiteBold(String text) {
        out().println(ansi().fg(Ansi.Color.WHITE).bold().a(text).reset());
    }
    
    // Multi-color line support
    public static void print(String text, Ansi.Color color) {
        out().print(ansi().fg(color).a(text).reset());
    }
    
    public static void printBold(String text, Ansi.Color color) {
        out().print(ansi().fg(color).bold().a(text).reset());
    }
    
    // Separator line
    public static void separator() {
        cyan("━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━");
    }
    
    // Blank line
    public static void blank() {
        out().println();
    }
    
    // Complex multi-color line helper
    public static void multiColor(Object... parts) {
        for (int i = 0; i < parts.length; i += 2) {
            if (i + 1 < parts.length) {
                String text = (String) parts[i];
                Ansi.Color color = (Ansi.Color) parts[i + 1];
                out().print(ansi().fg(color).a(text).reset());
            }
        }
        out().println();
    }
    
    // Helper for label: value patterns (common in showcases)
    public static void labelValue(String label, String value, Ansi.Color labelColor, Ansi.Color valueColor, boolean boldValue) {
        out().print(ansi().fg(labelColor).a(label).reset());
        if (boldValue) {
            out().println(ansi().fg(valueColor).bold().a(value).reset());
        } else {
            out().println(ansi().fg(valueColor).a(value).reset());
        }
    }
    
    // Convenience methods for common label:value patterns
    public static void whiteLabel(String label, String value, Ansi.Color valueColor) {
        labelValue(label, value, Ansi.Color.WHITE, valueColor, false);
    }
    
    public static void whiteLabelBold(String label, String value, Ansi.Color valueColor) {
        labelValue(label, value, Ansi.Color.WHITE, valueColor, true);
    }
}
