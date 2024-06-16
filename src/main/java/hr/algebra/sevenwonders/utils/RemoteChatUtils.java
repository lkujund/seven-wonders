package hr.algebra.sevenwonders.utils;

import hr.algebra.sevenwonders.chat.RemoteChatService;
import hr.algebra.sevenwonders.chat.RemoteChatServiceImpl;
import hr.algebra.sevenwonders.model.ConfigurationKey;

import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.rmi.server.UnicastRemoteObject;

public class RemoteChatUtils {
    public static RemoteChatService startRmiRemoteChatServer() {
        RemoteChatService remoteChatService = new RemoteChatServiceImpl();
        Integer rmiPort = ConfigurationReader.readIntegerConfigurationValue(ConfigurationKey.RMI_PORT);
        Integer randomPortHint = ConfigurationReader.readIntegerConfigurationValue(ConfigurationKey.RANDOM_PORT_HINT);

        try {
            Registry registry = LocateRegistry.createRegistry(rmiPort);
            RemoteChatService skeleton = (RemoteChatService) UnicastRemoteObject.exportObject(remoteChatService,
                    randomPortHint);
            registry.rebind(RemoteChatService.REMOTE_CHAT_OBJECT_NAME, skeleton);
            System.err.println("Object registered in RMI registry");
        } catch (RemoteException e) {
            e.printStackTrace();
        }

        return remoteChatService;
    }

    public static RemoteChatService startRmiRemoteChatClient() {
        Integer rmiPort = ConfigurationReader.readIntegerConfigurationValue(ConfigurationKey.RMI_PORT);
        String host = ConfigurationReader.readStringConfigurationValue(ConfigurationKey.HOST);
        RemoteChatService remoteChatService = null;

        try {
            Registry registry = LocateRegistry.getRegistry(host, rmiPort);
            remoteChatService = (RemoteChatService) registry.lookup(
                    RemoteChatService.REMOTE_CHAT_OBJECT_NAME);
        } catch (RemoteException | NotBoundException e) {
            e.printStackTrace();
        }

        return  remoteChatService;
    }
}
