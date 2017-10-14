package com.vfasad.security;

import com.vfasad.entity.User;
import com.vfasad.repo.UserRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.provider.OAuth2Authentication;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Map;

@Slf4j
public class InjectUserInterceptor extends HandlerInterceptorAdapter {
    private UserRepository userRepository;

    public InjectUserInterceptor(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        log.info("Processing request [{}:{}]", request.getRequestURI(), request.getMethod());

        return true;
    }

    @Override
    public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler, ModelAndView modelAndView) throws Exception {
        if (modelAndView == null) {
            return;
        }

        log.info("Postprocessing view [{}]", modelAndView.getViewName());

        OAuth2Authentication authentication = (OAuth2Authentication) SecurityContextHolder.getContext().getAuthentication();
        Map<String, String> details = (Map) authentication.getDetails();
        User user = userRepository.getByEmail(details.get("email")).orElseThrow(() -> new AccessDeniedException("User is not found in database."));
        modelAndView.addObject("user", user);

        log.info("User [{}] is added to model", user.getName());
    }
}
