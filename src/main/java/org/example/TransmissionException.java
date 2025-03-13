package org.example;

public class TransmissionException extends Exception {
    private String componentName;

    public TransmissionException(String message) {
        super(message);
    }

    public String getComponentName() {
        return componentName;
    }

}
