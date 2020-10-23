package com.wkqchen20.game.client;

import com.wkqchen20.game.client.network.Handler;
import com.wkqchen20.game.common.TransferRequestResponseData;
import io.netty.bootstrap.Bootstrap;
import io.netty.channel.Channel;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.handler.codec.protobuf.ProtobufDecoder;
import io.netty.handler.codec.protobuf.ProtobufEncoder;
import io.netty.handler.codec.protobuf.ProtobufVarint32FrameDecoder;
import io.netty.handler.codec.protobuf.ProtobufVarint32LengthFieldPrepender;
import io.netty.handler.timeout.IdleStateHandler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import picocli.CommandLine;
import picocli.CommandLine.*;

import java.util.concurrent.TimeUnit;

/**
 * @Author: wkqchen20
 * @Date: 2020/10/13
 */

@Command(name = "client", aliases = "c", mixinStandardHelpOptions = true, version = "1.0", description = "client")
public class SimpleClient implements Runnable {
    private static final Logger logger = LoggerFactory.getLogger(SimpleClient.class);

    @Option(names = {"--host"}, defaultValue = "127.0.0.1", description = "server host, default: ${DEFAULT-VALUE}")
    private String host;
    @Option(names = {"-p", "--port"}, defaultValue = "8080", description = "server port, default: ${DEFAULT-VALUE}")
    private int port;

    public static void main(String[] args) {
        SimpleClient simpleClient = new SimpleClient();
        CommandLine cmd = new CommandLine(simpleClient);
        logger.info("connect to remote host:{}, port:{}...", simpleClient.host, simpleClient.port);
        int exitCode = cmd.execute(args);
        System.exit(exitCode);
    }

    void startClient() throws InterruptedException {
        EventLoopGroup group = new NioEventLoopGroup();
        try {
            Bootstrap bootstrap = new Bootstrap()
                    .group(group)
                    .channel(NioSocketChannel.class)
                    .handler(new ChannelInitializer<SocketChannel>() {
                        @Override
                        protected void initChannel(SocketChannel ch) throws Exception {
                            ch.pipeline()
                              .addLast(new IdleStateHandler(0, 4, 0, TimeUnit.SECONDS))
                              .addLast(new ProtobufVarint32FrameDecoder())
                              .addLast(new ProtobufDecoder(TransferRequestResponseData.TransferRequestResponseDataProto.getDefaultInstance()))
                              .addLast(new ProtobufVarint32LengthFieldPrepender())
                              .addLast(new ProtobufEncoder())
                              .addLast(new Handler());
                        }
                    });
            Channel channel = bootstrap.connect(host, port).sync().channel();
            logger.info("connected.");
            channel.closeFuture().sync();
        } finally {
            group.shutdownGracefully().sync();
        }
    }

    @Override
    public void run() {
        try {
            startClient();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}
