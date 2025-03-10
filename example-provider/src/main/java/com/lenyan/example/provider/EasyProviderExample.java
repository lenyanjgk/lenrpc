package com.lenyan.example.provider;

import com.lenyan.example.common.service.UserService;
import com.lenyan.lenrpc.RpcApplication;
import com.lenyan.lenrpc.config.RpcConfig;
import com.lenyan.lenrpc.registry.LocalRegistry;
import com.lenyan.lenrpc.server.HttpServer;
import com.lenyan.lenrpc.server.VertxHttpServer;
import com.lenyan.lenrpc.utils.ConfigUtils;

/**
 * 简易服务提供者示例
 *
 * @author <a href="https://github.com/lenyanjgk">程序员冷颜</a>
 */
public class EasyProviderExample {

    public static void main(String[] args) {
        // 加载配置
        RpcConfig config = ConfigUtils.loadConfig(RpcConfig.class, "rpc");

        // RPC 框架初始化
        RpcApplication.init(config);

        // 打印初始配置
        System.out.println("初始配置：");
        System.out.println("服务名称：" + config.getName());
        System.out.println("服务版本：" + config.getVersion());
        System.out.println("服务描述：" + config.getDescription());
        System.out.println("作者：" + config.getAuthor());
        System.out.println("公司：" + config.getCompany());
        System.out.println("地址：" + config.getAddress());
        System.out.println("服务器地址：" + config.getServerHost());
        System.out.println("服务器端口：" + config.getServerPort());

        // 注册服务
        LocalRegistry.register(UserService.class.getName(), UserServiceImpl.class);

        // 启动 web 服务
        HttpServer httpServer = new VertxHttpServer();
        httpServer.doStart(config.getServerPort());
    }
}
