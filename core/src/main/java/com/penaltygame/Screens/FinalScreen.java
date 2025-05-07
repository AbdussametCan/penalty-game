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
import com.penaltygame.GameScreen;
import com.penaltygame.Shoot.FirstScreen;

import java.util.*;
import java.util.List;

public class FinalScreen extends BaseScreen implements GameScreen {

    private final List<String> semiFinalWinners;  // SemiFinal'dan gelen kazananlar
    private final String playerTeam;
    private final String opponentTeam;
    private final String imagePath;
    private final String trophyPath;
    private final List<String> quarterFinalTeams;
    private final String leagueName;

    private String finalWinner = null;

    public FinalScreen(PenaltyGame game, List<String> quarterFinalTeams, List<String> semiFinalWinners, String playerTeam, String opponentTeam, String imagePath, String trophyPath, String leagueName) {
        super(game);
        this.quarterFinalTeams = quarterFinalTeams;
        this.semiFinalWinners = semiFinalWinners;
        this.playerTeam = playerTeam;
        this.opponentTeam = opponentTeam;
        this.imagePath = imagePath;
        this.trophyPath = trophyPath;
        this.leagueName = leagueName;

    }


    @Override
    protected void addContent() {
        Skin skin = game.assetManager.get("uiskin.json", Skin.class);
        float screenWidth = Gdx.graphics.getWidth();
        float screenHeight = Gdx.graphics.getHeight();

        // Bracket görseli (Çeyrek final, Yarı final, Final için)
        Texture bracketTexture = new Texture(Gdx.files.internal("bracket.png"));
        Image bracketImage = new Image(bracketTexture);
        float scale = 1.5f;
        bracketImage.setSize(bracketTexture.getWidth() * scale, bracketTexture.getHeight() * scale);
        bracketImage.setPosition((screenWidth - bracketImage.getWidth()) / 2f, (screenHeight - bracketImage.getHeight()) / 2f);
        stage.addActor(bracketImage);

        // Çeyrek final takımlarını ekle
        float[][] quarterPositions = {
            {90, 685}, {90, 575}, {90, 461}, {90, 350},
            {screenWidth - 255, 685}, {screenWidth - 255, 575}, {screenWidth - 255, 461}, {screenWidth - 255, 350}
        };

        for (int i = 0; i < quarterFinalTeams.size(); i++) {
            String team = quarterFinalTeams.get(i);
            TextButton btn = createTeamButton(team, skin);
            btn.setPosition(quarterPositions[i][0], quarterPositions[i][1]);
            stage.addActor(btn);
        }

        // Yarı final takımlarını ekle
        float[][] semiPositions = {
            {375, 630}, {375, 406},
            {screenWidth - 540, 630}, {screenWidth - 540, 406}
        };

        for (int i = 0; i < semiFinalWinners.size() && i < semiPositions.length; i++) {
            String team = semiFinalWinners.get(i);
            TextButton btn = createTeamButton(team, skin);
            btn.setPosition(semiPositions[i][0], semiPositions[i][1]);
            stage.addActor(btn);
        }

        // Finalistleri ekle (Kazananlar)
        float[][] finalPositions = {
            {665, 517}, {screenWidth - 827, 517}
        };

        List<String> finalistler = new ArrayList<>();
        if (semiFinalWinners.contains(playerTeam)) finalistler.add(playerTeam);
        if (semiFinalWinners.contains(opponentTeam)) finalistler.add(opponentTeam);

        for (int i = 0; i < finalistler.size(); i++) {
            String team = finalistler.get(i);
            TextButton btn = createTeamButton(team, skin);
            btn.setPosition(finalPositions[i][0], finalPositions[i][1]);
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
                game.setScreen(new FirstScreen(game, playerTeam, opponentTeam, FinalScreen.this));
            }
        });
        stage.addActor(playButton);

        // Şampiyonluk kupası
        Texture trophyTexture = game.assetManager.get(trophyPath, Texture.class);
        Image trophyImage = new Image(trophyTexture);
        trophyImage.setSize(350, 350);
        trophyImage.setPosition((screenWidth - 350) / 2f, 375);
        stage.addActor(trophyImage);

        Image roadImage = new Image(game.assetManager.get("InterfacePng/roadfinal.png", Texture.class));
        roadImage.setSize(1000, 200);
        roadImage.setPosition((screenWidth - roadImage.getWidth()) / 2f, screenHeight - roadImage.getHeight() - 75);
        stage.addActor(roadImage);
    }


    // Takım butonları için
    private TextButton createTeamButton(String team, Skin skin) {
        TextButton btn;
        if (team.equals(playerTeam)) {
            TextButton.TextButtonStyle redStyle = new TextButton.TextButtonStyle();
            redStyle.up = skin.getDrawable("default-round-down");
            redStyle.font = skin.getFont("default-font");
            btn = new TextButton(team, redStyle);
            btn.getLabel().setColor(Color.YELLOW);
        } else {
            btn = new TextButton(team, skin);
        }
        btn.setSize(170, 45);
        btn.getLabel().setFontScale(1f);
        btn.getLabel().setAlignment(Align.center);
        return btn;
    }

    // Finalde kazananı belirleyip gösterme
    @Override
    public void onGameEnd(boolean playerWon, String opponentTeam) {
        game.setScreen(new ResultScreen(game, playerWon, leagueName));
    }
}
