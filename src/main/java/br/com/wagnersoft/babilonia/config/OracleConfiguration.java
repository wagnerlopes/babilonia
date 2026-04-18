package br.com.wagnersoft.babilonia.config;

import java.sql.SQLException;

import javax.sql.DataSource;
import jakarta.validation.constraints.NotNull;

//import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
//import org.springframework.context.annotation.Configuration;

import lombok.Setter;

/** OracleConfiguration para configuração datasource Oracle.
 * <p>Desabilitado, podendo ser utilizado caso haja necessidade de alterar parâmetros default.
 * @author WagnerSoft
 * @since 0.1
 * @version 0.1
 */
@Setter
//@Configuration
//@ConfigurationProperties("spring.oracle")
public class OracleConfiguration {

  @NotNull
  private String username;

  @NotNull
  private String password;

  @NotNull
  private String url;

  @NotNull
  private String factoryClassName;

  @Bean
  public DataSource dataSource() throws SQLException {
    /*
     * final PoolDataSource pds = PoolDataSourceFactory.getPoolDataSource();
     * pds.setURL(url); pds.setUser(username); pds.setPassword(password);
     * pds.setConnectionFactoryClassName(factoryClassName);
     * pds.setConnectionPoolName("DB_POOL");
     * pds.setDatabaseName("DB_PROD");
     * pds.setAbandonedConnectionTimeout(1000);
     * pds.setConnectionWaitTimeout(120);
     * pds.setFastConnectionFailoverEnabled(true);
     * pds.setInactiveConnectionTimeout(1000); pds.setInitialPoolSize(10);
     * pds.setMinPoolSize(20); pds.setMaxPoolSize(1000);
     * pds.setConnectionProperty("v$session.program", "BABILONIA");
     * pds.setValidateConnectionOnBorrow(true);
     * pds.setSQLForValidateConnection("SELECT 1 FROM DUAL"); return pds;
     * 
     */
    return null;
    }

}
