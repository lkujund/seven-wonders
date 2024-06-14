package hr.algebra.sevenwonders.controller;

import hr.algebra.sevenwonders.model.Card;
import hr.algebra.sevenwonders.utils.*;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.event.ActionEvent;
import javafx.event.Event;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.geometry.Insets;
import javafx.scene.Node;
import javafx.scene.control.*;
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


    private List<Label> p1Scores;
    private List<Label> p2Scores;
    public void startGame() {
        //TODO: za multiplayer -> game starta samo server, provjeriti rolu i sakriti elemente od suprotnog igraca
        p1Scores = Arrays.asList(lbP1Civil, lbP1Science, lbP1Military, lbP1Trade, lbP1Resource, lbP1Gold, lbP1Total);
        p2Scores = Arrays.asList(lbP2Civil, lbP2Science, lbP2Military, lbP2Trade, lbP2Resource, lbP2Gold, lbP2Total);
        GameUtils.setupBoard(p1Scores, p2Scores, fpPlayerOneCards, fpPlayerTwoCards, lbWinner, GameController.this);
    }

    public void loadGame() {
//        FileUtils.loadGame(...);
    }
    public void saveGame() {
//        FileUtils.saveGame(...);
    }


    public void replayGame() {
        GameStateUtils.replayGame();
    }

    public void exportDocumentation() {
        ReflectionUtils.createDocumentation();
    }


    public void sendChatMessage() {
        ChatUtils.sendMessage(tfMessage, taChatBox);
    }


    public void cardClicked(ActionEvent actionEvent) {

        //TODO: za multiplayer -> slozit logiku da se sve salje u game state
        // i zavisno o roli se popunjavaju elementi (client: popunjava dobivena p1 polja od servera, server: p2 polja od klijenta, default: sva polja bez TCP)

        Button button = (Button) actionEvent.getSource();
        if (button.getParent() == fpPlayerOneCards)
        {
            GameUtils.playCard(button, fpPlayerOneCards, fpPlayerOnePlayedCard);
        }
        if (button.getParent() == fpPlayerTwoCards)
        {
            GameUtils.playCard(button, fpPlayerTwoCards, fpPlayerTwoPlayedCard);
        }
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
            GameUtils.concludeGame(p1Scores, lbP1Total, p2Scores, lbP2Total, lbWinner);
        } else
        {
            //TODO: za multiplayer -> provjerit player rolu (client: slati serveru, server: slati klijentu, default: swapDecks)
            GameUtils.swapDecks(fpPlayerOneCards, fpPlayerTwoCards);
        }
    }
}
