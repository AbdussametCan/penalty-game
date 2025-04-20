package com.penaltygame.Oyun;

public class Oyuncu extends Kullanici {

    public Oyuncu(String team) {
        super(team);
    }

    public void shoot(Top top, String direction, int speed) {
        top.setTrajectory(direction, speed);
    }

}
