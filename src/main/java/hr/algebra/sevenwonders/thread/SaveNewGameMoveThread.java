package hr.algebra.sevenwonders.thread;

import hr.algebra.sevenwonders.model.GameMove;

public class SaveNewGameMoveThread extends GameMoveThread implements Runnable {

    private GameMove gameMove;

    public SaveNewGameMoveThread(GameMove gameMove) {
        this.gameMove = gameMove;
    }

    @Override
    public void run() {
        saveNewGameMove(gameMove);
    }
}
