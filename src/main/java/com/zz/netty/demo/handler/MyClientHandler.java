package com.zz.netty.demo.handler;

import com.zz.netty.demo.server.MyServer;
import io.netty.channel.Channel;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.concurrent.BlockingQueue;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.TimeUnit;

public class MyClientHandler extends SimpleChannelInboundHandler<String> {

    private static final ConcurrentHashMap<String,BlockingQueue<String>> resultMap = new ConcurrentHashMap<>();
    private static final Logger logger = LoggerFactory.getLogger(MyServer.class);

    /**
     * 接收服务端响应消息，并将消息放入到blockingQueue中
     * @param channelHandlerContext
     * @param s
     * @throws Exception
     */
    @Override
    protected void channelRead0(ChannelHandlerContext channelHandlerContext, String s) throws Exception {
        logger.debug("received msg[{}] from server.",s);
        BlockingQueue<String> queue = resultMap.get(s);
        if (null == queue){
            queue = new LinkedBlockingQueue<String>(1);
            resultMap.putIfAbsent(s,queue);
        }
        queue.add(s);
    }

    /**
     * 在指定的时间间隔内将消息发送到指定的channel中，时间为null则以阻塞方式读取响应内容。
     * @param msg
     * @param timeout
     * @param channel
     * @return
     */
    public String send(String msg, Long timeout, Channel channel){
        logger.debug("sending message [{}] to server...",msg);
        resultMap.putIfAbsent(msg,new LinkedBlockingQueue<>(1));
        channel.writeAndFlush(msg+"^");//※※※※※※将消息写入到channel中※※※※※※
        String response = null;
        if (null == timeout){
            try {
                response = resultMap.get(msg).take();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        if (null !=timeout){
            try {
                response =  resultMap.get(msg).poll(timeout, TimeUnit.SECONDS);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        resultMap.remove(msg);
        return response;
    }
}
