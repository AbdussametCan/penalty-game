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

    //Aşağıda kaleci ile ilgili metotlar var.
    public void yeniYonSec(String topYonGercek) {
        bot.yeniYonSec(topYonGercek);
    }

    public void yukseklikAyarla(float topY) {
        bot.yukseklikAyarla(topY);
    }

    public String getSecilenYon() {
        return bot.getSecilenYon();
    }

    public Texture getPozisyonResmi() {
        String key = bot.getResimAnahtari();
        Texture texture = pozisyonlar.get(key);

        if (texture == null) {
            System.err.println("⚠ Kaleci pozisyon görseli bulunamadı → key: " + key);
            return pozisyonlar.get("orta");
        }

        return texture;
    }
}
