package com.vfasad.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.core.annotation.AnnotationUtils;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.validation.ConstraintViolation;
import javax.validation.ConstraintViolationException;

import static java.util.stream.Collectors.toList;

@Slf4j
@ControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(AccessDeniedException.class)
    public String accessDenied(HttpServletRequest req, AccessDeniedException e) throws Exception {
        log.warn("Exception is caught.", e);
        return "redirect:/denied";
    }

    @ExceptionHandler(ConstraintViolationException.class)
    public ModelAndView constraintViolation(HttpServletRequest req, ConstraintViolationException e) throws Exception {
        log.warn("Exception is caught.", e);
        ModelAndView mav = new ModelAndView("error/error");
        mav.addObject("messages", e.getConstraintViolations()
                .stream()
                .map(ConstraintViolation::getMessage)
                .collect(toList()));
        mav.addObject("url", req.getRequestURL());
        return mav;
    }

    @ExceptionHandler(Exception.class)
    public ModelAndView defaultErrorHandler(HttpServletRequest req, Exception e) throws Exception {
        log.warn("Exception is caught.", e);
        // If the exception is annotated with @ResponseStatus rethrow it and let
        // the framework handle it - like the OrderNotFoundException example
        // at the start of this post.
        // AnnotationUtils is a Spring Framework utility class.
        if (AnnotationUtils.findAnnotation
                (e.getClass(), ResponseStatus.class) != null)
            throw e;

        // Otherwise setup and send the user to a default error-view.
        ModelAndView mav = new ModelAndView("error/error");
        mav.addObject("messages", new String[]{e.getMessage()});
        mav.addObject("url", req.getRequestURL());
        return mav;
    }
}
