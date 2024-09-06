package com.sky.properties;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Component
@ConfigurationProperties(prefix = "sky.reids")
@Data
public class RedisProperties {
    // token有效期
    private long LOGIN_TTL;
}
