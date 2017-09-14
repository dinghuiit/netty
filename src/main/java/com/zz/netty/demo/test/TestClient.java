package com.zz.netty.demo.test;


import com.zz.netty.demo.client.INettyClient;
import com.zz.netty.demo.client.SimpleNettyClient;

public class TestClient {
    public static void main(String[] args) throws InterruptedException {
        INettyClient simpleNettyClient = new SimpleNettyClient();
        simpleNettyClient.connect("127.0.0.1",1118);
        String response = simpleNettyClient.sendSync("hello");
        System.out.println(response);
    }
}
