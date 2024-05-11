/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Main.java to edit this template
 */
package rnass;
import java.util.*;

/**
 *
 * @author PC
 */
public class RNAss {
    static private ArrayList<Loop> allLoops = new ArrayList<Loop>();
    static String seq = "aauuuuucccccgg";
    static int[] seqCopy = new int[seq.length()];
    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
         // "ugagcgaauucagc" "agcacacaggc" "gggaccuucc" "aauuuuucccccgg" "aacccuuguaguaaccau"
        Dictionary<String, Integer> scoringScheme = new Hashtable<>();
        Arrays.fill(seqCopy, -1);
        scoringScheme.put("gc", 1);
        scoringScheme.put("cg", 1);
        scoringScheme.put("au", 1);
        scoringScheme.put("ua", 1);
        scoringScheme.put("ug", 1);
        scoringScheme.put("gu", 1);
        
        ArrayList<MatrixEntry[]> matrix = calculateMatrix(seq, scoringScheme);
        MatrixEntry el = matrix.get(0)[seq.length()-1];
        backtrace(el);
        String[] dotBracket = new String[seq.length()];
        
        Nt[] temp = new Nt[seq.length()];
        for (int i = 0; i < seq.length(); i++) {
            temp[i] = new Nt(-1, ".");
        }
        
        for (int i = 0; i < seq.length(); i++) {
            temp[i].setSeqIndex(i);
            if (i != 0) {
                temp[i].setPrev(temp[i-1]);
            }
            if (i != seq.length()-1) {
                temp[i].setNext(temp[i+1]);
            }
            
            if (seqCopy[i] == -1) {
                dotBracket[i] = ".";
                temp[i].setDotBracketChar(".");
            }
            else if (i < seqCopy[i]) {
                dotBracket[i] = "(";
                temp[i].setDotBracketChar("(");
                temp[i].setMatch(temp[seqCopy[i]]);
            }
            else {
                dotBracket[i] = ")";
                temp[i].setDotBracketChar(")");
            }
        }
        Nt head = temp[0];
        System.out.println(String.join("", dotBracket));
        
        /*
        Nt curr = head;
        System.out.println(curr);
        while (curr.getNext() != null) {
            curr = curr.getNext();
            System.out.println(curr);
        }
        */
        
        calculateLoops(head, null);
        System.out.println(allLoops.get(0));
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
                int bifurcation = 0;
                int bifK = 0;
                
                for (int k = 0; k < j - 3; k++) {
                    int val1, val2;
                    if (k <= 0) val1 = 0;
                    else val1 = entryValue(matrix.get(i)[k-1]);
                    if (k >= seq.length()) val2 = 0;
                    else val2 = entryValue(matrix.get(i+k+1)[j-k-2]);
                    
                    if (couple((k+i), (j+i), scoringScheme, seq) == 1 && val1 + val2 + couple((k+i), (j+i), scoringScheme, seq) > bifurcation) {
                        bifurcation = val1 + val2 + couple((k+i), (j+i), scoringScheme, seq);
                        bifK = k;
                    }
                    //System.out.println("--" + i + " " + (k-1) + ", " + (i+k+1) + " " + (j-k-2) + " | " + (k+i) + " " + (j+i) + " | " + val1 + " + " + val2 + " + " + couple((k+i), (j+i), scoringScheme, seq));
                }
                int max = Math.max(left, bifurcation);
                if (max == bifurcation) {
                    MatrixEntry bt1 = bifK <= 0 ? null : matrix.get(i)[bifK-1];
                    MatrixEntry bt2 = bifK >= seq.length() ? null : matrix.get(i + bifK + 1)[j - bifK - 2];
                    matrix.get(i)[j].addBacktrace(bt1, bt2);
                    matrix.get(i)[j].addIndices((bifK+i), (j+i));
                }
                else {
                    matrix.get(i)[j].addBacktrace(null, matrix.get(i)[j-1]);
                    matrix.get(i)[j].addIndices(-1, -1);
                }
                matrix.get(i)[j].setValue(max);
                //System.out.println(i + ", " + j + " " + max + ", changeIndices " + matrix.get(i)[j].getChangeIndices() + ", backtrace to " + i + " " + (j-1));
            }
            //System.out.println("============");
        }
        
        return matrix;
    }
    
    static private void backtrace(MatrixEntry el) {
        if (el != null && el.getValue() != 0) {
            int change1 = el.getChangeIndices()[0];
            int change2 = el.getChangeIndices()[1];
            if (change1 == -1 && change2 == -1) {
                backtrace(el.getBacktraceTo()[1]);
            }
            else {
                seqCopy[change1] = change2;
                seqCopy[change2] = change1;
                backtrace(el.getBacktraceTo()[0]);
                backtrace(el.getBacktraceTo()[1]);
            }
        }
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
