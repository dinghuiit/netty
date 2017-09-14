package com.zz.netty.demo.server;

import com.zz.netty.demo.handler.NettyServerHandler;
import io.netty.bootstrap.ServerBootstrap;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.Channel;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelOption;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.handler.codec.DelimiterBasedFrameDecoder;
import io.netty.handler.codec.string.StringDecoder;
import io.netty.handler.codec.string.StringEncoder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;

@Component
public class NettyServer {
    private static final Logger logger = LoggerFactory.getLogger(NettyServer.class);

    private Channel channel;
    private EventLoopGroup bossGroup;
    private EventLoopGroup workerGroup;

    @Value("${netty.server.host:127.0.0.1}")
    private String host = "127.0.0.1";
    @Value("${netty.server.port:1118}")
    private int port = 1118;
    public NettyServer(){};
    public NettyServer(String host, int port){
        this.host = host;
        this.port = port;
    }
    @PostConstruct
    public void start() throws InterruptedException {
        if (logger.isDebugEnabled()) {
            logger.debug("Netty server is starting...");
        }
        ServerBootstrap b = new ServerBootstrap();
        bossGroup = new NioEventLoopGroup();
        workerGroup = new NioEventLoopGroup();
        b.group(bossGroup, workerGroup);
        b.channel(NioServerSocketChannel.class);
        b.option(ChannelOption.SO_BACKLOG, 128);
        b.childOption(ChannelOption.SO_KEEPALIVE, true);
        b.childHandler(new ChannelInitializer<SocketChannel>() {
            @Override
            protected void initChannel(SocketChannel socketChannel) throws Exception {
                //使用^作为消息分隔符，防止粘包情况出现。
                ByteBuf delimiter = Unpooled.copiedBuffer("^".getBytes());
                socketChannel.pipeline().addLast(new DelimiterBasedFrameDecoder(102400,delimiter));
                socketChannel.pipeline().addLast(new StringEncoder());
                socketChannel.pipeline().addLast(new StringDecoder());
                socketChannel.pipeline().addLast(new NettyServerHandler());
            }
        });
        channel = b.bind(host, port).sync().channel();
        logger.debug("server started, listening to port [{}].", port);
    }

    @PreDestroy
    public void stop() {
        workerGroup.shutdownGracefully();
        bossGroup.shutdownGracefully();
        if (null != channel) {
            channel.closeFuture().syncUninterruptibly();
        }
        logger.debug("server stoped!");
    }
}
