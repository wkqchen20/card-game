package com.wkqchen20.game.landlords.common;

import com.alibaba.fastjson.annotation.JSONCreator;
import com.alibaba.fastjson.annotation.JSONField;
import com.wkqchen20.game.common.Card;

import java.util.Comparator;
import java.util.Objects;
import java.util.function.ToIntFunction;

import static com.wkqchen20.game.landlords.common.LCard.Color.NONE_0;
import static com.wkqchen20.game.landlords.common.LCard.Color.NONE_1;

/**
 * @Author: wkqchen20
 * @Date: 2020/9/10
 */
public class LCard extends Card {

    private static final Comparator COMPARATOR = Comparator.comparingInt((ToIntFunction<LCard>) value -> value.value.val)
                                                           .thenComparingInt(value -> value.color.id);

    private final Value value;
    private final Color color;

    @JSONCreator
    private LCard(@JSONField(name = "value") Value value, @JSONField(name = "color") Color color) {
        this.value = value;
        this.color = color;
    }

    public static LCard of(Value value, Color color) {
        return new LCard(value, color);
    }

    public static LCard bigKing() {
        return new LCard(Value.VALUE_BIG_KING, NONE_1);
    }

    public static LCard smallKing() {
        return new LCard(Value.VALUE_SMALL_KING, NONE_0);
    }

    public Value getValue() {
        return value;
    }

    public Color getColor() {
        return color;
    }

    @Override
    public int compareTo(Object o) {
        return COMPARATOR.compare(this, o);
    }

    @Override
    public String toString() {
        return "lcard{" +
                "value=" + value +
                ", color=" + color +
                ")";
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        LCard that = (LCard) o;
        return value == that.value && color == that.color;
    }

    @Override
    public int hashCode() {
        return Objects.hash(value, color);
    }

    public enum Color {

        NONE_0(0, "X"),
        NONE_1(0, "D"),
        HEART(1, "♥"),
        SQUARE(2, "︎♦"),
        BLACK(3, "♠"),
        PLUM(4, "♣");

        public final int id;
        public final String display;

        Color(int id, String display) {
            this.id = id;
            this.display = display;
        }
    }

    public enum Value {

        VALUE_3(3, "3"),

        VALUE_4(4, "4"),

        VALUE_5(5, "5"),

        VALUE_6(6, "6"),

        VALUE_7(7, "7"),

        VALUE_8(8, "8"),

        VALUE_9(9, "9"),

        VALUE_10(10, "10"),

        VALUE_J(11, "J"),

        VALUE_Q(12, "Q"),

        VALUE_K(13, "K"),

        VALUE_A(14, "A"),

        VALUE_2(16, "2"),

        VALUE_SMALL_KING(18, "W"),

        VALUE_BIG_KING(20, "W"),
        ;

        public final int val;
        public final String display;

        Value(int val, String display) {
            this.val = val;
            this.display = display;
        }

    }

}
