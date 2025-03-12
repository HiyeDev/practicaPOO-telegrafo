package org.example;

import java.util.ArrayList;
import java.util.List;

public class TelegraphSystem {
    private List<TelegraphComponent> components;
    private TelegraphLogger logger;
    private String originalMessage;
    private TransmissionReport report;

    public TelegraphSystem() {
        this.components = new ArrayList<>();
        this.logger = TelegraphLogger.getInstance();
        this.report = new TransmissionReport();
    }

    public void addComponent(TelegraphComponent component) {
        components.add(component);
    }

    public void setupConnections() {
        for (int i = 0; i < components.size() - 1; i++) {
            components.get(i).connectTo(components.get(i + 1));
        }
    }

    public void run(String message) {
        this.originalMessage = message;
        logger.info("TelegraphSystem", "Starting transmission of message: '" + message + "'");

        try {
            if (!components.isEmpty() && components.get(0) instanceof Transmitter) {
                Transmitter transmitter = (Transmitter) components.get(0);
                transmitter.sendSignal(message);


                if (components.get(components.size() - 1) instanceof Receiver) {
                    Receiver receiver = (Receiver) components.get(components.size() - 1);
                    String receivedMessage = receiver.getReceivedMessage();

                    verifyMessageIntegrity(message, receivedMessage);
                }

                generateTransmissionReport();
            } else {
                logger.error("TelegraphSystem", "Invalid setup: First component must be a Transmitter");
                throw new TransmissionException("Invalid setup: First component must be a Transmitter");
            }
        } catch (TransmissionException e) {
            logger.critical("TelegraphSystem", "Transmission failed: " + e.getMessage());
            report.setSuccessful(false);
            report.setFailureReason(e.getMessage());
        }
    }

    private void verifyMessageIntegrity(String original, String received) throws TransmissionException {
        logger.info("TelegraphSystem", "Verifying message integrity");

        if (received == null || received.isEmpty()) {
            logger.error("TelegraphSystem", "Message was not received");
            throw new TransmissionException("Message was not received");
        }

        if (!original.equals(received)) {
            logger.warning("TelegraphSystem", "Message corruption detected");
            logger.warning("TelegraphSystem", "Original: '" + original + "'");
            logger.warning("TelegraphSystem", "Received: '" + received + "'");

            analyzeCorruption(original, received);
        } else {
            logger.info("TelegraphSystem", "Message integrity verified: OK");
        }
    }

    private void analyzeCorruption(String original, String received) {
        int levenshteinDistance = calculateDifference(original, received);
        int maxLength = Math.max(original.length(), received.length());
        double similarityPercentage = (1 - (double) levenshteinDistance / maxLength) * 100;

        String analysisMessage = String.format(
                "Message similarity: %.2f%%. %d characters differ.",
                similarityPercentage, levenshteinDistance);

        logger.info("TelegraphSystem", analysisMessage);

        if (similarityPercentage < 50) {
            logger.error("TelegraphSystem", "Severe message corruption detected");
        }
    }

    private int calculateDifference(String a, String b) {
        if (a.isEmpty()) return b.length();
        if (b.isEmpty()) return a.length();

        int matchingChars = 0;
        int minLength = Math.min(a.length(), b.length());

        for (int i = 0; i < minLength; i++) {
            if (a.charAt(i) == b.charAt(i)) {
                matchingChars++;
            }
        }

        int differentChars = minLength - matchingChars;
        int lengthDifference = Math.abs(a.length() - b.length());

        return differentChars + lengthDifference;
    }

    public TransmissionReport generateTransmissionReport() {
        report.setLogs(logger.getLogs());
        report.setOriginalMessage(originalMessage);

        if (components.get(components.size() - 1) instanceof Receiver) {
            Receiver receiver = (Receiver) components.get(components.size() - 1);
            report.setReceivedMessage(receiver.getReceivedMessage());
        }

        return report;
    }
}
