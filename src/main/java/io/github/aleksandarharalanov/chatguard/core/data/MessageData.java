package io.github.aleksandarharalanov.chatguard.core.data;

import java.util.HashMap;
import java.util.LinkedList;

public final class MessageData {

    private static final HashMap<String, LinkedList<String>> playerMessages = new HashMap<>();

    private MessageData() {}

    public static HashMap<String, LinkedList<String>> getPlayerMessages() {
        return playerMessages;
    }

    public static LinkedList<String> getMessageHistory(String playerName) {
        return playerMessages.computeIfAbsent(playerName, k -> new LinkedList<>());
    }
}
