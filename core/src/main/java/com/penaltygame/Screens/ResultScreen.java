package com.penaltygame.Screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.ImageButton;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import com.penaltygame.PenaltyGame;

public class ResultScreen extends BaseScreen {

    private final boolean playerWon;
    private final String leagueName;
    private Texture resultTexture;
    private Texture trophyTexture;

    public ResultScreen(PenaltyGame game, boolean playerWon, String leagueName) {
        super(game);
        this.playerWon = playerWon;
        this.leagueName = leagueName;
    }

    @Override
    protected void addContent() {
        if (!playerWon) {
            // Lose ekranı
            resultTexture = new Texture(Gdx.files.internal("InterfacePng/Result/lose.png"));
            Image resultImage = new Image(resultTexture);
            resultImage.setSize(Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
            stage.addActor(resultImage);

            // Try Again tıklama alanı (örnek koordinatlar ve boyut, ihtiyaca göre değiştir)
            ImageButton invisibleButton = new ImageButton(new TextureRegionDrawable(new TextureRegion(new Texture(Gdx.files.internal("InterfacePng/Result/seffaf.png")))));
            invisibleButton.setSize(300, 100);
            invisibleButton.setPosition((Gdx.graphics.getWidth() - 300) / 2f, 150);
            invisibleButton.getColor().a = 0f; // Görünmez yap

            invisibleButton.addListener(new ClickListener() {
                @Override
                public void clicked(InputEvent event, float x, float y) {
                    game.setScreen(new OpeningScreen(game));
                }
            });
            stage.addActor(invisibleButton);
        }
        else {
            // Win ekranı arka plan
            resultTexture = new Texture(Gdx.files.internal("InterfacePng/Result/win.png"));
            Image winBg = new Image(resultTexture);
            winBg.setSize(Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
            stage.addActor(winBg);

            // Kupa görseli
            String cupPath;
            switch (leagueName) {
                case "Champions League":
                    cupPath = "InterfacePng/Trophy/championstrophy.png";
                    break;
                case "Turkish League":
                    cupPath = "InterfacePng/Trophy/turkishtrophy.png";
                    break;
                case "World Cup":
                    cupPath = "InterfacePng/Trophy/worldtrophy.png";
                    break;
                case "Europa Cup":
                    cupPath = "InterfacePng/Trophy/eurotrophy.png";
                    break;
                default:
                    cupPath = "InterfacePng/default_win.png";
            }

            trophyTexture = new Texture(Gdx.files.internal(cupPath));
            Image trophyImage = new Image(trophyTexture);
            trophyImage.setSize(500, 500);
            trophyImage.setPosition((Gdx.graphics.getWidth() - 500) / 2f, 310);
            stage.addActor(trophyImage);

            // YOU WIN yazısı için görünmez tıklama alanı
            Texture transparent = new Texture(Gdx.files.internal("InterfacePng/Result/seffaf.png"));
            ImageButton winButton = new ImageButton(new TextureRegionDrawable(new TextureRegion(transparent)));
            winButton.setSize(300, 100);
            winButton.setPosition((Gdx.graphics.getWidth() - 300) / 2f, 130); // YOU WIN yazısına hizalanacak
            winButton.getColor().a = 0f;

            winButton.addListener(new ClickListener() {
                @Override
                public void clicked(InputEvent event, float x, float y) {
                    game.setScreen(new OpeningScreen(game));
                }
            });

            stage.addActor(winButton);
        }

    }

    @Override
    public void dispose() {
        if (resultTexture != null) resultTexture.dispose();
        if (trophyTexture != null) trophyTexture.dispose();
        super.dispose();
    }

    @Override
    public void onGameEnd(boolean playerWon, String opponentTeam) {
        // Bu ekran oyunun sonunda tekrar çağrılmıyor
    }
}
