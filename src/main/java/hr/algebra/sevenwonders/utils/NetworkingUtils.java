package hr.algebra.sevenwonders.utils;

import hr.algebra.sevenwonders.model.ConfigurationKey;
import hr.algebra.sevenwonders.model.GameState;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;

public class NetworkingUtils {
    public static void sendRequestToServer(GameState gameState) {

        Integer serverPort = ConfigurationReader.readIntegerConfigurationValue(ConfigurationKey.SERVER_PORT);
        String host = ConfigurationReader.readStringConfigurationValue(ConfigurationKey.HOST);

        try (Socket clientSocket = new Socket(host, serverPort)) {
            System.err.println("Client is connecting to " + clientSocket.getInetAddress() + ":" +clientSocket.getPort());
            sendSerializableRequest(clientSocket, gameState);
        } catch (IOException | ClassNotFoundException e) {
            e.printStackTrace();
        }
    }

    public static void sendRequestToClient(GameState gameState) {

        Integer clientPort = ConfigurationReader.readIntegerConfigurationValue(ConfigurationKey.CLIENT_PORT);
        String host = ConfigurationReader.readStringConfigurationValue(ConfigurationKey.HOST);

        try (Socket clientSocket = new Socket(host, clientPort)) {
            System.err.println("Client is connecting to " + clientSocket.getInetAddress() + ":" +clientSocket.getPort());
            sendSerializableRequest(clientSocket, gameState);
        } catch (IOException | ClassNotFoundException e) {
            e.printStackTrace();
        }
    }

    private static void sendSerializableRequest(Socket client, GameState gameState) throws IOException, ClassNotFoundException {
        ObjectOutputStream oos = new ObjectOutputStream(client.getOutputStream());
        ObjectInputStream ois = new ObjectInputStream(client.getInputStream());
        oos.writeObject(gameState);
        System.out.println("Confirmation received: " + (String) ois.readObject());
    }

    public static void sendGameStateToServer(GameState gameState) {
        sendRequestToServer(gameState);
    }

    public static void sendGameStateToClient(GameState gameState) {
        sendRequestToClient(gameState);
    }
}
