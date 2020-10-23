package com.wkqchen20.game.landlords.core.command;

import com.google.common.base.Preconditions;
import com.google.common.collect.Lists;
import com.wkqchen20.game.common.constant.Errors;
import com.wkqchen20.game.core.*;
import com.wkqchen20.game.landlords.core.LGame;
import com.wkqchen20.game.landlords.core.LPlayerBehavior;

import java.util.Collections;
import java.util.List;
import java.util.Objects;

/**
 * @Author: wkqchen20
 * @Date: 2020/9/11
 */
public abstract class AbstractLPlayCommand implements PlayCommand {

    @Override
    public ExecuteResult execute(Player player, String data) {
        throw new UnsupportedOperationException("不支持的操作");
    }

    @Override
    public ExecuteResult play(Player player, Game game, String data) {
        Preconditions.checkState(game instanceof LGame);
        Preconditions.checkState(Objects.nonNull(player));
        if (!player.equals(game.currentPlayerToPlay())) {
            return ExecuteResult.fail(Errors.GAME_NOT_YOUR_TURN);
        }
        return playLandlords(player, (LGame) game, data);
    }

    protected abstract ExecuteResult playLandlords(Player player, LGame game, String data);

    /**
     * 最近一圈的出牌信息，不包含当前玩家
     * @param game
     * @return
     */
    protected List<LPlayerBehavior> getLatestOneRoundBehaviors(LGame game) {
        int playerCount = game.gameType().allowPlayerCount();
        List<LPlayerBehavior> ret = Lists.newArrayListWithCapacity(playerCount - 1);
        List<LPlayerBehavior> playerBehaviors = game.playerBehaviors();
        for (int index = 0, i = playerBehaviors.size() - 1; i >= 0 && index < playerCount - 1; i--, index++) {
            LPlayerBehavior tmp = playerBehaviors.get(i);
            if (tmp.isElection()) {
                break;
            }
            ret.add(tmp);
        }
        Collections.reverse(ret);
        return ret;
    }
}
