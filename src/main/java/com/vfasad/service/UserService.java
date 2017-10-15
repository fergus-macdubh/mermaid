package com.vfasad.service;

import com.vfasad.entity.User;
import com.vfasad.repo.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.provider.OAuth2Authentication;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;

@Component
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
        return userRepository.getByEmail(details.get("email")).orElseThrow(() -> new AccessDeniedException("User is not found in database."));
    }

    public User updateUser(Map<String, String> details) {
        User user = userRepository.getByEmail(details.get(EMAIL))
                .orElse(new User());
        userRepository.save(user);

        user.setEmail(details.get(EMAIL));
        user.setName(details.get(NAME));
        user.setGivenName(details.get(GIVEN_NAME));
        user.setFamilyName(details.get(FAMILY_NAME));
        user.setPicture(details.get(PICTURE));
        user.setGender(details.get(GENDER));
        user.setLocale(details.get(LOCALE));

        return user;
    }

    public List<User> getAdmins() {
        return userRepository.getByRole("ROLE_ADMIN");
    }

    public List<User> getManagers() {
        return userRepository.getByRole("ROLE_SALES");
    }
}
