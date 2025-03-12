package org.example;

public interface TelegraphComponent {
    void transmit(Signal signal);
    void connectTo(TelegraphComponent nextComponent);
    TelegraphComponent getNextComponent();
}
