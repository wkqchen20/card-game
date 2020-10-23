package com.wkqchen20.game.client.command;

import com.alibaba.fastjson.JSON;
import com.wkqchen20.game.client.Context;
import com.wkqchen20.game.client.util.Printer;
import com.wkqchen20.game.common.command.ClientCommand;
import com.wkqchen20.game.common.command.CommandTag;
import com.wkqchen20.game.common.entity.DisplayPlayer;
import com.wkqchen20.game.common.entity.RoomDetail;
import io.netty.channel.Channel;

import java.util.List;

/**
 * @Author: wkqchen20
 * @Date: 2020/10/14
 */
@CommandTag(ClientCommand.JOINED_ROOM)
public class JoinedRoomCommand implements Command {

    @Override
    public void execute(Channel channel, String data) {
        RoomDetail detail = JSON.parseObject(data, RoomDetail.class);
        List<DisplayPlayer> players = detail.getPlayers();
        DisplayPlayer latestJoinedPlayer = players.get(players.size() - 1);
        if (!latestJoinedPlayer.equals(Context.getPlayer())) {
            Printer.notice(String.format("玩家%s进入房间", latestJoinedPlayer.display()));
        } else {
            Printer.notice("欢迎进入房间");
        }
        int waitForPlayerCount = detail.getAllowPlayerCount() - detail.getCurrentPlayerCount();
        if (waitForPlayerCount == 0) {
            Printer.notice("游戏即将开始");
        } else {
            Printer.notice("等待" + waitForPlayerCount + "个玩家加入");
        }
    }
}
