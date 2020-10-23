package com.wkqchen20.game.client.command;

import com.google.common.collect.Maps;
import com.wkqchen20.game.common.command.CommandNotFindException;
import com.wkqchen20.game.common.command.CommandTag;
import com.wkqchen20.game.common.command.Commands;
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
public final class ClientCommands {
    private static final Logger logger = LoggerFactory.getLogger(ClientCommands.class);

    private static Map<String, Command> commandMap = Maps.newHashMap();

    static {
        Set<Class<?>> classSet = Commands.commandClassSet();
        for (Class<?> aClass : classSet) {
            Try.run(() -> {
                Command command = (Command) aClass.newInstance();
                CommandTag tag = aClass.getAnnotation(CommandTag.class);
                String[] values = tag.value();
                Arrays.stream(values).forEach(val -> {
                    commandMap.put(val, command);
                    logger.info("init command:{} -> {}", val, aClass.getName());
                });
            }).onFailure(ex -> {
                throw new IllegalStateException("can not instance PlayCommand:" + aClass.getSimpleName(), ex);
            });
        }
    }

    private ClientCommands() {}

    public static Command getCommand(String command) throws CommandNotFindException {
        if (!commandMap.containsKey(command)) {
            throw new CommandNotFindException();
        }
        return commandMap.get(command);
    }

}
