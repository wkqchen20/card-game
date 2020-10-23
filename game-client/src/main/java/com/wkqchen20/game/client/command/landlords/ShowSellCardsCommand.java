package com.wkqchen20.game.client.command.landlords;

import com.alibaba.fastjson.JSON;
import com.wkqchen20.game.client.command.Command;
import com.wkqchen20.game.client.util.Printer;
import com.wkqchen20.game.common.command.ClientCommand;
import com.wkqchen20.game.common.command.CommandTag;
import com.wkqchen20.game.common.entity.DisplayPlayer;
import com.wkqchen20.game.landlords.common.LCard;
import com.wkqchen20.game.landlords.common.entity.PlayerWithSellCards;
import io.netty.channel.Channel;

import java.util.List;

/**
 * @Author: wkqchen20
 * @Date: 2020/10/19
 */
@CommandTag(ClientCommand.LANDLORDS_SHOW_SELL_CARDS)
public class ShowSellCardsCommand implements Command {

    @Override
    public void execute(Channel channel, String data) {
        PlayerWithSellCards psc = JSON.parseObject(data, PlayerWithSellCards.class);
        DisplayPlayer player = psc.getPlayer();
        List<LCard> cards = psc.getCards();
        Printer.notice(String.format("玩家：%s出牌：\n", player.display()));
        Printer.landlordsSellCards(cards);
    }

}
