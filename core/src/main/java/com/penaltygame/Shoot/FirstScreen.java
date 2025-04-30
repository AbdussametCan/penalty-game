package com.penaltygame.Shoot;

import com.badlogic.gdx.*;
import com.badlogic.gdx.graphics.*;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Vector2;
import com.penaltygame.PenaltyGame;
import com.penaltygame.Oyun.Kale;

public class FirstScreen implements Screen {
    final PenaltyGame game;
    Texture ballTexture, directionBar, heightBar, backgroundTexture;
    Vector2 ballPosition;
    Vector2 velocity;
    Kale kale;

    int clickStage = 0;
    float directionValue = 0f;
    float heightValue = 0f;

    float barTimer = 0;
    boolean barIncreasing = true;
    float barSpeed = 1.2f;

    boolean directionLocked = false;
    boolean heightLocked = false;
    boolean isShooting = false;

    private BitmapFont font = new BitmapFont();
    private ShapeRenderer shapeRenderer;

    boolean golOldu = false;
    float golTimer = 0f;

    public FirstScreen(final PenaltyGame game) {
        this.game = game;

        backgroundTexture = new Texture("field_background.png");
        ballTexture = new Texture("Shoot/ball.png");
        directionBar = new Texture("Shoot/bar_1.png");
        heightBar = new Texture("Shoot/bar_2.png");

        ballPosition = new Vector2(960, 150);
        velocity = new Vector2();

        kale = new Kale(500, 430, 920, 270);
        shapeRenderer = new ShapeRenderer();

        Gdx.input.setInputProcessor(new InputAdapter() {
            @Override
            public boolean touchDown(int x, int y, int pointer, int button) {
                if (isShooting) return false;

                if (clickStage == 0) {
                    directionValue = barTimer;
                    directionLocked = true;
                    clickStage++;
                } else if (clickStage == 1) {
                    heightValue = barTimer;
                    heightLocked = true;
                    shootBall();
                    clickStage = 0;
                    directionLocked = heightLocked = false;
                }
                return true;
            }
        });
    }

    private void updateBar(float delta) {
        if (!directionLocked || !heightLocked) {
            if (barIncreasing) {
                barTimer += delta * barSpeed;
                if (barTimer >= 1f) {
                    barTimer = 1f;
                    barIncreasing = false;
                }
            } else {
                barTimer -= delta * barSpeed;
                if (barTimer <= 0f) {
                    barTimer = 0f;
                    barIncreasing = true;
                }
            }
        }
    }

    private void shootBall() {
        float angleX = directionValue * 270f - 135f;
        float angleY = heightValue * 60f + 30f;

        float speed = 600f;

        float radX = (float) Math.toRadians(angleX);
        float radY = (float) Math.toRadians(angleY);

        float dx = (float) Math.sin(radX);
        float dy = (float) Math.sin(radY);

        Vector2 dir = new Vector2(dx, dy).nor();

        velocity.set(dir.scl(speed));
        isShooting = true;
    }

    @Override
    public void render(float delta) {
        updateBar(delta);

        if (golOldu) {
            golTimer -= delta;
            if (golTimer <= 0) {
                golOldu = false;
            }
        }

        if (isShooting) {
            ballPosition.mulAdd(velocity, delta);

            if (ballPosition.x >= kale.getAlan().x &&
                ballPosition.x <= kale.getAlan().x + kale.getAlan().width &&
                ballPosition.y >= kale.getAlan().y + kale.getAlan().height / 2) {
                golOldu = true;
                golTimer = 2f;
                reset();
            }

            if (ballPosition.y > Gdx.graphics.getHeight()) {
                reset();
            }
        }

        game.batch.begin();
        game.batch.draw(backgroundTexture, 0, 0);
        game.batch.draw(ballTexture, ballPosition.x - 16, ballPosition.y - 16, 32, 32);

        drawBars();

        if (golOldu) {
            font.getData().setScale(4f);
            font.draw(game.batch, "GOOOOL!!!", 700, 600);
            font.getData().setScale(1f);
        }

        game.batch.end();

        shapeRenderer.begin(ShapeRenderer.ShapeType.Line);
        shapeRenderer.setColor(Color.RED);
        shapeRenderer.rect(kale.getAlan().x, kale.getAlan().y, kale.getAlan().width, kale.getAlan().height);
        shapeRenderer.end();
    }

    private void drawBars() {
        int barWidth = 20;
        int maxHeight = 150;
        float baseY = 50;

        int spacing = 40;

        if (!directionLocked) {
            float dirWidth = barTimer * maxHeight;
            game.batch.draw(directionBar, 50, baseY, dirWidth, barWidth);
            font.draw(game.batch, "L", 30, baseY + 10);
            font.draw(game.batch, "R", 50 + maxHeight + 10, baseY + 10);
        } else if (!heightLocked) {
            float heightBarHeight = barTimer * maxHeight;
            game.batch.draw(heightBar, 150, baseY, barWidth, heightBarHeight);
            font.draw(game.batch, "Low", 140, baseY - 10);
            font.draw(game.batch, "High", 140, baseY + maxHeight + 15);
        }
    }

    public void reset() {
        ballPosition.set(960, 150);
        velocity.set(0, 0);
        isShooting = false;
    }

    @Override public void dispose() {
        ballTexture.dispose();
        directionBar.dispose();
        heightBar.dispose();
        backgroundTexture.dispose();
        shapeRenderer.dispose();
    }

    @Override public void show() {}
    @Override public void resize(int width, int height) {}
    @Override public void pause() {}
    @Override public void resume() {}
    @Override public void hide() {}
}
