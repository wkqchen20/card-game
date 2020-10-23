package com.wkqchen20.game.core;

import com.google.common.base.Preconditions;
import com.google.common.collect.ImmutableList;
import com.google.common.collect.Lists;
import com.wkqchen20.game.common.constant.Errors;
import io.vavr.control.Either;

import java.util.List;

/**
 * @Author: wkqchen20
 * @Date: 2020/9/10
 */
public abstract class Room {

    /**
     * 房间允许空间的时间 100s
     */
    private static final int IDLE_ALLOW_MAX_MILL_SECONDS = 100_1000;
    private final int id;
    private final String name;
    private final List<Player> players;
    private final HumanPlayer createdPlayer;
    private long idleTime;

    public Room(int id, String name, HumanPlayer createdPlayer) {
        this.id = id;
        this.name = name;
        this.createdPlayer = createdPlayer;
        this.players = Lists.newArrayListWithCapacity(4);
        players.add(createdPlayer);
    }

    public List<Player> getPlayers() {
        return ImmutableList.copyOf(players);
    }

    public boolean isEmpty() {
        return players.size() == 0;
    }

    public Either<Errors, Void> addPlayer(Player player) {
        Preconditions.checkNotNull(player, "player should not be null");
        if (players.size() == getGame().gameType().allowPlayerCount()) {
            return Either.left(Errors.ROOM_PLAYER_FULL);
        }
        if (players.contains(player)) {
            return Either.right(null);
        }
        players.add(player);
        return Either.right(null);
    }

    public void removePlayer(Player player) {
        players.remove(player);
    }

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public boolean canCleanResource() {
        Game game = getGame();
        if (game.isPlaying()) {
            return false;
        }
        if (players.isEmpty()) {
            return true;
        }
        if (System.currentTimeMillis() - idleTime >= IDLE_ALLOW_MAX_MILL_SECONDS) {
            return true;
        }
        return false;
    }

    public Game startGame() {
        Game g = getGame();
        g.start();
        return g;
    }

    public abstract Game getGame();
}
