package com.zz.netty.demo.test;


import com.zz.netty.demo.server.NettyServer;

public class TestServer {
    public static void main(String[] args) throws InterruptedException {
        NettyServer server = new NettyServer("127.0.0.1",1118);
        server.start();
    }
}
