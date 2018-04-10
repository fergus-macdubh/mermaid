package com.vfasad.test.controller;

import com.vfasad.controller.UserController;
import com.vfasad.entity.Team;
import com.vfasad.entity.User;
import com.vfasad.service.TeamService;
import com.vfasad.service.UserService;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.util.ArrayList;
import java.util.List;

import static org.mockito.Mockito.*;
import static org.junit.Assert.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;


@RunWith(MockitoJUnitRunner.class)
public class UserControllerTest {

    @Mock
    private TeamService teamService;

    @Mock
    private UserService userService;

    private MockMvc mockMvc;

    @Rule
    public ExpectedException expectedException = ExpectedException.none();

    @InjectMocks
    private UserController userController;

    @Before
    public void setUp() {
        this.mockMvc = MockMvcBuilders.standaloneSetup(userController).build();
    }

    @Test
    public void testUsers() throws Exception {
        List<User> userList = generateUserList();
        List<Team> teamList = generateTeamList();
        when(userService.findAll()).thenReturn(userList);
        when(teamService.findAll()).thenReturn(teamList);

        mockMvc.perform(
                get("/users"))
                .andExpect(status().isOk())
                .andExpect(view().name("users/users"))
                .andExpect(model().attributeExists("users"))
                .andExpect(model().attribute("users", userList))
                .andExpect(model().attributeExists("teams"))
                .andExpect(model().attribute("teams", teamList));
    }

    @Test
    public void testUpdateUserRole() throws Exception {
        String role = "ADMIN";
        Long userId = 1L;

        mockMvc.perform(post("/users/role")
                .param("userId", userId.toString())
                .param("role", role))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/users"));
        verify(userService, times(1)).updateUserRole(userId, role);
    }

    @Test
    public void testUpdateUserTeam() throws Exception {
        Long userId = 1L;
        Long teamId = 1L;

        mockMvc.perform(post("/users/team")
                .param("userId", userId.toString())
                .param("teamId", teamId.toString()))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/users"));
        verify(userService, times(1)).updateUserTeam(userId, teamId);
    }

    @Test
    public void testAddUserForm() throws Exception {
        List<Team> teamList = generateTeamList();
        when(teamService.findAll()).thenReturn(teamList);

        mockMvc.perform(get("/users/add"))
                .andExpect(status().isOk())
                .andExpect(view().name("users/user-form"))
                .andExpect(model().attributeExists("teams"))
                .andExpect(model().attribute("teams", teamList));
    }

    @Test
    public void testAddUser() throws Exception {
        when(teamService.getTeam(anyLong())).thenReturn(new Team());

        mockMvc.perform(post("/users/add")
                .param("name", "Name")
                .param("email", "email")
                .param("givenName", "givenName")
                .param("familyName", "familyName")
                .param("role", "role")
                .param("teamId", "1"))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/users"));

        verify(userService, times(1)).updateUser(any(User.class));
    }

    @Test
    public void testEditUserForm() throws Exception {
        User user = generateUser();
        List<Team> teamList = generateTeamList();
        when(userService.getUser(anyLong())).thenReturn(user);
        when(teamService.findAll()).thenReturn(teamList);

        mockMvc.perform(get("/users/{id}/edit", 1))
                .andExpect(status().isOk())
                .andExpect(view().name("users/user-form"))
                .andExpect(model().attributeExists("targetUser"))
                .andExpect(model().attribute("targetUser", user))
                .andExpect(model().attributeExists("teams"))
                .andExpect(model().attribute("teams", teamList));
    }

    @Test
    public void testUpdateUserAdmin() throws Exception {
        User user = generateUser();
        user.setRole("ROLE_ADMIN");
        Team team = generateTeam("name", "color");
        when(userService.getUser(anyLong())).thenReturn(user);
        when(teamService.getTeam(anyLong())).thenReturn(team);

        testUpdateUser(user, team);
        assertNotEquals("User email shouldn't be updated for ROLE_ADMIN", "newEmail", user.getEmail());
        assertNotEquals("User role shouldn't be updated for ROLE_ADMIN", "ROLE_NEW", user.getRole());
    }

    @Test
    public void testUpdateUserNotAdmin() throws Exception {
        User user = generateUser();
        user.setRole("ROLE_USER");
        Team team = generateTeam("name", "color");
        when(userService.getUser(anyLong())).thenReturn(user);
        when(teamService.getTeam(anyLong())).thenReturn(team);

        testUpdateUser(user, team);
        assertEquals("Invalid user email", "newEmail", user.getEmail());
        assertEquals("Invalid user role", "ROLE_NEW", user.getRole());
    }

    private void testUpdateUser(User user, Team team) throws Exception {
        mockMvc.perform(post("/users/{id}/edit", 1)
                .param("name", "newName")
                .param("email", "newEmail")
                .param("givenName", "newGivenName")
                .param("familyName", "newFamilyName")
                .param("role", "ROLE_NEW")
                .param("teamId", "1"))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/users"));

        assertEquals("Invalid user name", "newName", user.getName());
        assertEquals("Invalid user givenName", "newGivenName", user.getGivenName());
        assertEquals("Invalid user familyName", "newFamilyName", user.getFamilyName());
        assertEquals("Invalid user team", team, user.getTeam());
        verify(userService, times(1)).updateUser(user);
    }

    @Test
    public void testDeleteUser() throws Exception {
        Long userId = 1L;
        User user = generateUser();
        user.setRole("ROLE_USER");
        user.setDeleted(false);
        when(userService.getUser(anyLong())).thenReturn(user);

        mockMvc.perform(get("/users/{id}/delete", userId))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/users"));
        assertTrue("User deleted flag is invalid", user.isDeleted());
        verify(userService, times(1)).updateUser(user);
    }

    @Test
    public void testDeleteUserException() {
        Long userId = 1L;
        expectedException.expect(IllegalArgumentException.class);
        expectedException.expectMessage("User with role ROLE_ADMIN cannot be deleted. Id [" + userId + "]");
        User user = generateUser();
        user.setRole("ROLE_ADMIN");
        when(userService.getUser(anyLong())).thenReturn(user);

        userController.deleteUser(userId);
    }

    private Team generateTeam(String name, String color) {
        Team team = new Team();
        team.setName(name);
        team.setColor(color);

        return team;
    }

    private List<Team> generateTeamList() {
        List<Team> teamList = new ArrayList<>();
        teamList.add(generateTeam("team1","blue"));
        teamList.add(generateTeam("team2","red"));

        return teamList;
    }

    private User generateUser() {
        User user = new User("email","name1","givenName1","familyName1",null,"female","en");
        return user;
    }

    private List<User> generateUserList() {
        List<User> userList = new ArrayList<>();
        userList.add(new User("test@gmail.com","Name","GivenName","FamilyName",null,"female","en"));
        userList.add(new User("test2@gmail.com","Name2","GivenName2","FamilyName2",null,"female","en"));

        return userList;
    }
}
