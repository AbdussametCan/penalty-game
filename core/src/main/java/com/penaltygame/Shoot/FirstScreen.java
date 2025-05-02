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
    Texture ballTexture, backgroundTexture;
    Vector2 kaleciPozisyon;
    Kale kale;
    Kaleci kaleci;
    SkorBoard skorBoard;
    Shoot shoot;

    BitmapFont font = new BitmapFont();
    ShapeRenderer shapeRenderer = new ShapeRenderer();

    int clickStage = 0;
    boolean oyuncuSirasi = true;
    boolean golOldu = false, kurtardi = false, oyunBitti = false;
    float mesajTimer = 0f;

    int atisSayisiOyuncu = 0, atisSayisiBot = 0;
    final int maxAtis = 5;
    boolean kaleciKararVerdi = false;

    String kazananTakim = "";

    public FirstScreen(final PenaltyGame game, String takim1, String takim2) {
        this.game = game;
        ballTexture = new Texture("Shoot/ball.png");
        backgroundTexture = new Texture("field_background.png");

        kale = new Kale(500, 430, 920, 270);
        kaleci = new Kaleci(game.assetManager);
        skorBoard = new SkorBoard(takim1, takim2);
        kaleciPozisyon = new Vector2(910, 430);
        shoot = new Shoot();

        Gdx.input.setInputProcessor(new InputAdapter() {
            @Override
            public boolean touchDown(int x, int y, int pointer, int button) {
                if (shoot.isShooting() || oyunBitti || !oyuncuSirasi) return false;

                if (clickStage == 0) {
                    shoot.lockDirection();
                    clickStage++;
                } else if (clickStage == 1) {
                    shoot.lockPower();
                    clickStage = 0;
                }
                return true;
            }
        });
    }

    private void tamamlaSira() {
        kaleciKararVerdi = false;
        if (oyuncuSirasi) atisSayisiOyuncu++;
        else atisSayisiBot++;

        boolean oyuncuKazandi = skorBoard.getSkorSaldiran() > skorBoard.getSkorSavunan();
        boolean botKazandi = skorBoard.getSkorSavunan() > skorBoard.getSkorSaldiran();

        if ((atisSayisiOyuncu >= maxAtis || atisSayisiBot >= maxAtis)) {
            if (atisSayisiOyuncu == atisSayisiBot) {
                if (oyuncuKazandi || botKazandi) {
                    oyunBitti = true;
                    kazananTakim = oyuncuKazandi ? skorBoard.getTakimSaldiran() : skorBoard.getTakimSavunan();
                    return;
                }
            } else if (skorBoard.getSkorSaldiran() > skorBoard.getSkorSavunan() + (maxAtis - atisSayisiBot) ||
                skorBoard.getSkorSavunan() > skorBoard.getSkorSaldiran() + (maxAtis - atisSayisiOyuncu)) {
                oyunBitti = true;
                kazananTakim = oyuncuKazandi ? skorBoard.getTakimSaldiran() : skorBoard.getTakimSavunan();
                return;
            }
        }

        oyuncuSirasi = !oyuncuSirasi;
        clickStage = 0;
        shoot.reset();
    }

    @Override
    public void render(float delta) {
        shoot.updateBars(delta);

        if ((golOldu || kurtardi) && (mesajTimer -= delta) <= 0) {
            golOldu = kurtardi = false;
            tamamlaSira();
        }

        if (shoot.isShooting()) {
            if (!kaleciKararVerdi) {
                kaleci.yeniYonSec(shoot.getTopYonu());
                kaleci.yukseklikAyarla(shoot.getBallPosition().y);
                kaleciKararVerdi = true;
            }

            shoot.updateBall(delta);
            if (shoot.isSaved(kaleci.getSecilenYon())) {
                kurtardi = true;
                skorBoard.kurtardi();
                mesajTimer = 2f;
                shoot.reset();
            } else if (shoot.isGoal(kale)) {
                golOldu = true;
                skorBoard.golAtti();
                mesajTimer = 2f;
                shoot.reset();
            } else if (shoot.isShotComplete(kale)) {
                shoot.reset();
                tamamlaSira();
            }
        }

        game.batch.begin();
        game.batch.draw(backgroundTexture, 0, 0);
        Vector2 pos = shoot.getBallPosition();
        game.batch.draw(ballTexture, pos.x - 16, pos.y - 16, 32, 32);

        float kaleciWidth = 200f;
        float kaleciHeight = 240f;
        float kaleciX = kale.getAlan().x + (kale.getAlan().width - kaleciWidth) / 2f;
        float kaleciY = kale.getAlan().y - 50;

        game.batch.draw(kaleci.getPozisyonTexture(), kaleciX, kaleciY, kaleciWidth, kaleciHeight);

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
        if (shoot.isShooting() || oyunBitti) return;

        shapeRenderer.begin(ShapeRenderer.ShapeType.Filled);

        if (!shoot.isDirectionLocked()) {
            float angle = shoot.getDirectionTimer() * 180f;
            float rad = (float) Math.toRadians(angle);
            float length = 100f;
            float startX = 960, startY = 150;
            float endX = startX + (float) Math.cos(rad) * length;
            float endY = startY + (float) Math.sin(rad) * length;
            shapeRenderer.setColor(Color.YELLOW);
            shapeRenderer.rectLine(startX, startY, endX, endY, 5f);
        }

        if (!shoot.isPowerLocked() && shoot.isDirectionLocked()) {
            shapeRenderer.setColor(Color.BLUE);
            shapeRenderer.rect(50, 50, 20, shoot.getPowerTimer() * 150);
        }

        shapeRenderer.end();
    }

    @Override public void dispose() {
        ballTexture.dispose();
        backgroundTexture.dispose();
        shapeRenderer.dispose();
    }

    @Override public void show() {}
    @Override public void resize(int width, int height) {}
    @Override public void pause() {}
    @Override public void resume() {}
    @Override public void hide() {}
}
