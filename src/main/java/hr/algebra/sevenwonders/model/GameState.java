package hr.algebra.sevenwonders.model;

import javafx.scene.control.Button;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.FlowPane;

import java.io.BufferedInputStream;
import java.io.Serializable;

public class GameState implements Serializable {

    //buttoni nisu serializable, koristit objekte umjesto tog (card)
    private Card[] playerOneHand;
    private Card playerOnePlayedCard;
    private Card[] playerTwoHand;
    private Card playerTwoPlayedCard;


//Umjesto anchorpanea poslat array stringova vrijednosti labelova i tocnim redom ih slat i citat
    private String[] scoreboardState;

    public GameState(){

    }
    public GameState(Card[] playerOneHand, Card playerOnePlayedCard, Card[] playerTwoHand, Card playerTwoPlayedCard, String[] scoreboardState ) {
        this.playerOneHand = playerOneHand;
        this.playerOnePlayedCard = playerOnePlayedCard;
        this.playerTwoHand = playerTwoHand;
        this.playerTwoPlayedCard = playerTwoPlayedCard;
        this.scoreboardState = scoreboardState;
    }

    public Card[] getPlayerOneHand() {
        return playerOneHand;
    }

    public void setPlayerOneHand(Card[] playerOneHand) {
        this.playerOneHand = playerOneHand;
    }

    public Card getPlayerOnePlayedCard() {
        return playerOnePlayedCard;
    }

    public void setPlayerOnePlayedCard(Card playerOnePlayedCard) {
        this.playerOnePlayedCard = playerOnePlayedCard;
    }

    public Card[] getPlayerTwoHand() {
        return playerTwoHand;
    }

    public void setPlayerTwoHand(Card[] playerTwoHand) {
        this.playerTwoHand = playerTwoHand;
    }

    public Card getPlayerTwoPlayedCard() {
        return playerTwoPlayedCard;
    }

    public void setPlayerTwoPlayedCard(Card playerTwoPlayedCard) {
        this.playerTwoPlayedCard = playerTwoPlayedCard;
    }

    public String[] getScoreboardState() {
        return scoreboardState;
    }

    public void setScoreboardState(String[] scoreboardState) {
        this.scoreboardState = scoreboardState;
    }
}
