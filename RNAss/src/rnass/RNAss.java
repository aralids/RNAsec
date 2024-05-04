/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Main.java to edit this template
 */
package rnass;
import java.util.ArrayList;
import java.util.Dictionary;
import java.util.Hashtable;

/**
 *
 * @author PC
 */
public class RNAss {
    static private ArrayList<Loop> allLoops = new ArrayList<Loop>();
    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        String seq = "aauuuuucccccgg"; // "ugagcgaauucagc" "agcacacaggc" "gggaccuucc"
        Dictionary<String, Integer> scoringScheme = new Hashtable<>();
        scoringScheme.put("gc", 1);
        scoringScheme.put("cg", 1);
        scoringScheme.put("au", 1);
        scoringScheme.put("ua", 1);
        scoringScheme.put("ug", 0);
        scoringScheme.put("gu", 0);
        ArrayList<int[]> matrix = new ArrayList<int[]>();
        
        calculateMatrix(seq, scoringScheme);
        
        /*
        String[] dotBracket = new String[seq.length()];
        int max = matrix.get(0)[seq.length() - 1];
        int start = 0;
        int end = seq.length() - 1;
        Nt startNt = null;
        Nt endNt = null;
        Nt head = null;
        Nt tail = null;
        while (max != 0) {
            //System.out.println(max + " " + start + " " + end + " " + seq.charAt(start) + "" + seq.charAt(start+end));
            if (scoringScheme.get(seq.charAt(start) + "" + seq.charAt(start + end)) != null && matrix.get(start + 1)[end - 2] == max - scoringScheme.get(seq.charAt(start) + "" + seq.charAt(start + end))) {
                dotBracket[start] = "(";
                dotBracket[start + end] = ")";
                
                Nt newStartNt = new Nt(start, "(");
                if (startNt != null) startNt.setNext(newStartNt);
                newStartNt.setPrev(startNt);
                startNt = newStartNt;
                
                Nt newEndNt = new Nt(start + end, ")");
                if (endNt != null) endNt.setPrev(newEndNt);
                newEndNt.setNext(endNt);
                endNt = newEndNt;
                
                newStartNt.setMatch(newEndNt);
                if (start == 0) head = startNt;
                if (end == seq.length()) tail = endNt;
                
                max -= scoringScheme.get(seq.charAt(start) + "" + seq.charAt(start + end));
                start++;
                end -= 2;
            }
            else if (matrix.get(start + 1)[end - 1] == max) {
                dotBracket[start] = ".";
                
                Nt newStartNt = new Nt(start, ".");
                if (startNt != null) startNt.setNext(newStartNt);
                newStartNt.setPrev(startNt);
                startNt = newStartNt;
                if (start == 0) head = startNt;
                
                start++;
                end--;
            }
            else {
                dotBracket[start + end] = ".";
                
                Nt newEndNt = new Nt(start + end, ".");
                if (endNt != null) endNt.setPrev(newEndNt);
                newEndNt.setNext(endNt);
                endNt = newEndNt;
                if (end == seq.length()) tail = endNt;
                
                end--;
            }
        }
   
        for (int i = start; i <= start + end; i++) {
                dotBracket[i] = ".";
                Nt newStartNt = new Nt(i, ".");
                if (startNt != null) startNt.setNext(newStartNt);
                newStartNt.setPrev(startNt);
                startNt = newStartNt;
        }
        
        startNt.setNext(endNt);
        endNt.setPrev(startNt);
        
        String dotBracketString = String.join("", dotBracket);
        Nt el = head;
        
        System.out.println(el);
        while (el.getNext() != null) {
            el = el.getNext();
            System.out.println(el);
        }
        
        //System.out.println(seq);
        //System.out.println(dotBracketString);
        calculateLoops(head, tail);
        
        //System.out.println(allLoops.get(0).getNeighbors());
        */
    }
    
    static private int couple(int i, int j, Dictionary<String, Integer> scoringScheme, String seq) {
        if (scoringScheme.get(seq.charAt(i) + "" + seq.charAt(j)) != null) {
            return scoringScheme.get(seq.charAt(i) + "" + seq.charAt(j));
        }
        else
            return 0;
    }
    
    static private ArrayList<MatrixEntry[]> calculateMatrix(String seq, Dictionary<String, Integer> scoringScheme) {
        ArrayList<MatrixEntry[]> matrix = new ArrayList<>();
        for (int i = 0; i < seq.length(); i++) {
            MatrixEntry[] entry = new MatrixEntry[seq.length()];
            for (int j = 0; j < seq.length(); j++) {
                entry[j] = new MatrixEntry();
            }
            matrix.add(entry);
        }
        
        for (int k = 4; k <= seq.length(); k++) {
            for (int i = 0; i < seq.length() - k; i++) {
                int j = i + k;
                if (j - i > 3) {
                    int bottom = matrix.get(i+1)[j].getValue();
                    int left = matrix.get(i)[j-1].getValue();
                    int bottomLeft = matrix.get(i+1)[j-1].getValue() + couple(i, j, scoringScheme, seq);
                    int bifValue = -Integer.MIN_VALUE;
                    for (int t = i; t < j; t++) {     
                        //System.out.println(i + " " + t + " | " + (t+1) + " " + (j));
                        if (matrix.get(i)[t].getValue() + matrix.get(t+1)[j].getValue() > bifValue) {
                            bifValue = matrix.get(i)[t].getValue() + matrix.get(t+1)[j].getValue();
                        }
                    }
                    int max = Math.max(Math.max(Math.max(left, bottom), bottomLeft), bifValue);
                    matrix.get(i)[j].setValue(max);
                    System.out.println(i + ", " + j + " | " + max);
                }
            }
            System.out.println("============");
        }
        
        return matrix;
    }
    
    static private Loop calculateLoops(Nt head, Nt tail) {
        Loop currLoop = new Loop();
        allLoops.add(currLoop);
        Nt currEl = head;
        while (currEl != null && !currEl.compare(tail)) {
            currEl.setLoop(currLoop);
            currLoop.incrementNts();
            currLoop.addNt(currEl.getSeqIndex());
            if (currEl.getDotBracketChar() == "." || currEl.getDotBracketChar() == ")") currEl = currEl.getNext();
            else if (currEl.getDotBracketChar() == "(") {
                currLoop.addNb(calculateLoops(currEl.getNext(), currEl.getMatch()));
                currEl = currEl.getMatch();
            }
        }
        return currLoop;
    }
}
