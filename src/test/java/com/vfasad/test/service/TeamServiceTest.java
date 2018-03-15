package com.vfasad.test.service;

import com.vfasad.entity.Team;
import com.vfasad.entity.User;
import com.vfasad.exception.NotFoundException;
import com.vfasad.repo.TeamRepository;
import com.vfasad.repo.UserRepository;
import com.vfasad.service.TeamService;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.mockito.Mockito.*;
import static org.junit.Assert.*;

@RunWith(MockitoJUnitRunner.class)
public class TeamServiceTest {
    private static final String TEAM_NAME = "team";
    private static final String TEAM_COLOR = "color";

    @Mock
    private TeamRepository teamRepository;

    @Mock
    private UserRepository userRepository;

    @InjectMocks
    private TeamService teamService;

    @Rule
    public ExpectedException expectedException = ExpectedException.none();

    @Test
    public void testFindAll() {
        List<Team> teamList = generateTeamList();

        when(teamRepository.findAllByOrderByName()).thenReturn(teamList);

        List<Team> teamListResult = teamService.findAll();
        assertEquals("Test teams should be equal", teamList, teamListResult);
    }

    @Test
    public void testGetTeam() {
        Team team = generateTeam(TEAM_NAME, TEAM_COLOR);
        when(teamRepository.findById(anyLong())).thenReturn(Optional.of(team));

        Team teamResult = teamService.getTeam(1L);
        assertNotNull("Team shouldn't be null", teamResult);
        assertEquals("Incorrect team name", teamResult.getName(), TEAM_NAME);
        assertEquals("Incorrect team color", teamResult.getColor(), TEAM_COLOR);

        teamResult = teamService.getTeam(null);
        assertNull("For null input id returned value should be null", teamResult);
    }

    @Test
    public void testGetTeamNotFound() {
        expectedException.expect(NotFoundException.class);
        expectedException.expectMessage("Team with provided id is not found.");

        when(teamRepository.findById(anyLong())).thenReturn(Optional.empty());
        teamService.getTeam(1L);
    }

    @Test
    public void testGetTeamUsers() {
        List<User> userList = generateUserList();

        when(userRepository.findAllByTeam(any(Team.class))).thenReturn(userList);
        when(teamRepository.findById(anyLong())).thenReturn(Optional.of(new Team()));

        List<User> userListResult = teamService.getTeamUsers(1L);
        assertEquals("getTeamUsers return incorrect userList", userList, userListResult);
    }

    @Test
    public void testDeleteTeam() {
        Team team = generateTeam(TEAM_NAME, TEAM_COLOR);
        List<User> userList = generateUserList();

        when(userRepository.findAllByTeam(any(Team.class))).thenReturn(userList);
        teamService.delete(team);

        userList.forEach(user -> {
            assertNull("User's team should be null", user.getTeam());
            verify(userRepository, times(1)).save(user);
        });
        verify(teamRepository, times(1)).delete(team);
    }

    @Test
    public void testUpdateTeam() {
        Team team = generateTeam(TEAM_NAME, TEAM_COLOR);
        teamService.updateTeam(team);
        verify(teamRepository, times(1)).save(team);
    }

    @Test
    public void testCreateTeam() {
        Team team = generateTeam(TEAM_NAME, TEAM_COLOR);
        teamService.createTeam(TEAM_NAME, TEAM_COLOR);
        verify(teamRepository, times(1)).save(team);
    }

    private Team generateTeam(String name, String color) {
        Team team = new Team();
        team.setName(name);
        team.setColor(color);

        return team;
    }

    private List<User> generateUserList() {
        Team team = generateTeam(TEAM_NAME, TEAM_COLOR);
        List<User> userList = new ArrayList<>();
        userList.add(new User("test@gmail.com","Name","GivenName","FamilyName",null,"female","en"));
        userList.add(new User("test2@gmail.com","Name2","GivenName2","FamilyName2",null,"female","en"));
        userList.forEach(user -> user.setTeam(team));

        return userList;
    }

    private List<Team> generateTeamList() {
        List<Team> teamList = new ArrayList<>();
        teamList.add(generateTeam("team1","blue"));
        teamList.add(generateTeam("team2","red"));

        return teamList;
    }
}
