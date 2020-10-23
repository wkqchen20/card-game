package com.wkqchen20.game.server;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.wkqchen20.game.core.Game;
import com.wkqchen20.game.core.GameType;
import com.wkqchen20.game.core.Player;
import com.wkqchen20.game.core.Room;
import com.wkqchen20.game.server.entity.ClientInfo;
import com.wkqchen20.game.server.flow.LandlordsFlow;
import io.netty.channel.Channel;
import org.squirrelframework.foundation.fsm.StateMachine;

import java.util.*;

/**
 * @Author: wkqchen20
 * @Date: 2020/10/13
 */
public class Context {

    private Context() {}

    private static Map<String, Player> CHANNEL_PLAYER_MAP = Maps.newConcurrentMap();
    private static Map<String, Channel> ID_CHANNEL_MAP = Maps.newConcurrentMap();
    private static Map<Integer, ClientInfo> PLAYER_CONTEXT_MAP = Maps.newConcurrentMap();
    private static Map<Integer, StateMachine> GAME_FLOW_MAP = Maps.newConcurrentMap();
    private static Map<GameType, List<Room>> GAME_TYPE_ROOMS_MAP = Maps.newConcurrentMap();

    public static void newPlayer(Player player, Channel channel) {
        String channelId = channelId(channel);
        ID_CHANNEL_MAP.put(channelId, channel);
        CHANNEL_PLAYER_MAP.put(channelId, player);
    }

    public static Player getPlayer(Channel channel) {
        String channelId = channelId(channel);
        return CHANNEL_PLAYER_MAP.get(channelId);
    }

    public static Channel getChannel(Player player) {
        Optional<String> optional = CHANNEL_PLAYER_MAP.entrySet()
                                                      .stream()
                                                      .filter(entry -> Objects.equals(entry.getValue(), player))
                                                      .map(Map.Entry::getKey)
                                                      .findFirst();
        // TODO
        String channelId = optional.get();
        return ID_CHANNEL_MAP.get(channelId);
    }

    public static void clean(Channel ch) {
        // TODO
        String channelId = channelId(ch);
        ID_CHANNEL_MAP.remove(channelId);
        CHANNEL_PLAYER_MAP.remove(channelId);

    }

    public static ClientInfo clientInfo(Player player) {
        PLAYER_CONTEXT_MAP.putIfAbsent(player.getId(), new ClientInfo());
        return PLAYER_CONTEXT_MAP.get(player.getId());
    }

    public static List<Room> rooms(GameType gameType) {
        if (!GAME_TYPE_ROOMS_MAP.containsKey(gameType)) {
            return Collections.emptyList();
        }
        return ImmutableList.copyOf(GAME_TYPE_ROOMS_MAP.get(gameType));
    }

    public static void newRoom(GameType gameType, Room room) {
        GAME_TYPE_ROOMS_MAP.putIfAbsent(gameType, Lists.newCopyOnWriteArrayList());
        GAME_TYPE_ROOMS_MAP.get(gameType).add(room);
    }

    public static void removeRoom(GameType gameType, Room room) {
        if (!GAME_TYPE_ROOMS_MAP.containsKey(gameType)) {
            return;
        }
        GAME_TYPE_ROOMS_MAP.get(gameType).remove(room);
    }

    public static String channelId(Channel channel) {
        return channel.id().asLongText();
    }

    public static StateMachine newFlow(Game game) {
        GAME_FLOW_MAP.put(game.id(), LandlordsFlow.landlordsFlow());
        return GAME_FLOW_MAP.get(game.id());
    }

    public static StateMachine fromGame(Game game) {
        return GAME_FLOW_MAP.get(game.id());
    }

}
