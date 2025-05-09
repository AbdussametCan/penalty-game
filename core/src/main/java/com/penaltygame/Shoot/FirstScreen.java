package com.penaltygame.Shoot;

import com.penaltygame.GameScreen;
import com.penaltygame.Screens.ResultScreen;
import com.penaltygame.bot.OyuncuBot;
import com.penaltygame.Oyun.Kale;
import com.penaltygame.Oyun.Kaleci;
import com.penaltygame.Oyun.SkorBoard;
import com.penaltygame.PenaltyGame;
import com.badlogic.gdx.*;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.graphics.*;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.*;
import com.badlogic.gdx.scenes.scene2d.ui.ImageButton;
import com.badlogic.gdx.scenes.scene2d.utils.*;
import com.badlogic.gdx.utils.viewport.ScreenViewport;

public class FirstScreen implements Screen {

    final PenaltyGame game;
    Texture ballTexture, backgroundTexture;
    Texture arrowTexture;
    TextureRegion arrowRegion;
    Vector2 kaleciPozisyon;
    Kale kale;
    Kaleci kaleci;
    SkorBoard skorBoard;
    Shoot shoot;
    OyuncuBot bot = new OyuncuBot();

    BitmapFont font = new BitmapFont();
    ShapeRenderer shapeRenderer = new ShapeRenderer();

    boolean kaleciPozisyonKilitli = false;
    boolean oyuncuSirasi = true;
    boolean golOldu = false, kurtardi = false, oyunBitti = false;
    boolean kaleciKararVerdi = false;
    boolean seriPenaltilar = false;

    int clickStage = 0;
    int atisSayisiOyuncu = 0, atisSayisiBot = 0;
    final int MAX_ATIS = 5;

    float mesajTimer = 0f;
    float botSutTimer = -1f;
    String kazananTakim = "";

    float oyuncuKaleciX = 860;
    final float oyuncuKaleciMinX = 700;
    final float oyuncuKaleciMaxX = 1120;

    private GameScreen returnScreen;
    private String playerTeam;
    private String opponentTeam;

    private Stage buttonStage;

    public FirstScreen(final PenaltyGame game, String playerTeam, String opponentTeam, GameScreen returnScreen) {
        this.game = game;
        this.playerTeam = playerTeam;
        this.opponentTeam = opponentTeam;
        this.returnScreen = returnScreen;

        backgroundTexture = new Texture("field_background.png");
        ballTexture = new Texture("Shoot/ball.png");
        arrowTexture = game.assetManager.get("InterfacePng/arrow.png", Texture.class);
        arrowRegion = new TextureRegion(arrowTexture);

        kale = new Kale(500, 430, 920, 270);
        kaleci = new Kaleci(game.assetManager);
        skorBoard = new SkorBoard(playerTeam, opponentTeam);
        kaleciPozisyon = new Vector2(910, 430);
        shoot = new Shoot();
    }

    @Override
    public void show() {
        buttonStage = new Stage(new ScreenViewport());
        addUIButtons();
        Gdx.input.setInputProcessor(new InputMultiplexer(buttonStage, new InputAdapter() {
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
        }));
    }

