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
import com.penaltygame.Shoot.MatchScreen;
import com.penaltygame.GameScreen;

import java.util.*;
import java.util.List;

public class QuarterFinal extends BaseScreen implements GameScreen {

    private final List<String> shuffledTeams;
    private final String selectedTeam;
    private final String imagePath;
    private final String trophyPath;
    private  final String leagueName;

    public QuarterFinal(PenaltyGame game, String[] teams, String selectedTeam, String imagePath, String trophyPath,String leagueName) {
        super(game);
        this.shuffledTeams = new ArrayList<>(Arrays.asList(teams));
        Collections.shuffle(this.shuffledTeams);
        this.selectedTeam = selectedTeam;
        this.imagePath = imagePath;
        this.trophyPath = trophyPath;
        this.leagueName = leagueName;
    }

    @Override
    protected void addContent() {
        Skin skin = game.assetManager.get("uiskin.json", Skin.class);
        float screenWidth = Gdx.graphics.getWidth();
        float screenHeight = Gdx.graphics.getHeight();

        //Eşleşme tablosu aktarılır.
        Texture bracketTexture = new Texture(Gdx.files.internal("bracket.png"));
        Image bracketImage = new Image(bracketTexture);
        float scale = 1.5f;
        bracketImage.setSize(bracketTexture.getWidth() * scale, bracketTexture.getHeight() * scale);
        bracketImage.setPosition((screenWidth - bracketImage.getWidth()) / 2f, (screenHeight - bracketImage.getHeight()) / 2f);
        stage.addActor(bracketImage);

        //Takım isimlerinin fikstürdeki konumları.
        float[][] positions = {
            {90, 685}, {90, 575}, {90, 461}, {90, 350},
            {screenWidth - 255, 685}, {screenWidth - 255, 575}, {screenWidth - 255, 461}, {screenWidth - 255, 350}
        };

        //Rastgele takım ataması.
        for (int i = 0; i < 8; i++) {
            String team = shuffledTeams.get(i);

            //Seçtiğimiz takım kırmızı arka fonla gösterilir.
            TextButton teamButton;
            if (team.equals(selectedTeam)) {
                TextButton.TextButtonStyle redStyle = new TextButton.TextButtonStyle();
                redStyle.up = skin.getDrawable("default-round-down");
                redStyle.font = skin.getFont("default-font");
                teamButton = new TextButton(team, redStyle);
                teamButton.getLabel().setColor(Color.YELLOW);
            }
            else {
                teamButton = new TextButton(team, skin);
            }

            teamButton.setSize(170, 45);
            teamButton.setPosition(positions[i][0], positions[i][1]);
            teamButton.getLabel().setFontScale(1f);
            teamButton.getLabel().setAlignment(Align.center);
            stage.addActor(teamButton);
        }


        //Play butonu.
        TextureRegionDrawable playDrawable = new TextureRegionDrawable(new TextureRegion(
            game.assetManager.get("InterfacePng/play.png", Texture.class)));
        ImageButton playButton = new ImageButton(playDrawable);
        playButton.setSize(300, 150);
        playButton.setPosition(screenWidth - 300, 20);
        playButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                for (int i = 0; i < 8; i += 2) {
                    if (shuffledTeams.get(i).equals(selectedTeam) || shuffledTeams.get(i + 1).equals(selectedTeam)) {
                        String rakip = shuffledTeams.get(i).equals(selectedTeam) ? shuffledTeams.get(i + 1) : shuffledTeams.get(i);
                        game.setScreen(new MatchScreen(game, selectedTeam, rakip, QuarterFinal.this));
                        break;
                    }
                }
            }
        });
        stage.addActor(playButton);

        Image roadImage = new Image(game.assetManager.get("InterfacePng/roadfinal.png", Texture.class));
        roadImage.setSize(1000, 200);
        roadImage.setPosition((screenWidth - roadImage.getWidth()) / 2f, screenHeight - roadImage.getHeight() - 75);
        stage.addActor(roadImage);


        //Her lige özel kupa resmi.
        Image trophyImage = new Image(game.assetManager.get(trophyPath, Texture.class));
        trophyImage.setSize(350, 350);
        trophyImage.setPosition((screenWidth - 350) / 2f, 375);
        stage.addActor(trophyImage);

        addgeri(() -> {
            game.setScreen(new TeamSelectionScreen(game, shuffledTeams.toArray(new String[0]), imagePath, trophyPath,leagueName));
        });
    }


    //Maç bittikten sonraki aşamalar.
    @Override
    public void onGameEnd(boolean playerWon, String opponentTeam) {
        String winner = playerWon ? selectedTeam : opponentTeam;
        List<String> kazananlar = new ArrayList<>();

        for (int i = 0; i < 8; i += 2) {
            String teamA = shuffledTeams.get(i);
            String teamB = shuffledTeams.get(i + 1);

            if (teamA.equals(selectedTeam) || teamB.equals(selectedTeam)) {
                kazananlar.add(winner);
            } else {
                kazananlar.add(Math.random() < 0.5 ? teamA : teamB);
            }
        }

        game.setScreen(new SemiFinal(game, shuffledTeams, kazananlar, winner, imagePath, trophyPath,leagueName));
    }
}
