package org.example;

public class Signal {
    private String message;
    private double  strength;
    private boolean isTransmitting;

    public Signal(String message, double initialStrength) {
        this.message = message;
        this.strength = Math.min(Math.max(initialStrength, 0), 100);
        this.isTransmitting = true;
    }

    public void reduceStrength(double reductionAmount) {
        this.strength = Math.max(0, this.strength - reductionAmount);
        if (this.strength <= 0) {
            this.isTransmitting = false;
        }
    }

    public void amplifySignal(double newStrength) {
        this.strength = Math.min(newStrength, 100);
        this.isTransmitting = true;
    }

    public boolean isDetectable(double signalThreshold) {
        return strength >= signalThreshold;
    }

    public String getMessage() {
        return message;
    }

    public double getStrength() {
        return strength;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public void setStrength(double strength) {
        this.strength = strength;
    }

    public void setTransmitting(boolean transmitting) {
        isTransmitting = transmitting;
    }
}
