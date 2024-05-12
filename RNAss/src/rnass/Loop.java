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
    private int size = 2;
    private ArrayList<Integer> ntList = new ArrayList<Integer>();
    private double[] p1 = {0, 0.95};
    private double[] p2 = {0.3, 0};
    private double center = 0;
    private double[] circPoints;
    private String dir = "+";
    private ArrayList<Loop> neighbors = new ArrayList<Loop>();
    
    public void increment() {
        this.size++;
    }
    
    public void calculateCenters(double lineSize) {
    }

    public int getSize() {
        return size;
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
    
    public void calculateOrigin() {
        double innerAngle = 360 / this.size;
        double outerAngle = (180 - innerAngle) / 2;
        double thirdLen = 0.5;
        double median = thirdLen * Math.tan(outerAngle * Math.PI / 180);
        double radius = median / Math.sin(outerAngle * Math.PI / 180);
        
        double slope = this.p2[0] != this.p1[0] ? (this.p2[1] - this.p1[1]) / (this.p2[0] - this.p1[0]) : 0;
        double perpSlope = -1 / slope;
        double[] midPoint = {(this.p1[0] + this.p2[0]) / 2, (this.p1[1] + this.p2[1]) / 2};
        System.out.println("slope: " + slope + " , dir: " + this.dir);
        System.out.println("midPoint: " + midPoint[0] + " " + midPoint[1]);
        double centerX, centerY, lastX, lastY;
        if ((slope >= 0 && "-".equals(this.dir)) || (slope < 0 && "+".equals(this.dir))) {
            centerX = this.p2[0] == this.p1[0] ? midPoint[0] + median : midPoint[0] + (median * Math.sqrt(1 / (1 + Math.pow(perpSlope, 2))));
            centerY = this.p2[1] == this.p1[1] ? midPoint[1] + median : midPoint[1] + (perpSlope * median * Math.sqrt(1 / (1 + Math.pow(perpSlope, 2))));
        }
        else {
            centerX = this.p2[0] == this.p1[0] ? midPoint[0] - median : midPoint[0] - (median * Math.sqrt(1 / (1 + Math.pow(perpSlope, 2))));
            centerY = this.p2[1] == this.p1[1] ? midPoint[1] - median : midPoint[1] - (perpSlope * median * Math.sqrt(1 / (1 + Math.pow(perpSlope, 2))));
        }
        if ("+".equals(this.dir)) {
            lastX = this.p1[0] >= this.p2[0] ? this.p1[0] : this.p2[0];
            lastY = this.p1[0] >= this.p2[0] ? this.p1[1] : this.p2[1];
        }
        else {
            lastX = this.p1[0] >= this.p2[0] ? this.p2[0] : this.p1[0];
            lastY = this.p1[0] >= this.p2[0] ? this.p2[1] : this.p1[1];
        }
        
        for (int i = 2; i < this.size; i++) {
            double lX = (lastX - centerX) * Math.cos(innerAngle * Math.PI / 180) - (lastY - centerY) * Math.sin(innerAngle * Math.PI / 180) + centerX;
            double lY = (lastX - centerX) * Math.sin(innerAngle * Math.PI / 180) + (lastY - centerY) * Math.cos(innerAngle * Math.PI / 180) + centerY;
            System.out.println(lX + " " + lY);
            lastX = lX;
            lastY = lY;
        }
        System.out.println("center: " + centerX + ", " + centerY);
        
    }

    @Override
    public String toString() {
        return "Loop{" + "ntList=" + ntList.toString() + '}';
    }
    
    
}
