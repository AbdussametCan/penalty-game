package com.penaltygame;

import com.badlogic.gdx.Application;
import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Graphics.DisplayMode;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.penaltygame.Screens.OpeningScreen;
import com.penaltygame.Shoot.FirstScreen;

public class PenaltyGame extends Game {

    public SpriteBatch batch;
    public AssetManager assetManager;

    // Müzikler
    public Music fifaSong;
    public Music wakaSong;
    public Music euroSong;
    public Music nosaSong;

    private int currentSongIndex = 0;
    private Music[] musicQueue;

    @Override
    public void create() {
        batch = new SpriteBatch();
        assetManager = new AssetManager();

        // Asset'leri Yükle
        assetManager.load("InterfacePng/start.png", Texture.class);
        assetManager.load("InterfacePng/soundOn.png", Texture.class);
        assetManager.load("InterfacePng/soundOff.png", Texture.class);
        assetManager.load("InterfacePng/background.png", Texture.class);
        assetManager.load("InterfacePng/back.png", Texture.class);
        assetManager.load("InterfacePng/photo.png", Texture.class);
        assetManager.load("InterfacePng/exit.png", Texture.class);
        assetManager.load("InterfacePng/next.png", Texture.class);
        assetManager.load("bracket.png", Texture.class);
        assetManager.load("InterfacePng/play.png", Texture.class);
        assetManager.load("InterfacePng/roadfinal.png", Texture.class);
        assetManager.load("InterfacePng/Trophy/championstrophy.png", Texture.class);
        assetManager.load("InterfacePng/Trophy/turkishtrophy.png", Texture.class);
        assetManager.load("InterfacePng/Trophy/worldtrophy.png", Texture.class);
        assetManager.load("InterfacePng/Trophy/eurotrophy.png", Texture.class);

        // UI Skin
        assetManager.load("uiskin.json", Skin.class);

        // Müzikleri Yükle
        assetManager.load("GameSong/fifasong.mp3", Music.class);
        assetManager.load("GameSong/wakasong.mp3", Music.class);
        assetManager.load("GameSong/eurosong.mp3", Music.class);
        assetManager.load("GameSong/nosasong.mp3", Music.class);



        //Shoot assetleri
        assetManager.load("Shoot/ball.png", Texture.class);
        assetManager.load("Shoot/bar_1.png", Texture.class);
        assetManager.load("Shoot/bar_2.png", Texture.class);
        assetManager.load("Shoot/bar_3.png", Texture.class);

        //SAHA
        assetManager.load("field_background.png",Texture.class);


        // Asset'lerin yüklenmesini bekle
        assetManager.finishLoading();

        // Müzikleri Al
        fifaSong = assetManager.get("GameSong/fifasong.mp3", Music.class);
        wakaSong = assetManager.get("GameSong/wakasong.mp3", Music.class);
        euroSong = assetManager.get("GameSong/eurosong.mp3", Music.class);
        nosaSong = assetManager.get("GameSong/nosasong.mp3", Music.class);

        // Müzik sırasını oluştur
        musicQueue = new Music[] { fifaSong, wakaSong, euroSong, nosaSong };
        playNextSong(); // Müzik başlat

        // ✅ Tam ekran modu sadece masaüstünde aktif edilsin
        if (Gdx.app.getType() == Application.ApplicationType.Desktop) {
            DisplayMode displayMode = Gdx.graphics.getDisplayMode(Gdx.graphics.getMonitor());
            if (!Gdx.graphics.isFullscreen()) {
                Gdx.graphics.setFullscreenMode(displayMode);
            }
        }

        // İlk ekranı FirstScreen olarak ayarla
        setScreen(new OpeningScreen(this));
    }

    private void playNextSong() {
        if (currentSongIndex < musicQueue.length) {
            Music currentMusic = musicQueue[currentSongIndex];
            currentMusic.setOnCompletionListener(music -> {
                currentSongIndex++;
                if (currentSongIndex >= musicQueue.length) {
                    currentSongIndex = 0;
                }
                playNextSong();
            });
            currentMusic.play();
        }
    }

    public void skipToNextSong() {
        if (currentSongIndex < musicQueue.length) {
            Music currentMusic = musicQueue[currentSongIndex];
            currentMusic.stop();
            currentSongIndex++;
            if (currentSongIndex >= musicQueue.length) {
                currentSongIndex = 0;
            }
            playNextSong();
        }
    }

    public void toggleSound(boolean isOn) {
        if (isOn) {
            Music currentMusic = musicQueue[currentSongIndex];
            if (!currentMusic.isPlaying()) {
                currentMusic.play();
            }
        } else {
            stopAllMusic();
        }
    }

    private void stopAllMusic() {
        for (Music music : musicQueue) {
            if (music != null && music.isPlaying()) {
                music.stop();
            }
        }
    }

    public Music getCurrentMusic() {
        return musicQueue[currentSongIndex];
    }

    public SpriteBatch getBatch() {
        return batch;
    }


    @Override
    public void render() {
        super.render();
    }

    @Override
    public void dispose() {
        super.dispose();
        batch.dispose();
        assetManager.dispose();
    }
}
