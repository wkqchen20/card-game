package com.wkqchen20.game.core.command;

import com.wkqchen20.game.common.command.CommandTag;
import com.wkqchen20.game.core.*;

import static com.wkqchen20.game.common.command.ServerCommand.*;

/**
 * @Author: wkqchen20
 * @Date: 2020/10/9
 */
@CommandTag({CHOOSE_GAME, BACK, GAME_STATUS_INIT, GAME_STATUS_PLAYING, GAME_STATUS_GAMEOVER})
public class IgnoreCommand implements Command, PlayCommand {

    @Override
    public ExecuteResult execute(Player player, String data) {
        return ExecuteResult.success();
    }

    @Override
    public ExecuteResult play(Player player, Game game, String data) {
        return ExecuteResult.success();
    }

}
