package hr.algebra.sevenwonders.controller;

import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.event.ActionEvent;
import javafx.event.Event;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.FlowPane;
import javafx.util.Duration;

import java.time.LocalDateTime;
import java.util.Set;
import java.util.concurrent.atomic.AtomicInteger;

public class GameController {

    //Board elementi
    @FXML
    public AnchorPane apBoard;
    @FXML
    public FlowPane fpPlayerOneCards;
    @FXML
    public FlowPane fpPlayerTwoCards;


    //Scoreboard elementi
    @FXML
    public AnchorPane apScoreboard;
    @FXML
    public Label lbP1Civil;
    @FXML
    public Label lbP1Science;
    @FXML
    public Label lbP1Military;
    @FXML
    public Label lbP1Trade;
    @FXML
    public Label lbP1Wonder;
    @FXML
    public Label lbP1Gold;
    @FXML
    public Label lbP1Total;
    @FXML
    public Label lbP2Civil;
    @FXML
    public Label lbP2Science;
    @FXML
    public Label lbP2Military;
    @FXML
    public Label lbP2Trade;
    @FXML
    public Label lbP2Wonder;
    @FXML
    public Label lbP2Gold;
    @FXML
    public Label lbP2Total;
    @FXML
    public Label lbWinner;


    //Chat elementi
    @FXML
    public AnchorPane apChat;
    @FXML
    public TextField tfMessage;
    @FXML
    public Button btnSendMessage;
    @FXML
    public TextArea taChatBox;


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
