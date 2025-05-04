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
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public class QuarterFinal extends BaseScreen implements GameScreen {

    private final String[] teams;
    private final String selectedTeam;
    private final String imagePath;
    private final String trophyPath;

    public QuarterFinal(PenaltyGame game, String[] teams, String selectedTeam, String imagePath, String trophyPath) {
        super(game);
        this.teams = teams;
        this.selectedTeam = selectedTeam;
        this.imagePath = imagePath;
        this.trophyPath = trophyPath;
    }

    @Override
    protected void addContent() {
        Skin skin = game.assetManager.get("uiskin.json", Skin.class);

        Texture bracketTexture = new Texture(Gdx.files.internal("bracket.png"));
        Image bracketImage = new Image(bracketTexture);

        float scale = 1.5f;
        float scaledWidth = bracketTexture.getWidth() * scale;
        float scaledHeight = bracketTexture.getHeight() * scale;
        float screenWidth = Gdx.graphics.getWidth();
        float screenHeight = Gdx.graphics.getHeight();

        bracketImage.setSize(scaledWidth, scaledHeight);
        bracketImage.setPosition((screenWidth - scaledWidth) / 2f, (screenHeight - scaledHeight) / 2f);
        stage.addActor(bracketImage);

        List<String> ordered = new ArrayList<>(Arrays.asList(teams));

        float[][] positions = {
            {90, 685}, {90, 575}, {90, 461}, {90, 350},
            {screenWidth - 260, 685}, {screenWidth - 260, 575}, {screenWidth - 260, 461}, {screenWidth - 260, 350}
        };

        for (int i = 0; i < 8; i++) {
            String team = ordered.get(i);

            TextButton teamButton;
            if (team.equals(selectedTeam)) {
                TextButton.TextButtonStyle redStyle = new TextButton.TextButtonStyle();
                redStyle.up = skin.getDrawable("default-round-down");
                redStyle.font = skin.getFont("default-font");
                teamButton = new TextButton(team, redStyle);
                teamButton.getLabel().setColor(Color.YELLOW);
            } else {
                teamButton = new TextButton(team, skin);
            }

            teamButton.setSize(170, 45);
            teamButton.setPosition(positions[i][0], positions[i][1]);
            teamButton.getLabel().setFontScale(1f);
            teamButton.getLabel().setAlignment(Align.center);

            stage.addActor(teamButton);
        }

        Texture playTexture = game.assetManager.get("InterfacePng/play.png", Texture.class);
        TextureRegionDrawable playDrawable = new TextureRegionDrawable(new TextureRegion(playTexture));

        ImageButton playButton = new ImageButton(playDrawable);
        playButton.setSize(300, 150);
        playButton.setPosition(screenWidth - 300, 20);

        playButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                List<String> kalanlar = new ArrayList<>(Arrays.asList(teams));
                kalanlar.remove(selectedTeam);
                Collections.shuffle(kalanlar);
                String rakipTeam = kalanlar.get(0);
                game.setScreen(new FirstScreen(game, selectedTeam, rakipTeam, QuarterFinal.this));
            }
        });

        stage.addActor(playButton);

        Texture roadTexture = game.assetManager.get("InterfacePng/roadfinal.png", Texture.class);
        Image roadImage = new Image(roadTexture);
        roadImage.setSize(1000, 200);
        roadImage.setPosition((screenWidth - roadImage.getWidth()) / 2f, screenHeight - roadImage.getHeight() - 75);
        stage.addActor(roadImage);

        Texture trophyTexture = game.assetManager.get(trophyPath, Texture.class);
        Image trophyImage = new Image(trophyTexture);
        trophyImage.setSize(350, 350);
        trophyImage.setPosition((screenWidth - 350) / 2f, 375);
        stage.addActor(trophyImage);

        addBackButton(() -> game.setScreen(new TeamSelectionScreen(game, teams, imagePath, trophyPath)));
    }

    @Override
    public void onGameEnd(boolean playerWon, String opponentTeam) {
        String winner = playerWon ? selectedTeam : opponentTeam;
        List<String> kazananlar = new ArrayList<>();
        kazananlar.add(winner);

        List<String> kalanlar = new ArrayList<>(Arrays.asList(teams));
        kalanlar.remove(selectedTeam);
        kalanlar.remove(opponentTeam);

        Collections.shuffle(kalanlar);

        for (int i = 0; i < 6; i += 2) {
            String teamA = kalanlar.get(i);
            String teamB = kalanlar.get(i + 1);
            kazananlar.add(Math.random() < 0.5 ? teamA : teamB);
        }

        game.setScreen(new SemiFinal(game, Arrays.asList(teams), kazananlar, winner, imagePath, trophyPath));
    }
}
