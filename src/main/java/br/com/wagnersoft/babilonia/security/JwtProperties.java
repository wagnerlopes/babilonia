package br.com.wagnersoft.babilonia.security;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

import lombok.Data;

/** 
 * Leitura e armazenamento propriedades de seguranção do application.yml.
 * 
 * @author Wagner Lopes
 * @since 1.0
 * @version 1.0
 */
@Component
@ConfigurationProperties(prefix = "security.jwt")
@Data
public class JwtProperties {

  private String token;

}
