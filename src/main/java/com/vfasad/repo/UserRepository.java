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

    List<User> getByRoleIn(String ...role);

    List<User> findAllByOrderByEmail();

    List<User> findAllByTeam(Team team);
}
