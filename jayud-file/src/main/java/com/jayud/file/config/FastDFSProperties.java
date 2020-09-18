package com.jayud.file.config;

import com.github.tobato.fastdfs.FdfsClientConfig;
import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.EnableMBeanExport;
import org.springframework.context.annotation.Import;
import org.springframework.jmx.support.RegistrationPolicy;
import org.springframework.stereotype.Component;

@Data
@Component
@Import(FdfsClientConfig.class)
@EnableMBeanExport(registration = RegistrationPolicy.IGNORE_EXISTING)
@ConfigurationProperties(prefix = "fdfs")
public class FastDFSProperties {
    private String resHost;
    private String storagePort;
    private String serverPath;
}