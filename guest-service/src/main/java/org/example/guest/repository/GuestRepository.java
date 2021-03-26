package org.example.guest.repository;

import org.example.guest.entity.GuestEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 *  The repository for GuestEntity
 */
@Repository
public interface GuestRepository extends JpaRepository<GuestEntity, Integer> {
}
