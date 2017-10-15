package com.vfasad.repo;

import com.vfasad.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {
    Optional<User> getByEmail(String email);

    List<User> getByRole(String role);

    List<User> findAllByOrderByEmail();
}
