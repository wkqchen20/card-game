package com.wkqchen20.game.client.command;

import com.alibaba.fastjson.JSON;
import com.wkqchen20.game.client.Context;
import com.wkqchen20.game.client.util.Inputs;
import com.wkqchen20.game.client.util.Printer;
import com.wkqchen20.game.common.TransferRequestResponseData.TransferRequestResponseDataProto;
import com.wkqchen20.game.common.command.ClientCommand;
import com.wkqchen20.game.common.command.CommandTag;
import io.netty.channel.Channel;
import io.vavr.control.Try;

import java.util.List;
import java.util.stream.IntStream;

import static com.wkqchen20.game.common.command.ServerCommand.CHOOSE_GAME;

/**
 * @Author: wkqchen20
 * @Date: 2020/10/14
 */
@CommandTag(ClientCommand.OPTION_GAMES)
public class GamesCommand implements Command {

    @Override
    public void execute(Channel channel, String data) {
        StringBuilder sb = new StringBuilder("游戏类型：").append(System.lineSeparator());
        List<String> types = JSON.parseArray(data, String.class);
        IntStream.range(0, types.size()).forEach(idx -> {
            String type = types.get(idx);
            sb.append(idx).append(". ").append(type).append(System.lineSeparator());
        });
        sb.append("请选择：");
        Printer.waitForInput(sb.toString());
        Integer idx;
        while (true) {
            String choose = Inputs.input();
            Integer id = Try.of(() -> Integer.valueOf(choose)).getOrNull();
            if (id == null || id < 0 || id >= types.size()) {
                Printer.waitForInput("输入有误，请重新选择：");
            } else {
                idx = id;
                break;
            }
        }
        String game = types.get(idx);
        Context.setGameType(game);
        channel.writeAndFlush(TransferRequestResponseDataProto.newBuilder().setCommand(CHOOSE_GAME).setData(game).build());
    }
}
