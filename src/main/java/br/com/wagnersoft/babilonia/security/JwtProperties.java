package br.com.wagnersoft.babilonia.security;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

import lombok.Data;

@Component
@ConfigurationProperties(prefix = "security.jwt")
@Data
public class JwtProperties {

    private String token;
    
}
