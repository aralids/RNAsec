/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package rnass;
import java.util.ArrayList;

/**
 *
 * @author Dilara Sarach
 */
class Loop {
    private int nts = 2;
    private ArrayList<Integer> ntList = new ArrayList<Integer>();
    private double origin = 0;
    private double[] centers;
    private ArrayList<Loop> neighbors = new ArrayList<Loop>();
    
    public void incrementNts() {
        this.nts++;
    }
    
    public void calculateCenters(double lineSize) {
    }

    public int getNts() {
        return nts;
    }
    
    public void addNt(int seqIndex) {
        this.ntList.add(seqIndex);
    }
    
    public void addNb(Loop nb) {
        this.neighbors.add(nb);
    }

    public String getNeighbors() {
        return this.neighbors.toString();
    }

    @Override
    public String toString() {
        return "Loop{" + "ntList=" + ntList.toString() + '}';
    }
    
    
}
