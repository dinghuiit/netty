package com.zz.netty.demo.test;

import com.zz.netty.demo.client.INettyClient;
import com.zz.netty.demo.client.MyClient;

public class TestClient {
    public static void main(String[] args) throws InterruptedException {
        INettyClient myClient = new MyClient();
        myClient.connect("127.0.0.1",1118);
        String response = myClient.sendSync("hello");
        System.out.println(response);
    }
}
