package com.wkqchen20.game.core;


import com.wkqchen20.game.common.Card;

import java.util.List;

/**
 * @Author: wkqchen20
 * @Date: 2020/9/10
 */
public interface Game<S extends Status, C extends Card> {

    int id();

    GameType gameType();

    boolean isPlaying();

    void start();

    /**
     * 当前轮到出牌的玩家
     *
     * @return
     */
    Player currentPlayerToPlay();

    /**
     * 是否轮到玩家出牌
     *
     * @param player
     * @return
     */
    boolean isPlayerTurn(Player player);

    /**
     * 出牌人流转
     *
     * @return
     */
    Player turnToNext();

    /**
     * 玩家列表
     *
     * @return
     */
    List<PlayerWithCards<C>> playerWithCards();

    /**
     * 保存玩家行为
     *
     * @param playerBehavior
     */
    void addBehavior(PlayerBehavior playerBehavior);

    /**
     * 玩家行为信息
     *
     * @return
     */
    List<? extends PlayerBehavior> playerBehaviors();

    void addListener(GameListener listener);

    /**
     * 游戏状态
     * @param
     * @return
     */
    S status();

    interface GameListener {

        void onStatusChanged(Game game);
    }
}
