package org.example;

import java.util.Random;

public class Cable implements TelegraphComponent {
    private double length;
    private double degradationRate;
    private double signalThreshold;
    private double failureProbability;
    private TelegraphComponent nextComponent;
    private final TelegraphLogger logger = TelegraphLogger.getInstance();
    private final Random random = new Random();

    public Cable(double length, double degradationRate) {
        this.length = length;
        this.degradationRate = degradationRate;
        this.signalThreshold = 10.0;
        this.failureProbability = 5.0;
        logger.info("Cable", "New cable initialized: length=" + length + "km, degradation=" + degradationRate +
                "% per km, failure probability=" + failureProbability + "% per 10km");
    }

    @Override
    public void transmit(Signal signal) {
        logger.info("Cable", "Transmitting signal through " + length + "km cable");
        logger.info("Cable", "Initial signal strength: " + signal.getStrength() + "%");

        for (int km = 1; km <= length; km++) {
            signal.reduceStrength(degradationRate);

            if (km % 10 == 0 || km == (int)length || signal.getStrength() < 30) {
                logger.info("Cable", "Signal strength at km " + km + ": " + signal.getStrength() + "%");
            }

            if (km % 10 == 0) {
                if (checkRandomFailure()) {
                    logger.critical("Cable", "Random failure occurred at kilometer " + km + "!");
                    logger.error("Cable", "Signal lost due to cable failure");

                    signal.reduceStrength(signal.getStrength());

                    return;
                }
            }

            if (!signal.isDetectable(signalThreshold)) {
                logger.warning("Cable", "Signal weakened below threshold " + signalThreshold + "% at kilometer " + km);

                if (nextComponent instanceof Relay) {
                    logger.info("Cable", "Signal is weak but next component is a relay. Transmitting anyway...");
                    nextComponent.transmit(signal);
                } else {
                    logger.error("Cable", "Signal lost at kilometer " + km + " (no relay available to amplify)");
                    System.err.println("Cable: Signal lost (no relay available to amplify)");
                }
                return;
            }
        }

        logger.info("Cable", "Signal successfully transmitted through entire cable");

        if (nextComponent != null) {
            logger.info("Cable", "Passing signal to next component");
            nextComponent.transmit(signal);
        } else {
            logger.warning("Cable", "End of line - no next component to receive signal");
        }
    }

    private boolean checkRandomFailure() {
        double randomValue = random.nextDouble() * 100;

        boolean failure = randomValue < failureProbability;

        if (failure) {
            logger.warning("Cable", "Random failure check: FAILURE (random value: " +
                    String.format("%.2f", randomValue) + ", threshold: " + failureProbability + ")");
        } else {
            logger.info("Cable", "Random failure check: Ok (random value: " +
            String.format("%.2f", randomValue) + ", threshold: " + failureProbability + ")");
        }

        return failure;
    }

    @Override
    public void connectTo(TelegraphComponent nextComponent) {
        this.nextComponent = nextComponent;

        if (nextComponent instanceof Relay) {
            logger.info("Cable", "Connected to a Relay");
        } else if (nextComponent instanceof Receiver) {
            logger.info("Cable", "Connected to a Receiver");
        } else {
            logger.info("Cable", "Connected to next component");
        }
    }

    @Override
    public TelegraphComponent getNextComponent() {
        return this.nextComponent;
    }

    // Getters y setters
    public void setSignalThreshold(double signalThreshold) {
        this.signalThreshold = signalThreshold;
        logger.info("Cable", "Signal threshold updated to " + signalThreshold + "%");
    }

    public double getLength() {
        return length;
    }

    public double getDegradationRate() {
        return degradationRate;
    }

    public double getSignalThreshold() {
        return signalThreshold;
    }
}