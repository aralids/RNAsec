/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package rnass;

/**
 *
 * @author PC
 */
class Nt {
    
    private int seqIndex;
    private String dotBracketChar;
    private Nt prev = null;
    private Nt next = null;
    private Nt match = null;
    private Loop loop;
    
    public Nt(int seqIndex, String dotBracketChar) {
        this.seqIndex = seqIndex;
        this.dotBracketChar = dotBracketChar;
    }

    public void setPrev(Nt prev) {
        this.prev = prev;
    }

    public void setNext(Nt next) {
        this.next = next;
    }

    public void setMatch(Nt match) {
        this.match = match;
    }

    @Override
    public String toString() {
        int prevSeqIndex = this.prev != null ? this.prev.getSeqIndex() : -1; 
        int nextSeqIndex = this.next != null ? this.next.getSeqIndex() : -1; 
        int matchSeqIndex = this.match != null ? this.match.getSeqIndex() : -1; 
        return "Nt{" + "seqIndex=" + this.seqIndex + ", dotBracketChar=" + this.dotBracketChar + ", matchSeqIndex=" + matchSeqIndex + ", prevSeqIndex=" + prevSeqIndex + ", nextSeqIndex=" + nextSeqIndex + '}';
    }

    public int getSeqIndex() {
        return this.seqIndex;
    }

    public Nt getNext() {
        return next;
    }

    public Nt getMatch() {
        return match;
    }

    public void setLoop(Loop loop) {
        this.loop = loop;
    }

    public String getDotBracketChar() {
        return this.dotBracketChar;
    }

    public Nt getPrev() {
        return this.prev;
    }
    
    public boolean compare(Nt other) {
        return other != null && other.getSeqIndex() == this.seqIndex;
    }

    Object getLoop() {
        return this.loop.getNts();
    }

    public void setSeqIndex(int seqIndex) {
        this.seqIndex = seqIndex;
    }

    public void setDotBracketChar(String dotBracketChar) {
        this.dotBracketChar = dotBracketChar;
    }
}
