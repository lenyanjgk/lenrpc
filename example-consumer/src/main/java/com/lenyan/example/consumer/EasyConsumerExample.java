package com.lenyan.example.consumer;


import com.lenyan.example.common.model.User;
import com.lenyan.example.common.service.UserService;

/**
 * 简易服务消费者示例
 */
public class EasyConsumerExample {

    public static void main(String[] args) {
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
    }
}
