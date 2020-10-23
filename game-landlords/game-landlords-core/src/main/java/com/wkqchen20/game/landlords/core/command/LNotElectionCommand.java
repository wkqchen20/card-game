package com.wkqchen20.game.landlords.core.command;

import com.wkqchen20.game.common.command.CommandTag;
import com.wkqchen20.game.common.constant.Errors;
import com.wkqchen20.game.core.ExecuteResult;
import com.wkqchen20.game.core.Player;
import com.wkqchen20.game.landlords.common.LCommandType;
import com.wkqchen20.game.landlords.core.LGame;
import com.wkqchen20.game.landlords.core.LPlayerBehavior;
import com.wkqchen20.game.landlords.core.LStatus;

import java.util.List;

/**
 * 不抢.
 * 如果一轮下去都不抢，给到第一个不抢的
 *
 * @Author: wkqchen20
 * @Date: 2020/10/10
 */
@CommandTag(LCommandType.NOT_ELECTION)
public class LNotElectionCommand extends AbstractLPlayCommand {

    @Override
    protected ExecuteResult playLandlords(Player player, LGame game, String data) {
        if (game.status() != LStatus.ELECTION) {
            return ExecuteResult.fail(Errors.GAME_CAN_NOT_PASS_WHEN_STATUS_ERROR);
        }
        List<LPlayerBehavior> behaviors = game.playerBehaviors();
        if (behaviors.size() == game.gameType().allowPlayerCount() - 1) {
            // 一轮了
            // 给第一个玩家发地主
            LPlayerBehavior pb = behaviors.get(0);
            game.setLandlordPlayer(pb.getPlayer());
        } else {
            game.turnToNext();
        }
        game.addBehavior(LPlayerBehavior.notElect(player));
        return ExecuteResult.success();
    }
}
