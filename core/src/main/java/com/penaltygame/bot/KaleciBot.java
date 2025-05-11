package com.penaltygame.bot;

import java.util.Random;

public class KaleciBot {
    private String secilenYon = "center"; // left, center, right
    private String yukseklik = "alt";     // alt, üst
    private Random random = new Random();

    // Oyuncunun gerçek yönüne göre %50 tahmin eder
    public void yeniYonSec(String topYonGercek) {
        boolean dogruTahmin = random.nextBoolean();
        if (dogruTahmin) {
            secilenYon = topYonGercek;
        }
        else {
            String[] digerYonler;
            switch (topYonGercek) {  //yönler tanımlandı.
                case "left":
                    digerYonler = new String[]{
                        "center", "right"};
                    break;
                case "center":
                    digerYonler = new String[]{
                        "left", "right"};
                    break;
                default:
                    digerYonler = new String[]{
                        "left", "center"};
                    break;
            }

            secilenYon = digerYonler[random.nextInt(2)];
        }
    }

    public void yukseklikAyarla(float topY) { // Yükseklik ayarı yapar.
        yukseklik = topY < 500 ? "alt" : "üst";
    }

    public String getSecilenYon() {
        return secilenYon;
    }


    public String getResimAnahtari() { //Uygun pozisyon ile uygun resmi birleştirir.
        switch (secilenYon) {
            case "left": return "sol_" + yukseklik;
            case "right": return "sag_" + yukseklik;
            default: return "orta";
        }
    }
}
