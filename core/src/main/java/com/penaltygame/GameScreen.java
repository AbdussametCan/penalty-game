// Step 1: GameScreen interface'e gerekli metodu ekliyoruz
package com.penaltygame;

import com.badlogic.gdx.Screen;

public interface GameScreen extends Screen {

    //Her ekran bitişinden sonra yapılacak işlemler her Screende ayrı ayrı gösterilir.
    void onGameEnd(boolean playerWon, String opponentTeam);
}
