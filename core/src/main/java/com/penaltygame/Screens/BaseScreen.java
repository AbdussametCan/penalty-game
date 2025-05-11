package com.penaltygame.Screens;

import com.penaltygame.GameScreen;
import com.penaltygame.PenaltyGame;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.ImageButton;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import com.badlogic.gdx.utils.viewport.ScreenViewport;
import com.badlogic.gdx.audio.Music;

public abstract class BaseScreen implements GameScreen {

    protected final PenaltyGame game;
    protected Stage stage;
    private boolean sesAc = true;
    private ImageButton sesButonu;
    private ImageButton sonraki;
    private ImageButton geri;


    //Buton ölçüleri ve fonksiyonlarının bulunduğu sınıf.
    public BaseScreen(PenaltyGame game) {
        this.game = game;
    }

    @Override
    public void show() {
        stage = new Stage(new ScreenViewport());
        Gdx.input.setInputProcessor(stage);

        Texture bgTexture = game.assetManager.get("InterfacePng/background.png", Texture.class);
        Image background = new Image(bgTexture);
        background.setFillParent(true);
        stage.addActor(background);

        addsesButonu();
        addsonraki();
        addExitButton();
        addContent();
    }

    protected abstract void addContent();

    //Geri butonu.
    protected void addgeri(Runnable onClickAction) {
        Texture backTexture = game.assetManager.get("InterfacePng/back.png", Texture.class);
        geri = new ImageButton(new TextureRegionDrawable(new TextureRegion(backTexture)));

        float buttonSize = 200f;
        geri.setSize(buttonSize, buttonSize);
        geri.setPosition((Gdx.graphics.getWidth() - buttonSize) / 2f, 40);

        geri.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                if (onClickAction != null) {
                    onClickAction.run();
                }
            }
        });

        stage.addActor(geri);
    }

    //Ses butonları.
    private void addsesButonu() {
        Texture soundOnTexture = game.assetManager.get("InterfacePng/soundOn.png", Texture.class);
        Texture soundOffTexture = game.assetManager.get("InterfacePng/soundOff.png", Texture.class);

        float buttonSize = 100f;

        TextureRegionDrawable soundDrawable = new TextureRegionDrawable(new TextureRegion(soundOnTexture));
        soundDrawable.setMinSize(buttonSize, buttonSize);

        sesButonu = new ImageButton(soundDrawable);
        sesButonu.setSize(buttonSize, buttonSize);
        sesButonu.setPosition(Gdx.graphics.getWidth() - buttonSize - 20, Gdx.graphics.getHeight() - buttonSize - 20);

        sesButonu.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                sesAc = !sesAc;
                Texture newTexture = sesAc ? soundOnTexture : soundOffTexture;
                TextureRegionDrawable newDrawable = new TextureRegionDrawable(new TextureRegion(newTexture));
                newDrawable.setMinSize(buttonSize, buttonSize);
                sesButonu.getStyle().imageUp = newDrawable;

                toggleVolume(sesAc);
            }
        });

        stage.addActor(sesButonu);
    }


    //Sesi açıp kapatma.
    private void toggleVolume(boolean isOn) {
        Music currentMusic = game.getCurrentMusic();
        if (currentMusic != null) {
            currentMusic.setVolume(isOn ? 1.0f : 0.0f);
            if (!currentMusic.isPlaying()) {
                currentMusic.play();
            }
        }
    }

    //Çıkış butonu.
    private void addExitButton() {
        Texture exitTexture = game.assetManager.get("InterfacePng/exit.png", Texture.class);
        float buttonSize = 120f;

        TextureRegionDrawable exitDrawable = new TextureRegionDrawable(new TextureRegion(exitTexture));
        exitDrawable.setMinSize(buttonSize, buttonSize);

        ImageButton exitButton = new ImageButton(exitDrawable);
        exitButton.setSize(buttonSize, buttonSize);
        exitButton.setPosition(20, Gdx.graphics.getHeight() - buttonSize - 20);

        exitButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                Gdx.app.exit();
            }
        });

        stage.addActor(exitButton);
    }

    //Sonraki şarkıya geçiş.
    private void addsonraki() {
        Texture nextTexture = game.assetManager.get("InterfacePng/next.png", Texture.class);
        float buttonSize = 115f;

        TextureRegionDrawable nextDrawable = new TextureRegionDrawable(new TextureRegion(nextTexture));
        nextDrawable.setMinSize(buttonSize, buttonSize);

        sonraki = new ImageButton(nextDrawable);
        sonraki.setSize(buttonSize, buttonSize);
        sonraki.setPosition(Gdx.graphics.getWidth() - (buttonSize * 2) - 40, Gdx.graphics.getHeight() - buttonSize - 20);

        sonraki.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                game.skipToNextSong();
            }
        });

        stage.addActor(sonraki);
    }

    @Override
    public void render(float delta) {
        stage.act(delta);
        stage.draw();
    }

    @Override public void resize(int width, int height) {
        stage.getViewport().update(width, height, true);
    }

    @Override public void pause() {}
    @Override public void resume() {}
    @Override public void hide() {}

    @Override
    public void dispose() {
        stage.dispose();
    }
}
