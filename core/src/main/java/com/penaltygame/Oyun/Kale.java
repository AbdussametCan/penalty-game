package com.penaltygame.Oyun;

public class Kale {
    public boolean checkGoal(String ballDirection, String keeperDirection) {
        boolean isGoal = !ballDirection.equals(keeperDirection);
        System.out.println(isGoal ? "GOOOL!" : "Kurtardı!");
        return isGoal;
    }
}
