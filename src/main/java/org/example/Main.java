package org.example;

import java.io.PrintWriter;

//TIP To <b>Run</b> code, press <shortcut actionId="Run"/> or
// click the <icon src="AllIcons.Actions.Execute"/> icon in the gutter.
public class Main {
    public static void main(String[] args) {
        //TIP Press <shortcut actionId="ShowIntentionActions"/> with your caret at the highlighted text
        // to see how IntelliJ IDEA suggests fixing it.
       Transmitter transmitter = new Transmitter();
       Cable cable1 = new Cable(50, 10);
       Relay relay = new Relay();
       Cable cable2 = new Cable(20, 2);
       Receiver receiver = new Receiver();

       TelegraphSystem telegraphSystem = new TelegraphSystem();

        telegraphSystem.addComponent(transmitter);
        telegraphSystem.addComponent(cable1);
        telegraphSystem.addComponent(relay);
        telegraphSystem.addComponent(cable2);
        telegraphSystem.addComponent(receiver);

        telegraphSystem.setupConnections();
        telegraphSystem.run("SOS");

        TransmissionReport report = telegraphSystem.generateTransmissionReport();
        System.out.println(report.getSummary());

        try (PrintWriter writer = new PrintWriter("./transmission_report.txt")) {
            writer.print(report.getSummary());
        } catch (Exception e) {
            System.err.println("Error to save summary: " + e.getMessage());
        }
    }
}