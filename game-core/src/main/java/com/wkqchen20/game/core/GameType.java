package com.wkqchen20.game.core;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.Lists;
import com.wkqchen20.game.common.Card;
import io.vavr.control.Try;
import org.reflections.Reflections;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;
import java.util.Set;

/**
 * 游戏类型
 *
 * @Author: wkqchen20
 * @Date: 2020/9/10
 */
public interface GameType<C extends Card, S extends SellType> {

    String name();

    int allowPlayerCount();

    DistributeAndRemainCardsTuple<C> distributeCards(List<C> cards);

    boolean supportSellType(S sellType);

    class GameTypes {

        private static final Logger logger = LoggerFactory.getLogger(GameTypes.class);

        private static final List<GameType> GAME_TYPES = Lists.newArrayList();

        static {
            Reflections reflections = com.wkqchen20.game.common.util.Reflections.getReflections();
            Set<Class<? extends GameType>> gameTypes = reflections.getSubTypesOf(GameType.class);
            gameTypes.stream().forEach(clazz -> {
                Try.run(() -> {
                    GAME_TYPES.add(clazz.newInstance());
                    logger.info("init game type:" + clazz.getName());
                }).onFailure(ex -> {
                    throw new IllegalStateException("can not instance GameType:" + clazz.getName(), ex);
                });
            });
        }

        private GameTypes() {
        }

        public static List<GameType> gameTypes() {
            return ImmutableList.copyOf(GAME_TYPES);
        }
    }

    class DistributeAndRemainCardsTuple<C extends Card> {
        private List<List<C>> distributeCardsList;
        private List<C> remainCards;

        public DistributeAndRemainCardsTuple(List<List<C>> distributeCardsList, List<C> remainCards) {
            this.distributeCardsList = distributeCardsList;
            this.remainCards = remainCards;
        }

        public List<List<C>> getDistributeCardsList() {
            return distributeCardsList;
        }

        public List<C> getRemainCards() {
            return remainCards;
        }
    }
}
