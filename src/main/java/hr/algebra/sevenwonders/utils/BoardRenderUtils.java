package hr.algebra.sevenwonders.utils;

import hr.algebra.sevenwonders.controller.GameController;
import hr.algebra.sevenwonders.model.Card;
import hr.algebra.sevenwonders.model.GameState;
import javafx.geometry.Insets;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.FlowPane;

import java.util.Arrays;
import java.util.List;

public class BoardRenderUtils {
    public static void drawBoardFromGameState(GameState gameState, GameController gameController){
        gameController.p1Scores = Arrays.asList(gameController.lbP1Civil, gameController.lbP1Science, gameController.lbP1Military, gameController.lbP1Trade, gameController.lbP1Resource, gameController.lbP1Gold, gameController.lbP1Total);
        gameController.p2Scores = Arrays.asList(gameController.lbP2Civil, gameController.lbP2Science, gameController.lbP2Military, gameController.lbP2Trade, gameController.lbP2Resource, gameController.lbP2Gold, gameController.lbP2Total);

        gameController.fpPlayerOneCards.getChildren().clear();
        gameController.fpPlayerTwoCards.getChildren().clear();
        gameController.fpPlayerOnePlayedCard.getChildren().clear();
        gameController.fpPlayerTwoPlayedCard.getChildren().clear();


        Button[] playerOneHandCards = renderHandCards(gameState.getPlayerOneHand(), gameController);
        if (playerOneHandCards != null){
            gameController.fpPlayerOneCards.getChildren().addAll(playerOneHandCards);
        }

        Button playerOnePlayedCard = renderPlayedCard(gameState.getPlayerOnePlayedCard());
        if (playerOnePlayedCard != null){
            gameController.fpPlayerOnePlayedCard.getChildren().addAll(playerOnePlayedCard);
        }

        Button[] playerTwoHandCards = renderHandCards(gameState.getPlayerTwoHand(), gameController);
        if (playerTwoHandCards != null){
            gameController.fpPlayerTwoCards.getChildren().addAll(playerTwoHandCards);
        }

        Button playerTwoPlayedCard = renderPlayedCard(gameState.getPlayerTwoPlayedCard());
        if (playerTwoPlayedCard != null){
            gameController.fpPlayerTwoPlayedCard.getChildren().addAll(playerTwoPlayedCard);
        }

        renderScoreBoardFromGameState(gameController, gameState);

    }

    private static void renderScoreBoardFromGameState(GameController gameController, GameState gameState) {
        String[] scores = gameState.getScoreboardState();
        if (scores != null){
            gameController.lbP1Civil.setText(scores[0]);
            gameController.lbP1Science.setText(scores[1]);
            gameController.lbP1Military.setText(scores[2]);
            gameController.lbP1Trade.setText(scores[3]);
            gameController.lbP1Resource.setText(scores[4]);
            gameController.lbP1Gold.setText(scores[5]);
            gameController.lbP1Total.setText(scores[6]);
            gameController.lbP2Civil.setText(scores[7]);
            gameController.lbP2Science.setText(scores[8]);
            gameController.lbP2Military.setText(scores[9]);
            gameController.lbP2Trade.setText(scores[10]);
            gameController.lbP2Resource.setText(scores[11]);
            gameController.lbP2Gold.setText(scores[12]);
            gameController.lbP2Total.setText(scores[13]);
        }
    }

    private static Button renderPlayedCard(Card playedCard) {
        if (playedCard == null){
            return null;
        }
        return new Button(){{
            setText(String.format("%s\nCOST: %d\nPOINTS: %d", playedCard.name, playedCard.cost, playedCard.score));
            setStyle(String.format(
                    "-fx-wrap-text: true;" +
                            "-fx-background-color: %s;" +
                            "-fx-font-family: Bookman Old Style;" +
                            "-fx-font-size: 10pt;", playedCard.cardType.hexColor));
            setPrefSize(110, 130);
            FlowPane.setMargin(this,  new Insets(5));
            setUserData(playedCard);
        }};
    }

    private static Button[] renderHandCards(Card[] playerHand, GameController gameController) {
        if (playerHand == null){
            return null;
        }
        return Arrays
                .stream(playerHand)
                .map(card -> new Button(){{
                    setText(String.format("%s\nCOST: %d\nPOINTS: %d", card.name, card.cost, card.score));
                    setStyle(String.format(
                            "-fx-wrap-text: true;" +
                                    "-fx-background-color: %s;" +
                                    "-fx-font-family: Bookman Old Style;" +
                                    "-fx-font-size: 10pt;", card.cardType.hexColor));
                    setPrefSize(110, 130);
                    setOnMouseClicked(gameController::cardClicked);
                    FlowPane.setMargin(this,  new Insets(5));
                    setUserData(card);
                }}).toArray(Button[]::new);
    }
}
