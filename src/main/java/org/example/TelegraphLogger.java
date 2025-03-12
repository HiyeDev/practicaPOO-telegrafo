package org.example;

import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

public class TelegraphLogger {
    private static TelegraphLogger instance;

    public enum LogLevel {
        INFO, WARNING, ERROR, CRITICAL
    }

    private static final String RESET = "\u001B[0m";
    private static final String BLACK = "\u001B[30m";
    private static final String RED = "\u001B[31m";
    private static final String GREEN = "\u001B[32m";
    private static final String YELLOW = "\u001B[33m";
    private static final String BLUE = "\u001B[34m";
    private static final String PURPLE = "\u001B[35m";
    private static final String CYAN = "\u001B[36m";
    private static final String WHITE = "\u001B[37m";

    private static final String BOLD = "\u001B[1m";
    private static final String BACKGROUND_RED = "\u001B[41m";
    private static final String BACKGROUND_GREEN = "\u001B[42m";
    private static final String BACKGROUND_YELLOW = "\u001B[43m";
    private static final String BACKGROUND_WHITE = "\u001B[47m";
    private static final String BACKGROUND_EMPTY = "\u001B[48m";

    private List<String> logs;
    private boolean writeToFile;
    private String logFilePath;

    private TelegraphLogger() {
        logs = new ArrayList<>();
        writeToFile = true;
        logFilePath = "telegraph_log.txt";
    }

    public static synchronized TelegraphLogger getInstance() {
        if (instance == null) {
            instance = new TelegraphLogger();
        }
        return instance;
    }

    public void log(LogLevel level, String component, String message) {
        String timestamp = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss.SSS"));
        String plainLogEntry = String.format("[%s] [%s] [%s] %s", timestamp, level, component, message);

        logs.add(plainLogEntry);

        String coloredLogEntry = formatColoredEntry(timestamp, level, component, message);

        System.out.println(coloredLogEntry);

        if (writeToFile) {
            try (PrintWriter out = new PrintWriter(new FileWriter(logFilePath, true))){
                out.println(plainLogEntry);
            } catch (IOException e) {
                System.err.println("Error writing to log file: " + e.getMessage());
            }
        }
    }

    private String formatColoredEntry(String timestamp, LogLevel level, String component, String message) {
        StringBuilder builder = new StringBuilder();

        builder.append(CYAN).append("[").append(timestamp).append("]").append(RESET).append(" ");

        switch (level) {
            case INFO -> builder.append(BACKGROUND_GREEN).append(BOLD).append("[").append(level).append("]").append(RESET).append(BACKGROUND_EMPTY);
            case WARNING -> builder.append(BACKGROUND_YELLOW).append(BOLD).append("[").append(level).append("]").append(RESET).append(BACKGROUND_EMPTY);
            case ERROR -> builder.append(BACKGROUND_RED).append(BOLD).append("[").append(level).append("]").append(RESET).append(BACKGROUND_EMPTY);
            case CRITICAL -> builder.append(BACKGROUND_WHITE).append(BOLD).append(RED).append("[").append(level).append("]").append(RESET).append(BACKGROUND_EMPTY);
        }

        builder.append(" ").append(PURPLE). append("[").append(component).append("]").append(RESET).append(" ");

        switch (level) {
            case INFO -> builder.append(message);
            case WARNING -> builder.append(YELLOW).append(message).append(RESET);
            case ERROR, CRITICAL-> builder.append(RED).append(message).append(RESET);
        }

        return builder.toString();
    }

    public void printHeader(String title) {
        String line = "═".repeat(title.length() + 8);
        System.out.println(BLUE + BOLD + "╔" + line + "╗" + RESET);
        System.out.println(BLUE + BOLD + "║   " + title + "   ║" + RESET);
        System.out.println(BLUE + BOLD + "╚" + line + "╝" + RESET);
    }

    public void printSeparator() {
        System.out.println("-".repeat(80));
    }

    public void printSucces(String message) {
        System.out.println(BACKGROUND_GREEN + BLACK + BOLD + " SUCCES " + RESET + " " + GREEN + message + RESET);
    }

    public void printFailure(String message) {
        System.out.println(BACKGROUND_RED + WHITE + BOLD + " FAILURE " + RESET + " " + RED + message + RESET);
    }

    public List<String> getLogs() {
        return logs;
    }

    public void clear() {
        logs.clear();
    }

    public void info(String component, String message) {
        log(LogLevel.INFO, component, message);
    }

    public void warning(String component, String message) {
        log(LogLevel.WARNING, component, message);
    }

    public void error(String component, String message) {
        log(LogLevel.ERROR, component, message);
    }

    public void critical(String component, String message) {
        log(LogLevel.CRITICAL, component, message);
    }
}
