package com.penaltygame.Oyun;

import java.util.ArrayList;
import java.util.List;

public class SkorBoard {
    private final String evSahibiTakim;
    private final String deplasmanTakim;

    private String takimSaldiran;
    private String takimSavunan;

    private int skorSaldiran = 0;
    private int skorSavunan = 0;

    private List<Boolean> gecmisSaldiran = new ArrayList<>();
    private List<Boolean> gecmisSavunan = new ArrayList<>();

    public SkorBoard(String evSahibiTakim, String deplasmanTakim) {
        this.evSahibiTakim = evSahibiTakim;
        this.deplasmanTakim = deplasmanTakim;

        this.takimSaldiran = evSahibiTakim;
        this.takimSavunan = deplasmanTakim;
    }

    public void golAtti() {
        skorSaldiran++;
        gecmisSaldiran.add(true);  // Gol oldu
        gecmisSavunan.add(false);  // Kurtaramadı
    }

    public void kurtardi() {
        gecmisSaldiran.add(false); // Gol olmadı
        gecmisSavunan.add(true);   // Kurtardı
    }

    public void takimlariDegistir() {
        // Saldıran ve savunan rolleri değişir ama ev sahibi ve deplasman değişmez
        String tempTakim = takimSaldiran;
        takimSaldiran = takimSavunan;
        takimSavunan = tempTakim;

        int tempSkor = skorSaldiran;
        skorSaldiran = skorSavunan;
        skorSavunan = tempSkor;

        List<Boolean> tempGecmis = gecmisSaldiran;
        gecmisSaldiran = gecmisSavunan;
        gecmisSavunan = tempGecmis;
    }

    public void reset() {
        skorSaldiran = 0;
        skorSavunan = 0;
        gecmisSaldiran.clear();
        gecmisSavunan.clear();
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

    public List<Boolean> getGecmisSaldiran() {
        return gecmisSaldiran;
    }

    public List<Boolean> getGecmisSavunan() {
        return gecmisSavunan;
    }

    public Boolean getSaldiranAtis(int index) {
        return index < gecmisSaldiran.size() ? gecmisSaldiran.get(index) : null;
    }

    public Boolean getSavunanAtis(int index) {
        return index < gecmisSavunan.size() ? gecmisSavunan.get(index) : null;
    }

    public String getEvSahibiTakim() {
        return evSahibiTakim;
    }

    public String getDeplasmanTakim() {
        return deplasmanTakim;
    }

    // Yeni: Saldıran takım için sonucu manuel olarak ekle
    public void ekleSaldiranAtis(boolean golMu) {
        gecmisSaldiran.add(golMu);
    }

    // Yeni: Savunan takım için sonucu manuel olarak ekle
    public void ekleSavunanAtis(boolean kurtardiMi) {
        gecmisSavunan.add(kurtardiMi);
    }
}
