package hr.algebra.sevenwonders.utils;

import hr.algebra.sevenwonders.GameApplication;
import hr.algebra.sevenwonders.chat.service.RemoteChatService;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;

import java.rmi.RemoteException;
import java.util.List;

public class ChatUtils {

    public static void sendChatMessage(TextField chatMessageTextField, RemoteChatService remoteChatService,TextArea chatMessagesTextArea) {
        String chatMessage = chatMessageTextField.getText();

        try {
            remoteChatService.sendMessage(GameApplication.activeUserRole.name() + ": " + chatMessage);

            chatMessageTextField.clear();

            List<String> chatMessages = remoteChatService.getAllChatMessages();

            chatMessagesTextArea.clear();

            for (String message : chatMessages) {
                chatMessagesTextArea.appendText(message + "\n");
            }
        } catch (RemoteException e) {
            throw new RuntimeException(e);
        }
    }

    public static void updateChatMessages(RemoteChatService remoteChatService,TextArea chatMessagesTextArea){
        List<String> chatMessages = null;
        try {
            chatMessages = remoteChatService.getAllChatMessages();

            chatMessagesTextArea.clear();

            for (String message : chatMessages) {
                chatMessagesTextArea.appendText(message + "\n");
            }
        } catch (RemoteException e) {
            throw new RuntimeException(e);
        }
    }
}
