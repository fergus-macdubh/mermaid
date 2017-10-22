package com.vfasad.service;

import com.vfasad.entity.User;
import com.vfasad.exception.NotFoundException;
import com.vfasad.repo.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.provider.OAuth2Authentication;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.Collections;
import java.util.List;
import java.util.Map;

@Service
public class UserService {
    private static final String EMAIL = "email";
    private static final String NAME = "name";
    private static final String GIVEN_NAME = "given_name";
    private static final String FAMILY_NAME = "family_name";
    private static final String PICTURE = "picture";
    private static final String GENDER = "gender";
    private static final String LOCALE = "locale";

    @Autowired
    private UserRepository userRepository;

    public User getCurrentUser() {
        OAuth2Authentication authentication = (OAuth2Authentication) SecurityContextHolder.getContext().getAuthentication();
        if (authentication == null) {
            return null;
        }
        Map<String, String> details = (Map) authentication.getDetails();
        User user = userRepository.getByEmail(details.get("email"))
                .orElse(new User(details.get("email"), "Незнакомец", "Незнакомец", "", "", "", ""));

        updateGrantedAuthorities(authentication, user.getRole());

        return user;
    }

    private void updateGrantedAuthorities(OAuth2Authentication authentication, String role) {
        if (authentication.getAuthorities().size() == 0
                || ((GrantedAuthority) authentication.getAuthorities().toArray()[0]).getAuthority().equals(role)) {
            return;
        }

        UsernamePasswordAuthenticationToken token = new UsernamePasswordAuthenticationToken(
                authentication.getPrincipal(),
                authentication.getCredentials(),
                StringUtils.isEmpty(role)
                        ? Collections.emptyList()
                        : Collections.singletonList(new SimpleGrantedAuthority(role)));
        authentication = new OAuth2Authentication(authentication.getOAuth2Request(), token);
        authentication.setDetails(authentication.getDetails());
    }

    public User updateUser(Map<String, String> details) {
        User user = userRepository.getByEmail(details.get(EMAIL))
                .orElse(new User());

        user.setEmail(details.get(EMAIL));
        user.setName(details.get(NAME));
        user.setGivenName(details.get(GIVEN_NAME));
        user.setFamilyName(details.get(FAMILY_NAME));
        user.setPicture(details.get(PICTURE));
        user.setGender(details.get(GENDER));
        user.setLocale(details.get(LOCALE));

        return userRepository.save(user);
    }

    public List<User> getAdmins() {
        return userRepository.getByRoleIn("ROLE_ADMIN");
    }

    public List<User> getManagers() {
        return userRepository.getByRoleIn("ROLE_SALES", "ROLE_OPERATOR");
    }

    public List<User> findAll() {
        return userRepository.findAllByOrderByEmail();
    }

    public void updateUserRole(Long userId, String role) {
        User user = getUser(userId);
        user.setRole(role);
        userRepository.save(user);
    }

    public User getUser(Long id) {
        return userRepository.getById(id).orElseThrow(() -> new NotFoundException("User with provided id is not found."));
    }
}
