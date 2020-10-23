package com.wkqchen20.game.common.command;

import org.reflections.Reflections;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Set;

public final class Commands {

    private static final Logger logger = LoggerFactory.getLogger(Commands.class);
    private static Set<Class<?>> commandClassSet;

    static {
        Reflections reflections = com.wkqchen20.game.common.util.Reflections.getReflections();
        commandClassSet = reflections.getTypesAnnotatedWith(CommandTag.class);
        commandClassSet.stream().forEach(clazz -> {
            logger.info("find command:" + clazz.getName());
        });
    }

    private Commands() {}

    public static Set<Class<?>> commandClassSet() {
        return commandClassSet;
    }

}
