package hr.algebra.sevenwonders.controller;

import hr.algebra.sevenwonders.model.Card;
import hr.algebra.sevenwonders.utils.CardLoaderUtils;
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
        p1Scores = Arrays.asList(lbP1Civil, lbP1Science, lbP1Military, lbP1Trade, lbP1Resource, lbP1Gold, lbP1Total);
        p2Scores = Arrays.asList(lbP2Civil, lbP2Science, lbP2Military, lbP2Trade, lbP2Resource, lbP2Gold, lbP2Total);
        fpPlayerOneCards.getChildren().clear();
        fpPlayerTwoCards.getChildren().clear();
        lbWinner.setText("");
        p1Scores.forEach(p -> p.setText("0"));
        p2Scores.forEach(p -> p.setText("0"));

        List<Button> cards = CardLoaderUtils.loadFourteenCards();
        List<Button> p1Cards = cards.subList(0,7);
        List<Button> p2Cards = cards.subList(7,14);

        for (Button card : p1Cards){
            card.setOnAction(GameController.this::cardClicked);
            fpPlayerOneCards.getChildren().add(card);
        }
        for (Button card : p2Cards){
            card.setOnAction(GameController.this::cardClicked);
            fpPlayerTwoCards.getChildren().add(card);
        }
    }

    public void loadGame() {
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

    public void exportDocumentation() {
    }


    public void sendChatMessage() {
//        String chatMessage = chatMessageTextField.getText();
//        chatMessageTextField.clear();
//        ChatUtils.sendChatMessage(chatMessage, chatTextArea);
    }


    public void cardClicked(ActionEvent actionEvent) {
        Button button = (Button) actionEvent.getSource();
        if (button.getParent() == fpPlayerOneCards)
        {
            if (fpPlayerOnePlayedCard.getChildren().isEmpty())
            {
                fpPlayerOneCards.getChildren().remove(button);
                fpPlayerOnePlayedCard.getChildren().add(button);
            }

        }
        if (button.getParent() == fpPlayerTwoCards)
        {
            if (fpPlayerTwoPlayedCard.getChildren().isEmpty())
            {
                fpPlayerTwoCards.getChildren().remove(button);
                fpPlayerTwoPlayedCard.getChildren().add(button);
            }
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
        Card card = (Card) cardButton.getUserData();
        switch (card.cardType){
            case BLUE_CIVIL -> {
                if (cardButton.getParent() == fpPlayerOnePlayedCard)
                {
                    setScore(lbP1Civil, card.score);
                }else {
                    setScore(lbP2Civil, card.score);
                }
            }
            case GREEN_SCIENCE ->{
                if (cardButton.getParent() == fpPlayerOnePlayedCard)
                {
                    setScore(lbP1Science, card.score);
                }else {
                    setScore(lbP2Science, card.score);
                }
            }
            case RED_MILITARY ->{
                if (cardButton.getParent() == fpPlayerOnePlayedCard)
                {
                    setScore(lbP1Military, card.score);
                }else {
                    setScore(lbP2Military, card.score);
                }
            }
            case GREY_TRADE ->{
                if (cardButton.getParent() == fpPlayerOnePlayedCard)
                {
                    setScore(lbP1Trade, card.score);
                }else {
                    setScore(lbP2Trade, card.score);
                }
            }
            case BROWN_RESOURCE ->{
                if (cardButton.getParent() == fpPlayerOnePlayedCard)
                {
                    setScore(lbP1Resource, card.score);
                } else {
                    setScore(lbP2Resource, card.score);
                }
            }
            case YELLOW_GOLD ->{
                if (cardButton.getParent() == fpPlayerOnePlayedCard)
                {
                    setScore(lbP1Gold, card.score);
                }else {
                    setScore(lbP2Gold, card.score);
                }
            }
        }
    }

    private void setScore(Label scoreLabel, int score) {
        int currResult = Integer.parseInt(scoreLabel.getText());
        scoreLabel.setText(String.valueOf(currResult + score));
    }


    private void checkRemainingCards() {
        if (fpPlayerOneCards.getChildren().isEmpty() && fpPlayerTwoCards.getChildren().isEmpty())
        {
            concludeGame();
        } else
        {
            swapDecks();
        }
    }

    private void swapDecks() {
        List<Node> helperList = new ArrayList<>(fpPlayerOneCards.getChildren());
        fpPlayerOneCards.getChildren().setAll(fpPlayerTwoCards.getChildren());
        fpPlayerTwoCards.getChildren().setAll(helperList);
    }

    private void concludeGame() {
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
}
