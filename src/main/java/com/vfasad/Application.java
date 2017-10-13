package com.vfasad;


import org.springframework.beans.BeansException;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.web.WebMvcAutoConfiguration;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.context.annotation.Import;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;

@SpringBootApplication
@EnableWebMvc
@Import(AuthConfig.class)
public class Application extends WebMvcAutoConfiguration implements ApplicationContextAware {


    public static void main(String[] args) {

        SpringApplication.run(Application.class, args);
    }
//
//    @Bean
//    @Autowired
//    public FilterRegistrationBean filterRegistrationBean(UserRepository userRepository) {
//        FilterRegistrationBean registration = new FilterRegistrationBean();
//        AutohoritiesFilter filter = new AutohoritiesFilter();
//        filter.setUserRepository(userRepository);
//        registration.setFilter(filter);
//        registration.addUrlPatterns("/*");
//        registration.setName("authoritiesFilter");
//        return registration;
//    }

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        System.out.println("BEANS:");
        for (String bean : applicationContext.getBeanDefinitionNames()) {
            System.out.println(bean);
        }
    }

}
