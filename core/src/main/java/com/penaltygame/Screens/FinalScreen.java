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

import java.util.List;

public class FinalScreen extends BaseScreen implements GameScreen {

    private final List<String> quarterTeams;
    private final List<String> semiWinners;
    private final String finalWinner;
    private final String playerTeam;
    private final String imagePath;
    private final String trophyPath;

    public FinalScreen(PenaltyGame game, List<String> quarterTeams, List<String> semiWinners, String finalWinner, String playerTeam, String imagePath, String trophyPath) {
        super(game);
        this.quarterTeams = quarterTeams;
        this.semiWinners = semiWinners;
        this.finalWinner = finalWinner;
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
        float scale = 1.5f;
        bracketImage.setSize(bracketTexture.getWidth() * scale, bracketTexture.getHeight() * scale);
        bracketImage.setPosition((screenWidth - bracketImage.getWidth()) / 2f, (screenHeight - bracketImage.getHeight()) / 2f);
        stage.addActor(bracketImage);

        float[][] quarterPositions = {
            {90, 685}, {90, 575}, {90, 461}, {90, 350},
            {screenWidth - 260, 685}, {screenWidth - 260, 575}, {screenWidth - 260, 461}, {screenWidth - 260, 350}
        };

        for (int i = 0; i < quarterTeams.size(); i++) {
            String team = quarterTeams.get(i);
            TextButton button = createTeamButton(team, skin);
            button.setPosition(quarterPositions[i][0], quarterPositions[i][1]);
            stage.addActor(button);
        }

        float[][] semiPositions = {
            {320, 630}, {320, 400},
            {screenWidth - 430, 630}, {screenWidth - 430, 400}
        };

        for (int i = 0; i < semiWinners.size(); i++) {
            String team = semiWinners.get(i);
            TextButton button = createTeamButton(team, skin);
            button.setPosition(semiPositions[i][0], semiPositions[i][1]);
            stage.addActor(button);
        }

        float[][] finalPositions = {
            {screenWidth / 2f - 85, 550}, {screenWidth / 2f - 85, 430}
        };

        for (int i = 0; i < 2; i++) {
            String team = semiWinners.get(i);  // final eşleşmesi
            TextButton button = createTeamButton(team, skin);
            button.setPosition(finalPositions[i][0], finalPositions[i][1]);
            stage.addActor(button);
        }

        // Kazanan gösterimi
        Label.LabelStyle style = new Label.LabelStyle(skin.getFont("default-font"), Color.GOLD);
        Label winnerLabel = new Label(finalWinner + " ŞAMPİYON!", style);
        winnerLabel.setFontScale(2f);
        winnerLabel.setPosition(screenWidth / 2f - 200, 700);
        stage.addActor(winnerLabel);

        // Kupa
        Image trophy = new Image(game.assetManager.get(trophyPath, Texture.class));
        trophy.setSize(350, 350);
        trophy.setPosition((screenWidth - 350) / 2f, 260);
        stage.addActor(trophy);

        addBackButton(() -> game.setScreen(new TeamSelectionScreen(game, quarterTeams.toArray(new String[0]), imagePath, trophyPath)));
    }

    private TextButton createTeamButton(String team, Skin skin) {
        if (team.equals(playerTeam)) {
            TextButton.TextButtonStyle redStyle = new TextButton.TextButtonStyle();
            redStyle.up = skin.getDrawable("default-round-down");
            redStyle.font = skin.getFont("default-font");
            TextButton button = new TextButton(team, redStyle);
            button.getLabel().setColor(Color.YELLOW);
            button.setSize(170, 45);
            button.getLabel().setFontScale(1f);
            button.getLabel().setAlignment(Align.center);
            return button;
        } else {
            TextButton button = new TextButton(team, skin);
            button.setSize(170, 45);
            button.getLabel().setFontScale(1f);
            button.getLabel().setAlignment(Align.center);
            return button;
        }
    }

    @Override
    public void onGameEnd(boolean playerWon, String opponentTeam) {
        // Not used in FinalScreen
    }
}
