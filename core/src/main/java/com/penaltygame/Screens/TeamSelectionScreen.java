package com.penaltygame.Screens;

import com.penaltygame.PenaltyGame;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.ui.ImageButton;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import com.badlogic.gdx.scenes.scene2d.InputEvent;

public class TeamSelectionScreen extends BaseScreen {

    private final String[] teams;
    private final String imagePath;
    private final String trophyPath;

    public TeamSelectionScreen(PenaltyGame game, String[] teams, String imagePath, String trophyPath) {
        super(game);
        this.teams = teams;
        this.imagePath = imagePath;
        this.trophyPath = trophyPath;

    }
    @Override
    public void onGameEnd(boolean playerWon, String opponentTeam) {
        // Bu ekran için gerekli değilse boş bırakılabilir
    }


    @Override
    protected void addContent() {
        Skin skin = game.assetManager.get("uiskin.json", Skin.class);

        Table table = new Table();
        table.setFillParent(true);
        table.top(); // Üstten başlat
        stage.addActor(table);

        // 📷 Lig fotoğrafı
        Texture leagueTexture = new Texture(Gdx.files.internal(imagePath));
        Image leagueImage = new Image(leagueTexture);

        float topPadding = 30f;
        table.row().padTop(topPadding);
        table.add(leagueImage).padBottom(60).colspan(4).center().row();

        float spaceBetweenImageAndButtons = 50f;
        table.row().padBottom(spaceBetweenImageAndButtons);

        // 🧩 Takım butonları
        table.defaults().width(200).height(60).pad(20).center();

        for (int i = 0; i < teams.length; i++) {
            String team = teams[i];
            TextButton teamButton = new TextButton(team, skin);
            teamButton.getLabel().setFontScale(1f);

            teamButton.addListener(new ClickListener() {
                @Override
                public void clicked(InputEvent event, float x, float y) {
                    System.out.println("Takım seçildi: " + team);
                    game.setScreen(new QuarterFinal(game, teams, team, imagePath, trophyPath)); // ileride yönlendir
                }
            });

            table.add(teamButton);

            if ((i + 1) % 4 == 0) {
                table.row();
            }

        }

        // Back tuşu
        addBackButton(() -> game.setScreen(new LeagueSelectionScreen(game)));
    }

}
