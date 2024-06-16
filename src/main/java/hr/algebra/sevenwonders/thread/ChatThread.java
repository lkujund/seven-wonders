package hr.algebra.sevenwonders.thread;

import hr.algebra.sevenwonders.controller.GameController;
import hr.algebra.sevenwonders.model.GameMove;
import hr.algebra.sevenwonders.utils.ChatUtils;
import javafx.application.Platform;

import java.rmi.RemoteException;

public class ChatThread implements Runnable{
    @Override
    public void run() {
        while (true) {
            Platform.runLater(() -> {
                GameController gc = GameController.theGameController;
                ChatUtils.updateChatMessages(
                        gc.getChatServiceInstance(), gc.taChatBox
                );
            });

            try {
                Thread.sleep(500);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        }
    }
}
