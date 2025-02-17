package com.lenyan.example.provider;


import com.lenyan.lenrpc.server.HttpServer;
import com.lenyan.lenrpc.server.VertxHttpServer;

/**
 * 简易服务提供者示例
 */
public class EasyProviderExample {

    public static void main(String[] args) {
        // 启动 web 服务
        HttpServer httpServer = new VertxHttpServer();
        httpServer.doStart(8080);
    }
}
