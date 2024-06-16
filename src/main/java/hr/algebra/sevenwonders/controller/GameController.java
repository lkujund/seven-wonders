package hr.algebra.sevenwonders.controller;

import hr.algebra.sevenwonders.GameApplication;
import hr.algebra.sevenwonders.chat.service.RemoteChatService;
import hr.algebra.sevenwonders.model.Card;
import hr.algebra.sevenwonders.model.GameMove;
import hr.algebra.sevenwonders.model.GameState;
import hr.algebra.sevenwonders.model.UserRole;
import hr.algebra.sevenwonders.thread.ChatThread;
import hr.algebra.sevenwonders.thread.GetLastGameMoveThread;
import hr.algebra.sevenwonders.utils.*;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.input.KeyCode;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.FlowPane;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Stream;

public class GameController {

    public static GameController theGameController;

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

    //meni elementi
    @FXML
    public MenuItem miStartGame;
    @FXML
    public MenuItem miLoadGame;
    @FXML
    public MenuItem miSaveGame;
    @FXML
    public MenuItem miReplayGame;


    private RemoteChatService remoteChatService;

    public RemoteChatService getChatServiceInstance(){ return  remoteChatService; }

    public List<Label> p1Scores;
    public List<Label> p2Scores;

    public GameController() {
        theGameController = GameController.this;
    }


    public void initialize(){
        if (GameApplication.activeUserRole == UserRole.CLIENT || GameApplication.activeUserRole == UserRole.SERVER){
            ChatThread chatThread
                    = new ChatThread();
            Thread thread = new Thread(chatThread);
            thread.start();
            if (GameApplication.activeUserRole == UserRole.CLIENT){
                remoteChatService = RmiRemoteChatUtils.startRmiRemoteChatClient();
                disableFeaturesForClient();
            } else {
                remoteChatService = RmiRemoteChatUtils.startRmiRemoteChatServer();
                disableFeaturesForServer();
            }
        tfMessage.setOnKeyPressed(event -> {
                    if (event.getCode().equals(KeyCode.ENTER)) {
                        sendChatMessage();
                    }
                }
        );
        } else {
            disableChatForSinglePlayer();
        }
    }

    private void disableChatForSinglePlayer() {
        taChatBox.setDisable(true);
        tfMessage.setDisable(true);
        btnSendMessage.setDisable(true);
    }

    private void disableFeaturesForServer() {
        miLoadGame.setDisable(true);
        miSaveGame.setDisable(true);
        miReplayGame.setDisable(true);
        fpPlayerTwoCards.setVisible(false);

    }

    private void disableFeaturesForClient() {
        miStartGame.setDisable(true);
        miLoadGame.setDisable(true);
        miSaveGame.setDisable(true);
        miReplayGame.setDisable(true);
        fpPlayerOneCards.setVisible(false);
    }

    public void startGame() {

        if (GameApplication.activeUserRole == UserRole.SINGLEPLAYER){
            GetLastGameMoveThread getLastGameMoveThread
                    = new GetLastGameMoveThread(theLastGameMoveLabel);
            Thread starterThread = new Thread(getLastGameMoveThread);
            starterThread.start();
            XmlUtils.createNewReplayFile();
        }
        p1Scores = Arrays.asList(lbP1Civil, lbP1Science, lbP1Military, lbP1Trade, lbP1Resource, lbP1Gold, lbP1Total);
        p2Scores = Arrays.asList(lbP2Civil, lbP2Science, lbP2Military, lbP2Trade, lbP2Resource, lbP2Gold, lbP2Total);
        GameUtils.setupBoard(p1Scores, p2Scores, fpPlayerOneCards, fpPlayerTwoCards, lbWinner, GameController.this);
        if (GameApplication.activeUserRole == UserRole.SERVER){
            GameState gameStateSnapshot = GameStateUtils.createGameStateSnapshot(GameController.this);
            NetworkingUtils.sendGameStateToClient(gameStateSnapshot);
        }
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
        ChatUtils.sendChatMessage(tfMessage, remoteChatService, taChatBox);
    }


