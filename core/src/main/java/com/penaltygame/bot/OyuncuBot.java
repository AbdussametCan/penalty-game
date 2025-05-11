package com.penaltygame.bot;

public class OyuncuBot {

    public float sutYonu;
    public float yukseklik;
    public float sutGucu;

    // Bot oyuncunun şut yönü, yüksekliği ve gücünün rastgele belirlendiği yer.

    public void sutHesapla() {
        double r = Math.random();

        if (r < 0.95) {
            sutYonu = (float)(80 + Math.random() * 20);
        }
        else {
            sutYonu = Math.random() < 0.5 ? (float)(45 + Math.random() * 30)
                : (float)(105 + Math.random() * 30);
        }

        yukseklik = (float)(0.4 + Math.random() * 0.2); // 0.4–0.6
        sutGucu = (float)(0.65 + Math.random() * 0.2);  // 0.65–0.85
    }
}
