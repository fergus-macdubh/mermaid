package com.vfasad.controller;

import com.vfasad.entity.Team;
import com.vfasad.entity.User;
import com.vfasad.service.TeamService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.annotation.Secured;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static com.vfasad.entity.User.ROLE_ADMIN;
import static com.vfasad.entity.User.ROLE_OPERATOR;

@Controller
public class TeamController {
    @Autowired
    private TeamService teamService;

    @GetMapping("/teams")
    @Secured({ROLE_ADMIN, ROLE_OPERATOR})
    public ModelAndView teams() {
        ModelAndView modelAndView = new ModelAndView("users/teams");
        List<Team> teams = teamService.findAll();
        Map<String, List<User>> teamsWithUsers = new HashMap<>();
        teams.forEach(t -> teamsWithUsers.put(t.getId().toString(), teamService.getTeamUsers(t)));
        modelAndView.addObject("teams", teams);
        modelAndView.addObject("users", teamsWithUsers);
        return modelAndView;
    }

    @GetMapping("/teams/add")
    @Secured({ROLE_ADMIN, ROLE_OPERATOR})
    public ModelAndView addTeam() {
        return new ModelAndView("users/team-form");
    }

    @PostMapping("/teams/add")
    @Secured({ROLE_ADMIN, ROLE_OPERATOR})
    public String addTeam(
            @RequestParam String name) {
        teamService.createTeam(name);
        return "redirect:/teams";
    }

    @GetMapping("/teams/{id}/edit")
    @Secured({ROLE_ADMIN, ROLE_OPERATOR})
    public ModelAndView editTeam(@PathVariable Long id) {
        ModelAndView modelAndView = new ModelAndView("users/team-form");
        modelAndView.addObject("team", teamService.getTeam(id));
        return modelAndView;
    }

    @PostMapping("/teams/{id}/edit")
    @Secured({ROLE_ADMIN, ROLE_OPERATOR})
    public String updateTeam(
            @PathVariable Long id,
            @RequestParam String name) {
        Team team = teamService.getTeam(id);
        team.setName(name);
        teamService.updateTeam(team);
        return "redirect:/teams";
    }

    @GetMapping("/teams/{id}/delete")
    public String updateTeam(
            @PathVariable Long id) {
        Team team = teamService.getTeam(id);
        teamService.delete(team);
        return "redirect:/teams";
    }
}
