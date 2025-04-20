package com.penaltygame.Screens;

import com.penaltygame.PenaltyGame;
import com.badlogic.gdx.scenes.scene2d.ui.ImageButton;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;

public class LeagueSelectionScreen extends BaseScreen {

    public LeagueSelectionScreen(PenaltyGame game) {
        super(game);
    }

    @Override
    protected void addContent() {
        Skin skin = game.assetManager.get("uiskin.json", Skin.class);

        Table table = new Table();
        table.setFillParent(true);
        table.center();
        stage.addActor(table);
        table.defaults().width(300).height(60).pad(10).center();

        addLeagueButton(table, "Champions League", skin,
            new String[]{"Real Madrid", "Barcelona", "Bayern Munchen", "Liverpool", "Manchester City", "PSG", "Inter", "Atletico Madrid"},
            "InterfacePng/champions.png",
            "InterfacePng/Trophy/championstrophy.png");

        addLeagueButton(table, "Turkish League", skin,
            new String[]{"Galatasaray", "Fenerbahce", "Besiktas", "Trabzonspor", "Basaksehir", "Goztepe", "Samsunspor", "Sivasspor"},
            "InterfacePng/turkish.png",
            "InterfacePng/Trophy/turkishtrophy.png");

        addLeagueButton(table, "Europa Cup", skin,
            new String[]{"Turkey", "France", "Germany", "Portugal", "Spain", "England", "Italy", "Belgium"},
            "InterfacePng/eurocup.png",
            "InterfacePng/Trophy/eurotrophy.png");

        addLeagueButton(table, "World Cup", skin,
            new String[]{"Argentina", "France", "Brazil", "Spain", "Senegal", "Uruguay", "Cameroon", "Turkey"},
            "InterfacePng/worldcup.png",
            "InterfacePng/Trophy/worldtrophy.png");

        addBackButton(() -> game.setScreen(new OpeningScreen(game)));
    }

    private void addLeagueButton(Table table, String leagueName, Skin skin, String[] teams, String imagePath, String trophyPath) {
        TextButton button = new TextButton(leagueName, skin);
        button.getLabel().setFontScale(1f);

        button.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                game.setScreen(new TeamSelectionScreen(game, teams, imagePath, trophyPath));
            }
        });

        table.add(button).row();
    }
}
