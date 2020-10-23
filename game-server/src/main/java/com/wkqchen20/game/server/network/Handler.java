package com.wkqchen20.game.server.network;

import com.wkqchen20.game.common.TransferRequestResponseData.TransferRequestResponseDataProto;
import com.wkqchen20.game.common.command.ClientCommand;
import com.wkqchen20.game.common.command.ServerCommand;
import com.wkqchen20.game.core.*;
import com.wkqchen20.game.landlords.common.LCommandType;
import com.wkqchen20.game.landlords.core.LGame;
import com.wkqchen20.game.server.Context;
import com.wkqchen20.game.server.flow.CommonFlow;
import com.wkqchen20.game.server.flow.FlowContext;
import com.wkqchen20.game.server.flow.LandlordsGameContext;
import io.netty.channel.Channel;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.handler.timeout.IdleState;
import io.netty.handler.timeout.IdleStateEvent;
import io.netty.util.internal.StringUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.squirrelframework.foundation.fsm.StateMachine;

import java.util.Optional;

public class Handler extends ChannelInboundHandlerAdapter {
    private static final Logger logger = LoggerFactory.getLogger(Handler.class);

    @Override
    public void channelRegistered(ChannelHandlerContext ctx) throws Exception {
        Channel ch = ctx.channel();
        HumanPlayer player = new HumanPlayer(StringUtil.EMPTY_STRING);
        Context.newPlayer(player, ch);
        ch.writeAndFlush(TransferRequestResponseDataProto.newBuilder().setCommand(ClientCommand.CONNECTED).build());
        ch.writeAndFlush(TransferRequestResponseDataProto.newBuilder().setCommand(ClientCommand.SET_PLAYER_NICKNAME).build());
        StateMachine sm = CommonFlow.commonFlow(ClientCommand.SET_PLAYER_NICKNAME);
        Context.clientInfo(player).setCommonFsm(sm);
    }

    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        if (!(msg instanceof TransferRequestResponseDataProto)) {
            return;
        }
        TransferRequestResponseDataProto data = (TransferRequestResponseDataProto) msg;
        String commandName = data.getCommand();
        if (ServerCommand.HEART_BEAT.equals(commandName)) {
            return;
        }
        Channel channel = ctx.channel();
        Player player = Context.getPlayer(channel);
        if (ServerCommand.isCommonType(commandName)) {
            Command command = ServerCommands.getCommand(commandName);
            ExecuteResult result = command.execute(player, data.getData());
            FlowContext flowContext;
            if (!result.isSucceed()) {
                channel.writeAndFlush(TransferRequestResponseDataProto.newBuilder().setCommand(ClientCommand.ILLEGAL).setData(result.getErrors().error).build());
                flowContext = FlowContext.failed(player, channel, data.getData(), result.getErrors());
            } else {
                flowContext = FlowContext.succeed(player, channel, data.getData());
            }
            Context.clientInfo(player).getCommonFsm().fire(commandName, flowContext);
            return;
        }
        if (LCommandType.isLandlordsType(commandName)) {
            Optional<Room> joinedRoom = player.getJoinedRoom();
            if (!joinedRoom.isPresent()) {
                channel.writeAndFlush(TransferRequestResponseDataProto.newBuilder().setCommand(ClientCommand.ILLEGAL).setData("不在游戏中").build());
                return;
            }
            LGame game = (LGame) joinedRoom.get().getGame();
            PlayCommand playCommand = ServerCommands.getPlayCommand(commandName);
            ExecuteResult result = player.play(playCommand, data.getData());
            if (!result.isSucceed()) {
                channel.writeAndFlush(TransferRequestResponseDataProto.newBuilder().setCommand(ClientCommand.ILLEGAL).setData(result.getErrors().error).build());
                Context.fromGame(game).fire(commandName, LandlordsGameContext.failed(result.getErrors(), game, player));
            } else {
                Context.fromGame(game).fire(commandName, LandlordsGameContext.of(game, player));
            }
        }
    }

    @Override
    public void userEventTriggered(ChannelHandlerContext ctx, Object evt) throws Exception {
        if (evt instanceof IdleStateEvent) {
            IdleStateEvent event = (IdleStateEvent) evt;
            if (event.state() == IdleState.READER_IDLE) {
                try {
                    // The client do not send request for a long time
                    // TODO should notice other players of the room that will replace using robot behavior
                    ctx.channel().close();
                } catch (Exception e) {
                }
            }
        } else {
            super.userEventTriggered(ctx, evt);
        }
    }

    @Override
    public void channelUnregistered(ChannelHandlerContext ctx) throws Exception {
        Channel channel = ctx.channel();
        Context.clean(channel);
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        logger.error("got exception", cause);
    }
}
