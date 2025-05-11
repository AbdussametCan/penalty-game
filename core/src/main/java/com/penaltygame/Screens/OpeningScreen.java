package com.penaltygame.Screens;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.ImageButton;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.penaltygame.PenaltyGame;

public class OpeningScreen extends BaseScreen {

    public OpeningScreen(PenaltyGame game) {
        super(game);
    }

    @Override
    protected void addContent() {
        Table table = new Table();
        table.setFillParent(true);
        table.center();
        stage.addActor(table);

        Texture titleTexture = game.assetManager.get("InterfacePng/photo.png", Texture.class);
        Image title = new Image(titleTexture);
        table.add(title).padBottom(30).row();

        // Start butonu.
        Texture startBtnTexture = game.assetManager.get("InterfacePng/start.png", Texture.class);
        ImageButton startButton = new ImageButton(new TextureRegionDrawable(new TextureRegion(startBtnTexture)));

        startButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                game.setScreen(new LeagueSelectionScreen(game));
            }
        });

        table.add(startButton).padBottom(-235).row();
    }

    @Override
    public void onGameEnd(boolean playerWon, String opponentTeam) {
        // Bu ekranın sonunda bir şey olmadığı için çalışmıyor.
    }
}
