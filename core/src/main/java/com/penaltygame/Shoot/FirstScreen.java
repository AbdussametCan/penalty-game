package com.penaltygame.Shoot;

import com.penaltygame.GameScreen;
import com.penaltygame.Screens.ResultScreen;
import com.penaltygame.Screens.BaseScreen;
import com.penaltygame.bot.OyuncuBot;
import com.badlogic.gdx.*;
import com.badlogic.gdx.graphics.*;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Vector2;
import com.penaltygame.PenaltyGame;
import com.penaltygame.Oyun.Kale;
import com.penaltygame.Oyun.Kaleci;
import com.penaltygame.Oyun.SkorBoard;
import com.badlogic.gdx.graphics.g2d.TextureRegion;

public class FirstScreen extends BaseScreen {
    Texture ballTexture, backgroundTexture;
    Vector2 kaleciPozisyon;
    Kale kale;
    Kaleci kaleci;
    SkorBoard skorBoard;
    Shoot shoot;
    OyuncuBot bot = new OyuncuBot();

    Texture arrowTexture;
    TextureRegion arrowRegion;

    boolean kaleciPozisyonKilitli = false;
    BitmapFont font = new BitmapFont();
    ShapeRenderer shapeRenderer = new ShapeRenderer();

    int clickStage = 0;
    boolean oyuncuSirasi = true;
    boolean golOldu = false, kurtardi = false, oyunBitti = false;
    float mesajTimer = 0f;
    float botSutTimer = -1f;

    int atisSayisiOyuncu = 0, atisSayisiBot = 0;
    final int MAX_ATIS = 5;
    boolean kaleciKararVerdi = false;
    boolean seriPenaltilar = false;
    String kazananTakim = "";

    float oyuncuKaleciX = 860;
    final float oyuncuKaleciMinX = 700;
    final float oyuncuKaleciMaxX = 1120;

    private GameScreen returnScreen;
    private String playerTeam;
    private String opponentTeam;

    public FirstScreen(final PenaltyGame game, String playerTeam, String opponentTeam, GameScreen returnScreen) {
        super(game);
        this.playerTeam = playerTeam;
        this.opponentTeam = opponentTeam;
        this.returnScreen = returnScreen;
    }

    @Override
    protected void addContent() {
        backgroundTexture = new Texture("field_background.png");
        ballTexture = new Texture("Shoot/ball.png");
        kale = new Kale(500, 430, 920, 270);
        kaleci = new Kaleci(game.assetManager);
        skorBoard = new SkorBoard(playerTeam, opponentTeam);
        kaleciPozisyon = new Vector2(910, 430);
        shoot = new Shoot();

        arrowTexture = game.assetManager.get("InterfacePng/arrow.png", Texture.class);
        arrowRegion = new TextureRegion(arrowTexture);

        Gdx.input.setInputProcessor(new InputAdapter() {
            @Override
            public boolean touchDown(int x, int y, int pointer, int button) {
                if (shoot.isShooting() || oyunBitti) return false;

                if (oyuncuSirasi) {
                    if (clickStage == 0) {
                        shoot.lockDirection();
                        clickStage++;
                    } else if (clickStage == 1) {
                        shoot.lockPower();
                        clickStage = 0;
                    }
                } else {
                    oyuncuKaleciX = Math.max(oyuncuKaleciMinX, Math.min(x - 100, oyuncuKaleciMaxX));
                    kaleciPozisyonKilitli = true;
                }
                return true;
            }
        });
    }

    private void resetShotState() {
        clickStage = 0;
        kaleciKararVerdi = false;
        shoot.reset();
    }

    private void tamamlaSira() {
        kaleciKararVerdi = false;
        kaleciPozisyonKilitli = false;

        if (oyuncuSirasi) atisSayisiOyuncu++;
        else atisSayisiBot++;

        if (!seriPenaltilar && atisSayisiOyuncu >= MAX_ATIS && atisSayisiBot >= MAX_ATIS) {
            if (skorBoard.getSkorSaldiran() == skorBoard.getSkorSavunan()) {
                seriPenaltilar = true;
            } else {
                oyunBitti = true;
                kazananTakim = skorBoard.getSkorSaldiran() > skorBoard.getSkorSavunan()
                    ? skorBoard.getTakimSaldiran() : skorBoard.getTakimSavunan();
                return;
            }
        }

        if (seriPenaltilar) {
            if (Math.abs(skorBoard.getSkorSaldiran() - skorBoard.getSkorSavunan()) >= 1
                && atisSayisiOyuncu > MAX_ATIS && atisSayisiOyuncu == atisSayisiBot) {
                oyunBitti = true;
                kazananTakim = skorBoard.getSkorSaldiran() > skorBoard.getSkorSavunan()
                    ? skorBoard.getTakimSaldiran() : skorBoard.getTakimSavunan();
                return;
            }
        }

        oyuncuSirasi = !oyuncuSirasi;
        skorBoard.takimlariDegistir();
        clickStage = 0;
        shoot.reset();

        if (!oyuncuSirasi) {
            botSutTimer = 2f;
        }
    }

