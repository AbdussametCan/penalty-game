package com.penaltygame.Shoot;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.*;
import com.badlogic.gdx.graphics.*;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Vector2;
import com.penaltygame.PenaltyGame;

public class FirstScreen implements Screen {
    final PenaltyGame game;
    Texture ballTexture, directionBar, heightBar, powerBar;
    Vector2 ballPosition;
    Vector2 velocity;

    int clickStage = 0;
    float power = 0f;
    float directionX = 0f;
    float height = 0f;

    float barSpeed = 1.5f;
    float barTimer = 0;
    boolean barIncreasing = true;

    boolean isShooting = false;

    float directionValue = 0f, heightValue = 0f, powerValue = 0f;
    boolean directionLocked = false, heightLocked = false, powerLocked = false;

    public FirstScreen(final PenaltyGame game) {
        this.game = game;

        ballTexture = new Texture("Shoot/ball.png");
        directionBar = new Texture("Shoot/bar_1.png");
        heightBar = new Texture("Shoot/bar_2.png");
        powerBar = new Texture("Shoot/bar_3.png");

        ballPosition = new Vector2(400, 100);
        velocity = new Vector2();

        Gdx.input.setInputProcessor(new InputAdapter() {
            @Override
            public boolean touchDown(int x, int y, int pointer, int button) {
                if (isShooting) return false;

                switch (clickStage) {
                    case 0:
                        directionValue = barTimer;
                        directionLocked = true;
                        directionX = directionValue;
                        clickStage++;
                        break;
                    case 1:
                        heightValue = barTimer;
                        heightLocked = true;
                        height = heightValue;
                        clickStage++;
                        break;
                    case 2:
                        powerValue = barTimer;
                        powerLocked = true;
                        power = powerValue;
                        shootBall();
                        clickStage = 0;
                        directionLocked = heightLocked = powerLocked = false;
                        break;
                }
                return true;
            }
        });
    }

    private void updateBar(float delta) {
        if (!directionLocked && clickStage == 0) {
            updateBarTimer(delta);
            directionValue = barTimer;
        } else if (!heightLocked && clickStage == 1) {
            updateBarTimer(delta);
            heightValue = barTimer;
        } else if (!powerLocked && clickStage == 2) {
            updateBarTimer(delta);
            powerValue = barTimer;
        }
    }

    private void updateBarTimer(float delta) {
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

    private void shootBall() {
        float angleY = height * 60 + 15;         // Yukarı yön: 15° - 75°
        float angleX = directionX * 120 - 60;    // Yön: -60° (sol) ile +60° (sağ) arası

        float speed = 300 + power * 400;

        float radX = (float) Math.toRadians(angleX);
        float radY = (float) Math.toRadians(angleY);

        // Yön vektörünü açılarla oluştur
        Vector2 dir = new Vector2((float) Math.sin(radX), (float) Math.sin(radY)).nor();

        velocity.set(dir.scl(speed));
        isShooting = true;
    }


    @Override
    public void render(float delta) {
        updateBar(delta);

        Gdx.gl.glClearColor(0, 0.5f, 0, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        if (isShooting) {
            ballPosition.mulAdd(velocity, delta);
            if (ballPosition.y > Gdx.graphics.getHeight()) {
                reset();
            }
        }

        game.batch.begin();
        game.batch.draw(ballTexture, ballPosition.x - 16, ballPosition.y - 16, 32, 32);
        game.batch.end();

        drawBars();
    }

    private BitmapFont font = new BitmapFont(); // Sınıfın üstüne eklenmeli

    private void drawBars() {
        game.batch.begin();

        int barWidth = 20;
        int maxHeight = 150;
        float baseY = 50;

        // Barlar arası mesafe
        int spacing = 40;

        // Bar X konumları
        float directionX = 50f;
        float heightX = directionX + maxHeight + spacing;      // direction bar bitimi + spacing
        float powerX = heightX + spacing + barWidth;

        // Direction bar (soldan sağa)
        float dirWidth = directionValue * maxHeight;
        game.batch.draw(directionBar, directionX, baseY, dirWidth, barWidth);

        // Label: L - R
        font.draw(game.batch, "L", directionX - 15f, baseY + barWidth / 2f + 5f);
        font.draw(game.batch, "R", directionX + maxHeight + 5f, baseY + barWidth / 2f + 5f);

        // Height bar (aşağıdan yukarı)
        float hgtHeight = heightValue * maxHeight;
        game.batch.draw(heightBar, heightX, baseY, barWidth, hgtHeight);

        font.draw(game.batch, "Low", heightX, baseY - 10f);
        font.draw(game.batch, "High", heightX, baseY + maxHeight + 15f);

        // Power bar
        float pwrHeight = powerValue * maxHeight;
        game.batch.draw(powerBar, powerX, baseY, barWidth, pwrHeight);

        font.draw(game.batch, "Min", powerX, baseY - 10f);
        font.draw(game.batch, "Max", powerX, baseY + maxHeight + 15f);

        game.batch.end();
    }




    public void reset() {
        ballPosition.set(400, 100);
        velocity.set(0, 0);
        isShooting = false;
    }

    @Override public void dispose() {
        ballTexture.dispose();
        directionBar.dispose();
        heightBar.dispose();
        powerBar.dispose();
    }

    @Override public void show() {}
    @Override public void resize(int width, int height) {}
    @Override public void pause() {}
    @Override public void resume() {}
    @Override public void hide(){}
}
