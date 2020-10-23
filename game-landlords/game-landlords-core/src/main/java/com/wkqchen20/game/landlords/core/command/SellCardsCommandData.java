package com.wkqchen20.game.landlords.core.command;

import com.wkqchen20.game.landlords.common.LCard;
import com.wkqchen20.game.landlords.core.LSellCardsCalculates;
import com.wkqchen20.game.landlords.core.LSellType;

import java.util.List;

/**
 * @Author: wkqchen20
 * @Date: 2020/10/10
 */
public class SellCardsCommandData {

    private final List<LCard> cards;
    private final LSellType type;
    private final int score;

    public SellCardsCommandData(List<LCard> cards, LSellType type, int score) {
        this.cards = cards;
        this.type = type;
        this.score = score;
    }

    public static SellCardsCommandData of(LSellCardsCalculates data) {
        return new SellCardsCommandData(data.getCards(), data.getType(), data.getScore());
    }

    public List<LCard> getCards() {
        return cards;
    }

    public LSellType getType() {
        return type;
    }

    public int getScore() {
        return score;
    }
}
