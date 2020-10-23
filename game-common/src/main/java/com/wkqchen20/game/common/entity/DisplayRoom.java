package com.wkqchen20.game.common.entity;

/**
 * @Author: wkqchen20
 * @Date: 2020/10/14
 */
public class DisplayRoom {

    private int id;
    private String name;
    private int allowPlayerCount;
    private int currentPlayerCount;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getAllowPlayerCount() {
        return allowPlayerCount;
    }

    public void setAllowPlayerCount(int allowPlayerCount) {
        this.allowPlayerCount = allowPlayerCount;
    }

    public int getCurrentPlayerCount() {
        return currentPlayerCount;
    }

    public void setCurrentPlayerCount(int currentPlayerCount) {
        this.currentPlayerCount = currentPlayerCount;
    }
}
