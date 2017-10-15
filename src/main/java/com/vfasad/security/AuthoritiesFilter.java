package com.vfasad.security;

import com.vfasad.entity.User;
import com.vfasad.service.UserService;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.provider.OAuth2Authentication;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.GenericFilterBean;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import java.io.IOException;
import java.util.Collections;
import java.util.Map;

public class AuthoritiesFilter extends GenericFilterBean {
    private UserService userService;

    public AuthoritiesFilter(UserService userService) {
        this.userService = userService;
    }

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        OAuth2Authentication oAuth2Authentication = (OAuth2Authentication) authentication;

        if (oAuth2Authentication != null && oAuth2Authentication.getUserAuthentication().getDetails() != null) {
            SecurityContextHolder.getContext().setAuthentication(processAuthentication(authentication));
        }

        chain.doFilter(request, response);
    }

    private OAuth2Authentication processAuthentication(Authentication authentication) {
        OAuth2Authentication oAuth2Authentication = (OAuth2Authentication) authentication;
        Map<String, String> details = (Map<String, String>) oAuth2Authentication.getUserAuthentication().getDetails();

        User user = userService.updateUser(details);

        UsernamePasswordAuthenticationToken token = new UsernamePasswordAuthenticationToken(
                oAuth2Authentication.getPrincipal(),
                oAuth2Authentication.getCredentials(),
                StringUtils.isEmpty(user.getRole())
                        ? Collections.emptyList()
                        : Collections.singletonList(new SimpleGrantedAuthority(user.getRole())));
        oAuth2Authentication = new OAuth2Authentication(oAuth2Authentication.getOAuth2Request(), token);
        oAuth2Authentication.setDetails(details);
        return oAuth2Authentication;
    }
}
