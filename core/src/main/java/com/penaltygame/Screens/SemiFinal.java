package com.penaltygame.Screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.ui.*;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import com.badlogic.gdx.utils.Align;
import com.penaltygame.PenaltyGame;
import com.penaltygame.Shoot.FirstScreen;
import com.penaltygame.GameScreen;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class SemiFinal extends BaseScreen implements GameScreen {

    private final List<String> tumTakimlar; // 8 takim
    private final List<String> kazananlar;  // 4 takim (QuarterFinal'dan gelen)
    private final String playerTeam;
    private final String imagePath;
    private final String trophyPath;

    public SemiFinal(PenaltyGame game, List<String> tumTakimlar, List<String> kazananlar, String playerTeam, String imagePath, String trophyPath) {
        super(game);
        this.tumTakimlar = tumTakimlar;
        this.kazananlar = kazananlar;
        this.playerTeam = playerTeam;
        this.imagePath = imagePath;
        this.trophyPath = trophyPath;
    }

    @Override
    protected void addContent() {
        Skin skin = game.assetManager.get("uiskin.json", Skin.class);
        float screenWidth = Gdx.graphics.getWidth();
        float screenHeight = Gdx.graphics.getHeight();

        Texture bracketTexture = new Texture(Gdx.files.internal("bracket.png"));
        Image bracketImage = new Image(bracketTexture);
        bracketImage.setSize(bracketTexture.getWidth() * 1.5f, bracketTexture.getHeight() * 1.5f);
        bracketImage.setPosition((screenWidth - bracketImage.getWidth()) / 2f, (screenHeight - bracketImage.getHeight()) / 2f);
        stage.addActor(bracketImage);

        // QuarterFinal takimlari (soldan saga 8 takim)
        float[][] quarterPositions = {
            {90, 685}, {90, 575}, {90, 461}, {90, 350},
            {screenWidth - 255, 685}, {screenWidth - 255, 575}, {screenWidth - 255, 461}, {screenWidth - 255, 350}
        };

        for (int i = 0; i < tumTakimlar.size(); i++) {
            String team = tumTakimlar.get(i);
            TextButton button = new TextButton(team, skin);
            if (team.equals(playerTeam)) button.getLabel().setColor(Color.YELLOW);
            button.setSize(170, 45);
            button.setPosition(quarterPositions[i][0], quarterPositions[i][1]);
            button.getLabel().setFontScale(1f);
            button.getLabel().setAlignment(Align.center);
            stage.addActor(button);
        }

        // SemiFinale kalan takimlar (4 kazanan - 2'li eslesmelerin galibi)
        float[][] semiPositions = {
            {375, 630}, {375, 406},
            {screenWidth - 540, 630}, {screenWidth - 540, 406}
        };

        for (int i = 0; i < kazananlar.size() && i < semiPositions.length; i++) {
            String kazanan = kazananlar.get(i);

            TextButton btn;
            if (kazanan.equals(playerTeam)) {
                TextButton.TextButtonStyle redStyle = new TextButton.TextButtonStyle();
                redStyle.up = skin.getDrawable("default-round-down");
                redStyle.font = skin.getFont("default-font");
                btn = new TextButton(kazanan, redStyle);
                btn.getLabel().setColor(Color.YELLOW);
            } else {
                btn = new TextButton(kazanan, skin);
            }

            btn.setSize(170, 45);
            btn.setPosition(semiPositions[i][0], semiPositions[i][1]);
            btn.getLabel().setFontScale(1f);
            btn.getLabel().setAlignment(Align.center);
            stage.addActor(btn);
        }


        // Play butonu
        Texture playTexture = game.assetManager.get("InterfacePng/play.png", Texture.class);
        ImageButton playButton = new ImageButton(new TextureRegionDrawable(new TextureRegion(playTexture)));
        playButton.setSize(300, 150);
        playButton.setPosition(screenWidth - 300, 20);

        playButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                List<String> rakipler = new ArrayList<>(kazananlar);
                rakipler.remove(playerTeam);
                Collections.shuffle(rakipler);
                String rakip = rakipler.get(0);
                game.setScreen(new FirstScreen(game, playerTeam, rakip, SemiFinal.this));
            }
        });
        stage.addActor(playButton);

        // Trophy
        Texture trophyTexture = game.assetManager.get(trophyPath, Texture.class);
        Image trophyImage = new Image(trophyTexture);
        trophyImage.setSize(350, 350);
        trophyImage.setPosition((screenWidth - 350) / 2f, 375);
        stage.addActor(trophyImage);

        // Geri donus butonu
        addBackButton(() -> game.setScreen(new TeamSelectionScreen(game, tumTakimlar.toArray(new String[0]), imagePath, trophyPath)));
    }


    @Override
    public void onGameEnd(boolean playerWon, String opponentTeam) {
        List<String> finalTakimlar = new ArrayList<>(kazananlar);
        finalTakimlar.remove(playerWon ? opponentTeam : playerTeam);
        String ilerleyen = playerWon ? playerTeam : opponentTeam;
        finalTakimlar.add(ilerleyen);

        game.setScreen(new FinalScreen(game, tumTakimlar, finalTakimlar, ilerleyen, playerTeam, imagePath, trophyPath));
    }
}
