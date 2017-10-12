package com.vfasad;


import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.security.oauth2.client.EnableOAuth2Sso;
import org.springframework.boot.autoconfigure.web.WebMvcAutoConfiguration;
import org.springframework.context.annotation.Import;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;

@SpringBootApplication
@EnableWebMvc
@EnableOAuth2Sso
@Import(WebSecurityConfig.class)
public class Application extends WebMvcAutoConfiguration {
    public static void main(String[] args) {
        SpringApplication.run(Application.class, args);
    }

    protected void configure(HttpSecurity http) throws Exception {
        http.csrf().disable();
    }
}
