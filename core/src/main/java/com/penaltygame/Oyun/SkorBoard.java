package com.penaltygame.Oyun;

public class SkorBoard {
    private final String takimA;
    private final String takimB;
    private int skorA = 0;
    private int skorB = 0;

    public SkorBoard(String takimA, String takimB) {
        this.takimA = takimA;
        this.takimB = takimB;
    }

    // oyuncuSirada = true -> A takımı atıyor
    public void golAtti(boolean oyuncuSirada) {
        if (oyuncuSirada) skorA++;
        else skorB++;
    }

    public void reset() {
        skorA = 0;
        skorB = 0;
    }

    public String getTakimA() { return takimA; }
    public String getTakimB() { return takimB; }
    public int getSkorA() { return skorA; }
    public int getSkorB() { return skorB; }
}
