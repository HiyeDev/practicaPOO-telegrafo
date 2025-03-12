package org.example;

public class Receiver implements TelegraphComponent {
    private String receivedMessage;
    private TelegraphComponent nextComponent;
    private final TelegraphLogger logger = TelegraphLogger.getInstance();

    public Receiver() {
        this.receivedMessage = "";
        logger.info("Receiver", "New receiver initialized");
    }

    @Override
    public void transmit(Signal signal) {
        logger.info("Receiver", "Signal received with strength " + signal.getStrength() + "%");

        if (signal == null) {
            logger.error("Receiver", "Null signal received");
            return;
        }

        String morseCode = signal.getMessage();
        logger.info("Receiver", "Morse code received: " + morseCode);

        if (morseCode == null || morseCode.isEmpty()) {
            logger.error("Receiver", "Empty or null Morse code received");
            receivedMessage = "";
            return;
        }

        try {
            this.receivedMessage = MorseCodec.decode(morseCode);
            logger.info("Receiver", "Successfully decoded message: '" + receivedMessage + "'");
        } catch (Exception e) {
            logger.error("Receiver", "Error decoding Morse code: " + e.getMessage());
            receivedMessage = "";
        }

        displayMessage();

        if (nextComponent != null) {
            logger.warning("Receiver", "Unexpected component after receiver, forwarding signal");
            nextComponent.transmit(signal);
        }
    }

    public void displayMessage() {
        if (receivedMessage == null || receivedMessage.isEmpty()) {
            logger.warning("Receiver", "No message to display");
        } else {
            logger.info("Receiver", "Displaying message: '" + receivedMessage + "'");
        }
    }

    @Override
    public void connectTo(TelegraphComponent nextComponent) {
        this.nextComponent = nextComponent;
        logger.warning("Receiver", "Connected to another component - receivers are typically the last component");
    }

    @Override
    public TelegraphComponent getNextComponent() {
        return nextComponent;
    }

    public String getReceivedMessage() {
        return receivedMessage;
    }

    private void setReceivedMessage(String receivedMessage) {
        logger.info("Receiver", "Setting received message: '" + receivedMessage + "'");
        this.receivedMessage = receivedMessage;
    }

    public void reset() {
        logger.info("Receiver", "Resetting receiver");
        this.receivedMessage = "";
    }
}