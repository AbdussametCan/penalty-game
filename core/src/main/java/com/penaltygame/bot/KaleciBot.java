package com.penaltygame.bot;

public class KaleciBot {

    private final float tahminBasarisi = 0.5f;
    private String mevcutHareket = "bos";

    public String yonBelirle(float hedefYon){

        float rastgele = (float) Math.random();

        if(rastgele < tahminBasarisi)
        {
            if(hedefYon < -0.25f)
            {
                return "sol";
            }
            else if(hedefYon > 0.25f)
            {
                return "sag";
            }
            else
            {
                return "orta";
            }
        }
        else
        {
            int yanlisTahminYon = (int)(Math.random() * 3);

            switch(yanlisTahminYon)
            {
                case 0:
                    return "sol";
                case 1:
                    return "orta";
                case 2:
                    return "sag";
            }
        }
        return "orta";
    }

    public void setMevcutHareket(String mevcutHareket){
        this.mevcutHareket = mevcutHareket;
    }

    public String getMevcutHareket(){
        return mevcutHareket;
    }
}
