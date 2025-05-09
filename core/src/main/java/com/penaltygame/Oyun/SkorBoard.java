package com.penaltygame.Oyun;

public class SkorBoard {
    private String playerTeam;
    private String botTeam;
    private int playerScore = 0;
    private int botScore = 0;

    public SkorBoard(String playerTeam, String botTeam) {
        this.playerTeam = playerTeam;
        this.botTeam = botTeam;
    }

    public void golAtti(boolean playerShooting) {
        if (playerShooting) playerScore++;
        else botScore++;
    }

    public void reset() {
        playerScore = 0;
        botScore = 0;
    }

    public String getPlayerTeam() {
        return playerTeam;
    }

    public String getBotTeam() {
        return botTeam;
    }

    public int getPlayerScore() {
        return playerScore;
    }

    public int getBotScore() {
        return botScore;
    }
}
