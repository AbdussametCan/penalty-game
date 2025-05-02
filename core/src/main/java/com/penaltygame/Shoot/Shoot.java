package com.penaltygame.Shoot;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.math.Vector2;
import com.penaltygame.Oyun.Kale;

public class Shoot {
    private Vector2 ballPosition, velocity;
    private float directionTimer = 0f, powerTimer = 0f;
    private boolean directionIncreasing = true, powerIncreasing = true;
    private boolean directionLocked = false, powerLocked = false;
    private boolean isShooting = false;
    private float directionValue = 0f, powerValue = 0f;
    private String topYonu = "";

    public Shoot() {
        ballPosition = new Vector2(960, 150);
        velocity = new Vector2();
    }

    public void updateBars(float delta) {
        if (!directionLocked) {
            directionTimer += (directionIncreasing ? 1 : -1) * delta;
            if (directionTimer >= 1f) {
                directionTimer = 1f;
                directionIncreasing = false;
            } else if (directionTimer <= 0f) {
                directionTimer = 0f;
                directionIncreasing = true;
            }
        }

        if (!powerLocked && directionLocked) {
            powerTimer += (powerIncreasing ? 1 : -1) * delta;
            if (powerTimer >= 1f) {
                powerTimer = 1f;
                powerIncreasing = false;
            } else if (powerTimer <= 0f) {
                powerTimer = 0f;
                powerIncreasing = true;
            }
        }
    }

    public void lockDirection() {
        directionValue = directionTimer;
        directionLocked = true;
    }

    public void lockPower() {
        powerValue = powerTimer;
        powerLocked = true;
        float angle = directionValue * 180f;
        float speed = 300f + powerValue * 400f;
        float rad = (float) Math.toRadians(angle);
        Vector2 dir = new Vector2((float) Math.cos(rad), (float) Math.sin(rad)).nor();
        velocity.set(dir.scl(speed));
        isShooting = true;

        topYonu = angle < 60 ? "right" : angle > 120 ? "left" : "center";
    }

    public void updateBall(float delta) {
        if (isShooting) {
            ballPosition.mulAdd(velocity, delta);
        }
    }

    public boolean isShotComplete(Kale kale) {
        return ballPosition.x < 0 || ballPosition.x > Gdx.graphics.getWidth() ||
            ballPosition.y < 0 || ballPosition.y > Gdx.graphics.getHeight() ||
            isGoal(kale) || isSaved();
    }

    public boolean isGoal(Kale kale) {
        return ballPosition.x >= kale.getAlan().x &&
            ballPosition.x <= kale.getAlan().x + kale.getAlan().width &&
            ballPosition.y >= kale.getAlan().y + kale.getAlan().height / 2;
    }

    public boolean isSaved(String kaleciYonu) {
        return ballPosition.y > 250 && ballPosition.y < 400 &&
            kaleciYonu.equals(topYonu);
    }

    public boolean isSaved() {
        return false; // dışarıdan yön verilmeden kontrol edilemez
    }

    public void reset() {
        ballPosition.set(960, 150);
        velocity.set(0, 0);
        isShooting = false;
        directionTimer = powerTimer = 0f;
        directionLocked = powerLocked = false;
        directionIncreasing = powerIncreasing = true;
        topYonu = "";
    }

    public Vector2 getBallPosition() {
        return ballPosition;
    }

    public boolean isShooting() {
        return isShooting;
    }

    public boolean isDirectionLocked() {
        return directionLocked;
    }

    public boolean isPowerLocked() {
        return powerLocked;
    }

    public float getDirectionTimer() {
        return directionTimer;
    }

    public float getPowerTimer() {
        return powerTimer;
    }

    public String getTopYonu() {
        return topYonu;
    }
}
