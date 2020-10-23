package com.wkqchen20.game.landlords.core;

import com.google.common.collect.Lists;
import org.junit.jupiter.api.Test;

import static com.wkqchen20.game.landlords.common.LCard.Color.*;
import static com.wkqchen20.game.landlords.common.LCard.Value.*;
import static com.wkqchen20.game.landlords.common.LCard.*;
import static org.junit.jupiter.api.Assertions.assertEquals;

class LSellCardsCalculatesTest {

    @Test
    public void shouldBeSingleTypeWhenOneCard() {
        LSellCardsCalculates data = LSellCardsCalculates.of(Lists.newArrayList(bigKing()));
        assertEquals(data.getType(), LSellType.SINGLE);
    }

    @Test
    public void shouldBeDoubleTypeWhenPairCard() {
        LSellCardsCalculates data = LSellCardsCalculates.of(Lists.newArrayList(of(VALUE_2, HEART), of(VALUE_2, BLACK)));
        assertEquals(data.getType(), LSellType.DOUBLE);
    }

    @Test
    public void shouldBeKingBombTypeWhenKing() {
        LSellCardsCalculates data = LSellCardsCalculates.of(Lists.newArrayList(smallKing(), bigKing()));
        assertEquals(data.getType(), LSellType.KING_BOMB);
    }

    @Test
    public void shouldBeIllegalTypeWhenKing() {
        LSellCardsCalculates data = LSellCardsCalculates.of(Lists.newArrayList(bigKing(), of(VALUE_2, BLACK)));
        assertEquals(data.getType(), LSellType.ILLEGAL);
    }

    @Test
    public void shouldBeThreeTypeWhenThreeSameValCards() {
        LSellCardsCalculates data = LSellCardsCalculates.of(Lists.newArrayList(of(VALUE_2, HEART), of(VALUE_2, BLACK), of(VALUE_2, SQUARE)));
        assertEquals(data.getType(), LSellType.THREE);
    }

    @Test
    public void shouldBeIllegalTypeWhenNotThreeSameValCards() {
        LSellCardsCalculates data = LSellCardsCalculates.of(Lists.newArrayList(bigKing(), of(VALUE_2, BLACK), of(VALUE_2, SQUARE)));
        assertEquals(data.getType(), LSellType.ILLEGAL);
    }

    @Test
    public void shouldBeBombTypeWhenFourSameValCards() {
        LSellCardsCalculates data = LSellCardsCalculates.of(Lists.newArrayList(of(VALUE_2, HEART), of(VALUE_2, BLACK), of(VALUE_2, PLUM), of(VALUE_2, SQUARE)));
        assertEquals(data.getType(), LSellType.BOMB);
    }

    @Test
    public void shouldBeThreeZonesSingleTypeWhenThreeSameValCardsWithOneOtherCard() {
        LSellCardsCalculates data = LSellCardsCalculates.of(Lists.newArrayList(of(VALUE_2, HEART), of(VALUE_2, BLACK), of(VALUE_2, PLUM), smallKing()));
        assertEquals(data.getType(), LSellType.THREE_ZONES_SINGLE);
    }

    @Test
    public void shouldBeIllegalTypeWhenNotFourSameValCard() {
        LSellCardsCalculates data = LSellCardsCalculates.of(Lists.newArrayList(of(VALUE_2, HEART), of(VALUE_3, BLACK), of(VALUE_8, PLUM), smallKing()));
        assertEquals(data.getType(), LSellType.ILLEGAL);
    }

    @Test
    public void shouldBeSingleStraightType() {
        LSellCardsCalculates data = LSellCardsCalculates.of(Lists.newArrayList(
                of(VALUE_3, BLACK),
                of(VALUE_4, PLUM),
                of(VALUE_5, PLUM),
                of(VALUE_6, PLUM),
                of(VALUE_7, HEART),
                of(VALUE_8, PLUM)));
        assertEquals(data.getType(), LSellType.SINGLE_STRAIGHT);
    }

    @Test
    public void shouldBeIllegalType() {
        LSellCardsCalculates data = LSellCardsCalculates.of(Lists.newArrayList(
                of(VALUE_2, BLACK),
                of(VALUE_3, BLACK),
                of(VALUE_4, PLUM),
                of(VALUE_5, PLUM),
                of(VALUE_6, PLUM),
                of(VALUE_7, HEART),
                of(VALUE_8, PLUM)));
        assertEquals(data.getType(), LSellType.ILLEGAL);
    }

