package com.lenyan.lenrpc.utils;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.util.StrUtil;
import cn.hutool.setting.dialect.Props;
import lombok.extern.slf4j.Slf4j;
import com.lenyan.lenrpc.constant.RpcConstant;
import java.io.File;

/**
 * 配置工具类
 * 支持多种格式配置文件(.yml/.yaml/.properties)的加载
 * 支持环境配置和配置文件变更自动更新
 */
@Slf4j
public class ConfigUtils {

    /**
     * 加载配置对象
     *
     * @param tClass 要加载配置的目标类
     * @param prefix 配置文件中的前缀，用于指定要加载的配置项范围
     * @param <T>    泛型参数，表示返回的配置对象类型
     * @return 返回加载了配置的对象实例
     */
    public static <T> T loadConfig(Class<T> tClass, String prefix) {
        // 调用带环境参数的方法，环境参数为空字符串
        return loadConfig(tClass, prefix, "");
    }

    /**
     * 加载配置对象，支持区分环境
     * 这个方法用于从配置文件中加载配置到指定的类对象中
     * 
     * @param tClass      要加载配置的目标类
     * @param prefix      配置文件中的前缀，用于指定要加载的配置项范围
     * @param environment 环境标识，用于加载不同环境的配置文件，如dev/prod等
     * @param <T>         泛型参数，表示返回的配置对象类型
     * @return 返回加载了配置的对象实例
     */
    public static <T> T loadConfig(Class<T> tClass, String prefix, String environment) {
        try {
            // 构建基础配置文件名,默认为"application"
            StringBuilder configFilePrefix = new StringBuilder("application");

            // 如果指定了环境，则在文件名中添加环境标识
            // 例如: application-dev.yml, application-prod.yml
            boolean hasEnvironment = StrUtil.isNotBlank(environment);
            if (hasEnvironment) {
                configFilePrefix.append("-").append(environment);
            }

            // 尝试加载不同格式的配置文件（包含环境的配置文件）
            Props props = tryLoadConfigFile(configFilePrefix.toString());

            // 如果带环境的配置文件不存在，并且指定了环境，则尝试加载默认配置文件（不包含环境）
            if (props == null && hasEnvironment) {
                log.info("未找到环境{}的配置文件，尝试加载默认配置文件", environment);
                props = tryLoadConfigFile("application");
            }

            // 如果所有尝试都失败，则返回默认配置对象
            if (props == null) {
                log.info("未找到任何配置文件，使用默认配置");
                // 创建空Props对象并转换为目标类型，此时会使用目标类的默认值
                return BeanUtil.toBean(new Props(), tClass);
            }

            // 将配置转换为目标类的实例并返回
            return BeanUtil.toBean(props, tClass);
        } catch (Exception e) {
            // 捕获所有异常，确保配置加载失败不会影响程序运行
            log.error("加载配置文件异常，使用默认配置", e);
            return BeanUtil.toBean(new Props(), tClass);
        }
    }

    /**
     * 尝试加载指定前缀的配置文件，支持多种文件格式
     *
     * @param configFilePrefix 配置文件前缀（不包含扩展名）
     * @return 加载的配置Props对象，如果所有格式都不存在，则返回null
     */
    private static Props tryLoadConfigFile(String configFilePrefix) {
        // 优先尝试从类路径加载（resources目录下）
        for (String extension : RpcConstant.SUPPORT_FILE_EXTENSIONS) {
            String configFileName = configFilePrefix + extension;
            try {
                // 使用Hutool工具类Props加载配置文件
                Props props = new Props(configFileName);
                log.info("成功加载配置文件: {}", configFileName);
                return props;
            } catch (Exception e) {
                // 这个格式的配置文件不存在，继续尝试下一个格式
                log.debug("未找到配置文件: {}", configFileName);
            }
        }

        // 如果从类路径加载失败，尝试从文件系统加载（绝对路径或相对路径）
        for (String extension : RpcConstant.SUPPORT_FILE_EXTENSIONS) {
            String configFileName = configFilePrefix + extension;
            File configFile = new File(configFileName);
            if (configFile.exists()) {
                try {
                    // 从文件系统加载配置
                    Props props = new Props(configFile);
                    log.info("成功加载文件系统配置文件: {}", configFile.getAbsolutePath());
                    return props;
                } catch (Exception e) {
                    // 文件存在但读取失败，可能是权限问题或格式错误
                    log.warn("读取文件系统配置文件失败: {}", configFile.getAbsolutePath(), e);
                }
            }
        }

        // 所有尝试都失败，返回null表示没有找到配置文件
        return null;
    }

    /**
     * 支持监听配置文件的变更，并自动更新配置对象
     * 使用 Hutool 工具类的 props.autoLoad() 实现配置文件变更的监听和自动加载
     *
     * @param props 要监听的配置对象
     */
    public static void autoLoad(Props props) {
        if (props == null) {
            log.warn("配置对象为空，无法启用自动加载功能");
            return;
        }

        try {
            // 启用配置文件自动加载
            props.autoLoad(true);
            log.info("已启用配置文件自动加载功能");
        } catch (Exception e) {
            log.error("启用配置文件自动加载功能失败", e);
        }
    }
}
