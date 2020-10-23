package com.wkqchen20.game.landlords.core;

import com.wkqchen20.game.core.Player;
import com.wkqchen20.game.core.PlayerBehavior;
import com.wkqchen20.game.landlords.core.command.SellCardsCommandData;

/**
 * 斗地主行为
 *
 * @Author: wkqchen20
 * @Date: 2020/10/9
 */
public class LPlayerBehavior extends PlayerBehavior {

    private final SellCardsCommandData data;
    private final BehaviorStatus status;

    private LPlayerBehavior(Player player, SellCardsCommandData data, BehaviorStatus status) {
        super(player);
        this.data = data;
        this.status = status;
    }

    /**
     * 出牌
     *
     * @param player
     * @param data
     * @return
     */
    public static LPlayerBehavior sell(Player player, SellCardsCommandData data) {
        return new LPlayerBehavior(player, data, BehaviorStatus.SELL);
    }

    /**
     * 过
     *
     * @param player
     * @return
     */
    public static LPlayerBehavior pass(Player player) {
        return new LPlayerBehavior(player, null, BehaviorStatus.PASS);
    }

    /**
     * 抢地主
     *
     * @param player
     * @return
     */
    public static LPlayerBehavior elect(Player player) {
        return new LPlayerBehavior(player, null, BehaviorStatus.ELECT);
    }

    public static LPlayerBehavior notElect(Player player) {
        return new LPlayerBehavior(player, null, BehaviorStatus.NOT_ELECT);
    }

    public SellCardsCommandData getData() {
        return data;
    }

    public BehaviorStatus getStatus() {
        return status;
    }

    public boolean isElection() {
        return status == BehaviorStatus.ELECT || status == BehaviorStatus.NOT_ELECT;
    }
}