    @Test
    public void shouldBeDoubleStraightType() {
        LSellCardsCalculates data = LSellCardsCalculates.of(Lists.newArrayList(
                of(VALUE_3, HEART),
                of(VALUE_3, BLACK),
                of(VALUE_4, PLUM),
                of(VALUE_4, HEART),
                of(VALUE_5, HEART),
                of(VALUE_5, PLUM),
                of(VALUE_6, HEART),
                of(VALUE_6, PLUM)));
        assertEquals(data.getType(), LSellType.DOUBLE_STRAIGHT);
    }

    @Test
    public void shouldBeIllegalType2() {
        LSellCardsCalculates data = LSellCardsCalculates.of(Lists.newArrayList(
                of(VALUE_2, HEART),
                of(VALUE_2, PLUM),
                of(VALUE_3, BLACK),
                of(VALUE_3, BLACK),
                of(VALUE_4, PLUM),
                of(VALUE_4, HEART),
                of(VALUE_5, HEART),
                of(VALUE_5, PLUM),
                of(VALUE_6, HEART),
                of(VALUE_6, PLUM)));
        assertEquals(data.getType(), LSellType.ILLEGAL);
    }

    @Test
    public void shouldBeFourZonesDoubleType() {
        LSellCardsCalculates data = LSellCardsCalculates.of(Lists.newArrayList(
                of(VALUE_2, HEART),
                of(VALUE_2, SQUARE),
                of(VALUE_2, BLACK),
                of(VALUE_2, PLUM),
                of(VALUE_4, PLUM),
                of(VALUE_4, HEART)));
        assertEquals(data.getType(), LSellType.FOUR_ZONES_DOUBLE);
    }

    @Test
    public void shouldBeIllegalType3() {
        LSellCardsCalculates data = LSellCardsCalculates.of(Lists.newArrayList(
                of(VALUE_2, HEART),
                of(VALUE_2, SQUARE),
                of(VALUE_2, BLACK),
                of(VALUE_2, PLUM),
                of(VALUE_4, PLUM),
                of(VALUE_5, HEART)));
        assertEquals(data.getType(), LSellType.ILLEGAL);
    }

    @Test
    public void shouldBeThreeStraightOneType() {
        LSellCardsCalculates data = LSellCardsCalculates.of(Lists.newArrayList(
                of(VALUE_3, HEART),
                of(VALUE_3, SQUARE),
                of(VALUE_3, BLACK),
                of(VALUE_4, SQUARE),
                of(VALUE_4, PLUM),
                of(VALUE_4, HEART),
                of(VALUE_5, HEART),
                of(VALUE_6, HEART)
        ));
        assertEquals(data.getType(), LSellType.THREE_STRAIGHT_WITH_SINGLE);
    }

    @Test
    public void shouldBeIllegalType4() {
        LSellCardsCalculates data = LSellCardsCalculates.of(Lists.newArrayList(
                of(VALUE_3, HEART),
                of(VALUE_3, SQUARE),
                of(VALUE_3, BLACK),
                of(VALUE_5, SQUARE),
                of(VALUE_5, PLUM),
                of(VALUE_5, HEART),
                of(VALUE_6, HEART),
                of(VALUE_7, HEART)
        ));
        assertEquals(data.getType(), LSellType.ILLEGAL);
    }

    @Test
    public void shouldBeThreeStraightDoubleType() {
        LSellCardsCalculates data = LSellCardsCalculates.of(Lists.newArrayList(
                of(VALUE_3, HEART),
                of(VALUE_3, SQUARE),
                of(VALUE_3, BLACK),
                of(VALUE_4, SQUARE),
                of(VALUE_4, PLUM),
                of(VALUE_4, HEART),
                of(VALUE_5, HEART),
                of(VALUE_5, HEART),
                of(VALUE_6, HEART),
                of(VALUE_6, PLUM)
        ));
        assertEquals(data.getType(), LSellType.THREE_STRAIGHT_WITH_DOUBLE);
    }

    @Test
    public void shouldBeIllegalType5() {
        LSellCardsCalculates data = LSellCardsCalculates.of(Lists.newArrayList(
                of(VALUE_3, HEART),
                of(VALUE_3, SQUARE),
                of(VALUE_3, BLACK),
                of(VALUE_5, SQUARE),
                of(VALUE_5, PLUM),
                of(VALUE_5, HEART),
                of(VALUE_6, HEART),
                of(VALUE_6, HEART),
                of(VALUE_7, HEART),
                of(VALUE_7, HEART)
        ));
        assertEquals(data.getType(), LSellType.ILLEGAL);
    }


}