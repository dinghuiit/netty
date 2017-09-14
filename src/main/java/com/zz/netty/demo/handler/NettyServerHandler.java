package com.zz.netty.demo.handler;

import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class NettyServerHandler extends SimpleChannelInboundHandler<String> {

    private static final Logger logger = LoggerFactory.getLogger(NettyServerHandler.class);
    @Override
    protected void channelRead0(ChannelHandlerContext channelHandlerContext, String s) throws Exception {
        String result = handle(s);
        logger.debug("server sending back with result [{}].",result);
        channelHandlerContext.writeAndFlush(s+"^");
    }

    private String handle(String s) {
        logger.debug("handling message [{}]",s);
//        String response = "reponsed data :".concat(s).concat("\n");
        return s+"\n";
    }
}
