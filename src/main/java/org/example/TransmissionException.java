package org.example;

public class TransmissionException extends Exception {
    private String componentName;
    private int kilometerId;

    public TransmissionException(String message) {
        super(message);
    }

    public TransmissionException(String message, String componentName) {
        super(message);
        this.componentName = componentName;
    }

    public TransmissionException(String message, String componentName, int kilometerId) {
        super(message);
        this.componentName = componentName;
        this.kilometerId = kilometerId;
    }

    public String getComponentName() {
        return componentName;
    }

    public int getKilometerId() {
        return kilometerId;
    }
}
