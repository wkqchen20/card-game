package com.wkqchen20.game.client.command.landlords;

import com.wkqchen20.game.client.command.Command;
import com.wkqchen20.game.client.util.Inputs;
import com.wkqchen20.game.client.util.Printer;
import com.wkqchen20.game.common.TransferRequestResponseData.TransferRequestResponseDataProto;
import com.wkqchen20.game.common.command.ClientCommand;
import com.wkqchen20.game.common.command.CommandTag;
import com.wkqchen20.game.landlords.common.LCommandType;
import io.netty.channel.Channel;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @Author: wkqchen20
 * @Date: 2020/10/19
 */
@CommandTag(ClientCommand.LANDLORDS_OPTION_ELECT)
public class OptionElectCommand implements Command {
    private static final Logger log = LoggerFactory.getLogger(OptionElectCommand.class);

    @Override
    public void execute(Channel channel, String data) {
        Printer.notice("是否选地主？y/n");
        while (true) {
            String input = Inputs.input();
            if (input.equalsIgnoreCase("y")) {
                channel.writeAndFlush(TransferRequestResponseDataProto.newBuilder().setCommand(LCommandType.ELECTION).build());
                break;
            } else if (input.equalsIgnoreCase("n")) {
                channel.writeAndFlush(TransferRequestResponseDataProto.newBuilder().setCommand(LCommandType.NOT_ELECTION).build());
                break;
            } else {
                log.warn("player inputs:{} illegal", input);
                Printer.notice("输入有误，是否选地主？y/n");
            }
        }

    }
}
