package com.vfasad.controller;

import com.vfasad.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.annotation.Secured;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import static com.vfasad.entity.User.ROLE_ADMIN;

@Controller
public class UserController {
    @Autowired
    private UserService userService;

    @RequestMapping(value = "/users", method = RequestMethod.GET)
    @Secured(ROLE_ADMIN)
    public ModelAndView products() {
        ModelAndView model = new ModelAndView("users/users");
        model.addObject("users", userService.findAll());
        return model;
    }

    @RequestMapping(value = "/users", method = RequestMethod.POST)
    @Secured(ROLE_ADMIN)
    public String updateUser(
            @RequestParam Long userId,
            @RequestParam String role) {
        userService.updateUserRole(userId, role);
        return "redirect:/users";
    }
}
