package com.penaltygame.GoalKeeper;


import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;


public class PlayerGoalkeeper {
    public enum State { IDLE, MOVING }
    public State currentState = State.IDLE;

    public Vector2 position = new Vector2(400, 100); // Başlangıç pozisyonu

    private Vector2 targetPosition = new Vector2(position);
    private float moveSpeed = 600f;

    public void update(float delta) {
        if (Gdx.input.justTouched()) {
            float touchX = Gdx.input.getX();
            float touchY = Gdx.graphics.getHeight() - Gdx.input.getY();
            targetPosition.set(touchX, touchY);
            currentState = State.MOVING;
        }

        // Hareket
        if (currentState == State.MOVING) {
            Vector2 direction = new Vector2(targetPosition).sub(position).nor();
            position.mulAdd(direction, moveSpeed * delta);

            if (position.dst(targetPosition) < 10f) {
                position.set(targetPosition);
                currentState = State.IDLE;
            }
        }
    }

    public void draw(SpriteBatch batch, TextureRegion currentFrame) {
        batch.draw(currentFrame, position.x, position.y);
    }
}
