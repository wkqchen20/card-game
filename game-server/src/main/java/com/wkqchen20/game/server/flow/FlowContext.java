package com.wkqchen20.game.server.flow;


import com.wkqchen20.game.common.constant.Errors;
import com.wkqchen20.game.core.Player;
import io.netty.channel.Channel;

/**
 * @Author: wkqchen20
 * @Date: 2020/10/14
 */
public class FlowContext extends ConditionContext {

    private final Player player;
    private final Channel channel;
    private final String requestData;

    private FlowContext(Player player, Channel channel, String requestData, Errors responseErr) {
        super(responseErr);
        this.player = player;
        this.channel = channel;
        this.requestData = requestData;
    }

    public static FlowContext succeed(Player player, Channel channel, String requestData) {
        return new FlowContext(player, channel, requestData, null);
    }

    public static FlowContext failed(Player player, Channel channel, String requestData, Errors errors) {
        return new FlowContext(player, channel, requestData, errors);
    }

    public Player getPlayer() {
        return player;
    }

    public Channel getChannel() {
        return channel;
    }

    public String getRequestData() {
        return requestData;
    }

    @Override
    public String toString() {
        return "FlowContext{" +
                "player=" + player +
                ", data='" + requestData + '\'' +
                '}';
    }
}
