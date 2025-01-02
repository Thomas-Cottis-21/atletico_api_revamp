package com.atletico.atletico_revamp.config;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.PropertySource;
import org.springframework.stereotype.Component;

@Getter
@Setter
@Component
@PropertySource("classpath:application-dev.properties")
@ConfigurationProperties(prefix = "jwt")
public class JwtConfigurationProperties {

    private String secretKey;
    private long validity;

}
