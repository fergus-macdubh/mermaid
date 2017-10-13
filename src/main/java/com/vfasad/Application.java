package com.vfasad;


import com.vfasad.repo.UserRepository;
import com.vfasad.security.AuthoritiesFilter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.security.oauth2.client.EnableOAuth2Sso;
import org.springframework.boot.autoconfigure.web.WebMvcAutoConfiguration;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.core.Ordered;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;

@SpringBootApplication
@EnableWebMvc
@EnableOAuth2Sso
@EnableGlobalMethodSecurity(securedEnabled = true)
//@Import(AuthConfig.class)
public class Application extends WebMvcAutoConfiguration {
    public static void main(String[] args) {
        SpringApplication.run(Application.class, args);
    }

    @Bean
    @Autowired
    public FilterRegistrationBean filterRegistrationBean(UserRepository userRepository) {
        FilterRegistrationBean registration = new FilterRegistrationBean();
        AuthoritiesFilter filter = new AuthoritiesFilter();
        filter.setUserRepository(userRepository);
        registration.setFilter(filter);
        registration.addUrlPatterns("/*");
        registration.setName("authoritiesFilter");
        registration.setOrder(Ordered.LOWEST_PRECEDENCE);
        return registration;
    }
}
