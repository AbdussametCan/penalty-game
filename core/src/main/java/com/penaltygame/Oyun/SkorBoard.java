package com.penaltygame.Oyun;

public class SkorBoard {
    private String takimSaldiran;
    private String takimSavunan;
    private int skorSaldiran = 0;
    private int skorSavunan = 0;

    public SkorBoard(String takimSaldiran, String takimSavunan) {
        this.takimSaldiran = takimSaldiran;
        this.takimSavunan = takimSavunan;
    }

    public void golAtti() {
        skorSaldiran++;
    }

    public void kurtardi() {
        skorSavunan++;
    }

    public String getTakimSaldiran() {
        return takimSaldiran;
    }

    public String getTakimSavunan() {
        return takimSavunan;
    }

    public int getSkorSaldiran() {
        return skorSaldiran;
    }

    public int getSkorSavunan() {
        return skorSavunan;
    }

    public void takimlariDegistir() {
        String temp = takimSaldiran;
        takimSaldiran = takimSavunan;
        takimSavunan = temp;

        int tempSkor = skorSaldiran;
        skorSaldiran = skorSavunan;
        skorSavunan = tempSkor;
    }

    public void reset() {
        skorSaldiran = 0;
        skorSavunan = 0;
    }
    public String getSaldiranTakimAdi() {
        return takimSaldiran;
    }

    public String getSavunanTakimAdi() {
        return takimSavunan;
    }
}
