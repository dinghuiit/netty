package com.zz.netty.demo.test;

import com.zz.netty.demo.server.MyServer;

public class TestServer {
    public static void main(String[] args) throws InterruptedException {
        MyServer server = new MyServer("127.0.0.1",1118);
        server.start();
    }
}
