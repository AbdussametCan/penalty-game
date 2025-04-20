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
    private boolean soundOn = true;
    private ImageButton soundButton;
    private ImageButton nextButton;
    private ImageButton backButton;

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

        addSoundButton();
        addNextButton();
        addExitButton();
        addContent(); // içerik
    }

    protected abstract void addContent();

    protected void addBackButton(Runnable onClickAction) {
        Texture backTexture = game.assetManager.get("InterfacePng/back.png", Texture.class);
        backButton = new ImageButton(new TextureRegionDrawable(new TextureRegion(backTexture)));

        float buttonSize = 200f;
        backButton.setSize(buttonSize, buttonSize);
        backButton.setPosition((Gdx.graphics.getWidth() - buttonSize) / 2f, 40);

        backButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                if (onClickAction != null) {
                    onClickAction.run();
                }
            }
        });

        stage.addActor(backButton);
    }

    private void addSoundButton() {
        Texture soundOnTexture = game.assetManager.get("InterfacePng/soundOn.png", Texture.class);
        Texture soundOffTexture = game.assetManager.get("InterfacePng/soundOff.png", Texture.class);

        float buttonSize = 100f;

        TextureRegionDrawable soundDrawable = new TextureRegionDrawable(new TextureRegion(soundOnTexture));
        soundDrawable.setMinSize(buttonSize, buttonSize);

        soundButton = new ImageButton(soundDrawable);
        soundButton.setSize(buttonSize, buttonSize);
        soundButton.setPosition(Gdx.graphics.getWidth() - buttonSize - 20, Gdx.graphics.getHeight() - buttonSize - 20);

        soundButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                soundOn = !soundOn;
                Texture newTexture = soundOn ? soundOnTexture : soundOffTexture;
                TextureRegionDrawable newDrawable = new TextureRegionDrawable(new TextureRegion(newTexture));
                newDrawable.setMinSize(buttonSize, buttonSize);
                soundButton.getStyle().imageUp = newDrawable;

                toggleVolume(soundOn);
            }
        });

        stage.addActor(soundButton);
    }

    private void toggleVolume(boolean isOn) {
        Music currentMusic = game.getCurrentMusic();
        if (currentMusic != null) {
            currentMusic.setVolume(isOn ? 1.0f : 0.0f); // ✅ sesi sıfırla veya geri getir
            if (!currentMusic.isPlaying()) {
                currentMusic.play(); // çalmıyorsa başlat
            }
        }
    }

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

    private void addNextButton() {
        Texture nextTexture = game.assetManager.get("InterfacePng/next.png", Texture.class);
        float buttonSize = 115f;

        TextureRegionDrawable nextDrawable = new TextureRegionDrawable(new TextureRegion(nextTexture));
        nextDrawable.setMinSize(buttonSize, buttonSize);

        nextButton = new ImageButton(nextDrawable);
        nextButton.setSize(buttonSize, buttonSize);
        nextButton.setPosition(Gdx.graphics.getWidth() - (buttonSize * 2) - 40, Gdx.graphics.getHeight() - buttonSize - 20);

        nextButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                game.skipToNextSong();
            }
        });

        stage.addActor(nextButton);
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
