/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package rnass;
import java.util.*;

/**
 *
 * @author Dilara Sarach
 */
class Loop {

    Loop(Nt prev, Nt next) {
        Nt[] enclosingPair = {prev, next};
        this.enclosingPair = enclosingPair;
    }
    
    private int size = 2;
    private ArrayList<Integer> ntList = new ArrayList<>();
    private ArrayList<Nt> nts = new ArrayList<>();
    private Nt[] enclosingPair;
    private double parentCy;
    private ArrayList<Loop> neighbors = new ArrayList<>();
    
    public void increment() {
        this.size++;
    }
    
    public void calculateCenters(double lineSize) {
    }

    public int getSize() {
        return size;
    }
    
    public void addNt(Nt nt) {
        this.ntList.add(nt.getSeqIndex());
        this.nts.add(nt);
    }
    
    public void addNb(Loop nb) {
        this.neighbors.add(nb);
    }

    public String getNeighbors() {
        return this.neighbors.toString();
    }

    public void setParentCy(double parentCy) {
        this.parentCy = parentCy;
    }
    
    public void calculateOrigin() {
        String dir;
        if (enclosingPair[0] == null) {
            dir = "-";
        }
        else if ((enclosingPair[0].getCxCy()[1] + enclosingPair[1].getCxCy()[1]) / 2 >= this.parentCy) {
            dir = "-";
        }
        else {
            dir = "+";
        }
        
        System.out.println(ntList.toString() + " " + dir + " " + this.size);
        double[] p1, p2;
        if (enclosingPair[0] != null) {
            p1 = this.enclosingPair[0].getCxCy();
            p2 = this.enclosingPair[1].getCxCy();
        }
        else {
            p1 = new double[] {0, 0};
            p2 = new double[] {0.792, 0.61};
        }
        System.out.println("encPair coords: " + Arrays.toString(p1) + " " + Arrays.toString(p2));
        
        double innerAngle = 360 / this.size;
        double outerAngle = (180 - innerAngle) / 2;
        double thirdLen = 0.5;
        double median = thirdLen * Math.tan(outerAngle * Math.PI / 180);
        
        double slope = p2[0] != p1[0] ? (p2[1] - p1[1]) / (p2[0] - p1[0]) : 0;
        double perpSlope = -1 / slope;
        double[] midPoint = {(p1[0] + p2[0]) / 2, (p1[1] + p2[1]) / 2};
        
        double centerX, centerY, lastX, lastY;
        if ((slope > 0 && "-".equals(dir)) || (slope <= 0 && "+".equals(dir))) {
            centerX = p2[0] == p1[0] ? midPoint[0] + median : midPoint[0] + (median * Math.sqrt(1 / (1 + Math.pow(perpSlope, 2))));
            centerY = p2[1] == p1[1] ? midPoint[1] + median : midPoint[1] + (perpSlope * median * Math.sqrt(1 / (1 + Math.pow(perpSlope, 2))));
        }
        else {
            centerX = p2[0] == p1[0] ? midPoint[0] - median : midPoint[0] - (median * Math.sqrt(1 / (1 + Math.pow(perpSlope, 2))));
            centerY = p2[1] == p1[1] ? midPoint[1] - median : midPoint[1] - (perpSlope * median * Math.sqrt(1 / (1 + Math.pow(perpSlope, 2))));
        }
        boolean reverse;
        if ("+".equals(dir)) {
            lastX = p1[0] >= p2[0] ? p1[0] : p2[0];
            lastY = p1[0] >= p2[0] ? p1[1] : p2[1];
            reverse = p1[0] < p2[0];
        }
        else {
            lastX = p1[0] >= p2[0] ? p2[0] : p1[0];
            lastY = p1[0] >= p2[0] ? p2[1] : p1[1];
            reverse = p1[0] >= p2[0];
        }
        
        double[][] centers = new double[this.size-2][2];
        
        for (int i = 2; i < this.size; i++) {
            double lX = (lastX - centerX) * Math.cos(innerAngle * Math.PI / 180) - (lastY - centerY) * Math.sin(innerAngle * Math.PI / 180) + centerX;
            double lY = (lastX - centerX) * Math.sin(innerAngle * Math.PI / 180) + (lastY - centerY) * Math.cos(innerAngle * Math.PI / 180) + centerY;
            
            if (reverse) {
                centers[(this.size-2) - (i-2) - 1][0] = lX;
                centers[(this.size-2) - (i-2) - 1][1] = lY;
            }
            else {
                centers[i-2][0] = lX;
                centers[i-2][1] = lY;
            }
            lastX = lX;
            lastY = lY;
        }
        for (int i = 0; i < this.size-2; i++) {
            this.nts.get(i).setCxCy(centers[i]); 
            System.out.println(ntList.get(i) + ": " + Arrays.toString(centers[i])); 
        }
        for (int i = 0; i < this.neighbors.size(); i++) {
            this.neighbors.get(i).setParentCy(centerY);
        }
        System.out.println("\n");
    }

    @Override
    public String toString() {
        int enc1 = this.enclosingPair[0] == null ? -1 : this.enclosingPair[0].getSeqIndex();
        int enc2 = this.enclosingPair[1] == null ? 14 : this.enclosingPair[1].getSeqIndex();
        return "Loop{" + "encPair=" + enc1 + " " + enc2 + " ntList=" + ntList.toString() + '}';
    }
    
    
}
