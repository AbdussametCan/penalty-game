package com.penaltygame.Shoot;

import com.badlogic.gdx.*;
import com.badlogic.gdx.graphics.*;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Vector2;
import com.penaltygame.PenaltyGame;
import com.penaltygame.Oyun.Kale;
import com.penaltygame.Oyun.Kaleci;
import com.penaltygame.Oyun.SkorBoard;

public class FirstScreen implements Screen {
    final PenaltyGame game;
    Texture ballTexture, backgroundTexture, goalkeeperTexture;
    Vector2 ballPosition, velocity, kaleciPozisyon;
    Kale kale;
    Kaleci kaleci;
    SkorBoard skorBoard;

    BitmapFont font = new BitmapFont();
    ShapeRenderer shapeRenderer = new ShapeRenderer();

    int clickStage = 0;
    boolean oyuncuSirasi = true;
    boolean directionLocked = false, powerLocked = false, isShooting = false;
    boolean golOldu = false, kurtardi = false, oyunBitti = false;
    float directionTimer = 0f, powerTimer = 0f;
    boolean directionIncreasing = true, powerIncreasing = true;
    float directionValue = 0f, powerValue = 0f;
    float mesajTimer = 0f;

    int atisSayisiOyuncu = 0, atisSayisiBot = 0;
    final int maxAtis = 5;

    String topYonu = "", kazananTakim = "";

    public FirstScreen(final PenaltyGame game, String takim1, String takim2) {
        this.game = game;
        ballTexture = new Texture("Shoot/ball.png");
        backgroundTexture = new Texture("field_background.png");

        try {
            goalkeeperTexture = new Texture("goalkeeper_idle.png");
        } catch (Exception e) {
            goalkeeperTexture = null;
        }

        kale = new Kale(500, 430, 920, 270);
        kaleci = new Kaleci("Bot");
        skorBoard = new SkorBoard(takim1, takim2);
        ballPosition = new Vector2(960, 150);
        velocity = new Vector2();
        kaleciPozisyon = new Vector2(910, 430);

        Gdx.input.setInputProcessor(new InputAdapter() {
            @Override
            public boolean touchDown(int x, int y, int pointer, int button) {
                if (isShooting || oyunBitti || !oyuncuSirasi) return false;

                if (clickStage == 0) {
                    directionValue = directionTimer;
                    directionLocked = true;
                    clickStage++;
                } else if (clickStage == 1) {
                    powerValue = powerTimer;
                    powerLocked = true;
                    kaleci.yeniYonSec();
                    setKaleciPozisyon();
                    shootBall();
                    directionLocked = powerLocked = false;
                    clickStage = 0;
                }
                return true;
            }
        });
    }

    private void setKaleciPozisyon() {
        switch (kaleci.getSecilenYon()) {
            case "left": kaleciPozisyon.set(750, 430); break;
            case "right": kaleciPozisyon.set(1070, 430); break;
            default: kaleciPozisyon.set(910, 430); break;
        }
    }

