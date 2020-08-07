package com.jayud.file.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.cloud.context.config.annotation.RefreshScope;

import java.util.List;

/**
 * @author larry
 * 配置文件
 */
@Data
@RefreshScope
@ConfigurationProperties(prefix = "file")
public class FdfsConfig {
    private String baseUrl;
    private List<String> allowImageTypes;
    private List<String> allowFileTypes;
}
