package com.wkqchen20.game.common.entity;

import java.util.List;

/**
 * @Author: wkqchen20
 * @Date: 2020/10/14
 */
public class RoomDetail extends DisplayRoom {

    private List<DisplayPlayer> players;

    public List<DisplayPlayer> getPlayers() {
        return players;
    }

    public void setPlayers(List<DisplayPlayer> players) {
        this.players = players;
    }

    @Override
    public String toString() {
        return "RoomDetail{" +
                "players=" + players +
                '}';
    }
}
