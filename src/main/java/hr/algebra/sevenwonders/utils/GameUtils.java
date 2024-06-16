package hr.algebra.sevenwonders.utils;

import hr.algebra.sevenwonders.GameApplication;
import hr.algebra.sevenwonders.controller.GameController;
import hr.algebra.sevenwonders.model.Card;
import hr.algebra.sevenwonders.model.GameState;
import hr.algebra.sevenwonders.model.UserRole;
import javafx.geometry.Insets;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.FlowPane;
import javafx.util.Pair;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class GameUtils {

    public static void setupBoard(List<Label> p1Scores, List<Label> p2Scores, FlowPane fpPlayerOneCards, FlowPane fpPlayerTwoCards, Label lbWinner, GameController gameController) {
        fpPlayerOneCards.getChildren().clear();
        fpPlayerTwoCards.getChildren().clear();
        gameController.fpPlayerOnePlayedCard.getChildren().clear();
        gameController.fpPlayerTwoPlayedCard.getChildren().clear();
        lbWinner.setText("");
        p1Scores.forEach(p -> p.setText("0"));
        p2Scores.forEach(p -> p.setText("0"));
        gameController.lbP1Gold.setText("3");
        gameController.lbP2Gold.setText("3");

        List<Button> cards = CardLoaderUtils.loadFourteenCards();
        List<Button> p1Cards = cards.subList(0,7);
        List<Button> p2Cards = cards.subList(7,14);

        for (Button card : p1Cards){
            card.setOnMouseClicked(gameController::cardClicked);
            fpPlayerOneCards.getChildren().add(card);
        }
        for (Button card : p2Cards){
            card.setOnMouseClicked(gameController::cardClicked);
            fpPlayerTwoCards.getChildren().add(card);
        }
    }

    public static void playCard(Button button, FlowPane fpPlayerCards, FlowPane fpPlayedCard, Label playerGold) {
        if (hasGoldToPlayCard(button, playerGold)) {
            if (fpPlayedCard.getChildren().isEmpty()) {
                fpPlayerCards.getChildren().remove(button);
                fpPlayedCard.getChildren().add(button);
            }
        }
    }

    private static boolean hasGoldToPlayCard(Button button, Label playerGold) {
        int currGold = Integer.parseInt(playerGold.getText());
        int cardCost = ((Card) button.getUserData()).cost;
        if (currGold >= cardCost){
            playerGold.setText(String.valueOf(currGold - cardCost));
            return true;
        }else {
            return false;
        }
    }

    public static void swapDecks(FlowPane fpPlayerOneCards, FlowPane fpPlayerTwoCards){
        List<Node> helperList = new ArrayList<>(fpPlayerOneCards.getChildren());
        fpPlayerOneCards.getChildren().setAll(fpPlayerTwoCards.getChildren());
        fpPlayerTwoCards.getChildren().setAll(helperList);
    }

    public static void concludeGame(List<Label> p1Scores, Label lbP1Total, List<Label> p2Scores, Label lbP2Total, Label lbWinner) {
        int p1Result = p1Scores.stream().map(s -> Integer.parseInt(s.getText())).reduce(0, Integer::sum);
        lbP1Total.setText(String.valueOf(p1Result));
        int p2Result = p2Scores.stream().map(s -> Integer.parseInt(s.getText())).reduce(0, Integer::sum);
        lbP2Total.setText(String.valueOf(p2Result));
        if (p1Result != p2Result)
        {
            lbWinner.setText(String.format("PLAYER %d WINS!", p1Result > p2Result ? 1 : 2));
        }else {
            lbWinner.setText("DRAW!");
        }
    }

    public static void evaluateCardScore(Button cardButton, GameController gameController, String player) {

        Card card = (Card) cardButton.getUserData();
        switch (card.cardType){
            case BLUE_CIVIL -> setScore(
                    Objects.equals(player, "P1") ? gameController.lbP1Civil : gameController.lbP2Civil,
                    card.score);

            case GREEN_SCIENCE -> setScore(
                    Objects.equals(player, "P1") ? gameController.lbP1Science : gameController.lbP2Science,
                    card.score);

            case RED_MILITARY -> setScore(
                    Objects.equals(player, "P1") ? gameController.lbP1Military : gameController.lbP2Military,
                    card.score);

            case GREY_TRADE -> setScore(
                    Objects.equals(player, "P1") ? gameController.lbP1Trade : gameController.lbP2Trade,
                    card.score);

            case BROWN_RESOURCE -> setScore(
                    Objects.equals(player, "P1") ? gameController.lbP1Resource : gameController.lbP2Resource,
                    card.score);

            case YELLOW_GOLD -> setScore(
                    Objects.equals(player, "P1") ? gameController.lbP1Gold : gameController.lbP2Gold,
                    card.score);
        }
    }
    private static void setScore(Label scoreLabel, int score) {
        int currResult = Integer.parseInt(scoreLabel.getText());
        scoreLabel.setText(String.valueOf(currResult + score));
    }

    public static void discard(Button button, FlowPane fpPlayerCards, FlowPane fpPlayedCard, Label lbGold) {
        if (fpPlayedCard.getChildren().isEmpty()) {
            fpPlayerCards.getChildren().remove(button);
            Button discardCardButton = new Button(){{
                setText("DISCARD");
                setStyle("-fx-background-color: black;-fx-font-family: Bookman Old Style;-fx-font-size: 10pt;");
                setPrefSize(110, 130);
                FlowPane.setMargin(this,  new Insets(5));
                setUserData(Card.DISCARD_CARD);
            }};
            fpPlayedCard.getChildren().add(discardCardButton);
        }
    }
}
