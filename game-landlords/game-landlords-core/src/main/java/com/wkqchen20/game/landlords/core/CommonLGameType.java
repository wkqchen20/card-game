package com.wkqchen20.game.landlords.core;

import com.google.common.collect.Sets;
import com.wkqchen20.game.core.GameType;
import com.wkqchen20.game.landlords.common.LCard;

import java.util.Collections;
import java.util.List;
import java.util.Set;
import java.util.stream.IntStream;

import static java.util.stream.Collectors.toList;

/**
 * @Author: wkqchen20
 * @Date: 2020/10/9
 */
public class CommonLGameType implements GameType<LCard, LSellType> {

    private static final Set<LSellType> SUPPORTED_TYPE = Sets.newHashSet();

    @Override
    public String name() {
        return "经典斗地主";
    }

    @Override
    public int allowPlayerCount() {
        return 3;
    }

    @Override
    public DistributeAndRemainCardsTuple<LCard> distributeCards(List<LCard> cards) {
        Collections.shuffle(cards);
        List<List<LCard>> distributeCardsList = IntStream.range(0, 3)
                                                         .mapToObj(idx -> cards.subList(idx * 17, (idx + 1) * 17).stream().sorted().collect(toList()))
                                                         .collect(toList());
        List<LCard> remainCards = cards.subList(51, 54);
        return new DistributeAndRemainCardsTuple<>(distributeCardsList, remainCards);
    }

    @Override
    public boolean supportSellType(LSellType sellType) {
        return SUPPORTED_TYPE.contains(sellType);
    }

}
