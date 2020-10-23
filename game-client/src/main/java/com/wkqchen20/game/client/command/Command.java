package com.wkqchen20.game.client.command;

import io.netty.channel.Channel;

/**
 * @Author: wkqchen20
 * @Date: 2020/10/14
 */
public interface Command {

    void execute(Channel channel, String data);
}
