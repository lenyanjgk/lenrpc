package com.lenyan.lenrpc.config;

import lombok.Data;

/**
 * RPC 框架配置
 */
@Data
public class RpcConfig {

    /**
     * 名称
     */
    private String name = "len-rpc";

    /**
     * 版本号
     */
    private String version = "1.0";

    /**
     * 服务器主机名
     */
    private String serverHost = "localhost";

    /**
     * 服务器端口号
     */
    private Integer serverPort = 8080;

    /**
     * 服务描述
     */
    private String description;

    /**
     * 作者
     */
    private String author;

    /**
     * 公司
     */
    private String company;

    /**
     * 地址
     */
    private String address;
}
