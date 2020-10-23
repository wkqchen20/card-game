package com.wkqchen20.game.landlords.core;

import com.wkqchen20.game.core.SellType;

public enum LSellType implements SellType {
    SINGLE("单个牌"),

    DOUBLE("对子牌"),

    THREE("三张牌"),

    THREE_ZONES_SINGLE("三带单"),

    SINGLE_STRAIGHT("单顺子"),

    DOUBLE_STRAIGHT("双顺子"),

    BOMB("炸弹"),

    KING_BOMB("王炸"),

    FOUR_ZONES_DOUBLE("四带对"),

    THREE_STRAIGHT_WITH_SINGLE("飞机带单牌"),

    THREE_STRAIGHT_WITH_DOUBLE("飞机带对牌"),

    ILLEGAL("非法"),

    ;

    public final String desc;

    LSellType(String desc) {
        this.desc = desc;
    }

}