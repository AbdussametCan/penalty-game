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

    // Gol atan taraf skor alır — şu an saldıran
    public void golAtti() {
        skorSaldiran++;
    }

    // Kurtarışta skor değişmez
    public void kurtardi() {
        // Sadece görsel mesaj için
    }

    // Skorları ve takımları döndür
    public void takimlariDegistir() {
        String tempTakim = takimSaldiran;
        takimSaldiran = takimSavunan;
        takimSavunan = tempTakim;

        int tempSkor = skorSaldiran;
        skorSaldiran = skorSavunan;
        skorSavunan = tempSkor;
    }

    public void reset() {
        skorSaldiran = 0;
        skorSavunan = 0;
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

    public String getSaldiranTakimAdi() {
        return takimSaldiran;
    }

    public String getSavunanTakimAdi() {
        return takimSavunan;
    }
}
