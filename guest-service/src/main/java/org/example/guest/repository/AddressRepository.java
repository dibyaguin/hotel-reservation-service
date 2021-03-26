package org.example.guest.repository;

import org.example.guest.entity.AddressEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 *  The repository for AddressEntity
 */
@Repository
public interface AddressRepository extends JpaRepository<AddressEntity, Integer> {
}