    private void updateBars(float delta) {
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

    private void shootBall() {
        float angle = directionValue * 180f;
        float speed = 300f + powerValue * 400f;
        float rad = (float) Math.toRadians(angle);
        Vector2 dir = new Vector2((float) Math.cos(rad), (float) Math.sin(rad)).nor();

        velocity.set(dir.scl(speed));
        isShooting = true;

        topYonu = angle < 60 ? "right" : angle > 120 ? "left" : "center";
    }

    private void tamamlaSira() {
        if (oyuncuSirasi) atisSayisiOyuncu++;
        else atisSayisiBot++;

        // 🏁 Bitirme koşulları
        boolean maxReached = atisSayisiOyuncu >= maxAtis && atisSayisiBot >= maxAtis;
        boolean oyuncuKazandi = skorBoard.getSkorSaldiran() > skorBoard.getSkorSavunan();
        boolean botKazandi = skorBoard.getSkorSavunan() > skorBoard.getSkorSaldiran();

        if ((atisSayisiOyuncu >= maxAtis || atisSayisiBot >= maxAtis)) {
            if (atisSayisiOyuncu == atisSayisiBot) {
                if (oyuncuKazandi || botKazandi) {
                    oyunBitti = true;
                    kazananTakim = oyuncuKazandi
                        ? skorBoard.getTakimSaldiran()
                        : skorBoard.getTakimSavunan();
                    return;
                }
            } else if (skorBoard.getSkorSaldiran() > skorBoard.getSkorSavunan() + (maxAtis - atisSayisiBot) ||
                skorBoard.getSkorSavunan() > skorBoard.getSkorSaldiran() + (maxAtis - atisSayisiOyuncu)) {
                oyunBitti = true;
                kazananTakim = oyuncuKazandi
                    ? skorBoard.getTakimSaldiran()
                    : skorBoard.getTakimSavunan();
                return;
            }
        }

        // Sırayı değiştir ve imleç/güç sıfırla
        oyuncuSirasi = !oyuncuSirasi;
        directionTimer = 0f;
        directionIncreasing = true;
        powerTimer = 0f;
        powerIncreasing = true;
        directionLocked = powerLocked = false;
        clickStage = 0;
    }

    @Override
    public void render(float delta) {
        updateBars(delta);

        if ((golOldu || kurtardi) && (mesajTimer -= delta) <= 0) {
            golOldu = kurtardi = false;
            tamamlaSira();
        }

        if (isShooting) {
            ballPosition.mulAdd(velocity, delta);
            if (ballPosition.y > 250 && ballPosition.y < 400 &&
                kaleci.getSecilenYon().equals(topYonu)) {
                kurtardi = true;
                skorBoard.kurtardi();
                mesajTimer = 2f;
                isShooting = false;
                reset();
            } else if (ballPosition.x >= kale.getAlan().x &&
                ballPosition.x <= kale.getAlan().x + kale.getAlan().width &&
                ballPosition.y >= kale.getAlan().y + kale.getAlan().height / 2) {
                golOldu = true;
                skorBoard.golAtti();
                mesajTimer = 2f;
                isShooting = false;
                reset();
            } else if (ballPosition.x < 0 || ballPosition.x > Gdx.graphics.getWidth() ||
                ballPosition.y < 0 || ballPosition.y > Gdx.graphics.getHeight()) {
                isShooting = false;
                reset();
                tamamlaSira();
            }
        }

        game.batch.begin();
        game.batch.draw(backgroundTexture, 0, 0);
        game.batch.draw(ballTexture, ballPosition.x - 16, ballPosition.y - 16, 32, 32);
        if (goalkeeperTexture != null)
            game.batch.draw(goalkeeperTexture, kaleciPozisyon.x, kaleciPozisyon.y, 64, 96);

        font.getData().setScale(4f);
        if (golOldu) font.draw(game.batch, "GOOOOL!", 700, 600);
        else if (kurtardi) font.draw(game.batch, "KURTARDI!", 680, 600);
        else if (oyunBitti) font.draw(game.batch, kazananTakim + " KAZANDI!", 500, 650);

        font.getData().setScale(1.5f);
        font.setColor(Color.WHITE);
        font.draw(game.batch, skorBoard.getTakimSaldiran() + ": " + skorBoard.getSkorSaldiran(), Gdx.graphics.getWidth() - 400, 100);
        font.draw(game.batch, skorBoard.getTakimSavunan() + ": " + skorBoard.getSkorSavunan(), Gdx.graphics.getWidth() - 400, 70);
        game.batch.end();

        drawIndicators();

        shapeRenderer.begin(ShapeRenderer.ShapeType.Line);
        shapeRenderer.setColor(Color.RED);
        shapeRenderer.rect(kale.getAlan().x, kale.getAlan().y, kale.getAlan().width, kale.getAlan().height);
        shapeRenderer.end();
    }

    private void drawIndicators() {
        if (isShooting || oyunBitti) return;

        shapeRenderer.begin(ShapeRenderer.ShapeType.Filled);
        if (!directionLocked) {
            float angle = directionTimer * 180f;
            float rad = (float) Math.toRadians(angle);
            float length = 100f;
            float startX = 960, startY = 150;
            float endX = startX + (float) Math.cos(rad) * length;
            float endY = startY + (float) Math.sin(rad) * length;
            shapeRenderer.setColor(Color.YELLOW);
            shapeRenderer.rectLine(startX, startY, endX, endY, 5f);
        }

        if (!powerLocked && directionLocked) {
            shapeRenderer.setColor(Color.BLUE);
            shapeRenderer.rect(50, 50, 20, powerTimer * 150);
        }

        shapeRenderer.end();
    }

    public void reset() {
        ballPosition.set(960, 150);
        velocity.set(0, 0);
        isShooting = false;
    }

    @Override public void dispose() {
        ballTexture.dispose();
        backgroundTexture.dispose();
        if (goalkeeperTexture != null) goalkeeperTexture.dispose();
        shapeRenderer.dispose();
    }

    @Override public void show() {}
    @Override public void resize(int width, int height) {}
    @Override public void pause() {}
    @Override public void resume() {}
    @Override public void hide() {}
}
