package hr.algebra.sevenwonders.utils;

import hr.algebra.sevenwonders.GameApplication;
import hr.algebra.sevenwonders.controller.GameController;
import hr.algebra.sevenwonders.model.Card;
import hr.algebra.sevenwonders.model.GameMove;
import hr.algebra.sevenwonders.model.GameState;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.FlowPane;
import javafx.util.Duration;

import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import java.util.Set;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Stream;

public class GameStateUtils {
    public static void replayGame(GameController gameController){
        gameController.lbWinner.setText("");
        List<Label> p1Scores = Arrays.asList(gameController.lbP1Civil, gameController.lbP1Science, gameController.lbP1Military, gameController.lbP1Trade, gameController.lbP1Resource, gameController.lbP1Gold, gameController.lbP1Total);
        List<Label> p2Scores = Arrays.asList(gameController.lbP2Civil, gameController.lbP2Science, gameController.lbP2Military, gameController.lbP2Trade, gameController.lbP2Resource, gameController.lbP2Gold, gameController.lbP2Total);
        gameController.fpPlayerOneCards.getChildren().clear();
        gameController.fpPlayerOnePlayedCard.getChildren().clear();
        gameController.fpPlayerTwoCards.getChildren().clear();
        gameController.fpPlayerTwoPlayedCard.getChildren().clear();
        List<GameMove> gameMoveList = XmlUtils.readGameMoves();

        if(gameMoveList.isEmpty()) {
            return;
        }

        AtomicInteger counter = new AtomicInteger(0);

        Timeline timeline = new Timeline(new KeyFrame(Duration.ZERO, e -> {
            GameMove gameMove = gameMoveList.get(counter.get());

            renderMoveOnBoard(gameController, gameMove);


            counter.set(counter.get() + 1);
        }), new KeyFrame(Duration.seconds(1)));
        timeline.setCycleCount(gameMoveList.size());
        timeline.setOnFinished(actionEvent -> GameUtils.concludeGame(p1Scores, gameController.lbP1Total, p2Scores, gameController.lbP2Total, gameController.lbWinner));
        timeline.play();

    }

    private static void renderMoveOnBoard(GameController gameController, GameMove gameMove) {



        Card playedCard = gameMove.getCard();
        Button cardButton = new Button(){{
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

        Button[] handCards = Arrays
                .stream(gameMove
                        .getPlayerHandState())
                        .map(card -> new Button(){{
                            setText(String.format("%s\nCOST: %d\nPOINTS: %d", card.name, card.cost, card.score));
                            setStyle(String.format(
                                    "-fx-wrap-text: true;" +
                                            "-fx-background-color: %s;" +
                                            "-fx-font-family: Bookman Old Style;" +
                                            "-fx-font-size: 10pt;", card.cardType.hexColor));
                            setPrefSize(110, 130);
                            FlowPane.setMargin(this,  new Insets(5));
                            setUserData(card);
                        }}).toArray(Button[]::new);

        if (Objects.equals(gameMove.getPlayerName(), "P1")){
            gameController.fpPlayerOneCards.getChildren().clear();
            gameController.fpPlayerOnePlayedCard.getChildren().clear();
            gameController.fpPlayerOneCards.getChildren().addAll(handCards);
            gameController.fpPlayerOnePlayedCard.getChildren().add(cardButton);
        } else if (Objects.equals(gameMove.getPlayerName(), "P2")){
            gameController.fpPlayerTwoCards.getChildren().clear();
            gameController.fpPlayerTwoPlayedCard.getChildren().clear();
            gameController.fpPlayerTwoCards.getChildren().addAll(handCards);
            gameController.fpPlayerTwoPlayedCard.getChildren().add(cardButton);
        }

        renderScoreboard(gameController, gameMove);
    }



    private static void renderScoreboard(GameController gameController, GameMove gameMove) {


        String[] scores = gameMove.getScoreBoardState();
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

    public static GameState createGameStateSnapshot(GameController gameController) {
        GameState gameState = new GameState();
        List<Label> p1Scores = Arrays.asList(gameController.lbP1Civil, gameController.lbP1Science, gameController.lbP1Military, gameController.lbP1Trade, gameController.lbP1Resource, gameController.lbP1Gold, gameController.lbP1Total);
        List<Label> p2Scores = Arrays.asList(gameController.lbP2Civil, gameController.lbP2Science, gameController.lbP2Military, gameController.lbP2Trade, gameController.lbP2Resource, gameController.lbP2Gold, gameController.lbP2Total);

        Label[] scoreboardState = Stream.concat(p1Scores.stream(), p2Scores.stream()).toArray(Label[]::new);
        gameState.setScoreboardState(Arrays.stream(scoreboardState).map(Label::getText).toArray(String[]::new));
        gameState.setPlayerOneHand(getPlayerHand(gameController.fpPlayerOneCards));
        gameState.setPlayerOnePlayedCard(getPlayerPlayedCard(gameController.fpPlayerOnePlayedCard));
        gameState.setPlayerTwoHand(getPlayerHand(gameController.fpPlayerTwoCards));
        gameState.setPlayerTwoPlayedCard(getPlayerPlayedCard(gameController.fpPlayerTwoPlayedCard));

        return gameState;
    }

    private static Card getPlayerPlayedCard(FlowPane fpPlayedCard) {
        if (!fpPlayedCard.getChildren().isEmpty()){
            Button button = (Button) fpPlayedCard.getChildren().getFirst();
            return (Card) button.getUserData();
        }
        return null;
    }

    private static Card[] getPlayerHand(FlowPane fpPlayerCards) {
        if (!fpPlayerCards.getChildren().isEmpty()){
            Button[] buttons = fpPlayerCards.getChildren().toArray(Button[]::new);
            return Arrays.stream(buttons).map(x -> (Card) x.getUserData()).toArray(Card[]::new);
        }
        return null;
    }
}
