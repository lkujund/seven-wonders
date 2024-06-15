package hr.algebra.sevenwonders.model;

import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.AnchorPane;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.stream.Collectors;

public class GameMove implements Serializable {

    private Card card;

    private String playerName;

    private Card[] playerHandState;
    private String[] scoreBoardState;

    private LocalDateTime dateTime;

    public GameMove() {
        this.card = Card.DISCARD_CARD;
        this.playerName = "";
        this.playerHandState = new Card[]{};
        this.scoreBoardState = new String[]{};
        this.dateTime = LocalDateTime.now();
    }
    public GameMove(Card card, String playerName, Card[] playerHandState, String[] scoreBoardState, LocalDateTime dateTime) {
        this.card = card;
        this.playerName = playerName;
        this.playerHandState = playerHandState;
        this.scoreBoardState = scoreBoardState;
        this.dateTime = dateTime;
    }

    public Card getCard() {
        return card;
    }

    public String getCardName() {
        return card.name;
    }

    public void setCard(Card card) {
        this.card = card;
    }

    public String getPlayerName() {
        return playerName;
    }

    public void setPlayerName(String playerName) {
        this.playerName = playerName;
    }

    public Card[] getPlayerHandState() {
        return playerHandState;
    }

    public String getPlayerHandStateToString() {
        return Arrays.stream(playerHandState).map(x -> x.toString()).collect(Collectors.joining(","));
    }
    public void setPlayerHandState(Card[] playerHandState) {
        this.playerHandState = playerHandState;
    }

    public String[] getScoreBoardState() {
        return scoreBoardState;
    }

    public void setScoreBoardState(String[] scoreBoardState) {
        this.scoreBoardState = scoreBoardState;
    }

    public LocalDateTime getDateTime() {
        return dateTime;
    }

    public void setDateTime(LocalDateTime dateTime) {
        this.dateTime = dateTime;
    }
}
