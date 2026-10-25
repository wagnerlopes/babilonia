package br.com.wagnersoft.babilonia.config;

import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.filter.CorsFilter;

import br.com.wagnersoft.babilonia.security.ApiKeyFilter;

@Configuration
public class FilterOrderConfig {

    @Bean
    public FilterRegistrationBean<CorsFilter> corsFilterRegistration(CorsFilter corsFilter) {
        FilterRegistrationBean<CorsFilter> registrationBean = new FilterRegistrationBean<>(corsFilter);
        registrationBean.setOrder(0); // prioridade máxima, roda primeiro
        return registrationBean;
    }

    @Bean
    public FilterRegistrationBean<ApiKeyFilter> apiKeyFilterRegistration(ApiKeyFilter apiKeyFilter) {
        FilterRegistrationBean<ApiKeyFilter> registrationBean = new FilterRegistrationBean<>(apiKeyFilter);
        registrationBean.setOrder(1); // roda depois do CorsFilter
        return registrationBean;
    }

}
