package com.penaltygame.Shoot;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.math.Vector2;
import com.penaltygame.Oyun.Kale;

public class Shoot {
    private Vector2 topPozisyon, hiz;
    private float yonZamanlayici = 0f, gucZamanlayici = 0f;
    private boolean yonArtisi = true, gucArtisi = true;
    private boolean yonKilit = false, gucKilit = false;
    private boolean isShooting = false;
    private float yonDegeri = 0f, gucDegeri = 0f;
    private String topYonu = "";


    public Shoot() {
        topPozisyon = new Vector2(960, 150);
        hiz= new Vector2();
    }

    // Yön ve güç göstergelerini  günceller.
    public void updateBars(float delta) {
        if (!yonKilit) {
            yonZamanlayici += (yonArtisi ? 1 : -1) * delta;
            if (yonZamanlayici >= 1f) {
                yonZamanlayici = 1f;
                yonArtisi = false;
            }
            else if (yonZamanlayici <= 0f) {
                yonZamanlayici = 0f;
                yonArtisi = true;
            }
        }

        if (!gucKilit && yonKilit) {
            gucZamanlayici += (gucArtisi ? 1 : -1) * delta;
            if (gucZamanlayici >= 1f) {
                gucZamanlayici = 1f;
                gucArtisi = false;
            }
            else if (gucZamanlayici <= 0f) {
                gucZamanlayici = 0f;
                gucArtisi = true;
            }
        }
    }

    public Vector2 getVelocity() {
        return hiz;
    }

    // Oyuncu yön barını kilitlediğinde çağrılır.
    public void lockDirection() {
        yonDegeri = yonZamanlayici;
        yonKilit = true;
    }

    // Oyuncu güç barını kilitlediğinde çağrılır.
    public void lockPower() {
        gucDegeri = gucZamanlayici;
        gucKilit = true;

        float angle = 90f + (yonDegeri - 0.5f) * 2f * 90f; // 180°'lik dönüş
        float speed = 300f + gucDegeri * 700f;
        float rad = (float) Math.toRadians(angle);
        Vector2 dir = new Vector2((float) Math.cos(rad), (float) Math.sin(rad)).nor();
        hiz.set(dir.scl(speed));
        isShooting = true;

        topYonu = angle < 60 ? "right" : angle > 120 ? "left" : "center";
    }

    // BOT'un şut çekmesini sağlar.
    public void baslaBotSutu(float angle, float power, float height) {
        this.yonKilit = true;
        this.gucKilit = true;
        this.yonZamanlayici = (angle - 90f) / 90f / 2f + 0.5f; // açıyı directionTimer'a çevir
        this.gucZamanlayici = power;
        this.topPozisyon.set(960, 150);
        this.hiz.set(
            (float) Math.cos(Math.toRadians(angle)) * power * 800,
            height * power * 900
        );
        this.isShooting = true;

        // 🔧 Buraya yön ataması ekle
        topYonu = angle < 60 ? "right" : angle > 120 ? "left" : "orta";
    }


    // Topun pozisyonunun günceller.
    public void updateBall(float delta) {
        if (isShooting) {
            topPozisyon.mulAdd(hiz, delta);
        }
    }
    // Şutun tamamlanıp tamamlanmadığını kontrol eder.
    public boolean isShotComplete(Kale kale) {
        return topPozisyon.x < 0 || topPozisyon.x > Gdx.graphics.getWidth() ||
            topPozisyon.y < 0 || topPozisyon.y > Gdx.graphics.getHeight() ||
            isGoal(kale) || isSaved();
    }

    // Topun kaleye girip girmediğini kontrol eder.
    public boolean isGoal(Kale kale) {
        return topPozisyon.x >= kale.getAlan().x &&
            topPozisyon.x <= kale.getAlan().x + kale.getAlan().width &&
            topPozisyon.y >= kale.getAlan().y + kale.getAlan().height / 2;
    }
    // Topun kurtarılıp kurtarılmadığını kontrol eder.
    public boolean isSaved(String kaleciYonu) {
        return topPozisyon.y > 250 && topPozisyon.y < 400 &&
            kaleciYonu.equals(topYonu);
    }

    public boolean isSaved() {
        return false;
    }

    // Topun ve ilgili tüm kontrol değişkenlerinin başlangıç durumuna sıfırlanması sağlar.
    public void reset() {
        topPozisyon.set(960, 150);
        hiz.set(0, 0);
        isShooting = false;
        yonZamanlayici = gucZamanlayici = 0f;
        yonKilit = gucKilit = false;
        yonArtisi = gucArtisi = true;
        topYonu = "";
    }

    public Vector2 getBallPosition() {
        return topPozisyon;
    }

    public boolean isShooting() {
        return isShooting;
    }

    public boolean isDirectionLocked() {
        return yonKilit;
    }
    public boolean isPowerLocked() {
        return gucKilit;
    }
    public float getDirectionTimer() {
        return yonZamanlayici;
    }

    public float getPowerTimer() {
        return gucZamanlayici;
    }

    public String getTopYonu() {
        return topYonu;
    }
    public String getTopYonKey() {
        String yukseklik = getBallPosition().y < 500 ? "alt" : "üst";
        switch (topYonu) {
            case "left": return "sol_" + yukseklik;
            case "right": return "sag_" + yukseklik;
            case "center": return "orta";
            default:
                return "orta";
        }
    }
}
