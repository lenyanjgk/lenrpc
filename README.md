# LenRPC - 轻量级RPC框架

LenRPC是一个简单易用的轻量级RPC（远程过程调用）框架，专为Java应用设计。它提供了高效的服务注册、发现和远程调用功能，同时支持灵活的配置管理。

## 目录结构

```
lenrpc/
├── len-rpc-core/         # 核心模块，包含框架的基本实现
├── len-rpc-easy/         # 简化版模块，提供更易用的API
├── example-common/       # 示例公共模块，定义接口和模型
├── example-provider/     # 服务提供者示例
└── example-consumer/     # 服务消费者示例
```

## 主要特性

- **简单易用**：提供简洁的API，降低使用门槛
- **高效通信**：基于Vertx实现的高性能通信
- **灵活配置**：支持多种配置文件格式和动态更新
- **服务注册与发现**：简单的服务注册和发现机制
- **负载均衡**：支持多种负载均衡策略

## 配置功能亮点

LenRPC框架提供了强大而灵活的配置管理功能：

1. **多格式配置文件支持**
   - 支持 `.yml`、`.yaml`、`.properties` 等多种格式的配置文件
   - 配置文件自动识别和加载

2. **环境隔离**
   - 支持不同环境（dev、test、prod等）的配置文件
   - 通过 `application-{env}.yml` 格式实现环境配置隔离

3. **配置自动更新**
   - 支持监听配置文件变更并自动更新配置对象
   - 无需重启应用即可应用新配置

4. **中文支持**
   - 完全支持配置文件中的中文字符
   - 自动处理编码问题

5. **配置分组**
   - 通过嵌套配置类实现配置分组
   - 提高配置的可维护性和可读性

## 快速开始

### 1. 添加依赖

```xml
<dependency>
    <groupId>com.lenyan</groupId>
    <artifactId>len-rpc-core</artifactId>
    <version>1.0-SNAPSHOT</version>
</dependency>
```

### 2. 定义服务接口

```java
package com.lenyan.example.common.service;

import com.lenyan.example.common.model.User;

public interface UserService {
    User getUser(User user);
}
```

### 3. 实现服务（服务提供方）

```java
package com.lenyan.example.provider.service;

import com.lenyan.example.common.model.User;
import com.lenyan.example.common.service.UserService;

public class UserServiceImpl implements UserService {
    @Override
    public User getUser(User user) {
        System.out.println("用户名：" + user.getName());
        user.setName("返回: " + user.getName());
        return user;
    }
}
```

### 4. 配置服务

创建 `application.yml` 文件：

```yaml
rpc:
  name: lenrpc
  version: 1.0
  serverHost: localhost
  serverPort: 8080
```

或使用 `application.properties`：

```properties
rpc.name=lenrpc
rpc.version=1.0
rpc.serverHost=localhost
rpc.serverPort=8080
```

### 5. 启动服务提供方

```java
public class ProviderExample {
    public static void main(String[] args) {
        // 初始化RPC配置
        RpcApplication.init();
        
        // 注册服务
        UserServiceImpl userService = new UserServiceImpl();
        ServiceRegistry registry = new ServiceRegistry();
        registry.register(UserService.class, userService);
        
        // 启动服务
        RpcServer server = new RpcServer();
        server.start();
    }
}
```

### 6. 消费服务（服务消费方）

```java
public class ConsumerExample {
    public static void main(String[] args) {
        // 初始化RPC配置
        RpcApplication.init();
        
        // 创建代理
        UserService userService = new UserServiceProxy();
        
        // 调用远程服务
        User user = new User();
        user.setName("lenyan");
        User result = userService.getUser(user);
        System.out.println("结果: " + result.getName());
    }
}
```

## 配置详解

### 基本配置

LenRPC使用`RpcConfig`类管理框架配置：

```java
public class RpcConfig {
    private String name = "len-rpc";     // 名称
    private String version = "1.0";      // 版本号
    private String serverHost = "localhost";  // 服务器主机名
    private Integer serverPort = 8080;   // 服务器端口号
    private boolean mock = false;        // 模拟调用
    private boolean autoReload = false;  // 配置文件自动更新
    private String charset = "UTF-8";    // 配置文件编码
}
```

### 多环境配置

支持不同环境的配置文件：

- `application.yml`：默认配置
- `application-dev.yml`：开发环境配置
- `application-test.yml`：测试环境配置
- `application-prod.yml`：生产环境配置

使用示例：

```java
// 加载开发环境配置
RpcConfig config = ConfigUtils.loadConfig(RpcConfig.class, "rpc", "dev");

// 加载默认配置
RpcConfig config = ConfigUtils.loadConfig(RpcConfig.class, "rpc");
```

### 配置自动更新

要启用配置自动更新，只需在配置文件中设置：

```yaml
rpc:
  autoReload: true
```

框架会自动监听配置文件变更，并在文件变更时重新加载配置。

## 架构设计

LenRPC框架采用分层架构设计：

1. **接口层**：定义服务接口规范
2. **代理层**：负责客户端的远程调用代理
3. **网络传输层**：基于Vertx实现的高效网络通信
4. **序列化层**：支持多种序列化方式
5. **注册中心**：服务注册与发现
6. **负载均衡**：多种负载均衡策略

## 技术栈

- **Java 8+**：基础编程语言
- **Vertx**：高性能异步网络通信
- **Hutool**：国产工具库，提供配置加载等功能
- **Lombok**：简化代码编写
- **Logback**：日志管理

## 未来规划

- 支持更多序列化方式
- 增强服务注册中心功能
- 添加服务监控和熔断机制
- 支持微服务集群部署

## 贡献指南

欢迎为LenRPC项目贡献代码！请遵循以下步骤：

1. Fork本项目
2. 创建您的特性分支 (`git checkout -b feature/AmazingFeature`)
3. 提交您的更改 (`git commit -m 'Add some AmazingFeature'`)
4. 推送到分支 (`git push origin feature/AmazingFeature`)
5. 打开一个Pull Request

## 许可证

本项目采用MIT许可证 - 详情请参阅 [LICENSE](LICENSE) 文件

## 作者

- **程序员冷颜** - [GitHub](https://github.com/lenyanjgk)