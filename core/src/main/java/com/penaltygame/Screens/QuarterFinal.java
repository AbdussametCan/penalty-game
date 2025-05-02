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

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public class QuarterFinal extends BaseScreen {

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
        float originalWidth = bracketTexture.getWidth();
        float originalHeight = bracketTexture.getHeight();
        float scaledWidth = originalWidth * scale;
        float scaledHeight = originalHeight * scale;

        bracketImage.setSize(scaledWidth, scaledHeight);

        float screenWidth = Gdx.graphics.getWidth();
        float screenHeight = Gdx.graphics.getHeight();
        bracketImage.setPosition((screenWidth - scaledWidth) / 2f, (screenHeight - scaledHeight) / 2f);
        stage.addActor(bracketImage);

        // Takımları sabit sırada tut
        List<String> ordered = new ArrayList<>(Arrays.asList(teams));

        float[][] positions = {
            {90, 685},
            {90, 575},
            {90, 461},
            {90, 350},
            {screenWidth - 260, 685},
            {screenWidth - 260, 575},
            {screenWidth - 260, 461},
            {screenWidth - 260, 350}
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
                game.setScreen(new FirstScreen(game, selectedTeam, rakipTeam));
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
        float trophyWidth = 350;
        float trophyHeight = 350;
        trophyImage.setSize(trophyWidth, trophyHeight);
        float centerX = (screenWidth - trophyWidth) / 2f;
        float centerY = 375;
        trophyImage.setPosition(centerX, centerY);
        stage.addActor(trophyImage);

        addBackButton(() -> game.setScreen(new TeamSelectionScreen(game, teams, imagePath, trophyPath)));
    }
}
