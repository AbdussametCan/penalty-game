package com.penaltygame.Oyun;

import java.util.Random;

public class Kaleci extends Kullanici {
    private String secilenYon = "center";

    public Kaleci(String team) {
        super(team);
    }

    // Rastgele yön seçer (sol, orta, sağ)
    public void yeniYonSec() {
        String[] yonler = {"left", "center", "right"};
        int index = new Random().nextInt(yonler.length);
        secilenYon = yonler[index];
    }

    public String getSecilenYon() {
        return secilenYon;
    }

    public void dive() {
        System.out.println(team + " kalecisi " + secilenYon + " yönüne atladı!");
    }
}
