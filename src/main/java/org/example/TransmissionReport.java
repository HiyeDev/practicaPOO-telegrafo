package org.example;

import java.util.List;

public class TransmissionReport {
    private String originalMessage;
    private String receivedMessage;
    private boolean isSuccessful;
    private String failureReason;
    private List<String> logs;

    public TransmissionReport() {
        this.isSuccessful = true;
    }

    public List<String> getLogs() {
        return logs;
    }

    public String getReceivedMessage() {
        return receivedMessage;
    }

    public String getFailureReason() {
        return failureReason;
    }

    public String getOriginalMessage() {
        return originalMessage;
    }

    public void setReceivedMessage(String receivedMessage) {
        this.receivedMessage = receivedMessage;
    }

    public void setFailureReason(String failureReason) {
        this.failureReason = failureReason;
    }

    public void setLogs(List<String> logs) {
        this.logs = logs;
    }

    public void setOriginalMessage(String originalMessage) {
        this.originalMessage = originalMessage;
    }

    public void setSuccessful(boolean successful) {
        isSuccessful = successful;
    }

    public String getSummary() {
        StringBuilder summary = new StringBuilder();
        summary.append("=== TRANSMISSION REPORT ===\n");
        summary.append("Status: ").append(isSuccessful ? "SUCCESS" : "FAILURE").append("\n");

        if (!isSuccessful && failureReason != null) {
            summary.append("Failure reason ").append(failureReason).append("\n");
        }

        summary.append("Original message: ").append(originalMessage).append("\n");
        summary.append("Received message: ").append(receivedMessage).append("\n");

        if (originalMessage != null && receivedMessage != null) {
            boolean match = originalMessage.equals(receivedMessage);
            summary.append("Message integrity: ").append(match ? "INTACT" : "CORRUPTED").append("\n");

            if (!match) {
                int differentChars = 0;
                for (int i = 0; i < Math.min(originalMessage.length(), receivedMessage.length()); i++) {
                    if (originalMessage.charAt(i) != receivedMessage.charAt(i)) {
                        differentChars++;
                    }
                }

                int lengthDiff = Math.abs(originalMessage.length() - receivedMessage.length());
                differentChars += lengthDiff;

                int maxLength = Math.max(originalMessage.length(), receivedMessage.length());
                double similarityPercentage = (1 - (double) differentChars / maxLength) * 100;

                summary.append(String.format("Message similarity: %.2f%%\n" , similarityPercentage));
            }

            summary.append("\n=== EVENT LOG ===\n");
            if (logs != null) {
                for (String log : logs) {
                    if (log.contains("ERROR") || log.contains("CRITICAL")) {
                        summary.append("!. ");
                    }
                    summary.append(log).append("\n");
                }
            }
        }

        return summary.toString();
    }
}
