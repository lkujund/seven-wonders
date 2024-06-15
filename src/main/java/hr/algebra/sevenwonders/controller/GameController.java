package hr.algebra.sevenwonders.controller;

import hr.algebra.sevenwonders.model.Card;
import hr.algebra.sevenwonders.model.GameMove;
import hr.algebra.sevenwonders.model.GameState;
import hr.algebra.sevenwonders.thread.GetLastGameMoveThread;
import hr.algebra.sevenwonders.utils.*;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.beans.Observable;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.Event;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.geometry.Insets;
import javafx.scene.Node;
import javafx.scene.control.*;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.FlowPane;
import javafx.util.Duration;
import javafx.util.Pair;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Set;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Stream;

public class GameController {

    //Board elementi
    @FXML
    public AnchorPane apBoard;
    @FXML
    public FlowPane fpPlayerOneCards;
    @FXML
    public FlowPane fpPlayerTwoCards;
    @FXML
    public FlowPane fpPlayerOnePlayedCard;
    @FXML
    public FlowPane fpPlayerTwoPlayedCard;
    @FXML
    private Label theLastGameMoveLabel;

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
    public Label lbP1Resource;
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
    public Label lbP2Resource;
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


    public List<Label> p1Scores;
    public List<Label> p2Scores;

    public void startGame() {


        GetLastGameMoveThread getLastGameMoveThread
                = new GetLastGameMoveThread(theLastGameMoveLabel);
        Thread starterThread = new Thread(getLastGameMoveThread);
        starterThread.start();
        //TODO: za multiplayer -> game starta samo server, provjeriti rolu i sakriti elemente od suprotnog igraca
        XmlUtils.createNewReplayFile();
        p1Scores = Arrays.asList(lbP1Civil, lbP1Science, lbP1Military, lbP1Trade, lbP1Resource, lbP1Gold, lbP1Total);
        p2Scores = Arrays.asList(lbP2Civil, lbP2Science, lbP2Military, lbP2Trade, lbP2Resource, lbP2Gold, lbP2Total);
        GameUtils.setupBoard(p1Scores, p2Scores, fpPlayerOneCards, fpPlayerTwoCards, lbWinner, GameController.this);
    }

    public void loadGame() {
        GameState gameState = FileUtils.loadGame();
        BoardRenderUtils.drawBoardFromGameState(gameState, GameController.this);
        GetLastGameMoveThread getLastGameMoveThread
                = new GetLastGameMoveThread(theLastGameMoveLabel);
        Thread starterThread = new Thread(getLastGameMoveThread);
        starterThread.start();
    }
    public void saveGame() {
        FileUtils.saveGame(GameStateUtils.createGameStateSnapshot(GameController.this));
    }


    public void replayGame() {

        GameStateUtils.replayGame(GameController.this);

    }

    public void exportDocumentation() {
        ReflectionUtils.createDocumentation();
    }


    public void sendChatMessage() {
        ChatUtils.sendMessage(tfMessage, taChatBox);
    }


    public void cardClicked(MouseEvent mouseEvent) {

        //TODO: za multiplayer -> slozit logiku da se sve salje u game state
        // i zavisno o roli se popunjavaju elementi (client: popunjava dobivena p1 polja od servera, server: p2 polja od klijenta, default: sva polja bez TCP)

        Button button = (Button) mouseEvent.getSource();
        GameMove gameMove = new GameMove();
        if (button.getParent() == fpPlayerOneCards)
        {
            if (mouseEvent.getButton() == MouseButton.PRIMARY){
                GameUtils.playCard(button, fpPlayerOneCards, fpPlayerOnePlayedCard, lbP1Gold);
            } else if (mouseEvent.getButton() == MouseButton.SECONDARY){
                GameUtils.discard(button, fpPlayerOneCards, fpPlayerOnePlayedCard, lbP1Gold);
            }
            gameMove.setPlayerName("P1");
            gameMove.setPlayerHandState((fpPlayerOneCards
                    .getChildren())
                    .stream()
                    .map(x -> (Button) x)
                    .map(b -> (Card) b.getUserData())
                    .toArray(Card[]::new));
        }
        else if (button.getParent() == fpPlayerTwoCards)
        {
            if (mouseEvent.getButton() == MouseButton.PRIMARY) {
                GameUtils.playCard(button, fpPlayerTwoCards, fpPlayerTwoPlayedCard, lbP2Gold);
            } else if (mouseEvent.getButton() == MouseButton.SECONDARY){
                GameUtils.discard(button, fpPlayerTwoCards, fpPlayerTwoPlayedCard, lbP2Gold);
            }
            gameMove.setPlayerName("P2");
            gameMove.setPlayerHandState((fpPlayerTwoCards
                    .getChildren())
                    .stream()
                    .map(x -> (Button) x)
                    .map(b -> (Card) b.getUserData())
                    .toArray(Card[]::new));
        }
        gameMove.setCard((Card) button.getUserData());
        gameMove.setDateTime(LocalDateTime.now());

        List<Label> scores = Stream.concat(p1Scores.stream(), p2Scores.stream()).toList();
        gameMove.setScoreBoardState(scores.stream().map(Label::getText).toArray(String[]::new));
        GameMoveUtils.saveGameMove(gameMove);
        XmlUtils.saveGameMove(gameMove);
        checkPlayedCards();
    }

    private void checkPlayedCards() {
        if (!fpPlayerOnePlayedCard.getChildren().isEmpty() &&
        !fpPlayerTwoPlayedCard.getChildren().isEmpty())
        {
            evaluateCardScore((Button)fpPlayerOnePlayedCard.getChildren().getFirst());
            evaluateCardScore((Button) fpPlayerTwoPlayedCard.getChildren().getFirst());


            fpPlayerOnePlayedCard.getChildren().clear();
            fpPlayerTwoPlayedCard.getChildren().clear();

            checkRemainingCards();
        }
    }




    private void evaluateCardScore(Button cardButton) {
        if (cardButton.getParent() == fpPlayerOnePlayedCard)
        {
            GameUtils.evaluateCardScore(cardButton, GameController.this, "P1");
        }else
        {
            GameUtils.evaluateCardScore(cardButton, GameController.this, "P2");
        }
    }

    private void setScore(Label scoreLabel, int score) {
        int currResult = Integer.parseInt(scoreLabel.getText());
        scoreLabel.setText(String.valueOf(currResult + score));
    }


    private void checkRemainingCards() {
        if (fpPlayerOneCards.getChildren().isEmpty() && fpPlayerTwoCards.getChildren().isEmpty())
        {
            List<Label> scores = Stream.concat(p1Scores.stream(), p2Scores.stream()).toList();
            GameMove lastGameMove = new GameMove(){{
                setScoreBoardState(scores.stream().map(Label::getText).toArray(String[]::new));
                setDateTime(LocalDateTime.now());
            }};
            XmlUtils.saveGameMove(lastGameMove);

            GameUtils.concludeGame(p1Scores, lbP1Total, p2Scores, lbP2Total, lbWinner);
        } else
        {
            //TODO: za multiplayer -> provjerit player rolu (client: slati serveru, server: slati klijentu, default: swapDecks)
            GameUtils.swapDecks(fpPlayerOneCards, fpPlayerTwoCards);
        }
    }
}
