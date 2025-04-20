package com.penaltygame.Oyun;

public class Top {
    private String direction;
    private int speed;

    // Kullanıcıdan gelen yön ve hızı ayarlamak için
    public void setTrajectory(String direction, int speed) {
        this.direction = direction;
        this.speed = speed;
    }

    // Bilgileri almak için
    public String getDirection() {
        return direction;
    }

    public int getSpeed() {
        return speed;
    }

    // Yapay zekâ topu rastgele bir yöne gönderir
    public void rastgeleAtisYap() {
        String[] yonler = {"left", "center", "right"};
        int index = (int)(Math.random() * yonler.length);
        int hiz = 70 + (int)(Math.random() * 31); // 70-100
        this.direction = yonler[index];
        this.speed = hiz;
    }
}
