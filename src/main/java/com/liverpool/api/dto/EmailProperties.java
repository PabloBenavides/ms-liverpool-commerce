package com.liverpool.api.dto;

import lombok.Getter;
import lombok.Setter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;
import org.springframework.stereotype.Component;

@Configuration
@Getter
@Setter
@Component
@ConfigurationProperties(prefix = "email")
public class EmailProperties {

    @Value("${spring.mail.host}")
    private String host;

    @Value("${spring.mail.port}")
    private Integer port;
    
    @Value("${spring.mail.email_user}")
    private String email_user;
    
    @Value("${spring.mail.email_password}")
    private String email_password;
    
    @Value("${spring.mail.protocol}")
    private String protocol;
    
    @Value("${spring.mail.ssl}")
    private String ssl;

}
