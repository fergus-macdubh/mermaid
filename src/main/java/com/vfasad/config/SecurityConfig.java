package com.vfasad.config;

import com.vfasad.security.AuthoritiesFilter;
import com.vfasad.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.security.oauth2.client.EnableOAuth2Sso;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.Ordered;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;

@Configuration
@EnableOAuth2Sso
@EnableGlobalMethodSecurity(securedEnabled = true)
public class SecurityConfig {
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

