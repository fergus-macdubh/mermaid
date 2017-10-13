package com.vfasad.security;

import com.vfasad.entity.User;
import com.vfasad.repo.UserRepository;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.provider.OAuth2Authentication;
import org.springframework.web.filter.GenericFilterBean;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import java.io.IOException;
import java.util.Map;
import java.util.stream.Collectors;

public class AuthoritiesFilter extends GenericFilterBean {
    public static final String EMAIL = "email";
    public static final String NAME = "name";
    public static final String GIVEN_NAME = "given_name";
    public static final String FAMILY_NAME = "family_name";
    public static final String PICTURE = "picture";
    public static final String GENDER = "gender";
    public static final String LOCALE = "locale";

    private UserRepository userRepository;

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

        User user = userRepository.getByEmail(details.get(EMAIL))
                .orElse(new User());
        updateUser(user, details);
        userRepository.save(user);

        UsernamePasswordAuthenticationToken token = new UsernamePasswordAuthenticationToken(
                oAuth2Authentication.getPrincipal(),
                oAuth2Authentication.getCredentials(),
                user.getAuthorities().stream().map(SimpleGrantedAuthority::new).collect(Collectors.toList()));
        oAuth2Authentication = new OAuth2Authentication(oAuth2Authentication.getOAuth2Request(), token);
        oAuth2Authentication.setDetails(details);
        return oAuth2Authentication;
    }

    private void updateUser(User user, Map<String, String> details) {
        user.setEmail(details.get(EMAIL));
        user.setName(details.get(NAME));
        user.setGivenName(details.get(GIVEN_NAME));
        user.setFamilyName(details.get(FAMILY_NAME));
        user.setPicture(details.get(PICTURE));
        user.setGender(details.get(GENDER));
        user.setLocale(details.get(LOCALE));
    }

    public void setUserRepository(UserRepository userRepository) {
        this.userRepository = userRepository;
    }
}
