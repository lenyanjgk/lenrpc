package com.lenyan.lenrpc.constant;

/**
 * RPC 相关常量
 *
 * @author <a href="https://github.com/lenyanjgk">程序员冷颜</a>
 */
public interface RpcConstant {

    /**
     * 默认配置文件加载前缀
     */
    String DEFAULT_CONFIG_PREFIX = "rpc";

    /**
     * 支持的配置文件格式
     */
    String[] SUPPORT_FILE_EXTENSIONS = { ".yml", ".yaml", ".properties" };
}
