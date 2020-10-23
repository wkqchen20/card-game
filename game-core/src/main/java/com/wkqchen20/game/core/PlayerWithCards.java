package com.wkqchen20.game.core;

import com.wkqchen20.game.common.Card;

import java.util.List;

/**
 * @Author: wkqchen20
 * @Date: 2020/9/12
 */
public class PlayerWithCards<C extends Card> {

    private final Player player;
    private final List<C> cards;

    public PlayerWithCards(Player player, List<C> cards) {
        this.player = player;
        this.cards = cards;
    }

    public Player getPlayer() {
        return player;
    }

    public List<C> getCards() {
        return cards;
    }
}