    public void cardClicked(MouseEvent mouseEvent) {

        Button button = (Button) mouseEvent.getSource();
        GameMove gameMove = new GameMove();


        if (button.getParent() == fpPlayerOneCards)
        {
            if (mouseEvent.getButton() == MouseButton.PRIMARY){
                GameUtils.playCard(button, fpPlayerOneCards, fpPlayerOnePlayedCard, lbP1Gold);
            } else if (mouseEvent.getButton() == MouseButton.SECONDARY){
                GameUtils.discard(button, fpPlayerOneCards, fpPlayerOnePlayedCard, lbP1Gold);
            }
            if (GameApplication.activeUserRole == UserRole.SINGLEPLAYER){
                gameMove.setPlayerName("P1");
                gameMove.setPlayerHandState((fpPlayerOneCards
                        .getChildren())
                        .stream()
                        .map(x -> (Button) x)
                        .map(b -> (Card) b.getUserData())
                        .toArray(Card[]::new));
            }
        }
        else if (button.getParent() == fpPlayerTwoCards)
        {
            if (mouseEvent.getButton() == MouseButton.PRIMARY) {
                GameUtils.playCard(button, fpPlayerTwoCards, fpPlayerTwoPlayedCard, lbP2Gold);
            } else if (mouseEvent.getButton() == MouseButton.SECONDARY){
                GameUtils.discard(button, fpPlayerTwoCards, fpPlayerTwoPlayedCard, lbP2Gold);
            }
            if (GameApplication.activeUserRole == UserRole.SINGLEPLAYER){
                gameMove.setPlayerName("P2");
                gameMove.setPlayerHandState((fpPlayerTwoCards
                        .getChildren())
                        .stream()
                        .map(x -> (Button) x)
                        .map(b -> (Card) b.getUserData())
                        .toArray(Card[]::new));
            }
        }
        if (GameApplication.activeUserRole == UserRole.SINGLEPLAYER){
            gameMove.setCard((Card) button.getUserData());
            gameMove.setDateTime(LocalDateTime.now());
            List<Label> scores = Stream.concat(p1Scores.stream(), p2Scores.stream()).toList();
            gameMove.setScoreBoardState(scores.stream().map(Label::getText).toArray(String[]::new));
            GameMoveUtils.saveGameMove(gameMove);
            XmlUtils.saveGameMove(gameMove);
        }

        if (GameApplication.activeUserRole == UserRole.SERVER){
            checkPlayedCards();
            GameState gameStateSnapshot = GameStateUtils.createGameStateSnapshot(theGameController);
            NetworkingUtils.sendGameStateToClient(gameStateSnapshot);
        }
        else if (GameApplication.activeUserRole == UserRole.SINGLEPLAYER){
            checkPlayedCards();
        }
        else if (GameApplication.activeUserRole == UserRole.CLIENT){
            GameState gameStateSnapshot = GameStateUtils.createGameStateSnapshot(theGameController);
            NetworkingUtils.sendGameStateToServer(gameStateSnapshot);
        }
    }

    public void checkPlayedCards() {
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



    public void checkRemainingCards() {
        if (fpPlayerOneCards.getChildren().isEmpty() && fpPlayerTwoCards.getChildren().isEmpty())
        {
            List<Label> scores = Stream.concat(p1Scores.stream(), p2Scores.stream()).toList();
            if (GameApplication.activeUserRole == UserRole.SINGLEPLAYER){
                GameMove lastGameMove = new GameMove(){{
                    setScoreBoardState(scores.stream().map(Label::getText).toArray(String[]::new));
                    setDateTime(LocalDateTime.now());
                }};
                XmlUtils.saveGameMove(lastGameMove);
            }

            GameUtils.concludeGame(p1Scores, lbP1Total, p2Scores, lbP2Total, lbWinner);
        } else
        {
            GameUtils.swapDecks(fpPlayerOneCards, fpPlayerTwoCards);
        }
    }
}
