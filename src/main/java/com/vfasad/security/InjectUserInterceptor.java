package com.vfasad.security;

import com.vfasad.entity.User;
import com.vfasad.service.UserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.provider.OAuth2Authentication;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@Slf4j
public class InjectUserInterceptor extends HandlerInterceptorAdapter {
    private UserService userService;

    public InjectUserInterceptor(UserService userService) {
        this.userService = userService;
    }

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        log.info("Processing request [{}:{}]", request.getRequestURI(), request.getMethod());

        OAuth2Authentication authentication = (OAuth2Authentication) SecurityContextHolder.getContext().getAuthentication();
        User user = userService.getCurrentUser();

        if (user == null) {
            return false;
        }

        if (userService.isRoleChanged(authentication, user.getRole())) {
            userService.updateAuthorities(authentication, user.getRole());
        }

        return true;
    }

    @Override
    public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler, ModelAndView modelAndView) throws Exception {
        if (modelAndView == null) {
            return;
        }

        log.info("Postprocessing view [{}]", modelAndView.getViewName());

        User user = userService.getCurrentUser();
        modelAndView.addObject("user", user);

        log.info("User [{}] is added to model", user.getName());
    }
}
