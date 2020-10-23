package com.wkqchen20.game.core;

/**
 * 交互命令
 *
 * @Author: wkqchen20
 * @Date: 2020/9/11
 */
public interface Command {

    /**
     * 命令执行
     *
     * @param player
     * @param data
     * @return
     */
    ExecuteResult execute(Player player, String data);
}
