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
        String seq = "aacccuuguaguaaccau"; // "ugagcgaauucagc" "agcacacaggc" "gggaccuucc" "aauuuuucccccgg" "aacccuuguaguaaccau"
        Dictionary<String, Integer> scoringScheme = new Hashtable<>();
        scoringScheme.put("gc", 1);
        scoringScheme.put("cg", 1);
        scoringScheme.put("au", 1);
        scoringScheme.put("ua", 1);
        scoringScheme.put("ug", 1);
        scoringScheme.put("gu", 1);
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
    
    static private int entryValue(MatrixEntry entry) {
        if (entry == null) return 0;
        else return entry.getValue();
    }
            
    static private ArrayList<MatrixEntry[]> calculateMatrix(String seq, Dictionary<String, Integer> scoringScheme) {
        ArrayList<MatrixEntry[]> matrix = new ArrayList<>();
        for (int i = 0; i < seq.length(); i++) {
            MatrixEntry[] entry = new MatrixEntry[seq.length() - i];
            for (int j = 0; j < seq.length() - i; j++) {
                entry[j] = new MatrixEntry();
            }
            matrix.add(entry);
        }
        
        for (int j = 4; j <= seq.length(); j++) {
            for (int i = 0; i < seq.length() - j; i++) {
                int left = entryValue(matrix.get(i)[j-1]);
                int bottom = entryValue(matrix.get(i+1)[j-1]);
                int bottomLeft = entryValue(matrix.get(i+1)[j-2]) + couple(i, j, scoringScheme, seq);
                int bifurcation = 0;
                
                for (int k = 0; k <= j - 3; k++) {
                    int val1, val2;
                    if (k <= 0) val1 = 0;
                    else val1 = entryValue(matrix.get(i)[k-1]);
                    if (k >= seq.length()) val2 = 0;
                    else val2 = entryValue(matrix.get(i+k+1)[j-k-2]);
                    
                    if (couple((k+i), (j+i), scoringScheme, seq) == 1) {
                        bifurcation = Math.max(bifurcation, val1 + val2 + couple((k+i), (j+i), scoringScheme, seq));
                    }
                    //System.out.println("--" + i + " " + (k-1) + ", " + (i+k+1) + " " + (j-k-2) + " | " + (k+i) + " " + (j+i) + " | " + val1 + " + " + val2 + " + " + couple((k+i), (j+i), scoringScheme, seq));
                }
                int max = Math.max(left, bifurcation);
                matrix.get(i)[j].setValue(max);
                System.out.println(i + ", " + j + " " + max + "\n");
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
