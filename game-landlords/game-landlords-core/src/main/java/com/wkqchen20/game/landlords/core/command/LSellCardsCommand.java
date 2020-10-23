package com.wkqchen20.game.landlords.core.command;

import com.alibaba.fastjson.JSON;
import com.wkqchen20.game.common.command.CommandTag;
import com.wkqchen20.game.common.constant.Errors;
import com.wkqchen20.game.core.ExecuteResult;
import com.wkqchen20.game.core.Player;
import com.wkqchen20.game.core.PlayerBehavior;
import com.wkqchen20.game.landlords.common.LCard;
import com.wkqchen20.game.landlords.common.LCommandType;
import com.wkqchen20.game.landlords.core.*;

import java.util.List;
import java.util.Objects;

/**
 * @Author: wkqchen20
 * @Date: 2020/10/10
 */
@CommandTag(LCommandType.SELL)
public class LSellCardsCommand extends AbstractLPlayCommand {

    @Override
    protected ExecuteResult playLandlords(Player player, LGame game, String cardsJson) {
        if (game.status() != LStatus.PLAYING) {
            return ExecuteResult.fail(Errors.GAME_CAN_NOT_PASS_WHEN_STATUS_ERROR);
        }
        List<LCard> lCards = JSON.parseArray(cardsJson, LCard.class);
        LSellCardsCalculates calculates = LSellCardsCalculates.of(lCards);
        if (calculates.getType() == LSellType.ILLEGAL) {
            return ExecuteResult.fail(Errors.GAME_CARD_ILLEGAL);
        }
        SellCardsCommandData data = SellCardsCommandData.of(calculates);
        List<LPlayerBehavior> playerBehaviors = getLatestOneRoundBehaviors(game);
        if (playerBehaviors.isEmpty()) {
            game.addBehavior(LPlayerBehavior.sell(player, data));
            game.turnToNext();
            return ExecuteResult.success();
        }
        // 不能上把出完，这把继续出
        // 这种情况不应该存在
        PlayerBehavior latestBehavior = playerBehaviors.get(playerBehaviors.size() - 1);
        Player latestBehaviorPlayer = latestBehavior.getPlayer();
        if (latestBehaviorPlayer.equals(player)) {
            return ExecuteResult.fail(Errors.GAME_CAN_NOT_SELL_AGAIN);
        }
        // 一轮都是pass，不用比较大小了
        if (playerBehaviors.stream().allMatch(pb -> pb.getStatus() == BehaviorStatus.PASS)) {
            game.addBehavior(LPlayerBehavior.sell(player, data));
            return turnToNext(game);
        }
        // 否则上把出牌的信息做比较
        LPlayerBehavior latestNotPassBehavior = null;
        for (int i = playerBehaviors.size() - 1; i >= 0; i--) {
            LPlayerBehavior tmp = playerBehaviors.get(i);
            if (tmp.getStatus() != BehaviorStatus.PASS) {
                latestNotPassBehavior = tmp;
                break;
            }
        }
        // latestNotPassBehavior 不会为空
        LPlayerBehavior nonNullLatestNotPassBehavior = Objects.requireNonNull(latestNotPassBehavior);
        if (!canCompare(data, nonNullLatestNotPassBehavior) || !biggerThanLatest(data, nonNullLatestNotPassBehavior)) {
            return ExecuteResult.fail(Errors.GAME_CARD_CAN_NOT_COMPARE);
        }
        game.addBehavior(LPlayerBehavior.sell(player, data));
        return turnToNext(game);
    }

    private ExecuteResult turnToNext(LGame game) {
        if (game.status() != LStatus.OVER) {
            game.turnToNext();
        }
        return ExecuteResult.success();
    }

    private boolean canCompare(SellCardsCommandData data, LPlayerBehavior latestSellBehavior) {
        // 牌型一样，牌数一样
        SellCardsCommandData latestSellData = latestSellBehavior.getData();
        boolean flag1 = data.getType() == latestSellData.getType() && data.getCards().size() == latestSellData.getCards().size();
        // 炸弹
        boolean flag2 = data.getType() == LSellType.BOMB || data.getType() == LSellType.KING_BOMB;
        return flag1 || flag2;
    }

    private boolean biggerThanLatest(SellCardsCommandData data, LPlayerBehavior latestSellBehavior) {
        return data.getScore() > latestSellBehavior.getData().getScore();
    }

}
