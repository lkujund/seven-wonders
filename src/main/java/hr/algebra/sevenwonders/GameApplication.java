package hr.algebra.sevenwonders;

import hr.algebra.sevenwonders.controller.GameController;
import hr.algebra.sevenwonders.model.ConfigurationKey;
import hr.algebra.sevenwonders.model.GameState;
import hr.algebra.sevenwonders.model.UserRole;
import hr.algebra.sevenwonders.utils.*;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.time.LocalDateTime;

public class GameApplication extends Application {

    private static Scene mainScene;
    public static UserRole activeUserRole = UserRole.SINGLEPLAYER;

    @Override
    public void start(Stage stage) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(GameApplication.class.getResource("boardScreen.fxml"));
        Scene scene = new Scene(fxmlLoader.load(), 1440, 900);
        stage.setTitle("Seven Wonders - Luka Kujundzic - " + (activeUserRole != null ? activeUserRole.name() : "SINGLEPLAYER"));
        stage.setScene(scene);
        stage.setResizable(false);
        stage.show();
        mainScene = scene;
    }

    private static void startServer() {
        acceptRequestsOnServer();
    }

    private static void acceptRequestsOnServer() {

        Integer serverPort = ConfigurationReader.readIntegerConfigurationValue(ConfigurationKey.SERVER_PORT);

        try (ServerSocket serverSocket =
                     new ServerSocket(serverPort)) {
            System.err.println("Server listening on port: " + serverSocket.getLocalPort());

            while (true) {
                Socket clientSocket = serverSocket.accept();
                System.err.println("Client connected from port: " + clientSocket.getPort());
                Platform.runLater(() -> processSerializableClient(clientSocket));
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static void startClient() {
        acceptRequestsOnClient();
    }

    private static void acceptRequestsOnClient() {

        Integer clientPort = ConfigurationReader.readIntegerConfigurationValue(ConfigurationKey.CLIENT_PORT);

        try (ServerSocket serverSocket =
                     new ServerSocket(clientPort)) {
            System.err.println("Server listening on port: " + serverSocket.getLocalPort());

            while (true) {
                Socket clientSocket = serverSocket.accept();
                System.err.println("Client connected from port: " + clientSocket.getPort());
                Platform.runLater(() -> processSerializableClient(clientSocket));
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static void processSerializableClient(Socket clientSocket) {
        try (ObjectInputStream ois = new ObjectInputStream(clientSocket.getInputStream());
             ObjectOutputStream oos = new ObjectOutputStream(clientSocket.getOutputStream())) {
            GameState gameState = (GameState) ois.readObject();
            BoardRenderUtils.drawBoardFromGameState(gameState, GameController.theGameController);
            GameController.theGameController.checkPlayedCards();
            System.out.println("Game state received at " + LocalDateTime.now());
            oos.writeObject("Confirmation");
            if (GameApplication.activeUserRole == UserRole.SERVER){
                GameState gameStateSnapshot = GameStateUtils.createGameStateSnapshot(GameController.theGameController);
                NetworkingUtils.sendGameStateToClient(gameStateSnapshot);
            }
        } catch (IOException | ClassNotFoundException e) {
            e.printStackTrace();
        }
    }
    public static Scene getMainScene(){
        return mainScene;
    }

    public static void main(String[] args) {
        new Thread(Application::launch).start();

        String userRoleLoggedIn = args.length > 0 ? args[0] : "SINGLEPLAYER";
        Boolean userLoggedIn = false;

        for (UserRole userRole : UserRole.values()) {
            if (userRole.name().equals(userRoleLoggedIn)) {
                userLoggedIn = true;
                activeUserRole = userRole;
            }
        }

        if (userLoggedIn) {
            if (UserRole.SERVER.name().equals(activeUserRole.name())) {
                startServer();
            } else if (UserRole.CLIENT.name().equals(activeUserRole.name())) {
                startClient();
            }
        }
    }
}