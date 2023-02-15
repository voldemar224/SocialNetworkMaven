package org.example;

import java.io.IOException;

public class Main {
    public static void main(String[] args) throws IOException {
        Simulation sim = new Simulation();
        sim.simulationProcess(10, 1);
    }
}