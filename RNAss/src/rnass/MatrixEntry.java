/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package rnass;
import java.util.ArrayList;

/**
 *
 * @author PC
 */
class MatrixEntry {
    private int value;
    private ArrayList<MatrixEntry[]> backtraceTo = new ArrayList<>();
    private ArrayList<int[]> changeIndices = new ArrayList<>();

    public MatrixEntry() {
        this.value = 0;
    }
    
    public void setValue(int value) {
        this.value = value;
    }

    public int getValue() {
        return value;
    }
    
    public void addBacktrace(MatrixEntry i, MatrixEntry j) {
        MatrixEntry[] arr = {i, j};
        this.backtraceTo.add(arr);
    }
    
    public void addIndices(int i, int j) {
        int[] arr = {i, j};
        this.changeIndices.add(arr);
    }

    public int[] getChangeIndices() {
        return changeIndices.get(0);
    }

    public MatrixEntry[] getBacktraceTo() {
        return backtraceTo.get(0);
    }

    @Override
    public String toString() {
        return "" + value + "";
    }
    
    
}

