package com.penaltygame.bot;

public class PenaltyBot{

    public static void main(String[] args){

        KaleciBot kaleci = new KaleciBot();
        OyuncuBot oyuncu = new OyuncuBot();

        oyuncu.sutHesapla();
        String botKarar = kaleci.yonBelirle(oyuncu.sutYonu);
        kaleci.setMevcutHareket(botKarar);
    }
}
