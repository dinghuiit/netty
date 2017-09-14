package com.zz.netty.demo.client;

public interface INettyClient {
    /**
     * 连接到指定的服务端
     * @param host
     * @param port
     * @throws InterruptedException
     */
    public void connect(String host, int port) throws InterruptedException;

    /**
     * 发送消息后同步获取响应消息
     * @param message
     * @return
     */
    public String sendSync(String message);

    /**
     * 发送消息后异步获取响应消息，超时后返回null
     * @param message
     * @param timeoutInSeconds
     * @return
     */
    public String sendAsync(String message, Long timeoutInSeconds);

    /**
     * 关闭客户端连接
     */
    public void close();
}
