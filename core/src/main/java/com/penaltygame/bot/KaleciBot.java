package com.penaltygame.bot;

import java.util.Random;

public class KaleciBot {
    private String secilenYon = "center"; // left, center, right
    private String yukseklik = "alt";     // alt, üst
    private Random random = new Random();

    // Oyuncunun gerçek yönüne göre %50 tahmin etsin
    public void yeniYonSec(String topYonGercek) {
        boolean dogruTahmin = random.nextBoolean(); // %50 ihtimalle true
        if (dogruTahmin) {
            secilenYon = topYonGercek;
        } else {
            String[] digerYonler;
            switch (topYonGercek) {
                case "left":
                    digerYonler = new String[]{"center", "right"};
                    break;
                case "center":
                    digerYonler = new String[]{"left", "right"};
                    break;
                default:
                    digerYonler = new String[]{"left", "center"};
                    break;
            }

            secilenYon = digerYonler[random.nextInt(2)];
        }
    }

    public void yukseklikAyarla(float topY) {
        yukseklik = topY < 500 ? "alt" : "üst";
    }

    public String getSecilenYon() {
        return secilenYon;
    }

    public String getYukseklik() {
        return yukseklik;
    }

    public String getTextureKey() {
        // "center" yönüne "orta" ismini döndür
        if (secilenYon.equals("center")) {
            return "orta";
        }
        return secilenYon + "_" + yukseklik;
    }
}
