package com.wkqchen20.game.core;

/**
 * @Author: wkqchen20
 * @Date: 2020/10/9
 */
public interface PlayCommand extends Command {

    ExecuteResult play(Player player, Game game, String data);
}
