package com.vfasad.repo;

import com.vfasad.entity.Team;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface TeamRepository extends JpaRepository<Team, Long> {
    Optional<Team> findById(Long teamId);

    List<Team> findAllByOrderByName();
}
