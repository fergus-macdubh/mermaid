package com.vfasad.test.service;

import com.vfasad.entity.Team;
import com.vfasad.entity.User;
import com.vfasad.exception.NotFoundException;
import com.vfasad.repo.UserRepository;
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
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.provider.OAuth2Authentication;
import org.springframework.security.oauth2.provider.OAuth2Request;

import java.util.*;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

@RunWith(MockitoJUnitRunner.class)
public class UserServiceTest {
    private static final Long USER_ID = 1L;
    private static final String EMAIL = "email";
    private static final String NAME = "name";
    private static final String GIVEN_NAME = "given_name";
    private static final String FAMILY_NAME = "family_name";
    private static final String PICTURE = "picture";
    private static final String GENDER = "gender";
    private static final String LOCALE = "locale";
    private static final int DELETED_INDEX = 2;
    private static final int TEAM_NULL_INDEX = 5;
    private static final String ROLE_ADMIN = "ROLE_ADMIN";

    @Mock
    private UserRepository userRepository;

    @Mock
    private TeamService teamService;

    @Mock
    private OAuth2Authentication authentication;

    @Mock
    private SecurityContext securityContext;

    @Mock
    private OAuth2Request oAuth2Request;

    @Rule
    public ExpectedException expectedException = ExpectedException.none();

    @InjectMocks
    private UserService userService;

    @Before
    public void setSecurityContextHolder() {
        SecurityContextHolder.setContext(securityContext);
    }

    @Test
    public void testGetCurrentUserNullAuth() {
        when(securityContext.getAuthentication()).thenReturn(null);
        User resultUser = userService.getCurrentUser();
        assertNull("User should be null", resultUser);
    }

    @Test
    public void testGetCurrentUserPositive() {
        User user = generateUser();

        when(securityContext.getAuthentication()).thenReturn(authentication);
        when(authentication.getDetails()).thenReturn(Collections.emptyMap());
        when(userRepository.getByEmail(anyString())).thenReturn(Optional.of(user));

        User resultUser = userService.getCurrentUser();
        checkUserDetails(user, resultUser, null);
    }

    @Test
    public void testGetCurrentUserStranger() {
        when(securityContext.getAuthentication()).thenReturn(authentication);
        when(authentication.getDetails()).thenReturn(Collections.emptyMap());
        when(userRepository.getByEmail(anyString())).thenReturn(Optional.empty());

        User resultUser = userService.getCurrentUser();
        assertEquals("Incorrect name", "Незнакомец", resultUser.getName());
        assertEquals("Incorrect given name", "Незнакомец", resultUser.getGivenName());
        assertEquals("Incorrect family name", "", resultUser.getFamilyName());
        assertEquals("Incorrect picture", "", resultUser.getPicture());
        assertEquals("Incorrect gender", "", resultUser.getGender());
        assertEquals("Incorrect locale", "", resultUser.getLocale());
    }

    @Test
    public void testUpdateUserByDetails() {
        Map<String, String> details = generateDetails();
        User user = generateUser();
        User returnUser = generateUser();

        when(userRepository.getByEmail(anyString())).thenReturn(Optional.of(user));
        when(userRepository.save(user)).thenReturn(returnUser);
        User resultUser = userService.updateUser(details);
        checkUserDetails(returnUser, resultUser, null);
        //check that user details were updated in method
        checkUserDetails(null, user, details);
    }

    private void checkUserDetails(User user, User resultUser, Map<String, String> details){
        assertEquals("Incorrect email",
                user == null ? details.get(EMAIL) : user.getEmail(),
                resultUser.getEmail());
        assertEquals("Incorrect name",
                user == null ? details.get(NAME) : user.getName(),
                resultUser.getName());
        assertEquals("Incorrect given name",
                user == null ? details.get(GIVEN_NAME) : user.getGivenName(),
                resultUser.getGivenName());
        assertEquals("Incorrect family name",
                user == null ? details.get(FAMILY_NAME) : user.getFamilyName(),
                resultUser.getFamilyName());
        assertEquals("Incorrect picture",
                user == null ? details.get(PICTURE) : user.getPicture(),
                resultUser.getPicture());
        assertEquals("Incorrect gender",
                user == null ? details.get(GENDER) : user.getGender(),
                resultUser.getGender());
        assertEquals("Incorrect locale",
                user == null ? details.get(LOCALE) : user.getLocale(),
                resultUser.getLocale());
        assertTrue("Incorrect deleted flag",
                user == null ? !resultUser.isDeleted() : (user.isDeleted() == resultUser.isDeleted()));
    }

    @Test
    public void testUpdateUserByUser() {
        User user = generateUser();
        userService.updateUser(user);
        verify(userRepository, times(1)).save(user);
    }

    @Test
    public void testGetAdmins() {
        List<User> userList = generateUserList();
        when(userRepository.getByRoleInOrderByName("ROLE_ADMIN")).thenReturn(userList);
        List<User> resultUserList = userService.getAdmins();

        assertEquals("getAdmins returned incorrect userList", userList, resultUserList);
    }

    @Test
    public void testGetManagers() {
        List<User> userList = generateUserList();
        when(userRepository.getByRoleInOrderByName("ROLE_SALES", "ROLE_OPERATOR", "ROLE_ADMIN")).thenReturn(userList);
        List<User> resultUserList = userService.getManagers();

        userList.remove(userList.get(DELETED_INDEX));
        boolean containsDeleted = resultUserList.stream().anyMatch(user -> user.isDeleted());
        assertEquals("getManagers returned incorrect userList", userList, resultUserList);
        assertFalse("Managers list shouldn't contain deleted users", containsDeleted);
    }

