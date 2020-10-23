package com.wkqchen20.game.client.command.landlords;

import com.alibaba.fastjson.JSON;
import com.google.common.base.Splitter;
import com.wkqchen20.game.client.Context;
import com.wkqchen20.game.client.command.Command;
import com.wkqchen20.game.client.util.Inputs;
import com.wkqchen20.game.client.util.Printer;
import com.wkqchen20.game.common.TransferRequestResponseData.TransferRequestResponseDataProto;
import com.wkqchen20.game.common.command.ClientCommand;
import com.wkqchen20.game.common.command.CommandTag;
import com.wkqchen20.game.landlords.common.LCard;
import com.wkqchen20.game.landlords.common.LCommandType;
import io.netty.channel.Channel;
import io.vavr.control.Try;

import java.util.List;

import static java.util.stream.Collectors.toList;

/**
 * @Author: wkqchen20
 * @Date: 2020/10/19
 */
@CommandTag(ClientCommand.LANDLORDS_OPTION_PLAY)
public class OptionPlayCommand implements Command {
    private static final Splitter SPLITTER = Splitter.on(",").trimResults().omitEmptyStrings();

    @Override
    public void execute(Channel channel, String data) {
        Printer.notice("轮到你出牌了，不出输入'pass'；出牌输入下标','分隔出牌（如：1,2,3）：");
        while (true) {
            String input = Inputs.input();
            if (input.equalsIgnoreCase("pass")) {
                channel.writeAndFlush(TransferRequestResponseDataProto.newBuilder().setCommand(LCommandType.PASS).build());
                return;
            }
            List<LCard> lCards = Try.of(() -> {
                List<String> ids = SPLITTER.splitToList(input);
                List<Integer> idxList = ids.stream().mapToInt(Integer::parseInt).boxed().sorted().collect(toList());
                List<LCard> cards = Context.getCards(LCard.class);
                if (!idxList.stream().allMatch(id -> id >= 1 && id <= cards.size())) {
                    return null;
                }
                return idxList.stream().map(id -> cards.get(id - 1)).collect(toList());
            }).getOrNull();
            if (lCards == null) {
                Printer.notice("输入有误，输入下标','分隔出牌（如：1,2,3）：");
                continue;
            }
            channel.writeAndFlush(TransferRequestResponseDataProto.newBuilder().setCommand(LCommandType.SELL).setData(JSON.toJSONString(lCards)).build());
            break;
        }

    }
}
