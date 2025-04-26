package com.penaltygame.GoalKeeper;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;

public class Main extends ApplicationAdapter {
    SpriteBatch batch;
    Texture goalkeeperTexture;
    TextureRegion goalkeeperRegion;
    PlayerGoalkeeper goalkeeper;

    @Override
    public void create() {
        batch = new SpriteBatch();
        goalkeeperTexture = new Texture("goalkeeper_idle.png");
        goalkeeperRegion = new TextureRegion(goalkeeperTexture);
        goalkeeper = new PlayerGoalkeeper();
    }

    @Override
    public void render() {
        float delta = Gdx.graphics.getDeltaTime();
        goalkeeper.update(delta);

        Gdx.gl.glClearColor(0, 0, 0, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        batch.begin();

        batch.draw(goalkeeperRegion, goalkeeper.position.x, goalkeeper.position.y, 80, 120);


        batch.end();
    }


    @Override
    public void dispose() {
        batch.dispose();
        goalkeeperTexture.dispose();
    }
}
