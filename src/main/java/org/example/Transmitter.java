package org.example;

public class Transmitter implements TelegraphComponent {
    private boolean isOn;
    private String message;
    private TelegraphComponent nextComponent;
    private final TelegraphLogger logger = TelegraphLogger.getInstance();

    public Transmitter() {
        this.isOn = true;
        this.message = "";
        logger.info("Transmitter", "New transmitter initialized and ready");
    }

    public void sendSignal(String message) {
        logger.info("Transmitter", "Attempting to send message: '" + message + "'");

        if (!isOn) {
            logger.error("Transmitter", "Cannot send signal - transmitter is OFF");
            return;
        }

        this.message = message;
        logger.info("Transmitter", "Converting message to Morse code");

        String morseCode = convertIntoAPulseSequence();
        logger.info("Transmitter", "Morse code generated: " + morseCode);

        logger.info("Transmitter", "Creating signal with initial strength: 100%");
        Signal signal = new Signal(morseCode, 100);

        if (nextComponent != null) {
            logger.info("Transmitter", "Transmitting signal to next component");
            nextComponent.transmit(signal);
        } else {
            logger.critical("Transmitter", "No component connected to transmitter - signal lost");
            System.err.println("Error. No component connected to transmitter");
        }
    }

    private String convertIntoAPulseSequence() {
        return MorseCodec.encode(message);
    }

    @Override
    public void transmit(Signal signal) {
        logger.warning("Transmitter", "Received signal but transmitter cannot receive signals, only send them");
    }

    @Override
    public void connectTo(TelegraphComponent nextComponent) {
        this.nextComponent = nextComponent;
        logger.info("Transmitter", "Connected to next component");
    }

    @Override
    public TelegraphComponent getNextComponent() {
        return nextComponent;
    }

    public boolean getOn() {
        return isOn;
    }

    public String getMessage() {
        return message;
    }

    public void setOn(boolean on) {
        this.isOn = on;
        logger.info("Transmitter", "Transmitter power set to: " + (on ? "ON" : "OFF"));
    }

    public void setMessage(String message) {
        this.message = message;
        logger.info("Transmitter", "Message set to: '" + message + "'");
    }
}