package com.wkqchen20.game.client.command.landlords;

import com.alibaba.fastjson.JSON;
import com.wkqchen20.game.client.Context;
import com.wkqchen20.game.client.command.Command;
import com.wkqchen20.game.client.util.Printer;
import com.wkqchen20.game.common.command.ClientCommand;
import com.wkqchen20.game.common.command.CommandTag;
import com.wkqchen20.game.common.entity.DisplayPlayer;
import io.netty.channel.Channel;

/**
 * @Author: wkqchen20
 * @Date: 2020/10/19
 */
@CommandTag(ClientCommand.LANDLORDS_OPTION_IS_LANDLORDS)
public class OptionIsLandlordsCommand implements Command {

    @Override
    public void execute(Channel channel, String data) {
        DisplayPlayer player = JSON.parseObject(data, DisplayPlayer.class);
        if (player.equals(Context.getPlayer())) {
            Printer.notice("恭喜你成为地主");
        } else {
            Printer.notice(String.format("玩家%s成为地主", player.display()));
        }
    }
}
