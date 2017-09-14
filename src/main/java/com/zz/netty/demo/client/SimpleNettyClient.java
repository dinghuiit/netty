package com.zz.netty.demo.client;

import com.zz.netty.demo.handler.NettyClientHandler;
import io.netty.bootstrap.Bootstrap;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.Channel;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelOption;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.handler.codec.DelimiterBasedFrameDecoder;
import io.netty.handler.codec.string.StringDecoder;
import io.netty.handler.codec.string.StringEncoder;

public class SimpleNettyClient implements INettyClient {

    private String host;
    private int port;
    private Channel channel;
    private EventLoopGroup workGroup;
    private NettyClientHandler myClientHandler = new NettyClientHandler();

    public SimpleNettyClient(){}
    public SimpleNettyClient(String host, int port){
        this.host = host;
        this.port = port;
    }

    public void connect() throws InterruptedException {
        connect(host,port);
    }

    @Override
    public void connect(String host, int port) throws InterruptedException {
        Bootstrap b = new Bootstrap();
        workGroup = new NioEventLoopGroup();
        b.group(workGroup);
        b.channel(NioSocketChannel.class);
        b.option(ChannelOption.SO_KEEPALIVE, true);
        b.handler(new ChannelInitializer<SocketChannel>() {
            @Override
            protected void initChannel(SocketChannel socketChannel) throws Exception {
                ByteBuf delimiter = Unpooled.copiedBuffer("^".getBytes());
                socketChannel.pipeline().addLast(new DelimiterBasedFrameDecoder(102400, delimiter));
//                socketChannel.pipeline().addLast(new LineBasedFrameDecoder(1024));
                socketChannel.pipeline().addLast(new StringEncoder());
                socketChannel.pipeline().addLast(new StringDecoder());
//                socketChannel.pipeline().addLastdLast(new ObjectDecoder(1024 * 1024, ClassResolvers.weakCachingConcurrentResolver(this.getClass().getClassLoader())));
//                socketChannel.pipeline().addLastdLast(new ObjectEncoder());
                socketChannel.pipeline().addLast(myClientHandler);
            }
        });
        channel = b.connect(host, port).syncUninterruptibly().channel();

    }

    @Override
    public String sendSync(String message) {
        return myClientHandler.send(message, null, channel);
    }

    @Override
    public String sendAsync(String message, Long timeoutInSeconds) {
        return myClientHandler.send(message, timeoutInSeconds, channel);
    }

    @Override
    public void close() {
        if (null != workGroup) {
            workGroup.shutdownGracefully();
        }
    }
}
