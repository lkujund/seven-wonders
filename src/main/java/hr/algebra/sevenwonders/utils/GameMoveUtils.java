package hr.algebra.sevenwonders.utils;

import hr.algebra.sevenwonders.model.GameMove;
import hr.algebra.sevenwonders.thread.SaveNewGameMoveThread;
import javafx.scene.control.Button;

import java.time.LocalDateTime;

public class GameMoveUtils {

    public static void saveGameMove(GameMove gameMove) {

        XmlUtils.saveGameMove(gameMove);

        SaveNewGameMoveThread saveNewGameMoveThread
                = new SaveNewGameMoveThread(gameMove);
        Thread staterThread = new Thread(saveNewGameMoveThread);
        staterThread.start();
    }

}