    private void addUIButtons() {
        float buttonSize = 110f;
        float padding = 20f;

        Texture soundOn = game.assetManager.get("InterfacePng/soundOn.png", Texture.class);
        Texture soundOff = game.assetManager.get("InterfacePng/soundOff.png", Texture.class);
        final boolean[] soundState = {true};

        ImageButton soundBtn = new ImageButton(new TextureRegionDrawable(new TextureRegion(soundOn)));
        soundBtn.setSize(buttonSize, buttonSize);
        soundBtn.setPosition(Gdx.graphics.getWidth() - buttonSize - padding, Gdx.graphics.getHeight() - buttonSize - padding);
        soundBtn.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                soundState[0] = !soundState[0];
                Music music = game.getCurrentMusic();
                if (music != null) music.setVolume(soundState[0] ? 1f : 0f);
                soundBtn.getStyle().imageUp = new TextureRegionDrawable(new TextureRegion(soundState[0] ? soundOn : soundOff));
            }
        });

        Texture exitTex = game.assetManager.get("InterfacePng/exit.png", Texture.class);
        ImageButton exitBtn = new ImageButton(new TextureRegionDrawable(new TextureRegion(exitTex)));
        exitBtn.setSize(buttonSize, buttonSize);
        exitBtn.setPosition(padding, Gdx.graphics.getHeight() - buttonSize - padding);
        exitBtn.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                Gdx.app.exit();
            }
        });

        Texture nextTex = game.assetManager.get("InterfacePng/next.png", Texture.class);
        ImageButton nextBtn = new ImageButton(new TextureRegionDrawable(new TextureRegion(nextTex)));
        nextBtn.setSize(buttonSize, buttonSize);
        nextBtn.setPosition(Gdx.graphics.getWidth() - 2 * (buttonSize + padding), Gdx.graphics.getHeight() - buttonSize - padding);
        nextBtn.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                game.skipToNextSong();
            }
        });

        buttonStage.addActor(soundBtn);
        buttonStage.addActor(exitBtn);
        buttonStage.addActor(nextBtn);
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
            if (skorBoard.getPlayerScore() == skorBoard.getBotScore()) {
                seriPenaltilar = true;
            } else {
                oyunBitti = true;
                kazananTakim = skorBoard.getPlayerScore() > skorBoard.getBotScore() ? playerTeam : opponentTeam;
                return;
            }
        }

        if (seriPenaltilar && Math.abs(skorBoard.getPlayerScore() - skorBoard.getBotScore()) >= 1 && atisSayisiOyuncu > MAX_ATIS && atisSayisiOyuncu == atisSayisiBot) {
            oyunBitti = true;
            kazananTakim = skorBoard.getPlayerScore() > skorBoard.getBotScore() ? playerTeam : opponentTeam;
            return;
        }

        oyuncuSirasi = !oyuncuSirasi;
        clickStage = 0;
        shoot.reset();

        if (!oyuncuSirasi) botSutTimer = 2f;
    }

    @Override
    public void render(float delta) {
        shoot.updateBars(delta);

        if ((golOldu || kurtardi) && (mesajTimer -= delta) <= 0) {
            golOldu = kurtardi = false;
            tamamlaSira();
        }

        if (!oyuncuSirasi && !shoot.isShooting() && !oyunBitti && kaleciPozisyonKilitli) {
            if (botSutTimer > 0) botSutTimer -= delta;
            else if (botSutTimer != -1f) {
                bot.sutHesapla();
                shoot.baslaBotSutu(bot.sutYonu * 90f + 90f, bot.sutGucu, bot.yukseklik);
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
                    kurtardi = true; mesajTimer = 2f; resetShotState();
                } else if (shoot.isGoal(kale)) {
                    golOldu = true; skorBoard.golAtti(true); mesajTimer = 2f; resetShotState();
                } else if (shoot.isShotComplete(kale)) {
                    resetShotState(); tamamlaSira();
                }
            } else {
                float topX = shoot.getBallPosition().x;
                if (kaleciPozisyonKilitli && topX > oyuncuKaleciX && topX < oyuncuKaleciX + 200f) {
                    kurtardi = true; mesajTimer = 2f; resetShotState();
                } else if (shoot.isGoal(kale)) {
                    golOldu = true; skorBoard.golAtti(false); mesajTimer = 2f; resetShotState();
                } else if (shoot.isShotComplete(kale)) {
                    resetShotState(); tamamlaSira();
                }
            }
        }

        game.batch.begin();
        game.batch.draw(backgroundTexture, 0, 0);
        Vector2 pos = shoot.getBallPosition();
        game.batch.draw(ballTexture, pos.x - 16, pos.y - 16, 32, 32);

        float kaleciX = oyuncuSirasi ? kale.getAlan().x + (kale.getAlan().width - 200) / 2f : oyuncuKaleciX;
        float kaleciY = kale.getAlan().y - 50;
        game.batch.draw(kaleci.getPozisyonTexture(), kaleciX, kaleciY, 200, 240);

        font.getData().setScale(4f);
        if (golOldu) font.draw(game.batch, "GOOOOL!", 700, 600);
        else if (kurtardi) font.draw(game.batch, "KURTARDI!", 680, 600);
        else if (oyunBitti) {
            font.draw(game.batch, kazananTakim + " KAZANDI!", 500, 650);
            if (returnScreen != null) {
                boolean playerWon = kazananTakim.equals(playerTeam);
                if (playerWon) returnScreen.onGameEnd(true, opponentTeam);
                else game.setScreen(new ResultScreen(game, false, game.getSelectedLeague()));
                returnScreen = null;
            }
        }

        font.getData().setScale(1.5f);
        font.setColor(Color.WHITE);
        font.draw(game.batch, playerTeam + ": " + skorBoard.getPlayerScore(), Gdx.graphics.getWidth() - 400, 100);
        font.draw(game.batch, opponentTeam + ": " + skorBoard.getBotScore(), Gdx.graphics.getWidth() - 400, 70);
        game.batch.end();

        drawIndicators();

        buttonStage.act(delta);
        buttonStage.draw();
    }

    private void drawIndicators() {
        if (shoot.isShooting() || oyunBitti || !oyuncuSirasi) return;
        if (!shoot.isDirectionLocked()) {
            float angle = (shoot.getDirectionTimer() - 0.5f) * 2f * 90f;
            float arrowX = shoot.getBallPosition().x;
            float arrowY = shoot.getBallPosition().y + 16f;

            game.batch.begin();
            game.batch.draw(arrowRegion, arrowX - 30, arrowY, 30, 0, 60, 100, 1f, 1f, angle);
            game.batch.end();
        }

        if (!shoot.isPowerLocked() && shoot.isDirectionLocked()) {
            shapeRenderer.begin(ShapeRenderer.ShapeType.Filled);
            shapeRenderer.setColor(Color.BLUE);
            shapeRenderer.rect(50, 50, 20, shoot.getPowerTimer() * 150);
            shapeRenderer.end();
        }
    }

    @Override public void resize(int width, int height) {
        if (buttonStage != null)
            buttonStage.getViewport().update(width, height, true);
    }

    @Override public void pause() {}
    @Override public void resume() {}
    @Override public void hide() {}
    @Override public void dispose() {
        ballTexture.dispose();
        backgroundTexture.dispose();
        shapeRenderer.dispose();
        if (buttonStage != null) buttonStage.dispose();
    }

    public void onGameEnd(boolean playerWon, String opponentTeam) {}
}
