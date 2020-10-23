package com.wkqchen20.game.core;

import com.google.common.collect.Maps;
import com.wkqchen20.game.common.command.CommandTag;
import com.wkqchen20.game.common.command.Commands;
import com.wkqchen20.game.common.command.CommandNotFindException;
import io.vavr.control.Try;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Arrays;
import java.util.Map;
import java.util.Set;

/**
 * @Author: wkqchen20
 * @Date: 2020/10/9
 */
public final class ServerCommands {

    private static final Logger logger = LoggerFactory.getLogger(ServerCommands.class);
    private static Map<String, Command> commandMap = Maps.newHashMap();
    private static Map<String, PlayCommand> playCommandMap = Maps.newHashMap();

    static {
        Set<Class<?>> classSet = Commands.commandClassSet();
        for (Class<?> aClass : classSet) {
            if (PlayCommand.class.isAssignableFrom(aClass)) {
                Try.run(() -> {
                    PlayCommand command = (PlayCommand) aClass.newInstance();
                    CommandTag tag = aClass.getAnnotation(CommandTag.class);
                    String[] values = tag.value();
                    Arrays.stream(values).forEach(val -> {
                        playCommandMap.put(val, command);
                        logger.info("init command: {} -> {}", val, aClass.getName());
                    });
                }).onFailure(ex -> {
                    throw new IllegalStateException("can not instance PlayCommand:" + aClass.getSimpleName(), ex);
                });
                continue;
            }
            if (Command.class.isAssignableFrom(aClass)) {
                Try.run(() -> {
                    Command command = (Command) aClass.newInstance();
                    CommandTag tag = aClass.getAnnotation(CommandTag.class);
                    String[] values = tag.value();
                    Arrays.stream(values).forEach(val -> {
                        commandMap.put(val, command);
                        logger.info("init command: {} -> {}", val, aClass.getName());
                    });
                }).onFailure(ex -> {
                    throw new IllegalStateException("can not instance Command:" + aClass.getSimpleName(), ex);
                });
            }
        }
    }

    private ServerCommands() {}

    public static Command getCommand(String command) throws CommandNotFindException {
        if (!commandMap.containsKey(command)) {
            throw new CommandNotFindException();
        }
        return commandMap.get(command);
    }

    public static PlayCommand getPlayCommand(String command) throws CommandNotFindException {
        if (!playCommandMap.containsKey(command)) {
            throw new CommandNotFindException();
        }
        return playCommandMap.get(command);
    }

}
