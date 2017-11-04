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
        return teamRepository.findById(teamId)
                .orElseThrow(() -> new NotFoundException("Team with provided id is not found."));
    }

    public List<User> getTeamUsers(Team team) {
        return userRepository.findAllByTeam(team);
    }

    public List<User> getTeamUsers(long teamId) {
        return getTeamUsers(getTeam(teamId));
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

    public void createTeam(String name) {
        Team team = new Team();
        team.setName(name);
        teamRepository.save(team);
    }
}
