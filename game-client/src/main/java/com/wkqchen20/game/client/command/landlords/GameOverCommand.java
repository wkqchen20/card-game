package com.wkqchen20.game.client.command.landlords;

import com.wkqchen20.game.client.command.Command;
import com.wkqchen20.game.client.util.Printer;
import com.wkqchen20.game.common.command.ClientCommand;
import com.wkqchen20.game.common.command.CommandTag;
import io.netty.channel.Channel;

/**
 * @Author: wkqchen20
 * @Date: 2020/10/19
 */
@CommandTag(ClientCommand.LANDLORDS_GAME_OVER)
public class GameOverCommand implements Command {

    @Override
    public void execute(Channel channel, String data) {
        boolean win = data.equalsIgnoreCase("win") ? true : false;
        if (win) {
            Printer.notice("恭喜你赢得了胜利!");
        } else {
            Printer.notice("你输了，再接再厉!");
        }
    }
}
