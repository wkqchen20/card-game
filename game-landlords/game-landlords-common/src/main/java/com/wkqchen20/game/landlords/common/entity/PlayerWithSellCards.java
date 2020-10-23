package com.wkqchen20.game.landlords.common.entity;

import com.wkqchen20.game.common.entity.DisplayPlayer;
import com.wkqchen20.game.landlords.common.LCard;

import java.util.List;

/**
 * @Author: wkqchen20
 * @Date: 2020/10/22
 */
public class PlayerWithSellCards {
    private DisplayPlayer player;
    private List<LCard> cards;

    public DisplayPlayer getPlayer() {
        return player;
    }

    public void setPlayer(DisplayPlayer player) {
        this.player = player;
    }

    public List<LCard> getCards() {
        return cards;
    }

    public void setCards(List<LCard> cards) {
        this.cards = cards;
    }
}
