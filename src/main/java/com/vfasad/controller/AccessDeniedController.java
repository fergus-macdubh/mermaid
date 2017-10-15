package com.vfasad.controller;

import com.vfasad.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;

@Controller
public class AccessDeniedController {
    @Autowired
    private UserService userService;

    @RequestMapping(value = "/denied", method = RequestMethod.GET)
    public ModelAndView accessDenied() {
        ModelAndView model = new ModelAndView("error/access-denied");
        model.addObject("admins", userService.getAdmins());
        return model;
    }
}