    @Override
    public void render(float delta) {
        shoot.updateBars(delta);

        if ((golOldu || kurtardi) && (mesajTimer -= delta) <= 0) {
            golOldu = kurtardi = false;
            tamamlaSira();
        }

        if (!oyuncuSirasi && !shoot.isShooting() && !oyunBitti && kaleciPozisyonKilitli) {
            if (botSutTimer > 0) {
                botSutTimer -= delta;
            } else if (botSutTimer != -1f) {
                bot.sutHesapla();
                float angle = bot.sutYonu * 90f + 90f;
                float power = bot.sutGucu;
                float height = bot.yukseklik;

                shoot.baslaBotSutu(angle, power, height);
                botSutTimer = -1f;
            }
        }

        if (shoot.isShooting()) {
            if (!kaleciKararVerdi) {
                kaleci.yeniYonSec(shoot.getTopYonu());
                kaleci.yukseklikAyarla(shoot.getBallPosition().y);
                kaleciKararVerdi = true;
            }

            shoot.updateBall(delta);

            if (oyuncuSirasi) {
                if (shoot.isSaved(kaleci.getSecilenYon())) {
                    kurtardi = true;
                    mesajTimer = 2f;
                    resetShotState();
                } else if (shoot.isGoal(kale)) {
                    golOldu = true;
                    skorBoard.golAtti();
                    mesajTimer = 2f;
                    resetShotState();
                } else if (shoot.isShotComplete(kale)) {
                    resetShotState();
                    tamamlaSira();
                }
            } else {
                float topX = shoot.getBallPosition().x;

                if (kaleciPozisyonKilitli && topX > oyuncuKaleciX && topX < oyuncuKaleciX + 200f) {
                    kurtardi = true;
                    skorBoard.kurtardi();
                    mesajTimer = 2f;
                    resetShotState();
                } else if (shoot.isGoal(kale)) {
                    golOldu = true;
                    skorBoard.golAtti();
                    mesajTimer = 2f;
                    resetShotState();
                } else if (shoot.isShotComplete(kale)) {
                    resetShotState();
                    tamamlaSira();
                }
            }
        }

        game.batch.begin();
        game.batch.draw(backgroundTexture, 0, 0);
        Vector2 pos = shoot.getBallPosition();
        game.batch.draw(ballTexture, pos.x - 16, pos.y - 16, 32, 32);

        float kaleciWidth = 200f;
        float kaleciHeight = 240f;
        float kaleciX = oyuncuSirasi ? kale.getAlan().x + (kale.getAlan().width - kaleciWidth) / 2f : oyuncuKaleciX;
        float kaleciY = kale.getAlan().y - 50;
        game.batch.draw(kaleci.getPozisyonTexture(), kaleciX, kaleciY, kaleciWidth, kaleciHeight);

        font.getData().setScale(4f);
        if (golOldu) font.draw(game.batch, "GOOOOL!", 700, 600);
        else if (kurtardi) font.draw(game.batch, "KURTARDI!", 680, 600);
        else if (oyunBitti) {
            font.draw(game.batch, kazananTakim + " KAZANDI!", 500, 650);

            if (returnScreen != null) {
                boolean playerWon = kazananTakim.equals(playerTeam);

                if (playerWon) {
                    returnScreen.onGameEnd(true, opponentTeam);
                } else {
                    game.setScreen(new ResultScreen(game, false, game.getSelectedLeague()));
                }

                returnScreen = null;
            }
        }

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
        if (shoot.isShooting() || oyunBitti || !oyuncuSirasi) return;

        if (!shoot.isDirectionLocked()) {
            float angle = (shoot.getDirectionTimer() - 0.5f) * 2f * 90f;
            float arrowX = shoot.getBallPosition().x;
            float arrowY = shoot.getBallPosition().y + 16f;

            float width = 60f;
            float height = 100f;

            float originX = width / 2f;
            float originY = 0f;

            game.batch.begin();
            game.batch.draw(
                arrowRegion,
                arrowX - originX, arrowY,
                originX, originY,
                width, height,
                1f, 1f,
                angle
            );
            game.batch.end();
        }

        if (!shoot.isPowerLocked() && shoot.isDirectionLocked()) {
            shapeRenderer.begin(ShapeRenderer.ShapeType.Filled);
            shapeRenderer.setColor(Color.BLUE);
            shapeRenderer.rect(50, 50, 20, shoot.getPowerTimer() * 150);
            shapeRenderer.end();
        }
    }

    @Override public void dispose() {
        ballTexture.dispose();
        backgroundTexture.dispose();
        shapeRenderer.dispose();
    }

    @Override public void resize(int width, int height) {
        super.resize(width, height);
    }

    @Override
    public void onGameEnd(boolean playerWon, String opponentTeam) {
        // Bu ekran oyunu bitiren ekran değil, bu yüzden genelde bir şey yapmana gerek yok
        // Ancak yine de boş bırakmak yerine gerekirse log yazabilirsin
    }

    @Override public void pause() {}
    @Override public void resume() {}
    @Override public void hide() {}
}
