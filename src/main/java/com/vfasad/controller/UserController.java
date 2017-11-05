package com.vfasad.controller;

import com.vfasad.entity.User;
import com.vfasad.service.TeamService;
import com.vfasad.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.annotation.Secured;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
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
            @RequestParam
                    Long teamId) {
        userService.updateUserTeam(userId, teamId);
        return "redirect:/users";
    }

    @GetMapping("/users/add")
    @Secured(ROLE_ADMIN)
    public ModelAndView addOrderForm() {
        ModelAndView modelAndView = new ModelAndView("users/user-form");
        modelAndView.addObject("teams", teamService.findAll());
        return modelAndView;
    }

    @PostMapping("/users/add")
    @Secured(ROLE_ADMIN)
    public String addOrder(
            @RequestParam String name,
            @RequestParam String email,
            @RequestParam String givenName,
            @RequestParam String familyName,
            @RequestParam String role,
            @RequestParam(required = false) Long teamId) {
        User user = new User(email, name, givenName, familyName, null, "male", "en");
        user.setRole(role);
        user.setTeam(teamService.getTeam(teamId));
        userService.updateUser(user);
        return "redirect:/users";
    }

    @RequestMapping(value = "/users/{id}/edit", method = RequestMethod.GET)
    @Secured(ROLE_ADMIN)
    public ModelAndView editProductForm(@PathVariable Long id) {
        ModelAndView modelAndView = new ModelAndView("users/user-form");
        modelAndView.addObject("targetUser", userService.getUser(id));
        modelAndView.addObject("teams", teamService.findAll());
        return modelAndView;
    }

    @RequestMapping(value = "/users/{id}/edit", method = RequestMethod.POST)
    @Secured(ROLE_ADMIN)
    public String updateOrder(
            @PathVariable Long id,
            @RequestParam String name,
            @RequestParam String email,
            @RequestParam String givenName,
            @RequestParam String familyName,
            @RequestParam String role,
            @RequestParam(required = false) Long teamId) {
        User user = userService.getUser(id);
        user.setName(name);
        user.setGivenName(givenName);
        user.setFamilyName(familyName);
        user.setTeam(teamService.getTeam(teamId));

        if (!user.getRole().equals(ROLE_ADMIN)) {
            user.setEmail(email);
            user.setRole(role);
        }
        userService.updateUser(user);
        return "redirect:/users";
    }

    @GetMapping("/users/{id}/delete")
    @Secured(ROLE_ADMIN)
    public String deleteOrder(
            @PathVariable Long id) {
        User user = userService.getUser(id);

        if (user.getRole().equals(ROLE_ADMIN)) {
            throw new IllegalArgumentException("User with role ROLE_ADMIN cannot be deleted. Id [" + id + "]");
        }

        user.setDeleted(true);
        userService.updateUser(user);
        return "redirect:/users";
    }
}
