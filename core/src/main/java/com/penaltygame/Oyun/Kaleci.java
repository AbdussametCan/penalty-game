package com.penaltygame.Oyun;


import java.util.Random;

public class Kaleci extends Kullanici {

    public Kaleci(String team) {
        super(team);
    }

    // Manuel yönle atlama
    public void dive(String direction) {
        System.out.println(team + " kalecisi " + direction + " yönüne atladı.");
    }

    // Yapay zekâ: rastgele yön seçer
    public String rastgeleYonBelirle() {
        String[] yonler = {"left", "center", "right"};
        int index = new Random().nextInt(yonler.length);
        return yonler[index];
    }
}

