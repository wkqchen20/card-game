package com.wkqchen20.game.server.command;

import com.wkqchen20.game.common.command.CommandTag;
import com.wkqchen20.game.common.constant.Errors;
import com.wkqchen20.game.core.Command;
import com.wkqchen20.game.core.ExecuteResult;
import com.wkqchen20.game.core.GameType;
import com.wkqchen20.game.core.Player;
import com.wkqchen20.game.server.Context;
import com.wkqchen20.game.server.entity.ClientInfo;

import java.util.Optional;

import static com.wkqchen20.game.common.command.ServerCommand.CHOOSE_GAME;

/**
 * @Author: wkqchen20
 * @Date: 2020/10/9
 */
@CommandTag({CHOOSE_GAME})
public class ChooseGameCommand implements Command {

    @Override
    public ExecuteResult execute(Player player, String data) {
        ClientInfo clientInfo = Context.clientInfo(player);
        Optional<GameType> optional = GameType.GameTypes.gameTypes().stream().filter(g -> g.name().equals(data)).findFirst();
        if (!optional.isPresent()) {
            return ExecuteResult.fail(Errors.GAME_TYPE_UNKNOWN);
        }
        clientInfo.setGameType(optional.get());
        return ExecuteResult.success();
    }

}
