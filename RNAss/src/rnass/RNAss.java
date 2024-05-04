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
    
    static private ArrayList<MatrixEntry[]> calculateMatrix(String seq, Dictionary<String, Integer> scoringScheme) {
        ArrayList<MatrixEntry[]> matrix = new ArrayList<>();
        for (int i = 0; i < seq.length(); i++) {
            MatrixEntry[] entry = new MatrixEntry[seq.length() - i + 1];
            for (int j = 0; j < seq.length() - i + 1; j++) {
                entry[j] = new MatrixEntry();
            }
            matrix.add(entry);
        }
        
        for (int j = 5; j <= seq.length(); j++) {
            for (int i = 0; i < seq.length(); i++) {
                if (j <= seq.length() - i) {
                    //System.out.println("i, j: " + i + ", " + j);
                    int bottom = matrix.get(i+1)[j-1].getValue();
                    int left = matrix.get(i)[j-1].getValue();
                    int bottomLeft = scoringScheme.get(seq.charAt(i) + "" + seq.charAt(i+j-1)) == null ? matrix.get(i+1)[j-2].getValue() : matrix.get(i+1)[j-2].getValue() + scoringScheme.get(seq.charAt(i) + "" + seq.charAt(i+j-1));
                    int bifValue = -Integer.MIN_VALUE;
                    int[] bifArg = new int[3];


                    for (int k = i + 1; k < i + j; k++) {
                        System.out.println(": " + i + " " + (k-1-i) + " | " + k + " " + (j - k + i - 1) + ", " + matrix.get(i)[k-1 - i].getValue() + " + " + matrix.get(k)[j - k + i - 1].getValue());
                        if (scoringScheme.get(seq.charAt(k-1) + "" + seq.charAt(j)) != null && matrix.get(i)[k-1 - i].getValue() + matrix.get(k)[j - k + i - 1].getValue() > bifValue) {
                            bifValue = matrix.get(i)[k-1 - i].getValue() + matrix.get(k)[j - k + i - 1].getValue();
                            bifArg[0] = i;
                            bifArg[1] = j;
                            bifArg[2] = k;
                        } 
                    }

                    matrix.get(i)[j].setValue(Math.max(Math.max(Math.max(bottom, left), bottomLeft), bifValue));

                    if (matrix.get(i)[j].getValue() == left) {
                        matrix.get(i)[j].setLeft(true);
                        System.out.println("LEFT");
                    }
                    if (matrix.get(i)[j].getValue() == bottom) {
                        matrix.get(i)[j].setBottom(true);
                        System.out.println("BOTTOM");
                    }
                    if (matrix.get(i)[j].getValue() == bottomLeft) {
                        matrix.get(i)[j].setBottomLeft(true);
                        System.out.println("BOTTOM-LEFT");
                    }
                    if (matrix.get(i)[j].getValue() == bifValue) {
                        matrix.get(i)[j].setBifurcation(true);
                        matrix.get(i)[j].setBifurcationArg(bifArg);
                        System.out.println("BIFURCATION");
                    }
                    System.out.println(i + " " + j + " " + matrix.get(i)[j] + " " + seq.charAt(i) + "" + seq.charAt(i+j-1));
                }
            }
            System.out.println("=================");
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
