package com.vfasad;

import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;

@EnableWebSecurity
@Configuration
public class WebSecurityConfig {
    protected void configure(HttpSecurity http) throws Exception {
        http.csrf().disable();
    }
}
