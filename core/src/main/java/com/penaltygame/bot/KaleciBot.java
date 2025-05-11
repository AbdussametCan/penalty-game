package com.penaltygame.bot;

import java.util.Random;

public class KaleciBot {
    private String secilenYon = "center"; // left, center, right
    private String yukseklik = "alt";     // alt, üst
    private Random random = new Random();

    public void setSecilenYon(String yon){
        this.secilenYon = yon;
    }

    public void setYukseklik(String yukseklik){
        this.yukseklik = yukseklik;
    }

    // Oyuncunun gerçek yönüne göre %50 ihtimalle doğru tahmin yapar
    public void yeniYonSec(String topYonGercek) {
        boolean dogruTahmin = random.nextBoolean();
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

    // Görsel için kullanılan sprite anahtarını döndürür
    public String getResimAnahtari() {
        switch (secilenYon) {
            case "left": return "sol_" + yukseklik;
            case "right": return "sag_" + yukseklik;
            default: return "orta";
        }
    }

    // Kalecinin gerçek yön tahminini döndürür — Shoot sınıfı ile karşılaştırmada kullanılır
    public String getYonAnahtari() {
        switch (secilenYon) {
            case "left": return "sol_" + yukseklik;
            case "right": return "sag_" + yukseklik;
            default: return "orta";
        }
    }

    public String getSecilenYon() {
        return secilenYon;
    }
}

