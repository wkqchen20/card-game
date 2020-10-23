package com.wkqchen20.game.client.network;


import com.wkqchen20.game.client.command.ClientCommands;
import com.wkqchen20.game.client.command.Command;
import com.wkqchen20.game.common.TransferRequestResponseData.TransferRequestResponseDataProto;
import com.wkqchen20.game.common.command.ServerCommand;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.handler.timeout.IdleState;
import io.netty.handler.timeout.IdleStateEvent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class Handler extends ChannelInboundHandlerAdapter {

    private static final Logger logger = LoggerFactory.getLogger(Handler.class);

    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        if (msg instanceof TransferRequestResponseDataProto) {
            TransferRequestResponseDataProto data = (TransferRequestResponseDataProto) msg;
            if (ServerCommand.HEART_BEAT.equals(data.getCommand())) {
                return;
            }
            Command command = ClientCommands.getCommand(data.getCommand());
            command.execute(ctx.channel(), data.getData());
        }
    }

    @Override
    public void userEventTriggered(ChannelHandlerContext ctx, Object evt) throws Exception {
        if (evt instanceof IdleStateEvent) {
            IdleStateEvent event = (IdleStateEvent) evt;
            if (event.state() == IdleState.WRITER_IDLE) {
                logger.info("HEART_BEAT...");
                ctx.channel().writeAndFlush(TransferRequestResponseDataProto.newBuilder().setCommand(ServerCommand.HEART_BEAT).build());
            }
        }
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        logger.error("got exception", cause);
        if (cause instanceof java.io.IOException) {
            System.out.println("The network is not good or did not operate for a long time, has been offline");
            System.exit(0);
        }
    }

}
