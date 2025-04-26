package com.penaltygame.bot;

public class OyuncuBot {

    public float sutYonu;
    public float yukseklik;
    public float sutGucu;

    public void sutHesapla(){

        sutYonu = (float)((Math.random() * 2) - 1);
        yukseklik = (float)(0.2 + (Math.random() * 0.6));
        sutGucu = (float)(0.5 + (Math.random() * 0.5));
    }
}
