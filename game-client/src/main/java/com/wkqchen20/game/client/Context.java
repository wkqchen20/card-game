package com.wkqchen20.game.client;

import com.wkqchen20.game.common.Card;
import com.wkqchen20.game.common.entity.DisplayPlayer;

import java.util.Collections;
import java.util.List;

import static java.util.stream.Collectors.toList;

/**
 * @Author: wkqchen20
 * @Date: 2020/10/14
 */
public final class Context {

    private static DisplayPlayer player;
    private static String gameType;
    private static String rooms;
    private static List<? extends Card> cards;

    private Context() {}

    public static DisplayPlayer getPlayer() {
        return player;
    }

    public static void setPlayer(DisplayPlayer player) {
        Context.player = player;
    }

    public static String getGameType() {
        return gameType;
    }

    public static void setGameType(String gameType) {
        Context.gameType = gameType;
    }

    public static String getRooms() {
        return rooms;
    }

    public static void setRooms(String rooms) {
        Context.rooms = rooms;
    }

    public static <R extends Card> List<R> getCards(Class<R> clazz) {
        if (cards == null) {
            return Collections.emptyList();
        }
        return cards.stream().map(c -> clazz.cast(c)).collect(toList());
    }

    public static void setCards(List<? extends Card> lCards) {
        Context.cards = lCards;
    }
}
