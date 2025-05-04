package com.penaltygame.lwjgl3;
import com.badlogic.gdx.backends.lwjgl3.Lwjgl3Application;
import com.badlogic.gdx.backends.lwjgl3.Lwjgl3ApplicationConfiguration;
import com.penaltygame.PenaltyGame;

public class lwjgl3Launcher {
    public static void main (String[] arg) {
        Lwjgl3ApplicationConfiguration config = new Lwjgl3ApplicationConfiguration();
        config.setTitle("PenaltyGame");
        config.setWindowedMode(1920, 1080);
        new Lwjgl3Application(new PenaltyGame(), config);
    }
}
