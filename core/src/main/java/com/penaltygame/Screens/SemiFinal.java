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
import java.util.List;

public class SemiFinal extends BaseScreen implements GameScreen {
    private final List<String> tumTakimlar;  // 8 takım (çeyrek finalistler)
    private final List<String> kazananlar;   // 4 takım (yarı finalistler)
    private final String playerTeam;
    private final String imagePath;
    private final String trophyPath;
    private final String leagueName;

    public SemiFinal(PenaltyGame game, List<String> tumTakimlar, List<String> kazananlar, String playerTeam, String imagePath, String trophyPath, String leagueName) {
        super(game);
        this.tumTakimlar = tumTakimlar;
        this.kazananlar = kazananlar;
        this.playerTeam = playerTeam;
        this.imagePath = imagePath;
        this.trophyPath = trophyPath;
        this.leagueName = leagueName;
    }

    @Override
    protected void addContent() {
        Skin skin = game.assetManager.get("uiskin.json", Skin.class);
        float screenWidth = Gdx.graphics.getWidth();
        float screenHeight = Gdx.graphics.getHeight();

        // Bracket arka planı
        Texture bracketTexture = new Texture(Gdx.files.internal("bracket.png"));
        Image bracketImage = new Image(bracketTexture);
        bracketImage.setSize(bracketTexture.getWidth() * 1.5f, bracketTexture.getHeight() * 1.5f);
        bracketImage.setPosition((screenWidth - bracketImage.getWidth()) / 2f, (screenHeight - bracketImage.getHeight()) / 2f);
        stage.addActor(bracketImage);

        Image roadImage = new Image(game.assetManager.get("InterfacePng/roadfinal.png", Texture.class));
        roadImage.setSize(1000, 200);
        roadImage.setPosition((screenWidth - roadImage.getWidth()) / 2f, screenHeight - roadImage.getHeight() - 75);
        stage.addActor(roadImage);


        // Çeyrek final pozisyonları
        float[][] quarterPositions = {
            {90, 685}, {90, 575}, {90, 461}, {90, 350},
            {screenWidth - 255, 685}, {screenWidth - 255, 575}, {screenWidth - 255, 461}, {screenWidth - 255, 350}
        };

        for (int i = 0; i < tumTakimlar.size(); i++) {
            String team = tumTakimlar.get(i);
            TextButton button = createTeamButton(team, skin);
            button.setPosition(quarterPositions[i][0], quarterPositions[i][1]);
            stage.addActor(button);
        }

        // Yarı final pozisyonları
        float[][] semiPositions = {
            {375, 630}, {375, 406},
            {screenWidth - 540, 630}, {screenWidth - 540, 406}
        };

        for (int i = 0; i < kazananlar.size() && i < semiPositions.length; i++) {
            String team = kazananlar.get(i);
            TextButton button = createTeamButton(team, skin);
            button.setPosition(semiPositions[i][0], semiPositions[i][1]);
            stage.addActor(button);
        }

        // Play butonu
        Texture playTexture = game.assetManager.get("InterfacePng/play.png", Texture.class);
        ImageButton playButton = new ImageButton(new TextureRegionDrawable(new TextureRegion(playTexture)));
        playButton.setSize(300, 150);
        playButton.setPosition(screenWidth - 300, 20);
        playButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                String rakip = null;
                if (kazananlar.size() == 4) {
                    if (playerTeam.equals(kazananlar.get(0))) rakip = kazananlar.get(1);
                    else if (playerTeam.equals(kazananlar.get(1))) rakip = kazananlar.get(0);
                    else if (playerTeam.equals(kazananlar.get(2))) rakip = kazananlar.get(3);
                    else if (playerTeam.equals(kazananlar.get(3))) rakip = kazananlar.get(2);
                }
                if (rakip != null) {
                    game.setScreen(new FirstScreen(game, playerTeam, rakip, SemiFinal.this));
                }
            }
        });
        stage.addActor(playButton);
    }

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
    @Override
    public void onGameEnd(boolean playerWon, String opponentTeam) {
        List<String> finalTeams = new ArrayList<>();
        String winner = playerWon ? playerTeam : opponentTeam;
        finalTeams.add(winner);

        String digerFinalist = null;
        if (kazananlar.size() == 4) {
            if (playerTeam.equals(kazananlar.get(0)) || playerTeam.equals(kazananlar.get(1))) {
                digerFinalist = kazananlar.get(2).equals(opponentTeam) ? kazananlar.get(3) : kazananlar.get(2);
            } else {
                digerFinalist = kazananlar.get(0).equals(opponentTeam) ? kazananlar.get(1) : kazananlar.get(0);
            }
        }
        if (digerFinalist != null) finalTeams.add(digerFinalist);

        boolean playerStartedOnLeft = playerTeam.equals(kazananlar.get(0)) || playerTeam.equals(kazananlar.get(1));

        game.setScreen(new FinalScreen(game, tumTakimlar, kazananlar, winner, finalTeams.get(1), imagePath, trophyPath, leagueName, playerStartedOnLeft));
    }
}
