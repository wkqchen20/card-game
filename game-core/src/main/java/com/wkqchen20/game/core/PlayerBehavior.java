package com.wkqchen20.game.core;

/**
 * @Author: wkqchen20
 * @Date: 2020/9/14
 */
public abstract class PlayerBehavior {

    protected final Player player;

    protected PlayerBehavior(Player player) {
        this.player = player;
    }

    public Player getPlayer() {
        return player;
    }

}
