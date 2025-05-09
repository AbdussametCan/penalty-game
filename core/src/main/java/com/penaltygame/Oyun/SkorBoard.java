package com.penaltygame.Oyun;

import java.util.ArrayList;
import java.util.List;

public class SkorBoard {
    private final String takimA;
    private final String takimB;

    private final List<Boolean> atislarA = new ArrayList<>();
    private final List<Boolean> atislarB = new ArrayList<>();

    public SkorBoard(String takimA, String takimB) {
        this.takimA = takimA;
        this.takimB = takimB;
    }

    public void addShot(boolean oyuncuSirada, boolean golOldu) {
        if (oyuncuSirada) {
            atislarA.add(golOldu);
        } else {
            atislarB.add(golOldu);
        }
    }

    public List<Boolean> getAtislarA() {
        return atislarA;
    }

    public List<Boolean> getAtislarB() {
        return atislarB;
    }

    public int getSkorA() {
        return (int) atislarA.stream().filter(b -> b).count();
    }

    public int getSkorB() {
        return (int) atislarB.stream().filter(b -> b).count();
    }

    public String getTakimA() {
        return takimA;
    }

    public String getTakimB() {
        return takimB;
    }

    public void reset() {
        atislarA.clear();
        atislarB.clear();
    }
}
