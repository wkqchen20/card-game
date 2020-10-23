package com.wkqchen20.game.client.command;

import com.alibaba.fastjson.JSON;
import com.wkqchen20.game.client.Context;
import com.wkqchen20.game.client.util.Inputs;
import com.wkqchen20.game.client.util.Printer;
import com.wkqchen20.game.common.TransferRequestResponseData.TransferRequestResponseDataProto;
import com.wkqchen20.game.common.command.ClientCommand;
import com.wkqchen20.game.common.command.CommandTag;
import com.wkqchen20.game.common.entity.DisplayRoom;
import io.netty.channel.Channel;
import io.vavr.control.Try;

import java.util.List;
import java.util.Optional;
import java.util.stream.IntStream;

import static com.wkqchen20.game.common.command.ServerCommand.BACK;
import static com.wkqchen20.game.common.command.ServerCommand.CHOOSE_ROOM;

/**
 * @Author: wkqchen20
 * @Date: 2020/10/14
 */
@CommandTag(ClientCommand.OPTION_ROOMS)
public class RoomsCommand implements Command {

    @Override
    public void execute(Channel channel, String data) {
        StringBuilder sb = new StringBuilder("房间：").append(System.lineSeparator());
        sb.append("0：创建新的房间").append(System.lineSeparator());
        List<DisplayRoom> rooms = JSON.parseArray(data, DisplayRoom.class);
        IntStream.range(0, rooms.size()).forEach(idx -> {
            DisplayRoom room = rooms.get(idx);
            sb.append(room.getId()).append(": ").append(room.getName()).append("(").append(room.getCurrentPlayerCount()).append(")").append(System.lineSeparator());
        });
        sb.append("#：返回上一级").append(System.lineSeparator());
        sb.append("请选择：");
        Printer.waitForInput(sb.toString());
        DisplayRoom choose = null;
        while (true) {
            String input = Inputs.input();
            if ("#".equals(input)) {
                channel.writeAndFlush(TransferRequestResponseDataProto.newBuilder().setCommand(BACK).build());
                break;
            }
            Integer idx = Try.of(() -> Integer.valueOf(input)).getOrNull();
            if (idx == null) {
                Printer.waitForInput("输入有误，请重新选择：");
                continue;
            }
            if (idx == 0) {
                Context.setRooms(data);
                Try.run(() -> ClientCommands.getCommand(ClientCommand.NEW_ROOM).execute(channel, null)).onFailure(ex -> {
                    throw new IllegalStateException(ex);
                });
                break;
            }
            Optional<DisplayRoom> optional = rooms.stream().filter(r -> r.getId() == idx).findAny();
            if (!optional.isPresent()) {
                Printer.waitForInput("输入有误，请重新选择：");
                continue;
            }
            choose = optional.get();
            break;
        }
        if (choose != null) {
            channel.writeAndFlush(TransferRequestResponseDataProto.newBuilder().setCommand(CHOOSE_ROOM).setData(String.valueOf(choose.getId())).build());
        }
    }
}
