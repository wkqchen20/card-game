package com.wkqchen20.game.landlords.core;

import com.google.common.collect.ImmutableBiMap;
import com.google.common.collect.Sets;
import com.wkqchen20.game.landlords.common.LCard;

import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import static com.wkqchen20.game.landlords.common.LCard.Value.VALUE_BIG_KING;
import static com.wkqchen20.game.landlords.common.LCard.Value.VALUE_SMALL_KING;
import static com.wkqchen20.game.landlords.core.LSellType.*;
import static io.vavr.API.*;
import static java.util.stream.Collectors.*;

/**
 * @Author: wkqchen20
 * @Date: 2020/10/12
 */
public class LSellCardsCalculates {

    private List<LCard> cards;
    private int score;
    private LSellType type;

    public LSellCardsCalculates(List<LCard> cards) {
        this.cards = cards;
        this.resolveCards();
    }

    public static LSellCardsCalculates of(List<LCard> orderedCards) {
        return new LSellCardsCalculates(orderedCards);
    }

    private void resolveCards() {
        int size = cards.size();
        Match(size).of(
                Case($(1), () -> {
                    type = SINGLE;
                    score = cards.get(0).getValue().val;
                    return null;
                }),
                Case($(2), () -> {
                    if (cards.get(0).getValue() == cards.get(1).getValue()) {
                        type = DOUBLE;
                        score = cards.get(0).getValue().val * 2;
                    } else if (cards.get(0).getValue() == VALUE_SMALL_KING && cards.get(1).getValue() == VALUE_BIG_KING) {
                        type = KING_BOMB;
                        score = Integer.MAX_VALUE;
                    } else {
                        type = ILLEGAL;
                    }
                    return null;
                }),
                Case($(3), () -> {
                    LCard.Value value = cards.get(0).getValue();
                    if (cards.stream().allMatch(c -> c.getValue() == value)) {
                        type = THREE;
                        score = value.val * 3;
                    } else {
                        type = ILLEGAL;
                    }
                    return null;
                }),
                Case($(4), () -> {
                    LCard.Value value = cards.get(0).getValue();
                    if (cards.stream().allMatch(c -> c.getValue() == value)) {
                        type = BOMB;
                        score = value.val * 4 * 100;
                        return null;
                    }
                    Map<LCard.Value, Long> val2count = cards.stream().map(LCard::getValue).collect(groupingBy(val -> val, counting()));
                    if (val2count.size() == 2 && val2count.containsValue(Long.valueOf(1)) && val2count.containsValue(Long.valueOf(3))) {
                        ImmutableBiMap<LCard.Value, Long> biMap = ImmutableBiMap.copyOf(val2count);
                        LCard.Value specialCardVal = biMap.inverse().get(Long.valueOf(3));
                        type = THREE_ZONES_SINGLE;
                        score = specialCardVal.val * 3;
                    } else {
                        type = ILLEGAL;
                    }
                    return null;
                }),
                Case($(), () -> {
                    List<LCard.Value> cardVals = cards.stream().map(LCard::getValue).distinct().collect(toList());
                    Map<LCard.Value, Long> val2count = cards.stream().map(LCard::getValue).collect(groupingBy(val -> val, counting()));
                    if (cardVals.size() == size) {
                        // 顺子
                        return setSingleStraight(cardVals);
                    }
                    if (cardVals.size() == size / 2 && val2count.values().stream().allMatch(val -> val == 2)) {
                        // 连对
                        return setDoubleStraight(cardVals);
                    }
                    if (size == 6) {
                        // 四带二
                        return setFourZonesDouble(val2count);
                    }
                    if (size >= 8 && size % 4 == 0) {
                        // 飞机带单
                        return setThreeStraightWithSingle(val2count);
                    }
                    if (size >= 10 && size % 5 == 0) {
                        // 飞机带双
                        return setThreeStraightWithDouble(val2count);
                    }
                    type = ILLEGAL;
                    return null;
                })
        );
    }

    private Void setSingleStraight(List<LCard.Value> cardVals) {
        if (isSeq(cardVals)) {
            type = SINGLE_STRAIGHT;
            score = cardVals.stream().mapToInt(v -> v.val).sum();
        } else {
            type = ILLEGAL;
        }
        return null;
    }

    private Void setDoubleStraight(List<LCard.Value> cardVals) {
        if (isSeq(cardVals)) {
            type = DOUBLE_STRAIGHT;
            score = cardVals.stream().mapToInt(v -> v.val).sum() * 2;
        } else {
            type = ILLEGAL;
        }
        return null;
    }

    private Void setFourZonesDouble(Map<LCard.Value, Long> val2count) {
        if (val2count.size() == 2 && val2count.containsValue(Long.valueOf(2)) && val2count.containsValue(Long.valueOf(4))) {
            type = FOUR_ZONES_DOUBLE;
            ImmutableBiMap<Long, LCard.Value> biMap = ImmutableBiMap.copyOf(val2count).inverse();
            LCard.Value value = biMap.get(Long.valueOf(4));
            score = value.val * 4;
        } else {
            type = ILLEGAL;
        }
        return null;
    }

    private Void setThreeStraightWithSingle(Map<LCard.Value, Long> val2count) {
        Set<LCard.Value> specialThree = Sets.newHashSetWithExpectedSize(2);
        Set<LCard.Value> specialOne = Sets.newHashSetWithExpectedSize(2);
        for (Map.Entry<LCard.Value, Long> entry : val2count.entrySet()) {
            if (entry.getValue() == 3) {
                specialThree.add(entry.getKey());
            } else if (entry.getValue() == 1) {
                specialOne.add(entry.getKey());
            }
        }
        if (specialThree.size() > 0
                && specialThree.size() == specialOne.size()
                && isSeq(specialThree.stream().sorted(Comparator.comparingInt(v -> v.val)).collect(toList()))) {
            type = THREE_STRAIGHT_WITH_SINGLE;
            score = specialThree.stream().mapToInt(v -> v.val).sum() * 3;
            return null;
        }
        type = ILLEGAL;
        return null;
    }

    private Void setThreeStraightWithDouble(Map<LCard.Value, Long> val2count) {
        Set<LCard.Value> specialThree = Sets.newHashSetWithExpectedSize(2);
        Set<LCard.Value> specialTwo = Sets.newHashSetWithExpectedSize(2);
        for (Map.Entry<LCard.Value, Long> entry : val2count.entrySet()) {
            if (entry.getValue() == 3) {
                specialThree.add(entry.getKey());
            } else if (entry.getValue() == 2) {
                specialTwo.add(entry.getKey());
            }
        }
        if (specialThree.size() > 0
                && specialThree.size() == specialTwo.size()
                && isSeq(specialThree.stream().sorted(Comparator.comparingInt(v -> v.val)).collect(toList()))) {
            type = THREE_STRAIGHT_WITH_DOUBLE;
            score = specialThree.stream().mapToInt(v -> v.val).sum() * 3;
            return null;
        }
        type = ILLEGAL;
        return null;
    }

    public int getScore() {
        return score;
    }

    public LSellType getType() {
        return type;
    }

    public List<LCard> getCards() {
        return cards;
    }

    /**
     * 是否是顺序的
     *
     * @return
     */
    private static boolean isSeq(List<LCard.Value> orderedList) {
        LCard.Value val = null;
        for (int i = 0; i < orderedList.size(); i++) {
            LCard.Value current = orderedList.get(i);
            if (i != 0) {
                if (val.val != current.val - 1) {
                    return false;
                }
            }
            val = current;
        }
        return true;
    }
}
