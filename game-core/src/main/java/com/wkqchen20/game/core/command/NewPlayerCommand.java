package com.wkqchen20.game.core.command;

import com.wkqchen20.game.common.command.CommandTag;
import com.wkqchen20.game.common.command.ServerCommand;
import com.wkqchen20.game.core.Command;
import com.wkqchen20.game.core.ExecuteResult;
import com.wkqchen20.game.core.Player;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @Author: wkqchen20
 * @Date: 2020/10/9
 */
@CommandTag(ServerCommand.NEW_PLAYER)
public class NewPlayerCommand implements Command {

    private static final Logger logger = LoggerFactory.getLogger(NewPlayerCommand.class);

    @Override
    public ExecuteResult execute(Player player, String data) {
        String name = data;
        player.setNickname(name);
        logger.info("set player:{} nickname:{}", player.getId(), data);
        return ExecuteResult.success();
    }
}
