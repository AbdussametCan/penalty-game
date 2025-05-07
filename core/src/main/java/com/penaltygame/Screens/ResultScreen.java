package com.penaltygame.Screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.penaltygame.PenaltyGame;

public  class ResultScreen extends BaseScreen {

    private final boolean playerWon;
    private final String leagueName;
    private Texture resultTexture;

    public ResultScreen(PenaltyGame game, boolean playerWon, String leagueName) {
        super(game);
        this.playerWon = playerWon;
        this.leagueName = leagueName;
    }

    @Override
    protected void addContent() {
        if (!playerWon) {
            // Kaybedince sadece lose.png göster
            resultTexture = new Texture(Gdx.files.internal("InterfacePng/Result/lose.png"));
            Image resultImage = new Image(resultTexture);
            resultImage.setSize(Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
            stage.addActor(resultImage);
        } else {
            // 1. win.png arka plan
            Texture winBgTexture = new Texture(Gdx.files.internal("InterfacePng/Result/win.png"));
            Image winBg = new Image(winBgTexture);
            winBg.setSize(Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
            stage.addActor(winBg);

            // 2. kupa PNG üst katman
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

            resultTexture = new Texture(Gdx.files.internal(cupPath));
            Image trophyImage = new Image(resultTexture);
            trophyImage.setSize(500, 500); // Boyutu ayarla
            trophyImage.setPosition((Gdx.graphics.getWidth() - 400) / 2f, 350); // Ortala
            stage.addActor(trophyImage);
        }
    }


    @Override
    public void dispose() {
        if (resultTexture != null) resultTexture.dispose();
        super.dispose();
    }
    @Override
    public void onGameEnd(boolean playerWon, String opponentTeam) {
        // ResultScreen oyunun bitişine tepki vermeyecek, boş bırakıyoruz
    }
}
