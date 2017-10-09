package com.vfasad.repo;

import com.vfasad.dto.storage.StorageAction;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface StorageActionRepository extends JpaRepository<StorageAction, Long> {
    List<StorageAction> findByItemId(Long itemId);
}
