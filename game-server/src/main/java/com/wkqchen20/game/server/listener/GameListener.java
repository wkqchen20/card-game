package com.wkqchen20.game.server.listener;

import com.google.common.collect.Lists;
import com.wkqchen20.game.common.Card;
import com.wkqchen20.game.common.util.Reflections;
import com.wkqchen20.game.core.Game;
import com.wkqchen20.game.core.Status;
import io.vavr.control.Try;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;
import java.util.Set;

/**
 * @Author: wkqchen20
 * @Date: 2020/10/15
 */
public class GameListener<S extends Status, C extends Card, G extends Game<S, C>> implements Game.GameListener {

    public static final GameListener LISTENER = new GameListener();
    private static final Logger logger = LoggerFactory.getLogger(GameListener.class);
    private static List<GameLifeCycleHandler> handlers = Lists.newArrayList();

    static {
        org.reflections.Reflections reflections = Reflections.getReflections();
        Set<Class<? extends GameLifeCycleHandler>> classSet = reflections.getSubTypesOf(GameLifeCycleHandler.class);
        classSet.stream().forEach(clazz -> {
            Try.run(() -> {
                GameLifeCycleHandler handler = clazz.newInstance();
                handlers.add(handler);
                logger.info("instance handler:" + clazz.getName());
            }).onFailure(ex -> {
                throw new IllegalStateException("can not instance class:" + clazz.getName(), ex);
            });
        });
    }

    private GameListener() {
    }

    @Override
    public void onStatusChanged(Game game) {
        handlers.stream().filter(handler -> handler.isSupport(game)).forEach(handler -> {
            handler.doStatusChanged(game);
        });
    }

}
