package com.lenyan.example.provider;


import com.lenyan.example.common.service.UserService;
import com.lenyan.lenrpc.registry.LocalRegistry;
import com.lenyan.lenrpc.server.HttpServer;
import com.lenyan.lenrpc.server.VertxHttpServer;

/**
 * 简易服务提供者示例
 */
public class EasyProviderExample {

    public static void main(String[] args) {
        // 注册服务
        LocalRegistry.register(UserService.class.getName(), UserServiceImpl.class);

        // 启动 web 服务
        HttpServer httpServer = new VertxHttpServer();
        httpServer.doStart(8080);
    }
}
