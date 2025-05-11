package com.penaltygame.Oyun;

import com.badlogic.gdx.math.Rectangle;

// Kale ölçüleri ile ilgili metotlar yer alıyor.
public class Kale {
    private Rectangle alan;

    public Kale(float x, float y, float width, float height) {
        alan = new Rectangle(x, y, width, height);
    }

    public Rectangle getAlan() {
        return alan;
    }

}
