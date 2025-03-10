package com.lenyan.example.consumer;

import com.lenyan.example.common.model.User;
import com.lenyan.example.common.service.UserService;
import com.lenyan.lenrpc.config.RpcConfig;
import com.lenyan.lenrpc.utils.ConfigUtils;

/**
 * 简易服务消费者示例
 */
public class EasyConsumerExample {

    public static void main(String[] args) {
        while (true) {
            RpcConfig rpc = ConfigUtils.loadConfig(RpcConfig.class, "rpc");
            System.out.println(rpc);
            UserService userService = new UserServiceProxy();
            // todo 需要获取 UserService 的实现类对象
            User user = new User();
            user.setName("lenyan");
            // 调用
            User newUser = userService.getUser(user);
            if (newUser != null) {
                System.out.println(newUser.getName());
            } else {
                System.out.println("user == null");
            }
            // 进程休眠10秒
            try {
                Thread.sleep(3000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }

    }
}
