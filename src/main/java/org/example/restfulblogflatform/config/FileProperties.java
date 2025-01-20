package org.example.restfulblogflatform.config;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Getter
@Setter
@ConfigurationProperties(prefix = "file.upload")
@Configuration
public class FileProperties {
    private boolean enabled;
    private String location;
    private String maxFileSize;
    private String maxRequestSize;
    private String path;
}
