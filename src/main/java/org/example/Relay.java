package org.example;

public class Relay implements TelegraphComponent {
    private boolean active;
    private int batteryLevel;
    private TelegraphComponent nextComponent;
    private final TelegraphLogger logger = TelegraphLogger.getInstance();

    public Relay() {
        this.active = true;
        this.batteryLevel = 100;
        logger.info("Relay", "New relay initialized: active=true, batteryLevel=100%");
    }

    @Override
    public void transmit(Signal signal) {
        logger.info("Relay", "Receiving signal with strength: " + signal.getStrength() + "%");

        if (!active) {
            logger.error("Relay", "Inactive, cannot amplify signal");
            return;
        }

        if (batteryLevel <= 0) {
            logger.error("Relay", "Battery depleted, cannot amplify signal");
            active = false;
            return;
        }

        if (batteryLevel <= 20) {
            logger.warning("Relay", "Battery level low: " + batteryLevel + "%");
        }

        logger.info("Relay", "Amplifying signal from " + signal.getStrength() + "% to 100%");
        signal.amplifySignal(100);
        batteryLevel -= 10;

        logger.info("Relay", "Battery level after amplification: " + batteryLevel + "%");

        if (nextComponent != null) {
            logger.info("Relay", "Forwarding amplified signal to next component");
            nextComponent.transmit(signal);
        } else {
            logger.warning("Relay", "No next component connected, signal stops here");
        }
    }

    public void recharge() {
        logger.info("Relay", "Recharging battery from " + batteryLevel + "% to 100%");
        batteryLevel = 100;
        active = true;
        logger.info("Relay", "Relay is now active with full battery");
    }

    @Override
    public void connectTo(TelegraphComponent nextComponent) {
        this.nextComponent = nextComponent;

        if (nextComponent instanceof Cable) {
            logger.info("Relay", "Connected to a Cable");
        } else if (nextComponent instanceof Receiver) {
            logger.info("Relay", "Connected to a Receiver");
        } else {
            logger.info("Relay", "Connected to next component");
        }
    }

    @Override
    public TelegraphComponent getNextComponent() {
        return nextComponent;
    }

    public int getBatteryLevel() {
        return batteryLevel;
    }

    public boolean isActive() {
        return active;
    }

    public void setActive(boolean active) {
        this.active = active;
        logger.info("Relay", "Relay active state changed to: " + (active ? "active" : "inactive"));
    }

    public void setBatteryLevel(int batteryLevel) {
        if (batteryLevel < 0) {
            batteryLevel = 0;
            logger.warning("Relay", "Attempted to set negative battery level, set to 0 instead");
        } else if (batteryLevel > 100) {
            batteryLevel = 100;
            logger.warning("Relay", "Attempted to set battery level above 100, capped at 100%");
        }

        logger.info("Relay", "Battery level changed from " + this.batteryLevel + "% to " + batteryLevel + "%");
        this.batteryLevel = batteryLevel;

        if (batteryLevel == 0 && active) {
            active = false;
            logger.warning("Relay", "Battery depleted, relay automatically deactivated");
        }
    }
}