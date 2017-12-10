package com.vfasad.service;

import com.vfasad.entity.Team;
import com.vfasad.entity.User;
import com.vfasad.exception.NotFoundException;
import com.vfasad.repo.TeamRepository;
import com.vfasad.repo.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class TeamService {
    @Autowired
    TeamRepository teamRepository;

    @Autowired
    UserRepository userRepository;

    public List<Team> findAll() {
        return teamRepository.findAllByOrderByName();
    }

    public Team getTeam(Long teamId) {
        if (teamId == null) {
            return null;
        }

        return teamRepository.findById(teamId)
                .orElseThrow(() -> new NotFoundException("Team with provided id is not found."));
    }

    public List<User> getTeamUsers(long teamId) {
        return userRepository.findAllByTeam(getTeam(teamId));
    }

    public void updateTeam(Team team) {
        teamRepository.save(team);
    }

    public void delete(Team team) {
        getTeamUsers(team).forEach(
                user -> {
                    user.setTeam(null);
                    userRepository.save(user);
                });
        teamRepository.delete(team);
    }

    public void createTeam(String name, String color) {
        Team team = new Team();
        team.setName(name);
        team.setColor(color);
        teamRepository.save(team);
    }

    private List<User> getTeamUsers(Team team) {
        return userRepository.findAllByTeam(team);
    }
}
