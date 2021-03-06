package com.vfasad.repo;

import com.vfasad.entity.Team;
import com.vfasad.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {
    Optional<User> getByEmail(String email);

    Optional<User> getById(Long id);

    List<User> getByRoleInOrderByName(String ...role);

    default List<User> findAllByOrderByEmail() {
        return findAllByDeletedOrderByEmail(false);
    }

    default List<User> findAllByTeam(Team team) {
        return findAllByTeamAndDeleted(team, false);
    }

    List<User> findAllByDeletedOrderByEmail(boolean deleted);

    List<User> findAllByTeamAndDeleted(Team team, boolean deleted);
}
