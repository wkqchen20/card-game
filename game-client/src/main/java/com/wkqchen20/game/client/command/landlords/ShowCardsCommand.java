package com.wkqchen20.game.client.command.landlords;

import com.alibaba.fastjson.JSON;
import com.wkqchen20.game.client.Context;
import com.wkqchen20.game.client.command.Command;
import com.wkqchen20.game.client.util.Printer;
import com.wkqchen20.game.common.command.ClientCommand;
import com.wkqchen20.game.common.command.CommandTag;
import com.wkqchen20.game.landlords.common.LCard;
import io.netty.channel.Channel;

import java.util.Collections;
import java.util.List;

/**
 * @Author: wkqchen20
 * @Date: 2020/10/19
 */
@CommandTag(ClientCommand.LANDLORDS_SHOW_CARDS)
public class ShowCardsCommand implements Command {

    @Override
    public void execute(Channel channel, String data) {
        List<LCard> lCards = JSON.parseArray(data, LCard.class);
        Collections.sort(lCards);
        Context.setCards(lCards);
        Printer.landlordsCards(lCards);
    }
}
