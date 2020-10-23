package com.wkqchen20.game.client.command.landlords;

import com.alibaba.fastjson.JSON;
import com.wkqchen20.game.client.command.Command;
import com.wkqchen20.game.client.util.Printer;
import com.wkqchen20.game.common.command.ClientCommand;
import com.wkqchen20.game.common.command.CommandTag;
import com.wkqchen20.game.landlords.common.entity.PlayerWithPass;
import io.netty.channel.Channel;

/**
 * @Author: wkqchen20
 * @Date: 2020/10/19
 */
@CommandTag(ClientCommand.LANDLORDS_SHOW_PASS)
public class ShowPassCommand implements Command {

    @Override
    public void execute(Channel channel, String data) {
        PlayerWithPass pp = JSON.parseObject(data, PlayerWithPass.class);
        Printer.notice(String.format("玩家：%s不出\n", pp.getPlayer().display()));
    }
}
