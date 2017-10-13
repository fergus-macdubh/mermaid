//package com.vfasad;
//
//
//import org.springframework.boot.autoconfigure.security.oauth2.client.OAuth2SsoCustomConfiguration;
//import org.springframework.boot.autoconfigure.security.oauth2.client.OAuth2SsoDefaultConfiguration;
//import org.springframework.boot.autoconfigure.security.oauth2.client.OAuth2SsoProperties;
//import org.springframework.boot.autoconfigure.security.oauth2.resource.ResourceServerTokenServicesConfiguration;
//import org.springframework.boot.context.properties.EnableConfigurationProperties;
//import org.springframework.context.ApplicationContext;
//import org.springframework.context.annotation.Configuration;
//import org.springframework.context.annotation.Import;
//import org.springframework.security.config.annotation.web.builders.HttpSecurity;
//import org.springframework.security.core.userdetails.UserDetails;
//import org.springframework.security.core.userdetails.UserDetailsService;
//import org.springframework.security.core.userdetails.UsernameNotFoundException;
//import org.springframework.security.oauth2.config.annotation.web.configuration.EnableOAuth2Client;
//
//@Configuration
//@EnableOAuth2Client
//@EnableConfigurationProperties(OAuth2SsoProperties.class)
//@Import({ OAuth2SsoCustomConfiguration.class, ResourceServerTokenServicesConfiguration.class })
//public class AuthConfig extends OAuth2SsoDefaultConfiguration {
//    public AuthConfig(ApplicationContext applicationContext, OAuth2SsoProperties sso) {
//        super(applicationContext, sso);
//    }
//
//    @Override
//    protected void configure(HttpSecurity http) throws Exception {
//        super.configure(http);
//
//        http.userDetailsService(new UserDetailsService() {
//            @Override
//            public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
//                System.out.println("AAAAAAAAAAAAAAAAAAAA");
//                return null;
//            }
//        });
//    }
//}
