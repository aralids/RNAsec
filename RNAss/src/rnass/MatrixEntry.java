/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package rnass;

/**
 *
 * @author PC
 */
class MatrixEntry {
    private int value;
    private boolean left = false;
    private boolean bottom = false;
    private boolean bottomLeft = false;
    private boolean bifurcation = false;
    private int[] bifurcationArg;

    public MatrixEntry() {
        this.value = 0;
    }
    
    public void setValue(int value) {
        this.value = value;
    }

    public int getValue() {
        return value;
    }

    void setLeft(boolean b) {
        this.left = b;
    }
    
    void setBottom(boolean b) {
        this.bottom = b;
    }
    
    void setBottomLeft(boolean b) {
        this.bottomLeft = b;
    }
    
    void setBifurcation(boolean b) {
        this.bifurcation = b;
    }
    
    void setBifurcationArg(int[] bifArg) {
        this.bifurcationArg = bifArg;
    }

    @Override
    public String toString() {
        return "" + value + "";
    }
    
    
}

