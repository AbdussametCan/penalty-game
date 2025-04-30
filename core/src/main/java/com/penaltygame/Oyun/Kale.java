package com.penaltygame.Oyun;

import com.badlogic.gdx.math.Rectangle;

public class Kale {
    private Rectangle alan;

    public Kale(float x, float y, float width, float height) {
        alan = new Rectangle(x, y, width, height);
    }

    public boolean golMu(float topX, float topY) {
        return alan.contains(topX, topY);
    }

    public Rectangle getAlan() {
        return alan;
    }

}
