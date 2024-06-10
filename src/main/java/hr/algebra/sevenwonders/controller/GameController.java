package hr.algebra.sevenwonders.controller;

import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.event.ActionEvent;
import javafx.event.Event;
import javafx.event.EventHandler;
import javafx.scene.control.Button;
import javafx.util.Duration;

import java.time.LocalDateTime;
import java.util.Set;
import java.util.concurrent.atomic.AtomicInteger;

public class GameController {


    public void startNewGame()
    {

    }

    public void saveGame() {
//        FileUtils.saveGame(gameBoard, numberOfMoves, NUM_OF_ROWS, NUM_OF_ROWS, turn);
    }


    public void replayGame() {
//        Set<GameMove> gameMoves = XmlUtils.readAllGameMoves();
//        final AtomicInteger counter = new AtomicInteger(0);
//
//        Timeline clock = new Timeline(new KeyFrame(Duration.seconds(1),
//                new EventHandler<ActionEvent>() {
//                    @Override
//                    public void handle(ActionEvent event) {
//                        GameMove gameMove = (GameMove) gameMoves.toArray()[counter.get()];
//                        Symbol symbol = gameMove.getSymbol();
//                        String position = gameMove.getPosition();
//                        LocalDateTime localDateTime = gameMove.getLocalDateTime();
//
//                        Button button = (Button) HelloApplication.getMainScene().lookup("#" + position);
//                        button.setText(symbol.name());
//
//                        counter.set(counter.get() + 1);
//                    }
//                }), new KeyFrame(Duration.seconds(1)));
//        clock.setCycleCount(gameMoves.size());
//        clock.play();
    }


    public void startButtonPressed(Event event) {
// mozda ovako
    }


    public void sendChatMessage() {
//        String chatMessage = chatMessageTextField.getText();
//        chatMessageTextField.clear();
//        ChatUtils.sendChatMessage(chatMessage, chatTextArea);
    }

}
