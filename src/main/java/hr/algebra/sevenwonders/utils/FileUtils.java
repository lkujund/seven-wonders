package hr.algebra.sevenwonders.utils;

import hr.algebra.sevenwonders.model.GameState;

import java.io.*;

public class FileUtils {
    public static final String SAVE_GAME_FILE_NAME = "saveGame.dat";
    public static GameState loadGame(){
        GameState recoveredGameState;

        try(ObjectInputStream ois = new ObjectInputStream(new FileInputStream(FileUtils.SAVE_GAME_FILE_NAME))) {
            recoveredGameState = (GameState) ois.readObject();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }

        return recoveredGameState;
    }

    public static void saveGame(GameState gameState){

        try(ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(FileUtils.SAVE_GAME_FILE_NAME))) {
            oos.writeObject(gameState);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

    }
}