    @Test
    public void testFindAll() {
        List<User> userList = generateUserList();
        when(userRepository.findAllByOrderByEmail()).thenReturn(userList);
        List<User> resultUserList = userService.findAll();

        assertEquals("findAll returned incorrect userList", userList, resultUserList);
    }

    @Test
    public void testUpdateUserRole() {
        User user = generateUser();
        user.setRole("ROLE_OLD");
        when(userRepository.getById(anyLong())).thenReturn(Optional.of(user));

        userService.updateUserRole(USER_ID, ROLE_ADMIN);
        assertEquals("Incorrect user role", ROLE_ADMIN, user.getRole());
        verify(userRepository, times(1)).save(user);
    }

    @Test
    public void testGetUser() {
        User user = generateUser();
        when(userRepository.getById(anyLong())).thenReturn(Optional.of(user));

        User resultUser = userService.getUser(USER_ID);
        checkUserDetails(user, resultUser, null);
    }

    @Test
    public void testGetUserNotFoundException() {
        expectedException.expect(NotFoundException.class);
        expectedException.expectMessage("User with provided id is not found.");
        when(userRepository.getById(anyLong())).thenReturn(Optional.empty());
        userService.getUser(USER_ID);
    }

    @Test
    public void testIsRoleChangedFalse() {
        boolean result = userService.isRoleChanged(authentication, "");
        assertFalse("Role shouldn't be changed", result);

        when(authentication.getAuthorities())
                .thenReturn(Collections.singletonList(new SimpleGrantedAuthority(ROLE_ADMIN)));
        result = userService.isRoleChanged(authentication, ROLE_ADMIN);
        assertFalse("Role shouldn't be changed", result);
    }

    @Test
    public void testIsRoleChangedTrue() {
        when(authentication.getAuthorities())
                .thenReturn(Collections.singletonList(new SimpleGrantedAuthority(ROLE_ADMIN)));

        boolean result = userService.isRoleChanged(authentication, "ROLE_NEW");
        assertTrue("Role should be changed", result);
    }

    @Test
    public void testUpdateAuthorities() {
        User user = generateUser();
        when(authentication.getDetails()).thenReturn(user);
        when(authentication.getCredentials()).thenReturn(user);
        when(authentication.getPrincipal()).thenReturn(user);
        when(authentication.getOAuth2Request()).thenReturn(oAuth2Request);

        userService.updateAuthorities(authentication, "ROLE_NEW");
        assertEquals(authentication.getDetails(), user);
        verify(securityContext, times(1)).setAuthentication(any(OAuth2Authentication.class));
    }

    @Test
    public void testUpdateUserTeam() {
        User user = generateUser();
        Team team = generateTeam("name", "color");
        when(userRepository.getById(anyLong())).thenReturn(Optional.of(user));
        when(teamService.getTeam(anyLong())).thenReturn(team);
        userService.updateUserTeam(USER_ID, 1L);
        assertEquals("Incorrect user team", team, user.getTeam());
        verify(userRepository, times(1)).save(user);
    }

    @Test
    public void testGetUsersByTeamId() {
        List<User> userList = generateUserList();
        boolean isTrue;
        when(userRepository.findAll()).thenReturn(userList);

        Map<String, List<User>> resultUsersByTeamId = userService.getUsersByTeamId();
        assertEquals("Incorrect size of elements in collection", 2, resultUsersByTeamId.size());

        List<User> team1UserList = resultUsersByTeamId.get("1");
        isTrue = team1UserList.stream().allMatch(user -> user.getTeam().getId().equals(1L) && !user.isDeleted());
        assertTrue("Incorrect list of users of 1st team", isTrue);

        List<User> team2UserList = resultUsersByTeamId.get("2");
        isTrue = team2UserList.stream().allMatch(user -> user.getTeam().getId().equals(2L) && !user.isDeleted());
        assertTrue("Incorrect list of users of 2nd team", isTrue);
    }

    private Map<String, String> generateDetails() {
        Map<String, String> details = new HashMap<>();
        details.put(EMAIL, EMAIL);
        details.put(NAME, NAME);
        details.put(GIVEN_NAME, GIVEN_NAME);
        details.put(FAMILY_NAME, FAMILY_NAME);
        details.put(PICTURE, PICTURE);
        details.put(GENDER, GENDER);
        details.put(LOCALE, LOCALE);

        return details;
    }

    private User generateUser() {
        User user = new User(EMAIL,"name1","givenName1","familyName1",null,"female","en");
        user.setDeleted(false);
        return user;
    }

    private List<User> generateUserList() {
        List<User> userList = new ArrayList<>();
        userList.add(new User("test@gmail.com","Name","GivenName","FamilyName",null,"female","en"));
        userList.add(new User("test2@gmail.com","Name2","GivenName2","FamilyName2",null,"female","ru"));
        userList.add(new User("test3@gmail.com","Name3","GivenName3","FamilyName3",null,"male","en"));
        userList.add(new User("test4@gmail.com","Name4","GivenName4","FamilyName4",null,"male","uk"));
        userList.add(new User("test5@gmail.com","Name5","GivenName5","FamilyName5",null,"female","en"));
        userList.add(new User("test6@gmail.com","Name6","GivenName6","FamilyName6",null,"male","en"));
        Team team1 = generateTeam("name1", "color1");
        team1.setId(1L);
        Team team2 = generateTeam("name2", "color2");
        team2.setId(2L);
        userList.forEach(user -> user.setTeam(userList.indexOf(user)%2 == 0 ? team1 : team2));
        userList.get(DELETED_INDEX).setDeleted(true);
        userList.get(TEAM_NULL_INDEX).setTeam(null);
        return userList;
    }

    private Team generateTeam(String name, String color) {
        Team team = new Team();
        team.setName(name);
        team.setColor(color);

        return team;
    }
}
