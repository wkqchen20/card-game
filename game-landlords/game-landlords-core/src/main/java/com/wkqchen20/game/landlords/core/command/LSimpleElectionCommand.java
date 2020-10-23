package com.wkqchen20.game.landlords.core.command;

import com.wkqchen20.game.common.command.CommandTag;
import com.wkqchen20.game.common.constant.Errors;
import com.wkqchen20.game.core.ExecuteResult;
import com.wkqchen20.game.core.Player;
import com.wkqchen20.game.landlords.common.LCommandType;
import com.wkqchen20.game.landlords.core.LGame;
import com.wkqchen20.game.landlords.core.LPlayerBehavior;
import com.wkqchen20.game.landlords.core.LStatus;

/**
 * 抢地主</br>
 * 简单的抢地主行为，只要抢就给
 *
 * @Author: wkqchen20
 * @Date: 2020/10/10
 */
@CommandTag(LCommandType.ELECTION)
public class LSimpleElectionCommand extends AbstractLPlayCommand {

    @Override
    protected ExecuteResult playLandlords(Player player, LGame game, String data) {
        if (game.status() != LStatus.ELECTION) {
            return ExecuteResult.fail(Errors.GAME_CAN_NOT_PASS_WHEN_STATUS_ERROR);
        }
        game.setLandlordPlayer(player);
        game.addBehavior(LPlayerBehavior.elect(player));
        return ExecuteResult.success();
    }
}
