package com.wkqchen20.game.landlords.core.command;

import com.wkqchen20.game.common.command.CommandTag;
import com.wkqchen20.game.common.constant.Errors;
import com.wkqchen20.game.core.ExecuteResult;
import com.wkqchen20.game.core.Player;
import com.wkqchen20.game.landlords.common.LCommandType;
import com.wkqchen20.game.landlords.core.BehaviorStatus;
import com.wkqchen20.game.landlords.core.LGame;
import com.wkqchen20.game.landlords.core.LPlayerBehavior;
import com.wkqchen20.game.landlords.core.LStatus;

import java.util.List;

/**
 * 只针对出牌环节
 *
 * @Author: wkqchen20
 * @Date: 2020/10/9
 */
@CommandTag(LCommandType.PASS)
public class LPassCommand extends AbstractLPlayCommand {

    @Override
    protected ExecuteResult playLandlords(Player player, LGame game, String data) {
        LStatus status = game.status();
        if (status != LStatus.PLAYING) {
            return ExecuteResult.fail(Errors.GAME_CAN_NOT_PASS_WHEN_STATUS_ERROR);
        }
        List<LPlayerBehavior> playerBehaviors = getLatestOneRoundBehaviors(game);
        if (playerBehaviors.isEmpty()) {
            return ExecuteResult.fail(Errors.GAME_CAN_NOT_PASS_WHEN_NOT_SELL_CARDS);
        }
        LPlayerBehavior latestBehavior = playerBehaviors.get(playerBehaviors.size() - 1);
        BehaviorStatus behaviorStatus = latestBehavior.getStatus();
        if (latestBehavior.getPlayer().equals(player)) {
            // 重复出
            return ExecuteResult.fail(Errors.GAME_CAN_NOT_PASS_WHEN_NOT_SELL_CARDS);
        }
        if (behaviorStatus == BehaviorStatus.PASS) {
            // 找到最后一个出牌的人
            LPlayerBehavior latestNotPassBehavior = null;
            for (int i = playerBehaviors.size() - 1; i >= 0; i--) {
                LPlayerBehavior lPlayerBehavior = playerBehaviors.get(i);
                if (lPlayerBehavior.getStatus() == BehaviorStatus.SELL) {
                    latestNotPassBehavior = lPlayerBehavior;
                    break;
                }
            }
            // 一定存在
            if (latestNotPassBehavior.getPlayer().equals(player)) {
                return ExecuteResult.fail(Errors.GAME_CAN_NOT_PASS_WHEN_NOT_SELL_CARDS);
            }
        }
        game.turnToNext();
        game.addBehavior(LPlayerBehavior.pass(player));
        return ExecuteResult.success();
    }
}
