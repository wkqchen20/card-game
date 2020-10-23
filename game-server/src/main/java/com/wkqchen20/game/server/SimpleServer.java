package com.wkqchen20.game.server;

import com.wkqchen20.game.common.TransferRequestResponseData;
import com.wkqchen20.game.server.network.Handler;
import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.epoll.Epoll;
import io.netty.channel.epoll.EpollEventLoopGroup;
import io.netty.channel.epoll.EpollServerSocketChannel;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.handler.codec.protobuf.ProtobufDecoder;
import io.netty.handler.codec.protobuf.ProtobufEncoder;
import io.netty.handler.codec.protobuf.ProtobufVarint32FrameDecoder;
import io.netty.handler.codec.protobuf.ProtobufVarint32LengthFieldPrepender;
import io.netty.handler.timeout.IdleStateHandler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import picocli.CommandLine;

import java.net.InetSocketAddress;
import java.util.concurrent.TimeUnit;

import static picocli.CommandLine.Command;
import static picocli.CommandLine.Option;

/**
 * @Author: wkqchen20
 * @Date: 2020/10/13
 */
@Command(name = "server", aliases = "s", mixinStandardHelpOptions = true, version = "1.0", description = "server")
public class SimpleServer implements Runnable {

    private static final Logger logger = LoggerFactory.getLogger(SimpleServer.class);

    @Option(names = {"-p", "--port"}, defaultValue = "8080", description = "server port, default: ${DEFAULT-VALUE}")
    int port;

    public static void main(String[] args) {
        CommandLine cmd = new CommandLine(new SimpleServer());
        int exitCode = cmd.execute(args);
        System.exit(exitCode);
    }

    void startServer() throws InterruptedException {
        EventLoopGroup parentGroup = Epoll.isAvailable() ? new EpollEventLoopGroup() : new NioEventLoopGroup();
        EventLoopGroup childGroup = Epoll.isAvailable() ? new EpollEventLoopGroup() : new NioEventLoopGroup();
        try {
            ServerBootstrap bootstrap = new ServerBootstrap()
                    .group(parentGroup, childGroup)
                    .channel(Epoll.isAvailable() ? EpollServerSocketChannel.class : NioServerSocketChannel.class)
                    .localAddress(new InetSocketAddress(port))
                    .childHandler(new ChannelInitializer<SocketChannel>() {
                        @Override
                        protected void initChannel(SocketChannel ch) throws Exception {
                            ch.pipeline()
                              .addLast(new IdleStateHandler(10, 0, 0, TimeUnit.MINUTES))
                              .addLast(new ProtobufVarint32FrameDecoder())
                              .addLast(new ProtobufDecoder(TransferRequestResponseData.TransferRequestResponseDataProto.getDefaultInstance()))
                              .addLast(new ProtobufVarint32LengthFieldPrepender())
                              .addLast(new ProtobufEncoder())
                              .addLast(new Handler());
                        }
                    });
            ChannelFuture f = bootstrap.bind().sync();
            logger.info("server start...");
            f.channel().closeFuture().sync();
        } finally {
            parentGroup.shutdownGracefully();
            childGroup.shutdownGracefully();
        }
    }

    @Override
    public void run() {
        try {
            startServer();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}

