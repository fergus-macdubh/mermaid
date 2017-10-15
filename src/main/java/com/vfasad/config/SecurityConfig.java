package com.vfasad.config;

import com.vfasad.security.AuthoritiesFilter;
import com.vfasad.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.security.oauth2.client.OAuth2SsoCustomConfiguration;
import org.springframework.boot.autoconfigure.security.oauth2.client.OAuth2SsoDefaultConfiguration;
import org.springframework.boot.autoconfigure.security.oauth2.client.OAuth2SsoProperties;
import org.springframework.boot.autoconfigure.security.oauth2.resource.ResourceServerTokenServicesConfiguration;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.core.Ordered;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.oauth2.config.annotation.web.configuration.EnableOAuth2Client;
import org.springframework.security.web.access.AccessDeniedHandler;

@Configuration
@EnableOAuth2Client
@EnableConfigurationProperties(OAuth2SsoProperties.class)
@Import({OAuth2SsoCustomConfiguration.class,
        ResourceServerTokenServicesConfiguration.class})
@EnableGlobalMethodSecurity(securedEnabled = true)
public class SecurityConfig extends OAuth2SsoDefaultConfiguration {
    public SecurityConfig(ApplicationContext applicationContext, OAuth2SsoProperties sso) {
        super(applicationContext, sso);
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http.exceptionHandling().accessDeniedHandler(getApplicationContext().getBean(AccessDeniedHandler.class));

        super.configure(http);
    }

    @Bean
    @Autowired
    public FilterRegistrationBean filterRegistrationBean(UserService userService) {
        FilterRegistrationBean registration = new FilterRegistrationBean();
        registration.setFilter(new AuthoritiesFilter(userService));
        registration.addUrlPatterns("/*");
        registration.setName("authoritiesFilter");
        registration.setOrder(Ordered.LOWEST_PRECEDENCE);
        return registration;
    }
}

