package com.vfasad.controller;

import com.vfasad.service.TeamService;
import com.vfasad.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.annotation.Secured;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import static com.vfasad.entity.User.ROLE_ADMIN;

@Controller
public class UserController {
    @Autowired
    private UserService userService;
    @Autowired
    private TeamService teamService;

    @GetMapping("/users")
    @Secured(ROLE_ADMIN)
    public ModelAndView products() {
        ModelAndView model = new ModelAndView("users/users");
        model.addObject("users", userService.findAll());
        model.addObject("teams", teamService.findAll());
        return model;
    }

    @PostMapping("/users/role")
    @Secured(ROLE_ADMIN)
    public String updateUserRole(
            @RequestParam Long userId,
            @RequestParam String role) {
        userService.updateUserRole(userId, role);
        return "redirect:/users";
    }

    @PostMapping("/users/team")
    @Secured(ROLE_ADMIN)
    public String updateUserTeam(
            @RequestParam Long userId,
            @RequestParam Long teamId) {
        userService.updateUserTeam(userId, teamId);
        return "redirect:/users";
    }
}
