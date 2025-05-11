package com.penaltygame.Oyun;

import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.graphics.Texture;
import com.penaltygame.bot.KaleciBot;

import java.util.HashMap;
import java.util.Map;

public class Kaleci {
    private KaleciBot bot;
    private Map<String, Texture> pozisyonlar;

    public Kaleci(AssetManager manager) {
        bot = new KaleciBot();
        pozisyonlar = new HashMap<>();

        // Kaleci pozisyonları asset key'leriyle eşleşiyor
        pozisyonlar.put("sol_alt", manager.get("Kaleci/sol_alt.png", Texture.class));
        pozisyonlar.put("sol_üst", manager.get("Kaleci/sol_üst.png", Texture.class));
        pozisyonlar.put("orta", manager.get("Kaleci/orta.png", Texture.class));
        pozisyonlar.put("sag_alt", manager.get("Kaleci/sag_alt.png", Texture.class));
        pozisyonlar.put("sag_üst", manager.get("Kaleci/sag_üst.png", Texture.class));
    }

    // Oyuncunun gerçek yönüne göre kalecinin yön seçmesini sağlar
    public void yeniYonSec(String topYonGercek) {
        bot.yeniYonSec(topYonGercek);
    }

    // Topun yüksekliğine göre kalecinin alt/üst sıçrama kararını belirler
    public void yukseklikAyarla(float topY) {
        bot.yukseklikAyarla(topY);
    }

    // Kalecinin seçtiği yön (left, center, right)
    public String getSecilenYon() {
        return bot.getSecilenYon();
    }

    // Kalecinin sprite görüntüsünü döndürür
    public Texture getPozisyonResmi() {
        String key = bot.getResimAnahtari();
        Texture texture = pozisyonlar.get(key);

        if (texture == null) {
            return pozisyonlar.get("orta");
        }

        return texture;
    }

    // Kalecinin tahmin ettiği tam yön anahtarı (örneğin: "sol_alt", "sag_üst")
    public String getYonAnahtari() {
        return bot.getYonAnahtari();
    }

    // Kaleci yön ve yüksekliğini sıfırlar (her el sonrası çağrılabilir)
    public void resetPozisyon() {
        bot.setSecilenYon("center");
        bot.setYukseklik("alt");
    }
}
